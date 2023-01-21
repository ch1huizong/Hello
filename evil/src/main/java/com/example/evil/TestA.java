package com.example.evil;

import android.util.Log;

public class TestA {
    public static String name = "che";
    public static int age = 33;

    public static void print() {
        Log.d("Che", "TestA.print -> name:" + name + ",age:" + age);
    }
}
