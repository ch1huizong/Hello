package com.example.app_service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.app_service.entity.Message;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RemoteService extends Service {

    private boolean isConnected = false;

    // 使子进程的消息发送回主进程
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);

            // 处理消息
            Bundle data = msg.getData();
            data.setClassLoader(Message.class.getClassLoader());
            Message message = data.getParcelable("message");
            Messenger messengerClient = msg.replyTo;
            Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();

            // 发送响应
            try {
                Message reply = new Message();
                reply.setContent("message reply from remote");
                android.os.Message m = new android.os.Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable("message", reply);
                m.setData(bundle);
                messengerClient.send(m);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    //ArrayList<MessageReceiveListener> messageReceiveListener= new ArrayList<>();
    RemoteCallbackList<MessageReceiveListener> messageReceiveListenerRemoteCallbackList= new RemoteCallbackList<>();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture scheduledFuture;

    private Messenger messenger = new Messenger(handler);

    ////////////////////////////////////////////////////////////
    //
    //      开始实现服务接口
    //
    ////////////////////////////////////////////////////////////

    private IConnectionService connectionService = new IConnectionService.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                Thread.sleep(5000);
                isConnected = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });

                scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        int size = messageReceiveListenerRemoteCallbackList.beginBroadcast();
                        for (int i = 0; i < size ; i++) {
                            Message msg = new Message();
                            msg.setContent("<- This message from remote");
                            try {
                                messageReceiveListenerRemoteCallbackList.getBroadcastItem(i).onReceiveMessage(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        messageReceiveListenerRemoteCallbackList.finishBroadcast();
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isConnected = false;
            scheduledFuture.cancel(true); // 取消定时任务

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isConnected() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, String.valueOf(isConnected), Toast.LENGTH_SHORT).show();
                }
            });

            return isConnected;
        }
    };

    private IMessageService messageService = new IMessageService.Stub() {

        @Override
        public void sendMessage(Message message) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
            if (isConnected) {
                message.setSendSuccess(true);
            } else {
                message.setSendSuccess(false);
            }

            Log.d("Che", "remote got message => " + message.getContent());
        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            if (messageReceiveListener != null) {
                messageReceiveListenerRemoteCallbackList.register(messageReceiveListener);
            }
        }

        @Override
        public void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            if (messageReceiveListener != null) {
                messageReceiveListenerRemoteCallbackList.unregister(messageReceiveListener);
            }
        }
    };

    // 自己实现了一个服务管理器
    private IServiceManager serviceManager = new IServiceManager.Stub() {

        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            // 分发不同服务的binder
            if (IConnectionService.class.getSimpleName().equals(serviceName)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(serviceName)) {
                return messageService.asBinder();
            } else if(Messenger.class.getSimpleName().equals(serviceName)) {
                return messenger.getBinder();
            } else {
                return null;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder(); // 返回的是manager的binder
    }

    @Override
    public void onCreate() {
        super.onCreate();

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}
