package com.sd.storage.actions;


import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.model.VoteTimeModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * 设置时间
 */

public class TimeActionsCreator extends ActionsCreator {



    @Inject
    public TimeActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 设置时间
     */
    public void setTime(String vtstart,String vtend) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vtstart",vtstart);
        params.put("vtend",vtend);
        putObsToSubscriber(mApiService.setTime(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SETTIME, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 投票时间
     */
    public void selectAllVoteTime() {

        HashMap<String, String> params = new HashMap<>();

        putObsToSubscriber(mApiService.selectAllVoteTime(params), new CallBack<DataContainer<ArrayList<VoteTimeModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VoteTimeModel>> arrayListDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SELECTTIME, arrayListDataContainer));
            }
        });

    }


    public void voteTime() {
        HashMap<String, String> params = new HashMap<>();

        putObsToSubscriber(mApiService.voteTime(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VOTETIME, weekMeunModelDataContainer));
            }
        });

    }




}
