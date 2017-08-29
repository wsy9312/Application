package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class RestDetailPeopleFragment extends CommonFragment {

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
        String bCancel = entity.getBCancel();
        int levelNum = Integer.parseInt(levelNumStr);
        int process = Integer.parseInt(processStr);
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "人员请假", HandInputGroup.VALUE_TYPE.TEXT));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"审批中":"审批结束", HandInputGroup.VALUE_TYPE.TEXT));
        if (process == 1){
            String substring = multiLevelResultStr.substring(0, levelNum);
            if (substring.endsWith("1")){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批同意", HandInputGroup.VALUE_TYPE.TEXT));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "审批拒绝", HandInputGroup.VALUE_TYPE.TEXT));
            }
            setButtonsTitles(stringnull);
        }else if (process == 0){
            if(multiLevelResultStr.startsWith("1")){
                setButtonsTitles(stringnull);
            }
            list.add(new HandInputGroup.Holder("审批总级数", true, false, entity.getLevelNum()+"级", HandInputGroup.VALUE_TYPE.TEXT));
            int i = Integer.parseInt(multiLevelResultStr);
            int one = (i/10000)%10;
            int two = (i/1000)%10;
            int three = (i/100)%10;
            int four = (i/10)%10;
            int five =  i%10;
            if (four == 1){
                list.add(new HandInputGroup.Holder("当前待审批等级", true, false, "第5级", HandInputGroup.VALUE_TYPE.TEXT));
            } else if (three == 1){
                list.add(new HandInputGroup.Holder("当前待审批等级", true, false, "第4级", HandInputGroup.VALUE_TYPE.TEXT));
            } else if  (two == 1){
                list.add(new HandInputGroup.Holder("当前待审批等级", true, false, "第3级", HandInputGroup.VALUE_TYPE.TEXT));
            } else if  (one == 1){
                list.add(new HandInputGroup.Holder("当前待审批等级", true, false, "第2级", HandInputGroup.VALUE_TYPE.TEXT));
            } else if  (one == 0){
                list.add(new HandInputGroup.Holder("当前待审批等级", true, false, "第1级", HandInputGroup.VALUE_TYPE.TEXT));
            }
            /*if (bCancel.equals("0")){
                int i = Integer.parseInt(multiLevelResultStr);
                int one = (i/10000)%10;
                int two = (i/1000)%10;
                int three = (i/100)%10;
                int four = (i/10)%10;
                int five =  i%10;
                Log.e("one",one+""+two+""+three+""+four+""+five);
                if (one == 1){
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover1Name(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("待审批人", true, false, entity.getApprover2Name(), HandInputGroup.VALUE_TYPE.TEXT));
                }else if (two == 1){
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover1No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover2No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("待审批人", true, false, entity.getApprover3No(), HandInputGroup.VALUE_TYPE.TEXT));
                }else if (three == 1){
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover1No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover2No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover3No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("待审批人", true, false, entity.getApprover4No(), HandInputGroup.VALUE_TYPE.TEXT));
                }else if (four == 1){
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover1No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover2No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover3No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("已审批人", true, false, entity.getApprover4No(), HandInputGroup.VALUE_TYPE.TEXT));
                    list.add(new HandInputGroup.Holder("待审批人", true, false, entity.getApprover5No(), HandInputGroup.VALUE_TYPE.TEXT));
                }

            }*/
        }

        groups.add(new Group("流程摘要-摘要内容", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("请假类型", true, false, entity.getOnduty().equals("1")?"因公外出/请假":"因私外出/请假", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("请假原因", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否取消请假", true, false, entity.getBCancel().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("详细信息-基本信息", null, false, null, holderList));
        return groups;
    }

    private void getNameFromApproveNo(final int no, String approveNo){
        PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
        PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
        peopleInfoBean.setNo(approveNo);
        peopleInfoBean.setName("?");
        peopleInfoBean.setAuthenticationNo(approveNo);
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
                    switch (no){
                        case 1:
                            entity.setApprover1Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                            break;
                        case 2:
                            entity.setApprover2Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                            break;
                        case 3:
                            entity.setApprover3Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                            break;
                        case 4:
                            entity.setApprover4Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                            break;
                        case 5:
                            entity.setApprover5Name(peopleInfoEntity.getPeopleInfo().get(0).getName());
                            break;
                    }

                    L.e(TAG, "onSuccess: "+entity.getApprover1Name());
                    L.e(TAG, "onSuccess: "+entity.getApprover2Name());
                    L.e(TAG, "onSuccess: "+entity.getApprover3Name());
                    L.e(TAG, "onSuccess: "+entity.getApprover4Name());
                    L.e(TAG, "onSuccess: "+entity.getApprover5Name());
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

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("请假详情");
        toolbar.setTitleSize(18);
    }

    private String[] stringbutton = new String[]{"(是否取消请假)是","否"};
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
        if (title.equals("(是否取消请假)是")){
            peopleLeaveRrdBean.setBCancel("1");
        }else {
            peopleLeaveRrdBean.setBCancel("0");
        }
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
                    show("取消成功");
                }else{
                    show("取消失败");
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
                Toasty.success(getContext(),msg, Toast.LENGTH_SHORT,true).show();
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
                            setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));
                            L.e(TAG,entity.getProcess()+" "+entity.getMultiLevelResult()+" "+entity.getLevelNum());
//                            getNameFromApproveNo(1,peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover1No());
//                            getNameFromApproveNo(2,peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover2No());
//                            getNameFromApproveNo(3,peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover3No());
//                            getNameFromApproveNo(4,peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover4No());
//                            getNameFromApproveNo(5,peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getApprover5No());
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
