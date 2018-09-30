package com.sd.storage.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.sd.storage.Display;
import com.sd.storage.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by MrZhou on 2016/9/26.
 */
public abstract class BaseFragment extends Fragment{

    private Toolbar mToolbar;

    protected Context mContext;

    Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        View toolbarView = view.findViewById(R.id.toolbar);
        if (toolbarView != null) {
            mToolbar = (Toolbar) toolbarView;
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            if(null != tv_title){
                tv_title.setText(getToolbarTitle());
            }
            setToolbar(mToolbar);
        }
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    protected String getToolbarTitle(){
        return getString(R.string.app_name);
    }

    protected final void setSupportActionBar(Toolbar toolbar) {
        if(getActivity() instanceof  BaseActivity){
            ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected Display getDisplay() {
        return ((BaseActivity) getActivity()).getDisplay();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
