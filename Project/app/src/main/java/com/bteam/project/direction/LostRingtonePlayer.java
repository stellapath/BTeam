package com.bteam.project.direction;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.service.RingtonePlayer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LostRingtonePlayer {

    public interface OnFinishListener {
        void onPlayerFinished();
    }

    private Context context;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private LostRingtonePlayer.OnFinishListener onFinishListener;

    public LostRingtonePlayer(Context context) {
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.sharPrefHelper = new AlarmSharedPreferencesHelper(context);
        this.onFinishListener = (LostRingtonePlayer.OnFinishListener) context;
    }

    public void start() {
        // 다른 음악이 재생중일 경우 끄기
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        countDownTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(1), 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                stop();
            }
        }.start();

        // 진동 울리기
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[] { 2000, 2000 }, 0),
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build());
        } else {
            vibrator.vibrate(new long[] { 2000, 2000 }, 0);
        }

        // 벨소리
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            try {
                mediaPlayer.setDataSource(context,
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        mediaPlayer.start();

    }

    public void stop() {
        if (countDownTimer != null) countDownTimer.cancel();
        if (vibrator != null) vibrator.cancel();
        if (mediaPlayer != null) mediaPlayer.stop();
        if (onFinishListener != null) onFinishListener.onPlayerFinished();
    }
}
