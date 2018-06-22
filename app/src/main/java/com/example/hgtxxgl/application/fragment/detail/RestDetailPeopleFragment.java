package com.example.hgtxxgl.application.fragment.detail;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.NewLoginEntity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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

//人员请假申请详情
public class RestDetailPeopleFragment extends CommonFragment {

    private final static String TAG = "RestDetailPeopleFragment";
    private String s1;
    private String [][] buttonType = {{""},{"取消申请"},{"取消申请","重新提交"}};
    private int type = 0;
    private int fenNum;
    private NewLoginEntity.LoginBean loginBean;
    private PeopleInfoEntity.PeopleInfoBean peopleInfoBean;

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
        int process = Integer.parseInt(entity.getProcess());
        int substring = Integer.parseInt(entity.getResult());
        String bCancel = entity.getBCancel();
        if (process == 1 && bCancel.equals("0")){
            if (substring == 2){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人",true,false,peopleInfoBean.getName(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("单位",true,false,peopleInfoBean.getUnit(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("部门",true,false,peopleInfoBean.getDepartment(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("申请类型",true,false,entity.getOutType(),HandInputGroup.VALUE_TYPE.TEXTFILED).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,entity.getOutTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,entity.getInTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("事由",false,false,entity.getContent(),HandInputGroup.VALUE_TYPE.BIG_EDIT).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("去向",false,false,entity.getDestination(),HandInputGroup.VALUE_TYPE.TEXTFILED).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请",false,false,entity.getBFillup().equals("0")?"否":"是",HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                groups.add(1,new Group("基本信息", null,true,null,baseHolder));
                setButtonsTitles(buttonType[2]);
                //重新提交,取消申请
            }else if(substring == 0){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[0]);
                //无
            }else if (substring == 1){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[0]);
                //无
            }
        }else{
            if (bCancel.equals("0")){
                if (entity.getApproverNo().isEmpty()){
                    List<HandInputGroup.Holder> holders = new ArrayList<>();
                    holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("审批状态", true, false, "未审批", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    groups.add(0,new Group("流程信息", null, false, null, holders));
                    setButtonsTitles(buttonType[1]);
                }else{
                    List<HandInputGroup.Holder> holders = new ArrayList<>();
                    holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                    groups.add(0,new Group("流程信息", null, false, null, holders));
                    setButtonsTitles(buttonType[1]);
                }
                //取消申请
            }else{
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "已取消", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已取消", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                setButtonsTitles(buttonType[0]);
                //无
            }
            List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
            baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("申请类型", true, false, entity.getOutType(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
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
        }else {
            return buttonType[2];
        }
    }

    @Override
    public void onBottomButtonsClick(String title, final List<Group> groups) {
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        final String noindex = getArguments().getString("noindex");
        peopleLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        peopleLeaveRrdBean.setNo(loginBean.getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("取消申请")){
            if (entity.getBCancel().equals("1")){
                show("已取消,请勿重复操作!");
                return;
            }else{
                if (entity.getProcess().equals("1")){
                    if (entity.getResult().equals("0")||entity.getResult().equals("1")){
                        show("审批已结束,不可取消!");
                        return;
                    }
                }else if (entity.getProcess().equals("0")){
                    if (entity.getResult().equals("0")){
                        if (!entity.getApproverNo().isEmpty()){
                            show("审批进行中,不可取消!");
                            return;
                        }
                    }
                }
                peopleLeaveRrdBean.setBCancel("1");
                List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
                beanList.add(peopleLeaveRrdBean);
                peopleLeaveEntity.setPeopleLeaveRrd(beanList);
                String json = new Gson().toJson(peopleLeaveEntity);
                s1 = "modify " + json;
                L.e(TAG,s1);
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("是否确认?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        applyModify(getTempIP(), s1);
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
                        ToastUtil.showToast(getContext(),"请填写" + over);
                        setButtonllEnable(true);
                    }else {
                        //申请人ID
                        String realValueNO = peopleInfoBean.getNo();
                        String unit = getDisplayValueByKey("单位").getRealValue();
                        String applicant = getDisplayValueByKey("申请人").getRealValue();
                        String applicantType = getDisplayValueByKey("申请类型").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime = getDisplayValueByKey("归队时间").getRealValue();
                        String argument = getDisplayValueByKey("事由").getRealValue();
                        String goDirection  = getDisplayValueByKey("去向").getRealValue();
                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
                        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
                        peopleLeaveRrdBean.setDestination(goDirection);
                        peopleLeaveRrdBean.setNo(realValueNO);
                        peopleLeaveRrdBean.setOutType(applicantType);
                        peopleLeaveRrdBean.setOutTime(leaveTime);
                        peopleLeaveRrdBean.setInTime(returnTime);
                        peopleLeaveRrdBean.setContent(argument);
                        peopleLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        peopleLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                        peopleLeaveRrdBean.setIsAndroid("1");
                        peopleLeaveRrdBean.setNoIndex(noindex);
                        List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
                        beanList.add(peopleLeaveRrdBean);
                        peopleLeaveEntity.setPeopleLeaveRrd(beanList);
                        String json = new Gson().toJson(peopleLeaveEntity);
                        String s1 = "apply " + json;
                        HttpManager.getInstance().requestResultForm(getTempIP(), s1, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
                            @Override
                            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity) throws InterruptedException {
                            }

                            @Override
                            public void onFailure(final String msg) {
                            }

                            @Override
                            public void onResponse(String response) {
                                if (response.toLowerCase().contains("ok")) {
                                    show("提交成功");
                                    getActivity().finish();
                                }else{
                                    show("提交失败");
                                    getActivity().finish();
                                }
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
        }
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
                    getActivity().finish();
                }else{
                    show("已在审批当中,取消申请失败");
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBean = ApplicationApp.getNewLoginEntity().getLogin().get(0);
        peopleInfoBean = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0);
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
        String process = getArguments().getString("process");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        String noindex = getArguments().getString("noindex");
        final PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
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
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String toJson = new Gson().toJson(peopleLeaveEntity);
        String s="get "+toJson;
        L.e(TAG+"RestDetailPeopleFragment",s);
        HttpManager.getInstance().requestResultForm(getTempIP(), s, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (peopleLeaveEntity1 != null){
                            String bCancel = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getBCancel();
                            String process1 = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getProcess();
                            String result = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getResult();
                            if (process1.equals("1")){
                                if (result.equals("2")){
                                    type = 2;
                                }else if(result.equals("0")){
                                    type = 0;
                                }else if (result.equals("1")){
                                    type = 0;
                                }
                            }else{
                                if (bCancel.equals("0")){
                                    type = 1;
                                }else {
                                    type = 0;
                                }
                            }
                            String hisAnnotation = peopleLeaveEntity1.getPeopleLeaveRrd().get(0).getHisAnnotation();
                            String str = ";";
                            fenNum = StringUtils.method_5(hisAnnotation, str);
                            String [] arrAnnotation = hisAnnotation.split(";");
                            L.e(TAG,fenNum+"*******"+ arrAnnotation.length);
                            L.e(TAG+"RestDetailPeopleFragment",peopleLeaveEntity1.toString());
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
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    public PeopleLeaveEntity.PeopleLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(PeopleLeaveEntity.PeopleLeaveRrdBean entity) {
        this.entity = entity;
    }

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否取消请假")){
            showSelector(holder,new String[]{"是","否"});
        } else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }
    }
}
