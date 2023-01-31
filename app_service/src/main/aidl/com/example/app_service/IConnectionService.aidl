// IConnectionService.aidl
package com.example.app_service;

// 连接服务
interface IConnectionService {

    oneway void connect();

    void disconnect();

    boolean isConnected();
}
