package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 搜索
 */
public class SearchVageStore extends Store<Action> {


    private ArrayList<VageModel> vageModels = new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public SearchVageStore() {

    }

    public ArrayList<VageModel> getVageModels() {
        return vageModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.VAGESEARCHLIST:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new VageSearchListChange());
                } else {
                    dispatcherStore(new VageSearchListChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.SETGIVE:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new GiveChange());
                } else {
                    dispatcherStore(new GiveChangeError(objectDataContainer.code, objectDataContainer.message));
                }
                break;

        }
    }

    public static class GiveChange {
    }

    public static class GiveChangeError extends ErrorState {
        GiveChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class VageSearchListChange {
    }

    public static class VageSearchListChangeError extends ErrorState {
        VageSearchListChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
