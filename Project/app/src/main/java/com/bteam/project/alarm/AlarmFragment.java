package com.bteam.project.alarm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.alarm.dialog.DestinationPopupActivity;
import com.bteam.project.alarm.dialog.DurationPickerDialog;
import com.bteam.project.alarm.dialog.IntervalPickerDialog;
import com.bteam.project.alarm.dialog.MemoPopupActivity;
import com.bteam.project.alarm.dialog.RepeatPickerDialog;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.model.Alarm;

public class AlarmFragment extends Fragment {

    private static final String TAG = "AlarmFragment";

    private AlarmSharedPreferencesHelper helper;

    private LinearLayout wakeUpView, intervalView, repeatView, durationView, ringtoneView, vibrationView, memoView, locationView, arrivalView;
    private RecyclerView recyclerView;
    private SwitchCompat aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, duration, ringtone, vibration, memo, memoContent,
            destination, arrivalTime, arrivalLeft;
    private CountDownTimer wakeUpTimer, arrivalTimer;

    private Alarm alarm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        helper = new AlarmSharedPreferencesHelper(getActivity());

        initView(root);
        initAlarm();

        // 알람 스위치
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                helper.setTurnedOn(isChecked);
                if (isChecked) {
                    checkNotificationPolicy();
                    checkOverlayPermission();

                }
            }
        });

        // 기상 시간 클릭 이벤트
        wakeUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = alarm.getWakeUpHour();
                int minute = alarm.getWakeUpMinute();
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
                dialog.setNumberPickerValue(alarm.getInterval());
                dialog.show(getActivity().getSupportFragmentManager(), "intervalPicker");
            }
        });

        // 반복 횟수 클릭 이벤트
        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatPickerDialog dialog = new RepeatPickerDialog();
                dialog.setValueChangeListener(repeatListener);
                dialog.setNumberPickerValue(alarm.getRepeat());
                dialog.show(getActivity().getSupportFragmentManager(), "repeatPicker");
            }
        });

        // 벨소리 재생시간 클릭 이벤트
        durationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DurationPickerDialog dialog = new DurationPickerDialog();
                dialog.setValueChangeListener(durationListener);
                dialog.setNumberPickerValue(alarm.getDuration());
                dialog.show(getActivity().getSupportFragmentManager(), "durationPicker");
            }
        });

        // 벨소리 설정 클릭 이벤트
        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람 벨소리를 선택하세요");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, Common.REQUEST_RINGTONE);
            }
        });

        // 진동 클릭 이벤트
        vibrationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 토글
                if (alarm.isVibrate()) {
                    alarm.setVibrate(false);
                    helper.setIsVibrate(false);
                } else {
                    alarm.setVibrate(true);
                    helper.setIsVibrate(true);
                }
                initAlarm();
            }
        });

        // 메모 클릭 이벤트
        memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemoPopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_MEMO);
            }
        });

        // 목적지 설정
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DestinationPopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_DESTINATION);
            }
        });

        // 출근 시간 설정
        arrivalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = alarm.getArrivalHour();
                int minute = alarm.getArrivalMinute();
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), arrivalListener, hour, minute, false);
                dialog.show();
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

    // 알람 화면 불러오기 (저장된 정보 가져오기) -- 정보가 변경되었을 때 호출해야 함.
    private void initAlarm() {
        // 알람 객체 가져오기
        alarm = helper.getAllParams();

        // 기상 시간 가져오기
        wakeUpTime.setText( helper.millisToHour(alarm.getWakeUpTime()) );
        startWakeUpTimer( alarm.getWakeUpTime(), wakeUpLeft );

        // 알람 간격 가져오기
        interval.setText( alarm.getInterval() + "" );

        // 반복 횟수 가져오기
        repeat.setText( alarm.getRepeat() + "" );

        // 벨소리 재생 시간 가져오기
        duration.setText( alarm.getDuration() + "" );

        // 벨소리 이름 가져오기
        ringtone.setText( alarm.getRingtoneName() );

        // 진동 여부 가져오기
        if (alarm.isVibrate())  vibration.setText("진동이 켜져있습니다.");
        else                    vibration.setText("진동이 꺼져있습니다.");

        // 메모 가져오기
        if (alarm.getMemo() == "") memo.setText("메모가 있습니다.");
        else                       memo.setText("메모가 없습니다.");
        memoContent.setText( alarm.getMemo() );

        // 목적지 가져오기
        if (alarm.getDestination() == "") {
            destination.setText("없음");
        } else {
            destination.setText( alarm.getDestination() );
        }

        // 도착 시간 가져오기
        arrivalTime.setText( helper.millisToHour(alarm.getArrivalTime()) );
        startArrivalTimer( alarm.getArrivalTime(), arrivalLeft );

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
            initAlarm();
        }
    };

    // arrivalListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long millis = helper.timeToMillis(i, i1);
            helper.setArrivalTime(millis);
            initAlarm();
        }
    };

    // IntervalPickerListener
    private NumberPicker.OnValueChangeListener intervalListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            helper.setInterval(i);
            initAlarm();
        }
    };

    // RepeatPickerListener
    private NumberPicker.OnValueChangeListener repeatListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            helper.setRepeat(i);
            initAlarm();
        }
    };

    // DurationPickerListener
    private NumberPicker.OnValueChangeListener durationListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            helper.setDuration(i);
            initAlarm();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 벨소리 결과 받아오기
        if (requestCode == Common.REQUEST_RINGTONE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            String ringtoneName = RingtoneManager.getRingtone(getActivity(), uri).getTitle(getActivity());
            helper.setRingtoneUri(uri.toString());
            helper.setRingtoneName(ringtoneName);
            initAlarm();
        }

        // 메모 결과 받아오기
        if (requestCode == Common.REQUEST_MEMO && resultCode == Activity.RESULT_OK) {
            String content = data.getStringExtra("memoContent");
            boolean isRead = data.getBooleanExtra("isRead", false);
            helper.setMemo(content);
            helper.setIsRead(isRead);
            Toast.makeText(getActivity(), "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }

        // 목적지 결과 받아오기
        if (requestCode == Common.REQUEST_DESTINATION && resultCode == Activity.RESULT_OK) {
            // TODO 목적지 결과 받아오기
        }

    }

    private void checkNotificationPolicy() {
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.P) && (!Settings.canDrawOverlays(getActivity()))) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, Common.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }
}