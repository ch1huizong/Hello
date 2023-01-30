// IMessageService.aidl
package com.example.app_service;

import com.example.app_service.entity.Message;
import com.example.app_service.MessageReceiveListener;

// 消息服务
interface IMessageService {

    void sendMessage(in Message message);

    void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener);

    void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener);
}
