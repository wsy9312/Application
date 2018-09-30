package com.sd.storage.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TDevice {

	public static boolean GTE_HC;
	public static boolean GTE_ICS;
	public static boolean PRE_HC;
	private static Boolean _hasBigScreen = null;
	private static Boolean _hasCamera = null;
	public static float displayDensity = 0.0F;

	static {
		GTE_ICS = Build.VERSION.SDK_INT >= 14;
		GTE_HC = Build.VERSION.SDK_INT >= 11;
		PRE_HC = Build.VERSION.SDK_INT >= 11 ? false : true;
	}

	public static final boolean hasCamera(Context context) {
		if (_hasCamera == null) {
			PackageManager pckMgr = context.getPackageManager();
			boolean flag = pckMgr
					.hasSystemFeature("android.hardware.camera.front");
			boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
			boolean flag2;
			if (flag || flag1)
				flag2 = true;
			else
				flag2 = false;
			_hasCamera = Boolean.valueOf(flag2);
		}
		return _hasCamera.booleanValue();
	}

	public static void hideAnimatedView(View view) {
		if (PRE_HC && view != null)
			view.setPadding(view.getWidth(), 0, 0, 0);
	}

	public static void showAnimatedView(View view) {
		if (PRE_HC && view != null)
			view.setPadding(0, 0, 0, 0);
	}

	public static void showSoftKeyboard(Dialog dialog) {
		dialog.getWindow().setSoftInputMode(4);
	}

	public static void hideSoftKeyboard(View view, Context context) {
		if (view == null)
			return;
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showSoftKeyboard(View view, Context context) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}


	public static void setFullScreen(Activity activity) {
		WindowManager.LayoutParams params = activity.getWindow()
				.getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		activity.getWindow().setAttributes(params);
		activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	public static void cancelFullScreen(Activity activity) {
		WindowManager.LayoutParams params = activity.getWindow()
				.getAttributes();
		params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity.getWindow().setAttributes(params);
		activity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	public static void openDial(Context context, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(it);
	}

	public static void openSMS(Context context, String smsBody, String tel) {
		Uri uri = Uri.parse("smsto:" + tel);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", smsBody);
		context.startActivity(it);
	}

	public static void openDail(Context context) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void openSendMsg(Context context) {
		Uri uri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void openCamera(Context context) {
		Intent intent = new Intent(); // 调用照相机
		intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
		intent.setFlags(0x34c40000);
		context.startActivity(intent);
	}

	public static String getPhoneType() {
		return Build.MODEL;
	}

	public static void sendEmail(Context context, String email, String content) {
		try {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasStatusBar(Activity activity) {
		WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
		if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
			return false;
		} else {
			return true;
		}
	}

	public static void setCameraDisplayOrientation(Activity activity,
												   Camera camera) {
		// 获取后置摄像头
		int numberOfCameras = camera.getNumberOfCameras();
		int cid = 0;
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				cid = i;
			}
		}

		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cid, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
			case Surface.ROTATION_0:
				degrees = 0;
				break;
			case Surface.ROTATION_90:
				degrees = 90;
				break;
			case Surface.ROTATION_180:
				degrees = 180;
				break;
			case Surface.ROTATION_270:
				degrees = 270;
				break;
		}

		int result;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		camera.setDisplayOrientation(result);
	}

	/**
	 * 获取本地版本名称VersionName
	 *
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String version_code = "1.0";
		try {
			PackageManager packageManager = context.getApplicationContext().getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			version_code = packInfo.versionName;
		} catch (Exception e) {

		}
		return version_code;
	}


	/**
	 * 检查手机上是否安装了指定的软件
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName){
		//获取packagemanager
		final PackageManager packageManager = context.getApplicationContext().getPackageManager();
		//获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		//用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		//从pinfo中将包名字逐一取出，压入pName list中
		if(packageInfos != null){
			for(int i = 0; i < packageInfos.size(); i++){
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		//判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}


	/**
	 * 通过反射的方式获取状态栏高度
	 *
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return context.getApplicationContext().getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
