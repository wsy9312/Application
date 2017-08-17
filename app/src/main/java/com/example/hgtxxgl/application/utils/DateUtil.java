package com.example.hgtxxgl.application.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    //获取当前系统时间
    public static String getCurrentDate(){
        long time=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
    //获取当前前两分钟系统时间
    public static String getCurrentDateBefore(){
        long time=System.currentTimeMillis()-120000;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }

    //获取当前前两分钟系统时间
    public static String getCurrentDateLater(){
        long time=System.currentTimeMillis()+120000;
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1=format.format(d1);
        return t1;
    }
}
