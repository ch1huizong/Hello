package com.example.activity_lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class NewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Log.d("Che", " == NewActivity Task ID - " + getTaskId());
    }
}