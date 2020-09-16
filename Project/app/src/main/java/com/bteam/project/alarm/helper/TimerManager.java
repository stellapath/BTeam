package com.bteam.project.alarm.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bteam.project.Common;
import com.bteam.project.alarm.model.Alarm;
import com.bteam.project.alarm.receiver.AlarmBroadcastReceiver;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class TimerManager {

    private Context context;
    private AlarmManager alarmManager;
    private AlarmSharedPreferencesHelper helper;

    public TimerManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.helper = new AlarmSharedPreferencesHelper(context);
    }

    public void startTimer (long alarmMillis, int intervalMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmMillis);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(Common.KEY_IS_ONE_TIME, Boolean.FALSE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Common.REQUEST_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmMillis, TimeUnit.MINUTES.toMillis(intervalMinute), pendingIntent);
    }

    public void startSingleAlarmTimer(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(Common.KEY_IS_ONE_TIME, Boolean.FALSE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Common.REQUEST_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(millis, pendingIntent), pendingIntent);
    }

    public void startTimer (Alarm alarm) {
        startTimer(alarm.getWakeUpTime(), alarm.getInterval());
    }

    public void cancelTimer() {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, Common.REQUEST_ALARM, intent, 0);
        alarmManager.cancel(sender);
    }

    // 설정이 변경되었을 때 타이머 다시 실행
    public void resetSingleAlarmTimer(long millis) {
        cancelTimer();
        startSingleAlarmTimer(millis);
    }
}
