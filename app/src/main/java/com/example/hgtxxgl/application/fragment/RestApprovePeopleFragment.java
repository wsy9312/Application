package com.example.hgtxxgl.application.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.activity.LibMainActivity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
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

public class RestApprovePeopleFragment extends CommonFragment {
    private final static String TAG = "RestApprovePeopleFragment";
    private String noindex;
    private String no;

    public RestApprovePeopleFragment(){

    }

    private PeopleLeaveEntity.PeopleLeaveRrdBean entity = null;

    public PeopleInfoEntity.PeopleInfoBean getBean() {
        return bean;
    }

    public void setBean(PeopleInfoEntity.PeopleInfoBean bean) {
        this.bean = bean;
    }

    private PeopleInfoEntity.PeopleInfoBean bean = null;

    public static RestApprovePeopleFragment newInstance(Bundle bundle) {
        RestApprovePeopleFragment fragment = new RestApprovePeopleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"同意","拒绝"};
    }

    @Override
    public List<Group> getGroupList() {
        if (entity == null || bean == null) return null;
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
            String substring = multiLevelResultStr.substring(0, levelNum);
            if (substring.endsWith("1")){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批同意", HandInputGroup.VALUE_TYPE.TEXT));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批拒绝", HandInputGroup.VALUE_TYPE.TEXT));
            }

        }
        groups.add(new Group("流程摘要-摘要内容", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("流程内容",true,false, "人员请假",HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请人", true, false, bean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
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
        toolbar.setTitle("人员请假审批");
        toolbar.setTitleSize(18);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SN = getArguments().getString("SN");
        loadData();
        if (no.isEmpty()){
            return;
        }else{
            getNameFromNo(no);
        }
    }

    private void show(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    private void getNameFromNo(String no){
        PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
        PeopleInfoEntity.PeopleInfoBean peopleInfoBean =
                new PeopleInfoEntity.PeopleInfoBean();
        peopleInfoBean.setNo(no);
        peopleInfoBean.setName("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleInfoBean.setIsAndroid("1");
        List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
        beanList.add(peopleInfoBean);
        peopleEntity.setPeopleInfo(beanList);
        String json = new Gson().toJson(peopleEntity);
        String s1 = "get " + json;
        HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
            @Override
            public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                if (peopleInfoEntity != null){
                    setBean(peopleInfoEntity.getPeopleInfo().get(0));
                    if(getGroupList() == null){
                        setGroup(getGroupList());
                        setPb(true);
                        setButtonllEnable(false);
                        setDisplayTabs(false);
                        notifyDataSetChanged();
                    }else{
                        setGroup(getGroupList());
                        setPb(false);
                        setButtonllEnable(true);
                        setDisplayTabs(true);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    public void loadData() {
        no = getArguments().getString("no");
        String outtime = getArguments().getString("outtime");
        String intime = getArguments().getString("intime");
        String content = getArguments().getString("content");
        String levelnum = getArguments().getString("levelnum");
        String process = getArguments().getString("process");
        String multiLevelResult = getArguments().getString("multiLevelResult");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        noindex = getArguments().getString("noindex");
        final PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo(no);
        peopleLeaveRrdBean.setCurrentApproveNo("?");
        peopleLeaveRrdBean.setApprover1No("?");
        peopleLeaveRrdBean.setApprover2No("?");
        peopleLeaveRrdBean.setApprover3No("?");
        peopleLeaveRrdBean.setApprover4No("?");
        peopleLeaveRrdBean.setApprover5No("?");
        peopleLeaveRrdBean.setRegisterTime("?");
        peopleLeaveRrdBean.setOutTime("?");
        peopleLeaveRrdBean.setInTime("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setActualOutTime("?");
        peopleLeaveRrdBean.setActualInTime("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setMultiLevelResult("?");
        peopleLeaveRrdBean.setProcess("?");
        peopleLeaveRrdBean.setLevelNum("?");
        peopleLeaveRrdBean.setBCancel("?");
        peopleLeaveRrdBean.setBFillup("?");
        peopleLeaveRrdBean.setNoIndex(noindex);
        peopleLeaveRrdBean.setBeginNum("?");
        peopleLeaveRrdBean.setEndNum("?");
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String toJson = new Gson().toJson(peopleLeaveEntity);
        String s="get "+toJson;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));

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
    }

    @Override
    public void onBottomButtonsClick(String title, List<Group> groups) {
        setButtonllEnable(false);
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setCurrentApproveNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        peopleLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("同意")){
            peopleLeaveRrdBean.setResult("1");
        }else {
            peopleLeaveRrdBean.setResult("0");
        }
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
//        ToastUtil.showToast(getContext(),peopleLeaveRrdBean.getCurrentApproveNo()+" "+peopleLeaveRrdBean.getResult()+" "+noindex);
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
        beanList.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(beanList);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s1 = "approve " + json;
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("是否确认?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                approveStart(CommonValues.BASE_URL,s1);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

        }

    private void approveStart(String baseUrl, String s1) {
        HttpManager.getInstance().requestResultForm(baseUrl, s1, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity) throws InterruptedException {
            }

            @Override
            public void onFailure(final String msg) {
            }

            @Override
            public void onResponse(String response) {
                if (response.toLowerCase().contains("ok")) {
                    show("审批成功");
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LibMainActivity.class);
                    intent.putExtra("item",getArguments().getInt("item"));
                    intent.putExtra("tabIndex",getArguments().getInt("tabIndex"));
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }else{
                    show("审批失败");
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LibMainActivity.class);
                    intent.putExtra("item",getArguments().getInt("item"));
                    intent.putExtra("tabIndex",getArguments().getInt("tabIndex"));
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }
            }
        });

    }

    public PeopleLeaveEntity.PeopleLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(PeopleLeaveEntity.PeopleLeaveRrdBean entity) {
        this.entity = entity;
    }
}