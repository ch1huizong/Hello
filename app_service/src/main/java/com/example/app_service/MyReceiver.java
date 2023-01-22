package com.example.app_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// manifest中静态注册的receiver为什么没发挥作用?
public class MyReceiver extends BroadcastReceiver {

    private boolean isRegistered;

    public MyReceiver() {
        this.isRegistered = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Log.d("Che", "Network changed!");
        } else {
            Log.d("Che", "Handle other broadcast!");
        }
    }
}