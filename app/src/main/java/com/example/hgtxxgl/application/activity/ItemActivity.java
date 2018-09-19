package com.example.hgtxxgl.application.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.apply.CarApplyFragment;
import com.example.hgtxxgl.application.fragment.apply.GoOutApplyFragment;
import com.example.hgtxxgl.application.fragment.apply.RestApplyFragment;
import com.example.hgtxxgl.application.fragment.apply.SickApplyFragment;
import com.example.hgtxxgl.application.fragment.apply.WorkLeaveApplyFragment;
import com.example.hgtxxgl.application.fragment.approve.CarApproveDelayListFragment;
import com.example.hgtxxgl.application.fragment.approve.CarApproveDetailFragment;
import com.example.hgtxxgl.application.fragment.approve.CarApproveFinishListFragment;
import com.example.hgtxxgl.application.fragment.approve.CarApproveListFragment;
import com.example.hgtxxgl.application.fragment.approve.PeopleApproveDelayListFragment;
import com.example.hgtxxgl.application.fragment.approve.PeopleApproveDetailFragment;
import com.example.hgtxxgl.application.fragment.approve.PeopleApproveFinishListFragment;
import com.example.hgtxxgl.application.fragment.approve.PeopleApproveListFragment;
import com.example.hgtxxgl.application.fragment.approve.RestApproveCarFragment;
import com.example.hgtxxgl.application.fragment.detail.CarDetailFragment;
import com.example.hgtxxgl.application.fragment.detail.CarDetailListFragment;
import com.example.hgtxxgl.application.fragment.detail.PeopleDetailFragment;
import com.example.hgtxxgl.application.fragment.detail.PeopleDetailListFragment;
import com.example.hgtxxgl.application.fragment.detail.RestDetailCarFragment;
import com.example.hgtxxgl.application.fragment.total.chart.ChartFragment;
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

            case PageConfig.PAGE_LEAVE_APPROVE_CAR:
                checkFragment(RestApproveCarFragment.newInstance(getIntent().getBundleExtra("data")));
                break;

            case PageConfig.PAGE_LEAVE_DETAIL_CAR:
                checkFragment(RestDetailCarFragment.newInstance(getIntent().getBundleExtra("data")));
                break;

            //810
            case PageConfig.PAGE_APPLY_PEOPLE_SHI:
                checkFragment(WorkLeaveApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_PEOPLE_BING:
                checkFragment(SickApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_PEOPLE_XIU:
                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_PEOPLE_WAI:
                checkFragment(GoOutApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_CAR:
                checkFragment(CarApplyFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_APPLY_PEOPLE_WU:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_COMMISSION_PEOPLE_TOTAL:
                checkFragment(PeopleApproveListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_PEOPLE_DELAY_LIST:
                checkFragment(PeopleApproveDelayListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_PEOPLE_FINISH_LIST:
                checkFragment(PeopleApproveFinishListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_PEOPLE_DETAIL:
                checkFragment(PeopleApproveDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LAUNCH_PEOPLE_LIST:
                checkFragment(PeopleDetailListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LAUNCH_PEOPLE_DETAIL:
                checkFragment(PeopleDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LAUNCH_CAR_LIST:
                checkFragment(CarDetailListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_LAUNCH_CAR_DETAIL:
                checkFragment(CarDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_CAR_TOTAL:
                checkFragment(CarApproveListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_CAR_DELAY_LIST:
                checkFragment(CarApproveDelayListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_CAR_FINISH_LIST:
                checkFragment(CarApproveFinishListFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_COMMISSION_CAR_DETAIL:
                checkFragment(CarApproveDetailFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            case PageConfig.PAGE_CAR_APPROVE:
                checkFragment(new TempFragment());
                break;

            case PageConfig.PAGE_XINGZHENG:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_YIJIAN:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_CHART:
                checkFragment(ChartFragment.newInstance(getIntent().getBundleExtra("data")));
                break;
            //914生活
            case PageConfig.PAGE_FLOW_PRESENTFOOD:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_TOUPIAO:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_PAIHANG:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_MANAGE:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_VIDEO:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_BOOK:
                checkFragment(new TempFragment());
                break;
            case PageConfig.PAGE_FLOW_TINENG:
                checkFragment(new TempFragment());
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
        if (intExtra == PageConfig.PAGE_APPLY_PEOPLE_SHI||intExtra == PageConfig.PAGE_APPLY_PEOPLE_BING||
            intExtra == PageConfig.PAGE_APPLY_PEOPLE_XIU||intExtra == PageConfig.PAGE_APPLY_PEOPLE_WAI||
            intExtra == PageConfig.PAGE_APPLY_CAR){
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
