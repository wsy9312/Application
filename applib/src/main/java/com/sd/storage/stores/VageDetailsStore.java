package com.sd.storage.stores;


;
import com.sd.storage.actions.CommentModel;
import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.model.WeekMeunModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 详情
 */
public class VageDetailsStore extends Store<Action> {


    private ArrayList<VageModel> vageModels = new ArrayList<>();

    private ArrayList<CommentModel> commentModels = new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public VageDetailsStore() {

    }

    public ArrayList<CommentModel> getCommentModels() {
        return commentModels;
    }

    public VageModel getVageModel() {
        if (vageModels.size() > 0) {
            return vageModels.get(0);
        }
        return null;

    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.VEGEDETAILS:
                DataContainer<ArrayList<VageModel>> vageModelDataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vageModels = vageModelDataContainer.data;
                    dispatcherStore(new VegetChange());
                } else {
                    dispatcherStore(new VegeChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.VAGECOMMENTLIST:
                DataContainer<ArrayList<VageModel>> dataContainer = (DataContainer<ArrayList<VageModel>>) action.getData();
                if (dataContainer.getResultOK()) {
                    commentModels = dataContainer.data.get(0).coms;
                    dispatcherStore(new CommentListChange());
                } else {
                    dispatcherStore(new CommentListChangeError(dataContainer.code, dataContainer.message));
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

    public static class CommentListChange {
    }

    public static class CommentListChangeError extends ErrorState {
        CommentListChangeError(int state, String msge) {
            super(state, msge);
        }
    }

    public static class VegetChange {
    }

    public static class VegeChangeError extends ErrorState {
        VegeChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
