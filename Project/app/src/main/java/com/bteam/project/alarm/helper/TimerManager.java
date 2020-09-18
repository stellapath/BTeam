package com.bteam.project.alarm.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.bteam.project.Common;
import com.bteam.project.alarm.receiver.AlarmReceiver;

/**
 * 알람 타이머 관련 클래스
 */
public class TimerManager {

    private Context context;
    private AlarmManager alarmManager;
    private AlarmSharedPreferencesHelper helper;

    public TimerManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.helper = new AlarmSharedPreferencesHelper(context);
    }

    // 기상 알람 울리기
    public void startWakeUpTimer(long wakeUpTimeMillis) {
        Intent wakeUpIntent = new Intent(context, AlarmReceiver.class);
        wakeUpIntent.putExtra("title", "기상 시간");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, wakeUpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpTimeMillis, pendingIntent);
    }

    // 기상 알람 끄기
    public void cancelWakeUpTimer() {
        Intent wakeUpIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, wakeUpIntent, 0);
        alarmManager.cancel(pendingIntent);
    }

    // 기상 알람 재시작
    public void resetWakeUpTimer(long wakeUpTimeMillis) {
        cancelWakeUpTimer();
        startWakeUpTimer(wakeUpTimeMillis);
    }

    // 도착 알람 울리기
    // TODO 도착알람은 계산이 필요함. 설정한 도착 시간 - ( 목적지까지 걸리는 시간 | 현재 날씨 상황 )
    public void startArrivalTimer(long arrivalTimeMillis) {
        Intent arrivalIntent = new Intent(context, AlarmReceiver.class);
        arrivalIntent.putExtra("title", "출근 시간");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, arrivalIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millis = arrivalTimeMillis; // TODO 계산이 들어갈 곳
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
    }

    // 도착 알람 끄기
    public void cancelArrivalTimer() {
        Intent arrivalIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, arrivalIntent, 0);
        alarmManager.cancel(pendingIntent);
    }

    // 도착 알람 재시작
    public void resetArrivalTimer(long arrivalTimeMillis) {
        cancelArrivalTimer();
        startArrivalTimer(arrivalTimeMillis);
    }

}
