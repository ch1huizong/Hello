package com.example.app_guard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_guard.log.Logger;
import com.example.app_guard.ndkreverse.Lesson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// 代码混淆案例
public class MainActivity extends AppCompatActivity {
    private ClassTwo two;
    private ClassThree three;
    private ClassView v;
    private Lesson lesson;

    private Bundle bundle;

    private String name = "CheHuiZong";
    private String position = "Python Programmer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.d("Che", "MainActivity onCreate");

        Log.d("Che", name + " => " + position);

        two = new ClassTwo();
        three = new ClassThree();
        v = new ClassView(this);
        lesson = new Lesson();

        try {
            //bundle.putParcelable("key", new Book("Python"));
            functionA(null); // 类内实例方法
            Log.d("Che", functionB("onCreate1"));

            ClassOne.functionA(); // 静态方法

            two.functionB(); // 实例方法
            three.functionC();
            v.getView();

            lesson.main(); // native方法
            lesson.GetLessonName();
            lesson.SetLessonName("Python");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Che", "出错了?");
        }
    }

    public void functionA(View v) {

    }

    public String functionB(String message) throws NoSuchAlgorithmException {
        String data =  message + " calling funtionB";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes());
        BigInteger bigInteger = new BigInteger(1, md5.digest());

        return bigInteger.toString(16);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Che", "MainActivity onDestroy");
    }
}