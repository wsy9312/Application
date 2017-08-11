package com.example.hgtxxgl.application.fragment.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.example.hgtxxgl.application.R;

public class MessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_sheet);
		
		NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
	}
}
