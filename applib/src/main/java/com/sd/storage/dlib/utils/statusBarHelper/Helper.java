package com.sd.storage.dlib.utils.statusBarHelper;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by MrZhou on 2017/5/11.
 */
public class Helper {
    @IntDef({
            OTHER,
            MIUI,
            FLYME,
            ANDROID_M
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface SystemType {

    }

    public static final int OTHER = -1;
    public static final int MIUI = 1;
    public static final int FLYME = 2;
    public static final int ANDROID_M = 3;

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @return 1:MIUI 2:Flyme 3:android6.0
     */
    public static int statusBarLightMode(AppCompatActivity activity) {
        @SystemType int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (new MIUIHelper().setStatusBarLightMode(activity, true)) {
                result = MIUI;
            } else if (new FlymeHelper().setStatusBarLightMode(activity, true)) {
                result = FLYME;
            } else if (new AndroidMHelper().setStatusBarLightMode(activity, true)) {
                result = ANDROID_M;
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                activity.getWindow().setStatusBarColor(Color.BLACK);
                result = OTHER;
            }
        }
        return result;
    }
}
