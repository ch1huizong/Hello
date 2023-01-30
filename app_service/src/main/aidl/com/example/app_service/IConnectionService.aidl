// IConnectionService.aidl
package com.example.app_service;

// Declare any non-default types here with import statements

interface IConnectionService {

    oneway void connect();

    void disconnect();

    boolean isConnected();
}
