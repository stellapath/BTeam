package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bteam.project.alarm.helper.AlarmSharedPreferencesHelper;
import com.bteam.project.alarm.helper.TimerManager;

/**
 * 부팅 시에 설정된 알람 데이터를 가져와서 알람을 다시 설정
 */
public class BootCompletedIntentReceiver extends BroadcastReceiver {

    private final String INTENT_BOOT_COMPLETED_NAME = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(INTENT_BOOT_COMPLETED_NAME)) {
            TimerManager timerManager = new TimerManager(context);
            AlarmSharedPreferencesHelper helper = new AlarmSharedPreferencesHelper(context);
            long wakeUpTimeMillis = helper.getWakeUpTime();
            long arrivalTimeMillis = helper.getArrivalTime();
            timerManager.resetWakeUpTimer(wakeUpTimeMillis);
            timerManager.resetArrivalTimer(arrivalTimeMillis);
        }
    }

}