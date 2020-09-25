package com.bteam.project.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.alarm.dialog.DurationPickerDialog;
import com.bteam.project.alarm.helper.TimeCalculator;
import com.bteam.project.util.Common;
import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.MyAlarmManager;
import com.kevalpatel.ringtonepicker.RingtonePickerDialog;
import com.kevalpatel.ringtonepicker.RingtonePickerListener;

/**
 * 알람 울리는 순서
 * 알람 설정 -> 안드로이드 내부 데이터에 설정값 저장
 * -> new Intent(Context, Receiver.class);
 * -> PendingIntent.getBroadcast(Context, RequestCode, Intent, Flag);
 * -> AlarmManager....set=한번알림 setRepeating=반복알림 (AlarmManager.RTC_WAKEUP, Millis, PendingIntent);
 * -> Receiver : 알람매니저를 통해 알람을 설정하면 리시버가 받는다. 리시버를 통해 다음 동작을 실행한다.
 */
public class AlarmFragment extends Fragment {

    private static final String TAG = "AlarmFragment";

    private FragmentManager fragmentManager;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private TimeCalculator timeCalc;
    private MyAlarmManager alarmManager;

    private LinearLayout wakeUpView, intervalView, repeatView, durationView, ringtoneView,
            vibrationView, memoView, locationView, arrivalView;
    private RecyclerView recyclerView;
    private SwitchCompat aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, duration, ringtone,
            vibration, memo, memoContent, destination, arrivalTime, arrivalLeft;
    private CountDownTimer wakeUpTimer, arrivalTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        fragmentManager = getChildFragmentManager();
        sharPrefHelper = new AlarmSharedPreferencesHelper(getActivity());
        timeCalc = new TimeCalculator();
        alarmManager = new MyAlarmManager(getActivity());

        initView(root);
        initAlarm();

