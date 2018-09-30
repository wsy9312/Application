package com.sd.storage.dlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferencePlugin {

    private static PreferencePlugin mInstance;
    private SharedPreferences mSharedPreferences;
    private Editor mEditor;

    private PreferencePlugin(){

    }

    public static PreferencePlugin getInstance(Context context){
        if (mInstance == null) {
            mInstance = new PreferencePlugin();
            mInstance.init(context);
        }

        return mInstance;
    }

    public void init(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mSharedPreferences.edit();
    }

    protected void removeKey(String key) {
        mEditor = getSharedPreferences().edit();
        mEditor.remove(key);
        mEditor.commit();
    }

    protected void removeAll() {
        mEditor = getSharedPreferences().edit();
        mEditor.clear();
        mEditor.commit();
    }

    public void commitString(String key, String value) {
        mEditor = getSharedPreferences().edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getString(String key, String faillValue) {
        return getSharedPreferences().getString(key, faillValue);
    }

    public void commitInt(String key, int value) {
        mEditor = getSharedPreferences().edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getInt(String key, int failValue) {
        return getSharedPreferences().getInt(key, failValue);
    }

    public void commitLong(String key, long value) {
        mEditor = getSharedPreferences().edit();
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public long getLong(String key, long failValue) {
        return getSharedPreferences().getLong(key, failValue);
    }

    public void commitBoolean(String key, boolean value) {
        mEditor = getSharedPreferences().edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public Boolean getBoolean(String key, boolean failValue) {
        return getSharedPreferences().getBoolean(key, failValue);
    }

    public void remove(String key) {
        getSharedPreferences().edit().remove(key).commit();
    }

    public SharedPreferences getSharedPreferences() {

        return mSharedPreferences;
    }


}
