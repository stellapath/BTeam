package com.project.bteam.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.bteam.R;
import com.project.bteam.home.adapter.WeatherPagerAdapter;

import java.util.ArrayList;

/**
 * 날씨 상세 액티비티
 * 뷰페이저로 연결 됨
 */
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