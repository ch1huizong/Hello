package com.example.frida_demo1;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static String total = "@@@===@@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int m = fun(50, 30);
            Log.d("Che", String.valueOf(m));
            Log.d("Che", fun("get A LowerCASE MESSAGE"));
        }
    }

    public int fun(int x, int y) {
        return x + y;
    }

    public String fun(String x) { // 重载方法
        total += x;
        return x.toLowerCase();
    }

    private String secret() {
        return total;
    }

    private static String secret2() {
        return total;
    }

}
