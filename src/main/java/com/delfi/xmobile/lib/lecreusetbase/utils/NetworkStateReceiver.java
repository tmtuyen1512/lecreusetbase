package com.delfi.xmobile.lib.lecreusetbase.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by viet-dto on 09 Jun 2017
 */

public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "Network State Receiver";

    public static INetworkChangedCallback callback;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DetectStatusNetwork.ACTION_1)) {
            boolean status = intent.getBooleanExtra("STATUS_NETWORK", false);
            //Log.i(TAG, "Network " + status);
            if (callback != null)
                callback.networkChanged(status);
        }
    }
}
