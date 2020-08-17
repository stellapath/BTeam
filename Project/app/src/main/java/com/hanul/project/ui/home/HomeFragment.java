package com.hanul.project.ui.home;

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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<>();

        Weather weather1 = new Weather(R.drawable.gradation, R.drawable.ic_day, "27˚",
                "조금 흐림", "광주시 서구 농성동");
        items.add(new Item(0, weather1));

        recyclerView.setAdapter(new HomeAdapter(items));

        return root;
    }

}