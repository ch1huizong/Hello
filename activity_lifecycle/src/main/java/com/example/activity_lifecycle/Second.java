package com.example.activity_lifecycle;

import android.app.Activity;
import android.os.Bundle;

public class Second extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
