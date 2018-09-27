package com.example.hgtxxgl.application.utils.hand;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.login.LoginInfoBean;
import com.example.hgtxxgl.application.bean.login.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurrentCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount10Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount11Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount12Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount8Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount9Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveOutCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveRestCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveSickCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveWorkCountBean;

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
    private static TempPeopleLeaveCurrentCountBean currentCountBean;
    private static TempPeopleLeaveWorkCountBean workCountBean;
    private static TempPeopleLeaveSickCountBean sickCountBean;
    private static TempPeopleLeaveRestCountBean restCountBean;
    private static TempPeopleLeaveOutCountBean outCountBean;
    private static TempPeopleLeaveCurveCount8Bean count8Bean;
    private static TempPeopleLeaveCurveCount9Bean count9Bean;
    private static TempPeopleLeaveCurveCount10Bean count10Bean;
    private static TempPeopleLeaveCurveCount11Bean count11Bean;
    private static TempPeopleLeaveCurveCount12Bean count12Bean;

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

    public static TempPeopleLeaveCurrentCountBean getCurrentCountBean() {
        return currentCountBean;
    }

    public static void setCurrentCountBean(TempPeopleLeaveCurrentCountBean currentCountBean) {
        ApplicationApp.currentCountBean = currentCountBean;
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

    public static TempPeopleLeaveCurveCount8Bean getCount8Bean() {
        return count8Bean;
    }

    public static void setCount8Bean(TempPeopleLeaveCurveCount8Bean count8Bean) {
        ApplicationApp.count8Bean = count8Bean;
    }

    public static TempPeopleLeaveCurveCount9Bean getCount9Bean() {
        return count9Bean;
    }

    public static void setCount9Bean(TempPeopleLeaveCurveCount9Bean count9Bean) {
        ApplicationApp.count9Bean = count9Bean;
    }

    public static TempPeopleLeaveCurveCount10Bean getCount10Bean() {
        return count10Bean;
    }

    public static void setCount10Bean(TempPeopleLeaveCurveCount10Bean count10Bean) {
        ApplicationApp.count10Bean = count10Bean;
    }


    public static TempPeopleLeaveCurveCount11Bean getCount11Bean() {
        return count11Bean;
    }

    public static void setCount11Bean(TempPeopleLeaveCurveCount11Bean count11Bean) {
        ApplicationApp.count11Bean = count11Bean;
    }

    public static TempPeopleLeaveCurveCount12Bean getCount12Bean() {
        return count12Bean;
    }

    public static void setCount12Bean(TempPeopleLeaveCurveCount12Bean count12Bean) {
        ApplicationApp.count12Bean = count12Bean;
    }
}