package com.bteam.project.alarm.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.bteam.project.R;

/**
 * 알람이 울렸을 때 발생되는 Notification
 */
public class AlarmNotificationController {

    private Context context;
    private NotificationManager notificationManager;

    private final String CHANNEL_ID = "alarm-channel01";
    private final String CHANNEL_NAME = "ProjectAlarmChannel";

    public AlarmNotificationController(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show() {
        Intent intent = new Intent(context, DismissAlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("알람")
                .setContentText("클릭해서 알람을 종료하세요.")
                .setAutoCancel(true)
                .addAction(getAction())
                .setFullScreenIntent(pendingIntent, true);
        notificationManager.notify(1, notificationBuilder.build());
    }

    public void cancel() {
        notificationManager.cancelAll();
    }

    private NotificationCompat.Action getAction() {
        Intent intent = new Intent(context, AlarmNotificationReceiver.class);
        intent.putExtra("NotificationID", 1);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action.Builder(0, "알람 종료", pendingIntent).build();
    }

}
