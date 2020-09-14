package com.bteam.project.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bteam.project.R;
import com.bteam.project.home.adapter.WeatherAdapter;
import com.bteam.project.home.model.Weather;
import com.bteam.project.home.task.WeatherTask;

import java.util.ArrayList;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        WeatherAdapter adapter = new WeatherAdapter(getWeatherList());
        recyclerView.setAdapter(adapter);

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

}
