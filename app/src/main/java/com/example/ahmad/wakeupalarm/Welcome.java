package com.example.ahmad.wakeupalarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Welcome extends AppCompatActivity {

    // Initialization for the alarm Part
    final Calendar calendar = Calendar.getInstance();
    AlarmManager alarmManager;
    NumberPicker alarm_Hour;
    NumberPicker alarm_Min;
    NumberPicker ManualInputedHR;
    TextView update_text;
    Context context;
    Switch initialHrSwitch;
    Boolean switchBool;
    PendingIntent pendingIntent;
    int initialHRValue;
    int numberOfInitialInput = 0;
    boolean isDemoOn = false;

    @Override
    //this method is overriden to disable the back press
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onResume() {
        Intent getFromSettings = getIntent();
        isDemoOn = getFromSettings.getBooleanExtra("demo", false);
        numberOfInitialInput = getFromSettings.getIntExtra("NOIHRV", 15);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent i = new Intent(this, Settings.class);
                startActivity(i);
                break;
            case R.id.action_info:

                Intent b = new Intent(this, info.class);
                b.putExtra("int", numberOfInitialInput);
                startActivity(b);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (numberOfInitialInput == 0) {
            numberOfInitialInput = 15;
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        // The Manual HR
        ManualInputedHR = (NumberPicker) findViewById(R.id.ManualHR);
        ManualInputedHR.setMinValue(55);
        ManualInputedHR.setMaxValue(120);
        ManualInputedHR.setWrapSelectorWheel(true);


        // Setting Up the Switch

        initialHrSwitch = (Switch) findViewById(R.id.initialHeartRateSwitch);
        switchBool = false;
        initialHrSwitch.setChecked(switchBool);

        ManualInputedHR.setClickable(false);
        // What to do when the Switch is clicked

        initialHrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchBool = true;
                    initialHRValue = ManualInputedHR.getValue();
                    ManualInputedHR.setClickable(true);

                } else {
                    switchBool = false;

                    ManualInputedHR.setClickable(false);

                }
            }
        });


        //First Initialization

        this.context = this;
        alarm_Hour = (NumberPicker) findViewById(R.id.HourValue);
        alarm_Min = (NumberPicker) findViewById(R.id.minValue);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        update_text = (TextView) findViewById(R.id.alarmText);
        alarm_Hour.setMaxValue(23);
        alarm_Hour.setMinValue(0);
        alarm_Hour.setValue(Calendar.HOUR_OF_DAY);
        alarm_Hour.setWrapSelectorWheel(true);
        alarm_Min.setMinValue(0);
        alarm_Min.setMaxValue(59);
        alarm_Min.setValue(Calendar.MINUTE);
        alarm_Min.setWrapSelectorWheel(true);

        //this is a code found online that would not disable the bluetooth

        PackageManager pm = Welcome.this.getPackageManager();
        ComponentName componentName = new ComponentName(Welcome.this, ConnectionReceiver.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        // Take The value From the calendar

        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);
        final Intent connectionIntent = new Intent(this.context, ConnectionReceiver.class);


        // The Set alarm Click

        Button start_alarm = (Button) findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                //checking the switch

                if (switchBool) {
                    initialHRValue = ManualInputedHR.getValue();
                    my_intent.putExtra("initialValue", initialHRValue);
                    my_intent.putExtra("BoolSwitch", switchBool);
                } else {
                    my_intent.putExtra("int", numberOfInitialInput);
                    my_intent.putExtra("InitialHR", switchBool);
                }



                // For debugging purposes Make this run asap


                Calendar TimeNow;

                TimeNow = Calendar.getInstance();
                if (isDemoOn) {
                    // this is the one to delete
                    calendar.setTimeInMillis(TimeNow.getTimeInMillis() + 2000);
                    //launch the connection 20 seconde before
                } else {
                    //Regular Settings
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_Hour.getValue());
                    calendar.set(Calendar.MINUTE, alarm_Min.getValue());

                }
                //my_intent.putExtras(transmit);

                String pmOram;
                if (TimeNow.getTimeInMillis() <= calendar.getTimeInMillis()) {


                    int hour = alarm_Hour.getValue();
                    int min = alarm_Min.getValue();
                    String hour_s = String.valueOf(hour);
                    String min_s = String.valueOf(min);
                    pmOram = "am";
                    if (min < 10) {
                        min_s = "0" + String.valueOf(min);
                    }
                    if (hour > 12) {
                        hour_s = String.valueOf(hour - 12);

                        pmOram = "pm";
                    }


                    set_alarm_text("Alarm On , Set to:" + hour_s + ":" + min_s + " " + pmOram);

                    //Pending Intent Stuff
                    pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                            , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // pendingConnectionIntent= PendingIntent.getBroadcast(Welcome.this,0,connectionIntent,PendingIntent.FLAG_UPDATE_CURRENT);


                    //set the alarm Manager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pendingIntent);
                    // alarmManager.set(AlarmManager.RTC_WAKEUP,connectionTime.getTimeInMillis(),pendingConnectionIntent);

                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Chose a Time In the Future!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });
        Button alarm_off = (Button) findViewById(R.id.stop_alarm);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Alarm off!");
                alarmManager.cancel(pendingIntent);


            }
        });


        // The swith used to connect or not
        // Switch Connect = (Switch)findViewById(R.id.switch1);

    }


    private void set_alarm_text(String output) {

        update_text.setText(output);
    }
}
