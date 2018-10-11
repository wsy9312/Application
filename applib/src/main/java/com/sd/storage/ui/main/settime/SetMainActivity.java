package com.sd.storage.ui.main.settime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.ui.base.BaseSCActivity;

/**
 * Created by Administrator on 2018-09-21.
 */

public class SetMainActivity extends BaseSCActivity implements View.OnClickListener {

//    @BindView(R.id.tv_title)
    TextView tv_title;
    LinearLayout im_back;
    TextView tv_meun_setting;
    TextView tv_time_setting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        init();
    }


    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        tv_meun_setting = (TextView) findViewById(R.id.tv_meun_setting);
        tv_time_setting = (TextView) findViewById(R.id.tv_time_setting);
        im_back.setOnClickListener(this);
        tv_meun_setting.setOnClickListener(this);
        tv_time_setting.setOnClickListener(this);

        tv_title.setText(R.string.menu_mannage);
    }


//    @OnClick({R.id.im_back, R.id.tv_meun_setting, R.id.tv_time_setting})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.tv_meun_setting) {//菜谱投票
            getDisplay().startMeunManageActivity();

        } else if (i == R.id.tv_time_setting) {//菜谱排行
            getDisplay().startSetTimeActivity();

        }
    }

    @Override
    protected void initReturnEvent() {

    }

    @Override
    public Store[] getStoreArray() {
        return new Store[0];
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setmain;
    }
}
