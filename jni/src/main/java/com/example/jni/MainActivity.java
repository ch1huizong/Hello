package com.example.jni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("jni-lib");
    }

    static String[] strings = {"apple", "pear", "banana"};

    // todo:onCreate Native化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animal animal = new Animal("animal");

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // jni基础类型
                Log.d("Che", "callNativeInt: " + callNativeInt(7));
                Log.d("Che", "callNativeByte: " + callNativeByte((byte) 2));
                Log.d("Che", "callNativeBool: " + callNativeBool(true));
                Log.d("Che", "callNativeChar: " + callNativeChar('A'));

                // jni字符串类型
                Log.d("Che", "callNativeString: " + callNativeString("Welcome to china!"));
                stringMethod("Welcome!");

                // jni字符串数组(对象数组)
                Log.d("Che", "callNativeStringArray: " + callNativeStringArray(strings));


                // jni实例属性和类属性
                accessInstanceField(animal);
                Log.d("Che", "name is " + animal.getName());
                accessStaticField(animal);
                Log.d("Che", "num is " + Animal.getNum());

                // jni实例方法和静态方法
                accessInstanceMethod(animal);
                accessStaticMethod(animal);

                // jni中回调接口里的方法
                nativeCallBack(new ICallBackMethod() {
                    @Override
                    public void callback() {
                        Log.d("Che", "[ICallBackMethod] thread name is " + Thread.currentThread().getName());
                    }
                });

                // jni子线程里回调接口方法
                nativeThreadCallBack(new IThreadCallBack() {
                    @Override
                    public void callback() {
                        Log.d("Che", "[IThreadCallBack] thread name is " + Thread.currentThread().getName());
                    }
                });

                // jni调用类构造函数 - 两种方法
                Log.d("Che", "My name is " + invokeAnimalConstructor().getName());
                Log.d("Che", "My name is " + allocObjectConstructor().getName());

                // jni引用管理
                Log.d("Che", "Got: " + errorCacheLocalRef());
                Log.d("Che", "Got: " + cacheWithGlobalRef());
                useWeakGlobalRef();

                // jni异常处理
                nativeInvokeJavaException(MainActivity.this); // java代码本身有异常
                try {
                    nativeThrowException(); // native向java抛出异常
                } catch (IllegalArgumentException e) {
                    Log.e("Che", e.getMessage());
                }

//                // 线程的创建和等待
//                createNativeThread();
//                createNativeThreadWithArgs();
//                joinNativeThread();
//
//                try {
//                    waitNativeThread();
//                    Thread.sleep(3000);
//                    notifyNativeThread();
//                } catch (Exception e) {
//                }
//
//                startProductAndConsumerThread();
            }
        });
    }


    // jni基础类型
    public static native int callNativeInt(int a);
    public static native byte callNativeByte(byte a);
    public static native boolean callNativeBool(boolean a);
    public static native char callNativeChar(char a);

    // jni字符串类型
    public static native String callNativeString(String str);
    public static native void stringMethod(String str);

    // jni数组-字符串
    public static native String callNativeStringArray(String[] arr);

    // jni访问字段
    public static native void accessInstanceField(Animal a);
    public static native void accessStaticField(Animal a);

    // jni访问类的实例方法和类方法
    public static native void accessInstanceMethod(Animal a);
    public static native void accessStaticMethod(Animal a);

    // 传递接口
    public static native void nativeCallBack(ICallBackMethod c);
    public static native void nativeThreadCallBack(IThreadCallBack c); // 线程里运行特定方法

    // jni访问类的构造方法并返回类实例
    public static native Animal allocObjectConstructor();
    public static native Animal invokeAnimalConstructor();


    // jni全局、局部、弱引用使用
    public static native String errorCacheLocalRef();
    public static native String cacheWithGlobalRef();
    public static native void useWeakGlobalRef();

    // jni异常处理
    public native static void nativeInvokeJavaException(MainActivity m);
    public native static void nativeThrowException() throws IllegalArgumentException;

    private int operation() {
        return 2 / 0;
    }


    // jni线程操作
    public static native void createNativeThread();
    public static native void createNativeThreadWithArgs();
    public static native void joinNativeThread();

    // jni线程同步操作
    public static native void waitNativeThread();
    public static native void notifyNativeThread();

    // 生产者和消费者模型待增加
    public static native void startProductAndConsumerThread();
}