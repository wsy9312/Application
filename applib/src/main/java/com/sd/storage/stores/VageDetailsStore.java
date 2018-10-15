package com.sd.storage.stores;


import com.sd.storage.actions.CommentModel;
import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VegeTitle;

import java.util.ArrayList;

import javax.inject.Inject;

;

/**
 * 详情
 */
public class VageDetailsStore extends Store<Action> {





    private VegeTitle vegeTitle;

    private ArrayList<CommentModel> commentModels = new ArrayList<>();


    @Inject
    AppStore mAppStore;

    @Inject
    public VageDetailsStore() {

    }

    public ArrayList<CommentModel> getCommentModels() {
        return commentModels;
    }

    public VegeTitle getVegeTitle() {
        return vegeTitle;
    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.VEGEDETAILS:
                DataContainer<VegeTitle> vageModelDataContainer = (DataContainer<VegeTitle>) action.getData();
                if (vageModelDataContainer.getResultOK()) {
                    vegeTitle = vageModelDataContainer.data;
                    dispatcherStore(new VegetChange());
                } else {
                    dispatcherStore(new VegeChangeError(vageModelDataContainer.code, vageModelDataContainer.message));
                }
                break;
            case MeunAction.VAGECOMMENTLIST:
                DataContainer<ArrayList<CommentModel>> dataContainer = (DataContainer<ArrayList<CommentModel>>) action.getData();
                if (dataContainer.getResultOK()) {
                    commentModels = dataContainer.data;
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
