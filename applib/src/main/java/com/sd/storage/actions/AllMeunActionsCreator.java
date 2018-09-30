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
 * 总菜品库
 */

public class AllMeunActionsCreator extends ActionsCreator {

    @Inject
    public AllMeunActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }


    /**
     * 获取列表信息
     */
    public void getAllMeunList() {

        HashMap<String, String> params = new HashMap<>();

        putObsToSubscriber(mApiService.getAllMeunList(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ALLMEUNLIST, weekMeunModelDataContainer));
            }
        });

    }

    /**
     * 添加到购物车
     * @param vegeid
     */
    public void addVegeOrder(String vegeid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        putObsToSubscriber(mApiService.addVegeOrder(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ADDVEGEORDER, objectDataContainer));
            }
        });

    }

    /**
     * 搜索
     */
    public void getSearchList(String vegename) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegename",vegename);
        putObsToSubscriber(mApiService.getSearchStoreList(params), new CallBack<DataContainer<ArrayList<VageModel>>>(this) {
            @Override
            protected void doNext(DataContainer<ArrayList<VageModel>> weekMeunModelDataContainer) {

                dispatchAction(new MeunAction(MeunAction.SEARCHSTORE, weekMeunModelDataContainer));
            }
        });

    }


    /**
     * 删除菜品库的item
     * @param vegeid
     */
    public void deleteFoodstore(String vegeid) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        putObsToSubscriber(mApiService.deleteFoodstore(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.DELETEFOODSTROE, objectDataContainer));
            }
        });

    }

    /**
     * 添加到菜谱库
     * @param vegeid
     */
    public void addVegeWeek(String vegeid,String vegename,String vegedesc,String vegeimg,String heatid,String dayid,String typeid ) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegeid",vegeid);
        params.put("vegename",vegename);
        params.put("vegedesc",vegedesc);
        params.put("vegeimg",vegeimg);
        params.put("heatid",heatid);
        params.put("dayid",dayid);
        params.put("typeid",typeid);
        putObsToSubscriber(mApiService.addVegeWeek(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ADDVEGEWEEK, objectDataContainer));
            }
        });

    }


}
