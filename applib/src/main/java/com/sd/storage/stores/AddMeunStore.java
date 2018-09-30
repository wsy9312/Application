package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 添加菜谱
 */
public class AddMeunStore extends Store<Action> {



    @Inject
    AppStore mAppStore;

    @Inject
    public AddMeunStore() {

    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.ADDNEWVEGE:
                DataContainer<Object> objectDataContainer = (DataContainer<Object>) action.getData();
                if (objectDataContainer.getResultOK()) {
                    dispatcherStore(new AddChange());
                } else {
                    dispatcherStore(new AddChangeError(objectDataContainer.code, objectDataContainer.message));
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


}
