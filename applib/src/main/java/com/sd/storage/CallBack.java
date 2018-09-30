package com.sd.storage;



import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;

import rx.Subscriber;

/**
 * Created by MrZhou on 2016/10/1.
 */
public abstract class CallBack<T extends DataContainer> extends Subscriber<T> {

    private ActionsCreator mActionsCreator;

    private T t;

    public CallBack(ActionsCreator creator){
        mActionsCreator = creator;
    }

    /**
     * 返回正确状态数据
     * @param t
     */
    public void doResultOK(T t){

    }

    @Override
    public void onCompleted() {

        doNext(t);
        // 返回正确状态数据
        if(null != t && t.getResultOK()){
            doResultOK(t);
        }else{
            // 处理状态不对处理
            mActionsCreator.dispatchAction(new Action<>(Action.STATE_ERROR_ACTION, t));
        }
    }

    @Override
    public void onError(Throwable e) {
        // 错误处理
        mActionsCreator.dispatchAction(new Action<>(Action.STATE_ERROR_ACTION, e));
    }

    @Override
    public void onNext(T t) {
        this.t = t;
    }

    /**
     * 完成数据状态，200
     * @param t
     */
    protected abstract void doNext(T t);



}
