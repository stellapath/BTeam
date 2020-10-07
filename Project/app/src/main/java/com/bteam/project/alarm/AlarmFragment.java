package com.bteam.project.alarm;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteam.project.R;
import com.bteam.project.alarm.adapter.AlarmAdapter;
import com.bteam.project.alarm.dialog.DestinationPopupActivity;
import com.bteam.project.alarm.dialog.DurationPickerDialog;
import com.bteam.project.alarm.dialog.IntervalPickerDialog;
import com.bteam.project.alarm.dialog.MemoPopupActivity;
import com.bteam.project.alarm.dialog.RepeatPickerDialog;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.MyAlarmManager;
import com.bteam.project.alarm.helper.TimeCalculator;
import com.bteam.project.util.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
                    requestOverlayPermission();
                    requestLocationPermission();
                    startAlarm();
                } else {
                    // 체크를 해제했을 때
                    // 알람을 끈다
                    stopAlarm();
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
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), wakeUpListener,
                        hour, minute, false);
                dialog.show();
            }
        });

        // 반복 간격 클릭 이벤트
        intervalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntervalPickerDialog dialog = new IntervalPickerDialog();
                dialog.setValueChangeListener(intervalListener);
                dialog.setNumberPickerValue( sharPrefHelper.getInterval() );
                dialog.show(fragmentManager, "intervalPicker");
            }
        });

        // 반복 횟수 클릭 이벤트
        repeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RepeatPickerDialog dialog = new RepeatPickerDialog();
                dialog.setValueChangeListener(repeatListener);
                dialog.setNumberPickerValue( sharPrefHelper.getNumberOfAlarms() );
                dialog.show(fragmentManager, "numberOfAlarmsPicker");
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
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람 벨소리를 선택하세요.");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                        Uri.parse(sharPrefHelper.getRingtone()));
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, Common.REQUEST_RINGTONE);
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

        // 메모 클릭 이벤트
        memoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MemoPopupActivity.class);
                intent.putExtra("memoContent", sharPrefHelper.getMemo());
                intent.putExtra("isRead", sharPrefHelper.isRead());
                startActivityForResult(intent, Common.REQUEST_MEMO);
            }
        });

        // 목적지 설정 클릭 이벤트
        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DestinationPopupActivity.class);
                startActivityForResult(intent, Common.REQUEST_DESTINATION);
            }
        });

        // 도착시간 클릭 이벤트
        arrivalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timeCalc.getHour( sharPrefHelper.getArrivalMillis() );
                int minute = timeCalc.getMinute( sharPrefHelper.getArrivalMillis() );
                TimePickerDialog dialog = new TimePickerDialog(getActivity(), arrivalListener,
                        hour, minute, false);
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

    // 알람 화면 불러오기 (저장된 정보 가져오기)
    private void initAlarm() {

        // 알람 수 표시
        List<String> list = new ArrayList<>();
        list.add(timeCalc.toString(sharPrefHelper.getWakeUpMillis(), 0));
        for (int i = 1; i <= sharPrefHelper.getNumberOfAlarms(); i++) {
            long arrivalMillis = sharPrefHelper.getWakeUpMillis();
            int interval = sharPrefHelper.getInterval();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(arrivalMillis);
            calendar.add(Calendar.MINUTE, interval * i);
            list.add(timeCalc.toString(calendar.getTimeInMillis(), 0));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AlarmAdapter(list));

        // 알람 재설정
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

        // 반복 간격 가져오기
        interval.setText( sharPrefHelper.getInterval() + "" );

        // 반복 횟수 가져오기
        repeat.setText( sharPrefHelper.getNumberOfAlarms() + "" );

        // 벨소리 재생시간 가져오기
        duration.setText( sharPrefHelper.getDuration() + "" );

        // Ringtone 이름 가져오기
        if (sharPrefHelper.getRingtone().equals("")) {
            ringtone.setText("무음");
        } else {
            String ringtoneName = RingtoneManager.getRingtone(getActivity(),
                    Uri.parse(sharPrefHelper.getRingtone())).getTitle(getActivity());
            ringtone.setText(ringtoneName);
        }

        // 진동 여부 가져오기
        if ( sharPrefHelper.isVibrate() ) vibration.setText("진동이 울립니다.");
        else vibration.setText("진동이 울리지 않습니다.");

        // 메모 가져오기
        if (TextUtils.isEmpty(sharPrefHelper.getMemo())) {
            memo.setText("메모가 없습니다.");
        } else {
            memo.setText("메모가 있습니다.");
        }
        memoContent.setText(sharPrefHelper.getMemo());

        // 목적지 가져오기
        destination.setText(sharPrefHelper.getAddress());
    }

    // 알람 시작
    private void startAlarm() {
        long wakeUpTimeMillis = sharPrefHelper.getWakeUpMillis();
        long newMillis = timeCalc.isBefore(wakeUpTimeMillis);
        sharPrefHelper.setWakeUpMillis(newMillis);
        alarmManager.start(newMillis, Common.REQUEST_WAKEUP_ALARM);
        sharPrefHelper.setAlreadyRangAlarms(0);
        sharPrefHelper.setArrivalRang(false);
    }

    // 알람 종료
    private void stopAlarm() {
        alarmManager.stop(Common.REQUEST_WAKEUP_ALARM);
    }

    // 알람 재시작
    private void resetAlarm() {
        if ( !sharPrefHelper.isTurnedOn() ) return;
        alarmManager.reset(sharPrefHelper.getWakeUpMillis(), Common.REQUEST_WAKEUP_ALARM);
    }

    // 기상 타이머 시작
    private void startWakeUpTimer(long millis, final TextView textView) {
        if (wakeUpTimer != null) wakeUpTimer.cancel();
        wakeUpTimer = new CountDownTimer(millis - System.currentTimeMillis(),
                1000) {

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
        arrivalTimer = new CountDownTimer(millis - System.currentTimeMillis(),
                1000) {

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
            Toast.makeText(getActivity(), "기상시간이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }
    };

    // arrivalListener
    private TimePickerDialog.OnTimeSetListener arrivalListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            long arrivalMillis = timeCalc.getMillis(i, i1);
            if (arrivalMillis <= sharPrefHelper.getWakeUpMillis()) {
                Toast.makeText(getActivity(), "도착시간은 기상시간보다 더 늦은 시간으로 설정해야 합니다.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            sharPrefHelper.setArrivalMillis(arrivalMillis);
            sharPrefHelper.setTurnedOn(true);
            Toast.makeText(getActivity(), "도착시간이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }
    };

    // IntervalPickerListener
    private NumberPicker.OnValueChangeListener intervalListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            sharPrefHelper.setInterval(i);
            Toast.makeText(getActivity(), "설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }
    };

    // RepeatPickerListener
    private NumberPicker.OnValueChangeListener repeatListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            sharPrefHelper.setNumberOfAlarms(i);
            Toast.makeText(getActivity(), "설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }
    };

    // DurationPickerListener
    private NumberPicker.OnValueChangeListener durationListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            sharPrefHelper.setDuration(i);
            Toast.makeText(getActivity(), "설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.REQUEST_RINGTONE) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri == null) {
                sharPrefHelper.setRingtone("");
            } else {
                sharPrefHelper.setRingtone(uri.toString());
            }
            Toast.makeText(getActivity(), "설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }

        if (requestCode == Common.REQUEST_MEMO && resultCode == Activity.RESULT_OK) {
            String memoContent = data.getStringExtra("memoContent");
            boolean isRead = data.getBooleanExtra("isRead", false);
            sharPrefHelper.setMemo(memoContent);
            sharPrefHelper.setRead(isRead);
            Toast.makeText(getActivity(), "설정이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            initAlarm();
        }

        if (requestCode == Common.REQUEST_DESTINATION || resultCode == Activity.RESULT_OK) {
            initAlarm();
        }
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.P) && (!Settings.canDrawOverlays(getActivity()))) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, Common.REQUEST_OVERRAY_PERMISSION);
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 거절된 상태
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한이 거절된 상태
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        }
    }

    private void requestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Common.REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }
}