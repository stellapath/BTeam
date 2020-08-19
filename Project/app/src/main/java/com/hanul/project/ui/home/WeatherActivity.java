package com.hanul.project.ui.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.hanul.project.R;
import com.hanul.project.ui.home.adapter.WeatherPagerAdapter;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ViewPager viewPager = findViewById(R.id.weather_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new WeatherFragment());
        fragments.add(new WeatherSettingFragment());

        WeatherPagerAdapter adapter =
                new WeatherPagerAdapter(getSupportFragmentManager(), fragments,this);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.weather_tabs);
        tabs.setupWithViewPager(viewPager);
    }
}