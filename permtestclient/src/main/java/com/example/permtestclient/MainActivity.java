package com.example.permtestclient;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 启动hello中的带有权限的Activity
        Button button = findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("com.example.hello", "com.example.hello.PermActivity");
                        startActivity(intent);
                    }
                });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("com.example.hello", "com.example.hello.PermReceiver");
                        sendBroadcast(intent); // 启动另一个应用的Receiver
                    }
                });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("com.example.hello.open");
                        sendBroadcast(intent);
                    }
                });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        DangerActivityClick(view);
                    }
                });
    }

    // 对于带有danger权限的组件，当另一个应用申请打开的时候，有一个用户确认的过程, 在申请端需要检查确认结果
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void DangerActivityClick(View view) {
        if (checkSelfPermission("com.example.hello.Permission4") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"com.example.hello.Permission4"}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setClassName("com.example.hello", "com.example.hello.DangerActivity");

                    startActivity(intent);
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
