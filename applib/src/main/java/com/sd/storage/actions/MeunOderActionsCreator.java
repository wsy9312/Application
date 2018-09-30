package com.sd.storage.actions;


import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * 排序
 */

public class MeunOderActionsCreator extends ActionsCreator {

    @Inject
    public MeunOderActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 排序
     */
    public void getVegegiveOrder(String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type",type);
        putObsToSubscriber(mApiService.getVegegiveOrder(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.MEUNORDERLIST, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 请求总票数
     */
    public void voteSize( ) {
        HashMap<String, String> params = new HashMap<>();
        putObsToSubscriber(mApiService.voteSize(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VOTESIZE, weekMeunModelDataContainer));
            }
        });

    }









}
