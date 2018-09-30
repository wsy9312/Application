package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 编辑
 */
public class EditMeunStore extends Store<Action> {


    private ArrayList<VageModel> vageModels=new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public EditMeunStore() {

    }

    public ArrayList<VageModel> getVageModels() {
        return vageModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.SELECTVEGELIST:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new SelectMeunListChange());
                } else {
                    dispatcherStore(new SelectMeunListChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.ASSLASTLIST:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new AddChange());
                } else {
                    dispatcherStore(new AddChangeError(objectDataContainer.code, objectDataContainer.message));
                }
                break;
            case MeunAction.DELETEVEGEORDER:
                DataContainer<Object> dataContainer = (DataContainer<Object>) action.getData();
                if (dataContainer.getResultOK()) {
                    dispatcherStore(new DeleteChange());
                } else {
                    dispatcherStore(new DeleteChangeError(dataContainer.code, dataContainer.message));
                }
                break;




        }
    }

    public static class DeleteChange {
    }

    public static class DeleteChangeError extends ErrorState {
        DeleteChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class AddChange {
    }

    public static class AddChangeError extends ErrorState {
        AddChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class SelectMeunListChange {
    }

    public static class SelectMeunListChangeError extends ErrorState {
        SelectMeunListChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
