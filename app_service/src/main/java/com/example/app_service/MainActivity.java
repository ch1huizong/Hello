package com.example.app_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


// 客户端代码部分
public class MainActivity extends AppCompatActivity {
    private boolean bound; // 绑定了与否的标志

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("Che", "onServiceConnected");

            MyService.MyBinder binder = (MyService.MyBinder) iBinder;
            binder.getService().testToCall();
            Log.d("Che", "[Service] - add: " + binder.add(1, 2));

            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("Che", "onServiceDisconnected");

            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 动态注册一个broadcast receiver
        /*
        IntentFilter v = new IntentFilter();
        v.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.getApplicationContext().registerReceiver(new MyReceiver(), v);
         */


        // 第一种方式启动服务
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "Start Service");
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });


        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "Stop Service");
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "Start Foreground Service");
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "线程实现的三种方式");
                doThreads();
            }
        });


        // 第二种方式启动服务
        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = (String) ((Button) findViewById(R.id.button4)).getText();
                Log.d("Che", "Client " + info);

                boundClick(view);
            }
        });
    }

    // 客户端绑定代码部分
    public void boundClick(View view) {
        Intent intent = new Intent(this, MyService.class);
        if (!bound) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE); // 客户端触发服务开启
            ((Button) findViewById(R.id.button4)).setText("Unbind");
        } else {
            unbindService(connection);
            ((Button) findViewById(R.id.button4)).setText("Bind");
        }
    }

    public void doThreads() {
        // 1. 常规方式使用线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Che", "One - Got new thread: " + Thread.currentThread().getId());
            }
        }).start();

        // 2. 处理器方式使用线程
        Message message = Message.obtain();
        message.what = 1;
        message.obj = null;

        TestHandler mh = new TestHandler();  // 消息处理在新的线程中进行
        mh.sendMessage(message); // 发送消息

        // 3. 异步任务
        new TestTask().execute();
    }

    // thread method2
    class TestHandler extends Handler { // 但是为何它的Thread-ID总是2?
        @Override
        public void handleMessage(@NonNull Message msg) { // 线程处理逻辑,有点类似队列, 但是是独立的线程
            switch (msg.what) {
                case 1:
                    Log.d("Che", "Got message => " + msg.what);
                default:
                    Log.d("Che", "Two - Handler thread: " + Thread.currentThread().getId());
            }
            super.handleMessage(msg);
        }
    }

    // thread method3
    class TestTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d("Che", "Three - AsyncTask thread id: " + Thread.currentThread().getId());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    /*
    // AIDL方法调用
    private ServiceConnection mAIDLConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            try {
                IMyAidlInterface.Stub.asInterface(service).basicTypes(0, 0, true, 1, 1, "aa");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
     */

}