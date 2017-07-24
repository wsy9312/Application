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
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.ApplicationApp;
import com.example.hgtxxgl.application.utils.CacheManger;
import com.example.hgtxxgl.application.utils.CommonValues;
import com.example.hgtxxgl.application.utils.GsonUtil;
import com.example.hgtxxgl.application.utils.StatusBarUtils;
import com.example.hgtxxgl.application.utils.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;


public class NewsItemActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NewsItemActivity";
    private TextView tv;
    private String title;
    private ImageView image;
    private HandToolbar handToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        Intent getIntent = getIntent();
        title = getIntent.getStringExtra("title");
        initview();
    }

    private void initview() {
        tv = (TextView) findViewById(R.id.tv_news_title);
        image = (ImageView) findViewById(R.id.image_news);
        image.setOnClickListener(this);
        tv.setText(title);
        handToolbar = (HandToolbar) findViewById(R.id.itemactivity_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(false,this,0);
        handToolbar.setTitle("新闻内容");
        handToolbar.setTitleSize(20);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                File file = base64ToFile(CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE));
//                image.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(file)));

                String data = CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE_TEMP);
                NewsInfoEntity newsInfoEntity = GsonUtil.parseJsonToBean(data, NewsInfoEntity.class);
                String s = newsInfoEntity.toString();
                Log.e(TAG, s);
                boolean empty = newsInfoEntity.getNewsRrd().isEmpty();
                if (empty){
                    ToastUtil.showToast(ApplicationApp.context,"为空");
                }
                String picture1 = newsInfoEntity.getNewsRrd().get(0).getPicture1();
                image.setImageBitmap(stringtoBitmap(picture1));
//                String substring = data.substring(data.lastIndexOf("/")-1,data.lastIndexOf("\"")-1);
//                Log.e(TAG, substring);
//                String data1 = CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE_TEMP);
//                String substring1 = data.substring(data.lastIndexOf("\"")+1);
//                Log.e(TAG, substring1);
//                String data2 = CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE);
//                String substring2 = data.substring(data.lastIndexOf("\"")+1);
//                Log.e(TAG, substring2);
//                Bitmap bitmap = stringtoBitmap(substring);
//                image.setImageBitmap(bitmap);

//                NewsInfoEntity newsInfoEntity = GsonUtil.parseJsonToBean(data, NewsInfoEntity.class);
//                String picture1 = newsInfoEntity.getNewsRrd().get(0).getPicture1();
//                Log.e(TAG, picture1);
            }
        });

    }
    public Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_news:
                ToastUtil.showToast(this,"这是图片");
                break;
        }
    }
    /*FileChooserLayout.ActivityResultCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail_item);
        checkFragmentForLoading(getIntent().getIntExtra(PageConfig.PAGE_CODE, -1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
        if(callback != null){
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    void checkFragmentForLoading(int pageCode) {
        switch (pageCode) {
            case PAGE_APPLY_BLEAVE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AddOrUpdateFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_EXTRAWORK:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ExtraWorkApplyFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_REST:
                //请假申请
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RestApplyFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_TRAVEL_OFFER:
                //差旅报销
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TravelApplyFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_PAYMENT_FLOW:
                //付款流程
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, PayFlowFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_EXPENSE_OFFER:
                //费用报销发起
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CostApplyFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PAGE_APPLY_ENTERTAINMENT_EXPENSE:
                //发起招待费
                checkFragment(EnterExpenseApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PAGE_APPLY_POST_FILE:
                //发文申请
                checkFragment(PostFileApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;

            case PageConfig.PAGE_DISPLAY_BLEAVE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, DisplayFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PageConfig.PAGE_DISPLAY_OVERTIME:
                checkFragment(ExtraWorkDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_REST:
                checkFragment(RestDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_EXPENSE:
                //费用明细
                checkFragment(CostDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_PAYMENT:
                checkFragment(PayFlowDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_TRAVEL:
                checkFragment(TravelOfferDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_EXPENSE_OFFER:
                //招待费明细
                checkFragment(EnterExpenseDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_DISPLAY_POST_FILE:
                //发文明细
                checkFragment(PostFileDetailFagment.newInstance(getIntent().getBundleExtra("data")));
                break;

            case PageConfig.PAGE_APPROVE_EXTRAWORK:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ExtraWorkApproveFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PageConfig.PAGE_APPROVE_REST:
                //请假审批
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, RestApproveFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PageConfig.PAGE_APPROVE_TRAVEL:
                //差旅审批
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, TravelApproveFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PageConfig.PAGE_APPROVE_EXPENSE_OFFER:
                //费用审批
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CostApproveFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                break;
            case PageConfig.PAGE_APPROVE_BLEAVE:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, BTripApproveFragment.newInstance(getIntent().getBundleExtra("data"))).commit();
                //公出审批
                break;
            case PageConfig.PAGE_APPROVE_PAYMENT_FLOW:
                checkFragment(PayFlowApproveFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPROVE_ENTER_EXPENSE:
                //招待费审批
                checkFragment(EnterExpenseApproveFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPROVE_POST_FILE:
                //发文审批
                checkFragment(PostFileApproverFragment.newInstance(getIntent().getBundleExtra("data")));
                break;

            case PageConfig.PAGE_DISPLAY_UNIFIED:
                checkFragment(DisplayUnified.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPROVE_UNIFIED:
                checkFragment(ApproveUnified.newInstance(getIntent().getBundleExtra("data")));
                break;
        }

    }

    private void checkFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void setCallback(FileChooserLayout.ActivityResultCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onBackPressed() {
        int intExtra = getIntent().getIntExtra(PageConfig.PAGE_CODE, -1);
        if (intExtra == PageConfig.PAGE_APPLY_BLEAVE || intExtra == PageConfig.PAGE_APPLY_EXTRAWORK ||intExtra == PageConfig.PAGE_APPLY_REST ||
                intExtra == PageConfig.PAGE_APPLY_TRAVEL_OFFER ||intExtra == PageConfig.PAGE_APPLY_PAYMENT_FLOW ||intExtra == PageConfig.PAGE_APPLY_EXPENSE_OFFER ||
                intExtra == PageConfig.PAGE_APPLY_ENTERTAINMENT_EXPENSE ||intExtra == PageConfig.PAGE_APPLY_POST_FILE ){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
            builder.setMessage("是否确认退出?"); //设置内容
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    NewsItemActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            //参数都设置完成了，创建并显示出来
            builder.create().show();

        }else {
            super.onBackPressed();
        }
    }

*/

}
