package com.example.ahmad.wakeupalarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Welcome extends AppCompatActivity {
AlarmManager alarmManager;
    NumberPicker alarm_Hour;
    NumberPicker alarm_Min;
    TextView update_text;
    Context context;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //First Initialization
        this.context=this;
        alarm_Hour = (NumberPicker)findViewById(R.id.HourValue);
        alarm_Min= (NumberPicker)findViewById(R.id.minValue);
        alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        update_text = (TextView)findViewById(R.id.alarmText);
        alarm_Hour.setMaxValue(24);
        alarm_Hour.setMinValue(0);
        alarm_Hour.setValue(GregorianCalendar.HOUR);
        alarm_Hour.setWrapSelectorWheel(true);
        alarm_Min.setMinValue(0);
        alarm_Min.setMaxValue(59);
        alarm_Min.setValue(GregorianCalendar.MINUTE);
        alarm_Min.setWrapSelectorWheel(true);



        // Take The value From the calendar

        final Calendar calendar = Calendar.getInstance();
        final Intent my_intent = new Intent(this.context,AlarmReceiver.class);
        Button start_alarm=(Button)findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //setting Calendar to the TimePicker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_Hour.getValue());
                calendar.set(Calendar.MINUTE, alarm_Min.getValue());
                String pmOram;

                int hour = alarm_Hour.getValue();
                int min = alarm_Min.getValue();
                String hour_s=String.valueOf(hour);
                String min_s=String.valueOf(min);
                pmOram="am";
                if(min<10){
                    min_s="0"+String.valueOf(min);
                }
                 if(hour>12){
                     hour_s=String.valueOf(hour-12);

                     pmOram="pm";
                 }


                set_alarm_text("Alarm On , Set to:"+hour_s+":"+min_s+" "+pmOram);

                //Pending Intent Stuff
               pendingIntent = PendingIntent.getBroadcast(Welcome.this,0
                ,my_intent,PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm Manager
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                     pendingIntent);


            }
        });
        Button alarm_off=(Button)findViewById(R.id.stop_alarm);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Alarm off!");
                alarmManager.cancel(pendingIntent);



            }
        });
        Button checkHeartRate=(Button)findViewById(R.id.GoToMaiActivity);
        checkHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(Welcome.this,MainActivity.class);
                startActivity(intent);


            }
        });
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }
}
