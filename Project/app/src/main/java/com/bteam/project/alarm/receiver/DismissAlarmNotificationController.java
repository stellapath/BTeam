package com.bteam.project.alarm.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.bteam.project.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 화면이 켜져있을 때 상단 알람창 띄우기
 */
public class DismissAlarmNotificationController {

    public final int NOTIFICATION_ID = 1;
    public static final String INTENT_KEY_NOTIFICATION_ID = "notificationId";
    public final String CHANNEL_ID = "channel-01";
    private NotificationManager notificationManager;
    private Context context;
    private final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

    private String title;

    public DismissAlarmNotificationController(Context context, String title) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
        this.title = title;
    }

    public void showNotification() {

        Intent fullScreenIntent = new Intent(context, DismissAlarmActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, getChannelName(), IMPORTANCE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(getCurrentTime() + "에 설정된 알람이 울립니다.")
                .setAutoCancel(true)
                .addAction(getDismissNotificationAction())
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    public String getChannelName() {
        return context.getString(R.string.app_name) + "Channel";
    }

    public void cancelNotification() {
        notificationManager.cancelAll();
    }

    private NotificationCompat.Action getDismissNotificationAction() {
        Intent dismissIntent = new Intent(context, DismissNotificationReceiver.class);
        dismissIntent.putExtra(INTENT_KEY_NOTIFICATION_ID, NOTIFICATION_ID);

        PendingIntent dismissNotificationPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Action.Builder(
                0,
                "알람 끄기",
                dismissNotificationPendingIntent)
                .build();
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}
