package com.delfi.xmobile.lib.lecreusetbase.view.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delfi.xmobile.lib.lecreusetbase.R;
import com.delfi.xmobile.lib.lecreusetbase.utils.DateTime;
import com.delfi.xmobile.lib.lecreusetbase.view.customerview.StatusBar;
import com.delfi.xmobile.lib.xcore.template_v2.ui.EditRecordActivity;

import java.util.concurrent.TimeUnit;


/**
 * Created by DANHPC on 10/01/2018.
 */

public class BaseEditActivity<T> extends EditRecordActivity {


    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2);
    private Thread thread;

    private boolean keepRunning;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        this.keepRunning = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView text = (TextView) findViewById(R.id.tvDate);
                                if(text != null)
                                    text.setText(DateTime.getCurrentTimeOnTopBar(getBaseContext()));

                                ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                                if (imgWifi != null) {
                                    int numberOfLevels = 5;
                                    int level = 0;
                                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    if(wifiManager != null) {
                                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                        if (wifiInfo != null)
                                            level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

                                        if (wifiManager.isWifiEnabled()) {
                                            if (level == 0)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_0);
                                            else if (level == 1)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_1);
                                            else if (level == 2)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_2);
                                            else if (level == 3)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_3);
                                            else if (level == 4)
                                                imgWifi.setImageResource(R.drawable.ic_wifi_signal_level_4);
                                        }
                                        else
                                            imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                                    }
                                    else
                                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                                }

                                ImageView imgEthernet = (ImageView) findViewById(R.id.imgEthernet);
                                if (imgEthernet != null) {
                                    if(isEthernetConnected())
                                        imgEthernet.setVisibility(View.VISIBLE);
                                    else
                                        imgEthernet.setVisibility(View.GONE);
                                }

                                //Log.i("Runnable", BaseEditActivity.this.getClass().getSimpleName());
                            }
                        });
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.i("thread check time", e.getMessage());
                    }

                }while(keepRunning);

            }
        });
        thread.start();
    }

    public boolean isEthernetConnected(){
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()){
            return (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET);
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            StatusBar.addStatusBar(this);
        }
        catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(mBatInfoReceiver, intentFilter);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBatInfoReceiver);
        unregisterReceiver(wifiReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.keepRunning = false;
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            ProgressBar progressBattery = (ProgressBar) BaseEditActivity.this.findViewById(R.id.progressBattery);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                TextView tvBatteryPercent = (TextView) BaseEditActivity.this.findViewById(R.id.tvBatteryPercent);
                if(tvBatteryPercent != null){
                    tvBatteryPercent.setText(batteryPct + "%");
                }
                if(progressBattery != null)
                    progressBattery.setProgress(batteryPct);
            }
            if(status == BatteryManager.BATTERY_STATUS_CHARGING){
                if(progressBattery != null)
                    progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent_charge));
            }
            else {
                if(progressBattery != null)
                    progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent));
            }
            if(intent.getAction() == Intent.ACTION_POWER_CONNECTED && progressBattery != null) {
                progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent_charge));
            }
            else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED && progressBattery != null)
                progressBattery.setProgressDrawable(getResources().getDrawable(R.drawable.battery_percent));

            //Log.i("mBatInfoReceiver", level + " " + scale + " " + plugged + "\n" + intent.getAction());
        }
    };

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED: {
                    break;
                }

                case WifiManager.WIFI_STATE_ENABLING: {
                    break;
                }
                case WifiManager.WIFI_STATE_DISABLED: {
                    ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                    if(imgWifi != null){
                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                    }
                    break;
                }

                case WifiManager.WIFI_STATE_DISABLING: {
                    break;
                }

                case WifiManager.WIFI_STATE_UNKNOWN: {
                    ImageView imgWifi = (ImageView) findViewById(R.id.imgWifi);
                    if(imgWifi != null){
                        imgWifi.setImageResource(R.drawable.ic_wifi_disable);
                    }
                    break;
                }
            }
        } };
}
