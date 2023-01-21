package com.example.simplest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XHelpers;

////////////////////////////////////////////////////////////
//
// 魔改SandHook, hook自身
//
////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {

    public static void TestLog(int i) {
        Log.d("Che", "Test Log: " + i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XHelpers.findAndHookMethod(MainActivity.class, "TestLog", int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        int num = (int)param.args[0];

                        param.args[0] = 666;
                        Log.d("Che", "before TestLog Called!");
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        Log.d("Che", "after TestLog Called!");
                    }
                });

            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLog(1);
            }
        });
    }
}