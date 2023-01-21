package com.example.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


// 服务种类:
// 1. Scheduled服务
// 2. Started服务, 与启动组件无关
// 3. Bound服务, 当bind的服务的组件都解绑时，服务消亡
//
// Bound服务注意点: c-s形式的服务, 需要返回IBinder接口
//      1. 当c-s端在一个进程中的时候， IBinder可以直接继承Binder
//      2. 不在同一进程, 通过Message或者AIDL通信

// 以下是Bound服务，服务端的实现代码
public class MyService extends Service {
    private final IBinder mbinder = new MyBinder(); // 接口实例

    public MyService() {
    }

    public void testToCall() {
        Log.d("Che", "testToCall called!");
    }

    // 其次， 实现收到Client发送请求的, 服务端的回调
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Che", "MyService onBind.");
        return mbinder; // 注意, 必须返回一个IBinder
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Che", "MyService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Che", "MyService onDestrory");
    }

    // 客户端startService方式启动服务，服务部分的回调
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Che", "MyService onStartCommand");

        /*
        // start foreground, 有问题，可能需要创建channel
        Intent notification = new Intent(this, MainActivity.class);
        PendingIntent pend = PendingIntent.getActivity(this, 0, notification, 0);

        Notification n = new Notification
                .Builder(this)
                .setContentTitle("This is Foreground Service")
                .setContentText("Foreground")
                .setContentIntent(pend)
                .setTicker("Ticker")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(111, n);
         */
        return START_STICKY;
    }

    // 首先, 需要返回IBinder接口, 可以添加接口实现方法
    public class MyBinder extends Binder {

        MyService getService() {
            return MyService.this;
        }

        int add(int a, int b) { // 自添加的
            return a + b;
        }
    }
}