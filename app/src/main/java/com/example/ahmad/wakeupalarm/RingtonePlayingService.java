package com.example.ahmad.wakeupalarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ahmad on 3/16/2016.
 */
public class RingtonePlayingService extends Service {
    MediaPlayer mediasong;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received Start id" + startId + ":" + intent);
        mediasong = MediaPlayer.create(this, R.raw.alarmst);
        mediasong.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "You Have Woken Up", Toast.LENGTH_SHORT).show();
        mediasong.stop();
    }
}
