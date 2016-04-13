package com.example.ahmad.wakeupalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Welcome extends AppCompatActivity {


    // Initialization for the alarm Part
    final Calendar calendar = Calendar.getInstance();
    AlarmManager alarmManager;
    NumberPicker alarm_Hour;
    NumberPicker alarm_Min;
    TextView update_text;
    TextView dateToShow;
    Context context;
    Boolean switchBool = false;
    PendingIntent pendingIntent;
    int initialHRValue;
    int numberOfInitialInput = 0;
    boolean isDemoOn = false;
    Calendar alarmDate = Calendar.getInstance();
    boolean defaultDate = false;
    Calendar TimeNow = Calendar.getInstance();

    @Override
    //this method is overriden to disable the back press
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onResume() {
        Intent getFromSettings = getIntent();
        isDemoOn = getFromSettings.getBooleanExtra("demo", false);
        numberOfInitialInput = getFromSettings.getIntExtra("cba", 15);
        alarmDate.set(getFromSettings.getIntExtra("year", 0), getFromSettings.getIntExtra("month", 0), getFromSettings.getIntExtra("day", 0));
        switchBool = getFromSettings.getBooleanExtra("bls", false);
        initialHRValue = getFromSettings.getIntExtra("abc", 75);

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
                i.putExtra("HRstuff", switchBool);
                i.putExtra("demo", isDemoOn);
                i.putExtra("int", numberOfInitialInput);
                i.putExtra("date", alarmDate);
                i.putExtra("boolswitch", switchBool);
                startActivity(i);
                break;
            case R.id.action_info:

                Intent b = new Intent(this, info.class);
                startActivity(b);

        }

        return super.onOptionsItemSelected(item);
    }

