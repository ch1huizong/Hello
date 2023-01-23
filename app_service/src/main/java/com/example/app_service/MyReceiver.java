package com.example.app_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// manifest中静态注册的receiver为什么没发挥作用?
// 安卓版本限制?
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Che", "MyReceiver onReceived !!!");
    }
}