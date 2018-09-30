package com.sd.storage.actions;


import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.model.WeekMeunModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * 本周菜谱
 */

public class WeekMeunActionsCreator extends ActionsCreator {

    @Inject
    public WeekMeunActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 本周菜谱
     */
    public void getMeunType(String dayid ,String userid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("dayid",dayid);
        params.put("userid",userid);
        putObsToSubscriber(mApiService.WeekMeun(params), new CallBack<DataContainer<ArrayList<WeekMeunModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<WeekMeunModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.MEUNTYPE, weekMeunModelDataContainer));
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

    /**
     * 删除本周菜谱
     * @param foodid
     */
    public void deleteVege(String foodid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("foodid",foodid);
        putObsToSubscriber(mApiService.deleteVege(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.DELETEVEGE, objectDataContainer));
            }
        });

    }




}
