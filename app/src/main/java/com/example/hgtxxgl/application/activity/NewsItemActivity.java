package com.example.hgtxxgl.application.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//新闻详情页
public class NewsItemActivity extends AppCompatActivity {

    private static final String TAG = "NewsItemActivity";
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private HandToolbar handToolbar;
    private String title;
    private String content;
    private String modifytime;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvBody;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(NewsItemActivity.this);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        modifytime = intent.getStringExtra("modifyTime");
        initview();
    }

    private void initview() {
        tvTitle = (TextView) findViewById(R.id.tv_news_title);
        tvDate = (TextView) findViewById(R.id.tv_news_date);
        tvBody = (TextView) findViewById(R.id.tv_news_body);
        image1 = (ImageView) findViewById(R.id.image_news_one);
        image2 = (ImageView) findViewById(R.id.image_news_two);
        image3 = (ImageView) findViewById(R.id.image_news_three);
        image4 = (ImageView) findViewById(R.id.image_news_four);
        image5 = (ImageView) findViewById(R.id.image_news_five);
        pb = (ProgressBar) findViewById(R.id.news_pb);
        handToolbar = (HandToolbar) findViewById(R.id.itemactivity_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(false,this,0);
        handToolbar.setTitle("新闻内容");
        handToolbar.setTitleSize(20);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText(title);
                tvDate.setText(modifytime);
                tvBody.setText(content);
            }
        });
        getPictureFromTitle(title,modifytime,content);
    }

    private void getPictureFromTitle(String title, String modifytime, String content) {
        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle(title);
        newsRrdBean.setContent(content);
        newsRrdBean.setModifyTime(modifytime);
        newsRrdBean.setPicture1("?");
        newsRrdBean.setPicture2("?");
        newsRrdBean.setPicture3("?");
        newsRrdBean.setPicture4("?");
        newsRrdBean.setPicture5("?");
        newsRrdBean.setPicture1Len("?");
        newsRrdBean.setPicture2Len("?");
        newsRrdBean.setPicture3Len("?");
        newsRrdBean.setPicture4Len("?");
        newsRrdBean.setPicture5Len("?");
        newsRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        Log.e(TAG, "新闻图片: " + s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, NewsInfoEntity.class, new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(String json, NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0) {
                    pb.setVisibility(View.GONE);
                    String picture1 = newsInfoEntity.getNewsRrd().get(0).getPicture1();
                    String picture2 = newsInfoEntity.getNewsRrd().get(0).getPicture2();
                    String picture3 = newsInfoEntity.getNewsRrd().get(0).getPicture3();
                    String picture4 = newsInfoEntity.getNewsRrd().get(0).getPicture4();
                    String picture5 = newsInfoEntity.getNewsRrd().get(0).getPicture5();
                    String picture1Len = newsInfoEntity.getNewsRrd().get(0).getPicture1Len();
                    String picture2Len = newsInfoEntity.getNewsRrd().get(0).getPicture2Len();
                    String picture3Len = newsInfoEntity.getNewsRrd().get(0).getPicture3Len();
                    String picture4Len = newsInfoEntity.getNewsRrd().get(0).getPicture4Len();
                    String picture5Len = newsInfoEntity.getNewsRrd().get(0).getPicture5Len();
                    if (TextUtils.isEmpty(picture1)||picture1Len.equals("-9999")){
                        image1.setVisibility(View.GONE);
                    }else{
                        image1.setImageBitmap(stringtoBitmap(picture1));
                    }
                    if (TextUtils.isEmpty(picture2)||picture2Len.equals("-9999")){
                        image2.setVisibility(View.GONE);
                    }else{
                        image2.setImageBitmap(stringtoBitmap(picture2));
                    }
                    if (TextUtils.isEmpty(picture3)||picture3Len.equals("-9999")){
                        image3.setVisibility(View.GONE);
                    }else{
                        image3.setImageBitmap(stringtoBitmap(picture3));
                    }
                    if (TextUtils.isEmpty(picture4)||picture4Len.equals("-9999")){
                        image4.setVisibility(View.GONE);
                    }else{
                        image4.setImageBitmap(stringtoBitmap(picture4));
                    }
                    if (TextUtils.isEmpty(picture5)||picture5Len.equals("-9999")){
                        image5.setVisibility(View.GONE);
                    }else{
                        image5.setImageBitmap(stringtoBitmap(picture5));
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    //将字符串转换成Bitmap类型
    public Bitmap stringtoBitmap(String string){
        Bitmap bitmap=null;
        try {
            byte[]bitmapArray;
            bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
