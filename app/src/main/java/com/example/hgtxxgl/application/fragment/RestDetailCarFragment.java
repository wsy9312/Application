package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RestDetailCarFragment extends CommonFragment {


    private String SN;
    private final static String TAG = "RestDetailCarFragment";

    public RestDetailCarFragment(){
    }

    private CarLeaveEntity.CarLeaveRrdBean entity = null;

    public static RestDetailCarFragment newInstance(Bundle bundle) {
        RestDetailCarFragment fragment = new RestDetailCarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "车辆外出", HandInputGroup.VALUE_TYPE.TEXT));
        Group group = new Group("流程摘要-摘要内容", null, true, null, list).setrl(true).setv1(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName()).setv2(entity.getModifyTime());
        groups.add(group);
        groups.add(new Group("流程摘要-摘要",null,true,null,null).setrl(false));
        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("流程内容",true,false,"车辆外出",HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请人", false, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请车辆号牌", false, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", false, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", false, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("外出原因", false, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否取消请假", false, false, entity.getBCancel().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", false, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("详细信息-" + "基本信息", null, false, null, holderList).setrl(false));
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("车辆外出详情");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SN = getArguments().getString("SN");
        loadData();
    }

    public void loadData() {
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
//        carLeaveRrdBean.setCarNo(username);
        carLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String toJson = new Gson().toJson(carLeaveEntity);
        String s="apply "+toJson;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, CarLeaveEntity carLeaveEntity) throws InterruptedException {
                show(json);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setGroup(getGroupList());
                        setPb(false);
                        setButtonllEnable(true);
                        setDisplayTabs(true);
                        notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                show(msg);
            }

            @Override
            public void onResponse(String response) {
                show(response);
            }
        });

//        Map<String, Object> param = CommonValues.getCommonParams(getActivity());
//        param.put("userId",getArguments().getString("userId"));
//        param.put("barCode", getArguments().getString("barCode"));
//        param.put("workflowType", getArguments().getString("workflowType"));
//        HttpManager.getInstance().requestResultForm(CommonValues.REQ_REST_DETAIL, param, RestDetailBean.class, new HttpManager.ResultCallback<RestDetailBean>() {
//            @Override
//            public void onSuccess(String content, final RestDetailBean restDetailBean) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (restDetailBean != null && restDetailBean.getRetData() != null) {
//                            if (restDetailBean.getCode().equals("100")) {
//                                setEntity(restDetailBean.getRetData());
//                                setGroup(getGroupList());
//                                setPb(false);
//                                setButtonllEnable(true);
//                                setDisplayTabs(true);
//                                notifyDataSetChanged();
//                                return;
//
//                            }
//                        }else {
//                            ToastUtil.showToast(getContext(),restDetailBean.getMsg());
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String content) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showToast(getContext(),"请检查网络");
//                    }
//                });
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//            }
//        });
//        param.put("uid",getArguments().getString("SubmitBy"));
//        HttpManager.getInstance().requestResultForm(CommonValues.GET_USER_PHOTO, param, UserPhotoEntity.class, new HttpManager.ResultCallback<UserPhotoEntity>() {
//            @Override
//            public void onSuccess(String content, final UserPhotoEntity entity) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (entity != null && entity.getRetData() != null) {
//                            if (entity.getCode().equals("100")) {
//                                photo = entity.getRetData();
//                                if (getGroup().size() > 0){
//                                    getGroup().get(0).setDrawable(photo);
//                                    notifyGroupChanged(0,1);
//                                }
//                                return;
//                            }
//                        }
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(String content) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//            }
//        });
    }

    private void show(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    public CarLeaveEntity.CarLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(CarLeaveEntity.CarLeaveRrdBean entity) {
        this.entity = entity;
    }

    public String getSN(){
        return SN;
    }
}
