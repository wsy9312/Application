package com.example.hgtxxgl.application.fragment;

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
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
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
        return new String[]{"同 意","拒 绝"};
    }
    private String[] stringnull = new String[]{""};

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return null;
        List<Group> groups = new ArrayList<>();
        String levelNumStr = entity.getLevelNum();
        String processStr = entity.getProcess();
        String multiLevelResultStr = entity.getMultiLevelResult();
        int levelNum = Integer.parseInt(levelNumStr);
        int process = Integer.parseInt(processStr);
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder("流程类别", true, false, "人员请假", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"待审批":"已结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
        if (process == 1){
            setButtonsTitles(stringnull);
            String substring = multiLevelResultStr.substring(0, levelNum);
            if (substring.endsWith("1")){
                list.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }else{
                list.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
            }
        }
        groups.add(new Group("流程信息", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, getArguments().getString("name"), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOnduty().equals("1")?"因公请假":"因私请假", HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计外出时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("预计归来时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("申请原因", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补请假", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("基本信息", null, false, null, holderList));
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
        loadData();
//        if (no.isEmpty()){
//            setPb(true);
//        }else{
//            getNameFromNo(no);
//        }
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
        String levelnum = getArguments().getString("levelnum");
        String multiLevelResult = getArguments().getString("multiLevelResult");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        String process = getArguments().getString("process");
        L.e(TAG,"审批levelnum:"+levelnum);
        L.e(TAG,"审批multiLevelResult:"+multiLevelResult);
        L.e(TAG,"审批process:"+process);
        noindex = getArguments().getString("noindex");
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean1 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean1.setRegisterTime("?");
        peopleLeaveRrdBean1.setOutTime("?");
        peopleLeaveRrdBean1.setOnduty("?");
        peopleLeaveRrdBean1.setInTime("?");
        peopleLeaveRrdBean1.setContent("?");
        peopleLeaveRrdBean1.setActualOutTime("?");
        peopleLeaveRrdBean1.setActualInTime("?");
        peopleLeaveRrdBean1.setModifyTime("?");
        peopleLeaveRrdBean1.setMultiLevelResult("?");
        peopleLeaveRrdBean1.setProcess("?");
        peopleLeaveRrdBean1.setLevelNum("?");
        peopleLeaveRrdBean1.setBCancel("?");
        peopleLeaveRrdBean1.setBFillup("?");
        peopleLeaveRrdBean1.setBeginNum("?");
        peopleLeaveRrdBean1.setEndNum("?");
        peopleLeaveRrdBean1.setIsAndroid("1");
        peopleLeaveRrdBean1.setNoIndex(noindex);
        peopleLeaveRrdBean1.setNo(no);
        peopleLeaveRrdBean1.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean2 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean2.setRegisterTime("?");
        peopleLeaveRrdBean2.setOutTime("?");
        peopleLeaveRrdBean2.setOnduty("?");
        peopleLeaveRrdBean2.setInTime("?");
        peopleLeaveRrdBean2.setContent("?");
        peopleLeaveRrdBean2.setActualOutTime("?");
        peopleLeaveRrdBean2.setActualInTime("?");
        peopleLeaveRrdBean2.setModifyTime("?");
        peopleLeaveRrdBean2.setMultiLevelResult("?");
        peopleLeaveRrdBean2.setProcess("?");
        peopleLeaveRrdBean2.setLevelNum("?");
        peopleLeaveRrdBean2.setBCancel("?");
        peopleLeaveRrdBean2.setBFillup("?");
        peopleLeaveRrdBean2.setBeginNum("?");
        peopleLeaveRrdBean2.setEndNum("?");
        peopleLeaveRrdBean2.setIsAndroid("1");
        peopleLeaveRrdBean2.setNoIndex(noindex);
        peopleLeaveRrdBean2.setNo(no);
        peopleLeaveRrdBean2.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean3 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean3.setRegisterTime("?");
        peopleLeaveRrdBean3.setOutTime("?");
        peopleLeaveRrdBean3.setOnduty("?");
        peopleLeaveRrdBean3.setInTime("?");
        peopleLeaveRrdBean3.setContent("?");
        peopleLeaveRrdBean3.setActualOutTime("?");
        peopleLeaveRrdBean3.setActualInTime("?");
        peopleLeaveRrdBean3.setModifyTime("?");
        peopleLeaveRrdBean3.setMultiLevelResult("?");
        peopleLeaveRrdBean3.setProcess("?");
        peopleLeaveRrdBean3.setLevelNum("?");
        peopleLeaveRrdBean3.setBCancel("?");
        peopleLeaveRrdBean3.setBFillup("?");
        peopleLeaveRrdBean3.setBeginNum("?");
        peopleLeaveRrdBean3.setEndNum("?");
        peopleLeaveRrdBean3.setIsAndroid("1");
        peopleLeaveRrdBean3.setNoIndex(noindex);
        peopleLeaveRrdBean3.setNo(no);
        peopleLeaveRrdBean3.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean4 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean4.setRegisterTime("?");
        peopleLeaveRrdBean4.setOutTime("?");
        peopleLeaveRrdBean4.setOnduty("?");
        peopleLeaveRrdBean4.setInTime("?");
        peopleLeaveRrdBean4.setContent("?");
        peopleLeaveRrdBean4.setActualOutTime("?");
        peopleLeaveRrdBean4.setActualInTime("?");
        peopleLeaveRrdBean4.setModifyTime("?");
        peopleLeaveRrdBean4.setMultiLevelResult("?");
        peopleLeaveRrdBean4.setProcess("?");
        peopleLeaveRrdBean4.setLevelNum("?");
        peopleLeaveRrdBean4.setBCancel("?");
        peopleLeaveRrdBean4.setBFillup("?");
        peopleLeaveRrdBean4.setBeginNum("?");
        peopleLeaveRrdBean4.setEndNum("?");
        peopleLeaveRrdBean4.setIsAndroid("1");
        peopleLeaveRrdBean4.setNoIndex(noindex);
        peopleLeaveRrdBean4.setNo(no);
        peopleLeaveRrdBean4.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean5 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean5.setRegisterTime("?");
        peopleLeaveRrdBean5.setOutTime("?");
        peopleLeaveRrdBean5.setOnduty("?");
        peopleLeaveRrdBean5.setInTime("?");
        peopleLeaveRrdBean5.setContent("?");
        peopleLeaveRrdBean5.setActualOutTime("?");
        peopleLeaveRrdBean5.setActualInTime("?");
        peopleLeaveRrdBean5.setModifyTime("?");
        peopleLeaveRrdBean5.setMultiLevelResult("?");
        peopleLeaveRrdBean5.setProcess("?");
        peopleLeaveRrdBean5.setLevelNum("?");
        peopleLeaveRrdBean5.setBCancel("?");
        peopleLeaveRrdBean5.setBFillup("?");
        peopleLeaveRrdBean5.setBeginNum("?");
        peopleLeaveRrdBean5.setEndNum("?");
        peopleLeaveRrdBean5.setIsAndroid("1");
        peopleLeaveRrdBean5.setNoIndex(noindex);
        peopleLeaveRrdBean5.setNo(no);
        peopleLeaveRrdBean5.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
//        if (process.equals("1")){
            if (multiLevelResult.equals("00000")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }else if (multiLevelResult.equals("10000")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }else if(multiLevelResult.equals("11000")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }else if(multiLevelResult.equals("11100")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }else if(multiLevelResult.equals("11110")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }else if(multiLevelResult.equals("11111")){
                if (levelnum.equals("1")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("2")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("3")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("4")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }if (levelnum.equals("5")){
                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
                }
            }
//        }else{
//            if (levelnum.equals("5")){
//                if (multiLevelResult.equals("00000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("10000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11100")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11110")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else{
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }
//            } else if (levelnum.equals("4")){
//                if (multiLevelResult.equals("00000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("10000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11100")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11110")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else{
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }
//            } else if (levelnum.equals("3")){
//                if (multiLevelResult.equals("00000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("10000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11100")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11110")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else{
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }
//            } else if (levelnum.equals("2")){
//                if (multiLevelResult.equals("00000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("10000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11100")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11110")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else{
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }
//            } else if (levelnum.equals("1")){
//                if (multiLevelResult.equals("00000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("10000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11000")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11100")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else if (multiLevelResult.equals("11110")){
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }else{
//                    setApproveNo(peopleLeaveRrdBean1,peopleLeaveRrdBean2,peopleLeaveRrdBean3,peopleLeaveRrdBean4,peopleLeaveRrdBean5);
//                }
//            }
//        }
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(0,peopleLeaveRrdBean1);
        list.add(1,peopleLeaveRrdBean2);
        list.add(2,peopleLeaveRrdBean3);
        list.add(3,peopleLeaveRrdBean4);
        list.add(4,peopleLeaveRrdBean5);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String toJson = new Gson().toJson(peopleLeaveEntity);
        String s="get "+toJson;
        L.e(TAG,"人员审批详情:"+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            if (peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getBCancel().equals("0")){
                                setEntity(peopleLeaveEntity1.getPeopleLeaveRrd().get(0));
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
//                show(msg);
            }

            @Override
            public void onResponse(String response) {
//                show(response);
            }
        });
    }

    public void setApproveNo(PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean1,
                             PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean2,
                             PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean3,
                             PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean4,
                             PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean5){
        peopleLeaveRrdBean1.setApprover1No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean1.setApprover2No("?");
        peopleLeaveRrdBean1.setApprover3No("?");
        peopleLeaveRrdBean1.setApprover4No("?");
        peopleLeaveRrdBean1.setApprover5No("?");

        peopleLeaveRrdBean2.setApprover1No("?");
        peopleLeaveRrdBean2.setApprover2No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean2.setApprover3No("?");
        peopleLeaveRrdBean2.setApprover4No("?");
        peopleLeaveRrdBean2.setApprover5No("?");

        peopleLeaveRrdBean3.setApprover1No("?");
        peopleLeaveRrdBean3.setApprover2No("?");
        peopleLeaveRrdBean3.setApprover3No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean3.setApprover4No("?");
        peopleLeaveRrdBean3.setApprover5No("?");

        peopleLeaveRrdBean4.setApprover1No("?");
        peopleLeaveRrdBean4.setApprover2No("?");
        peopleLeaveRrdBean4.setApprover3No("?");
        peopleLeaveRrdBean4.setApprover4No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean4.setApprover5No("?");

        peopleLeaveRrdBean5.setApprover1No("?");
        peopleLeaveRrdBean5.setApprover2No("?");
        peopleLeaveRrdBean5.setApprover3No("?");
        peopleLeaveRrdBean5.setApprover4No("?");
        peopleLeaveRrdBean5.setApprover5No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
    }

    @Override
    public void onBottomButtonsClick(String title, List<Group> groups) {
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setCurrentApproveNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        peopleLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("同 意")){
            peopleLeaveRrdBean.setResult("1");
        }else {
            peopleLeaveRrdBean.setResult("0");
        }
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
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
//                    Intent intent = new Intent();
//                    intent.setClass(getContext(), LibMainActivity.class);
//                    intent.putExtra("item",getArguments().getInt("item"));
//                    intent.putExtra("tabIndex",getArguments().getInt("tabIndex"));
//                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }else{
                    show("您已审批,正在等待其他审批人审批");
//                    Intent intent = new Intent();
//                    intent.setClass(getContext(), LibMainActivity.class);
//                    intent.putExtra("item",getArguments().getInt("item"));
//                    intent.putExtra("tabIndex",getArguments().getInt("tabIndex"));
//                    getActivity().setResult(Activity.RESULT_OK,intent);
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
