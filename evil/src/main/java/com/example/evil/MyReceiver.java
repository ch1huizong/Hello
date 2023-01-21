package com.example.evil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// 1. 静态注册
// 2. 动态注册
// registerReceiver时, 分Application Context和Activity Context, 后者情况下可能会调用unregisterReceiver

// 需要一个动态权限申请的过程
public class MyReceiver extends BroadcastReceiver {
    private final String action = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Che", "Receiver成功了!");
        String a = intent.getAction();
        if (a.equals(action)) {
            Log.d("Che", "拦截到短信了!");
        }
    }
}