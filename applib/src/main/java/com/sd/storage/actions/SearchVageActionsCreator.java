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
 * 详情
 */

public class SearchVageActionsCreator extends ActionsCreator {

    @Inject
    public SearchVageActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 搜索
     */
    public void getSearchList(String vegename,String userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegename",vegename);
        params.put("userid",userid);
        putObsToSubscriber(mApiService.getSearchList(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VAGESEARCHLIST, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 点赞
     * @param vegeid
     * @param userid
     */
    public void setGive(String vegeid,String  userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        params.put("userid",userid);
        putObsToSubscriber(mApiService.setGive(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SETGIVE, objectDataContainer));
            }
        });

    }




}
