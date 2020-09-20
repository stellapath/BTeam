package com.bteam.project.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.bteam.project.R;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class BoardFragment extends Fragment {

    private MaterialViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_board, container, false);

        viewPager = root.findViewById(R.id.board_viewPager);

        viewPager.getViewPager().setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    // case 0: return NoticeFragment.newInstance();
                    default: return NoticeFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0: return "공지사항";
                    case 1: return "a";
                    case 2: return "b";
                    case 3: return "c";
                }
                return "";
            }
        });

        viewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        viewPager.getViewPager().setOffscreenPageLimit(viewPager.getViewPager().getAdapter().getCount());
        viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());

        final View logo = root.findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.notifyHeaderChanged();
                    Toast.makeText(getActivity(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return root;
    }

}