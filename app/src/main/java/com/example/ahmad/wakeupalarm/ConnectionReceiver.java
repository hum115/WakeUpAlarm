package com.example.ahmad.wakeupalarm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Set;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ZephyrProtocol;

/**
 * Created by Ahmad on 3/30/2016.
 */
public  class ConnectionReceiver extends BroadcastReceiver {

    Calendar AlramTime ;
    BluetoothAdapter adapter = null;
    BTClient _bt;
    ZephyrProtocol _protocol;
    NewConnectedListener _NConnListener;
    private final int HEART_RATE = 0x100;
    private final int INSTANT_SPEED = 0x101;
    HRarray initialHR = new HRarray(5);
    HRarray afterRing = new HRarray(5);
    Context context=this.context;

    public void onReceive(Context context, Intent intent) {


/*Sending a message to android that we are going to initiate a pairing request*/
        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        /*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
        context.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
        // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
        IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
        context.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);
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
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //Reset all the values to 0s

        } else {
            CharSequence text = "Unable to Connect...";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }

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
            TextView tv;
            switch (msg.what) {
                case HEART_RATE:
                    String HeartRatetext = msg.getData().getString("HeartRate");

                    int a=msg.getData().getInt("HeartRateValue");
                    System.out.println("Heart Rate Info is " + HeartRatetext + " and the one i am passing is " + a);
                    //Create the Logical steps that will either enable or disable the Alarm
                    initialHR.addValue(a);

                    break;

                case INSTANT_SPEED:
                    String InstantSpeedtext = msg.getData().getString("InstantSpeed");

                    break;

            }
        }

    };
}
