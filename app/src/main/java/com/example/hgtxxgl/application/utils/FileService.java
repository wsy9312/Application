package com.example.hgtxxgl.application.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FileService {
    public Context context;
    public FileService(Context context) {
        this.context = context;
    }
    public boolean saveToRom(String result,String fileName){
        try{
            FileOutputStream fos=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(result.getBytes());
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public Map<String,Integer> readFile(String fileName){
        Map<String,Integer> map=null;
        try{
            FileInputStream fis = context.openFileInput(fileName);
            String value = StreamTools.getValue(fis);
            String substring = value.substring(7,value.length()-1);
            String values[] = substring.split("\\.");

            if(values.length>0){
                map=new HashMap<String, Integer>();
                map.put("ip1", Integer.parseInt(values[0]));
                map.put("ip2", Integer.parseInt(values[1]));
                map.put("ip3", Integer.parseInt(values[2]));
                String[] split = values[3].split(":");
                map.put("ip4", Integer.parseInt(split[0]));
                map.put("ip5", Integer.parseInt(split[1]));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
