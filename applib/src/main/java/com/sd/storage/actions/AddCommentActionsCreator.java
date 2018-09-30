package com.sd.storage.actions;


import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * 添加评论
 */

public class AddCommentActionsCreator extends ActionsCreator {

    @Inject
    public AddCommentActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 添加评论
     */
    public void addComment(String vegeid ,String comreview,String username ) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        params.put("comreview",comreview);
        params.put("username",username);
        putObsToSubscriber(mApiService.AddComment(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ADDCOMMENT, objectDataContainer));
            }
        });

    }




}
