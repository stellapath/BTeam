package com.bteam.project.alarm.helper;

import android.content.Context;
import android.content.SharedPreferences;

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
    private final String WEATHER = "weather";
    private final String INTERVAL = "interval";
    private final String NUMBER_OF_ALARMS = "numberOfAlarms";
    private final String ALREALDY_RANG_ALARMS = "alreadyRangAlarms";
    private final String DURATION = "duration";
    private final String VIBRATE = "vibrate";
    private final String RINGTONE = "ringtone";
    private final String MEMO = "memo";
    private final String READ = "read";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String ADDRESS = "address";
    private final String ARRIVAL_TIME = "arrivalTime";
    private final String DISTANCE = "distance";
    private final String TRANSPORTATION = "transportation";

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
        return preferences.getLong(WAKEUP_TIME, System.currentTimeMillis());
    }

    public void setWeather(boolean isWeather) {
        editor.putBoolean(WEATHER, isWeather).apply();
    }

    public boolean isWeather() {
        return preferences.getBoolean(WEATHER, true);
    }

    public void setInterval(int interval) {
        editor.putInt(INTERVAL, interval).apply();
    }

    public int getInterval() {
        return preferences.getInt(INTERVAL, 5);
    }

    public void setNumberOfAlarms(int i) {
        editor.putInt(NUMBER_OF_ALARMS, i).apply();
    }

    public int getNumberOfAlarms() {
        return preferences.getInt(NUMBER_OF_ALARMS, 0);
    }

    public void setAlreadyRangAlarms(int i) {
        editor.putInt(ALREALDY_RANG_ALARMS, i).apply();
    }

    public int getAlreadyRangAlarms() {
        return preferences.getInt(ALREALDY_RANG_ALARMS, 0);
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

    public void setMemo(String memo) {
        editor.putString(MEMO, memo).apply();
    }

    public String getMemo() {
        return preferences.getString(MEMO, "");
    }

    public void setRead(boolean isRead) {
        editor.putBoolean(READ, isRead).apply();
    }

    public boolean isRead() {
        return preferences.getBoolean(READ, false);
    }

    public void setLatitude(double latitude) {
        String lat = latitude + "";
        editor.putString(LATITUDE, lat).apply();
    }

    public double getLatitude() {
        return Double.parseDouble(preferences.getString(LATITUDE, "0"));
    }

    public void setLongitude(double longitude) {
        String lon = longitude + "";
        editor.putString(LONGITUDE, lon).apply();
    }

    public double getLongitude() {
        return Double.parseDouble(preferences.getString(LONGITUDE, "0"));
    }

    public void setAddress(String addr) {
        editor.putString(ADDRESS, addr).apply();
    }

    public String getAddress() {
        return preferences.getString(ADDRESS, "설정된 목적지가 없습니다.");
    }

    public void setArrivalMillis(long millis) {
        editor.putLong(ARRIVAL_TIME, millis).apply();
    }

    public long getArrivalMillis() {
        return preferences.getLong(ARRIVAL_TIME, System.currentTimeMillis());
    }

    public void setDistance(float distance) {
        editor.putFloat(DISTANCE, distance).apply();
    }

    public float getDistance() {
        return preferences.getFloat(DISTANCE, 0);
    }

    public void setTransportation(String transportation) {
        editor.putString(TRANSPORTATION, transportation).apply();
    }

    public String getTransportation() {
        return preferences.getString(TRANSPORTATION, "walk");
    }
}


