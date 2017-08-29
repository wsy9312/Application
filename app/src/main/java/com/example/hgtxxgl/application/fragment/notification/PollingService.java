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
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.MessageEntity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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
	private NotificationCompat.Builder builder1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		builder1 = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.app_logo)
				//设置通知标题
				.setContentTitle("management system service is running");
		Notification notification = builder1.build();
		startForeground(110, notification);// 开始前台服务
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
				//设置通知内容
				.setContentTitle("您收到一条通知");
	}

	private void showNotification(String content) {
		Intent i = new Intent(this, LoginActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		//设置通知标题
		builder.setContentText(content)
				.setWhen(System.currentTimeMillis())
				.setContentIntent(pendingIntent);
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
			getDataAlarmMessage();
			getDataAlarmApproveCar();
			getDataAlarmApprovePeople();
		}
	}

	private void getDataAlarmApprovePeople() {
		Log.e(TAG,"当前时间service:_getDataAlarmApprovePeople "+DateUtil.getCurrentDateBefore()+"&&"+DateUtil.getCurrentDate());
		PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
		PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
		peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		peopleLeaveRrdBean.setBeginNum("1");
		peopleLeaveRrdBean.setContent("?");
		peopleLeaveRrdBean.setCurrentApproveNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		peopleLeaveRrdBean.setEndNum("100");
		peopleLeaveRrdBean.setIsAndroid("1");
		peopleLeaveRrdBean.setLevelNum("?");
		peopleLeaveRrdBean.setModifyTime("?");
		peopleLeaveRrdBean.setMultiLevelResult("?");
		peopleLeaveRrdBean.setNo("?");
		peopleLeaveRrdBean.setNoIndex("?");
		peopleLeaveRrdBean.setProcess("?");
		peopleLeaveRrdBean.setBCancel("?");
		List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
		list.add(peopleLeaveRrdBean);
		peopleLeaveEntity.setPeopleLeaveRrd(list);
		String json = new Gson().toJson(peopleLeaveEntity);
		final String s = "get " + json;
		Log.e(TAG, "当前时间service:_getDataAlarmApprovePeople "+s);
		String url = CommonValues.BASE_URL;
		HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
				if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
					for (int i = 0; i < 100; i++) {
						if (peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getBCancel().equals("0") && peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getProcess().equals("0")) {
							PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
							PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
							peopleInfoBean.setNo(peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getNo());
							peopleInfoBean.setName("?");
							peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
							peopleInfoBean.setIsAndroid("1");
							List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
							beanList.add(peopleInfoBean);
							peopleEntity.setPeopleInfo(beanList);
							String json1 = new Gson().toJson(peopleEntity);
							String s1 = "get " + json1;
							Log.e(TAG, "当前时间service:_getDataAlarmApprovePeople " + s1);
							HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL, s1, PeopleInfoEntity.class, new HttpManager.ResultCallback<PeopleInfoEntity>() {
								@Override
								public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
									if (peopleInfoEntity != null) {
										List<PeopleInfoEntity.PeopleInfoBean> peopleInfoBeen = peopleInfoEntity.getPeopleInfo();
										String content = peopleInfoBeen.get(0).getName();
										showNotification(content+"发来一条请假外出申请");
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
					}
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

	private void getDataAlarmApproveCar() {
		Log.e(TAG,"当前时间service:_getDataAlarmApproveCar"+DateUtil.getCurrentDateBefore()+"&&"+DateUtil.getCurrentDate());
		CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
		CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
		carLeaveRrdBean.setApproverNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		carLeaveRrdBean.setBeginNum("1");
		carLeaveRrdBean.setEndNum("100");
		carLeaveRrdBean.setContent("?");
		carLeaveRrdBean.setIsAndroid("1");
		carLeaveRrdBean.setModifyTime("?");
		carLeaveRrdBean.setNo("?");
		carLeaveRrdBean.setNoIndex("?");
		carLeaveRrdBean.setProcess("?");
		carLeaveRrdBean.setbCancel("?");
		List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
		list.add(carLeaveRrdBean);
		carLeaveEntity.setCarLeaveRrd(list);
		String json = new Gson().toJson(carLeaveEntity);
		String s = "get " + json;
		Log.e(TAG,"当前时间service:_getDataAlarmApproveCar"+s);
		String url = CommonValues.BASE_URL;
		HttpManager.getInstance().requestResultForm(url, s, CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
				if (carLeaveEntity1 != null && carLeaveEntity1.getCarLeaveRrd().size() > 0) {
					for (int i = 0; i < 100; i++) {
						if (carLeaveEntity1.getCarLeaveRrd().get(i).getbCancel().equals("0") && carLeaveEntity1.getCarLeaveRrd().get(i).getProcess().equals("0")) {
							PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
							PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
							peopleInfoBean.setNo(carLeaveEntity1.getCarLeaveRrd().get(i).getNo());
							peopleInfoBean.setName("?");
							peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
							peopleInfoBean.setIsAndroid("1");
							List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
							beanList.add(peopleInfoBean);
							peopleEntity.setPeopleInfo(beanList);
							String json1 = new Gson().toJson(peopleEntity);
							String s1 = "get " + json1;
							Log.e(TAG,"当前时间service:_getDataAlarmApproveCar"+json1);
							HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL, s1, PeopleInfoEntity.class, new HttpManager.ResultCallback<PeopleInfoEntity>() {
								@Override
								public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
									if (peopleInfoEntity != null) {
										List<PeopleInfoEntity.PeopleInfoBean> peopleInfoBeen = peopleInfoEntity.getPeopleInfo();
										String content = peopleInfoBeen.get(0).getName();
										showNotification(content+"发来一条车辆外出申请");
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
					}
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

	private void getDataAlarmMessage() {
		Log.e(TAG,"当前时间service:_getDataAlarmMessage"+DateUtil.getCurrentDateBefore()+"&&"+DateUtil.getCurrentDate());
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
					List<MessageEntity.MessageRrdBean> messageRrd = messageEntity.getMessageRrd();
					String content = messageRrd.get(0).getContent();
					showNotification(content);
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
