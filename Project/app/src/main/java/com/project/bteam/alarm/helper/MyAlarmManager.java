package com.project.bteam.alarm.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.bteam.alarm.receiver.AlarmReceiver;

/**
 * 알람 타이머 관련 클래스
 */
public class MyAlarmManager {

    private static final String TAG = "MyAlarmManager";

    private Context context;
    private AlarmManager alarmManager;
    private AlarmSharedPreferencesHelper sharPrefHelper;
    private TimeCalculator timeCalc;

    public MyAlarmManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.sharPrefHelper = new AlarmSharedPreferencesHelper(context);
        this.timeCalc = new TimeCalculator();
    }

    public void start(long millis, int requestCode) {
        Log.i(TAG, "start");

        long alarmTimeMillis = timeCalc.isBefore(millis);
        Log.i(TAG, "alarmTime: " + timeCalc.toString(millis, 0));

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAlarmClock(new AlarmManager
                .AlarmClockInfo(alarmTimeMillis, pendingIntent), pendingIntent);
    }

    public void stop(int requestCode) {
        Log.i(TAG, "stop");

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void reset(long millis, int requestCode) {
        Log.i(TAG, "reset");

        stop(requestCode);
        start(millis, requestCode);
    }

}
