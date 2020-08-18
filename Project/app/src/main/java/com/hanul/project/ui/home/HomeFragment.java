package com.hanul.project.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<>();

        WeatherTask weatherTask = new WeatherTask("2914065000");
        weatherTask.execute();
        Weather weather = null;
        try {
            weather = weatherTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        items.add(new Item(0, weather));

        recyclerView.setAdapter(new HomeAdapter(items));

        return root;
    }

}