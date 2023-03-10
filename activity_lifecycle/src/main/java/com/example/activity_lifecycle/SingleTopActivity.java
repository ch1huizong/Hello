package com.example.activity_lifecycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SingleTopActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_top);
        Log.d("Che", " == SingleTop onCreate");
        Log.d("Che", " == SingleTopActivity ID - " + getTaskId());

        Button btn = findViewById(R.id.single_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SingleTopActivity.this, SingleTopActivity.class));
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("Che", "This is a new intent from SingleTopActivity !!! ");
    }
}