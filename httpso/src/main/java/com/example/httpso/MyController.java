package com.example.httpso;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.framework.body.JsonBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MyController {

    @GetMapping("/sign")
    public void post_sign(HttpRequest request, HttpResponse response) {
        String sign = request.getQuery("sign");
        Map<String, String> m = new HashMap<>();

        m.put("signature", new com.example.hellonative.MainActivity().Dyn_GetSign(sign));
        m.put("app", MyApp.getInstance().getApplicationInfo().packageName);
        JSONObject ob = new JSONObject(m);

        response.setBody(new JsonBody(ob));
    }
}