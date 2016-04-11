package com.example.ahmad.wakeupalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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

        //Bundle abc;

        //Need to get the value coming from Bool and Int.
        CharSequence text = "The passed value is : "+ intent.getIntExtra("initialValue", 0);
        System.out.println("The value of HR is "+ intent.getIntExtra("initialValue",0));
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();


        Intent AlarmRing = new Intent(context, com.example.ahmad.wakeupalarm.AlarmRing.class);

        AlarmRing.putExtra("initialValue", intent.getIntExtra("initialValue", 0));
        AlarmRing.putExtra("BoolSwitch", intent.getBooleanExtra("BoolSwitch", false));
        AlarmRing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(AlarmRing);


    }
}