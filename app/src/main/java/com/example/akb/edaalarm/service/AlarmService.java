package com.example.akb.edaalarm.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

import com.example.akb.edaalarm.Alarmboastcast;
import com.example.akb.edaalarm.MainActivity;
import com.example.akb.edaalarm.activity.AlarmalertActivity;

/**
 * Created by akb on 2018/1/30.
 */

public class AlarmService extends IntentService {
    public AlarmService() {
        super("AlarmService");
    }
    @Override
    public IBinder onBind(Intent intent) {
        //playMusic();
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        //把该service创建为前台service
        Notification notification = new Notification();
        
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        startForeground(1, notification);
        //playMusic();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        /*if (player != null) {
            player.stop();
            player.release();
        }*/
    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
       /* if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if(bundle.getBoolean("music"))
                    playMusic();
                else
                    stopMusic();
            }
        }*/
    }
    @Override
    public void onHandleIntent(Intent intent){
        Intent i=new Intent(getApplicationContext(), AlarmalertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(i);
        Alarmboastcast.completeWakefulIntent(intent);
        //Toast.makeText(context,"ddg",Toast.LENGTH_SHORT).show();
    }




}