package com.bteam.project.alarm.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bteam.project.alarm.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 알람에 관한 설정들을 총괄하는 클래스
 * 데이터 읽기, 쓰기 등
 */
public class AlarmSharedPreferencesHelper {

    private static final String TAG = "AlarmSharedPreferencesH";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public AlarmSharedPreferencesHelper(Context context) {
        context = context;
        preferences = context.getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    private final String TURNED_ON = "turnedOn";
    private final String ALREADY_RANG_ALARMS = "AlreadyRangAlarms";
    private final String WAKEUP_TIME = "wakeUpTime";
    private final String WAKEUP_HOUR = "wakeUpHour";
    private final String WAKEUP_MINUTE = "wakeUpMinute";
    private final String INTERVAL = "interval";
    private final String REPEAT = "repeat";
    private final String DURATION = "duration";
    private final String RINGTONE_NAME = "ringtoneName";
    private final String RINGTONE_URI = "ringtoneUri";
    private final String ISVIBRATE = "isVibrate";
    private final String ISRING = "isRing";
    private final String ISREAD = "isRead";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String MEMO = "memo";
    private final String DESTINATION = "destination";
    private final String ARRIVAL_TIME = "arrivalTime";
    private final String ARRIVAL_HOUR = "arrivalHour";
    private final String ARRIVAL_MINUTE = "arrivalMinute";

    private final boolean DEFAULT_TURNED_ON = false;
    private final int DEFAULT_ALREADY_RANG_ALARMS = 0;
    private final int DEFAULT_WAKEUP_TIME = 0;
    private final int DEFAULT_WAKEUP_HOUR = 6;
    private final int DEFAULT_WAKEUP_MINUTE = 0;
    private final int DEFAULT_INTERVAL = 10;
    private final int DEFAULT_REPEAT = 5;
    private final int DEFAULT_DURATION = 1;
    private final String DEFAULT_RINGTONE_NAME = "default";
    private final String DEFAULT_RINGTONE_URI = "";
    private final boolean DEFAULT_ISVIBRATE = false;
    private final boolean DEFAULT_ISRING = false;
    private final boolean DEFAULT_ISREAD = false;
    private final float DEFAULT_LATITUDE = 0;
    private final float DEFAULT_LONGITUDE = 0;
    private final String DEFAULT_MEMO = "";
    private final String DEFAULT_DESTINATION = "";
    private final int DEFAULT_ARRIVAL_TIME = 0;
    private final int DEFAULT_ARRIVAL_HOUR = 8;
    private final int DEFAULT_ARRIVAL_MINUTE = 0;

    public Alarm getAllParams() {
        Alarm alarm = new Alarm();
        alarm.setTurnedOn( isTurnedOn() );
        alarm.setAlreadyRangAlarms( getAlreadyRangAlarms() );
        alarm.setWakeUpTime( getWakeUpTime() );
        alarm.setWakeUpHour( getWakeUpHour() );
        alarm.setWakeUpMinute( getWakeUpMinute() );
        alarm.setInterval( getInterval() );
        alarm.setRepeat( getRepeat() );
        alarm.setDuration( getDuration() );
        alarm.setRingtoneName( getRingtoneName() );
        alarm.setRingtoneUri( getRingtoneUri() );
        alarm.setRing( getIsRing() );
        alarm.setVibrate( getIsVibrate() );
        alarm.setMemo( getMemo() );
        alarm.setRead( getIsRead() );
        alarm.setDestination( getDestination() );
        alarm.setLatitude( getLatitude() );
        alarm.setLongitude( getLongitude() );
        alarm.setArrivalTime( getArrivalTime() );
        alarm.setArrivalHour( getArrivalHour() );
        alarm.setArrivalMinute( getArrivalMinute() );
        return alarm;
    }

    public boolean isTurnedOn() {
        return preferences.getBoolean(TURNED_ON, DEFAULT_TURNED_ON);
    }

    public void setTurnedOn(boolean turnedOn) {
        editor.putBoolean(TURNED_ON, turnedOn).apply();
    }

    public int getAlreadyRangAlarms() {
        return preferences.getInt(ALREADY_RANG_ALARMS, DEFAULT_ALREADY_RANG_ALARMS);
    }

    public void setAlreadyRangAlarms(int i) {
        editor.putInt(ALREADY_RANG_ALARMS, i).apply();
    }

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

    public String getRingtoneName() {
        return preferences.getString(RINGTONE_NAME, DEFAULT_RINGTONE_NAME);
    }

    public void setRingtoneName(String ringtoneName) {
        editor.putString(RINGTONE_NAME, ringtoneName).apply();
    }

    public String getRingtoneUri() {
        return preferences.getString(RINGTONE_URI, DEFAULT_RINGTONE_URI);
    }

    public void setRingtoneUri(String uri) {
        editor.putString(RINGTONE_URI, uri).apply();
    }

    public boolean getIsVibrate() {
        return preferences.getBoolean(ISVIBRATE, DEFAULT_ISVIBRATE);
    }

    public void setIsVibrate(boolean isVibrate) {
        editor.putBoolean(ISVIBRATE, isVibrate).apply();
    }

    public boolean getIsRing() {
        return preferences.getBoolean(ISRING, DEFAULT_ISRING);
    }

    public void setIsRing(boolean isRing) {
        editor.putBoolean(ISRING, isRing).apply();
    }

    public boolean getIsRead() {
        return preferences.getBoolean(ISREAD, DEFAULT_ISREAD);
    }

    public void setIsRead(boolean isRead) {
        editor.putBoolean(ISREAD, isRead).apply();
    }

    public float getLatitude() {
        return preferences.getFloat(LATITUDE, DEFAULT_LATITUDE);
    }

    public void setLatitude(float latitude) {
        editor.putFloat(LATITUDE, latitude).apply();
    }

    public float getLongitude() {
        return preferences.getFloat(LONGITUDE, DEFAULT_LONGITUDE);
    }

    public void setLongitude(float longitude) {
        editor.putFloat(LONGITUDE, longitude).apply();
    }

    public String getMemo() {
        return preferences.getString(MEMO, DEFAULT_MEMO);
    }

    public void setMemo(String memo) {
        editor.putString(MEMO, memo).apply();
    }

    public String getDestination() {
        return preferences.getString(DESTINATION, DEFAULT_DESTINATION);
    }

    public void setDestination(String destination) {
        editor.putString(DESTINATION, destination).apply();
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
        // 지정한 시간이 이미 지났다면 하루를 더한다
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTimeInMillis();
    }

    public String millisToHour(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("a hh : mm");
        return sdf.format(date);
    }

    public String millisToMonth(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM월 dd일 a hh시 mm분");
        return sdf.format(date);
    }

    public String millisToYear(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분");
        return sdf.format(date);
    }

    public String millisToString(long millis) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long elapsedSeconds = millis / secondsInMilli;

        if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            return elapsedSeconds + "초";
        }

        if (elapsedDays == 0 && elapsedHours == 0) {
            return elapsedMinutes + "분 " + elapsedSeconds + "초";
        }

        if (elapsedDays == 0) {
            return elapsedHours + "시간 " + elapsedMinutes + "분 " + elapsedSeconds + "초";
        }

        return elapsedDays + "일 " + elapsedHours + "시간 " + elapsedMinutes + "분 " + elapsedSeconds + "초";
    }

}


