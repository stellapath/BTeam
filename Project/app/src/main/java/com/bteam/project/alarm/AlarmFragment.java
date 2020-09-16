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
import android.util.Log;
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

    private static final String TAG = "AlarmFragment";

    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;

    private AlarmSharedPreferencesHelper helper;
    private LinearLayout wakeUpView, intervalView, repeatView, ringtoneView, vibrationView, memoView, locationView, arrivalView;
    private RecyclerView recyclerView;
    private Switch aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, ringtone, vibration, memo, memoContent,
                     destination, arrivalTime, arrivalLeft;
    private CountDownTimer wakeUpTimer, arrivalTimer;

    private boolean isVibrate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        helper = new AlarmSharedPreferencesHelper(getActivity());

        initView(root);
        initAlarm();

        // 기상 시간 클릭 이벤트
        wakeUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = helper.getWakeUpHour();
                int minute = helper.getWakeUpMinute();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), wakeUpListener, hour, minute, false);
                dialog.show();
            }
        });

        // 반복 간격 클릭 이벤트
        intervalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntervalPickerDialog dialog = new IntervalPickerDialog();
                dialog.setValueChangeListener(intervalListener);
                dialog.setNumberPickerValue(helper.getInterval());
                dialog.show(getActivity().getSupportFragmentManager(), "intervalPicker");
            }
        });

        // 반복 횟수 클릭 이벤트
        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatPickerDialog dialog = new RepeatPickerDialog();
                dialog.setValueChangeListener(repeatListener);
                dialog.setNumberPickerValue(helper.getRepeat());
                dialog.show(getActivity().getSupportFragmentManager(), "repeatPicker");
            }
        });

        // 알람음 클릭 이벤트
        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, Common.REQUEST_RINGTONE);
            }
        });

        // 진동 클릭 이벤트
        vibrationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 토글
                if (isVibrate) {
                    vibration.setText("진동이 꺼졌습니다.");
                    isVibrate = false;
                    helper.setIsVibrate(false);
                } else {
                    vibration.setText("진동이 켜졌습니다.");
                    isVibrate = true;
                    helper.setIsVibrate(true);
                }
            }
        });

        // 메모 설정
        memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 목적지 설정
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 출근 시간 설정
        arrivalView.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        initAlarm();
    }

    private void initView(View v) {
        wakeUpView = v.findViewById(R.id.alarm_wakeUpView);
        intervalView = v.findViewById(R.id.alarm_intervalView);
        repeatView = v.findViewById(R.id.alarm_repeatView);
        ringtoneView = v.findViewById(R.id.alarm_ringtoneView);
        vibrationView = v.findViewById(R.id.alarm_vibrationView);
        memoView = v.findViewById(R.id.alarm_memoView);
        locationView = v.findViewById(R.id.alarm_locationView);
        arrivalView = v.findViewById(R.id.alarm_arrivalView);
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
        wakeUpTime.setText(helper.millisToHour(helper.getWakeUpTime()));
        startWakeUpTimer(helper.getWakeUpTime(), wakeUpLeft);

        // 알람 간격 가져오기
        interval.setText( Integer.toString(helper.getInterval()) );

        // 반복 횟수 가져오기
        repeat.setText( Integer.toString(helper.getRepeat()) );

        // 벨소리 이름 가져오기

        // 진동 여부 가져오기
        isVibrate = helper.getIsVibrate();
        if (isVibrate) {
            vibration.setText("진동이 켜졌습니다.");
        } else {
            vibration.setText("진동이 꺼졌습니다.");
        }

        // 도착 시간 가져오기
        arrivalTime.setText(helper.millisToHour(helper.getArrivalTime()));
        startArrivalTimer(helper.getArrivalTime(), arrivalLeft);
    }

    // 기상 타이머 시작
    private void startWakeUpTimer(long millis, final TextView textView) {
        if (wakeUpTimer != null) wakeUpTimer.cancel();
        wakeUpTimer = new CountDownTimer(millis - System.currentTimeMillis(), 1000) {

            @Override
            public void onTick(long l) {
                wakeUpLeft.setText(helper.millisToString(l) + " 남았습니다.");
            }

            @Override
            public void onFinish() {
                textView.setText("알람 시간이 지났습니다.");
            }

        }.start();
    }

    // 도착 타이머 시작
    private void startArrivalTimer(long millis, final TextView textView) {
        if (arrivalTimer != null) arrivalTimer.cancel();
        arrivalTimer = new CountDownTimer(millis - System.currentTimeMillis(), 1000) {

            @Override
            public void onTick(long l) {
                arrivalLeft.setText(helper.millisToString(l) + " 남았습니다.");
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

            String message = helper.millisToMonth(helper.getWakeUpTime())
                           + "로 기상시간이 설정되었습니다.";
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            wakeUpTime.setText(helper.millisToHour(millis));
            startWakeUpTimer(millis, wakeUpLeft);
        }
    };

    // arrivalListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long millis = helper.timeToMillis(i, i1);
            helper.setArrivalTime(millis);

            String message = helper.millisToMonth(helper.getArrivalTime())
                           + "로 도착시간이 설정되었습니다.";
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

            arrivalTime.setText(helper.millisToHour(millis));
            startArrivalTimer(millis, arrivalLeft);
        }
    };

    // IntervalPickerListener
    private NumberPicker.OnValueChangeListener intervalListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            helper.setInterval(i);
            String message = "알람 간격이 " + i + "분으로 설정되었습니다.";
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            interval.setText(Integer.toString(i));
        }
    };

    // RepeatPickerListener
    private NumberPicker.OnValueChangeListener repeatListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            helper.setRepeat(i);
            String message = "알람이 " + i + "번 반복됩니다.";
            Toast.makeText(getActivity(), "선택된 숫자 : " + i, Toast.LENGTH_SHORT).show();
            repeat.setText(Integer.toString(i));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            String ringToneName = RingtoneManager.getRingtone(getActivity(), uri).getTitle(getActivity());
            ringtone.setText(ringToneName);
            helper.setRingtone(uri);
        }
    }
}