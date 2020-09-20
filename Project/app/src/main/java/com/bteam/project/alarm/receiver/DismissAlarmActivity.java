package com.bteam.project.alarm.receiver;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;

import com.bteam.project.R;
import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.TimerManager;
import com.bteam.project.alarm.service.RingtonePlayer;

import java.util.concurrent.TimeUnit;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class DismissAlarmActivity extends AppCompatActivity implements RingtonePlayer.OnFinishListener {

    Button dismissButton;
    TextClock textClock;
    RingtonePlayer ringtonePlayer;
    AlarmSharedPreferencesHelper sharPrefHelper;
    int numberOfAlreadyRangAlarms;
    TimerManager timerManager;
    AlarmNotificationController notificationController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss_alarm);
        showOnLockedScreen();

        // 알림 종료
        notificationController = new AlarmNotificationController(DismissAlarmActivity.this);
        notificationController.cancel();

        ringtonePlayer = new RingtonePlayer(DismissAlarmActivity.this);
        sharPrefHelper = new AlarmSharedPreferencesHelper(DismissAlarmActivity.this);
        timerManager = new TimerManager(DismissAlarmActivity.this);

        View layout = findViewById(R.id.dismissLayout);
        
        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        dismissButton = (Button) findViewById(R.id.button_dismiss_alarm);
        textClock = (TextClock) findViewById(R.id.text_clock_dismiss);
        dismissButton.setText("알람 끄기");

        // 다음 기상 알람 설정 (반복 횟수만큼 알람 설정)
        scheduleNextWakeUpAlarm();

        // 이미 울린 알람의 수
        numberOfAlreadyRangAlarms = sharPrefHelper.getAlreadyRangAlarms() + 1;
        sharPrefHelper.setAlreadyRangAlarms(numberOfAlreadyRangAlarms);

        ringtonePlayer.start();

        // 알람 스위치가 켜진 상태로 모든 알람이 끝나면 다음날로 알람 설정.
        if (numberOfAlreadyRangAlarms >= sharPrefHelper.getRepeat()) {
            long wakeUpTimeMillis = sharPrefHelper.getWakeUpTime() + TimeUnit.DAYS.toMillis(1);
            long arrivalTimeMillis = sharPrefHelper.getArrivalTime() + TimeUnit.DAYS.toMillis(1);
            timerManager.resetWakeUpTimer(wakeUpTimeMillis);
            timerManager.resetArrivalTimer(arrivalTimeMillis);
        }

        // 종료 버튼 클릭 이벤트
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtonePlayer.stop();
                finish();
            }
        });
    }

    // 미디어 플레이어가 끝났을 때 화면이 꺼짐
    @Override
    public void onPlayerFinished() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasWindowFocus()) {
            ringtonePlayer.stop();
        }
    }

    // 잠금 화면에서 화면 띄우기
    private void showOnLockedScreen() {
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    // 다음 기상 알람 설정
    private void scheduleNextWakeUpAlarm() {
        long intervalBetweenRepeatingAlarmsMillis = TimeUnit.MINUTES.toMillis(sharPrefHelper.getInterval());
        long currentTimeMillis = System.currentTimeMillis();
        long nextScheduleWakeUpTimeMillis = currentTimeMillis + intervalBetweenRepeatingAlarmsMillis;
        timerManager.resetWakeUpTimer(nextScheduleWakeUpTimeMillis);
    }
}