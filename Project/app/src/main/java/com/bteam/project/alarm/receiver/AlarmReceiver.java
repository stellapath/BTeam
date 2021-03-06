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
import com.bteam.project.alarm.ArrivalAlarmActivity;
import com.bteam.project.alarm.service.MyNotificationManager;
import com.bteam.project.util.Common;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MyNotificationManager notificationManager = new MyNotificationManager(context);
            notificationManager.show();
        } else {
            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            context.startActivity(alarmIntent);
        }
        */
        switch (intent.getIntExtra("requestCode", 0)) {
            case Common.REQUEST_WAKEUP_ALARM :
                Intent ringer = new Intent(context, AlarmActivity.class);
                context.startActivity(ringer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case Common.REQUEST_ARRIVAL_ALARM :
                Intent ringer2 = new Intent(context, ArrivalAlarmActivity.class);
                context.startActivity(ringer2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
