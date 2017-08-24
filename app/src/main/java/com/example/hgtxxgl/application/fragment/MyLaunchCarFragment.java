package com.example.hgtxxgl.application.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by HGTXxgl on 2017/8/23.
 */
public class MyLaunchCarFragment extends Fragment{
    public static MyLaunchCarFragment newInstance() {
        return null;
    }
    private MyLaunchFragment.DataCallback callback;

    public MyLaunchCarFragment setCallback(MyLaunchFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String str) {

    }
}
