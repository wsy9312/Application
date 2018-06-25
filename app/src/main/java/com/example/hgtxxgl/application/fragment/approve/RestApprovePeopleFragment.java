package com.example.hgtxxgl.application.fragment.approve;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StringUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//人员外出审批
public class RestApprovePeopleFragment extends CommonFragment {
    private final static String TAG = "RestApprovePeopleFragment";
    private String noindex;
    private String no;
    private int fenNum;
    private String authenticationNo;

    public RestApprovePeopleFragment(){

    }

    private PeopleLeaveEntity.PeopleLeaveRrdBean entity = null;

    public static RestApprovePeopleFragment newInstance(Bundle bundle) {
        RestApprovePeopleFragment fragment = new RestApprovePeopleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"同意结束","拒 绝","同意上报","退 回"};
    }

    private String[] stringnull = new String[]{""};

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return null;
        List<Group> groups = new ArrayList<>();
        int process = Integer.parseInt(entity.getProcess());
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"审批中":"审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        int substring = Integer.parseInt(entity.getResult());
        if (process == 1){
            setButtonsTitles(stringnull);
            switch (substring){
                case 0:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 1:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 2:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
            }
        }else{
            if(!entity.getApproverNo().isEmpty()){
                if (entity.getApproverNo().contains(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo())) {
                    setButtonsTitles(stringnull);
                }
            }
             list.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        }
        if (entity.getBCancel().equals("0")){
            if (entity.getProcess().equals("1")){
                setButtonsTitles(stringnull);
            }
        }

        groups.add(new Group("流程信息", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, getArguments().getString("name"), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("单位", true, false, entity.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("部门", true, false, entity.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("基本信息", null, false, null, holderList));

        String split1 = entity.getHisAnnotation();
        String split3 = entity.getApproverName();
        String [] arrAnnotation = split1.split(";");
        String [] arrName = split3.split(";");
        List<HandInputGroup.Holder> holder = new ArrayList<>();
        if (entity.getBCancel().equals("0")){
            if (process == 1){
                for (int i = 0; i < fenNum; i++) {
                    holder.add(new HandInputGroup.Holder(arrName[i], false, false, arrAnnotation[i], HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(170,170,170)));
                }
                groups.add(new Group("批注信息", null, false, null, holder));
            }else{
                if (fenNum == 0){
                    holder.add(new HandInputGroup.Holder("当前审批人批注:", false, false, "/请填写", HandInputGroup.VALUE_TYPE.BIG_EDIT));
                }else{
                    for (int i = 0; i < fenNum; i++) {
                        holder.add(new HandInputGroup.Holder(arrName[i], false, false, arrAnnotation[i], HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(170,170,170)));
                    }
                    if (!split3.contains(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName())){
                        holder.add(new HandInputGroup.Holder("当前审批人批注:", false, false, "/请填写", HandInputGroup.VALUE_TYPE.BIG_EDIT));
                    }
                }
                groups.add(new Group("批注信息", null, false, null, holder));
            }
        }
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("请假审批");
        toolbar.setTitleSize(18);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationNo = ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo();
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
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        String process = getArguments().getString("process");
        noindex = getArguments().getString("noindex");
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setRegisterTime("?");//
        peopleLeaveRrdBean.setOutTime("?");//
        peopleLeaveRrdBean.setInTime("?");//
        peopleLeaveRrdBean.setContent("?");//
        peopleLeaveRrdBean.setActualOutTime("?");//
        peopleLeaveRrdBean.setActualInTime("?");//
        peopleLeaveRrdBean.setModifyTime("?");//
        peopleLeaveRrdBean.setProcess("?");//
        peopleLeaveRrdBean.setBCancel("?");//
        peopleLeaveRrdBean.setBFillup("?");//
        peopleLeaveRrdBean.setIsAndroid("1");//
        peopleLeaveRrdBean.setNoIndex(noindex);//
        peopleLeaveRrdBean.setNo("?");//
        peopleLeaveRrdBean.setOutType("?");//
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());//
        peopleLeaveRrdBean.setDestination("?");//
        peopleLeaveRrdBean.setApproverNo("?");//
        peopleLeaveRrdBean.setHisAnnotation("?");//
        peopleLeaveRrdBean.setResult("?");//
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String toJson = new Gson().toJson(peopleLeaveEntity);
        String s="get "+toJson;
        L.e(TAG+"RestApprovePeopleFragment",s);
        HttpManager.getInstance().requestResultForm(getTempIP(), s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            if (peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getBCancel().equals("0")){
                                PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                                PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                                peopleInfoBean.setNo(no);
                                peopleInfoBean.setUnit("?");
                                peopleInfoBean.setDepartment("?");
                                peopleInfoBean.setIsAndroid("1");
                                List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                                beanList.add(peopleInfoBean);
                                peopleEntity.setPeopleInfo(beanList);
                                String json = new Gson().toJson(peopleEntity);
                                String s1 = "get " + json;
                                HttpManager.getInstance().requestResultForm(getTempIP(),s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
                                    @Override
                                    public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                                        if (peopleInfoEntity != null){
                                            peopleLeaveEntity1.getPeopleLeaveRrd().get(0).setUnit(peopleInfoEntity.getPeopleInfo().get(0).getUnit());
                                            peopleLeaveEntity1.getPeopleLeaveRrd().get(0).setDepartment(peopleInfoEntity.getPeopleInfo().get(0).getDepartment());
                                            String hisAnnotation = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getHisAnnotation();
                                            String str = ";";
                                            fenNum = StringUtils.method_5(hisAnnotation, str);
                                            L.e(TAG+"RestApprovePeopleFragment",peopleLeaveEntity1.toString());
                                            setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));
                                            setGroup(getGroupList());
                                            setPb(false);
                                            setButtonllEnable(true);
                                            notifyDataSetChanged();
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
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNoIndex(noindex);
        switch (title){
            case "同意结束":
                peopleLeaveRrdBean.setCurResult("3");
                break;
            case "拒 绝":
                peopleLeaveRrdBean.setCurResult("0");
                break;
            case "同意上报":
                peopleLeaveRrdBean.setCurResult("2");
                break;
            case "退 回":
                peopleLeaveRrdBean.setCurResult("1");
                break;
        }
        String realValue1 = getDisplayValueByKey("当前审批人批注:").getRealValue();
        String realValue = realValue1.isEmpty()?"无批注":realValue1;
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setCurannotation(realValue);
        peopleLeaveRrdBean.setIsAndroid("1");
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
                    getActivity().finish();
                }else{
                    show("审批已完成,正在等待其他人审批");
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
