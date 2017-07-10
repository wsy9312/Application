package com.example.hgtxxgl.application.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ting on 15/6/19.
 */
public class RequestBean implements Parcelable {
    public static final int RESPONSE_OK = 1;
    @ParamName("id")
    private String mId;
    @ParamName("name")
    private String mName;
    @ParamName("result")
    private int mResult;
    @ParamName("message")
    private String mMessage;
    public int getResult() {
        return mResult;
    }
    public String getMessage() {
        return mMessage;
    }
    public String getId()
    {
        return mId;
    }
    public String getName()
    {
        return mName;
    }
    public boolean succeed() {
        return getResult() == RESPONSE_OK;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
