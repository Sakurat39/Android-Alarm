package com.example.akb.edaalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.akb.edaalarm.service.AlarmService;

/**
 * Created by akb on 2018/1/31.
 */

public class Alarmboastcast extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    int h,m;
    String str;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        startWakefulService(context, service);
        setResultCode(Activity.RESULT_OK);
    }

}
