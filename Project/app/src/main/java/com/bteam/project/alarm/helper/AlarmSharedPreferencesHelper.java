package com.bteam.project.alarm.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 알람에 관한 설정들을 총괄하는 클래스
 * 데이터 읽기, 쓰기 등
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

    private final String WAKEUP_TIME = "wakeUpTime";
    private final String WAKEUP_HOUR = "wakeUpHour";
    private final String WAKEUP_MINUTE = "wakeUpMinute";
    private final String INTERVAL = "interval";
    private final String REPEAT = "repeat";
    private final String DURATION = "duration";
    private final String RINGTONE = "ringtone";
    private final String ARRIVAL_TIME = "arrivalTime";
    private final String ARRIVAL_HOUR = "arrivalHour";
    private final String ARRIVAL_MINUTE = "arrivalMinute";

    private final int DEFAULT_WAKEUP_TIME = 0;
    private final int DEFAULT_WAKEUP_HOUR = 6;
    private final int DEFAULT_WAKEUP_MINUTE = 0;
    private final int DEFAULT_INTERVAL = 10;
    private final int DEFAULT_REPEAT = 5;
    private final int DEFAULT_DURATION = 60;
    private final String DEFAULT_RINGTONE = "";
    private final int DEFAULT_ARRIVAL_TIME = 0;
    private final int DEFAULT_ARRIVAL_HOUR = 8;
    private final int DEFAULT_ARRIVAL_MINUTE = 0;
    public long getWakeUpTime() {
        return preferences.getLong(WAKEUP_TIME, DEFAULT_WAKEUP_TIME);
    }
    public void setWakeUpTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("kk");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        int hour = Integer.parseInt(hourFormat.format(date));
        int minute = Integer.parseInt(minuteFormat.format(date));

        editor.putLong(WAKEUP_TIME, time);
        editor.putInt(WAKEUP_HOUR, hour);
        editor.putInt(WAKEUP_MINUTE, minute);
        editor.apply();
    }
    public int getWakeUpHour() {
        return preferences.getInt(WAKEUP_HOUR, DEFAULT_WAKEUP_HOUR);
    }
    public int getWakeUpMinute() {
        return preferences.getInt(WAKEUP_MINUTE, DEFAULT_WAKEUP_MINUTE);
    }
    public int getInterval() {
        return preferences.getInt(INTERVAL, DEFAULT_INTERVAL);
    }
    public void setInterval(int interval) {
        editor.putInt(INTERVAL, interval).apply();
    }
    public int getRepeat() {
        return preferences.getInt(REPEAT, DEFAULT_REPEAT);
    }
    public void setRepeat(int repeat) {
        editor.putInt(REPEAT, repeat).apply();
    }
    public int getDuration() {
        return preferences.getInt(DURATION, DEFAULT_DURATION);
    }
    public void setDuration(int duration) {
        editor.putInt(DURATION, duration).apply();
    }
    public String getRingtone() {
        return preferences.getString(RINGTONE, DEFAULT_RINGTONE);
    }
    public void setRingtone(Uri uri) {
        editor.putString(RINGTONE, uri.toString()).apply();
    }

    public long getArrivalTime() {
        return preferences.getLong(ARRIVAL_TIME, DEFAULT_ARRIVAL_TIME);
    }
    public void setArrivalTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("kk");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        int hour = Integer.parseInt(hourFormat.format(date));
        int minute = Integer.parseInt(minuteFormat.format(date));

        editor.putLong(ARRIVAL_TIME, time);
        editor.putInt(ARRIVAL_HOUR, hour);
        editor.putInt(ARRIVAL_MINUTE, minute);
        editor.apply();
    }
    public int getArrivalHour() {
        return preferences.getInt(ARRIVAL_HOUR, DEFAULT_ARRIVAL_HOUR);
    }
    public int getArrivalMinute() {
        return preferences.getInt(ARRIVAL_MINUTE, DEFAULT_ARRIVAL_MINUTE);
    }

    public long timeToMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTimeInMillis();
    }

    public String timeToString(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("a hh : mm");
        return sdf.format(date);
    }
}


