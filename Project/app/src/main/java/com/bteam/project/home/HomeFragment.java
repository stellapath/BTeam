package com.bteam.project.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.home.adapter.HomeAdapter;
import com.bteam.project.home.model.Item;
import com.bteam.project.home.model.Weather;
import com.bteam.project.home.task.WeatherTask;

import java.util.ArrayList;
import java.util.List;

/* 홈 프래그먼트 */
public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<>();

        // 날씨 불러오기
        WeatherTask weatherTask = new WeatherTask("2914065000");
        weatherTask.execute();
        Weather weather = null;
        try {
            weather = weatherTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        items.add(new Item(0, weather));

        HomeAdapter homeAdapter = new HomeAdapter(items);
        recyclerView.setAdapter(homeAdapter);

        // 리사이클러뷰 클릭 이벤트
        final Weather finalWeather = weather;
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Toast.makeText(getActivity(), position + "번째 클릭", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }

}