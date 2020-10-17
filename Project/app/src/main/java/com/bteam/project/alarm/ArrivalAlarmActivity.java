package com.bteam.project.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.MyAlarmManager;
import com.bteam.project.alarm.service.MyNotificationManager;
import com.bteam.project.alarm.service.RingtonePlayer;
import com.bteam.project.util.Common;
import com.skyfishjy.library.RippleBackground;

import java.util.Calendar;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class ArrivalAlarmActivity extends AppCompatActivity implements RingtonePlayer.OnFinishListener {

    private AlarmSharedPreferencesHelper sharPrefHelper;
    private MyAlarmManager alarmManager;
    private RingtonePlayer ringtonePlayer;
    private MyNotificationManager notificationManager;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_alarm);

        sharPrefHelper = new AlarmSharedPreferencesHelper(this);
        alarmManager = new MyAlarmManager(this);
        ringtonePlayer = new RingtonePlayer(this);
        notificationManager = new MyNotificationManager(this);

        showOnLockedScreen();
        showLayoutFullScreen();
        startRippleBackground();
        setNextScheduleAlarm();

        finishButton = findViewById(R.id.arrival_alarm_finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasWindowFocus()) ringtonePlayer.stop();
        if (sharPrefHelper.isRead()) {
            Intent intent = new Intent(this, MemoActivity.class);
            startActivity(intent);
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
        View layout = findViewById(R.id.arrival_alarm_layout);
        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void setNextScheduleAlarm() {
        long arrivalMillis = sharPrefHelper.getArrivalMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(arrivalMillis);
        calendar.add(Calendar.DATE, 1);
        alarmManager.reset(calendar.getTimeInMillis(), Common.REQUEST_WAKEUP_ALARM);
        sharPrefHelper.setArrivalMillis(calendar.getTimeInMillis());
    }

    private void startRippleBackground() {
        RippleBackground rippleBackground = findViewById(R.id.arrival_alarm_ripple);
        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onPlayerFinished() {
        finish();
    }
}