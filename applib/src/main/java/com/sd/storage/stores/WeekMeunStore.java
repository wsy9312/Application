package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.WeekMeunModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 本周菜谱
 */
public class WeekMeunStore extends Store<Action> {


    public  ArrayList<WeekMeunModel>  weekMeunModels=new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public WeekMeunStore() {

    }

    public ArrayList<WeekMeunModel> getWeekMeunModels() {
        return weekMeunModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.MEUNTYPE:
                DataContainer<ArrayList<WeekMeunModel>> dataLoginContainer = (DataContainer<ArrayList<WeekMeunModel>>) action.getData();
                if (dataLoginContainer.getResultOK()) {
                    weekMeunModels = dataLoginContainer.data;
                    dispatcherStore(new MeunTypeChange());
                } else {
                    dispatcherStore(new MeunTypeChangeError(dataLoginContainer.code, dataLoginContainer.message));
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

            case MeunAction.DELETEVEGE:
                DataContainer<Object> objectContainer = (DataContainer<Object>) action.getData();
                if (objectContainer.getResultOK()) {
                    dispatcherStore(new DeletChange());
                } else {
                    dispatcherStore(new DeleteChangeError(objectContainer.code, objectContainer.message));
                }
                break;




        }
    }

    public static class DeletChange {
    }

    public static class DeleteChangeError extends ErrorState {
        DeleteChangeError(int state, String msge) {
            super(state, msge);
        }
    }
    public static class GiveChange {
    }

    public static class GiveChangeError extends ErrorState {
        GiveChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class MeunTypeChange {
    }

    public static class MeunTypeChangeError extends ErrorState {
        MeunTypeChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
