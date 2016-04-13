package com.example.ahmad.wakeupalarm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

public class Settings extends AppCompatActivity {
    int numberOfInitialHRinput;
    int initalHRValue;
    Switch dynamic;
    Switch statics;
    boolean isDemoOn = false;
    boolean boolSwitch = true;

    @Override
    public void onResume() {
        Intent getFromSettings = getIntent();
        isDemoOn = getFromSettings.getBooleanExtra("demo", false);
        numberOfInitialHRinput = getFromSettings.getIntExtra("int", 15);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Block the Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Setting up the Switchs

        dynamic = (Switch) findViewById(R.id.updateDynamic);
        statics = (Switch) findViewById(R.id.updateFix);

        //Getting From Old Welcome
        final Intent getFromSettings = getIntent();
        isDemoOn = getFromSettings.getBooleanExtra("demo", false);

        numberOfInitialHRinput = getFromSettings.getIntExtra("int", 15);
        boolSwitch = getFromSettings.getBooleanExtra("boolswitch", false);

        CheckStatus(boolSwitch);

        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.ValueOfInputs);
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
        dynamic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    statics.setChecked(false);
                    numberPicker.setMaxValue(40);
                    numberPicker.setMinValue(15);

                } else {
                    statics.setChecked(true);
                    numberPicker.setMaxValue(120);
                    numberPicker.setMinValue(55);
                }
            }
        });


        Button save = (Button) findViewById(R.id.SaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker numberPicker = (NumberPicker) findViewById(R.id.ValueOfInputs);
                Intent sentBack = new Intent(Settings.this, Welcome.class);
                sentBack.putExtra("demo", isDemoOn);
                sentBack.putExtra("boolswith", boolSwitch);

                if (boolSwitch) {
                    initalHRValue = numberPicker.getValue();
                    sentBack.putExtra("intOFHR", initalHRValue);

                } else {
                    numberOfInitialHRinput = numberPicker.getValue();
                    sentBack.putExtra("NOIHRV", numberOfInitialHRinput);
                }

                startActivity(sentBack);

            }
        });
    }

    public void CheckStatus(boolean status) {
        if (status) {
            dynamic.setChecked(false);
            statics.setChecked(true);
        } else {
            dynamic.setChecked(true);
            statics.setChecked(false);
        }

    }

}
