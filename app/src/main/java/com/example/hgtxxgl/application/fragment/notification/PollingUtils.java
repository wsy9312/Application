package com.example.hgtxxgl.application.fragment.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
//后台服务开启关闭管理类
public class PollingUtils {

	/**
	 * @param context
	 * @param seconds
	 * @param cls
	 * @param action
	 */ 
	public static void startPollingService(Context context, int seconds, Class<?> cls,String action) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		long triggerAtTime = SystemClock.elapsedRealtime();
		manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, seconds*1000, pendingIntent);
//		manager.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, seconds*1000, pendingIntent);
	}

	/**
	 * @param context
	 * @param cls
	 * @param action
	 */
	public static void stopPollingService(Context context, Class<?> cls,String action) {
		AlarmManager manager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, cls);
		intent.setAction(action);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.cancel(pendingIntent);
	}
}
