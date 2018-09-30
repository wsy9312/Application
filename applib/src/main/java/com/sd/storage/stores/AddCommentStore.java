package com.sd.storage.stores;



import com.sd.storage.actions.MeunAction;
import com.sd.storage.dlib.action.Action;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.WeekMeunModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 添加评论
 */
public class AddCommentStore extends Store<Action> {


    @Inject
    AppStore mAppStore;

    @Inject
    public AddCommentStore() {

    }

    @Override
    public void doAction(Action action) {
        switch (action.getType()) {
            case MeunAction.ADDCOMMENT:
                DataContainer<Object> addcommentContainer = (DataContainer<Object>) action.getData();
                if (addcommentContainer.getResultOK()) {
                    dispatcherStore(new AddCommentChange());
                } else {
                    dispatcherStore(new AddCommentChangeError(addcommentContainer.code, addcommentContainer.message));
                }
                break;

        }
    }


    public static class AddCommentChange {
    }

    public static class AddCommentChangeError extends ErrorState {
        AddCommentChangeError(int state, String msge) {
            super(state, msge);
        }
    }

}
