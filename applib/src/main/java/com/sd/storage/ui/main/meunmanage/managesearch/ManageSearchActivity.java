package com.sd.storage.ui.main.meunmanage.managesearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.ui.base.BaseSCActivity;

/**
 * Created by Administrator on 2018-09-13.
 */

public class ManageSearchActivity extends BaseSCActivity implements View.OnClickListener {


//    @BindView(R.id.et_search)
    EditText et_search;
    LinearLayout im_back;
    ImageView im_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageApplication.getApplication().getAppComponent().inject(this);
        init();
    }

    public void init() {
        et_search = (EditText) findViewById(R.id.et_search);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_search = (ImageView) findViewById(R.id.im_search);
        im_back.setOnClickListener(this);
        im_search.setOnClickListener(this);
    }

//    @OnClick({R.id.im_back, R.id.im_search})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_search) {
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
        return R.layout.activity_managesearch;
    }
}
