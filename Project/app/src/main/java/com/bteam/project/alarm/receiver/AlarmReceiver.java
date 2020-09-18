package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.bteam.project.alarm.service.AlarmService;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String KEY_IS_ONE_TIME = "onetime";
    DismissAlarmNotificationController dismissAlarmNotificationController;

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        dismissAlarmNotificationController = new DismissAlarmNotificationController(context, title);

        // 안드로이드 Q부터는 백그라운드에서 액티비티를 띄울 수 없음
        // 화면이 켜져 있을 때는 위에 작은 알람창만 하나 띄우고
        // 화면이 꺼져 있을 때는 액티비티를 띄운다
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            dismissAlarmNotificationController.showNotification();
        } else {
            Intent dismissAlarmIntent = new Intent(context, DismissAlarmActivity.class);
            context.startActivity(dismissAlarmIntent);
        }
    }
}
