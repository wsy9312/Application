package com.example.hgtxxgl.application.utils.hand;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.LoginInfoBean;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationApp extends Application {
    //handler
    public static Handler mainHandler;

    public static Context context;

    private String UPLOAD_URL="http:";

    private static PeopleInfoEntity peopleInfoEntity;

    private static LoginInfoBean newLoginEntity;

    public static List<?> images=new ArrayList<>();
    public static int H,W;

    public static String getIP() {
        return "http://"+ "192.168.6.107:8080"+"/";
    }

    public static void setIP(String IP) {
      //  ApplicationApp.IP = IP;
    }

    private static String IP ="";
    @Override
    public void onCreate() {
        super.onCreate();
        mainHandler = new Handler();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        getScreen(this);
        String[] urls = getResources().getStringArray(R.array.url);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
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

    public void getScreen(Context aty) {
        DisplayMetrics dm = aty.getResources().getDisplayMetrics();
        H=dm.heightPixels;
        W=dm.widthPixels;
    }

    public static PeopleInfoEntity getPeopleInfoEntity() {
        return peopleInfoEntity;
    }

    public static void setPeopleInfoEntity(PeopleInfoEntity peopleInfoEntity) {
        ApplicationApp.peopleInfoEntity = peopleInfoEntity;
    }

    public static LoginInfoBean getNewLoginEntity() {
        return newLoginEntity;
    }

    public static void setNewLoginEntity(LoginInfoBean newLoginEntity) {
        ApplicationApp.newLoginEntity = newLoginEntity;
    }

}