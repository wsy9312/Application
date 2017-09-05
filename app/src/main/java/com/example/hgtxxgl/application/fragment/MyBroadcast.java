package com.example.hgtxxgl.application.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by HGTXxgl on 2017/9/5.
 */

public class MyBroadcast extends BroadcastReceiver {

    OnUpdateUI onUpdateUI;
    @Override
    public void onReceive(Context context, Intent intent) {
        String progress = intent.getStringExtra("progress");
        onUpdateUI.updateUI(progress);
    }

    public void SetOnUpdateUI(OnUpdateUI onUpdateUI){
        this.onUpdateUI = onUpdateUI;
    }

}
