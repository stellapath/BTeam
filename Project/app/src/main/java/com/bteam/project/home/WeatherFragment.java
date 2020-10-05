package com.bteam.project.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bteam.project.R;
import com.bteam.project.home.adapter.WeatherAdapter;
import com.bteam.project.home.model.Weather;
import com.bteam.project.network.VolleySingleton;

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
 * 날씨 상세 프래그먼트
 */
public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";

    private SharedPreferences preferences;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        preferences = getActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);

        refreshLayout = root.findViewById(R.id.weather_refreshLayout);
        recyclerView = root.findViewById(R.id.weather_recyclerView);

        getWeatherList();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeatherList();
                refreshLayout.setRefreshing(false);
            }
        });

        return root;
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
                            setWeather(list);
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

    private void setWeather(List<Weather> list) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new WeatherAdapter(list));
    }

}
