package com.example.hgtxxgl.application.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.fragment.NotificationBroadcast;
import com.example.hgtxxgl.application.fragment.OnUpdateUI;
import com.example.hgtxxgl.application.fragment.OnUpdateUINO;
import com.example.hgtxxgl.application.fragment.PersonalActivity;
import com.example.hgtxxgl.application.fragment.TodoTotalBroadcast;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.BadgeView;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import static org.litepal.LitePalApplication.getContext;

//首页
public class LibMainActivity extends AppCompatActivity implements HandToolbar.OnButtonsClickCallback, View.OnClickListener {

    private static final String TAG = "LibMainActivity";
    private HandToolbar handToolbar;
    private String username;
    public static final String USER_NAME = "USER_NAME";
    public static final String PASS_WORD = "PASS_WORD";
    private boolean loginSucceed = false;
    private RadioGroup bottomBar;
    private Fragment[] fragments = new Fragment[4];
    private FragmentManager supportFragmentManager;
    private int lastIndex = -1, currentIndex;
    private static final int LOGIN_SUCESS = 0;
    private static final int LOGIN_FAILED = 1;
    private String[] title;
    private String password;
    private RadioButton newsCenter;
    private RadioButton notificationCenter;
    private RadioButton todoCar;
    private RadioButton launchCar;
    private RadioButton todoCenter;
    private RadioButton launchCenter;
    private RadioButton launchTotal;
    private RadioButton todoTotal;
    private int screenHalf;
    private BadgeView badgeViewTodo;
    public static final String FLAG = "UPDATE";
    public static final String FLAGNOT = "UPDATENOT";
    TodoTotalBroadcast todoTotalBroadcast;
    NotificationBroadcast notificationBroadcast;
    HorizontalScrollView scrollView;
    private FloatingActionButton fbcPeople;
    private FloatingActionButton fbcApply;
    private FloatingActionsMenu fbcMenu;
    private long time = 0;
    private BadgeView badgeViewNoti;

    //底部菜单栏单选按钮监听器
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            int scrollX = scrollView.getScrollX();
//            RadioButton rb = (RadioButton) findViewById(checkedId);
//            int left = rb.getLeft();
//            int leftScreen = left-scrollX;
//            scrollView.smoothScrollBy((leftScreen-screenHalf), 0);

            lastIndex = currentIndex;
            //角标选中新闻中心radiobutton
            if (checkedId == R.id.rb_main_news_center) {
                //设置当前按钮角标位置
                currentIndex = 0;
                //根据fragment实例改变当前界面的fragment
                changeFragment(0);
                fragments[0].onPause();
                handToolbar.setTitle(title[0]);

            } else if (checkedId == R.id.rb_main_notification_center) {
                currentIndex = 1;
                changeFragment(1);
                fragments[1].onPause();
                handToolbar.setTitle(title[1]);
                badgeViewNoti.hide();

            } else if (checkedId == R.id.rb_main_leave_launch_total){
                currentIndex = 2;
                changeFragment(2);
                fragments[2].onPause();
                handToolbar.setTitle(title[2]);

            } else if (checkedId == R.id.rb_main_leave_todo_total) {
                currentIndex = 3;
                changeFragment(3);
                fragments[3].onPause();
                handToolbar.setTitle(title[3]);
                badgeViewTodo.hide();
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
        title = new String[]{getString(R.string.lib_titile_news_center),getString(R.string.lib_title_notification_center),getString(R.string.lib_title_apply_center),getString(R.string.lib_title_approve_center)};
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
        //初始化fragment
        initFragment(false);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(LibMainActivity.this);

        todoTotalBroadcast = new TodoTotalBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FLAG);
        registerReceiver(todoTotalBroadcast, intentFilter);
        todoTotalBroadcast.SetOnUpdateUI(new OnUpdateUI() {
            @Override
            public void updateUI(String i) {
                badgeViewTodo.show();
            }
        });

        notificationBroadcast = new NotificationBroadcast();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(FLAGNOT);
        registerReceiver(notificationBroadcast, intentFilter1);
        notificationBroadcast.SetOnUpdateUI(new OnUpdateUINO() {
            @Override
            public void updateUINO(String i) {
                badgeViewNoti.show();
            }

        });
        fbcPeople = (FloatingActionButton) findViewById(R.id.button_fbc_people);
        fbcApply = (FloatingActionButton) findViewById(R.id.button_fbc_apply);
        fbcMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions_up);
        fbcPeople.setOnClickListener(this);
        fbcApply.setOnClickListener(this);
