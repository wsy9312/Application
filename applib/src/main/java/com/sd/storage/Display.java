package com.sd.storage;


import com.sd.storage.dlib.widgets.dialog.DialogControl;

/**
 * Created by MrZhou on 2017/5/13.
 */

public interface Display extends DialogControl {

    void setSupportActionBar(Object toolbar);


    void startWeekMenuActivity();

    void startVageDetailsActivity(String  vegeid);


    void startAddCommentActivity(String vegeid);

    void startMeunOrderActivity();

    void startSearchVageActivity();

    void startMeunManageActivity();

    void startManageSearchActivity();

    void startAddNewVegeActivity();

    void startMansageEditActivity();

    void startVoteManageActivity();

    void startOrderActivity();


    void startSetTimeActivity();

    void startSetMainActivity();


    void startAddWeekMeunActivity(String dayid,String typeid);

    void showWaittingDialog(String text);

    void showWaittingDialog();

    void hideWaittingDialog();









}