/////////THE ON CREATE OF THE WELCOME APP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //Also Part Of The On Resume

        Intent getFromDateSelector = getIntent();
        Calendar today = Calendar.getInstance();
        int Year = getFromDateSelector.getIntExtra("year", 2016);
        int Month = getFromDateSelector.getIntExtra("month", 4);
        int Day = getFromDateSelector.getIntExtra("day", 13);

        defaultDate = getFromDateSelector.getBooleanExtra("default", false);

        alarmDate.set(Calendar.MONTH, Month);
        alarmDate.set(Calendar.YEAR, Year);
        alarmDate.set(Calendar.DAY_OF_MONTH, Day);
        System.out.println(alarmDate.get(Calendar.YEAR));
        //Print The date Selected

        dateToShow = (TextView) findViewById(R.id.dateShow);
        dateToShow.setText(setDateValue(alarmDate));
        System.out.println(alarmDate.get(Calendar.YEAR));

        //Block the Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        if (numberOfInitialInput == 0) {
            numberOfInitialInput = 15;
        }


        //First Initialization

        this.context = this;
        alarm_Hour = (NumberPicker) findViewById(R.id.HourValue);
        alarm_Min = (NumberPicker) findViewById(R.id.minValue);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        update_text = (TextView) findViewById(R.id.alarmText);
        alarm_Hour.setMaxValue(23);
        alarm_Hour.setMinValue(0);
        alarm_Hour.setValue(today.get(Calendar.HOUR_OF_DAY));
        alarm_Hour.setWrapSelectorWheel(true);
        alarm_Min.setMinValue(0);
        alarm_Min.setMaxValue(59);
        alarm_Min.setValue(today.get(Calendar.MINUTE));
        alarm_Min.setWrapSelectorWheel(true);

        //this is a code found online that would not disable the bluetooth

        PackageManager pm = Welcome.this.getPackageManager();
        ComponentName componentName = new ComponentName(Welcome.this, ConnectionReceiver.class);
        pm.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        // Take The value From the calendar

        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);


        // Set the Date

        Button dateSelect = (Button) findViewById(R.id.DateBtn);
        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToDateActivity = new Intent(Welcome.this, DateSelect.class);
                startActivity(goToDateActivity);


            }
        });


        // The Set alarm Click


        Button start_alarm = (Button) findViewById(R.id.start_alarm);

        start_alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //checking the switch

                if (switchBool) {
                    my_intent.putExtra("initialValue", initialHRValue);
                    my_intent.putExtra("bls", switchBool);
                } else {
                    my_intent.putExtra("int", numberOfInitialInput);
                    my_intent.putExtra("bls", switchBool);
                }


                // For debugging purposes Make this run asap
                Calendar TimeNow;
                TimeNow = Calendar.getInstance();

                if (isDemoOn) {
                    // this is the one to delete
                    calendar.setTimeInMillis(TimeNow.getTimeInMillis() + 2000);
                    set_alarm_text("Alarm On , Set to : ");
                    pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                            , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    //set the alarm Manager
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            pendingIntent);

                    //launch the connection 20 seconde before
                } else {

                    //Regular Settings
                    calendar.set(Calendar.HOUR_OF_DAY, alarm_Hour.getValue());
                    calendar.set(Calendar.MINUTE, alarm_Min.getValue());

                    if (defaultDate) {

                        calendar.set(Calendar.YEAR, alarmDate.get(Calendar.YEAR));
                        calendar.set(Calendar.MONTH, alarmDate.get(Calendar.MONTH));
                        calendar.set(Calendar.DAY_OF_MONTH, alarmDate.get(Calendar.DAY_OF_MONTH));

                    } else {
                        calendar.set(Calendar.YEAR, 2016);
                        calendar.set(Calendar.MONTH, 04);
                        calendar.set(Calendar.DAY_OF_MONTH, 13);

                    }


                    //my_intent.putExtras(transmit);

                    String pmOram;
                    if (TimeNow.get(Calendar.YEAR) <= calendar.get(Calendar.YEAR)) {
                        if (TimeNow.get(Calendar.YEAR) < calendar.get(Calendar.YEAR)) {
                            System.out.println("Year is bigger");
                            int hour = alarm_Hour.getValue();
                            int min = alarm_Min.getValue();
                            String dateOfAlarm = setDateValue(calendar);
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
                            set_alarm_text("Alarm On , Set to : " + hour_s + ":" + min_s + " " + pmOram + " " + dateOfAlarm);
                            //Pending Intent Stuff
                            pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                                    , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            //set the alarm Manager
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    pendingIntent);

                        } else if (TimeNow.get(Calendar.MONTH) <= calendar.get(Calendar.MONTH)) {
                            if ((TimeNow.get(Calendar.MONTH) + 1) < calendar.get(Calendar.MONTH)) {
                                int hour = alarm_Hour.getValue();
                                int min = alarm_Min.getValue();
                                String dateOfAlarm = setDateValue(calendar);
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
                                set_alarm_text("Alarm On , Set to : " + hour_s + ":" + min_s + " " + pmOram + " " + dateOfAlarm);
                                //Pending Intent Stuff
                                pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                                        , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                //set the alarm Manager
                                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pendingIntent);

                            } else if (TimeNow.get(Calendar.DAY_OF_MONTH) <= calendar.get(Calendar.DAY_OF_MONTH)) {
                                if (TimeNow.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
                                    System.out.println("Day is bigger");

                                    int hour = alarm_Hour.getValue();
                                    int min = alarm_Min.getValue();
                                    String dateOfAlarm = setDateValue(calendar);
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
                                    set_alarm_text("Alarm On , Set to : " + hour_s + ":" + min_s + " " + pmOram + " " + dateOfAlarm);
                                    //Pending Intent Stuff
                                    pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                                            , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    //set the alarm Manager
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                            pendingIntent);

                                } else if (TimeNow.get(Calendar.HOUR_OF_DAY) <= calendar.get(Calendar.HOUR_OF_DAY)) {
                                    if (TimeNow.get(Calendar.HOUR_OF_DAY) < calendar.get(Calendar.HOUR_OF_DAY)) {
                                        System.out.println("Hour is bigger");
                                        int hour = alarm_Hour.getValue();
                                        int min = alarm_Min.getValue();
                                        String dateOfAlarm = setDateValue(calendar);
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
                                        set_alarm_text("Alarm On , Set to : " + hour_s + ":" + min_s + " " + pmOram + " " + dateOfAlarm);

                                        //Pending Intent Stuff
                                        pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                                                , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                                        //set the alarm Manager
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                pendingIntent);


                                    } else if (TimeNow.get(Calendar.MINUTE) < calendar.get(Calendar.MINUTE)) {
                                        if (!defaultDate) {
                                            calendar.set(Calendar.YEAR, TimeNow.get(Calendar.YEAR));
                                            calendar.set(Calendar.MONTH, TimeNow.get(Calendar.MONTH));
                                            calendar.set(Calendar.DAY_OF_MONTH, TimeNow.get(Calendar.DAY_OF_MONTH));
                                        }
                                        System.out.println("Min is bigger");
                                        int hour = alarm_Hour.getValue();
                                        int min = alarm_Min.getValue();
                                        String dateOfAlarm = setDateValue(calendar);
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
                                        set_alarm_text("Alarm On , Set to : " + hour_s + ":" + min_s + " " + pmOram + " " + dateOfAlarm);

                                        //Pending Intent Stuff
                                        pendingIntent = PendingIntent.getBroadcast(Welcome.this, 0
                                                , my_intent, PendingIntent.FLAG_UPDATE_CURRENT);


                                        //set the alarm Manager
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                pendingIntent);


                                    } else {
                                        Context context = getApplicationContext();
                                        CharSequence text = "Chose a Time In the Future!-min";
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }
                                } else {
                                    Context context = getApplicationContext();
                                    CharSequence text = "hose a Time In the Future!- Hour";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = "Chose a Time In the Future!-day";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }

                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Chose a Time In the Future!-month";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }


                    } else {
                        System.out.println("time " + TimeNow.get(Calendar.YEAR) + " calendar " + calendar.get(Calendar.YEAR));
                        Context context = getApplicationContext();
                        CharSequence text = "Chose a Time In the Future!-Year";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
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

    private String setDateValue(Calendar a) {
        String year = "" + a.get(Calendar.YEAR);
        String month = "" + (a.get(Calendar.MONTH));
        String day = "" + a.get(Calendar.DAY_OF_MONTH);
        String Result = "" + day + "/" + month + "/" + year;
        return "Date : " + Result;
    }

    private void set_alarm_text(String output) {

        if (isDemoOn) {
            if (output == "Alarm off!") {
                update_text.setText(output);

            } else {
                update_text.setText("Demo is On, Launch in 2 Sec...");
            }

        } else {

            update_text.setText(output);
        }
    }
}
