package com.sd.storage.actions;


import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.model.VegeTitle;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * 详情
 */

public class VageDetailsActionsCreator extends ActionsCreator {

    @Inject
    public VageDetailsActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 详情
     */
    public void getVeveDetails(String vegeid,String userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        params.put("userid",userid);
        putObsToSubscriber(mApiService.getVageDetails(params), new CallBack<DataContainer<VegeTitle>>(this) {
            @Override
            protected void doNext(DataContainer<VegeTitle> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VEGEDETAILS, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 评论列表
     */
    public void getCommentList(String vegeid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        putObsToSubscriber(mApiService.getCommentList(params), new CallBack<DataContainer<ArrayList<CommentModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<CommentModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.VAGECOMMENTLIST, weekMeunModelDataContainer));
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
