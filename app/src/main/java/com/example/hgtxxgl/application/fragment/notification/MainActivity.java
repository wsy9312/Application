package com.example.hgtxxgl.application.fragment.notification;

import android.app.Activity;
import android.os.Bundle;

import com.example.hgtxxgl.application.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("Start polling service...");
		PollingUtils.startPollingService(this, 1, PollingService.class, PollingService.ACTION);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("Stop polling service...");
//		PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
	}

}
