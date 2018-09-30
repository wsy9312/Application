package com.sd.storage.dlib.widgets.dialog;

import android.app.Activity;

import com.sd.storage.R;


/**
 * dialog 工具类
* @author wangzhiqiang
* @date 2015-2-24 下午8:51:46 
*  
*/
public class DialogHelper {


	/** 
	* 获得正在加载diglog
	* @param activity
	* @param message
	* @return
	*/ 
	public static WaitDialog getWaitDialog(Activity activity, int message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog.setMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	/** 
	* 获得正在加载diglog
	* @param activity
	* @param message
	* @return
	*/ 
	public static WaitDialog getWaitDialog(Activity activity, String message) {
		WaitDialog dialog = null;
		try {
//			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog = new WaitDialog(activity, R.style.loading_dialog);
			dialog.setMessage(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}

	/** 
	* 获得正在加载diglog 可以取消的
	* @param activity
	* @param message
	* @return
	*/ 
	public static WaitDialog getCancelableWaitDialog(Activity activity,
                                                     String message) {
		WaitDialog dialog = null;
		try {
			dialog = new WaitDialog(activity, R.style.dialog_waiting);
			dialog.setMessage(message);
			dialog.setCancelable(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dialog;
	}
}
