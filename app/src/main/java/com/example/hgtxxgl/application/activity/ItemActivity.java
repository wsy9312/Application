package com.example.hgtxxgl.application.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.apply.CarApplyFragment;
import com.example.hgtxxgl.application.fragment.apply.RestApplyFragment;
import com.example.hgtxxgl.application.fragment.approve.RestApproveCarFragment;
import com.example.hgtxxgl.application.fragment.approve.RestApprovePeopleFragment;
import com.example.hgtxxgl.application.fragment.detail.RestDetailCarFragment;
import com.example.hgtxxgl.application.fragment.detail.RestDetailPeopleFragment;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;

import java.util.List;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_detail_item);
        checkFragmentForLoading(getIntent().getIntExtra(PageConfig.PAGE_CODE, -1));
        SysExitUtil.activityList.add(ItemActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode,resultCode,data);
        }
    }

    void checkFragmentForLoading(int pageCode) {
        switch (pageCode) {
            case PageConfig.PAGE_APPLY_PEOPLE_OUT:
                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_CAR:
                checkFragment(CarApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_PEOPLE_IN:
                show("暂未开放");
//                checkFragment(OutVisitFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_DISPATCH_PEOPLE_OWN:
                show("暂未开放");
//                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_DISPATCH_PEOPLE_ELSE:
                show("暂未开放");
//                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_DISPATCH_CAR_OWN:
                show("暂未开放");
//                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_DISPATCH_CAR_ELSE:
                show("暂未开放");
//                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LEAVE_APPROVE_CAR:
                checkFragment(RestApproveCarFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LEAVE_APPROVE_PEOPLE:
                checkFragment(RestApprovePeopleFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LEAVE_DETAIL_CAR:
                checkFragment(RestDetailCarFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LEAVE_DETAIL_PEOPLE:
                checkFragment(RestDetailPeopleFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
        }
    }

    private void show(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getApplicationContext(),msg);
            }
        });
    }

    private void checkFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        int intExtra = getIntent().getIntExtra(PageConfig.PAGE_CODE, -1);
        if (intExtra == PageConfig.PAGE_APPLY_PEOPLE_OUT||intExtra == PageConfig.PAGE_APPLY_CAR||
            intExtra == PageConfig.PAGE_APPLY_PEOPLE_IN||intExtra == PageConfig.PAGE_APPLY_DISPATCH_PEOPLE_OWN||
            intExtra == PageConfig.PAGE_APPLY_DISPATCH_PEOPLE_ELSE||intExtra == PageConfig.PAGE_APPLY_DISPATCH_CAR_OWN||
            intExtra == PageConfig.PAGE_APPLY_DISPATCH_CAR_ELSE){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(R.string.make_sure_back_current_apply);
            builder.setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ItemActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {
            super.onBackPressed();
        }
    }
}
