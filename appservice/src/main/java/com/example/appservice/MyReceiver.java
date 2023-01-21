package com.example.appservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private boolean isRegistered;

    // 自定义逻辑

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Che", "Network changed!"); // 为什么没有效果？
    }
}