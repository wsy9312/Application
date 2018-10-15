package com.sd.storage;

/**
 * Created  on 2017/5/13.
 */

public class UrlManager {

    // http://192.168.1.107:8080/Canteen/today/todayAll.do
    //public static String HTTP_ROOT = "http://server.sinopharmspd.com:6041/";
    public static String HTTP_ROOT = "http://192.168.1.246:8080/";

    public static String USERID = "1";
    public static String USENAME = "WWW";
    public static String LEVEl = "1";


    public static String getLEVEl() {
        return LEVEl;
    }

    public static String getUSENAME() {
        return USENAME;
    }


    public static String getUSERID() {
        return USERID;
    }

    public static void setUSERID(String USERID) {
        UrlManager.USERID = USERID;
    }

    public static void setUSENAME(String USENAME) {
        UrlManager.USENAME = USENAME;
    }

    public static void setLEVEl(String LEVEl) {
        UrlManager.LEVEl = LEVEl;
    }

    public static String getUrlPath() {
        return HTTP_ROOT + "Canteen/";
    }

}


