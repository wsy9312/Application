package com.sd.storage.dlib.store;

import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.bus.ICompositeSubscription;
import com.sd.storage.dlib.bus.RxBus;
import com.sd.storage.dlib.model.DataContainer;

import java.net.UnknownHostException;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;


/**
 * Created by MrZhou on 2016/9/26.
 */
public abstract class Store<T extends Action> extends ICompositeSubscription {

    private RxBus mRxBus;

    public Store() {
        mRxBus = new RxBus();
    }

    /**
     * 发送事件
     *
     * @param object
     */
    public void dispatcherStore(Object object) {
        this.mRxBus.post(object);
    }

    public void onAction(T action) {
        switch (action.getType()) {

            case Action.STATE_ERROR_ACTION:
                Object object = action.getData();
                if (null != object) {
                    // 网络连接错误
                    if (action.getData() instanceof UnknownHostException) {
                        dispatcherStore(new AppErrorState(606));
                        return;
                    }
                    if (object instanceof DataContainer) {
                        DataContainer container = (DataContainer) action.getData();
                        // tokenId 不可用
                        if (container.code == 300) {
                            dispatcherStore(new AppErrorState(300));
                        } else {
                            dispatcherStore(new ErrorState(container.ErrCode, container.message));
                        }
                        return;
                    }

                    // 系统错误
                    dispatcherStore(new AppErrorState(500));
                }
                break;
            default:
                try {
                    doAction(action);
                } catch (ClassCastException ex) {
                    return;
                }
                break;
        }
    }

    /**
     * 添加观察者
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> type) {
        return mRxBus.toObservable(type);
    }


    public <T> Subscription toSubscription(Class<T> type, Action1<T> action1, Scheduler scheduler) {
        return mRxBus.toSubscription(type, action1, scheduler);
    }

    /**
     * 在主线程里执行订阅者
     *
     * @param type
     * @param action1
     * @param <T>
     * @return
     */
    public <T> Subscription toMainSubscription(Class<T> type, Action1<T> action1) {
        Subscription sub = mRxBus.toSubscription(type, action1);
        return putSubscription(sub);
    }

    public abstract void doAction(T action);

    public static class NetErrorEvent {
    }

    public static class ErrorState {

        public int state;
        public String msge;

        public ErrorState(int state) {
            this.state = state;
        }

        public ErrorState(int state, String msge) {
            this.state = state;
            this.msge = msge;
        }

        public ErrorState() {
        }
    }

    public static class AppErrorState {

        public int state;

        public AppErrorState(int state) {
            this.state = state;
        }
    }

}
