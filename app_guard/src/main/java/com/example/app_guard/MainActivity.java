package com.example.app_guard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_guard.ndkreverse.Lesson;


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

        Log.d("Che", name + " : " + position);
        Log.d("Che", "混淆模块开始启动");

        two = new ClassTwo();
        three = new ClassThree();
        v = new ClassView(this);
        lesson = new Lesson();

        try {
            //bundle.putParcelable("key", new Book());
            functionA(null); // 类内实例方法

            ClassOne.functionA(); // 静态方法

            two.functionB(); // 实例方法
            three.functionC();
            v.getView();

            lesson.main(); // native方法
            lesson.GetLessonName();
            lesson.SetLessonName("Python");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void functionA(View v) {

    }
}