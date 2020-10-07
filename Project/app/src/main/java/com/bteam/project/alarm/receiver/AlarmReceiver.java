package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.bteam.project.alarm.AlarmActivity;
import com.bteam.project.alarm.service.MyNotificationManager;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MyNotificationManager notificationManager = new MyNotificationManager(context);
            notificationManager.show();
        } else {
            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            context.startActivity(alarmIntent);
        }
    }
}
