package com.bteam.project.alarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 알람 시간이 되면 실행할 작업
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        RingtonePlayer ringtonePlayer = new RingtonePlayer(context);
        ringtonePlayer.start();
    }
}
