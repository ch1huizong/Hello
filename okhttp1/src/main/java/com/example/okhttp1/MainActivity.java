package com.example.okhttp1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;

    private String url1 = "https://httpbin.org/get";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);

    }

    public void doGet(View view, String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url1).build();

        Call call = client.newCall(request);

        // 执行call, 同步
        //Response response = call.execute();


        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("Che", "onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String rp = response.body().string();
                Log.e("Che", "onResponse: " + rp);
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Log.d("Che", "doGet method called!");
                try {
                    doGet(view, url1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.button2:
                Log.d("Che", "doGet Method");
                break;

            case R.id.button3:
                Log.d("Che", "doPost Method");
                break;

            case R.id.button4:
                Log.d("Che", "PostFile Method");
                break;

            case R.id.button5:
                Log.d("Che", "Post Method");
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}