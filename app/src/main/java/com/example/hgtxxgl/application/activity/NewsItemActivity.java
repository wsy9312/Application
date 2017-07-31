package com.example.hgtxxgl.application.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
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
    private ImageView image;
    private HandToolbar handToolbar;
    private String title;
    private String content;
    private String modifytime;
    private String picture1;
    private String picture2;
    private String picture3;
    private String picture4;
    private String picture5;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvBody;
    private ProgressBar pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
//        Bundle bundle = this.getIntent().getBundleExtra("data");
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        modifytime = intent.getStringExtra("modifyTime");
//        picture1 = intent.getStringExtra("picture1");
//        picture2 = intent.getStringExtra("picture2");
//        picture3 = intent.getStringExtra("picture3");
//        picture4 = intent.getStringExtra("picture4");
//        picture5 = intent.getStringExtra("picture5");
        initview();
    }

    private void initview() {
        tvTitle = (TextView) findViewById(R.id.tv_news_title);
        tvDate = (TextView) findViewById(R.id.tv_news_date);
        tvBody = (TextView) findViewById(R.id.tv_news_body);
        image = (ImageView) findViewById(R.id.image_news);
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
        newsRrdBean.setContent(modifytime);
        newsRrdBean.setModifyTime(content);
        newsRrdBean.setPicture1("?");
        newsRrdBean.setPicture2("?");
        newsRrdBean.setPicture3("?");
        newsRrdBean.setPicture4("?");
        newsRrdBean.setPicture5("?");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        Log.e(TAG, "postString: " + s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, NewsInfoEntity.class, "", new HttpManager.ResultCallback<NewsInfoEntity>() {
            @Override
            public void onSuccess(String json, NewsInfoEntity newsInfoEntity) throws InterruptedException {
                if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0) {
                    pb.setVisibility(View.GONE);
                    final String picture1 = newsInfoEntity.getNewsRrd().get(0).getPicture1();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setImageBitmap(stringtoBitmap(picture1));
                        }
                    });
                    final String picture2 = newsInfoEntity.getNewsRrd().get(0).getPicture2();
                    final String picture3 = newsInfoEntity.getNewsRrd().get(0).getPicture3();
                    final String picture4 = newsInfoEntity.getNewsRrd().get(0).getPicture4();
                    final String picture5 = newsInfoEntity.getNewsRrd().get(0).getPicture5();

                }
            }

            @Override
            public void onFailure(String msg) {

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
