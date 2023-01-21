package com.example.proguard.ndkreverse;

public class Lesson {
    static {
        System.loadLibrary("lesson");
    }

    public native void main();

    public native String GetLessonName();

    public native void SetLessonName(String name);
}
