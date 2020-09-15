package com.bteam.project.alarm;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;

public class AlarmFragment extends Fragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private LinearLayout view1, view2, view3, view4, view5, view6, view7, view8;
    private RecyclerView recyclerView;
    private Switch aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, sound, vibration, memo, memoContent,
                     destination, getOutTime, getOutLeft;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        preferences = getActivity().getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        editor = preferences.edit();

        initView(root);

        onClickListener();

        return root;
    }

    private void getAlarm() {
        int hour = preferences.getInt("hour", 0);
        int minute = preferences.getInt("minute", 0);
    }

    private void initView(View v) {
        view1 = v.findViewById(R.id.alarm_view1);   // 기상
        view2 = v.findViewById(R.id.alarm_view2);   // 간격
        view3 = v.findViewById(R.id.alarm_view3);   // 반복
        view4 = v.findViewById(R.id.alarm_view4);   // 소리
        view5 = v.findViewById(R.id.alarm_view5);   // 진동
        view6 = v.findViewById(R.id.alarm_view6);   // 메모
        view7 = v.findViewById(R.id.alarm_view7);   // 장소
        view8 = v.findViewById(R.id.alarm_view8);   // 도착
        wakeUpTime = v.findViewById(R.id.alarm_wakeUpTime);
    }

    // TimePickerDialog
    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            editor.putInt("hour", i);
            editor.putInt("minute", i1);
            editor.commit();
        }
    };

    // 클릭 리스너 모음
    private void onClickListener() {
        // 기상
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), timeSetListener, 6, 00, false);
                dialog.show();
            }
        });

        // 간격
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}