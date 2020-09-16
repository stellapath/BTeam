package com.bteam.project.alarm.dialog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.bteam.project.Common;
import com.bteam.project.R;
import com.bteam.project.alarm.DismissAlarmActivity;
import com.bteam.project.alarm.receiver.DismissNotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DismissAlarmNotificationController {

    private Context context;
    private NotificationManager notificationManager;

    public DismissAlarmNotificationController(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        Intent fullScreenIntent = new Intent(context, DismissAlarmActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, Common.REQUEST_ALARM,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    Common.CHANNEL_ID, "MultiAlarmChannel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, Common.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle("알람")
                .setContentText(getCurrentTime() + "에 설정된 알람이 울립니다.")
                .setAutoCancel(true)
                .addAction(getDismissNotificationAction())
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(Common.NOTIFICATION_ID, notificationBuilder.build());
    }

    private NotificationCompat.Action getDismissNotificationAction() {
        Intent dismissIntent = new Intent(context, DismissNotificationReceiver.class);
        dismissIntent.putExtra(Common.INTENT_KEY_NOTIFICATION_ID, Common.NOTIFICATION_ID);

        PendingIntent dismissNotificationPendingIntent
                = PendingIntent.getBroadcast(context, Common.REQUEST_ALARM, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Action.Builder(0, "알람 종료",
                dismissNotificationPendingIntent).build();
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}
