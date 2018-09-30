package com.sd.storage.ui.base;


import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.listener.OnErrorReloadListener;

import rx.functions.Action1;

/**
 * 基本的Store 和 Creator类型Activity，需要设置相应的store和creator,实现注册与销毁事件
 * Created by MrZhou on 2016/11/7.
 */
public abstract class BaseSCActivity extends BaseActivity  implements OnErrorReloadListener {


    @Override
    protected void onResume() {
        if (null != getActionsCreator()) {
            for (Store store : getStoreArray()) {
                getActionsCreator().register(store);
            }
        }
        initEvent();
        super.onResume();
    }

    protected void  initEvent() {
        /**
         * 全局错误数据
         */
        if (getStoreArray().length > 0) {
            getStoreArray()[0].toMainSubscription(Store.AppErrorState.class, getAppErrorState());
        }
        initReturnEvent();
    }

    protected abstract void initReturnEvent();

    /**
     * 错误处理
     */
    private Action1<Store.AppErrorState> getAppErrorState(){

        return new Action1<Store.AppErrorState>() {
            @Override
            public void call(Store.AppErrorState errorState) {
                switch (errorState.state){
                /*    // tokenId失效
                    case ErrorStateCode.TOKEN_ID_LOSE:
                        ToastUtils.showBaseToast(getString(R.string.user_logined_change_retry_login), BaseSCActivity.this);
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
                        onErrorNetworkChange();
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
                        break;*/
                }
            }
        };
    }

    /**
     * 网络错误
     */
    protected void onErrorNetworkChange(){
    }

    /**
     * 服务器错误
     */
    protected void onErrorServerChange(){
    }

    @Override
    public void onErrorReload() {

    }

    @Override
    protected void onPause() {
        if (null != getActionsCreator()) {
            for (Store store : getStoreArray()) {
                getActionsCreator().unregister(store);
            }
        }
        unSubscribe();
        super.onPause();
    }

    public abstract Store[] getStoreArray();

    public abstract ActionsCreator getActionsCreator();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void unSubscribe() {
        for (Store store : getStoreArray()) {
            store.unSubscribe();
        }
        if (null != getActionsCreator()) {
            getActionsCreator().unSubscribe();
        }
    }
}
