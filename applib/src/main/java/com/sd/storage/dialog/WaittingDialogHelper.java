package com.sd.storage.dialog;

import android.app.Activity;

import com.sd.storage.R;


/**
 * dialog 工具类
 *
 * @author wangzhiqiang
 * @date 2015-2-24 下午8:51:46
 */
public class WaittingDialogHelper {


    public static WaitingDialog getWaitDialog(Activity activity, String message) {
        WaitingDialog dialog = null;
        try {
            dialog = new WaitingDialog(activity, R.style.loading_dialog);
            dialog.setMessage(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dialog;
    }


}
