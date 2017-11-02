package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RestApproveCarFragment extends CommonFragment {

    private final static String TAG = "RestApproveCarFragment";
    private String noindex;
    private String no;
//    private String tempIP;

    public RestApproveCarFragment(){

    }

    private CarLeaveEntity.CarLeaveRrdBean entity = null;

    public PeopleInfoEntity.PeopleInfoBean getBean() {
        return bean;
    }

    public void setBean(PeopleInfoEntity.PeopleInfoBean bean) {
        this.bean = bean;
    }

    private PeopleInfoEntity.PeopleInfoBean bean = null;

    public static RestApproveCarFragment newInstance(Bundle bundle) {
        RestApproveCarFragment fragment = new RestApproveCarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"同 意","拒 绝"};
    }
    private String[] stringnull = new String[]{""};

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return null;
        List<Group> groups = new ArrayList<>();
        String processStr = entity.getProcess();
        int process = Integer.parseInt(processStr);
        String resultStr = entity.getResult();
        int result = Integer.parseInt(resultStr);
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "车辆外出", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"待审批":"已审批", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        if (process == 1){
            setButtonsTitles(stringnull);
            if (result == 1){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }
        }else{
            list.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        }

        groups.add(new Group("流程信息", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, getArguments().getString("name"), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOnduty().equals("1")?"因公外出":"因私外出", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请原因", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", true, false, entity.getbFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("基本信息", null, false, null, holderList));
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("车辆外出审批");
        toolbar.setTitleSize(18);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        no = getArguments().getString("no");
        String outtime = getArguments().getString("outtime");
        String intime = getArguments().getString("intime");
        String content = getArguments().getString("content");
        String process = getArguments().getString("process");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        noindex = getArguments().getString("noindex");
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setNo(no);
        carLeaveRrdBean.setApproverNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setRegisterTime("?");
        carLeaveRrdBean.setOnduty("?");
        carLeaveRrdBean.setOutTime("?");
        carLeaveRrdBean.setInTime("?");
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setContent("?");
        carLeaveRrdBean.setActualOutTime("?");
        carLeaveRrdBean.setActualInTime("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setProcess("?");
        carLeaveRrdBean.setbCancel("?");
        carLeaveRrdBean.setbFillup("?");
        carLeaveRrdBean.setNoIndex(noindex);
        carLeaveRrdBean.setBeginNum("?");
        carLeaveRrdBean.setEndNum("?");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setResult("?");
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String toJson = new Gson().toJson(carLeaveEntity);
        String s="get "+toJson;
        //        String url = CommonValues.BASE_URL;
//        String url = ApplicationApp.getIP();
//        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
//        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestResultForm(getTempIP(), s, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (carLeaveEntity1 != null){
                            if (carLeaveEntity1.getCarLeaveRrd().get(0).getbCancel().equals("0")){
                                setEntity(carLeaveEntity1.getCarLeaveRrd().get(0));
                                setGroup(getGroupList());
                                setPb(false);
                                setButtonllEnable(true);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    @Override
    public void onBottomButtonsClick(String title, List<Group> groups) {
        //        String url = CommonValues.BASE_URL;
//        final String url = ApplicationApp.getIP();
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setApproverNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        carLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("同 意")){
            carLeaveRrdBean.setResult("1");
        }else {
            carLeaveRrdBean.setResult("0");
        }
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
        beanList.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(beanList);
        String json = new Gson().toJson(carLeaveEntity);
        final String s1 = "approve " + json;
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("是否确认?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                approveStart(getTempIP(),s1);
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
        HttpManager.getInstance().requestResultForm(baseUrl, s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity) throws InterruptedException {
            }

            @Override
            public void onFailure(final String msg) {
            }

            @Override
            public void onResponse(String response) {
                if (response.toLowerCase().contains("ok")) {
                    show("审批成功");
                    getActivity().finish();
                }else{
                    show("申请人已取消申请,审批结束");
                    getActivity().finish();
                }
            }
        });

    }

    public CarLeaveEntity.CarLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(CarLeaveEntity.CarLeaveRrdBean entity) {
        this.entity = entity;
    }


}
