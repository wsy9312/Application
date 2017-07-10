package com.example.hgtxxgl.application.network;

/**
 * Created by ting on 15/6/19.
 */

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


public class RetrofitUtils {

    private static RestAdapter singleton;
    private static OkHttpClient client;

    public static <T> T createApi(Class<T> clazz) {
        if (client == null) {
            throw new RuntimeException("error init network api");
        }
        if (singleton == null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null) {
                    RestAdapter.Builder builder = new RestAdapter.Builder();
                    builder.setEndpoint(Config.getServer());//设置远程地址
                    builder.setConverter(new GsonConverter(GsonUtils.newInstance()));
                    builder.setClient(new OkClient(client));
                    builder.setLogLevel(Config.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
                    singleton = builder.build();
                }
            }
        }
        return singleton.create(clazz);
    }

    public static void init(Context context) {
        client = OkHttpUtils.getInstance(context);
    }
    public static void reset() {
        singleton = null;
    }
}