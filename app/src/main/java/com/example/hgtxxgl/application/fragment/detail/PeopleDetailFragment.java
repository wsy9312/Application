package com.example.hgtxxgl.application.fragment.detail;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.LoginInfoBean;
import com.example.hgtxxgl.application.bean.PeopleApplyBean;
import com.example.hgtxxgl.application.bean.PeopleCheckBackBean;
import com.example.hgtxxgl.application.bean.PeopleCheckOutBean;
import com.example.hgtxxgl.application.bean.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.PeopleLeaveDetailBean;
import com.example.hgtxxgl.application.bean.PeopleLeaveDetailRetractBean;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.StringUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

//人员请假申请详情
public class PeopleDetailFragment extends CommonFragment {

    private final static String TAG = "PeopleDetailFragment";
    private String s1;
    private String [][] buttonType = {{""},{"撤销申请"},{"重新提交"},{"确认离开"},{"确认归来"}};
    private int type = 0;
    private int fenNum;
    private LoginInfoBean.ApiAddLoginBean loginBean;
    private PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean;
    private String noindex;

    public PeopleDetailFragment(){

    }

    private PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean entity = null;

    public static PeopleDetailFragment newInstance(Bundle bundle) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        int process = Integer.parseInt(entity.getProcess());
        int substring = Integer.parseInt(entity.getResult());
        String bCancel = entity.getBCancel();
        if (process == 1){
            if (substring == 2){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人",true,false,peopleInfoBean.getName(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("所属单位",true,false,peopleInfoBean.getUnit(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("所属部门",true,false,peopleInfoBean.getDepartment(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,entity.getOutTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,entity.getInTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                if (!TextUtils.equals("-9999",entity.getVacationDays())){
                    baseHolder.add(new HandInputGroup.Holder("假期天数", true, false, entity.getVacationDays(), HandInputGroup.VALUE_TYPE.DOUBLE).setColor(Color.rgb(128,128,128)));
                }
                if (!TextUtils.equals("-9999",entity.getJourneyDays())){
                    baseHolder.add(new HandInputGroup.Holder("路途天数", true, false, entity.getJourneyDays(), HandInputGroup.VALUE_TYPE.DOUBLE).setColor(Color.rgb(128,128,128)));
                }
                baseHolder.add(new HandInputGroup.Holder("事由",true,false,entity.getContent(),HandInputGroup.VALUE_TYPE.BIG_EDIT).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("去向",true,false,entity.getDestination(),HandInputGroup.VALUE_TYPE.TEXTFILED).setColor(Color.rgb(128,128,128)));
                if (!TextUtils.isEmpty(entity.getVacationAddr())){
                    baseHolder.add(new HandInputGroup.Holder("疗养地址", true, false, entity.getVacationAddr(), HandInputGroup.VALUE_TYPE.TEXT));
                }
//                baseHolder.add(new HandInputGroup.Holder("是否后补申请",false,false,entity.getBFillup().equals("0")?"否":"是",HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                groups.add(1,new Group("基本信息", null,true,null,baseHolder));
                setButtonsTitles(buttonType[2]);
                //重新提交,取消申请
            }else if(substring == 0){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                if (!TextUtils.equals("-9999",entity.getVacationDays())){
                    baseHolder.add(new HandInputGroup.Holder("假期天数", true, false, entity.getVacationDays(), HandInputGroup.VALUE_TYPE.TEXT));
                }
                if (!TextUtils.equals("-9999",entity.getJourneyDays())){
                    baseHolder.add(new HandInputGroup.Holder("路途天数", true, false, entity.getJourneyDays(), HandInputGroup.VALUE_TYPE.TEXT));
                }
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                if (!TextUtils.isEmpty(entity.getVacationAddr())){
                    baseHolder.add(new HandInputGroup.Holder("疗养地址", true, false, entity.getVacationAddr(), HandInputGroup.VALUE_TYPE.TEXT));
                }
//                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[0]);
                //无
            }else if (substring == 1){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("实际离队时间", true, false, entity.getActualOutTime().isEmpty()?"":entity.getActualOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("实际归队时间", true, false, entity.getActualInTime().isEmpty()?"":entity.getActualInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                if (!TextUtils.equals("-9999",entity.getVacationDays())){
                    baseHolder.add(new HandInputGroup.Holder("假期天数", true, false, entity.getVacationDays(), HandInputGroup.VALUE_TYPE.TEXT));
                }
                if (!TextUtils.equals("-9999",entity.getJourneyDays())){
                    baseHolder.add(new HandInputGroup.Holder("路途天数", true, false, entity.getJourneyDays(), HandInputGroup.VALUE_TYPE.TEXT));
                }
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                if (!TextUtils.isEmpty(entity.getVacationAddr())){
                    baseHolder.add(new HandInputGroup.Holder("疗养地址", true, false, entity.getVacationAddr(), HandInputGroup.VALUE_TYPE.TEXT));
                }
//                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                if (entity.getOutStatus().equals("0") && entity.getActualInTime().isEmpty() && entity.getActualOutTime().isEmpty()){
                    setButtonsTitles(buttonType[3]);
                }else if (entity.getOutStatus().equals("1") && entity.getActualInTime().isEmpty() && !entity.getActualOutTime().isEmpty()){
                    setButtonsTitles(buttonType[4]);
                }else if (entity.getOutStatus().equals("0") && !entity.getActualInTime().isEmpty() && !entity.getActualOutTime().isEmpty()){
                    setButtonsTitles(buttonType[0]);
                }

                //无
            }

        }else if (process == 2){
            List<HandInputGroup.Holder> holders = new ArrayList<>();
            holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            groups.add(0,new Group("流程信息", null, false, null, holders));
            setButtonsTitles(buttonType[0]);
            List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
            baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
            if (!TextUtils.equals("-9999",entity.getVacationDays())){
                baseHolder.add(new HandInputGroup.Holder("假期天数", true, false, entity.getVacationDays(), HandInputGroup.VALUE_TYPE.TEXT));
            }
            if (!TextUtils.equals("-9999",entity.getJourneyDays())){
                baseHolder.add(new HandInputGroup.Holder("路途天数", true, false, entity.getJourneyDays(), HandInputGroup.VALUE_TYPE.TEXT));
            }
            baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
            if (!TextUtils.isEmpty(entity.getVacationAddr())){
                baseHolder.add(new HandInputGroup.Holder("疗养地址", true, false, entity.getVacationAddr(), HandInputGroup.VALUE_TYPE.TEXT));
            }
//            baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
            groups.add(1,new Group("基本信息", null, false, null, baseHolder));

        }else if (process == 0){
            if (bCancel.equals("0")){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "未审批", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                setButtonsTitles(buttonType[1]);
            }else if (bCancel.equals("1")){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "已撤销", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已撤销", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                setButtonsTitles(buttonType[0]);
            }
            List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
            baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
            if (!TextUtils.equals("-9999",entity.getVacationDays())){
                baseHolder.add(new HandInputGroup.Holder("假期天数", true, false, entity.getVacationDays(), HandInputGroup.VALUE_TYPE.TEXT));
            }
            if (!TextUtils.equals("-9999",entity.getJourneyDays())){
                baseHolder.add(new HandInputGroup.Holder("路途天数", true, false, entity.getJourneyDays(), HandInputGroup.VALUE_TYPE.TEXT));
            }
            baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
            if (!TextUtils.isEmpty(entity.getVacationAddr())){
                baseHolder.add(new HandInputGroup.Holder("疗养地址", true, false, entity.getVacationAddr(), HandInputGroup.VALUE_TYPE.TEXT));
            }
//            baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
            groups.add(1,new Group("基本信息", null, false, null, baseHolder));

        }
        String split1 = entity.getHisAnnotation();
        String split3 = entity.getApproverName();
        String [] arrAnnotation = split1.split(";");
        String [] arrName = split3.split(";");
        List<HandInputGroup.Holder> holder = new ArrayList<>();
        if (entity.getBCancel().equals("0")){
            if (fenNum > 0){
                for (int i = 0; i < fenNum; i++) {
                    if (arrAnnotation.length>0){
                        holder.add(new HandInputGroup.Holder(arrName[i], false, false, arrAnnotation[i], HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(170,170,170)));
                    }else{
                        holder.add(new HandInputGroup.Holder(arrName[i], false, false, "无批注", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(170,170,170)));
                    }
                }
            }
            groups.add(2,new Group("批注信息", null, false, null, holder));
        }
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("请假申请");
        toolbar.setTitleSize(18);
    }

    @Override
    public String[] getBottomButtonsTitles() {
        if(type == 0){
            return buttonType[0];
        }else if(type == 1){
            return buttonType[1];
        }else if (type == 2){
            return buttonType[2];
        }else if (type == 3){
            return buttonType[3];
        }else {
            return buttonType[4];
        }
    }

    @Override
    public void onBottomButtonsClick(String title, final List<Group> groups) {
        PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean peopleLeaveRrdBean = new PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean();
        final String noindex = getArguments().getString("noindex");
        peopleLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        peopleLeaveRrdBean.setNo(loginBean.getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setNoIndex(noindex);
        peopleLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
        if (title.equals("撤销申请")){
            if (entity.getBCancel().equals("1")){
                show("已撤销,请勿重复操作!");
                return;
            }else{
                if (entity.getProcess().equals("1")){
                    if (entity.getResult().equals("0")||entity.getResult().equals("1")){
                        show("审批已结束,不可撤销!");
                        return;
                    }
                }else if (entity.getProcess().equals("0")){
                    if (entity.getResult().equals("0")){
                        if (!entity.getApproverNo().isEmpty()){
                            show("审批进行中,不可撤销!");
                            return;
                        }
                    }
                }
                peopleLeaveRrdBean.setBCancel("1");
                String json = new Gson().toJson(peopleLeaveRrdBean);
                s1 = "Api_Retract_PeopleLeave " + json;
//                L.e(TAG,s1);
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("是否确认?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        applyModify(s1);
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
        }else if(title.equals("重新提交")){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("是否确认提交?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String over = isOver(groups);
                    if (over != null){
                        ToastUtil.showToast(getContext(),over+"不能为空!");
                        setButtonllEnable(true);
                    }else {
                        //申请人ID
                        String realValueNO = loginBean.getAuthenticationNo();
                        String unit = getDisplayValueByKey("所属单位").getRealValue();
                        String applicant = getDisplayValueByKey("申请人").getRealValue();
                        String applicantType = getDisplayValueByKey("申请类型").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime = getDisplayValueByKey("归队时间").getRealValue();
                        String argument = getDisplayValueByKey("事由").getRealValue();
                        String goDirection  = getDisplayValueByKey("去向").getRealValue();
//                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        PeopleApplyBean.ApiApplyPeopleLeaveBean peopleApplyBean = new PeopleApplyBean.ApiApplyPeopleLeaveBean();
                        peopleApplyBean.setDestination(goDirection);
                        peopleApplyBean.setNo(realValueNO);
                        peopleApplyBean.setOutType(applicantType);
                        peopleApplyBean.setOutTime(leaveTime);
                        peopleApplyBean.setInTime(returnTime);
                        peopleApplyBean.setContent(argument);
//                        peopleApplyBean.setBFillup(bFillup.equals("否")?"0":"1");
                        peopleApplyBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                        peopleApplyBean.setIsAndroid("1");
                        peopleApplyBean.setNoIndex(noindex);
                        peopleApplyBean.setTimeStamp(loginBean.getTimeStamp());
                        String json = new Gson().toJson(peopleApplyBean);
                        String s1 = "Api_Apply_PeopleLeave " + json;
                        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleApplyBean.class, new HttpManager.ResultNewCallback<PeopleApplyBean>() {
                            @Override
                            public void onSuccess(String json, PeopleApplyBean peopleApplyBean) throws Exception {
//                                L.e(TAG+"重新提交",json);
                                if (peopleApplyBean.getApi_Apply_PeopleLeave().get(0) == null){
                                    show("提交成功");
                                    getActivity().finish();
                                }else{
                                    show("提交失败");
                                    getActivity().finish();
                                }
                            }

                            @Override
                            public void onError(String msg) throws Exception {

                            }

                            @Override
                            public void onResponse(String response) throws Exception {

                            }

                            @Override
                            public void onBefore(Request request, int id) throws Exception {

                            }

                            @Override
                            public void onAfter(int id) throws Exception {

                            }

                            @Override
                            public void inProgress(float progress, long total, int id) throws Exception {

                            }
                        });
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            });
            builder.create().show();
        }else if (title.equals("确认离开")){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("是否确认提交?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    PeopleApplyBean.ApiApplyPeopleLeaveBean peopleApplyBean = new PeopleApplyBean.ApiApplyPeopleLeaveBean();
                    peopleApplyBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                    peopleApplyBean.setIsAndroid("1");
                    peopleApplyBean.setNoIndex(noindex);
                    peopleApplyBean.setTimeStamp(loginBean.getTimeStamp());
                    String json = new Gson().toJson(peopleApplyBean);
                    String s1 = "Api_Edit_CheckOutForPeo " + json;
                    HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleCheckOutBean.class, new HttpManager.ResultNewCallback<PeopleCheckOutBean>() {
                        @Override
                        public void onSuccess(String json, PeopleCheckOutBean peopleApplyBean) throws Exception {
                            L.e(TAG+" 确认离开",json);
                            if (json.contains("NotInTime")){
                                show("当前不在外出时间!");
                                getActivity().finish();
                            }
                            if (peopleApplyBean.getApi_Edit_CheckOutForPeo().get(0) == null||peopleApplyBean.getApi_Edit_CheckOutForPeo().size() == 0){
                                show("提交成功");
                                getActivity().finish();
                            }else{
                                show("提交失败");
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onError(String msg) throws Exception {

                        }

                        @Override
                        public void onResponse(String response) throws Exception {

                        }

                        @Override
                        public void onBefore(Request request, int id) throws Exception {

                        }

                        @Override
                        public void onAfter(int id) throws Exception {

                        }

                        @Override
                        public void inProgress(float progress, long total, int id) throws Exception {

                        }
                    });
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            });
            builder.create().show();
        }else if (title.equals("确认归来")){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("是否确认提交?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    PeopleApplyBean.ApiApplyPeopleLeaveBean peopleApplyBean = new PeopleApplyBean.ApiApplyPeopleLeaveBean();
                    peopleApplyBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                    peopleApplyBean.setIsAndroid("1");
                    peopleApplyBean.setNoIndex(noindex);
                    peopleApplyBean.setTimeStamp(loginBean.getTimeStamp());
                    String json = new Gson().toJson(peopleApplyBean);
                    String s1 = "Api_Edit_CheckBackForPeo " + json;
                    HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleCheckBackBean.class, new HttpManager.ResultNewCallback<PeopleCheckBackBean>() {
                        @Override
                        public void onSuccess(String json, PeopleCheckBackBean peopleApplyBean) throws Exception {
                            L.e(TAG+" 确认归来",json);
                            if (peopleApplyBean.getApi_Edit_CheckBackForPeo().get(0) == null||peopleApplyBean.getApi_Edit_CheckBackForPeo().size()==0){
                                show("提交成功");
                                getActivity().finish();
                            }else{
                                show("提交失败");
                                getActivity().finish();
                            }
                        }

                        @Override
                        public void onError(String msg) throws Exception {

                        }

                        @Override
                        public void onResponse(String response) throws Exception {

                        }

                        @Override
                        public void onBefore(Request request, int id) throws Exception {

                        }

                        @Override
                        public void onAfter(int id) throws Exception {

                        }

                        @Override
                        public void inProgress(float progress, long total, int id) throws Exception {

                        }
                    });
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    notifyDataSetChanged();
                }
            });
            builder.create().show();
        }
    }

    private void applyModify(String s1) {
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleLeaveDetailRetractBean.class, new HttpManager.ResultNewCallback<PeopleLeaveDetailRetractBean>() {
            @Override
            public void onSuccess(String json, PeopleLeaveDetailRetractBean peopleApplyBean) throws Exception {
                if (peopleApplyBean.getApi_Retract_PeopleLeave().get(0) == null){
                    show("撤销成功");
                    getActivity().finish();
                }else{
                    show("已在审批当中,撤销申请失败");
                    getActivity().finish();
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {

            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBean = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0);
        peopleInfoBean = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0);
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
        final String process = getArguments().getString("process");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        noindex = getArguments().getString("noindex");
        PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean peopleLeaveRrdBean = new PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean();
        peopleLeaveRrdBean.setNo(peopleInfoBean.getNo());//1
        peopleLeaveRrdBean.setRegisterTime("?");//2
        peopleLeaveRrdBean.setOutTime("?");//3
        peopleLeaveRrdBean.setInTime("?");//4
        peopleLeaveRrdBean.setContent("?");//5
        peopleLeaveRrdBean.setActualOutTime("?");//6
        peopleLeaveRrdBean.setActualInTime("?");//7
        peopleLeaveRrdBean.setModifyTime("?");//8
        peopleLeaveRrdBean.setProcess("?");//9
        peopleLeaveRrdBean.setBFillup("?");//10
        peopleLeaveRrdBean.setBCancel("?");//11
        peopleLeaveRrdBean.setNoIndex(noindex);//13
        peopleLeaveRrdBean.setDestination("?");//15
        peopleLeaveRrdBean.setApproverNo("?");//16
        peopleLeaveRrdBean.setApproverName("?");//20
        peopleLeaveRrdBean.setHisAnnotation("?");//17
        peopleLeaveRrdBean.setResult("?");//18
        peopleLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());//19
        peopleLeaveRrdBean.setIsAndroid("1");//12
        peopleLeaveRrdBean.setOutType("?");//14
        peopleLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());//14
        String toJson = new Gson().toJson(peopleLeaveRrdBean);
        String s="Api_Get_MyApplyForPeo "+toJson;
//        L.e(TAG+"PeopleDetailFragment",s);
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s, PeopleLeaveDetailBean.class, new HttpManager.ResultNewCallback<PeopleLeaveDetailBean>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveDetailBean peopleLeaveDetailBean) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveDetailBean != null){
                            L.e(TAG+"加载",peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).toString());
                            String bCancel = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getBCancel();
                            String process1 = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getProcess();
                            String result = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getResult();
                            String outStatus = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getOutStatus();
                            String actualOutTime = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getActualOutTime();
                            String actualInTime = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getActualInTime();
                            if (process1.equals("1")){
                                if (result.equals("2")){
                                    type = 2;
                                }else if(result.equals("0")){
                                    type = 0;
                                }else if (result.equals("1")){
                                    if (outStatus.equals("0") && actualInTime.isEmpty() && actualOutTime.isEmpty()){
                                        type = 3;
                                    }else if (outStatus.equals("1") && !actualOutTime.isEmpty() && actualInTime.isEmpty()){
                                        type = 4;
                                    }else if (outStatus.equals("0") && !actualOutTime.isEmpty() && !actualInTime.isEmpty()){
                                        type = 0;
                                    }
                                }
                            }else if (process.equals("0")){
                                if (bCancel.equals("0")){
                                    type = 1;
                                }else {
                                    type = 0;
                                }
                            }else if (process.equals("2")){
                                type = 0;
                            }
                            String hisAnnotation = peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).getHisAnnotation();
                            String str = ";";
                            fenNum = StringUtils.method_5(hisAnnotation, str);
                            String [] arrAnnotation = hisAnnotation.split(";");
                            setEntity(peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0));
                            setGroup(getGroupList());
                            setPb(false);
                            setButtonllEnable(true);
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {

            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    public void setEntity(PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean entity) {
        this.entity = entity;
    }

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否已撤销")){
            showSelector(holder,new String[]{"是","否"});
        } /*else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }*/
    }
}
