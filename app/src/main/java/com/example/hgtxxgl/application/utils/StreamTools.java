package com.example.hgtxxgl.application.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class StreamTools {
    public static String getValue(FileInputStream fis) throws Exception{
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int length=-1;
        while((length=fis.read(buffer))!=-1){
            stream.write(buffer,0,length);
        }
        stream.flush();
        stream.close();
        String value=stream.toString();
        return value;
    }
}
