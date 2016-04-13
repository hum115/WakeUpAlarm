package com.example.ahmad.wakeupalarm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import zephyr.android.HxMBT.BTClient;

public class AlarmRing extends AppCompatActivity {

    // Values coming From the Welcome Page If Initial value of Heart Rate is set

    boolean doesInitialHrExists;
    Intent ToGetInitialHR;
    int ValueComingFromWelcome;

    // the counter is to count how many values are going into the Initial HR array if needed

    int counter = 0;
    int NumberOfinitialFilling = 0;

    // These variable are used to get connection and values from HeartRate

    BTClient _bt;
    NewConnectedListener _NConnListener;
    private final int HEART_RATE = 0x100;
    private final int INSTANT_SPEED = 0x101;
    BluetoothAdapter adapter = null;

    //For Alarm
    Boolean isconnected = false;
    HRarray initialHR = new HRarray(5);
    HRarray afterRing = new HRarray(5);

    @Override
    //this method is overriden to disable the back press
    public void onBackPressed() {
        //do nothing
    }

    //find a way to overide the onPause and onStop methods. (nothing so far ...)


    @Override
    //this is the oncreate of the alarm ring activity
    protected void onCreate(Bundle savedInstanceState) {
        //Block the Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        if (NumberOfinitialFilling == 0) {
            NumberOfinitialFilling = 15;
        }

        //First See if there is a fixed initial Heart Rate

        ToGetInitialHR = getIntent();
        NumberOfinitialFilling = ToGetInitialHR.getIntExtra("int", 15);
        ValueComingFromWelcome = ToGetInitialHR.getIntExtra("initialValue", 0);
        doesInitialHrExists = ToGetInitialHR.getBooleanExtra("BoolSwitch", false);

        //Connect To the HR sensor


