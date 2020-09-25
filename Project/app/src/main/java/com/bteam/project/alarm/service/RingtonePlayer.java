package com.bteam.project.alarm.service;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;

import java.util.concurrent.TimeUnit;

public class RingtonePlayer {

    private Context context;
    private Vibrator vibrator;
    private Ringtone ringtone;
    private CountDownTimer countDownTimer;
    private AlarmSharedPreferencesHelper sharPrefHelper;

    public RingtonePlayer(Context context) {
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.sharPrefHelper = new AlarmSharedPreferencesHelper(context);
    }

    public void start() {

        long durationMinute = sharPrefHelper.getDuration();
        long durationMillis = TimeUnit.MINUTES.toMillis(durationMinute);

        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                stop();
            }
        }.start();

        // 진동
        if (sharPrefHelper.isVibrate()) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[] { 1000, 1000 }, 0));
            } else {
                vibrator.vibrate(new long[] { 1000, 1000 }, 0);
            }
        }
        /*
        // 벨소리
        if (!sharPrefHelper.getRingtone().equals("")) {
            Uri uri = Uri.parse(sharPrefHelper.getRingtone());
            ringtone = RingtoneManager.getRingtone(context, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ringtone.setLooping(true);
            }
            ringtone.play();
        }
        */
    }

    public void stop() {

        if (countDownTimer != null) countDownTimer.cancel();
        if (vibrator != null) vibrator.cancel();

    }

}
