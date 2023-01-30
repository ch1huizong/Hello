package com.example.app_service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.app_service.entity.Message;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RemoteService extends Service {

    private boolean isConnected = false;

    // 使子进程的消息发送回主进程
    private Handler handler = new Handler(Looper.getMainLooper());

    ArrayList<MessageReceiveListener> messageReceiveListenerArrayList = new ArrayList<>();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private ScheduledFuture scheduledFuture;

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
                        for (MessageReceiveListener l : messageReceiveListenerArrayList) {
                            Message msg = new Message();
                            msg.setContent("This message from remote");
                            try {
                                l.onReceiveMessage(msg);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
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

        }

        @Override
        public void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            if (messageReceiveListener != null) {
                messageReceiveListenerArrayList.add(messageReceiveListener);
            }

        }

        @Override
        public void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener) throws RemoteException {
            if (messageReceiveListener != null) {
                messageReceiveListenerArrayList.remove(messageReceiveListener);
            }
        }
    };

    private IServiceManager serviceManager = new IServiceManager.Stub() {

        @Override
        public IBinder getService(String serviceName) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(serviceName)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(serviceName)) {
                return messageService.asBinder();
            } else {
                return null;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }
}