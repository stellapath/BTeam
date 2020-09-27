package com.bteam.project.alarm.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bteam.project.R;
import com.bteam.project.alarm.AlarmActivity;
import com.bteam.project.util.Common;

public class MyNotificationManager {

    private Context context;
    private NotificationManager notificationManager;

    public MyNotificationManager(Context context) {
        this.context = context;
        this.notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private String CHANNEL_ID = "bteamAlarmChannel";
    private CharSequence CHANNEL_NAME = "bteamAlarmChannel";
    private int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    private int NOTIFICATION_ID = 1;

    public void show() {
        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, Common.REQUEST_NOTIFICATION_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("알람")
                .setContentText("여기를 터치하여 알람을 종료하세요.")
                .setAutoCancel(true)
                .setOngoing(true)
                .setFullScreenIntent(pendingIntent, true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void cancel() {
        notificationManager.cancelAll();
    }
}
