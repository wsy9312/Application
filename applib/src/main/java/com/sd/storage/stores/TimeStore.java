package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.model.VoteTimeModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 设置时间
 */
public class TimeStore extends Store<Action> {


    private ArrayList<VoteTimeModel> voteTimeModels = new ArrayList<>();

    private VoteTimeModel voteTimeModel;


    @Inject
    AppStore mAppStore;

    @Inject
    public TimeStore() {

    }

    public VoteTimeModel getVoteTimeModel() {

        if (null != voteTimeModels && voteTimeModels.size() > 0) {
            return voteTimeModels.get(0);
        }
        return null;

    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.SETTIME:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new SetTimeChange());
                } else {
                    dispatcherStore(new SetTimeChangeError(objectDataContainer.code, objectDataContainer.message));
                }
                break;
            case MeunAction.SELECTTIME:
                DataContainer<ArrayList<VoteTimeModel>> arrayListDataContainer = (DataContainer<ArrayList<VoteTimeModel>>) action.getData();
                if (arrayListDataContainer.getResultOK()) {
                    voteTimeModels = arrayListDataContainer.data;
                    dispatcherStore(new SelectTimeChange());
                } else {
                    dispatcherStore(new SelectTimeChangeError(arrayListDataContainer.code, arrayListDataContainer.message));
                }
                break;
            case MeunAction.VOTETIME:
                DataContainer<Object> objecContainer = (DataContainer<Object>) action.getData();
                if (objecContainer.getResultOK()) {
                    dispatcherStore(new VoteTimeChange());
                } else {
                    dispatcherStore(new VoteTimeChangeError(objecContainer.code, objecContainer.message));
                }
                break;


        }
    }

    public static class SetTimeChange {
    }

    public static class SetTimeChangeError extends ErrorState {
        SetTimeChangeError(int state, String msge) {
            super(state, msge);
        }
    }


    public static class SelectTimeChange {
    }

    public static class SelectTimeChangeError extends ErrorState {
        SelectTimeChangeError(int state, String msge) {
            super(state, msge);
        }
    }
    public static class VoteTimeChange {
    }

    public static class VoteTimeChangeError extends ErrorState {
        VoteTimeChangeError(int state, String msge) {
            super(state, msge);
        }
    }


}
