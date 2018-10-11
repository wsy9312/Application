package com.sd.storage.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.ui.base.BaseSCActivity;

/**
 * Created by Administrator on 2018-09-18.
 */

public class OrderActivity extends BaseSCActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        init();
    }


    public  void  init(){

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
        return R.layout.activity_order;
    }
}
