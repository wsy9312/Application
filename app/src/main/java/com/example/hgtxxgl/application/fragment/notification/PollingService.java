package com.example.hgtxxgl.application.fragment.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LibMainActivity;

/**
 * Polling service
 * @Author Ryan
 * @Create 2013-7-13 上午10:18:44
 */
public class PollingService extends Service {

	public static final String ACTION = "com.ryantang.service.PollingService";
	
	private NotificationManager mManager;
	private NotificationCompat.Builder builder;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new PollingThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.app_logo)
				//设置通知标题
				.setContentTitle("您得到一条最新消息")
				//设置通知内容
				.setContentText("这是消息内容")
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setAutoCancel(true);
	}

	private void showNotification() {
		Intent i = new Intent(this, LibMainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		builder.setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent);
		mManager.notify(1, builder.build());
	}

	int count = 0;
	class PollingThread extends Thread {
		@Override
		public void run() {
			System.out.println("Polling...");
			count ++;
			if (count % 1 == 0) {
				showNotification();
				System.out.println("New message!");
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
