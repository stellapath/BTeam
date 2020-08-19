package com.hanul.project.ui.home.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hanul.project.R;

import java.util.ArrayList;

public class WeatherPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.title_weather, R.string.title_weather_setting};
    private ArrayList<Fragment> fragments;
    private Context mContext;

    public WeatherPagerAdapter(@NonNull FragmentManager fm,
                               ArrayList<Fragment> fragments, Context context) {
        super(fm);
        this.fragments = fragments;
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
