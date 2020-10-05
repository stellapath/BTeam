package com.bteam.project.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.BoardListActivity;
import com.bteam.project.util.Common;
import com.bteam.project.R;
import com.bteam.project.util.MyMotionToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class BoardFragment extends Fragment {

    private static final String TAG = "BoardFragment";

    private CardView notice;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        initView(root);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BoardListActivity.class);
                intent.putExtra("category", 0);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initView(View root) {
        notice = root.findViewById(R.id.board_notice);
    }
}