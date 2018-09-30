package com.sd.storage.actions;


import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.bus.ICompositeSubscription;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.store.Store;


import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ActionsCreator extends ICompositeSubscription {

    protected Dispatcher mDispatcher;

    protected ApiService mApiService;

    @Inject
    public ActionsCreator(Dispatcher dispatcher, ApiService apiService){
        mDispatcher = dispatcher;
        mApiService = apiService;
    }

    public void dispatchAction(Action action) {
        mDispatcher.dispatch(action);
    }

    public void register(Store store) {
        mDispatcher.register(store);
    }

    public void unregister(Store store){
        mDispatcher.unregister(store);
    }

    /**
     * 添加线程切换以及网络重试观察者
     * @param observable
     * @param subscriber#
     * @return subscription
     */
  /*  public <T> Subscription putObsToSubscriber(Observable<T> observable, Subscriber<T> subscriber){
        Subscription subscription =
                observable.retryWhen(new RetryWhenProcess(2)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        putSubscription(subscription);
        return subscription;
    }*/


    public <T> Subscription putObsToSubscriber(Observable<T> observable, Subscriber<T> subscriber){
        Subscription subscription =
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        putSubscription(subscription);
        return subscription;
    }


}
