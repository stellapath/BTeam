package com.bteam.project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.TimeCalculator;
import com.bteam.project.home.adapter.TrafficAdapter;
import com.bteam.project.home.model.Traffic;
import com.bteam.project.network.VolleySingleton;
import com.bteam.project.util.Common;
import com.bteam.project.R;
import com.bteam.project.home.model.Weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 홈 프래그먼트
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private SharedPreferences preferences;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private TimeCalculator timeCalc;

    private SwipeRefreshLayout refreshLayout;
    private CardView cardView, alarmCardView;
    private ImageView background, icon;
    private TextView temperature, current, city;
    private RecyclerView traffic_recyclerView;
    private TextView alarm_txt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        preferences = getActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);
        sharPrefHelper = new AlarmSharedPreferencesHelper(getActivity());
        timeCalc = new TimeCalculator();

        initView(root);

        getWeatherList();

        // 날씨를 클릭하면 날씨 상세 액티비티로 이동
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivityForResult(intent, Common.REQUEST_WEATHER);
            }
        });

        // 교통 정보 불러오기
        getTraffic();

        // 알람 설정 됐는지 확인
        if (sharPrefHelper.isTurnedOn()) {
            alarm_txt.setText(timeCalc.toString(sharPrefHelper.getWakeUpMillis(), 0) + "에 알람이 설정되어 있습니다.");
        } else {
            alarm_txt.setText("알람이 설정되어 있지 않습니다.");
        }

        // 알람 클릭시 알람 탭으로 이동

        alarmCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.navigation_alarm);
            }
        });

        // 아래 스와이프 했을 때 새로고침 이벤트
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 날씨 새로 불러오기
                getWeatherList();
                // 교통정보 불러오기
                getTraffic();
                refreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void initView(View root) {
        refreshLayout = root.findViewById(R.id.home_refreshLayout);
        cardView = root.findViewById(R.id.home_weatherView);
        background = root.findViewById(R.id.image_weather_background);
        icon = root.findViewById(R.id.image_weather_icon);
        temperature = root.findViewById(R.id.text_weather_temperature);
        current = root.findViewById(R.id.text_weather_current);
        city = root.findViewById(R.id.text_weather_city);
        traffic_recyclerView = root.findViewById(R.id.traffic_recyclerView);
        alarm_txt = root.findViewById(R.id.home_alarm_txt);
        alarmCardView = root.findViewById(R.id.cardview_alarm);
    }

    // 날씨 불러오기
    private void getWeatherList() {
        final List<Weather> list = new ArrayList<>();
        String zone = preferences.getString("zone", "2914065000");
        String url = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=" + zone;
        StringRequest request = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(response));
                    Document document = builder.parse(is);

                    NodeList itemNodeList = document.getElementsByTagName("item");
                    Node itemNode = itemNodeList.item(0);
                    Element itemElement = (Element) itemNode;

                    // 지역 City
                    NodeList cityNodeList = itemElement.getElementsByTagName("category");
                    String city = cityNodeList.item(0).getChildNodes().item(0).getNodeValue();

                    NodeList dataNodeList = document.getElementsByTagName("data");
                    for (int i = 0; i < dataNodeList.getLength(); i++) {
                        Node node = dataNodeList.item(i);
                        Element element = (Element) node;

                        // 시간 hour
                        NodeList hourNodeList = element.getElementsByTagName("hour");
                        String hour = hourNodeList.item(0).getChildNodes().item(0).getNodeValue();

                        // 오늘 내일 모레 day
                        NodeList dayNodeList = element.getElementsByTagName("day");
                        String day = dayNodeList.item(0).getChildNodes().item(0).getNodeValue();

                        // 온도 Temperature
                        NodeList tempNodeList = element.getElementsByTagName("temp");
                        String temp = tempNodeList.item(0).getChildNodes().item(0).getNodeValue() + "˚";

                        // 상태 current
                        NodeList currNodeList = element.getElementsByTagName("wfKor");
                        String current = currNodeList.item(0).getChildNodes().item(0).getNodeValue();

                        list.add(new Weather(hour, day, temp, current, city));
                    }
                    showWeather(list.get(0));
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "날씨 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    // 날씨 객체의 정보를 화면에 표시
    private void showWeather(Weather weather) {
        background.setImageResource(weather.getBackground());
        icon.setImageResource(weather.getIcon());
        temperature.setText(weather.getTemperature());
        current.setText(weather.getCurrent());
        city.setText(weather.getCity());
    }

    // 교통정보 가져오기 <<돌발 통제정보 게시판>>
    private void getTraffic() {
        final List<Traffic> list = new ArrayList<>();
        String url1 = "http://www.gjtic.go.kr/utis/inc/list";
        StringRequest request = new StringRequest(Request.Method.GET, url1,
            new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int begin = response.indexOf("<tbody>");
                    int end = response.indexOf("</tbody>");
                    String cut = response.substring(begin, end + 8);

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputSource is = new InputSource(new StringReader(cut));
                    Document document = builder.parse(is);

                    // tbody
                    NodeList nodeList_tbody = document.getElementsByTagName("tbody");
                    Node node_tbody = nodeList_tbody.item(0);
                    Element element_tbody = (Element) node_tbody;

                    // tr
                    NodeList nodeList_tr = element_tbody.getElementsByTagName("tr");
                    for (int i = 0; i < nodeList_tr.getLength(); i++) {
                        Node node_tr = nodeList_tr.item(i);
                        Element element_tr = (Element) node_tr;

                        // td
                        NodeList nodeList_td = element_tr.getElementsByTagName("td");
                        Traffic traffic = new Traffic();
                        traffic.setNum(nodeList_td.item(0).getChildNodes().item(0).getNodeValue());
                        traffic.setTypeOfAccident(nodeList_td.item(1).getChildNodes().item(0).getNodeValue());
                        traffic.setStreetName(nodeList_td.item(2).getChildNodes().item(0).getNodeValue());
                        traffic.setDetail(nodeList_td.item(3).getChildNodes().item(0).getNodeValue());
                        traffic.setTime(nodeList_td.item(4).getChildNodes().item(0).getNodeValue());
                        list.add(traffic);
                    }
                    showTraffic(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "교통 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    // 교통 정보 표시하기
    private void showTraffic(List<Traffic> list) {
        traffic_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        traffic_recyclerView.setAdapter(new TrafficAdapter(list));
    }
}