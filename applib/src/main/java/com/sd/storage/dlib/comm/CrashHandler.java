package com.sd.storage.dlib.comm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import com.sd.storage.dlib.utils.FileUtils;
import com.sd.storage.dlib.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类
 * Created by MrZhou on 2016/11/30.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();

    // 程序的Context对象
    private Context mContext = null;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (mContext == null) {
            return;
        }

        if (ex != null) {
            Map<String, String> deviceInfo = collectDeviceInfo(mContext);
            StringBuffer errorInfor = new StringBuffer();
            for (Map.Entry<String, String> entry : deviceInfo.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                errorInfor.append(key + "=" + value + "\r\n");
            }
            errorInfor.append(StringUtils.throwableToString(ex));
            final String errorMsg = errorInfor.toString();
            // 写入log
            String path = FileUtils.getSdFilePath("ShunDao", "log.log");
            FileUtils.writeFile(path, errorInfor.toString());
            // 弹出提示
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();

                    showDialog(errorMsg);

                    Looper.loop();
                }
            }.start();
        }
    }

    public void showDialog(String errorInfor) {
        // 上传日志文件

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setTitle("程序异常信息：").setMessage(errorInfor);

        builder.show();
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public Map<String, String> collectDeviceInfo(Context ctx) {
        Map<String, String> infos = new HashMap<String, String>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
            }
        }
        return infos;
    }
}
