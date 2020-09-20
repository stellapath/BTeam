package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Notification 발생
        AlarmNotificationController notificationController = new AlarmNotificationController(context);
        notificationController.show();

    }
}
