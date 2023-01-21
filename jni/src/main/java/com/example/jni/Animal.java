package com.example.jni;

import android.util.Log;

public class Animal {

    public static int num = 0;
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public static int getNum() {
        return num;
    }

    public String getName() {
        return this.name;
    }


    //
    // C++中调用java实例方法和类方法
    //
    public void callInstanceMethod(int num) {
        Log.d("Che", "call instance method and num is " + num);
    }

    public static String callStaticMethod(String str) {
        if (str != null) {
            Log.d("Che", "call static method with " + str);
        } else {
            Log.d("Che", "call static method str is null");
        }
        return "";
    }

    public static String callStaticMethod(String[] strs, int num) {
        if (strs != null) {
            for (String str : strs) {
                Log.d("Che", "str in array is: " + str);
            }
        }
        return "";
    }

  }
