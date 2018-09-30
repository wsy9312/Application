package com.sd.storage.app;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by MrZhou on 2017/5/13.
 */
public class StorageApplication extends Application{

    public static StorageApplication mInstance;



    public static StorageApplication getApplication() {
        if(null == mInstance){
            mInstance = new StorageApplication();
        }
        return mInstance;
    }

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        MultiDex.install(this);
        appComponent = AppComponent.Initializer.init(this);
        appComponent.inject(this);
        SpeechUtility.createUtility(getApplicationContext(), "appid=5b1bfec9");
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
    public void removeUser(){

    }


}
