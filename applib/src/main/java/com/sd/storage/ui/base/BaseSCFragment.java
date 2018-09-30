package com.sd.storage.ui.base;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.common.ErrorStateCode;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.dlib.utils.ToastUtils;
import com.sd.storage.listener.OnErrorReloadListener;

import rx.functions.Action1;

/**
 * 基本的Store 和 Creator类型Fragment，需要设置相应的store和creator,实现注册与销毁事件
 * Created by MrZhou on 2016/11/7.
 */
public abstract class BaseSCFragment extends BaseFragment implements OnErrorReloadListener {

    @Override
    public void onResume() {
        if(null != getActionsCreator()){
            for (Store store: getStoreArray()) {
                getActionsCreator().register(store);
            }
        }
         initReturnEvent();
        super.onResume();
    }

    protected void initReturnEvent(){
        /**
         * 全局错误数据
         */
        if(getStoreArray().length > 0){
            getStoreArray()[0].toMainSubscription(Store.AppErrorState.class, getAppErrorState());
        }
    }

    @Override
    public void onPause() {
        if(null != getActionsCreator()){
            for (Store store: getStoreArray()) {
                getActionsCreator().unregister(store);
            }
        }
        unSubscribe();
        super.onPause();
    }


    public abstract Store[] getStoreArray();

    public abstract ActionsCreator getActionsCreator();

    /**
     * 全局共通错误处理
     */
    public Action1<Store.AppErrorState> getAppErrorState(){

        return new Action1<Store.AppErrorState>() {
            @Override
            public void call(Store.AppErrorState errorState) {
                if(null == getView()){
                    return;
                }
                switch (errorState.state){
                    // tokenId失效
                    case ErrorStateCode.TOKEN_ID_LOSE:
                        ToastUtils.showBaseToast(getString(R.string.user_logined_change_retry_login), mContext);
                        StorageApplication.getApplication().removeUser();
                        onErrorLoginStateChange();
                        break;
                    // 网络错误的
                    case ErrorStateCode.NETWORK_ERROR:
                        new AlertDialog.Builder(mContext).setMessage(R.string.network_failed).setPositiveButton(R.string.reset_try, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onErrorReload();
                            }
                        }).setNegativeButton(R.string.cancel, null).create().show();
                        onErrorNetWorkChange();

                        break;
                    // 页面找不到错误的
                    case ErrorStateCode.SERVER_ERROR:
                        new AlertDialog.Builder(mContext).setMessage(R.string.service_failed).setPositiveButton(R.string.reset_try, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onErrorReload();
                            }
                        }).setNegativeButton(R.string.cancel, null).create().show();
                        onErrorServerChange();
                        break;
                }
            }
        };
    }

    /**
     * 登录状态发生改变
     */
    protected void onErrorLoginStateChange(){}

    /**
     * 网络错误
     */
    protected void onErrorNetWorkChange(){}

    /**
     * 服务器错误
     */
    protected void onErrorServerChange(){}

    @Override
    public void onErrorReload() {
        if(getActivity() instanceof OnErrorReloadListener){
            ((OnErrorReloadListener) getActivity()).onErrorReload();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void unSubscribe(){
        for (Store store: getStoreArray()) {
            store.unSubscribe();
        }
        if(null != getActionsCreator()){
            getActionsCreator().unSubscribe();
        }
    }
}
