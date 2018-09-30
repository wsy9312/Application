package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 总菜品库
 */
public class AllMeunStore extends Store<Action> {


    private ArrayList<VageModel> vageModels=new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public AllMeunStore() {

    }

    public ArrayList<VageModel> getVageModels() {
        return vageModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.ALLMEUNLIST:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new AllMeunListChange());
                } else {
                    dispatcherStore(new AllMeunListChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.ADDVEGEORDER:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new AddChange());
                } else {
                    dispatcherStore(new AddChangeError(objectDataContainer.code, objectDataContainer.message));
                }
                break;

            case MeunAction.SEARCHSTORE:
                DataContainer<ArrayList<VageModel>> vageDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageDataContainer.getResultOK()) {
                    vageModels = vageDataContainer.data;
                    dispatcherStore(new SearhListChange());
                } else {
                    dispatcherStore(new SearchListChangeError(vageDataContainer.code, vageDataContainer.message));
                }
                break;

            case MeunAction.DELETEFOODSTROE:
                DataContainer<Object> objectContainer = (DataContainer<Object>) action.getData();
                if (objectContainer.getResultOK()) {
                    dispatcherStore(new DeleteChange());
                } else {
                    dispatcherStore(new DeleteChangeError(objectContainer.code, objectContainer.message));
                }
                break;
            case MeunAction.ADDVEGEWEEK:
                DataContainer<Object> objectContainer2 = (DataContainer<Object>) action.getData();
                if (objectContainer2.getResultOK()) {
                    dispatcherStore(new AddVegeChange());
                } else {
                    dispatcherStore(new AddVegeChangeError(objectContainer2.code, objectContainer2.message));
                }
                break;





        }
    }
    public static class AddChange {
    }

    public static class AddChangeError extends ErrorState {
        AddChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class AllMeunListChange {
    }

    public static class AllMeunListChangeError extends ErrorState {
        AllMeunListChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class SearhListChange {
    }

    public static class SearchListChangeError extends ErrorState {
        SearchListChangeError(int state, String msge) {
            super(state, msge);
        }
    }


    public static class DeleteChange {
    }

    public static class DeleteChangeError extends ErrorState {
        DeleteChangeError(int state, String msge) {
            super(state, msge);
        }
    }


    public static class AddVegeChange {
    }

    public static class AddVegeChangeError extends ErrorState {
        AddVegeChangeError(int state, String msge) {
            super(state, msge);
        }
    }


}
