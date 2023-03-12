package com.example.applifecycle;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;


public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        Log.d("Che", "MyApplication attachBaseContext called!");
//        Thread.dumpStack();


        if (base != null) {
            Log.d("Che", "base is not null");
        }

        try {
            // 获得ActivityThread类
            final Class<?> cActivityThread = Class.forName("android.app.ActivityThread");

            // 获取的ActivityThread实例
            final Method m_currentActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread");
            Object obj_currentActivityThread = m_currentActivityThread.invoke(null);
            Log.d("Che", "currentActivityThread is " + (obj_currentActivityThread == null ? "null" : "not null"));

            // 包名
            final Method m_currentPackageName = cActivityThread.getDeclaredMethod("currentPackageName");
            Log.d("Che", "currentPackageName " + m_currentPackageName.invoke(null));

            // 进程名
            final Method m_currentProcessName = cActivityThread.getDeclaredMethod("currentProcessName");
            Log.d("Che", "currentProcessName " + m_currentProcessName.invoke(null));

            //  mInitialApplication
            final Method m_currentApplication = cActivityThread.getDeclaredMethod("currentApplication");
            Object obj_currentApplication = m_currentApplication.invoke(null);
            Log.d("Che", "currentApplication is " + (obj_currentApplication == null ? "null" : "not null"));

            // 获得mPackages字段
            final Field m_Packages = cActivityThread.getDeclaredField("mPackages");
            m_Packages.setAccessible(true);
            Object obj_mPackages = m_Packages.get(obj_currentActivityThread);
            if (obj_mPackages != null) {
                Log.d("Che", "mPackages:");
                ArrayMap<String, WeakReference<Object>> obj_mPackages_raw = (ArrayMap<String, WeakReference<Object>>) (obj_mPackages);
                for (String i : obj_mPackages_raw.keySet()) {
                    Log.d("Che", "\t" + i);
                }

                // 获得LoadedApk类
                final Class<?> cLoadedApk = Class.forName("android.app.LoadedApk");
                Object obj_LoadedApk = ((WeakReference<Object>) obj_mPackages_raw.valueAt(0)).get();

                //      得到LoadedApk类的mclassLoader字段
                Field m_ClassLoader = cLoadedApk.getDeclaredField("mClassLoader");
                m_ClassLoader.setAccessible(true);

                //      加固关键点, 替换classloader
                MyClassLoader mLoader = new MyClassLoader(); // 构造自定义的ClassLoader
                //      拿到LoadedApk实例的ClassLoader对象
                ClassLoader cl_orgin = (ClassLoader) m_ClassLoader.get(obj_LoadedApk);

                mLoader.setBaseClassLoader(cl_orgin); // 代理功能
                //      替换, LoadedApk实例替换成新的ClassLoader对象
                m_ClassLoader.set(obj_LoadedApk, mLoader);

                Field myParent = ClassLoader.class.getDeclaredField("parent");
                myParent.setAccessible(true);
                myParent.set(mLoader, cl_orgin); // 构建双亲委派关系


                //      从LoadedApk里拿到application信息
                Field m_Application = cLoadedApk.getDeclaredField("mApplication");
                m_Application.setAccessible(true);
                // 此时LoadedApk里包裹的application也是空
                Log.d("Che", "LoadedApk mApplication is " + (m_Application.get(obj_LoadedApk) == null ? "null" : "not null"));


                final Class<?> cBaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
                final Method m_path = cBaseDexClassLoader.getDeclaredMethod("getLdLibraryPath");
                String path = (String) m_path.invoke(cl_orgin);
                Log.d("Che", "应用本身的Cloader包含的LdLibraryPath is => " + path);
            } else {
                Log.d("Che", "mPackages is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.attachBaseContext(base);
    }

    // todo: 实现一下fix Applicaiton
    @Override
    public void onCreate() {

        // 承接上一步

        // 这里主要是生成正确的Application，并设置LoadedApk.mApplication参数

        Log.d("Che", "MyApplication onCreate called!");
//        Thread.dumpStack();

        super.onCreate();

        BaseDexClassLoader clBase = (BaseDexClassLoader) ClassLoader.getSystemClassLoader();
        try {
            final Class<?> cBaseDexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            final Method m_path1 = cBaseDexClassLoader.getDeclaredMethod("getLdLibraryPath");
            String path = (String) m_path1.invoke(clBase);
            Log.d("Che", "系统Cloader包含的LdLibraryPath is => " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
