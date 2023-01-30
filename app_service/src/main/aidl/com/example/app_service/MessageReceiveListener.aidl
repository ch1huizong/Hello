// MessageReceiveListener.aidl
package com.example.app_service;

import com.example.app_service.entity.Message;


interface MessageReceiveListener {

    void onReceiveMessage(in Message message);
}
