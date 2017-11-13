package com.example.hgtxxgl.application.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//审批广播
public class TodoTotalBroadcast extends BroadcastReceiver {

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
