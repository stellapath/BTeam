package com.bteam.project.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bteam.project.R;
import com.bteam.project.board.adapter.BoardPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class BoardFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_board, container, false);

        ViewPager viewPager = root.findViewById(R.id.board_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new NoticeFragment());

        BoardPagerAdapter adapter = new BoardPagerAdapter(getActivity().getSupportFragmentManager(),
                fragments);
        viewPager.setAdapter(adapter);

        TabLayout tabs = root.findViewById(R.id.board_tabs);
        tabs.setupWithViewPager(viewPager);

        return root;
    }

}