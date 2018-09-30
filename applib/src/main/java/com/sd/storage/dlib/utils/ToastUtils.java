package com.sd.storage.dlib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.storage.R;


/**
 * Created by MrZhou on 2016/10/14.
 */
public class ToastUtils {

    public static void showBaseToast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showBaseToast(int resId, Context context){
        showBaseToast(context.getString(resId), context);
    }

    public static void showMyBaseToast(Context context, String text){
        Toast result = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, null);
        TextView tv = (TextView)v.findViewById(android.R.id.message);
        tv.setText(text);

        result.setView(v);
        result.setGravity(Gravity.CENTER_HORIZONTAL| Gravity.TOP, 0, DensityUtils.dpToPixel(context, 80));
        result.setDuration(Toast.LENGTH_LONG);
        result.show();
    }


}
