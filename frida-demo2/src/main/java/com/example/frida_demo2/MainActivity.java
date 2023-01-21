package com.example.frida_demo2;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username = this.findViewById(R.id.username);
        final EditText password = this.findViewById(R.id.password);
        final TextView msg = this.findViewById(R.id.textView);
        final Button btn = this.findViewById(R.id.login);

        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (username.getText().toString().compareTo("admin") == 0) {
                            msg.setText("You cannot login as admin");
                            return;
                        }

                        msg.setText("Sending To The Server:" + Base64.encodeToString((username.getText().toString() + ":" + password.getText().toString()).getBytes(), Base64.DEFAULT));
                    }
                });
    }
}
