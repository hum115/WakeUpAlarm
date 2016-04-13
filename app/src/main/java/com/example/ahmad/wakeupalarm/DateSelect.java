package com.example.ahmad.wakeupalarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select);
        Calendar today = Calendar.getInstance();

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMinDate(today.getTimeInMillis());





        Button saveAndGoBack = (Button) findViewById(R.id.button);
        saveAndGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Passing the Values of the DatePicker
                Intent goBackToWelcome = new Intent(DateSelect.this, Welcome.class);
                goBackToWelcome.putExtra("year", datePicker.getYear());
                goBackToWelcome.putExtra("month", datePicker.getMonth());
                goBackToWelcome.putExtra("day", datePicker.getDayOfMonth());
                goBackToWelcome.putExtra("default",true);
                startActivity(goBackToWelcome);
            }
        });


    }
}
