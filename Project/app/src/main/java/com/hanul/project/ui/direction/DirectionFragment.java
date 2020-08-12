package com.hanul.project.ui.direction;

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

public class DirectionFragment extends Fragment {

    private DirectionViewModel directionViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        directionViewModel =
                ViewModelProviders.of(this).get(DirectionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_direction, container, false);

        return root;
    }

}