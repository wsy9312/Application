package com.example.hgtxxgl.application.fragment.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LoginActivity;
import com.example.hgtxxgl.application.entity.MessageEntity;
import com.example.hgtxxgl.application.utils.DateUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PollingService extends Service {

	public static final String ACTION = "com.ryantang.service.PollingService";
	public static final String TAG = "PollingService";

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
		if (ApplicationApp.getNewLoginEntity()!=null){
			new PollingThread().start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.app_logo)
				//设置通知标题
				.setContentTitle("您收到一条通知")
				//设置通知内容
				.setContentText("请点击查看详情");
	}

	private void showNotification() {
		Intent i = new Intent(this, LoginActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		builder.setWhen(System.currentTimeMillis()).setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notification.ledARGB = Color.GREEN;
		notification.ledOnMS = 1000;
		notification.ledOffMS = 1000;
		notification.flags = Notification.FLAG_SHOW_LIGHTS;
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notification.sound = uri;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(1, notification);
	}

	class PollingThread extends Thread {
		@Override
		public void run() {
			getDataAlarm();
		}
	}

	private void getDataAlarm() {
		Log.e(TAG,"当前时间service:"+DateUtil.getCurrentDateBefore()+"&&"+DateUtil.getCurrentDate());
		MessageEntity messageEntity = new MessageEntity();
		MessageEntity.MessageRrdBean messageRrdBean = new MessageEntity.MessageRrdBean();
		messageRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		messageRrdBean.setTime(DateUtil.getCurrentDateBefore()+"&&"+DateUtil.getCurrentDate());
		messageRrdBean.setContent("?");
		messageRrdBean.setModifyTime("?");
		messageRrdBean.setObjects(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		messageRrdBean.setNoIndex("?");
		messageRrdBean.setIsAndroid("1");
		List<MessageEntity.MessageRrdBean> list = new ArrayList<>();
		list.add(messageRrdBean);
		messageEntity.setMessageRrd(list);
		String json = new Gson().toJson(messageEntity).replace("\\u0026","&");
		String s = "get " + json;
		Log.e(TAG, "getDataAlarm--"+s );
		String url = CommonValues.BASE_URL;
		HttpManager.getInstance().requestResultForm(url, s, MessageEntity.class, new HttpManager.ResultCallback<MessageEntity>() {
			@Override
			public void onSuccess(String json, MessageEntity messageEntity) throws InterruptedException {
				if (messageEntity != null && messageEntity.getMessageRrd().size() > 0){
					showNotification();
				}
			}

			@Override
			public void onFailure(String msg) {

			}

			@Override
			public void onResponse(String response) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "Service:onDestroy" );
	}

}
