package com.example.hgtxxgl.application.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
//个人中心标题头样式
public class PersonalHandToolbar extends FrameLayout {

    private TextView tvTitle;
    private ImageView tvRight,tvLeft,tvHistory;
    private OnButtonsClickCallback callback;
    private ImageView tvChange;
    private TextView tvBack;

    public PersonalHandToolbar(Context context) {
        super(context);
        setView(context);
    }

    public interface OnButtonsClickCallback {
        void onButtonClickListner(VIEWS views, int radioIndex);
    }

    public PersonalHandToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setView(context);

    }

    public PersonalHandToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setView(context);

    }

    public void setView(Context context) {
        inflate(context, R.layout.layout_personcenter_tool_bar, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (ImageView) findViewById(R.id.tv_right);
        tvLeft = (ImageView) findViewById(R.id.tv_left);
        tvHistory = (ImageView) findViewById(R.id.tv_history);
        tvChange = (ImageView) findViewById(R.id.tv_change);
        tvBack = (TextView) findViewById(R.id.tv_back);
    }

    public ImageView getTvRightBao(){
        return tvRight;
    }

    public ImageView getTvHistorySao(){
        return tvHistory;
    }

    public  ImageView getTvLeft(){
        return tvLeft;
    }

    public void setTitle(CharSequence title) {
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
    }

    public void setTitleSize(int size){
        tvTitle.setTextSize(size);
    }

    //添加二维码图片
    public void setLeftButton(Bitmap bitmap) {
        if (bitmap != null){
            tvLeft.setImageBitmap(bitmap);
        }else{
            return;
        }
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.LEFT_BUTTON, -1);
            }
        });
    }

    public void setRightButton(int resDrawable) {
        tvRight.setImageResource(resDrawable);
        tvRight.setVisibility(VISIBLE);
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.RIGHT_BUTTON, -1);
            }
        });
    }

    public void setHisButton(int resDrawable) {
        tvHistory.setImageResource(resDrawable);
        tvHistory.setVisibility(VISIBLE);
        tvHistory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.HISTORY_BUTTON, -1);
            }
        });
    }

    public void setChangeButton(int resDrawable) {
        tvChange.setImageResource(resDrawable);
        tvChange.setVisibility(VISIBLE);
        tvChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onButtonClickListner(VIEWS.CHANGE_BUTTON, -1);
            }
        });
    }

    public void setButtonsClickCallback(OnButtonsClickCallback clickCallback) {
        this.callback = clickCallback;
    }

    public enum VIEWS {
        LEFT_BUTTON, RIGHT_BUTTON,HISTORY_BUTTON,CHANGE_BUTTON,BACK_BUTTON
    }

    public void setDisplayHomeAsUpEnabled(boolean enable, final Activity activity) {
        if (enable) {
            setBackButton(R.drawable.ic_arrow_left);
            tvLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });
        }else{
            setBackButton(null);
            tvLeft.setVisibility(INVISIBLE);
        }
    }

    public void setBackButton(Integer resDrawable) {
        if (resDrawable != null) {
            Drawable drawable = getResources().getDrawable(resDrawable);
            if (drawable != null) {
                drawable.setBounds(0, 0, 60, 60);
            }
            tvBack.setCompoundDrawables(drawable, null, null, null);
        }
        if (resDrawable == null) {
            return;
        }
        tvBack.setVisibility(VISIBLE);
        if (!tvBack.hasOnClickListeners())
            tvBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onButtonClickListner(VIEWS.BACK_BUTTON, -1);
                }
            });
    }
}
