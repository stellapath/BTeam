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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.alarm.dialog.DestinationPopupActivity;
import com.bteam.project.alarm.dialog.DurationPickerDialog;
import com.bteam.project.alarm.dialog.IntervalPickerDialog;
import com.bteam.project.alarm.dialog.MemoPopupActivity;
import com.bteam.project.alarm.dialog.RepeatPickerDialog;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.TimerManager;

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

    private Context context;
    private FragmentManager fragmentManager;
    private AlarmSharedPreferencesHelper helper;
    private TimerManager timerManager;

    private LinearLayout wakeUpView, intervalView, repeatView, durationView, ringtoneView, vibrationView, memoView, locationView, arrivalView;
    private RecyclerView recyclerView;
    private SwitchCompat aSwitch;
    private TextView wakeUpTime, wakeUpLeft, interval, repeat, duration, ringtone, vibration, memo, memoContent,
            destination, arrivalTime, arrivalLeft;
    private CountDownTimer wakeUpTimer, arrivalTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);

        context = getActivity();
        fragmentManager = getActivity().getSupportFragmentManager();
        helper = new AlarmSharedPreferencesHelper(context);
        timerManager = new TimerManager(context);

        initView(root);
        initAlarm();

        // 알람 스위치
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                helper.setTurnedOn(isChecked);
                if (isChecked) {
                    // 스위치가 켜지면
                    // 권한 체크
                    checkNotificationPolicy();
                    checkOverlayPermission();
                    // 만약 과거의 알람일 경우 현재 시간에 맞춤
                    long wakeUpTime = helper.getReplacedTime( helper.getWakeUpTime() );
                    long arrivalTime = helper.getReplacedTime( helper.getArrivalTime() );
                    helper.setWakeUpTime(wakeUpTime);
                    helper.setArrivalTime(arrivalTime);
                    // 알람 타이머 시작
                    timerManager.startWakeUpTimer(helper.getWakeUpTime());
                    timerManager.startArrivalTimer(helper.getArrivalTime());
                    showToast("알람이 켜졌습니다.");
                    helper.setAlreadyRangAlarms(0);
                } else {
                    // 스위치가 꺼지면
                    // 알람 타이머 종료
                    timerManager.cancelWakeUpTimer();
                    timerManager.cancelArrivalTimer();
                    showToast("알람이 꺼졌습니다.");
                }
                initAlarm();
            }
        });

        // 기상 시간 클릭 이벤트
        wakeUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = helper.getWakeUpHour();
                int minute = helper.getWakeUpMinute();
                TimePickerDialog dialog = new TimePickerDialog(context, wakeUpListener, hour, minute, false);
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
                dialog.show(fragmentManager, "intervalPicker");
            }
        });

        // 반복 횟수 클릭 이벤트
        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatPickerDialog dialog = new RepeatPickerDialog();
                dialog.setValueChangeListener(repeatListener);
                dialog.setNumberPickerValue(helper.getRepeat());
                dialog.show(fragmentManager, "repeatPicker");
            }
        });

        // 벨소리 재생시간 클릭 이벤트
        durationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DurationPickerDialog dialog = new DurationPickerDialog();
                dialog.setValueChangeListener(durationListener);
                dialog.setNumberPickerValue(helper.getDuration());
                dialog.show(fragmentManager, "durationPicker");
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
                if (helper.getIsVibrate()) {
                    helper.setIsVibrate(false);
                } else {
                    helper.setIsVibrate(true);
                }
                initAlarm();
            }
        });

        // 메모 클릭 이벤트
        memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MemoPopupActivity.class);
                intent.putExtra("memoContent", helper.getMemo());
                intent.putExtra("isRead", helper.getIsRead());
                startActivityForResult(intent, Common.REQUEST_MEMO);
            }
        });

        // 목적지 설정
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DestinationPopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_DESTINATION);
            }
        });

        // 출근 시간 설정
        arrivalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = helper.getArrivalHour();
                int minute = helper.getArrivalMinute();
                TimePickerDialog dialog = new TimePickerDialog(context, arrivalListener, hour, minute, false);
                dialog.show();
            }
        });

        return root;
    }

    // 뷰 찾기
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
        // 스위치가 켜졌는지 꺼졌는디
        aSwitch.setChecked( helper.isTurnedOn() );

        // 기상 시간 가져오기
        wakeUpTime.setText( helper.millisToHour(helper.getWakeUpTime()) );
        startWakeUpTimer( helper.getWakeUpTime(), wakeUpLeft );

        // 알람 간격 가져오기
        interval.setText( helper.getInterval() + "" );

        // 반복 횟수 가져오기
        repeat.setText( helper.getRepeat() + "" );

        // 벨소리 재생 시간 가져오기
        duration.setText( helper.getDuration() + "" );

        // 벨소리 이름 가져오기
        ringtone.setText( helper.getRingtoneName() );

        // 진동 여부 가져오기
        if (helper.getIsVibrate())  vibration.setText("진동이 켜져있습니다.");
        else                        vibration.setText("진동이 꺼져있습니다.");

        // 메모 가져오기
        if (!helper.getMemo().equals("")) memo.setText("메모가 있습니다.");
        else                              memo.setText("메모가 없습니다.");
        memoContent.setText( helper.getMemo() );

        // 목적지 가져오기
        if (helper.getDestination().equals("")) {
            destination.setText("없음");
        } else {
            destination.setText( helper.getDestination() );
        }

        // 도착 시간 가져오기
        arrivalTime.setText( helper.millisToHour(helper.getArrivalTime()) );
        startArrivalTimer( helper.getArrivalTime(), arrivalLeft );

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
            helper.setAlreadyRangAlarms(0);
            String msg = helper.millisToMonth(millis) + " 알람이 설정되었습니다.";
            showToast(msg);
            initAlarm();
        }
    };

    // arrivalListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long millis = helper.timeToMillis(i, i1);
            helper.setArrivalTime(millis);
            String msg = helper.millisToMonth(millis) + " 알람이 설정되었습니다.";
            showToast(msg);
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
            String ringtoneName = RingtoneManager.getRingtone(context, uri).getTitle(context);
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
            showToast("메모가 저장되었습니다.");
            initAlarm();
        }

        // 목적지 결과 받아오기
        if (requestCode == Common.REQUEST_DESTINATION && resultCode == Activity.RESULT_OK) {
            // TODO 목적지 결과 받아오기
        }

    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void checkNotificationPolicy() {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.P) && (!Settings.canDrawOverlays(context))) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                startActivityForResult(intent, Common.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }
}