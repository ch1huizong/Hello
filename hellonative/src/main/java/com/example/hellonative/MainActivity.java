package com.example.hellonative;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.os.Build;

import java.io.OutputStream;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("static-lib");
        System.loadLibrary("dynamic-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String brand = Build.BRAND;
        String id = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("Che", "Build -> :" + brand + ", System.getString -> :" + id);

        Button bt = findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = findViewById(R.id.sample_text);
                tv.setText(stringFromJNI());

                // 静态注册
                Log.d("Che", "Hello1:" + Hello1());
                Log.d("Che", "Hello2:" + String.valueOf(Hello2(10)));

                // 动态注册
                Log.d("Che", "Dyn_Hello3:" + String.valueOf(Dyn_Hello3(MainActivity.this, 11, 22)));
                Log.d("Che", "Dyn_Acc:" + String.valueOf(Dyn_Acc(10)));

                // 验证函数调用的参数传递
//                Log.d("Che", "TestFunCall: " + TestFunCall(1, 2, 3, 4, 5, 6));
//                TestCompile();

                // SVC指令穿透
                //Log.d("Che", "MySysCall is called ");
                //MySyscall();

                // 内联汇编
                //InlineAsm();

                // Hook部分 - inlineHook
                //tv.setText(toHooked("che"));
            }
        });

        // Hooked
        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startHook();
            }
        });
    }


    // 静态注册的方法
    public static native String stringFromJNI();
    public static native String Hello1();
    public native int Hello2(int a);

    // 动态注册的方法
    public native int Dyn_Hello3(MainActivity m, int a, int b);
    public native int Dyn_Acc(int a);

    // ARM函数调用和数据格式
    public native int TestFunCall(int a, int b, int c, int d, int e, int f);
    public native void TestCompile();

    // SVC， 内联汇编，dobby hook
    public static native void MySyscall();
    public static native void InlineAsm();

    public static native String toHooked(String name);
    public static native void startHook();
}
