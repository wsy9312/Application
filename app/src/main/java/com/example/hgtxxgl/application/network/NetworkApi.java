package com.example.hgtxxgl.application.network;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * Created by ting on 15/12/5.
 */
public interface NetworkApi {

    @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("/api/2.0")
    void postCeShiFromWJ(@FieldMap Map<String, Object> params, Callback<Response> cb);


    @Headers("Content-Type: application/json;charset=UTF-8")
    @FormUrlEncoded
    @POST("/api/2.0")
    void loginFromWJ(@Query("json") String json, Callback<Response> cb);

}
