package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RestDetailPeopleFragment extends CommonFragment {

    private final static String TAG = "RestDetailPeopleFragment";
    private String name = null;
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
        String bCancel = entity.getBCancel();
        int levelNum = Integer.parseInt(levelNumStr);
        int process = Integer.parseInt(processStr);

        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "人员请假", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"未结束":"已结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        if (process == 1){
            setButtonsTitles(stringnull);
            String substring = multiLevelResultStr.substring(0, levelNum);
            if (substring.endsWith("1")){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }
        }else if (process == 0){
            if(multiLevelResultStr.startsWith("1")){
                setButtonsTitles(stringnull);
            }
            if(bCancel.equals("1")){
                setButtonsTitles(stringnull);
            }else if (bCancel.equals("0")){
                setButtonsTitles(stringbutton);
            }
            list.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        }
        list.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));

        groups.add(0,new Group("流程信息", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOnduty().equals("1")?"因公请假":"因私请假", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请原因", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(1,new Group("基本信息", null, false, null, holderList));
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("人员请假详情");
        toolbar.setTitleSize(18);
    }

    private String[] stringbutton = new String[]{"(取消请假) 是"/*,"否"*/};
    private String[] stringnull = new String[]{""};

    @Override
    public String[] getBottomButtonsTitles() {
        return stringbutton;
    }

    @Override
    public void onBottomButtonsClick(String title, List<Group> groups) {
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        String noindex = getArguments().getString("noindex");
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("(取消请假) 是")){
            if (entity.getBCancel().equals("1")){
                return;
            }else{
                peopleLeaveRrdBean.setBCancel("1");
            }
        }/*else {
            if (entity.getBCancel().equals("0")){
                show("当前已是未取消状态,请勿重复操作");
                return;
            }else{
                peopleLeaveRrdBean.setBCancel("0");
            }
        }*/
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
        beanList.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(beanList);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s1 = "modify " + json;
        L.e(TAG,s1);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("是否确认?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applyModify(CommonValues.BASE_URL,s1);
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

    private void applyModify(String baseUrl, String s1) {
        HttpManager.getInstance().requestResultForm(baseUrl, s1, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity) throws InterruptedException {
            }

            @Override
            public void onFailure(final String msg) {
            }

            @Override
            public void onResponse(String response) {
                if (response.contains("ok")) {
                    show("修改成功");
                    getActivity().finish();
                }else{
                    show("修改失败");
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
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
        String noindex = getArguments().getString("noindex");
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        peopleLeaveRrdBean.setCurrentApproveNo("?");
        peopleLeaveRrdBean.setOnduty("?");
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
        peopleLeaveRrdBean.setBFillup("?");
        peopleLeaveRrdBean.setBCancel("?");
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
        L.e(TAG,"申请后详情："+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            String approver1No = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover1No();
                            String approver2No = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover2No();
                            String approver3No = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover3No();
                            String approver4No = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover4No();
                            String approver5No = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover5No();
                            ArrayList<String> approveNoList = new ArrayList<>();
                            if (!approver1No.isEmpty()){
                                approveNoList.add(0,approver1No);
                            } if (!approver2No.isEmpty()){
                                approveNoList.add(1,approver2No);
                            } if (!approver3No.isEmpty()){
                                approveNoList.add(2,approver3No);
                            } if (!approver4No.isEmpty()){
                                approveNoList.add(3,approver4No);
                            } if (!approver5No.isEmpty()){
                                approveNoList.add(4,approver5No);
                            }
                            for (int i = 0; i < approveNoList.size(); i++) {
                                L.e(TAG,"当前:"+approveNoList.get(i));
                                PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                                PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                                peopleInfoBean.setNo(approveNoList.get(i));
                                peopleInfoBean.setName("?");
                                peopleInfoBean.setAuthenticationNo(approveNoList.get(i));
                                peopleInfoBean.setIsAndroid("1");
                                List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                                beanList.add(peopleInfoBean);
                                peopleEntity.setPeopleInfo(beanList);
                                String json1 = new Gson().toJson(peopleEntity);
                                String s1 = "get " + json1;
                                L.e(TAG,"s1"+s1);
                                HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
                                    @Override
                                    public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                                        if (peopleInfoEntity != null){
                                            L.e(TAG,"namenamename:   "+peopleInfoEntity.getPeopleInfo().get(0).getName());
                                            peopleLeaveEntity1.getPeopleLeaveRrd().get(0).setApprover1Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                                            L.e(TAG,"namename:   "+peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover1Name());
                                            name = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover1Name();
                                            L.e(TAG,"name:   "+name);
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
                            setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));
                            setGroup(getGroupList());
                            setPb(false);
                            setButtonllEnable(true);
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
//                show(msg);
            }

            @Override
            public void onResponse(String response) {
//                show(response);
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
