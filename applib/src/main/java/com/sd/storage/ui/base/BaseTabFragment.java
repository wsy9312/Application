package com.sd.storage.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MrZhou on 2016/9/28.
 */
public abstract class BaseTabFragment extends BaseSCFragment{
    // 缓存视图内容
    private View mViewContent;

    protected boolean isLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViewContent == null) {
            mViewContent = inflater.inflate(getLayoutId(), container, false);
        }
        // 缓存View判断是否含有parent, 如果有需要从parent删除, 否则发生已有parent的错误.
        ViewGroup parent = (ViewGroup) mViewContent.getParent();
        if (parent != null) {
            parent.removeView(mViewContent);
        }
        return mViewContent;
    }

    @Override
    public void onDestroy() {
        mViewContent = null;
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isLoaded = false;
    }

    public abstract int getLayoutId();

}
