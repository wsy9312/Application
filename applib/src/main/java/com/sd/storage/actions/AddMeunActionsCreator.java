package com.sd.storage.actions;

import com.sd.storage.CallBack;
import com.sd.storage.api.ApiService;
import com.sd.storage.dlib.dispatcher.Dispatcher;
import com.sd.storage.dlib.model.DataContainer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 总菜品库
 */

public class AddMeunActionsCreator extends ActionsCreator {

    @Inject
    public AddMeunActionsCreator(Dispatcher dispatcher, ApiService apiService) {
        super(dispatcher, apiService);
    }



    /**
     * 添加
     * @param vegename
     * @param vegedesc
     * @param vegeimg
     * @param heatid
     */
    public void addVege(String vegename, String vegedesc, String vegeimg, String heatid) {

        HashMap<String, String> params = new HashMap<>();
        params.put("vegename",vegename);
        params.put("vegedesc",vegedesc);
        params.put("vegeimg",vegeimg);
        params.put("heatid",heatid);
        putObsToSubscriber(mApiService.addNewVege(params), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> objectDataContainer) {

                dispatchAction(new MeunAction(MeunAction.ADDNEWVEGE, objectDataContainer));
            }
        });

    }

    public void uploadImage(String vegename, String vegedesc, String heatid,File vegeimg) {


            // 创建 RequestBody，用于封装 请求RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), vegeimg);
            MultipartBody.Part body = MultipartBody.Part.createFormData("myFile", vegeimg.getName(), requestFile);

        Map<String, String> params = new HashMap<>();
        params.put("vegename",vegename);
        params.put("vegedesc",vegedesc);
        params.put("heatid",heatid);

        putObsToSubscriber(mApiService.uploadImage(params, body), new CallBack<DataContainer<Object>>(this) {
            @Override
            protected void doNext(DataContainer<Object> dataContainer) {

                dispatchAction(new MeunAction(MeunAction.ADDNEWVEGE, dataContainer));

            }
        });

    }


}
