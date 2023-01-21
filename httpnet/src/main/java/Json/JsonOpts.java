package Json;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bluelinelabs.logansquare.LoganSquare;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonOpts {

    // Json字符串的构造 - 1
    public static String jsonCreate() {
        Course course = new Course();
        course.name = "http在安卓中的应用";
        course.author = "che";

        String[] arr = new String[]{"http基础", "json数据解析", "封装okhttp", "总结"};
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
            l.add(arr[i]);
        }
        course.content = l;

        TimeUnit t1 = new TimeUnit();
        t1.name = "All";
        t1.unit = "minute";
        t1.value = 200;

        TimeUnit t2 = new TimeUnit();
        t2.name = "http基础";
        t2.unit = "minute";
        t2.value = 60;

        TimeUnit t3 = new TimeUnit();
        t3.name = "json数据解析";
        t3.unit = "minute";
        t3.value = 50;

        CourseTime ct = new CourseTime();
        ct.total = t1;
        List<TimeUnit> ll = new ArrayList<TimeUnit>();
        ll.add(t2);
        ll.add(t3);
        ct.data = ll;

        course.time = ct;

        Gson gson = new Gson();
        String str = gson.toJson(course);
        return str;
    }

    // Json字符串的构造 - 2
    public static String jsonCreate1() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", "http在安卓中的应用");
            jo.put("author", "che");

            JSONArray ja = new JSONArray();
            ja.put("http基础");
            ja.put("json数据解析");
            jo.put("content", ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo.toString();
    }

    // Json字符串解析(原生)
    public static void jsonParseByNative(String str) {
        try {
            JSONObject jo = new JSONObject(str);
            String name = jo.optString("name");
            String author = jo.optString("author");
            JSONArray ja = jo.optJSONArray("content");
            Log.d("Che", "jsonParseByNative - " + "name=" + name + " array[1]=" + ja.get(1).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 利用Gson解析
    public static void jsonParseByGson(String str) {
        Gson gson = new Gson();
        Course c = gson.fromJson(str, Course.class); // 以Course模板格式解析
        Log.d("Che", "jsonParseByGson - " + "name=" + c.name + " array[0]=" + c.content.get(0));
    }

    // FastJson解析
    public static void jsonParseByFastJson(String str) {
        Course c = JSON.parseObject(str, Course.class);
        Log.d("Che", "jsonParseByFastJson - " + "name=" + c.name + " array[0]=" + c.content.get(0));
    }

    // LoganSquare解析
    public static void jsonParseByLogan(String str) {
        try {
            CourseInfo info = LoganSquare.parse(str, CourseInfo.class);
            Log.d("Che", "jsonParseByLogan - " + "name=" + info.name + " array[0]=" + info.content.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
