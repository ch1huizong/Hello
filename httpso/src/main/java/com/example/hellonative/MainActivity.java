package com.example.hellonative;

import android.app.Activity;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("static-lib");
        System.loadLibrary("dynamic-lib");
        System.loadLibrary("jni-lib");
    }

    // 静态注册的方法
    public static native String Hello1();

    public static native String stringFromJNI();

    public static native void MySyscall();

    public static native void InlineAsm();

    public static native String toHooked(String name);

    public static native void startHook();

    public native String Hello2(int a);

    public native int TestFunCall(int a, int b, int c, int d, int e, int f);

    public native void TestC();

    // 动态注册的方法
    public native int Dyn_Hello3(MainActivity m, int a, int b);

    public native int Dyn_Acc(int a);

    public native String Dyn_GetSign(String s);

}
