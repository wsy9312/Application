package com.example.hgtxxgl.application.activity;

import android.content.Context;
import android.content.Intent;

import com.example.hgtxxgl.application.fragment.notification.OnUpdateUI;
import com.example.hgtxxgl.application.fragment.notification.TodoTotalBroadcast;

/**
 * Created by HGTXxgl on 2018/7/19.
 */
public class DetailTotalBroadcast extends TodoTotalBroadcast {
    OnUpdateUI onUpdateUIDE;
    @Override
    public void onReceive(Context context, Intent intent) {
        String progress = intent.getStringExtra("progress");
        onUpdateUIDE.updateUI(progress);
    }

    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUIDE = onUpdateUI;
    }
}