        // 스위치 변경 이벤트
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                sharPrefHelper.setTurnedOn( isChecked );
                if (isChecked) {
                    // 체크가 되었을 때
                    // 알람을 켠다
                    long wakeUpTimeMillis = sharPrefHelper.getWakeUpMillis();
                    alarmManager.start(wakeUpTimeMillis, Common.REQUEST_WAKEUP_ALARM);
                } else {
                    // 체크를 해제했을 때
                    // 알람을 끈다
                    alarmManager.stop(Common.REQUEST_WAKEUP_ALARM);
                }
                initAlarm();
            }
        });

        // 기상시간 클릭 이벤트
        wakeUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timeCalc.getHour( sharPrefHelper.getWakeUpMillis() );
                int minute = timeCalc.getMinute( sharPrefHelper.getWakeUpMillis() );
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), wakeUpListener, hour, minute, false);
                dialog.show();
            }
        });

        // 벨소리 재생시간 클릭 이벤트
        durationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DurationPickerDialog dialog = new DurationPickerDialog();
                dialog.setValueChangeListener(durationListener);
                dialog.setNumberPickerValue( sharPrefHelper.getDuration() );
                dialog.show(fragmentManager, "durationPicker");
            }
        });

        // 벨소리 선택 클릭 이벤트
        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RingtonePickerDialog.Builder ringtonePickerBuilder
                        = new RingtonePickerDialog.Builder(getActivity(), getChildFragmentManager())
                        .setTitle("알람음 선택")
                        .setCurrentRingtoneUri( Uri.parse(sharPrefHelper.getRingtone()) )
                        .displayDefaultRingtone(true)
                        .displaySilentRingtone(true)
                        .setPositiveButtonText("확인")
                        .setCancelButtonText("취소")
                        .setPlaySampleWhileSelection(true)
                        .setListener(new RingtonePickerListener() {
                            @Override
                            public void OnRingtoneSelected(@NonNull String ringtoneName, @Nullable Uri ringtoneUri) {
                                sharPrefHelper.setRingtone( ringtoneUri.toString() );
                                Log.d(TAG, "OnRingtoneSelected: " + ringtoneUri.toString());
                            }
                        });

                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_MUSIC);
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_NOTIFICATION);
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_RINGTONE);
                ringtonePickerBuilder.addRingtoneType(RingtonePickerDialog.Builder.TYPE_ALARM);

                ringtonePickerBuilder.show();
            }
        });

        // 진동 클릭 이벤트
        vibrationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharPrefHelper.isVibrate()) {
                    sharPrefHelper.setVibrate(false);
                } else {
                    sharPrefHelper.setVibrate(true);
                }
                initAlarm();
            }
        });

        return root;
    }

    private void initView(View v) {
        aSwitch = v.findViewById(R.id.alarm_switch);
        recyclerView = v.findViewById(R.id.alarm_recyclerView);

        wakeUpView = v.findViewById(R.id.alarm_wakeUpView);
        intervalView = v.findViewById(R.id.alarm_intervalView);
        repeatView = v.findViewById(R.id.alarm_repeatView);
        durationView = v.findViewById(R.id.alarm_durationView);
        ringtoneView = v.findViewById(R.id.alarm_ringtoneView);
        vibrationView = v.findViewById(R.id.alarm_vibrationView);
        memoView = v.findViewById(R.id.alarm_memoView);
        locationView = v.findViewById(R.id.alarm_locationView);
        arrivalView = v.findViewById(R.id.alarm_arrivalView);

        wakeUpTime = v.findViewById(R.id.alarm_wakeUpTime);
        wakeUpLeft = v.findViewById(R.id.alarm_wakeUpLeft);
        interval = v.findViewById(R.id.alarm_interval);
        repeat = v.findViewById(R.id.alarm_repeat);
        duration = v.findViewById(R.id.alarm_duration);
        ringtone = v.findViewById(R.id.alarm_ringtone);
        vibration = v.findViewById(R.id.alarm_vibration);
        memo = v.findViewById(R.id.alarm_memo);
        memoContent = v.findViewById(R.id.alarm_memoContent);
        destination = v.findViewById(R.id.alarm_destination);
        arrivalTime = v.findViewById(R.id.alarm_arrivalTime);
        arrivalLeft = v.findViewById(R.id.alarm_arrivalLeft);
    }

    // 알람 화면 불러오기 (저장된 정보 가져오기)
    private void initAlarm() {

        // 알람 다시 불러오기
        resetAlarm();

        // 스위치가 켜졌는지 꺼졌는지
        aSwitch.setChecked( sharPrefHelper.isTurnedOn() );

        // 기상시간 가져오기
        long wakeUpMillis = sharPrefHelper.getWakeUpMillis();
        wakeUpTime.setText( timeCalc.toString(wakeUpMillis, 0) );
        if (aSwitch.isChecked()) {
            startWakeUpTimer(wakeUpMillis, wakeUpLeft);
        } else {
            wakeUpLeft.setText("알람이 꺼져 있습니다.");
            if (wakeUpTimer != null) wakeUpTimer.cancel();
        }

        // 벨소리 재생시간 가져오기
        duration.setText( sharPrefHelper.getDuration() + "" );

        // Ringtone Name 가져오기
        if (sharPrefHelper.getRingtone().equals("")) {
            sharPrefHelper.setRingtone(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString());
        }
        Uri ringtoneUri = Uri.parse(sharPrefHelper.getRingtone());
        ringtone.setText( ringtoneUri.toString() );

        // 진동 여부 가져오기
        if ( sharPrefHelper.isVibrate() ) vibration.setText("진동이 울립니다.");
        else vibration.setText("진동이 울리지 않습니다.");

    }

    // 알람 재시작
    private void resetAlarm() {
        if ( !sharPrefHelper.isTurnedOn() ) return;
        // 기상시간 재설정 alarmManager.reset(sharPrefHelper.getWakeUpMillis(), Common.REQUEST_WAKEUP_ALARM);

        // 도착시간 재설정

        // 울리고 있는 진동 / 알람 끄기.. <-- 이건 나중에 알림창이나 화면 끄면 되도록
    }

    // 기상 타이머 시작
    private void startWakeUpTimer(long millis, final TextView textView) {
        if (wakeUpTimer != null) wakeUpTimer.cancel();
        wakeUpTimer = new CountDownTimer(millis - System.currentTimeMillis(), 1000) {

            @Override
            public void onTick(long l) {
                wakeUpLeft.setText( timeCalc.getTimerString(l) );
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
                arrivalLeft.setText( timeCalc.getTimerString(l) );
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
            long wakeUpMillis = timeCalc.getMillis(i, i1);
            sharPrefHelper.setWakeUpMillis( timeCalc.isBefore(wakeUpMillis) );
            sharPrefHelper.setTurnedOn(true);
            initAlarm();
        }
    };

    // arrivalListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {

        }
    };

    // IntervalPickerListener
    private NumberPicker.OnValueChangeListener intervalListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {

        }
    };

    // RepeatPickerListener
    private NumberPicker.OnValueChangeListener repeatListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {

        }
    };

    // DurationPickerListener
    private NumberPicker.OnValueChangeListener durationListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            sharPrefHelper.setDuration(i);
            initAlarm();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                sharPrefHelper.setRingtone(uri.toString());
                Log.d(TAG, "ringtone uri: " + uri.toString()); // TODO 무음 선택시 뭐가 들어오는지 보기
                initAlarm();
            }
        }
    }

}