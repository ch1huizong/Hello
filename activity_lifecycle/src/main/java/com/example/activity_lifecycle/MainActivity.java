package com.example.activity_lifecycle;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public class MainActivity extends Activity {
    final static String SAVE_STRING = "Save TEST";

    static {
        System.loadLibrary("native-lib");
    }

    // Activity生命周期
    // 注意： 状态切换时，状态、数据、数据库保存
    // Task: back stack, 用户交互的一系列Activity集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Thread.dumpStack();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            Log.d("Che", savedInstanceState.getString(SAVE_STRING));
        }

        Log.d("Che", "MainActivity onCreate");
        Log.d("Che", "MainActivity Task ID - " + getTaskId()); // getTaskId 或者 ActivityManager.getAppTasks来获取当前Tasks信息(dumpsys activity activities)

        // => New activity
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
            }
        });

        // => Call self
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 一直启动新的重复activity
                // startActivity(new Intent(MainActivity.this, MainActivity.class));

//                startActivity(new Intent(MainActivity.this, NewActivity.class));

                // 启动新的Task，MainActivity实例增多
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                startActivity(intent);
            }
        });

        // => single top, 回退栈上top activity是它， 则不启动新的了, 在这个回退栈上
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SingleTopActivity.class));
            }
        });

        // => Finish
        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // => Finish And Remove Recents
        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        // => 进程间通信通过Intent - 内部拥有一个Bundle
        // Bundle(后两者的组合)
        // Parcelable(接口-操作)
        // Parcel(数据结构-底层)
        Button btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            Log.d("Che", "打开并完成了电话拨号应用");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Che", "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Che", "MainActivity onResume");
        Log.d("Che", "MainActivity is Running ...");
        //Thread.dumpStack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Che", "MainActivity onPause");
    }

    // 介于onPause - onStop之间, 前提是系统调度造成的状态切换, 才会有保存? (VS BACK)
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("Che", "Save State"); // outState可保存状态
        Log.d("Che", "MainActivity onSaveInstanceState");

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Che", "MainActivity onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Che", "MainActivity onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Che", "MainActivity onDestroy");
        Log.d("Che", "Everything is Over!");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Che", "onRestoreInstanceState");
    }

    public int ret1() {
        return 1;
    }

    public native String stringFromJNI();
}
