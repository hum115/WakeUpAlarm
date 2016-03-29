package com.example.ahmad.wakeupalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlarmRing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_alarm_ring);

        //create an intent to the ringntone service

        Intent service_intent=new Intent(AlarmRing.this,RingtonePlayingService.class);
        startService(service_intent);

        Button stopAlarm = (Button)findViewById(R.id.stop_alarm);
        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(AlarmRing.this, RingtonePlayingService.class);
                stopService(stopIntent);
                Intent welcomeBack = new Intent(AlarmRing.this,Welcome.class);
                startActivity(welcomeBack);

            }
        });



    }
}
