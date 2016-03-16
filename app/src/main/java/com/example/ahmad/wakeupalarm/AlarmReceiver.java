package com.example.ahmad.wakeupalarm;

/**
 * Created by Ahmad on 3/15/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Working","Working");
        //create an intent to the ringntone service
        Intent service_intent=new Intent(context,RingtonePlayingService.class);
        context.startService(service_intent);

    }
}
