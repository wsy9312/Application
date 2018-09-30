package com.sd.storage.dlib.action;

import java.util.HashMap;

/**
 * Created by MrZhou on 2016/9/26.
 */
public class Action<D> {

    public static final String ERROR_ACTION = "error";

    public static final String STATE_ERROR_ACTION = "state_error";

    private final String type;
    private D data;

    private HashMap<String, Object> bundle;


    public Action(String type, D data) {
        this.type = type;
        this.data = data;
        bundle = new HashMap<>();
    }

    public void putExtre(String key, Object value){
        bundle.put(key, value);
    }

    public Object getExtreValue(String key){
        return bundle.get(key);
    }

    public String getType() {
        return type;
    }

    public D getData() {
        return data;
    }
}
