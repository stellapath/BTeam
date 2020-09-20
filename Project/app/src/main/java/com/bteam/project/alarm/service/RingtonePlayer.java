package com.bteam.project.alarm.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;

import java.io.File;
import java.io.IOException;

public class RingtonePlayer {

    public interface OnFinishListener {
        void onPlayerFinished();
    }

    private Context context;
    private AlarmSharedPreferencesHelper helper;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private CountDownTimer countDownTimer;
    private OnFinishListener onFinishListener;

    public RingtonePlayer(Context context) {
        this.context = context;
        this.helper = new AlarmSharedPreferencesHelper(context);
        this.onFinishListener = (OnFinishListener) context;
    }

    private int initialRingerMode;
    private int durationSeconds;
    private boolean isReleased = false;

    public void start() {
        setNormalRingerMode();

        durationSeconds = helper.getDuration() * 60;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

        // 알람이 끝나지 않았으면 반복 재생
        if (durationSeconds > 0) {
            mediaPlayer.setLooping(true);
        }

        // 재생 시간이 0이면 노래 길이 만큼만 재생
        if (durationSeconds == 0) {
            durationSeconds = mediaPlayer.getDuration();
        }

        try {
            mediaPlayer.setDataSource(context, getRingtone());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                // 예외 시 기본 벨소리 사용
                mediaPlayer.setDataSource(context, getDefaultRingtoneUri());
                mediaPlayer.prepare();
            } catch (IOException e1) {
                e.printStackTrace();
            }
        }

        mediaPlayer.start();
        startCountDownTimer(durationSeconds);
    }

    public void stop() {
        if (isReleased) {
            return;
        }
        stopCountDownTimer();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            isReleased = true;
        }
        setInitialRingerMode();

        if (onFinishListener != null) {
            onFinishListener.onPlayerFinished();
        }

    }

    private void setNormalRingerMode() {  // in case phone is in "Vibrate" mode
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initialRingerMode = audioManager.getRingerMode();
        try {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void setInitialRingerMode() {
        audioManager.setRingerMode(initialRingerMode);
    }

    private Uri getRingtone() {
        String filename = helper.getRingtoneName();
        if (filename.equals("")) {
            return getDefaultRingtoneUri();
        } else {
            File ringtone = new File(context.getFilesDir(), filename);
            return Uri.fromFile(ringtone);
        }

    }

    public Uri getDefaultRingtoneUri() {
        Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); // content://settings/system/alarm_alert
        if (defaultRingtoneUri == null) {  // it could happen if user has never set alarm on a new device
            defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        return defaultRingtoneUri;
    }

    private void startCountDownTimer(int durationSec) {
        countDownTimer = new CountDownTimer(durationSec*1000, durationSec*1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
        countDownTimer.start();
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
