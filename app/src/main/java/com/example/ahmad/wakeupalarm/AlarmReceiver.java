package com.example.ahmad.wakeupalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ahmad on 3/15/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //create an intent to the ringntone service
        //Intent service_intent=new Intent(context,RingtonePlayingService.class);
        //context.startService(service_intent);

        //Go to Alarm Ring Activity

        Intent AlarmRing = new Intent(context, com.example.ahmad.wakeupalarm.AlarmRing.class);
        AlarmRing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(AlarmRing);

    }
}