package com.example.applifecycle;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MyClassLoader extends ClassLoader {

    public ClassLoader mBase;

    public void setBaseClassLoader(ClassLoader base) {
        mBase = base;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Log.d("Che", "== MyClassLoader findClass " + name);
        Class<?> cClassLoader = Class.forName("ClassLoader");
        try {
            Method m_findClass = cClassLoader.getDeclaredMethod("findClass", String.class);
            return (Class<?>) m_findClass.invoke(mBase, name); // 反射调用findClass
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return mBase.findClass(name); 没这个api?

        return null;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Log.d("Che", "=== MyClassLoader loadClass " + name);
        return mBase.loadClass(name);
    }
}