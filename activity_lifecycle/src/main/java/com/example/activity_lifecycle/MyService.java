package com.example.activity_lifecycle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private final IBinder mbinder = new MyBinder();

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Che", "MyService onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Che", "MyService onBind.");
        return mbinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Che", "MyService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Che", "MyService onDestrory");
    }

    public void testToCall() {
        Log.d("Che", "testToCall called!");
    }

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

}