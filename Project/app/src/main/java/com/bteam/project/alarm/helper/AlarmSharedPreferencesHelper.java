package com.bteam.project.alarm.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bteam.project.home.model.Weather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 알람에 관한 설정들을 총괄하는 클래스
 * 데이터 읽기, 쓰기 등
 * 밀리초 기준
 */
public class AlarmSharedPreferencesHelper {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public AlarmSharedPreferencesHelper(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    private final String TURNED_ON = "turnedOn";
    private final String WAKEUP_TIME = "wakeUpTime";
    private final String INTERVAL = "interval";

    private final String DURATION = "duration";

    private final String VIBRATE = "vibrate";
    private final String RINGTONE = "ringtone";

    public void setTurnedOn(boolean turnedOn) {
        editor.putBoolean(TURNED_ON, turnedOn).apply();
    }

    public boolean isTurnedOn() {
        return preferences.getBoolean(TURNED_ON, false);
    }

    public void setWakeUpMillis(long millis) {
        editor.putLong(WAKEUP_TIME, millis).apply();
    }

    public long getWakeUpMillis() {
        return preferences.getLong(WAKEUP_TIME, 0);
    }

    public void setInterval(int interval) {
        editor.putInt(INTERVAL, interval).apply();
    }

    public int getInterval() {
        return preferences.getInt(INTERVAL, 0);
    }



    public void setDuration(int duration) {
        editor.putInt(DURATION, duration).apply();
    }

    public int getDuration() {
        return preferences.getInt(DURATION, 1);
    }

    public void setVibrate(boolean isVibrate) {
        editor.putBoolean(VIBRATE, isVibrate).apply();
    }

    public boolean isVibrate() {
        return preferences.getBoolean(VIBRATE, false);
    }

    public void setRingtone(String ringtone) {
        editor.putString(RINGTONE, ringtone).apply();
    }

    public String getRingtone() {
        return preferences.getString(RINGTONE, "");
    }

}


