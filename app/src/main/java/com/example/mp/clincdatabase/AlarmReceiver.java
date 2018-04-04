package com.example.mp.clincdatabase;

import android.content.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

/**
 * Created by sonu on 09/04/17.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

        /*
        //Stop sound service to play sound for alarm
        context.startService(new Intent(context, AlarmSoundService.class));

        //This will send a notification message and show notification in notification tray
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmNotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        Log.d("Dumaan", "daan"); */

        String temp = intent.getStringExtra("type");
        String temp1 = intent.getStringExtra("physician");
        Log.i("CHECK", temp);

        // TODO: This method is called when the BroadcastReceiver is receiving
        startAlarmService(context, temp, temp1);


    }

    public void startAlarmService(Context context, String temp, String temp1){
        Log.wtf("RECEIVER", "RECEIVED THE ACTION");

        Intent intentNext = new Intent(context, AlarmNotificationService.class);
        intentNext.putExtra("type", temp);
        intentNext.putExtra("physician", temp1);
        //intentNext.putExtra(Schedule.TABLE, schedule);
        startWakefulService(context, intentNext);
    }


}