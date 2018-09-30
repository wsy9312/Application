package com.sd.storage;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sd.storage.dialog.WaitingDialog;
import com.sd.storage.dialog.WaittingDialogHelper;
import com.sd.storage.dlib.utils.StringUtils;
import com.sd.storage.dlib.widgets.dialog.DialogHelper;
import com.sd.storage.dlib.widgets.dialog.WaitDialog;
import com.sd.storage.ui.main.OrderActivity;
import com.sd.storage.ui.main.addcommend.AddCommentActivity;
import com.sd.storage.ui.main.meunmanage.MeunManageActivity;
import com.sd.storage.ui.main.meunmanage.addnewvege.AddNewVegeActivity;
import com.sd.storage.ui.main.meunmanage.addweekmeun.AddWeekMeunActivity;
import com.sd.storage.ui.main.meunmanage.manageedit.MansageEditActivity;
import com.sd.storage.ui.main.meunmanage.managesearch.ManageSearchActivity;
import com.sd.storage.ui.main.meunorder.MeunOrderActivity;
import com.sd.storage.ui.main.search.SearchVageActivity;
import com.sd.storage.ui.main.settime.SetMainActivity;
import com.sd.storage.ui.main.settime.SetTimeActivity;
import com.sd.storage.ui.main.vagedetails.VageDetailsActivity;
import com.sd.storage.ui.main.votemanage.VoteManageActivity;
import com.sd.storage.ui.main.weekmeun.WeekMenuActivity;

/**
 * Created by MrZhou on 2017/5/13.
 */
public class AndroidDisplay implements Display {

    private AppCompatActivity mActivity;
    private Toolbar mToolbar;

    public AndroidDisplay(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Override
    public void setSupportActionBar(Object toolbar) {
        mToolbar = (Toolbar) toolbar;

        if (mToolbar != null) {
            final ActionBar ab = mActivity.getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowTitleEnabled(false);
                ab.setHomeButtonEnabled(true);
                ab.setHomeAsUpIndicator(R.mipmap.ic_back_grey_v2);
            }
        }
    }





    @Override
    public void startWeekMenuActivity() {
        startActivity(WeekMenuActivity.class);
    }

    @Override
    public void startVageDetailsActivity(String vegeid) {
        Intent intent = new Intent(mActivity,VageDetailsActivity.class);
        intent.putExtra("vegeid", vegeid);
        startActivity(intent);
    }

    @Override
    public void startAddCommentActivity(String vegeid) {
        Intent intent = new Intent(mActivity,AddCommentActivity.class);
        intent.putExtra("vegeid", vegeid);
        startActivity(intent);

    }

    @Override
    public void startAddWeekMeunActivity(String dayid,String typeid) {
        Intent intent = new Intent(mActivity,AddWeekMeunActivity.class);
        intent.putExtra("dayid", dayid);
        intent.putExtra("typeid", typeid);
        startActivity(intent);

    }

    @Override
    public void startMeunOrderActivity() {
        startActivity(MeunOrderActivity.class);
    }

    @Override
    public void startSearchVageActivity() {
        startActivity(SearchVageActivity.class);
    }

    @Override
    public void startMeunManageActivity() {
        startActivity(MeunManageActivity.class);
    }

    @Override
    public void startManageSearchActivity() {
        startActivity(ManageSearchActivity.class);
    }

    @Override
    public void startAddNewVegeActivity() {
        startActivity(AddNewVegeActivity.class);
    }

    @Override
    public void startMansageEditActivity() {
        startActivity(MansageEditActivity.class);
    }

    @Override
    public void startVoteManageActivity() {
        startActivity(VoteManageActivity.class);
    }

    @Override
    public void startOrderActivity() {
        startActivity(OrderActivity.class);
    }


    @Override
    public void startSetTimeActivity() {
        startActivity(SetTimeActivity.class);
    }

    @Override
    public void startSetMainActivity() {
        startActivity(SetMainActivity.class);

    }


    public Intent initIntent(Class<?> clazz) {
        Intent initIntent = new Intent(mActivity, clazz);
        return initIntent;
    }

    public void startActivity(Class<?> clazz) {
        mActivity.startActivity(new Intent(mActivity, clazz));
    }



    public void startActivity(Intent intent) {
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    public void startForActivityResult(Intent intent, int requestCode) {
        mActivity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void hideWaitDialog() {
        if (null != mWaitDialog && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    @Override
    public void showWaitDialog() {
        showWaitDialog("");
    }

    @Override
    public void showWaitDialog(int resid) {
        String title = mActivity.getString(resid);
        showWaitDialog(title);
    }

    private WaitDialog mWaitDialog;

    @Override
    public void showWaitDialog(String text) {
        if (null == mWaitDialog) {
            mWaitDialog = DialogHelper.getWaitDialog(mActivity, mActivity.getString(R.string.loading_please_wait));
        }
        if (!mWaitDialog.isShowing()) {
            if (!StringUtils.isBlank(text)) {
                mWaitDialog.setMessage(text);
            }
            mWaitDialog.show();
        }
    }

    private WaitingDialog waitingDialog;

    @Override
    public void showWaittingDialog() {
        showWaittingDialog("");
    }

    @Override
    public void hideWaittingDialog() {
        if (null != waitingDialog && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        }
    }


    @Override
    public void showWaittingDialog(String text) {
        if (null == waitingDialog) {
            waitingDialog = WaittingDialogHelper.getWaitDialog(mActivity, mActivity.getString(R.string.loading_please_wait));
        }
        if (!waitingDialog.isShowing()) {
          /*  if (!StringUtils.isBlank(text)) {
                waitingDialog.setMessage(text);
            }*/
            waitingDialog.show();
        }
    }


}
