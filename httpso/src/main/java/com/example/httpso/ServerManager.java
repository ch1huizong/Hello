package com.example.httpso;

import android.content.Context;
import android.util.Log;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class ServerManager {

    private Server mServer;

    public ServerManager(Context context, InetAddress address, int port) {
        mServer = AndServer.webServer(context)
                .inetAddress(address)
                .port(port)
                .timeout(10, TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                    }

                    @Override
                    public void onStopped() {
                    }

                    @Override
                    public void onException(Exception e) {
                    }
                })
                .build();
    }

    public void startServer() {
        if (mServer.isRunning()) {
        } else {
            mServer.startup();
        }
    }

    public void stopServer() {
        if (mServer.isRunning()) {
            mServer.shutdown();
        } else {
            Log.w("AndServer", "The server has not started yet.");
        }
    }
}