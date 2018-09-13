package com.example.hgtxxgl.application.utils.hand;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.LoginInfoBean;
import com.example.hgtxxgl.application.bean.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.TempPeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.TempPeopleLeaveOutCountBean;
import com.example.hgtxxgl.application.bean.TempPeopleLeaveRestCountBean;
import com.example.hgtxxgl.application.bean.TempPeopleLeaveSickCountBean;
import com.example.hgtxxgl.application.bean.TempPeopleLeaveWorkCountBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationApp extends Application {
    //handler
    public static Handler mainHandler;

    public static Context context;

    private String UPLOAD_URL="http:";

    private static PeopleInfoBean peopleInfoBean;

    private static LoginInfoBean loginInfoBean;

    private static TempPeopleLeaveCountBean countBean;
    private static TempPeopleLeaveWorkCountBean workCountBean;
    private static TempPeopleLeaveSickCountBean sickCountBean;
    private static TempPeopleLeaveRestCountBean restCountBean;
    private static TempPeopleLeaveOutCountBean outCountBean;

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

    public static PeopleInfoBean getPeopleInfoBean() {
        return peopleInfoBean;
    }

    public static void setPeopleInfoBean(PeopleInfoBean peopleInfoBean) {
        ApplicationApp.peopleInfoBean = peopleInfoBean;
    }

    public static LoginInfoBean getLoginInfoBean() {
        return loginInfoBean;
    }

    public static void setLoginInfoBean(LoginInfoBean loginInfoBean) {
        ApplicationApp.loginInfoBean = loginInfoBean;
    }

    public static TempPeopleLeaveCountBean getCountBean() {
        return countBean;
    }

    public static void setCountBean(TempPeopleLeaveCountBean countBean) {
        ApplicationApp.countBean = countBean;
    }

    public static TempPeopleLeaveWorkCountBean getWorkCountBean() {
        return workCountBean;
    }

    public static void setWorkCountBean(TempPeopleLeaveWorkCountBean workCountBean) {
        ApplicationApp.workCountBean = workCountBean;
    }


    public static TempPeopleLeaveSickCountBean getSickCountBean() {
        return sickCountBean;
    }

    public static void setSickCountBean(TempPeopleLeaveSickCountBean sickCountBean) {
        ApplicationApp.sickCountBean = sickCountBean;
    }

    public static TempPeopleLeaveRestCountBean getRestCountBean() {
        return restCountBean;
    }

    public static void setRestCountBean(TempPeopleLeaveRestCountBean restCountBean) {
        ApplicationApp.restCountBean = restCountBean;
    }

    public static TempPeopleLeaveOutCountBean getOutCountBean() {
        return outCountBean;
    }

    public static void setOutCountBean(TempPeopleLeaveOutCountBean outCountBean) {
        ApplicationApp.outCountBean = outCountBean;
    }
}