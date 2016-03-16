package com.example.ahmad.wakeupalarm;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class SetTime extends AppCompatActivity {
    TimePicker alarmTimePicker;
    // function that will transform the Datepicker values into a string
    @TargetApi(Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        Button setime= (Button) findViewById(R.id.oktotime);
        Intent getdate = getIntent();
        final String date = getdate.getStringExtra("date");
        setime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTime.this, MainActivity.class);
                TimePicker a=(TimePicker)findViewById(R.id.timePicker);
                String Alarm = "The "+ date+", At "+ a.getHour()+" : "+a.getMinute();
                intent.putExtra("alarm", Alarm);

                startActivity(intent);


            }
        });
    }
}
