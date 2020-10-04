package com.project.bteam.alarm.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.bteam.util.Common;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"));
        MyAlarmManager alarmManager = new MyAlarmManager(context);
        AlarmSharedPreferencesHelper sharPrefHelper = new AlarmSharedPreferencesHelper(context);
        alarmManager.reset( sharPrefHelper.getWakeUpMillis(), Common.REQUEST_WAKEUP_ALARM );
    }
}
