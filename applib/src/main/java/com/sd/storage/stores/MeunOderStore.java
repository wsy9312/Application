package com.sd.storage.stores;


import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 排序
 */
public class MeunOderStore extends Store<Action> {


    private ArrayList<VageModel> vageModels;

    private double allSize;


    @Inject
    AppStore mAppStore;

    @Inject
    public MeunOderStore() {

    }

    public double getAllSize() {
        return allSize;
    }

    public ArrayList<VageModel> getVageModels() {
        return vageModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.MEUNORDERLIST:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new MeunOrderListtChange());
                } else {
                    dispatcherStore(new MeunOderListChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.VOTESIZE:
            DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
            if (objectDataContainer.getResultOK()) {
                allSize = (Double) objectDataContainer.data;
                dispatcherStore(new SizeChange());
            } else {
                dispatcherStore(new SizeChangeError(objectDataContainer.code, objectDataContainer.message));
            }
            break;

        }
    }


    public static class SizeChange {
    }

    public static class SizeChangeError extends ErrorState {
        SizeChangeError(int state, String msge) {
            super(state, msge);
        }
    }



    public static class MeunOrderListtChange {
    }

    public static class MeunOderListChangeError extends ErrorState {
        MeunOderListChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
