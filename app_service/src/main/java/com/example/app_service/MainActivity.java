package com.example.app_service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
//import android.os.Message;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_service.entity.Message;


// 服务种类:
// 1. Scheduled服务
// 2. Started服务, 与启动组件无关
// 3. Bound服务, 当bind的服务的组件都解绑时，服务消亡
//
// Bound服务注意点: c-s形式的服务, 需要返回IBinder接口!
//      1. 当c-s端在一个进程中的时候， IBinder可以直接继承Binder
//      2. 不在同一进程, 通过AIDL或Messenger通信


// 服务的客户端代码部分
public class MainActivity extends AppCompatActivity {

    private IConnectionService connectionServiceProxy;
    private IMessageService messageServiceProxy;
    private IServiceManager serviceManagerProxy;

    // 使用Messenger进程间通信
    private Messenger messengerProxy;

    private Handler handler_client = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            data.setClassLoader(Message.class.getClassLoader());
            Message message = data.getParcelable("message");
            handler_client.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    };
    private Messenger messengerClient = new Messenger(handler_client);

    // 反向使用
    private MessageReceiveListener messageReceiveListener = new MessageReceiveListener.Stub() {

        @Override
        public void onReceiveMessage(Message message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private boolean bound; // 绑定了与否的标志
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bound = true;
            Log.d("Che", "onServiceConnected");

            MyService.MyBinder binder = (MyService.MyBinder) iBinder; // 通过返回的binder，可以向内或向外 调用指定的功能
            binder.getService().testToCall(); // 这里是服务的功能调用
            Log.d("Che", "Service - add: " + binder.add(1, 2));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("Che", "onServiceDisconnected");
            bound = false;
        }
    };

    public void boundClick(View view) {
        Intent intent = new Intent(this, MyService.class);
        if (!bound) {
            bindService(intent, connection, Context.BIND_AUTO_CREATE); // 客户端发送请求, 立即返回; 后续处理爱connection中完成
            ((Button) findViewById(R.id.button4)).setText("Unbind");
        } else {
            unbindService(connection);
            ((Button) findViewById(R.id.button4)).setText("Bind");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // startService => 第一种方式启动服务
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

        // 前台服务
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "Start Foreground Service");
                startService(new Intent(MainActivity.this, MyService.class));
            }
        });

        // bindService => 第二种方式启动服务
        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = (String) ((Button) findViewById(R.id.button4)).getText();
                Log.d("Che", "Client " + info);
                boundClick(view);
            }
        });

        // 线程间通信
        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Che", "线程实现的三种方式");
                doThreads();
            }
        });

        // 动态注册broadcast receiver, 发挥作用了
        IntentFilter v = new IntentFilter();
        v.addAction("com.example.app_service.myaction");
        this.getApplicationContext().registerReceiver(new MyReceiver(), v); // context分是app的还是activity的!


        ////////////////////////////////////////////////////////////
        //
        // 启动子进程, 进程间通信AIDL
        //
        ////////////////////////////////////////////////////////////

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() { // 开启服务，设置回调

            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                try {
                    serviceManagerProxy = IServiceManager.Stub.asInterface(iBinder); // 获取到的是manager自己的binder, 得到连接
                    connectionServiceProxy = IConnectionService.Stub.asInterface(serviceManagerProxy.getService(IConnectionService.class.getSimpleName()));
                    messageServiceProxy = IMessageService.Stub.asInterface(serviceManagerProxy.getService(IMessageService.class.getSimpleName()));
                    messengerProxy = new Messenger(serviceManagerProxy.getService(Messenger.class.getSimpleName()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

        Button btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connectionServiceProxy.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn6 = findViewById(R.id.button6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    connectionServiceProxy.disconnect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn7 = findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = false;
                try {
                    isConnected = connectionServiceProxy.isConnected();
                    Log.d("Che", "isConnected => " + isConnected);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn8 = findViewById(R.id.button8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message msg = new Message();
                    msg.setContent("message from main client");
                    messageServiceProxy.sendMessage(msg);
                    Log.d("Che", "message.isSendSuccess => " + msg.isSendSuccess()+ ", content =>" + msg.getContent());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn9 = findViewById(R.id.button9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 会传递一个listener
                    messageServiceProxy.registerMessageReceiveListener(messageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });


        Button btn10 = findViewById(R.id.button10);
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    messageServiceProxy.unRegisterMessageReceiveListener(messageReceiveListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn_send_by_messenger = findViewById(R.id.btn_send_by_messenger);
        btn_send_by_messenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message message = new Message();
                    message.setContent("message from main by Messenger");

                    android.os.Message data = new android.os.Message();
                    data.replyTo = messengerClient;

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("message", message);
                    data.setData(bundle);
                    messengerProxy.send(data);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////
    //
    //              线程部分使用实例
    //
    ////////////////////////////////////////////////////////////

    public void doThreads() {
        // 1. 常规方式使用线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Che", "One - Got new thread: " + Thread.currentThread().getId());
            }
        }).start();

        // 2. 处理器方式使用线程
//        Message message = Message.obtain();
//        message.what = 1;
//        message.obj = null;
//
//        TestHandler mh = new TestHandler();  // 消息处理在新的线程中进行
//        mh.sendMessage(message); // 发送消息

        // 3. 异步任务
        new TestTask().execute();
    }

    // thread method2
//    class TestHandler extends Handler { // 但是为何它的Thread-ID总是2?
//        @Override
//        public void handleMessage(@NonNull Message msg) { // 线程处理逻辑,有点类似队列, 但是是独立的线程
//            switch (msg.what) {
//                case 1:
//                    Log.d("Che", "Got message => " + msg.what);
//                default:
//                    Log.d("Che", "Two - Handler thread: " + Thread.currentThread().getId());
//            }
//            super.handleMessage(msg);
//        }
//    }

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
}
