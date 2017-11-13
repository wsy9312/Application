package com.example.hgtxxgl.application.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//通知广播
public class NotificationBroadcast extends BroadcastReceiver {

    OnUpdateUINO onUpdateUINO;
    @Override
    public void onReceive(Context context, Intent intent) {
        String progress = intent.getStringExtra("progress");
        onUpdateUINO.updateUINO(progress);
    }

    public void SetOnUpdateUI(OnUpdateUINO onUpdateUINO){
        this.onUpdateUINO = onUpdateUINO;
    }

}
