package com.example.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static {
        System.loadLibrary("native-lib");
    }

    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Che", "Dynamic Receiver Start!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

        // 动态注册receiver
        IntentFilter i = new IntentFilter("com.example.hello.open");
        i.addCategory(Intent.CATEGORY_DEFAULT);
        getApplicationContext().registerReceiver(mReceiver, i, "com.example.hello.Permission3", null);
    }

    public void NoPermActivityClick(View view) {
        Intent intent = new Intent(MainActivity.this, NoPermActivity.class);
        startActivity(intent);
    }

    public void PermActivityClick(View view) {
        Intent intent = new Intent(MainActivity.this, PermActivity.class);
        startActivity(intent);
    }

    public void PermReceiverClick(View view) {
        Intent intent = new Intent(MainActivity.this, PermReceiver.class);
        sendBroadcast(intent);
    }

    public void DynamicPermReceiverClick(View view) {
        Intent intent = new Intent("com.example.hello.open");
        sendBroadcast(intent);
    }

    // 这个组件的权限掌握的不熟练!
    public void GrantUriPermClick(View view) {
        Intent intent = new Intent();
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        //noinspection deprecation
        startActivityForResult(intent, 0);
    }

    public void DangerActivityClick(View view) {
        Intent intent = new Intent(MainActivity.this, DangerActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Che", "result: " + resultCode);

        if (data != null) {
            Log.d("Che", data.getDataString());
        } else {
            Log.d("Che", "data is null");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public native String stringFromJNI();


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Log.d("Che", "NoPermActivity");
                NoPermActivityClick(view);
                break;

            case R.id.button2:
                Log.d("Che", "PermActivity");
                PermActivityClick(view);
                break;

            case R.id.button3:
                Log.d("Che", "PermReceiver");
                PermReceiverClick(view);
                break;

            case R.id.button4:
                Log.d("Che", "Dynamic Receiver");
                DynamicPermReceiverClick(view); // 应用内也要声明使用权限
                break;

            case R.id.button5:
                Log.d("Che", "Grant URI Perm");
                GrantUriPermClick(view);
                break;

            case R.id.button6:
                Log.d("Che", "Dangerous Activity");
                DangerActivityClick(view);
                break;
        }
    }
}