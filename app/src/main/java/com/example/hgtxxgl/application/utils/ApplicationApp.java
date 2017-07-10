package com.example.hgtxxgl.application.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import com.example.hgtxxgl.application.entity.LoginEntity;
import com.example.hgtxxgl.application.network.RetrofitUtils;


public class ApplicationApp extends Application {
    //handler
    public static Handler mainHandler;

    public static Context context;

    private static LoginEntity loginEntity;

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtils.init(this);
        mainHandler = new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
//        checkFirstIn();
    }

    public static LoginEntity getLoginEntity() {
        return loginEntity;
    }

    public static void setLoginEntity(LoginEntity loginEntity) {
        ApplicationApp.loginEntity = loginEntity;
    }

//    private void checkFirstIn() {
//        CommonValues.firstIn = getSharedPreferences("app", MODE_PRIVATE).getBoolean("firstIn", true);
//    }



}