package com.example.hgtxxgl.application.utils.hand;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CommonValues {
    public static final String BASE_URL = "http://192.168.1.155:8080/";

    public static final int CODE_OA_REQUEST = 2333;
    public static final int MYCOMM = 9527;
    public static final int MYLAUN = 9528;
    public static boolean firstIn = true;

    /**
     * 公用请求
     */

    public static String getLocalMacAddressFromBusybox(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");

        //如果返回的result == null，则说明网络不可取
        if(result==null){
            return "网络出错，请检查网络";
        }

        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if(result.length()>0 && result.contains("HWaddr")==true){
            Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);

             /*if(Mac.length()>1){
                 Mac = Mac.replaceAll(" ", "");
                 result = "";
                 String[] tmp = Mac.split(":");
                 for(int i = 0;i<tmp.length;++i){
                     result +=tmp[i];
                 }
             }*/
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine ()) != null && line.contains(filter)== false) {
                //result += line;
            }
            result = line;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Object> getCommonParams(Context context) {
        Map<String, Object> param = new HashMap<>();
//        param.put("userId", ApplicationApp.getLoginEntity() != null ? ApplicationApp.getLoginEntity().getUserId() : "");
        param.put("deviceId",getLocalMacAddressFromBusybox());
        param.put("deviceType", "Android");
        int apnType = NetworkHttpManager.getAPNType(context);
        if (apnType == 1){
            param.put("net", "WIFI");
        }else if (apnType == 2){
            param.put("net", "2G");
        }else if (apnType == 3){
            param.put("net", "3G");
        }else if (apnType == 4){
            param.put("net", "4G");
        }else if (apnType == 0){
            param.put("net", "");
        }
//        param.put("sign", ApplicationApp.getLoginEntity() != null ? ApplicationApp.getLoginEntity().getSign() : "");
        return param;
    }

}
