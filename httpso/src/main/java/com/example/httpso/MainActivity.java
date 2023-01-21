package com.example.httpso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = findViewById(R.id.textView);
                try {
                    InetAddress address = NetUtils.getLocalIPAddress();
                    if(address != null){
                        tv.setText(address.getHostAddress());
                    }else{
                        Log.d("Che", "未获取到IP地址");
                        tv.setText("未获取到IP地址");
                    }
                } catch (Exception e) {
                    Log.d("Che", "出问题了");
                    e.printStackTrace();
                }

                Intent it1 = new Intent(MainActivity.this, MyService.class);
                startService(it1);
                btn.setText("服务已开启");
            }
        });

    }
}