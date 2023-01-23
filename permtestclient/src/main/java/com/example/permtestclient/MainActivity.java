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

        // 启动permissions中的带有权限的Activity
        Button button = findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("com.example.permissions", "com.example.permissions.PermActivity");
                        startActivity(intent);
                    }
                });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClassName("com.example.permissions", "com.example.permissions.PermReceiver");
                        sendBroadcast(intent); // 启动另一个应用的Receiver
                    }
                });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("com.example.permissions.action1");
                        sendBroadcast(intent, "com.example.permissions.Permission3");
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void DangerActivityClick(View view) {
        if (checkSelfPermission("com.example.permissions.Permission4") != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{"com.example.permissions.Permission4"}, 0); // 请求权限
        } else {
            Intent intent = new Intent();
            intent.setClassName("com.example.permissions", "com.example.permissions.DangerActivity");
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setClassName("com.example.permissions", "com.example.permissions.DangerActivity");

                    startActivity(intent);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
