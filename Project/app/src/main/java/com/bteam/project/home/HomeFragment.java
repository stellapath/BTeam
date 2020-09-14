package com.bteam.project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.home.model.Weather;
import com.bteam.project.home.task.WeatherTask;

import java.util.ArrayList;

/**
 * 홈 프래그먼트
 */
public class HomeFragment extends Fragment {

    private SharedPreferences preferences;

    private SwipeRefreshLayout refreshLayout;
    private CardView cardView;
    private ImageView background, icon;
    private TextView temperature, current, city;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        preferences = getActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);

        refreshLayout = root.findViewById(R.id.home_refreshLayout);
        cardView = root.findViewById(R.id.home_weatherView);
        background = root.findViewById(R.id.image_weather_background);
        icon = root.findViewById(R.id.image_weather_icon);
        temperature = root.findViewById(R.id.text_weather_temperature);
        current = root.findViewById(R.id.text_weather_current);
        city = root.findViewById(R.id.text_weather_city);

        setWeather(getWeatherList().get(0));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivityForResult(intent, Common.REQUEST_WEATHER);
            }
        });

        // 아래로 스와이프 했을 때 새로고침 이벤트
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setWeather(getWeatherList().get(0));

                refreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    // 날씨 불러오기
    private ArrayList<Weather> getWeatherList() {
        String zone = preferences.getString("zone", "2914065000");
        WeatherTask weatherTask = new WeatherTask(zone);
        weatherTask.execute();
        ArrayList<Weather> list = null;
        try {
            list = weatherTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 날씨 붙이기
    private void setWeather(Weather weather) {
        background.setImageResource(weather.getBackground());
        icon.setImageResource(weather.getIcon());
        temperature.setText(weather.getTemperature());
        current.setText(weather.getCurrent());
        city.setText(weather.getCity());
    }

}