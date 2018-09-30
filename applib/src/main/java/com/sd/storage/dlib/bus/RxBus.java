package com.sd.storage.dlib.bus;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * 自定义Bus
 * Created by MrZhou on 2016/11/7.
 */
public class RxBus {


//    private static volatile RxBus instance;
    private final SerializedSubject<Object, Object> subject;

    public RxBus() {
        /**
         * PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
         */
        subject = new SerializedSubject<>(PublishSubject.create());
    }

//    public static RxBus getInstance() {
//        if (instance == null) {
//            synchronized (RxBus.class) {
//                if (instance == null) {
//                    instance = new RxBus();
//                }
//            }
//        }
//        return instance;
//    }

    public void post(Object object) {
        subject.onNext(object);
    }

    @NonNull
    public <T> Observable<T> toObservable(final Class<T> type) {
        return subject.ofType(type);
    }

    public boolean hasObservers() {
        return subject.hasObservers();
    }


    public <T> Subscription toSubscription(Class<T> type, Action1<T> action1) {
        return this
                .toObservable(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    public <T> Subscription toSubscription(Class<T> type, Action1<T> action1, Scheduler scheduler) {
        return this
                .toObservable(type)
                .subscribeOn(scheduler)
                .subscribe(action1);
    }

}
