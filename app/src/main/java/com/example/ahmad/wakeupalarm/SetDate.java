package com.example.ahmad.wakeupalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class SetDate extends AppCompatActivity {
// function that will transform the Datepicker values into a string
    public String setdatefct(DatePicker a) {
        int m = a.getMonth();
        int d = a.getDayOfMonth();
        int y = a.getYear();
        String date2 = "" + d + "/"+ m + "/" + y;
        return date2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);
        Button setdate = (Button) findViewById(R.id.oktodate);
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // overloading the intent with a string to pass value to the other Activity
                Intent intent = new Intent(SetDate.this, SetTime.class);
                intent.putExtra("date",setdatefct((DatePicker) findViewById(R.id.datePicker)));
                startActivity(intent);


            }
        });
    }

}