package com.sd.storage.dlib.action;

import com.sd.storage.dlib.model.DataContainer;

/**
 * Created by MrZhou on 2016/10/27.
 */
public class DataContainerAction<D> extends Action<DataContainer<D>> {

    public DataContainerAction(String type, DataContainer<D> data) {
        super(type, data);
    }
}
