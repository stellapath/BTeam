package com.bteam.project.alarm.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bteam.project.R;
import com.bteam.project.util.Common;

public class MyNotificationManager {

    private Context context;
    private NotificationManager notificationManager;

    public MyNotificationManager(Context context) {
        this.context = context;
        this.notificationManager
                = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private String channelId = "bteamAlarmChannel";
    private CharSequence channelName = "bteamAlarmChannel";
    private int importance = NotificationManager.IMPORTANCE_HIGH;

    public void show() {
        // 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(context, FullscreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, Common.REQUEST_NOTIFICATION_ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("알람")
                .setContentText("여기를 터치하여 알람을 종료하세요.")
                .setAutoCancel(true)
                .setOngoing(true)
                .setFullScreenIntent(pendingIntent, true);

        notificationManager.notify(1, notificationBuilder.build());
    }

    public void cancel() {
        notificationManager.cancelAll();
    }
}
