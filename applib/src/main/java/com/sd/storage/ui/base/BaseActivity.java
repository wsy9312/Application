package com.sd.storage.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sd.storage.AndroidDisplay;
import com.sd.storage.Display;
import com.sd.storage.R;
import com.sd.storage.dlib.comm.CommonMessageDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrZhou on 2016/9/26.
 */
public abstract class BaseActivity extends AppCompatActivity{

    private Display mDisplay;

    protected Context mContext;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnbinder = ButterKnife.bind(this);
        mContext = this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if(null != toolbar){
            TextView tv_title = (TextView) findViewById(R.id.tv_title);
            if(null != tv_title){
                tv_title.setText(getToolbarTitle());
            }
//            setToolbar(toolbar);
//        }
//        if(isStatusBarChange()){
//            Helper.statusBarLightMode(this);
//        }
    }

//    protected boolean isStatusBarChange(){
//        return true;
//    }

    protected String getToolbarTitle(){
        return getString(R.string.app_name);
    }

    protected final void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getDisplay().setSupportActionBar(toolbar);
    }

    protected abstract int getLayoutId();

    public Display getDisplay(){
        if(null == mDisplay){
            mDisplay = new AndroidDisplay(this);
        }
        return mDisplay;
    }

    public void setDisplay(Display mDisplay) {
        this.mDisplay = mDisplay;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 登录状态发生改变
     */
    protected void onErrorLoginStateChange(){

    }

    @Override
    protected void onDestroy() {
        setDisplay(null);
        if(null != mCommonMessageDialog){
            mCommonMessageDialog.dismiss();
        }
        mUnbinder.unbind();
        super.onDestroy();
    }

    CommonMessageDialog mCommonMessageDialog;

    protected void showAlertDialog(String msge, View.OnClickListener onClickListener){
        if(mCommonMessageDialog != null && mCommonMessageDialog.isShowing()){
            return;
        }
        mCommonMessageDialog = new CommonMessageDialog(mContext, msge, onClickListener);
        mCommonMessageDialog.show();
    }
}