//        Display d = getWindowManager().getDefaultDisplay();
//        DisplayMetrics dm = new DisplayMetrics();
//        d.getMetrics(dm);
//        //屏幕宽度的一半
//        screenHalf = d.getWidth()/5;
    }

    //接收登录界面传递的用户名密码参数
    private void acceptParam() {
        username = getIntent().getStringExtra(USER_NAME);
        password = getIntent().getStringExtra(PASS_WORD);
        if (username != null && password != null) {
            login(username,password);
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
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    //根据fragment实例改变当前界面的fragment
    public void changeFragment(int i) {
        if (fragments[i] == null) return;
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
        transaction.show(fragments[i]);
        transaction.commitNow();
    }

    //填充布局、初始化控件
    private void initView() {
        setContentView(R.layout.layout_lib_main);
        //pb = (ProgressBar) findViewById(R.id.lib_pb);fl_container
        handToolbar = (HandToolbar) findViewById(R.id.toolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, this);
        handToolbar.setBackHome(false,0);
        handToolbar.setButtonsClickCallback(this);
        handToolbar.setTitleSize(18);
        scrollView = (HorizontalScrollView) findViewById(R.id.scrollview);
        bottomBar = (RadioGroup) findViewById(R.id.bottom_bar);
        newsCenter = (RadioButton) findViewById(R.id.rb_main_news_center);
        notificationCenter = (RadioButton) findViewById(R.id.rb_main_notification_center);
        launchTotal = (RadioButton) findViewById(R.id.rb_main_leave_launch_total);
        todoTotal = (RadioButton) findViewById(R.id.rb_main_leave_todo_total);
        bottomBar.setOnCheckedChangeListener(listener);
        Button btnTodo = (Button) findViewById(R.id.btn_todo);
        Button btnNotification = (Button) findViewById(R.id.btn_notif);
        remindTodo(btnTodo);
        remindNoti(btnNotification);
    }

    private void remindNoti(View view) {
        badgeViewNoti = new BadgeView(this,view);
        badgeViewNoti.setText("");
        badgeViewNoti.setBadgePosition(BadgeView.POSITION_CUSTOM);
        badgeViewNoti.setTextColor(Color.WHITE);
        badgeViewNoti.setBadgeBackgroundColor(Color.RED);
        badgeViewNoti.setTextSize(7);
        badgeViewNoti.setBadgeMargin(1);
    }

    private void remindTodo(View view) {
        badgeViewTodo = new BadgeView(this, view);
        badgeViewTodo.setText("");
        badgeViewTodo.setBadgePosition(BadgeView.POSITION_CUSTOM);
        badgeViewTodo.setTextColor(Color.WHITE);
        badgeViewTodo.setBadgeBackgroundColor(Color.RED);
        badgeViewTodo.setTextSize(7);
        badgeViewTodo.setBadgeMargin(1);
    }

    //初始化首页四个子fragment
    private void initFragment(boolean loginSucceed) {
        if(fragments == null) return;
        if (loginSucceed) {
            if (supportFragmentManager.getFragments() != null && supportFragmentManager.getFragments().size() != 0) {
                supportFragmentManager.getFragments().clear();
            }
            FragmentTransaction transaction = supportFragmentManager.beginTransaction();
            fragments[0] = DetailFragment.newInstance(PageConfig.PAGE_NEWS);
            fragments[1] = DetailFragment.newInstance(PageConfig.PAGE_NOTIFICATION);
            fragments[2] = DetailFragment.newInstance(PageConfig.PAGE_LAUNCH_TOTAL);
            fragments[3] = DetailFragment.newInstance(PageConfig.PAGE_TODO_TOTAL);
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
                        launchTotal.setEnabled(true);
                        launchTotal.setBackgroundColor(0xffffff);
                        todoTotal.setEnabled(true);
                        todoTotal.setBackgroundColor(0xffffff);
                    }
                });
            } else if (what == LOGIN_FAILED) {
                initFragment(false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsCenter.setEnabled(false);
                        newsCenter.setBackgroundColor(0xd4d4d4);
                        notificationCenter.setEnabled(false);
                        notificationCenter.setBackgroundColor(0xd4d4d4);
                        launchTotal.setEnabled(false);
                        launchTotal.setBackgroundColor(0xd4d4d4);
                        todoTotal.setEnabled(false);
                        todoTotal.setBackgroundColor(0xd4d4d4);
                    }
                });
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onButtonClickListner(HandToolbar.VIEWS views, int radioIndex) {
        startActivity(new Intent(this, PersonalActivity.class));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 2000)) {
                Toast.makeText(this, R.string.again_back_to_desktop, Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_fbc_people:
                startActivity(new Intent(this,PersonalActivity.class));
                fbcMenu.collapse();
                break;
            case R.id.button_fbc_apply:
                Intent intent = new Intent(this, StartNewActivity.class);
//                intent.putExtra(PageConfig.PAGE_CODE, PAGE_APPLY_PEOPLE_OUT);
                startActivity(intent);
                fbcMenu.collapse();
                break;
        }
    }
}
