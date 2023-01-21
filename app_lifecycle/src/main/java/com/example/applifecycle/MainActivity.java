package com.example.applifecycle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.dumpStack();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context c = getApplicationContext();
        //testFunc(c, "/sdcard/3.dex");
        //testActivity(c);

        checkClassLoader(c.getClassLoader());
    }

    // 动态加载dex中的方法
    public static void testFunc(Context context, String dexpath) {
        DexClassLoader dexClassLoader = new DexClassLoader(dexpath, null, null, MainActivity.class.getClassLoader());
        try {
            Class<?> Test = dexClassLoader.loadClass("com.example.dexplugin.TestClass");
            Method m = Test.getDeclaredMethod("testFunc");
            Object o = Test.newInstance();
            m.invoke(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // 动态加载dex中的组件
    public static void testActivity(Context context) {
        ClassLoader dexClassLoader = context.getClassLoader();
        checkClassLoader(dexClassLoader);

        try {
            Class<?> Test = dexClassLoader.loadClass("com.example.dexplugin.TestActivity");
            context.startActivity(new Intent(context, Test));

//            Method m = Test.getDeclaredMethod("onCreate"); // 没有这个方法?
//            Object o = Test.newInstance();
//            m.invoke(o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 验证双亲委派模型
    public static void checkClassLoader(ClassLoader c) {
        // 验证ClassLoader的双亲模式
        ClassLoader thisClassLoader = c;
        Log.i("Che", "app:" + thisClassLoader);
        ClassLoader parentClassLoader = null;
        parentClassLoader = thisClassLoader.getParent();

        while (parentClassLoader != null) {
            Log.d("Che", "this:" + thisClassLoader + " ---parent: " + parentClassLoader);
            thisClassLoader = parentClassLoader;
            parentClassLoader = thisClassLoader.getParent();
        }
        Log.d("Che", "root:" + thisClassLoader);

    }
}
