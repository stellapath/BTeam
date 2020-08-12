package com.hanul.project.ui.board;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanul.project.R;
import com.hanul.project.ui.home.HomeViewModel;

public class BoardFragment extends Fragment {

    private BoardViewModel boardViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        boardViewModel =
                ViewModelProviders.of(this).get(BoardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_board, container, false);

        return root;
    }

}