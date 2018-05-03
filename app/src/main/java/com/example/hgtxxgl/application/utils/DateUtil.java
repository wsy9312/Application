package com.example.hgtxxgl.application.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    //获取当前时间前n分/时/天
    public static String getCurrentDateLater(/*int MinNumber, int HourNumber, */int DayNumber){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, DayNumber);
        Date m = c.getTime();
        String mon = format.format(m);
//        long time = System.currentTimeMillis();
//        if (MinNumber != 0){
//            time = System.currentTimeMillis()+MinNumber*60*1000;
//        }
//        if (HourNumber != 0){
//            time = System.currentTimeMillis()+HourNumber*60*60*1000;
//        }
//        if (DayNumber != 0){
//            time = System.currentTimeMillis()+DayNumber*24*60*60*1000;
//        }
//        Date d1=new Date(time);
//        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String t1=format.format(d1);
        return mon;
    }

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
