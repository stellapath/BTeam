package com.hanul.project.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hanul.project.R;
import com.hanul.project.ui.home.adapter.HomeAdapter;
import com.hanul.project.ui.home.model.Item;
import com.hanul.project.ui.home.model.Weather;
import com.hanul.project.ui.home.task.WeatherTask;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<>();

        /**
         * AsyncTask를 통해 날씨를 파싱하여 가져온 뒤
         * RecyclerView에 추가
         */
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

        /**
         * HomeAdapter에 전달할 클릭 리스너
         */
        final Weather finalWeather = weather;
        homeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Toast.makeText(getActivity(), position + "번째 클릭", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);

                }
            }
        });

        return root;
    }

}