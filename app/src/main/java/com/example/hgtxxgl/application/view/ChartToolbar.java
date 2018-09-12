package com.example.hgtxxgl.application.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;

public class ChartToolbar extends FrameLayout{
    private TextView tvTitle;
    private TextView tvLeft;
    private TextView tvTotalNum,tvCurrentNum,tvCurrentRate;

    public ChartToolbar(Context context) {
        super(context);
        setView(context);
    }

    public ChartToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setView(context);

    }

    public ChartToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setView(context);

    }

    public void setView(Context context) {
        inflate(context, R.layout.layout_chart_tool_bar, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTotalNum = (TextView) findViewById(R.id.chart_toolbar_number1);
        tvCurrentNum = (TextView) findViewById(R.id.chart_toolbar_number2);
        tvCurrentRate = (TextView) findViewById(R.id.chart_toolbar_number3);
    }

    public TextView getTvTotalNum() {
        return tvTotalNum;
    }

    public TextView getTvCurrentNum() {
        return tvCurrentNum;
    }

    public TextView getTvCurrentRate() {
        return tvCurrentRate;
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setTitleSize(int size){
        tvTitle.setTextSize(size);
    }

    //添加二维码图片
    public void setTotalNum(String num) {
        if (!num.isEmpty()){
            tvTotalNum.setText(num);
        }else{
            return;
        }
    }
    public void setCurrentNum(String num) {
        if (!num.isEmpty()){
            tvCurrentNum.setText(num);
        }else{
            return;
        }
    }
    public void setCurrentRate(String num) {
        if (!num.isEmpty()){
            tvCurrentRate.setText(num);
        }else{
            return;
        }
    }

    public void setDisplayHomeAsUpEnabled(final Activity activity) {
        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
}
