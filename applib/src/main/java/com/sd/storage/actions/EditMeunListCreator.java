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
 * 编辑
 */

public class EditMeunListCreator extends ActionsCreator {

    @Inject
    public EditMeunListCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 获取列表信息
     */
    public void getVegeMeunList() {

        HashMap<String, String> params = new HashMap<>();

        putObsToSubscriber(mApiService.getSelectVegeList(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SELECTVEGELIST, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 添加
     * @param vegeid
     */
    public void addVegeOrder(String vegeid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        putObsToSubscriber(mApiService.addVegeOrderLast(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ASSLASTLIST, objectDataContainer));
            }
        });

    }

    /**
     * 删除
     * @param vegeid
     */
    public void deleteVegeOrder(String vegeid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        putObsToSubscriber(mApiService.deleteVegeOrder(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.DELETEVEGEORDER, objectDataContainer));
            }
        });

    }




}
