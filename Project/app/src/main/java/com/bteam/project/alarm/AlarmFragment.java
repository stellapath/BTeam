package com.bteam.project.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.alarm.dialog.IntervalPickerDialog;
import com.bteam.project.alarm.dialog.RepeatPickerDialog;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmFragment extends Fragment {

    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;

    private AlarmSharedPreferencesHelper helper;
    private LinearLayout view1, view2, view3, view4, view5, view6, view7, view8;
    private RecyclerView recyclerView;
    private Switch aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, sound, vibration, memo, memoContent,
                     destination, arrivalTime, arrivalLeft;
    private CountDownTimer timer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        helper = new AlarmSharedPreferencesHelper(getActivity());

        initView(root);
        initAlarm();

        // 기상 시간 클릭 이벤트
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = helper.getWakeUpHour();
                int minute = helper.getWakeUpMinute();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), wakeUpListener, hour, minute, false);
                dialog.show();
            }
        });

        // 반복 간격 설정
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntervalPickerDialog dialog = new IntervalPickerDialog();
                dialog.setValueChangeListener(intervalListener);
                dialog.setNumberPickerValue(helper.getInterval());
                dialog.show(getActivity().getSupportFragmentManager(), "intervalPicker");
            }
        });

        // 반복 횟수 설정
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatPickerDialog dialog = new RepeatPickerDialog();
                dialog.setValueChangeListener(repeatListener);
                dialog.setNumberPickerValue(helper.getRepeat());
                dialog.show(getActivity().getSupportFragmentManager(), "repeatPicker");
            }
        });

        // 알람음 설정
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, Common.REQUEST_RINGTONE);
            }
        });

        // 진동 설정
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 메모 설정
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 목적지 설정
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 출근 시간 설정
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = helper.getArrivalHour();
                int minute = helper.getArrivalMinute();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), arrivalListener, hour, minute, false);
                dialog.show();
            }
        });

        return root;
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
        wakeUpLeft = v.findViewById(R.id.alarm_wakeUpLeft);
        interval = v.findViewById(R.id.alarm_interval);
        repeat = v.findViewById(R.id.alarm_repeat);
        vibration = v.findViewById(R.id.alarm_vibration);
        memo = v.findViewById(R.id.alarm_memo);
        memoContent = v.findViewById(R.id.alarm_memoContent);
        destination = v.findViewById(R.id.alarm_destination);
        arrivalTime = v.findViewById(R.id.alarm_arrivalTime);
        arrivalLeft = v.findViewById(R.id.alarm_arrivalLeft);
    }

    // 첫 알람 화면 불러오기 (저장된 정보 가져오기)
    private void initAlarm() {
        // 기상 시간 가져오기
        long millis = helper.getWakeUpTime();

        wakeUpTime.setText(helper.timeToString(millis));
        startTimer(millis, wakeUpLeft);

        // 알람 간격 가져오기
        int i = helper.getInterval();
        interval.setText(Integer.toString(i));

        // 반복 횟수 가져오기
        int r = helper.getRepeat();
        repeat.setText(Integer.toString(r));

        // 도착 시간 가져오기
        millis = helper.getArrivalTime();

        arrivalTime.setText(helper.timeToString(millis));
        startTimer(millis, arrivalLeft);
    }

    // 타이머 시작
    private void startTimer(long millis, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("kk시간 mm분 ss초 남았습니다.");
        timer = new CountDownTimer(millis - System.currentTimeMillis(), 1000) {

            @Override
            public void onTick(long l) {
                calendar.setTimeInMillis(l);
                Date date = calendar.getTime();
                textView.setText(format.format(date));
            }

            @Override
            public void onFinish() {
                textView.setText("알람 시간이 지났습니다.");
            }

        }.start();
    }

    // wakeUpListener
    private TimePickerDialog.OnTimeSetListener wakeUpListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long millis = helper.timeToMillis(i, i1);
            helper.setWakeUpTime(millis);

            wakeUpTime.setText(helper.timeToString(millis));
            startTimer(millis, wakeUpLeft);
        }
    };

    // wakeUpListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long millis = helper.timeToMillis(i, i1);
            helper.setArrivalTime(millis);

            arrivalTime.setText(helper.timeToString(millis));
            startTimer(millis, arrivalLeft);
        }
    };

    // IntervalPickerListener
    private NumberPicker.OnValueChangeListener intervalListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            Toast.makeText(getActivity(), "선택된 숫자 : " + i, Toast.LENGTH_SHORT).show();
        }
    };

    // RepeatPickerListener
    private NumberPicker.OnValueChangeListener repeatListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            Toast.makeText(getActivity(), "선택된 숫자 : " + i, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            helper.setRingtone(uri);
        }
    }
}