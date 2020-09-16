package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.TimerManager;
import com.bteam.project.alarm.model.Alarm;

public class BootCompletedIntentReceiver extends BroadcastReceiver {

    private final String LOG_TAG = this.getClass().getSimpleName();
    private final String INTENT_BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INTENT_BOOT_COMPLETED_NAME)) {
            TimerManager timerManager = new TimerManager(context);
            AlarmSharedPreferencesHelper helper = new AlarmSharedPreferencesHelper(context);
            Alarm alarm = helper.getAllParams();
            timerManager.resetSingleAlarmTimer(alarm.getWakeUpTime());
        }
    }

}
