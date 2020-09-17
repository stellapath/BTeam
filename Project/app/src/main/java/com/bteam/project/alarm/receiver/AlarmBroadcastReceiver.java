package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.bteam.project.alarm.DismissAlarmActivity;

/**
 * 알람 설정 시 호출됨
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {

    DismissAlarmNotificationController dismissAlarmNotificationController;

    @Override
    public void onReceive(Context context, Intent intent) {
        dismissAlarmNotificationController = new DismissAlarmNotificationController(context);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            dismissAlarmNotificationController.showNotification();
        } else {
            Intent dismissAlarmIntent = new Intent(context, DismissAlarmActivity.class);
            context.startActivity(dismissAlarmIntent);
        }
    }
}
