package com.sd.storage.actions;


import com.sd.storage.dlib.action.Action;

/**
 * Created by lenovo on 2018/1/18.
 */

public class MeunAction extends Action {
    public static final String MEUNTYPE = "meun_type";
    public static final String ADDCOMMENT = "addcomment";
    public static final String VAGECOMMENTLIST = "vagecommentlist";
    public static final String VEGEDETAILS = "vegedetails";
    public static final String SETGIVE = "setgive";
    public static final String VAGESEARCHLIST = "vagesearchlist";
    public static final String MEUNORDERLIST = "meunorderlist";

    public static final String ALLMEUNLIST = "allmeunlist";

    public static final String ADDVEGEORDER = "addvegeorder";

    public static final String DELETEVEGEORDER = "deletevegeorder";

    public static final String ASSLASTLIST = "addlastList";

    public static final String SELECTVEGELIST = "selectvegelist";

    public static final String VEGEVOTELIST = "vegevotelist";

    public static final String SETVOTE = "setvote";

    public static final String ADDNEWVEGE = "addnewvege";

    public static final String DELETEVEGE = "deleteVege";

    public static final String SEARCHSTORE = "searchstroe";


    public static final String DELETEFOODSTROE = "deleteFoodstore";

    public static final String ADDVEGEWEEK = "addvegeweek";

    public static final String SETTIME = "settime";

    public static final String VOTETIME = "voteTime";

    public static final String SELECTTIME = "selecttime";

    public static final String VOTESIZE = "votesize";

    public MeunAction(String type, Object data) {
        super(type, data);
    }


}
