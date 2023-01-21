package com.example.retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Login();
                    }
                });
    }

    public void Login() {
        Retrofit rf =
                new Retrofit.Builder()
                        .baseUrl(CONTANTS.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        LoginService ls = rf.create(LoginService.class);

        Call<Result> call = ls.Login("abbddfjfkjlkjf");

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.d("Che", "succcess: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}
