package com.example.applifecycle;

import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

// todo: 这两个方法中要设置的东西? 准确移交控制权？
public class MyApplication1 extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        try {
            final Class<?> cActivityThread = Class.forName("android.app.ActivityThread");
            final Method m_currentActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread");
            Object obj_currentActivityThread = m_currentActivityThread.invoke(null);

            final Field m_Packages = cActivityThread.getDeclaredField("mPackages");
            m_Packages.setAccessible(true);
            Object obj_mPackages = m_Packages.get(obj_currentActivityThread);

            if (obj_mPackages != null) {
                ArrayMap<String, WeakReference<Object>> obj_mPackages_raw = (ArrayMap<String, WeakReference<Object>>) (obj_mPackages);
                final Class<?> cLoadedApk = Class.forName("android.app.LoadedApk");
                Object obj_LoadedApk = ((WeakReference<Object>) obj_mPackages_raw.valueAt(0)).get();
                Field m_ClassLoader = cLoadedApk.getDeclaredField("mClassLoader"); // 得到LoadedApk对象里面的classLoader
                m_ClassLoader.setAccessible(true);
                ClassLoader cl_orgin = (ClassLoader) m_ClassLoader.get(obj_LoadedApk); // 原始的ClassLoader对象

                Log.d("Che", cl_orgin == base.getClassLoader() ? "同一个ClassLoader!" : "不相同!");

                // base里面的ClassLoader? 注意跟踪一下Context的传递问题
                DexClassLoader mLoader1 = new MyClassLoader1(base, "/sdcard/3.dex").getMyClassLoader(); // 构造自定义的ClassLoader

                // 设置
                m_ClassLoader.set(obj_LoadedApk, mLoader1);
            } else {
                Log.d("Che", "mPackages is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        Log.d("Che", "MyApplication1 onCreate called!");
        super.onCreate();
    }
}