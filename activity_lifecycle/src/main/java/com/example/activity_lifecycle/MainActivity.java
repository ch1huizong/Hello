package com.example.activity_lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
    final static String SAVE_STRING = "Save TEST";

    // Used to load the 'native-lib' library on application startup.
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

        /*
        String name = getIntent().getStringExtra("name"); // 没显示啊?
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();

        String d = runCommandWithRoot("ls -l /");
        Log.d("Che", "TMDDDD: \n" + d);
        Utils.MLog("Che", d);
        */

        if (savedInstanceState != null) {
            Log.d("Che", savedInstanceState.getString(SAVE_STRING));
        }

        Log.d("Che", "MainActivity onCreate");
        Log.d("Che", "MainActivity Task ID - " + getTaskId());

        // new activity
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
            }
        });


        // getTaskId 或者 ActivityManager.getAppTasks来获取当前Tasks信息(dumpsys activity activities)


        // call itself
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,MainActivity.class));

                // 启动新的Task，MainActivity实例增多
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
                startActivity(intent);

            }
        });

        // singletop, 不启动新的Task，MainActivity实例数量不变
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SingleTopActivity.class));
            }
        });

        // Finish
        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Finish And Remove Recents
        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAndRemoveTask();
            }
        });

        // 进程间通信通过Intent, Bundle, Parcelable(接口), Parcel(数据结构)
        Button btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel://10086"));
                startActivityForResult(intent, 1);
            }
        });

    }


    // Activity生命周期研究
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Che", "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Che", "MainActivity onResume");
        Log.d("Che", "MainActivity is Running..........");
        Thread.dumpStack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Che", "MainActivity onPause");
    }

    // 介于onPause - onStop之间, 前提是系统调度造成的状态切换，才会有保存(VS BACK)
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


    // native方法调用
    public int ret1() {
        return 1;
    }

    public native String stringFromJNI();

    // root方法执行
    public String runCommandWithRoot(String cmd) {
        /*
        StringBuffer data = null;
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(p.getOutputStream());
            DataInputStream in = new DataInputStream(p.getInputStream());

            out.writeBytes(cmd + "\n");
            out.writeBytes("exit\n");
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = null;
            p.waitFor();

            while ((line = reader.readLine()) != null) {
                Log.d("Che", "Line:" + line);
                data.append(line);
            }

            reader.close();
            Log.d("Che", "Rooted! Result -> " + data.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Che", "No root!");
        } finally {

        }
         */


        try {
            Process process = Runtime.getRuntime().exec("ps -e");
            InputStreamReader reader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(reader);
            int numRead;
            char[] buffer = new char[5000];
            StringBuffer commandOutput = new StringBuffer();
            while ((numRead = bufferedReader.read(buffer)) > 0) {
                commandOutput.append(buffer, 0, numRead);
            }
            bufferedReader.close();
            //process.waitFor();

            return commandOutput.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}