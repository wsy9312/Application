package com.example.hgtxxgl.application.fragment.detail;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
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
//车辆外出申请详情
public class RestDetailCarFragment extends CommonFragment {

    private final static String TAG = "RestDetailCarFragment";
    private String s1;
    private String [][] buttonType = {{""},{"(取消申请) 是"},{"(取消申请) 是","重新提交"}};
    private int type = 0;
    private int fenNum;

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
        int process = Integer.parseInt(entity.getProcess());
        int substring = Integer.parseInt(entity.getResult());
        String bCancel = entity.getBCancel();
        if (process == 1){
            if (substring == 2){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "车辆申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "被退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人",true,false,ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("单位",true,false,ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌",true,false,entity.getCarNo(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("驾驶员",true,false,entity.getDriverNo(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("带车干部",true,false,entity.getLeaderNo(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,entity.getOutTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,entity.getInTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("事由",false,false,entity.getContent(),HandInputGroup.VALUE_TYPE.BIG_EDIT).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("去向",false,false,entity.getDestination(),HandInputGroup.VALUE_TYPE.TEXTFILED).setColor(Color.rgb(170,170,170)));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请",false,false,entity.getBFillup().equals("0")?"否":"是",HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(170,170,170)));
                groups.add(1,new Group("基本信息", null,true,null,baseHolder));
                setButtonsTitles(buttonType[2]);
                //重新提交,取消申请
            }else if(substring == 0){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "不同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderNo(), HandInputGroup.VALUE_TYPE.TEXT));
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
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderNo(), HandInputGroup.VALUE_TYPE.TEXT));
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
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[1]);
                //取消申请
            }else{
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("流程内容", true, false, "请假申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已取消", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("单位", true, false, ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[0]);
                //无
            }
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
        toolbar.setTitle("车辆申请");
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
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        final String noindex = getArguments().getString("noindex");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setNoIndex(noindex);
        if (title.equals("(取消申请) 是")){
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
                carLeaveRrdBean.setBCancel("1");
                List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                beanList.add(carLeaveRrdBean);
                carLeaveEntity.setCarLeaveRrd(beanList);
                String json = new Gson().toJson(carLeaveEntity);
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
                        String realValueNO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
                        String unit = getDisplayValueByKey("单位").getRealValue();
                        String applicant = getDisplayValueByKey("申请人").getRealValue();
                        String carNo = getDisplayValueByKey("车辆号牌").getRealValue();
                        String driverName = getDisplayValueByKey("驾驶员").getRealValue();
                        String leaderName = getDisplayValueByKey("带队干部").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime = getDisplayValueByKey("归队时间").getRealValue();
                        String argument = getDisplayValueByKey("事由").getRealValue();
                        String goDirection  = getDisplayValueByKey("去向").getRealValue();
                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
                        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
                        carLeaveRrdBean.setDestination(goDirection);
                        carLeaveRrdBean.setCarNo(carNo);
                        carLeaveRrdBean.setDriverNo(driverName);
                        carLeaveRrdBean.setLeaderNo(leaderName);
                        carLeaveRrdBean.setNo(realValueNO);
                        carLeaveRrdBean.setOutTime(leaveTime);
                        carLeaveRrdBean.setInTime(returnTime);
                        carLeaveRrdBean.setContent(argument);
                        carLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                        carLeaveRrdBean.setIsAndroid("1");
                        carLeaveRrdBean.setNoIndex(noindex);
                        List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                        beanList.add(carLeaveRrdBean);
                        carLeaveEntity.setCarLeaveRrd(beanList);
                        String json = new Gson().toJson(carLeaveEntity);
                        String s1 = "apply " + json;
                        HttpManager.getInstance().requestResultForm(getTempIP(), s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
                            @Override
                            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity) throws InterruptedException {
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
        HttpManager.getInstance().requestResultForm(baseUrl, s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity) throws InterruptedException {
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
                    show("审批已完成,修改失败");
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
        String process = getArguments().getString("process");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        String noindex = getArguments().getString("noindex");
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        carLeaveRrdBean.setApproverNo("?");
        carLeaveRrdBean.setRegisterTime("?");
        carLeaveRrdBean.setOutTime("?");
        carLeaveRrdBean.setInTime("?");
        carLeaveRrdBean.setContent("?");
        carLeaveRrdBean.setActualOutTime("?");
        carLeaveRrdBean.setActualInTime("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setProcess("?");
        carLeaveRrdBean.setResult("?");
        carLeaveRrdBean.setBCancel("?");
        carLeaveRrdBean.setBFillup("?");
        carLeaveRrdBean.setNoIndex(noindex);
        carLeaveRrdBean.setBeginNum("?");
        carLeaveRrdBean.setEndNum("?");
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setDriverNo("?");
        carLeaveRrdBean.setLeaderNo("?");
        carLeaveRrdBean.setHisAnnotation("?");
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String toJson = new Gson().toJson(carLeaveEntity);
        String s="get "+toJson;
//        String url = ApplicationApp.getIP();
        HttpManager.getInstance().requestResultForm(getTempIP(), s, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (carLeaveEntity1 != null){
                            String bCancel = carLeaveEntity1.getCarLeaveRrd().get(0).getBCancel();
                            String process1 = carLeaveEntity1.getCarLeaveRrd().get(0).getProcess();
                            String result = carLeaveEntity1.getCarLeaveRrd().get(0).getResult();
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

                            String hisAnnotation = carLeaveEntity1.getCarLeaveRrd().get(0).getHisAnnotation();
                            String str = ";";
                            fenNum = StringUtils.method_5(hisAnnotation, str);
                            setEntity(carLeaveEntity1.getCarLeaveRrd().get(0));
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

    public CarLeaveEntity.CarLeaveRrdBean getEntity() {
        return entity;
    }

    public void setEntity(CarLeaveEntity.CarLeaveRrdBean entity) {
        this.entity = entity;
    }

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }
    }
}
