package com.example.hgtxxgl.application.network;


import com.example.hgtxxgl.application.utils.ApplicationApp;

/**
 * Created by ting on 15/6/19.
 */
public class Config {
    public static final String RESPONSE_CACHE = "http_cache";
    public static final long RESPONSE_CACHE_SIZE = 4 * 1024 * 1024; //4MByte
    public static final long HTTP_CONNECT_TIMEOUT = 2000;
    public static final long HTTP_READ_TIMEOUT = 10000;
    public static final boolean DEBUG = true;
    //范范
//    public static String TEST_SERVER_URL = "http://10.58.137.84:8000/ordering2";
//    public static String DEFAULT_SERVER_URL = "http://10.58.137.84:8000/ordering2";
    //测试服务器
//    public static String TEST_SERVER_URL = "http://172.51.96.167:8082/dzd/";
//    public static String DEFAULT_SERVER_URL = "http://172.51.96.167:8082/dzd/";

    //0.0.0.0.
//    public static String TEST_SERVER_URL = "http://10.58.134.70:8080/ordering2";
//    public static String DEFAULT_SERVER_URL = "http://10.58.134.70:8080/ordering2";
    //浩然服务器
//    public static String TEST_SERVER_URL = "http://10.58.134.70:8080/ordering2";
//    public static String DEFAULT_SERVER_URL = "http://10.58.134.70:8080/ordering2";
    //测试服务器
//    public static String TEST_SERVER_URL = "http://120.26.142.49:8080/dnd/";
//    public static String DEFAULT_SERVER_URL = "http://120.26.142.49:8080/dnd/";
    //正式服务器
//    public static String TEST_SERVER_URL = "http://app.dndchina.cn:8080/dnd/";
//    public static String DEFAULT_SERVER_URL = "http://app.dndchina.cn:8080/dnd/";
    //外网服务器
    public static String TEST_SERVER_URL = "http://112.64.173.178:20181/dzd/";
    public static String DEFAULT_SERVER_URL = "http://192.168.1.106:23456";


    public static String updateServer() {
        String server = SharedPrefsUtil.getValue(ApplicationApp.context,
                "server", Config.DEFAULT_SERVER_URL);

        if (server.endsWith("/")) {
            server = server.substring(0, server.length() - 1);
            SharedPrefsUtil.putValue(ApplicationApp.context,
                    "server", server);
        }
        RetrofitUtils.reset();

        return server;
    }

    public static String getServer() {
        return "http://192.168.1.106:8080";
    }
}
