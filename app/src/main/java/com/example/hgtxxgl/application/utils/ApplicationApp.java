package com.example.hgtxxgl.application.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;

import com.example.hgtxxgl.application.network.RetrofitUtils;


public class ApplicationApp extends Application {
    //handler
    public static Handler mainHandler;

    public static Context context;

    private String UPLOAD_URL="http:";

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitUtils.init(this);
        mainHandler = new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

//        //upload logfile , post params.
//        HttpParameters params = new HttpParameters();
//        params.add("key1", "value1");
//        params.add("key2", "value2");
//        params.add("key3", "value3");
//        //.......
//        //replace your key and value;
//
//        boolean isDebug = true;
//        LogCollector.setDebugMode(isDebug);
//        LogCollector.init(getApplicationContext(), UPLOAD_URL, params);//params can be null
    }

//        checkFirstIn();


//    private void checkFirstIn() {
//        CommonValues.firstIn = getSharedPreferences("app", MODE_PRIVATE).getBoolean("firstIn", true);
//    }



}