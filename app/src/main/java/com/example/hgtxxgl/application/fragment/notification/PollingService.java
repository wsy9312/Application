package com.example.hgtxxgl.application.fragment.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LibMainActivity;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.MessageEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.utils.DateUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.hgtxxgl.application.activity.LibMainActivity.FLAGAPPLY;
import static com.example.hgtxxgl.application.activity.LibMainActivity.FLAGAPPROVE;
import static com.example.hgtxxgl.application.activity.LibMainActivity.FLAGNOT;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;
//通知服务
public class PollingService extends Service {

	public static final String ACTION = "com.ryantang.service.PollingService";
	public static final String TAG = "PollingService";

	private NotificationManager mManager;
	//后台服务
	private NotificationCompat.Builder builder;
	//前台服务
	private NotificationCompat.Builder builder1;
	private List<String> list1;
	private List<String> list2;
	private List<String> list3;
	private List<String> list4;
	private List<String> list5;
	private String tempIP;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
		tempIP = share.getString("tempIP", "IP address is empty");
		builder1 = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.app_logo)
				.setContentTitle("区域信息安防软件");
		Notification notification = builder1.build();
		startForeground(110, notification);
		initNotifiManager();
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		list3 = new ArrayList<>();
		list4 = new ArrayList<>();
		list5 = new ArrayList<>();
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
				.setSmallIcon(R.mipmap.app_logo);
	}

	private void showNotification(String content) {
		Intent i = new Intent(this, LibMainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
		//设置通知标题
		builder.setContentText(getString(R.string.click_to_look))
				.setContentTitle(content)
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
			getDataAlarmApplyCar();
			getDataAlarmApplyPeople();
		}
	}

	public void getDataAlarmApplyCar() {
		CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
		CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
		carLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
		carLeaveRrdBean.setProcess("?");
		carLeaveRrdBean.setContent("?");
		carLeaveRrdBean.setBeginNum("1");
		carLeaveRrdBean.setEndNum("100");
		carLeaveRrdBean.setNoIndex("?");
		carLeaveRrdBean.setModifyTime("?");
		carLeaveRrdBean.setRegisterTime("?");
		carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		carLeaveRrdBean.setIsAndroid("1");
		carLeaveRrdBean.setBCancel("0");
		carLeaveRrdBean.setResult("?");
		carLeaveRrdBean.setApproverNo("?");
		List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
		list.add(carLeaveRrdBean);
		carLeaveEntity.setCarLeaveRrd(list);
		String json = new Gson().toJson(carLeaveEntity);
		String s = "get " + json;
		Log.e(TAG+"@",s);
		HttpManager.getInstance().requestResultForm(tempIP, s, CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
				if (carLeaveEntity1 != null && carLeaveEntity1.getCarLeaveRrd().size() > 0) {
					List<CarLeaveEntity.CarLeaveRrdBean> carLeaveRrd = carLeaveEntity1.getCarLeaveRrd();
					String modifyTime = carLeaveRrd.get(0).getModifyTime();
					if (!list4.contains(modifyTime)) {
						list4.add(modifyTime);
						for (int i = 0; i < 500; i++) {
							if (carLeaveEntity1.getCarLeaveRrd().get(i).getProcess().equals("1")) {
								showNotification("您有一条车辆申请已经审批完成");
								Intent intent = new Intent();
								intent.setAction(FLAGAPPLY);
								sendBroadcast(intent);
							}
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

	private void getDataAlarmApplyPeople() {
		PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
		PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
		peopleLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
		peopleLeaveRrdBean.setProcess("?");
		peopleLeaveRrdBean.setContent("?");
		peopleLeaveRrdBean.setBeginNum("1");
		peopleLeaveRrdBean.setEndNum("100");
		peopleLeaveRrdBean.setNoIndex("?");
		peopleLeaveRrdBean.setModifyTime("?");
		peopleLeaveRrdBean.setRegisterTime("?");
		peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		peopleLeaveRrdBean.setIsAndroid("1");
		peopleLeaveRrdBean.setBCancel("0");
		peopleLeaveRrdBean.setResult("?");
		peopleLeaveRrdBean.setDestination("?");
		peopleLeaveRrdBean.setApproverNo("?");
		List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
		list.add(peopleLeaveRrdBean);
		peopleLeaveEntity.setPeopleLeaveRrd(list);
		String json = new Gson().toJson(peopleLeaveEntity);
		String s = "get " + json;
		Log.e(TAG+"#",s);
		HttpManager.getInstance().requestResultForm(tempIP, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
				if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
					List<PeopleLeaveEntity.PeopleLeaveRrdBean> peopleLeaveRrd = peopleLeaveEntity1.getPeopleLeaveRrd();
					String modifyTime = peopleLeaveRrd.get(0).getModifyTime();
					if (!list5.contains(modifyTime)){
						list5.add(modifyTime);
						for (int i = 0; i < 500; i++) {
							if (peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getProcess().equals("1")) {
								showNotification("您有一条请假申请已经审批完成");
								Intent intent = new Intent();
								intent.setAction(FLAGAPPLY);
								sendBroadcast(intent);
							}
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

	private void getDataAlarmApprovePeople() {
		PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
		PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
		peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		peopleLeaveRrdBean.setBeginNum("1");
		peopleLeaveRrdBean.setEndNum("100");
		peopleLeaveRrdBean.setIsAndroid("1");
		peopleLeaveRrdBean.setModifyTime("?");
		peopleLeaveRrdBean.setNo("?");
		peopleLeaveRrdBean.setNoIndex("?");
		peopleLeaveRrdBean.setProcess("?");
		peopleLeaveRrdBean.setBCancel("0");
		peopleLeaveRrdBean.setApproverNo("?");
		peopleLeaveRrdBean.setContent("?");
		List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
		list.add(peopleLeaveRrdBean);
		peopleLeaveEntity.setPeopleLeaveRrd(list);
		String json = new Gson().toJson(peopleLeaveEntity);
		String s = "get " + json;
		Log.e(TAG+"#",s);
		HttpManager.getInstance().requestResultForm(tempIP, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
				if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
					List<PeopleLeaveEntity.PeopleLeaveRrdBean> peopleLeaveRrd = peopleLeaveEntity1.getPeopleLeaveRrd();
					String modifyTime = peopleLeaveRrd.get(0).getModifyTime();
					if (!list1.contains(modifyTime)){
						list1.add(modifyTime);
						for (int i = 0; i < 500; i++) {
							if (!peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getApproverNo().contains(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo())) {
								showNotification(getString(R.string.received_one_apply_rest));
								Intent intent = new Intent();
								intent.setAction(FLAGAPPROVE);
								sendBroadcast(intent);
							}
						}
						/*for (int i = 0; i < 100; i++) {
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
											showNotification(getString(R.string.received_one_apply_rest));
											Intent intent = new Intent();
											intent.setAction(FLAGAPPROVE);
											sendBroadcast(intent);
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
						}*/
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
		CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
		CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
		carLeaveRrdBean.setApproverNo("?");
		carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
		carLeaveRrdBean.setBeginNum("1");
		carLeaveRrdBean.setEndNum("100");
		carLeaveRrdBean.setContent("?");
		carLeaveRrdBean.setIsAndroid("1");
		carLeaveRrdBean.setModifyTime("?");
		carLeaveRrdBean.setNo("?");
		carLeaveRrdBean.setNoIndex("?");
		carLeaveRrdBean.setProcess("?");
		carLeaveRrdBean.setBCancel("0");
		List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
		list.add(carLeaveRrdBean);
		carLeaveEntity.setCarLeaveRrd(list);
		String json = new Gson().toJson(carLeaveEntity);
		String s = "get " + json;
		Log.e(TAG+"%",s);
		HttpManager.getInstance().requestResultForm(tempIP, s, CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
			@Override
			public void onSuccess(final String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
				if (carLeaveEntity1 != null && carLeaveEntity1.getCarLeaveRrd().size() > 0) {
					List<CarLeaveEntity.CarLeaveRrdBean> carLeaveRrd = carLeaveEntity1.getCarLeaveRrd();
					String modifyTime = carLeaveRrd.get(0).getModifyTime();
					if (!list2.contains(modifyTime)) {
						list2.add(modifyTime);
						for (int i = 0; i < 500; i++) {
							if (!carLeaveEntity1.getCarLeaveRrd().get(i).getApproverNo().contains(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo())) {
								showNotification(getString(R.string.received_one_car_appy));
								Intent intent = new Intent();
								intent.setAction(FLAGAPPROVE);
								sendBroadcast(intent);
							}
						}

						/*for (int i = 0; i < 100; i++) {
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
											showNotification(getString(R.string.received_one_car_appy));
											Intent intent = new Intent();
											intent.setAction(FLAGAPPROVE);
											sendBroadcast(intent);
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
						}*/
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

		HttpManager.getInstance().requestResultForm(tempIP, s, MessageEntity.class, new HttpManager.ResultCallback<MessageEntity>() {
			@Override
			public void onSuccess(String json, MessageEntity messageEntity) throws InterruptedException {
				if (messageEntity != null && messageEntity.getMessageRrd().size() > 0){
					List<MessageEntity.MessageRrdBean> messageRrd = messageEntity.getMessageRrd();
					String modifyTime = messageRrd.get(0).getModifyTime();
					if (!list3.contains(modifyTime)) {
						list3.add(modifyTime);
						String content = messageRrd.get(0).getContent();
						showNotification(getString(R.string.received_one_notification)+content);
						Intent intent = new Intent();
						intent.setAction(FLAGNOT);
						sendBroadcast(intent);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
