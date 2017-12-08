package com.example.hgtxxgl.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;

//通知详情页
public class NotificationItemActivity extends AppCompatActivity
{

    private static final String TAG = "NotificationItemActivity";
    private HandToolbar handToolbar;
    private String title;
    private String content;
    private String modifytime;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvBody;
    private String tab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout_notification);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(NotificationItemActivity.this);
        Intent intent = getIntent();
        tab = intent.getStringExtra("tab");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        modifytime = intent.getStringExtra("modifyTime");
        initview();
    }

    private void initview()
    {
        tvTitle = (TextView) findViewById(R.id.tv_news_title);
        tvDate = (TextView) findViewById(R.id.tv_news_date);
        tvBody = (TextView) findViewById(R.id.tv_news_body);
        handToolbar = (HandToolbar) findViewById(R.id.itemactivity_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle(tab);
        handToolbar.setTitleSize(18);
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tvTitle.setText(title);
                tvDate.setText(modifytime);
                tvBody.setText(content);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
