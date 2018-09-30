package com.sd.storage.dlib.bus;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by MrZhou on 2016/11/7.
 */
public abstract class ICompositeSubscription {

    CompositeSubscription mCompositeSubscription;

    public ICompositeSubscription(){
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * 添加到订阅管理
     * @param subscription
     */

    public Subscription putSubscription(Subscription subscription){
        mCompositeSubscription.add(subscription);
        return subscription;
    }

    /**
     * 取消订阅
     */
    public void unSubscribe(){
        if(null == mCompositeSubscription){
            return;
        }
        mCompositeSubscription.clear();
    }
}
