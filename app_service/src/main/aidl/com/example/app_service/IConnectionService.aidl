// IConnectionService.aidl
package com.example.app_service;

// 连接服务
interface IConnectionService {

    oneway void connect(); // 异步，不用等待处理返回值

    void disconnect();

    boolean isConnected();
}
