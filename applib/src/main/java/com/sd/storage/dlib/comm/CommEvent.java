package com.sd.storage.dlib.comm;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by MrZhou on 2016/11/30.
 */
public class CommEvent {

    private Fragment mFragment;
    private boolean isStack;
    private Bundle bundle;


    public CommEvent(Fragment mFragment, boolean isStack,
                         Bundle bundle) {
        super();
        this.mFragment = mFragment;
        this.isStack = isStack;
        this.bundle = bundle;
    }


    public Fragment getFragment() {
        return mFragment;
    }


    public Fragment getmFragment() {
        return mFragment;
    }


    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }


    public boolean isStack() {
        return isStack;
    }


    public void setStack(boolean isStack) {
        this.isStack = isStack;
    }


    public Bundle getBundle() {
        return bundle;
    }


    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
