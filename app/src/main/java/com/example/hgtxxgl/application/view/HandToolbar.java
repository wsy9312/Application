package com.example.hgtxxgl.application.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;

public class HandToolbar extends FrameLayout {

    private static final int RES_ID = R.layout.layout_tool_bar;
    private TextView tvTitle, tvLeft, tvRight;
    private OnButtonsClickCallback callback;
    private AlertDialog.Builder builder;

    public HandToolbar(Context context) {
        super(context);
        setView(context);
    }

    public interface OnButtonsClickCallback {
        void onButtonClickListner(VIEWS views, int radioIndex);
    }

    public HandToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setView(context);
    }

    public HandToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setView(context);
    }

    public void setDisplayHomeAsUpEnabled(boolean enable, final Activity activity) {
        if (enable) {
            setLeftButton(R.drawable.ic_arrow_left);
            tvLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }else{
            setLeftButton(null);
            tvLeft.setVisibility(INVISIBLE);
        }
    }

    public void setRightButtonDialog(final Activity activity, final String[] strings) {
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                builder=new AlertDialog.Builder(activity);
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            Log.e("标题栏右边菜单", "onClick: "+strings[i]);
                        }else if (i == 1){
                            Log.e("标题栏右边菜单", "onClick: "+strings[i]);
//                            activity.startActivity(new Intent(activity, TestScanActivity.class));
                        }
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setBackHome(boolean enable, /*final Activity activity,*/ int drawableResId){
        if (enable) {
            setRightButton(drawableResId);
//            tvRight.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    try {
////                        Class<?> clazz = Class.forName("com.example.hgtxxgl.application.fragment.PersonalActivity");
//                        intent.setClass(activity, clazz);
//                    } catch(Exception e){
//                        e.printStackTrace();
//                    }
//                    activity.startActivity(intent);
//                }
//            });
        }else{
            tvRight.setVisibility(GONE);
        }
    }

    public void setView(Context context) {
        inflate(context, R.layout.layout_tool_bar, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvLeft = (TextView) findViewById(R.id.tv_left);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
    }

    public void setTitleSize(int size){
        tvTitle.setTextSize(size);
    }

    public void setLeftButton(Integer resDrawable) {

        if (resDrawable != null) {
            Drawable drawable = getResources().getDrawable(resDrawable);
            if (drawable != null) {
                drawable.setBounds(0, 0, 60, 60);
            }
            tvLeft.setCompoundDrawables(drawable, null, null, null);
        }
        if (resDrawable == null) {
            return;
        }
        tvLeft.setVisibility(VISIBLE);
        if (!tvLeft.hasOnClickListeners())
        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.LEFT_BUTTON, -1);
            }
        });
    }

    public void setRightButton(Integer resDrawable) {
        if (resDrawable != null) {
            tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, resDrawable, 0);
        }
        if (resDrawable == null) {
            return;
        }
        tvRight.setVisibility(VISIBLE);
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.RIGHT_BUTTON, -1);
            }
        });
    }

    public void setButtonsClickCallback(OnButtonsClickCallback clickCallback) {
        this.callback = clickCallback;
    }

    public enum VIEWS {
        LEFT_BUTTON, RIGHT_BUTTON
    }
}
