package com.example.hgtxxgl.application.network;

import com.example.hgtxxgl.application.entity.LoginEntity;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;


/**
 * Created by ting on 15/12/5.
 */
public interface NetworkApi {

    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("/api/2.0")
    void postCeShiFromWJ(@FieldMap Map<String, Object> params, Callback<Response> cb);


//    @Headers("Content-Type: application/json;charset=UTF-8")
//    @FormUrlEncoded
//    @POST("/api/2.0")
//    void loginFromWJ(@Query("json") String json, Callback<Response> cb);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/2.0")
    void postFlyRoute(@Body RequestBody route , Callback<LoginEntity> call);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/api/2.0")
    void postString(@Body LoginEntity loginEntity , Callback<Response> call);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @FormUrlEncoded
    @POST("/api/2.0")
    void postString1(@Field("") String login, Callback<Response> call);
}
