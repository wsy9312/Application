package com.sd.storage.dlib.model;

/**
 * Created by MrZhou on 2016/10/1.
 */
public class DataContainer<T> {
    public T data;

    public boolean Success;
    public int ErrCode;
    public int code;

    public String message;


    public boolean getResultOK() {
        if (code == 200) {
            return true;
        }
        return false;
    }



  /*  public boolean getResultOK() {
        return Success;
    }*/


}
