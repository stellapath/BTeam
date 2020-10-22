package com.bteam.project.direction;

import androidx.appcompat.app.AppCompatActivity;

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
import com.skyfishjy.library.RippleBackground;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class LostAlarmActivity extends AppCompatActivity implements LostRingtonePlayer.OnFinishListener {

    private LostRingtonePlayer ringtonePlayer;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_alarm);

        ringtonePlayer = new LostRingtonePlayer(this);

        showOnLockedScreen();
        showLayoutFullScreen();
        startRippleBackground();

        ringtonePlayer.start();

        finishButton = findViewById(R.id.lost_alarm_finishButton);
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
    }

    private void showOnLockedScreen() {
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private void showLayoutFullScreen() {
        View layout = findViewById(R.id.lost_alarm_layout);
        layout.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void startRippleBackground() {
        RippleBackground rippleBackground = findViewById(R.id.lost_alarm_ripple);
        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onPlayerFinished() {
        finish();
    }
}