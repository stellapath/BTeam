package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bteam.project.alarm.service.AlarmService;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmService = new Intent(context, AlarmService.class);
        context.startService(intent);

        // 1) 알림 발생 (Notification)


        // 2) Ringtone 재생 + 진동


        // 3) 화면 띄우기 (DismissAlarmActivity)


    }
}
