package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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

/**
 * Created by Oliver on 2016/10/12.
 */

public class RestDetailPeopleFragment extends CommonFragment {
    private String SN;
    private final static String TAG = "RestDetailPeopleFragment";

    public RestDetailPeopleFragment(){

    }

    private PeopleLeaveEntity.PeopleLeaveRrdBean entity = null;

    public static RestDetailPeopleFragment newInstance(Bundle bundle) {
        RestDetailPeopleFragment fragment = new RestDetailPeopleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        String levelNumStr = entity.getLevelNum();
        String processStr = entity.getProcess();
        String multiLevelResultStr = entity.getMultiLevelResult();
        int levelNum = Integer.parseInt(levelNumStr);
        int process = Integer.parseInt(processStr);
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "人员请假", HandInputGroup.VALUE_TYPE.TEXT));
        list.add(new HandInputGroup.Holder("审批进度", true, false, process == 0?"审批中":"审批结束", HandInputGroup.VALUE_TYPE.TEXT));
        if (process == 1){
            String substring = multiLevelResultStr.substring(0, levelNum-1);
            if (substring.endsWith("1")){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批通过", HandInputGroup.VALUE_TYPE.TEXT));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批拒绝", HandInputGroup.VALUE_TYPE.TEXT));
            }

        }
        groups.add(new Group("流程摘要-摘要内容", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("流程内容",true,false, "人员请假",HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请人", true, false, entity.getNo(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("请假原因", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否取消请假", true, false, entity.getBCancel().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("详细信息-基本信息", null, false, null, holderList));
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("人员请假详情");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SN = getArguments().getString("SN");
        loadData();
    }

    private void show(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    public void loadData() {
        String no = getArguments().getString("no");
        String outtime = getArguments().getString("outtime");
        String intime = getArguments().getString("intime");
        String content = getArguments().getString("content");
        String levelnum = getArguments().getString("levelnum");
        String process = getArguments().getString("process");
        String multiLevelResult = getArguments().getString("multiLevelResult");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        final String noindex = getArguments().getString("noindex");
        final PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean =
                new PeopleLeaveEntity.PeopleLeaveRrdBean(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo(),"?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?",noindex,"?","?");
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String toJson = new Gson().toJson(peopleLeaveEntity);
        String s="get "+toJson;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                show(json);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            show("人员请假信息获取成功");
                            setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));
                            setGroup(getGroupList());
                            setPb(false);
                            setButtonllEnable(true);
                            setDisplayTabs(true);
                            notifyDataSetChanged();
                        }
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

    public PeopleLeaveEntity.PeopleLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(PeopleLeaveEntity.PeopleLeaveRrdBean entity) {
        this.entity = entity;
    }

    public String getSN(){
        return SN;
    }
}
