package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by DANHPC on 11/10/2017.
 */

public class DetectStatusNetwork extends Service {
    public static final String ACTION_1 = "dk.delfi.jma.utils.DetectStatusNetwork.DetectStatusNetworkAction";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(5); // periodic interval to check in seconds -> 2 seconds
    private static final String TAG = DetectStatusNetwork.class.getSimpleName();

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;
    private static int AUTO = 0;

    @Override
    public void onDestroy() {
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        ctx = this;

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleChanged();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, e.getMessage());
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_NOT_STICKY;
    }

    private void handleChanged() {
        try {
            boolean status = NetworkUtil.checkConnectedToServer();
            //Log.e(TAG, status + "");
            Intent broadcastIntent = new Intent(ACTION_1);
            broadcastIntent.putExtra("STATUS_NETWORK", status);
            sendBroadcast(broadcastIntent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