        /*Sending a message to android that we are going to initiate a pairing request*/
        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        /*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
        this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
        // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
        IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
        this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);
        String BhMacID = "00:07:80:9D:8A:E8";
        //String BhMacID = "00:07:80:88:F6:BF";
        adapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().startsWith("HXM")) {
                    BluetoothDevice btDevice = device;
                    BhMacID = btDevice.getAddress();
                    break;

                }
            }


        }

        TextView statusBT_1 = (TextView) findViewById(R.id.BTstatus);
        //BhMacID = btDevice.getAddress();
        BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
        String DeviceName = Device.getName();
        _bt = new BTClient(adapter, BhMacID);
        _NConnListener = new NewConnectedListener(Newhandler, Newhandler);
        _bt.addConnectedEventListener(_NConnListener);


        if (_bt.IsConnected()) {
            _bt.start();
            CharSequence text = "Connected to HxM " + DeviceName;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            statusBT_1.setText("Connected To Sensor");
            isconnected = true;

            TextView HRVALUESTATUS = (TextView) findViewById(R.id.HRValueStats);
            HRVALUESTATUS.setText("YOU NEED TO MOVE");


            //Reset all the values to 0s

        } else {
            CharSequence text = "Unable to Connect...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
            statusBT_1.setText("Not Connected");
            isconnected = false;
            TextView a = (TextView) findViewById(R.id.HRValueStats);
            a.setText("");
            a = (TextView) findViewById(R.id.HRinitial);
            a.setText("");
            a = (TextView) findViewById(R.id.HRafterRing);
            a.setText("");
        }

        final Button stopAlarm = (Button) findViewById(R.id.stop_alarm);

        if (isconnected) {
            stopAlarm.setClickable(false);
        }


        //create an intent to the ringntone service

        //This is Rigntone Service Playing.

        Intent service_intent = new Intent(AlarmRing.this, RingtonePlayingService.class);
        startService(service_intent);

        // the stop button.

        stopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isconnected == false) {
                    Intent stopIntent = new Intent(AlarmRing.this, RingtonePlayingService.class);
                    stopService(stopIntent);


                    Intent welcomeBack = new Intent(AlarmRing.this, Welcome.class);
                    startActivity(welcomeBack);
                } else {


                }
            }
        });


    }

    //This method gets the gets the extras from the BT receiver
    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }

    //this method also is used for the BT connection
    private class BTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BTIntent", intent.getAction());
            Bundle b = intent.getExtras();
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
            try {
                BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
                Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[]{String.class});
                byte[] pin = (byte[]) m.invoke(device, "1234");
                m = device.getClass().getMethod("setPin", new Class[]{pin.getClass()});
                Object result = m.invoke(device, pin);
                Log.d("BTTest", result.toString());
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    final Handler Newhandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HEART_RATE:

                    // declare the Textviews to be modified

                    TextView statusBT_1 = (TextView) findViewById(R.id.BTstatus);
                    TextView iniAvegrage = (TextView) findViewById(R.id.AverageINI);
                    TextView newAverage = (TextView) findViewById(R.id.AveragerNew);

                    if (isconnected == true) {

                        if (checkConnection(statusBT_1, isconnected)) {
                            checkConnection(statusBT_1, isconnected);
                            String HeartRatetext = msg.getData().getString("HeartRate");

                            //This is the Message Containing the Value of HR in int

                            int a = msg.getData().getInt("HeartRateValue");
                            System.out.println("Heart Rate Info is " + HeartRatetext + " and the one i am passing is " + a);

                            //Create Logic if there is an initial HR

                            //first step is if there is no initial Heart rate

                            if (!doesInitialHrExists) {
                                checkConnection(statusBT_1, isconnected);

                                fillupTheArrays(initialHR, afterRing, a, counter++, NumberOfinitialFilling);
                                if (NumberOfinitialFilling - counter > 0) {
                                    iniAvegrage.setText("" + initialHR.getAverage() + "," + (NumberOfinitialFilling - counter) + " left");
                                } else {
                                    iniAvegrage.setText("" + initialHR.getAverage());
                                }
                                newAverage.setText("" + afterRing.getAverage());
                                if (isHRbigger(initialHR.getAverage(), afterRing.getAverage(), 1.05) && isconnected == true) {

                                    TextView HRVALUESTATUS = (TextView) findViewById(R.id.HRValueStats);
                                    HRVALUESTATUS.setText("YOU ARE GOOD");
                                    CloseUp();
                                }
                            } else //the logic to do if there is any initial value for a heart rate
                            {
                                if (ValueComingFromWelcome != 0) {
                                    checkConnection(statusBT_1, isconnected);
                                    iniAvegrage.setText("" + ValueComingFromWelcome);
                                    afterRing.addValue(a);
                                    newAverage.setText("" + afterRing.getAverage());
                                    if (isHRbigger(ValueComingFromWelcome, afterRing.getAverage(), 1.05)) {
                                        TextView HRVALUESTATUS = (TextView) findViewById(R.id.HRValueStats);
                                        HRVALUESTATUS.setText("YOU ARE GOOD");
                                        CloseUp();
                                    }
                                    //Compare the Values
                                } else {
                                    doesInitialHrExists = false;
                                }

                            }

                        }
                    } else {
                        isconnected = false;
                        CharSequence text = "Connection Lost ... ";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(AlarmRing.this, text, duration);
                        toast.show();
                    }
                    break;

                case INSTANT_SPEED:

                    //we are not using this Speed.

                    String InstantSpeedtext = msg.getData().getString("InstantSpeed");

                    break;

            }
        }

    };

    public boolean checkConnection(TextView B, Boolean C) {
        if (!_bt.IsConnected()){

            B.setText("Lost Connection");
            isconnected = false;
            CharSequence text = "Connection Lost ... ";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AlarmRing.this, text, duration);
            toast.show();
            return false;
        }

        return true;
    }

    public void CloseUp() {
        Intent stopIntent = new Intent(AlarmRing.this, RingtonePlayingService.class);
        stopService(stopIntent);
        if (_bt.IsConnected()) {
            _bt.Close();
        }

        Intent welcomeBack = new Intent(AlarmRing.this, Welcome.class);
        startActivity(welcomeBack);


    }

    public void fillupTheArrays(HRarray i, HRarray n, int a, int count, int numbIn) {
        if (a < 0) {
            a = -a;
        }
        if (count < numbIn) {
            i.addValue(a);
        } else {
            n.addValue(a);
        }
    }

    // Function to compare two values
    public boolean isHRbigger(int initial, int NewHR, double PercentBigger) {

        return (initial * PercentBigger <= NewHR);

    }
}
