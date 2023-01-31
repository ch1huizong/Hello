// IMessageService.aidl
package com.example.app_service;

import com.example.app_service.entity.Message;
import com.example.app_service.MessageReceiveListener;


// 消息服务
interface IMessageService {

    // 传递对象的状态一致, 客户端-服务器端是两个不同的对象

    // in:  C -> S, Server端状态变更，不会影响Client端口
    // inout: C <-> S, 两端一致， C端需要Parcel操作支持
    // out: C <- S, 服务端获取不到Client发送的消息的状态, new了一个新的对象进行传递
    void sendMessage(inout Message message);

    void registerMessageReceiveListener(MessageReceiveListener messageReceiveListener);

    void unRegisterMessageReceiveListener(MessageReceiveListener messageReceiveListener);
}
