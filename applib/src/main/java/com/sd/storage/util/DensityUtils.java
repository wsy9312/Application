package com.sd.storage.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


/** 
 * 屏幕相关
* @author wangzhiqiang
* @date 2015-4-20 上午11:38:01 
*  
*/
public final class DensityUtils {

	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics;
	}

	/**
	 * 获得缩放因子 density
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context) {
		return getDisplayMetrics(context).density;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dpToPixel(Context context, float dpValue) {
		final float scale = getDensity(context);
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pixelsToDp(Context context, float pxValue) {
		final float scale = getDensity(context);
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
	 */
	public static int pixelsToSp(Context context, float pxValue) {
		float fontScale = getDisplayMetrics(context).scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px
	 */
	public static int spTopixels(Context context, float spValue) {
		float fontScale = getDisplayMetrics(context).scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取dialog宽度
	 */
	public static int getDialogW(Activity aty) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = aty.getResources().getDisplayMetrics();
		int w = dm.widthPixels - 100;
		// int w = aty.getWindowManager().getDefaultDisplay().getWidth() - 100;
		return w;
	}

	public static float getScreenHeight(Context context) {
		return getDisplayMetrics(context).heightPixels;
	}

	public static float getScreenWidth(Context context) {
		return getDisplayMetrics(context).widthPixels;
	}

	public static int[] getRealScreenSize(Activity activity) {
		int[] size = new int[2];
		int screenWidth = 0, screenHeight = 0;
		WindowManager w = activity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				screenWidth = (Integer) Display.class.getMethod("getRawWidth")
						.invoke(d);
				screenHeight = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d,
						realSize);
				screenWidth = realSize.x;
				screenHeight = realSize.y;
			} catch (Exception ignored) {
			}
		size[0] = screenWidth;
		size[1] = screenHeight;
		return size;
	}

	public static boolean isLandscape(Context context) {
		boolean flag;
		if (context.getResources().getConfiguration().orientation == 2)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean isPortrait(Context context) {
		boolean flag = true;
		if (context.getResources().getConfiguration().orientation != 1)
			flag = false;
		return flag;
	}

	/**
	 * 判断当前设备是否是平板电脑
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		boolean flag;
		if ((0xf & context.getResources().getConfiguration().screenLayout) >= 3)
			flag = true;
		else
			flag = false;
		return flag;

	}

}