package com.example.httpso;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class MyService extends Service {

    private static final String TAG = "Che";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: MyService");

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getByName("0.0.0.0");
                    Log.d(TAG, "onCreate: " + inetAddress.getHostAddress());
                    ServerManager serverManager = new ServerManager(getApplicationContext(), inetAddress, 8899);
                    serverManager.startServer();


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                Looper.loop();//增加部分
                //super.run();
            }
        }.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}