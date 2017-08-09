package com.example.hgtxxgl.application.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;

//首页
public class LibMainActivity extends AppCompatActivity implements HandToolbar.OnButtonsClickCallback {

    private static final String TAG = "LibMainActivity";
    private HandToolbar handToolbar;
    private String username;
    public static final String USER_NAME = "USER_NAME";
    public static final String PASS_WORD = "PASS_WORD";
    private boolean loginSucceed = false;
    private RadioGroup bottomBar;
    private Fragment[] fragments = new Fragment[6];
    private FragmentManager supportFragmentManager;
    private int lastIndex = -1, currentIndex;
    private static final int LOGIN_SUCESS = 0;
    private static final int LOGIN_FAILED = 1;
    private String[] title;
    private String password;
    private RadioButton newsCenter;
    private RadioButton notificationCenter;
    private RadioButton todoCenter;
    private RadioButton launchCenter;
//    private RadioButton applyCarCenter;
    private RadioButton applyPeoPleCenter;
    private RadioButton personalCenter;

    //底部菜单栏单选按钮监听器
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            lastIndex = currentIndex;
            //角标选中新闻中心radiobutton
            if (checkedId == R.id.rb_main_news_center) {
                //设置当前按钮角标位置
                currentIndex = 0;
                //根据fragment实例改变当前界面的fragment
                changeFragment(fragments[0]);
                fragments[0].onPause();
                //根据当前的fragment自定义toolbar动态设定标题
                handToolbar.setTitle(title[0]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            } else if (checkedId == R.id.rb_main_notification_center) {
                currentIndex = 1;
                changeFragment(fragments[1]);
                fragments[1].onPause();
                handToolbar.setTitle(title[1]);
                handToolbar.setBackHome(true,LibMainActivity.this,0);

            } else if (checkedId == R.id.rb_main_leave_todo_center) {
                currentIndex = 2;
                changeFragment(fragments[2]);
                fragments[2].onPause();
                handToolbar.setTitle(title[2]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            } else if (checkedId == R.id.rb_main_leave_launch_center){
                currentIndex = 3;
                changeFragment(fragments[3]);
                fragments[3].onPause();
                handToolbar.setTitle(title[3]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            }/*else if (checkedId == R.id.rb_main_leave_apply_car_center){
                currentIndex = 4;
                changeFragment(fragments[4]);
                fragments[4].onPause();
                handToolbar.setTitle(title[4]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            }*/ else if (checkedId == R.id.rb_main_leave_apply_people_center){
                currentIndex = 4;
                changeFragment(fragments[4]);
                fragments[4].onPause();
                handToolbar.setTitle(title[4]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            }
            else if (checkedId == R.id.rb_main_personal_center) {
                currentIndex = 5;
                changeFragment(fragments[5]);
                fragments[5].onPause();
                handToolbar.setTitle(title[5]);
                handToolbar.setBackHome(false,LibMainActivity.this,0);

            }
        }
    };

    /**
     * 调用入口
     * @param context
     * @param username
     */
    public static void startActivity(Context context, String username, String password) {
        Intent intent = new Intent(context, LibMainActivity.class);
        intent.putExtra(USER_NAME, username);
        intent.putExtra(PASS_WORD,password);
        context.startActivity(intent);
    }

    //初始化设置toolbar标题
    private void initTitle() {
        title = new String[]{"新闻","通知","审批","查看"/*,"车辆"*/,"请假","我的"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化设置toolbar标题
        initTitle();
        //初始化控件
        initView();
        //接收登录界面传递的用户名密码参数
        acceptParam();
        supportFragmentManager = getSupportFragmentManager();
        //初始化fragment(首页六个子界面)
        initFragment(false);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(LibMainActivity.this);
    }

    //接收登录界面传递的用户名密码参数
    private void acceptParam() {
        username = getIntent().getStringExtra(USER_NAME);
        password = getIntent().getStringExtra(PASS_WORD);
        if (username != null && password != null) {
            login(username,password);
        } else {
            show("未输入登陆用户");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //登录验证
    private void login(final String username, String password) {
        loginSucceed = true;
        handler.sendEmptyMessage(LOGIN_SUCESS);
    }

    //弹吐丝
    private void show(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ApplicationApp.context,msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //根据fragment实例改变当前界面的fragment
    public void changeFragment(Fragment frag) {
        if (frag == null) return;
        if (lastIndex == currentIndex) {
            return;
        }
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        if (lastIndex < currentIndex) {
            transaction.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out, R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        } else {
            transaction.setCustomAnimations(R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out, R.anim.fragment_slide_left_out, R.anim.fragment_slide_right_in);
        }
        transaction.hide(fragments[lastIndex]);
        transaction.show(frag);
        transaction.commitNow();
    }

    //填充布局、初始化控件
    private void initView() {
        setContentView(R.layout.lib_activity_main);
        //pb = (ProgressBar) findViewById(R.id.lib_pb);
        handToolbar = (HandToolbar) findViewById(R.id.toolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setTitleSize(20);
        bottomBar = (RadioGroup) findViewById(R.id.bottom_bar);
        newsCenter = (RadioButton) findViewById(R.id.rb_main_news_center);
        notificationCenter = (RadioButton) findViewById(R.id.rb_main_notification_center);
        todoCenter = (RadioButton) findViewById(R.id.rb_main_leave_todo_center);
        launchCenter = (RadioButton) findViewById(R.id.rb_main_leave_launch_center);
//        applyCarCenter = (RadioButton) findViewById(R.id.rb_main_leave_apply_car_center);
        applyPeoPleCenter = (RadioButton) findViewById(R.id.rb_main_leave_apply_people_center);
        personalCenter = (RadioButton) findViewById(R.id.rb_main_personal_center);
        bottomBar.setOnCheckedChangeListener(listener);
    }

    //初始化首页三个子fragment
    private void initFragment(boolean loginSucceed) {
        if(fragments == null) return;
        if (loginSucceed) {
            if (supportFragmentManager.getFragments() != null && supportFragmentManager.getFragments().size() != 0) {
                supportFragmentManager.getFragments().clear();
            }
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            fragments[0] = DetailFragment.newInstance(PageConfig.PAGE_NEWS);
            fragments[1] = DetailFragment.newInstance(PageConfig.PAGE_NOTIFICATION);
            fragments[2] = DetailFragment.newInstance(PageConfig.PAGE_TODO);
            fragments[3] = DetailFragment.newInstance(PageConfig.PAGE_LAUNCH);
//            fragments[4] = DetailFragment.newInstance(PageConfig.PAGE_APPLY_CAR);
            fragments[4] = DetailFragment.newInstance(PageConfig.PAGE_APPLY_PEOPLE);
            fragments[5] = DetailFragment.newInstance(PageConfig.PAGE_ME);
            for (int i = 0; i < fragments.length; i++) {
                transaction.add(R.id.fl_container, fragments[i]);
                transaction.hide(fragments[i]);
            }
            if(transaction!=null){
                transaction.show(fragments[0]);
                transaction.commitNow();
            }
            currentIndex = 0;
        }
    }

    //创建handler处理登录逻辑
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == LOGIN_SUCESS) {
                initFragment(true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsCenter.setEnabled(true);
                        newsCenter.setBackgroundColor(0xffffff);
                        notificationCenter.setEnabled(true);
                        notificationCenter.setBackgroundColor(0xffffff);
                        todoCenter.setEnabled(true);
                        todoCenter.setBackgroundColor(0xffffff);
                        launchCenter.setEnabled(true);
                        launchCenter.setBackgroundColor(0xffffff);
//                        applyCarCenter.setEnabled(true);
//                        applyCarCenter.setBackgroundColor(0xffffff);
                        applyPeoPleCenter.setEnabled(true);
                        applyPeoPleCenter.setBackgroundColor(0xffffff);
                        personalCenter.setEnabled(true);
                        personalCenter.setBackgroundColor(0xffffff);
                    }
                });
                //pb.setVisibility(View.GONE);
            } else if (what == LOGIN_FAILED) {
                initFragment(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsCenter.setEnabled(false);
                        newsCenter.setBackgroundColor(0xd4d4d4);
                        notificationCenter.setEnabled(false);
                        notificationCenter.setBackgroundColor(0xd4d4d4);
                        todoCenter.setEnabled(false);
                        todoCenter.setBackgroundColor(0xd4d4d4);
                        launchCenter.setEnabled(false);
                        launchCenter.setBackgroundColor(0xd4d4d4);
//                        applyCarCenter.setEnabled(false);
//                        applyCarCenter.setBackgroundColor(0xd4d4d4);
                        applyPeoPleCenter.setEnabled(false);
                        applyPeoPleCenter.setBackgroundColor(0xd4d4d4);
                        personalCenter.setEnabled(false);
                        personalCenter.setBackgroundColor(0xd4d4d4);
                    }
                });
                //pb.setVisibility(View.VISIBLE);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onButtonClickListner(HandToolbar.VIEWS views, int radioIndex) {

    }

}
