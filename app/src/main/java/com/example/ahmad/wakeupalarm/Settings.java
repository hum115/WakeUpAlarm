package com.example.ahmad.wakeupalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    int numberOfInitialHRinput;
    boolean isDemoOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.ValueOfInputs);
        numberPicker.setMaxValue(40);
        numberPicker.setMinValue(15);
        numberPicker.setWrapSelectorWheel(true);
        Intent getFromWelcome = getIntent();
        numberPicker.setValue(getFromWelcome.getIntExtra("int", 25));

        Switch demoSetUp = (Switch) findViewById(R.id.demoSwitch);
        demoSetUp.setChecked(isDemoOn);
        demoSetUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDemoOn = true;

                } else {
                    isDemoOn = false;


                }
            }
        });


        Button save = (Button) findViewById(R.id.SaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker numberPicker = (NumberPicker) findViewById(R.id.ValueOfInputs);
                numberOfInitialHRinput = numberPicker.getValue();

                Intent sentBack = new Intent(Settings.this, Welcome.class);
                sentBack.putExtra("demo",isDemoOn);
                sentBack.putExtra("NOIHRV", numberOfInitialHRinput);
                startActivity(sentBack);

            }
        });
    }

}