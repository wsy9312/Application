package com.example.hgtxxgl.application.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by HGTXxgl on 2017/8/14.
 */

public class DateUtil {

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
    }
    public static String getCurrentDateBefore(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.MINUTE,-2);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
    }
}
