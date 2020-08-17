package com.hanul.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hanul.project.R;
import com.hanul.project.ui.home.adapter.TripsAdater;
import com.hanul.project.ui.home.model.Ads;
import com.hanul.project.ui.home.model.Item;
import com.hanul.project.ui.home.model.News;
import com.hanul.project.ui.home.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        /********
         *
         ********/

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        List<Item> items = new ArrayList<>();

        // Trip
        Trip trip1 = new Trip(R.drawable.croatia, "Croatia",
                "Summer 2020 - 20 Days");
        items.add(new Item(0, trip1));

        // Ads
        Ads ads1 = new Ads("Ad: Christmas Holiday",
                "70% OFF on christmas sale");
        items.add(new Item(1, ads1));

        // News
        News news1 = new News(
                "Bali, Indonesia",
                "You'll find beaches, volcanoes, Komodo dragons and jungles sheltering elephants," +
                        "orangutans and tigers. Basically it's paradise."
        );
        items.add(new Item(2, news1));

        // Trip
        Trip trip2 = new Trip(R.drawable.borabora, "Bora Bora", "Monsoon 2020 - 10 Days");

        // News
        News news2 = new News(
                "Kerry Ireland",
                "All the way west in Ireland is one of the country's most scenic countries." +
                        "Kerry's mountains, lakes and coasts are post-card-perfect," +
                        "and that's before you add in Killarney National Park."
        );
        items.add(new Item(2, news2));

        // Trip
        Trip trip3 = new Trip(R.drawable.bali, "Bali", "Winter 2020 - 12 Days");
        items.add(new Item(0, trip3));

        // Ads
        Ads ads2 = new Ads("Ad: Summer Holiday", "50% OFF on your first trip");
        items.add(new Item(1, ads2));

        recyclerView.setAdapter(new TripsAdater(items));

        /*****
         *
         *****/

        return root;
    }

}