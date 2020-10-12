package com.bteam.project.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.MyAlarmManager;
import com.bteam.project.alarm.helper.TimeCalculator;
import com.bteam.project.alarm.service.MyNotificationManager;
import com.bteam.project.alarm.service.RingtonePlayer;
import com.bteam.project.util.Common;
import com.skyfishjy.library.RippleBackground;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class AlarmActivity extends AppCompatActivity implements RingtonePlayer.OnFinishListener {

    private AlarmSharedPreferencesHelper sharPrefHelper;
    private MyAlarmManager alarmManager;
    private TimeCalculator timeCalc;
    private RingtonePlayer ringtonePlayer;
    private MyNotificationManager notificationManager;

    private Button finishButton;

    int numberOfAlarms;     // 반복 횟수
    int alreadyRangAlarms;  // 이미 울린 알람 수
    int intervalMinute;     // 반복 간격 (분)
    long intervalMillis;    // 반복 간격 (밀리초)
    long wakeUpMillis;      // 기상 시간 (밀리초)
    long arrivalMillis;     // 도착 시간 (밀리초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        sharPrefHelper = new AlarmSharedPreferencesHelper(this);
        alarmManager = new MyAlarmManager(this);
        timeCalc = new TimeCalculator();
        ringtonePlayer = new RingtonePlayer(this);
        notificationManager = new MyNotificationManager(this);

        notificationManager.cancel();

        getTime();
        showOnLockedScreen();
        showLayoutFullScreen();
        startRippleBackground();
        setNextScheduleAlarm();

        ringtonePlayer.start();

        finishButton = findViewById(R.id.alarm_finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtonePlayer.stop();
                finish();
            }
        });

    }

    private void getTime() {
        numberOfAlarms = sharPrefHelper.getNumberOfAlarms();           // 반복 횟수
        alreadyRangAlarms = sharPrefHelper.getAlreadyRangAlarms();     // 이미 울린 알람 수
        intervalMinute = sharPrefHelper.getInterval();                 // 반복 간격 (분)
        intervalMillis = TimeUnit.MINUTES.toMillis(intervalMinute);    // 반복 간격 (밀리초)
        wakeUpMillis = sharPrefHelper.getWakeUpMillis();               // 기상 시간 (밀리초)
        arrivalMillis = sharPrefHelper.getArrivalMillis();             // 도착 시간 (밀리초)
    }

    private void setNextScheduleAlarm() {
        if (numberOfAlarms > alreadyRangAlarms) {
            // 반복 횟수가 남아있을 경우 알람 반복
            alarmManager.reset(System.currentTimeMillis() + intervalMillis,
                    Common.REQUEST_WAKEUP_ALARM);
            sharPrefHelper.setAlreadyRangAlarms(alreadyRangAlarms + 1);
        } else if (!sharPrefHelper.isArrivalRang()) {
            // 도착 알람이 울리지 않았다면 도착알람 울리기
            alarmManager.reset(arrivalMillis, Common.REQUEST_WAKEUP_ALARM);
            sharPrefHelper.setArrivalRang(true);
        } else {
            // 출발 알람이 울리면 기상시간에서 하루를 더한 뒤 알람 재설정
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(wakeUpMillis);
            calendar.add(Calendar.DATE, 1);
            alarmManager.reset(calendar.getTimeInMillis(), Common.REQUEST_WAKEUP_ALARM);
            sharPrefHelper.setWakeUpMillis(calendar.getTimeInMillis());
            sharPrefHelper.setAlreadyRangAlarms(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasWindowFocus()) ringtonePlayer.stop();

        // 반복이 끝나고
        if (numberOfAlarms <= alreadyRangAlarms) {
            // 날씨 예보가 켜져있고, 기상 알람일 경우에만 날씨 알람 띄우기
            if (sharPrefHelper.isWeather() && !sharPrefHelper.isArrivalRang()) {
                Intent intent = new Intent(this, YoutubeActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    private void showOnLockedScreen() {
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void showLayoutFullScreen() {
        View layout = findViewById(R.id.alarm_layout);
        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void startRippleBackground() {
        RippleBackground rippleBackground = findViewById(R.id.alarm_ripple);
        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onPlayerFinished() {
        finish();
    }
}