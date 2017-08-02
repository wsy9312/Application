package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hgtxxgl.application.entity.LeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;

import java.util.ArrayList;
import java.util.List;

public class RestApplyFragment extends CommonFragment {
    private LeaveEntity bean;

    public RestApplyFragment() {
    }

    public static RestApplyFragment newInstance() {
        RestApplyFragment restApplyFragment = new RestApplyFragment();
        return restApplyFragment;
    }

    public static RestApplyFragment newInstance(Bundle bundle) {
        RestApplyFragment fragment = new RestApplyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (getArguments() != null){

        }
    }

    @Override
    public List<Group> getGroupList() {
        List<Group> groups = new ArrayList<>();
        if (bean == null) {
            List<HandInputGroup.Holder> subHolder = new ArrayList<>();
            subHolder.add(new HandInputGroup.Holder("请假类别", false, false, "/请选择", HandInputGroup.VALUE_TYPE.BUTTONS));
            subHolder.add(new HandInputGroup.Holder("申请人", false, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder.add(new HandInputGroup.Holder("预计外出时间", false, false, "/请选择", HandInputGroup.VALUE_TYPE.DATE));
            subHolder.add(new HandInputGroup.Holder("预计归来时间", false, false, "/请选择", HandInputGroup.VALUE_TYPE.DATE));
            subHolder.add(new HandInputGroup.Holder("请假原因", false, false, "/请填写", HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder.add(new HandInputGroup.Holder("是否取消请假", false, false, "否", HandInputGroup.VALUE_TYPE.BUTTONS));
            subHolder.add(new HandInputGroup.Holder("是否后补请假", false, false, "否", HandInputGroup.VALUE_TYPE.BUTTONS));
            groups.add(new CommonFragment.Group("基本信息", null, true, null, subHolder));
        }else {
            List<HandInputGroup.Holder> subHolder = new ArrayList<>();
            List<LeaveEntity.ApplyBean> apply = bean.getApply();
            for (int i = 0; i < apply.size(); i++) {

            }
            subHolder.add(new HandInputGroup.Holder("请假类别", false, false, apply.get(i), HandInputGroup.VALUE_TYPE.BUTTONS));
            subHolder.add(new HandInputGroup.Holder("申请人", false, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder.add(new HandInputGroup.Holder("预计外出时间", false, false, "/请选择", HandInputGroup.VALUE_TYPE.DATE));
            subHolder.add(new HandInputGroup.Holder("预计归来时间", false, false, "/请选择", HandInputGroup.VALUE_TYPE.DATE));
            subHolder.add(new HandInputGroup.Holder("请假原因", false, false, "/请填写", HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder.add(new HandInputGroup.Holder("是否取消请假", false, false, "否", HandInputGroup.VALUE_TYPE.BUTTONS));
            subHolder.add(new HandInputGroup.Holder("是否后补请假", false, false, "否", HandInputGroup.VALUE_TYPE.BUTTONS));
            groups.add(new CommonFragment.Group("基本信息", null, true, null, subHolder));
        }
        return null;
    }
}