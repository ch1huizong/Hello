package com.example.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/app-biz-svc/user/login")
    @FormUrlEncoded
    Call<Result> Login(@Field("token") String token);
}
