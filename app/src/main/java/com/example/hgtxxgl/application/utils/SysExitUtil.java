package com.example.hgtxxgl.application.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HGTXxgl on 2017/8/2.
 */

public class SysExitUtil {
    public static List activityList = new ArrayList();

    public static void exit(){
        int siz=activityList.size();
        for(int i=0;i<siz;i++){
                if(activityList.get(i)!=null){
                         ((Activity) activityList.get(i)).finish();
                         }
                }
        }
}
