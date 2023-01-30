// IServiceManager.aidl
package com.example.app_service;

interface IServiceManager {

    IBinder getService(String serviceName);
}
