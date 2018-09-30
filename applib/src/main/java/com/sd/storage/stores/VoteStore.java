package com.sd.storage.stores;


import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 投票
 */
public class VoteStore extends Store<Action> {


    private ArrayList<VageModel> vageModels=new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public VoteStore() {

    }

    public ArrayList<VageModel> getVageModels() {
        return vageModels;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.VEGEVOTELIST:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new VegeVoteListChange());
                } else {
                    dispatcherStore(new VegeVoteListChangeeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.SETVOTE:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new VoteChange());
                } else {
                    dispatcherStore(new VoteChangeError(objectDataContainer.code, objectDataContainer.message));
                }
                break;



        }
    }
    public static class VoteChange {
    }

    public static class VoteChangeError extends ErrorState {
        VoteChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class VegeVoteListChange {
    }

    public static class VegeVoteListChangeeError extends ErrorState {
        VegeVoteListChangeeError(int state, String msge) {
            super(state, msge);
        }
    }

}
