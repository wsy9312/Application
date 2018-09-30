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
 * T投票
 */

public class VoteCreator extends ActionsCreator {

    @Inject
    public VoteCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 获取列表信息
     */
    public void getVoteListList(String userid,String heatid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("userid",userid);
        params.put("heatid",heatid);
        putObsToSubscriber(mApiService.getVoteListList(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VEGEVOTELIST, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 投票
     * @param vegeid
     */
    public void setVote(String vegeid,String userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        params.put("userid",userid);
        putObsToSubscriber(mApiService.setVote(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SETVOTE, objectDataContainer));
            }
        });

    }


}
