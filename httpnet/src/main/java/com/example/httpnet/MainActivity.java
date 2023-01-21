package com.example.httpnet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Http.HttpUtils;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView img;
    private SendUrlTask task;

    private static final String address = "http://192.168.1.10:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        img = (ImageView) findViewById(R.id.img);
        task = new SendUrlTask("https://m.baidu.com");
        task.execute();

        // t = new SendUrlTask("https://p1.ssl.qhimg.com/t0167bf11e82f16975b.png");

        /*
        // json字符串的创建
        Log.d("Che", JsonOpts.jsonCreate());
        Log.d("Che", JsonOpts.jsonCreate1());

        // json字符串的不同解析(原生、Gson、FastJson)
        String r1 = JsonOpts.jsonCreate1();
        JsonOpts.jsonParseByNative(r1);

        String r = JsonOpts.jsonCreate();
        JsonOpts.jsonParseByGson(r);
        JsonOpts.jsonParseByFastJson(r);
         */

        // SimpleOkHttp
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url1 = address + "/books/";
                try {
                    String ret1 = SimpleOkHttp.get(url1);
                    Log.d("Che", ret1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String url1 = address + "/books/";
                JSONObject jo = new JSONObject();
                try {
                    jo.put("bookname", "OkHttp的使用");
                    jo.put("icon", "OkHttp");
                    jo.put("book_description", "网络请求工具");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    String ret2 = SimpleOkHttp.post(url1, jo.toString());
                    Log.d("Che", ret2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
         */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
    }

    private class SendUrlTask extends AsyncTask<Void, Void, String> {
        private String mUrl;

        public SendUrlTask(String url) {
            mUrl = url;
        }

        @Override
        protected String doInBackground(Void... voids) {
            // return HttpUtils.loadImage(mUrl);
            return HttpUtils.sendUrl(mUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBlockNetworkImage(false);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.loadDataWithBaseURL(null, s, "text/html", "utf8", null);
            Log.d("Che", "get data:" + s);
            //
            //            if (s != null) {
            //                img.setImageBitmap(s);
            //            }

        }
    }
}
