package com.example.hgtxxgl.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.RestApplyFragment;
import com.example.hgtxxgl.application.fragment.RestApproveCarFragment;
import com.example.hgtxxgl.application.fragment.RestApprovePeopleFragment;
import com.example.hgtxxgl.application.fragment.RestDetailCarFragment;
import com.example.hgtxxgl.application.fragment.RestDetailPeopleFragment;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.PageConfig;

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
            case PageConfig.PAGE_LEAVE_APPLY_PEOPLE:
                checkFragment(RestApplyFragment.newInstance(getIntent().getBundleExtra("data")));
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

    private void checkFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        ItemActivity.super.onBackPressed();
    }
}
