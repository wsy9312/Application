package com.example.hgtxxgl.application.fragment.detail;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.car.CarApplyBean;
import com.example.hgtxxgl.application.bean.car.CarCheckBackBean;
import com.example.hgtxxgl.application.bean.car.CarCheckOutBean;
import com.example.hgtxxgl.application.bean.car.CarInfoBean;
import com.example.hgtxxgl.application.bean.car.CarLeaveDetailBean;
import com.example.hgtxxgl.application.bean.car.CarLeaveDetailRetractBean;
import com.example.hgtxxgl.application.bean.login.LoginInfoBean;
import com.example.hgtxxgl.application.bean.login.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleInfoBean;
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

//车辆外出申请详情
public class CarDetailFragment extends CommonFragment {

    private final static String TAG = "CarDetailFragment";
    private String s1;
    private String [][] buttonType = {{""},{"撤销申请"},{"重新提交"},{"确认离开"},{"确认归来"}};
    private int type = 0;
    private int fenNum;
    private String[] carNoArray;
    private String[] carOwnerNameArray;
    private String ownerNo1 = "";
    private String ownerNo2 = "";
    private LoginInfoBean.ApiAddLoginBean loginBean;
    private PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean;

    public CarDetailFragment(){

    }

    private CarLeaveDetailBean.ApiGetMyApplyForCarBean entity = null;

    public static CarDetailFragment newInstance(Bundle bundle) {
        CarDetailFragment fragment = new CarDetailFragment();
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
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人",true,false,peopleInfoBean.getName(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("所属单位",true,false,peopleInfoBean.getUnit(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("所属部门",true,false,peopleInfoBean.getDepartment(),HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌",false,false,entity.getCarNo(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("带车干部",false,false,entity.getLeaderName(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("驾驶员",false,false,entity.getDriverName(),HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,entity.getOutTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,entity.getInTime(),HandInputGroup.VALUE_TYPE.DATE).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("去向",false,false,entity.getDestination(),HandInputGroup.VALUE_TYPE.TEXTFILED).setColor(Color.rgb(128,128,128)));
                baseHolder.add(new HandInputGroup.Holder("事由",false,false,entity.getContent(),HandInputGroup.VALUE_TYPE.BIG_EDIT).setColor(Color.rgb(128,128,128)));
//                baseHolder.add(new HandInputGroup.Holder("是否后补申请",false,false,entity.getBFillup().equals("0")?"否":"是",HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(128,128,128)));
                groups.add(1,new Group("基本信息", null,true,null,baseHolder));
                setButtonsTitles(buttonType[2]);
                //重新提交,取消申请
            }else if(substring == 0){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(214,16,24)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
//                baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
                groups.add(1,new Group("基本信息", null, false, null, baseHolder));
                setButtonsTitles(buttonType[0]);
                //无
            }else if (substring == 1){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(0,128,0)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
                baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverName(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("实际离队时间", true, false, entity.getActualOutTime().isEmpty()?"":entity.getActualOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("实际归队时间", true, false, entity.getActualInTime().isEmpty()?"":entity.getActualInTime(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
                baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
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
            holders.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
            groups.add(0,new Group("流程信息", null, false, null, holders));
            List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
            baseHolder.add(new HandInputGroup.Holder("申请人", true, false, peopleInfoBean.getName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属单位", true, false, peopleInfoBean.getUnit(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("所属部门", true, false, peopleInfoBean.getDepartment(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
//            baseHolder.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
            groups.add(1,new Group("基本信息", null, false, null, baseHolder));
            setButtonsTitles(buttonType[0]);
        } else if (process == 0){
            if (bCancel.equals("0")){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
                holders.add(new HandInputGroup.Holder("审批状态", true, false, "未审批", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                holders.add(new HandInputGroup.Holder("审批结果", true, false, "无", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                holders.add(new HandInputGroup.Holder("是否已撤销", true, false, bCancel.equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(255,140,0)));
                groups.add(0,new Group("流程信息", null, false, null, holders));
                setButtonsTitles(buttonType[1]);
            }else if (bCancel.equals("1")){
                List<HandInputGroup.Holder> holders = new ArrayList<>();
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
            baseHolder.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverName(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
            baseHolder.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
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

    private void loadDraftData() {
        CarInfoBean.ApiGetCarInfoBean bean = new CarInfoBean.ApiGetCarInfoBean();
        bean.setNo("?");
        bean.setOwner1No("?");
        bean.setOwner2No("?");
        bean.setIsAndroid("1");
        bean.setTimeStamp(loginBean.getTimeStamp());
        bean.setAuthenticationNo(loginBean.getAuthenticationNo());
        String requestStr = "Api_Get_CarInfo "+new Gson().toJson(bean);
        L.e(TAG+"CarApplyFragment",requestStr);
        HttpManager.getInstance().requestNewResultForm(getTempIP(), requestStr, CarInfoBean.class, new HttpManager.ResultNewCallback<CarInfoBean>() {

            @Override
            public void onSuccess(String json, CarInfoBean carInfoBean) throws Exception {
                int size = carInfoBean.getApi_Get_CarInfo().size();
                List<String> carNoList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    carNoList.add(i,carInfoBean.getApi_Get_CarInfo().get(i).getNo());
                }
                carNoArray = carNoList.toArray(new String[carNoList.size()]);
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

    private void loadDraftData(String realValue) {
        CarInfoBean.ApiGetCarInfoBean bean = new CarInfoBean.ApiGetCarInfoBean();
        bean.setNo("?");
        bean.setOwner1No("?");
        bean.setOwner2No("?");
        bean.setIsAndroid("1");
        bean.setTimeStamp(loginBean.getTimeStamp());
        bean.setAuthenticationNo(loginBean.getAuthenticationNo());
        String requestStr = "Api_Get_CarInfo "+new Gson().toJson(bean);
        L.e(TAG+"CarApplyFragment",requestStr);
        HttpManager.getInstance().requestNewResultForm(getTempIP(), requestStr, CarInfoBean.class, new HttpManager.ResultNewCallback<CarInfoBean>() {

            @Override
            public void onSuccess(String json, CarInfoBean carInfoBean) throws Exception {
                List<String> carOwnerNameList = new ArrayList<>();
                int num = 0;
                if (!carInfoBean.getApi_Get_CarInfo().get(0).getOwner1Name().isEmpty()){
                    num++;
                }
                if (!carInfoBean.getApi_Get_CarInfo().get(0).getOwner2Name().isEmpty()){
                    num++;
                }
                if (num == 1){
                    carOwnerNameList.add(0,carInfoBean.getApi_Get_CarInfo().get(0).getOwner1Name());
                }else if(num == 2){
                    carOwnerNameList.add(0,carInfoBean.getApi_Get_CarInfo().get(0).getOwner1Name());
                    carOwnerNameList.add(1,carInfoBean.getApi_Get_CarInfo().get(0).getOwner2Name());
                }
                carOwnerNameArray = carOwnerNameList.toArray(new String[carOwnerNameList.size()]);
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
        final CarLeaveDetailBean.ApiGetMyApplyForCarBean carLeaveRrdBean = new CarLeaveDetailBean.ApiGetMyApplyForCarBean();
        final String noindex = getArguments().getString("noindex");
        final String carno = getArguments().getString("carno");
        final String driverno = getArguments().getString("driverno");
        final String leaderno = getArguments().getString("leaderno");
        carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        carLeaveRrdBean.setNo(loginBean.getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setNoIndex(noindex);
        carLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
        if (title.equals("撤销申请")){
            if (entity.getBCancel().equals("1")){
                show("已撤销,请勿重复操作!");
                return;
            }else{
                if (entity.getProcess().equals("1")){
                    show("审批已结束,不可撤销!");
                }else if (entity.getProcess().equals("2")){
                    show("审批进行中,不可撤销!");
                }
                carLeaveRrdBean.setBCancel("1");
                String json = new Gson().toJson(carLeaveRrdBean);
                s1 = "Api_Retract_CarLeave " + json;
                L.e(TAG,s1);
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
                        String unit = getDisplayValueByKey("单位").getRealValue();
                        String applicant = getDisplayValueByKey("申请人").getRealValue();
                        String carNo = getDisplayValueByKey("车辆号牌").getRealValue();
                        String driverName = getDisplayValueByKey("驾驶员").getRealValue();
                        String leaderName = getDisplayValueByKey("带车干部").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime = getDisplayValueByKey("归队时间").getRealValue();
                        String argument = getDisplayValueByKey("事由").getRealValue();
                        String goDirection  = getDisplayValueByKey("去向").getRealValue();
//                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        CarApplyBean.ApiApplyCarLeaveBean carLeaveRrdBean = new CarApplyBean.ApiApplyCarLeaveBean();
                        carLeaveRrdBean.setDestination(goDirection);
                        carLeaveRrdBean.setCarNo(carNo);
                        if (!ownerNo1.isEmpty()){
                            carLeaveRrdBean.setDriverNo(ownerNo1);
                        }else{
                            carLeaveRrdBean.setDriverNo(driverno);
                        }
                        if (!ownerNo2.isEmpty()){
                            carLeaveRrdBean.setLeaderNo(ownerNo2);
                        }else{
                            carLeaveRrdBean.setLeaderNo(leaderno);
                        }
                        carLeaveRrdBean.setNo(realValueNO);
                        carLeaveRrdBean.setOutTime(leaveTime);
                        carLeaveRrdBean.setInTime(returnTime);
                        carLeaveRrdBean.setContent(argument);
//                        carLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                        carLeaveRrdBean.setIsAndroid("1");
                        carLeaveRrdBean.setNoIndex(noindex);
                        carLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
                        String json = new Gson().toJson(carLeaveRrdBean);
                        String s1 = "Api_Apply_CarLeave " + json;
                        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, CarApplyBean.class, new HttpManager.ResultNewCallback<CarApplyBean>() {
                            @Override
                            public void onSuccess(String json, CarApplyBean carApplyBean) throws Exception {
                                if (carApplyBean.getApi_Apply_CarLeave().get(0) == null){
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
                    CarApplyBean.ApiApplyCarLeaveBean carLeaveRrdBean = new CarApplyBean.ApiApplyCarLeaveBean();
                    carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                    carLeaveRrdBean.setIsAndroid("1");
                    carLeaveRrdBean.setNoIndex(noindex);
                    carLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
                    String json = new Gson().toJson(carLeaveRrdBean);
                    String s1 = "Api_Edit_CheckOutForCar " + json;
                    HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, CarCheckOutBean.class, new HttpManager.ResultNewCallback<CarCheckOutBean>() {
                        @Override
                        public void onSuccess(String json, CarCheckOutBean carCheckOutBean) throws Exception {
                            L.e(TAG+" 确认离开",json);
                            if (json.contains("NotInTime")){
                                show("当前不在外出时间!");
                                getActivity().finish();
                            }
                            if (carCheckOutBean.getApi_Edit_CheckOutForCar().get(0) == null||carCheckOutBean.getApi_Edit_CheckOutForCar().size() == 0){
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
                    CarApplyBean.ApiApplyCarLeaveBean carLeaveRrdBean = new CarApplyBean.ApiApplyCarLeaveBean();
                    carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
                    carLeaveRrdBean.setIsAndroid("1");
                    carLeaveRrdBean.setNoIndex(noindex);
                    carLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
                    String json = new Gson().toJson(carLeaveRrdBean);
                    String s1 = "Api_Edit_CheckBackForCar " + json;
                    HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, CarCheckBackBean.class, new HttpManager.ResultNewCallback<CarCheckBackBean>() {
                        @Override
                        public void onSuccess(String json, CarCheckBackBean carCheckBackBean) throws Exception {
                            L.e(TAG+" 确认归来",json);
                            if (carCheckBackBean.getApi_Edit_CheckBackForCar().get(0) == null||carCheckBackBean.getApi_Edit_CheckBackForCar().size()==0){
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
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, CarLeaveDetailRetractBean.class, new HttpManager.ResultNewCallback<CarLeaveDetailRetractBean>() {

            @Override
            public void onSuccess(String json, CarLeaveDetailRetractBean carLeaveDetailRetractBean) throws Exception {
                if (carLeaveDetailRetractBean.getApi_Retract_CarLeave().get(0) == null){
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
        loadDraftData();
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
        CarLeaveDetailBean.ApiGetMyApplyForCarBean carLeaveRrdBean = new CarLeaveDetailBean.ApiGetMyApplyForCarBean();
        carLeaveRrdBean.setNo(peopleInfoBean.getAuthenticationNo());
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
        carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setDriverNo("?");
        carLeaveRrdBean.setLeaderNo("?");
        carLeaveRrdBean.setDriverName("?");
        carLeaveRrdBean.setLeaderName("?");
        carLeaveRrdBean.setHisAnnotation("?");
        carLeaveRrdBean.setDestination("?");
        carLeaveRrdBean.setTimeStamp(loginBean.getTimeStamp());
        String toJson = new Gson().toJson(carLeaveRrdBean);
        String s="Api_Get_MyApplyForCar "+toJson;
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s, CarLeaveDetailBean.class, new HttpManager.ResultNewCallback<CarLeaveDetailBean>() {
            @Override
            public void onSuccess(String json, final CarLeaveDetailBean carLeaveDetailBean) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (carLeaveDetailBean != null){
                            L.e(TAG+"加载",carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).toString());
                            String bCancel = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getBCancel();
                            String process = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getProcess();
                            String result = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getResult();
                            String outStatus = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getOutStatus();
                            String actualOutTime = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getActualOutTime();
                            String actualInTime = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getActualInTime();
                            if (process.equals("1")){
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

                            String hisAnnotation = carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0).getHisAnnotation();
                            String str = ";";
                            fenNum = StringUtils.method_5(hisAnnotation, str);
                            setEntity(carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0));
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

    public void setEntity(CarLeaveDetailBean.ApiGetMyApplyForCarBean entity) {
        this.entity = entity;
    }

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        }/* else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }*/ else if (holder.getKey().equals("车辆号牌")){
            if (!getDisplayValueByKey("驾驶员").getRealValue().isEmpty()){
                getDisplayValueByKey("驾驶员").setDispayValue("/请选择");
            }
            if (!getDisplayValueByKey("带车干部").getRealValue().isEmpty()){
                getDisplayValueByKey("带车干部").setDispayValue("/请选择");
            }
            if (carNoArray != null) {
                showSelector(holder, carNoArray, new OnSelectedResultCallback() {
                    @Override
                    public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                        String realValue = holder.getRealValue();
                        loadDraftData(realValue);
                    }
                });
            } else {
                ToastUtil.showToast(getContext(),"拉取失败");
            }
        } else if (holder.getKey().equals("驾驶员")||holder.getKey().equals("带车干部")){
            if (!getDisplayValueByKey("车辆号牌").getRealValue().isEmpty()) {
                if (carOwnerNameArray != null) {
                    showSelector(holder, carOwnerNameArray, new OnSelectedResultCallback() {
                        @Override
                        public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                            String str1 = ownGroup.getHolderByKey("驾驶员").getRealValue();
                            String str2 = ownGroup.getHolderByKey("带车干部").getRealValue();
                            for (int i = 0; i < ownGroup.getHolders().size(); i++) {
                                if (ownGroup.getHolders().get(i).getKey().equals("驾驶员")) {
                                    getNoFromName1(str1);
                                } else if (ownGroup.getHolders().get(i).getKey().equals("带车干部")) {
                                    getNoFromName2(str2);
                                }
                            }
                        }
                    });
                } else {
                    ToastUtil.showToast(getContext(), "拉取失败");
                }
            }else{
                show("请先选择车辆号牌!");
            }
        }
    }

    private void getNoFromName1(String driver) {
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setName(driver);
        peopleInfoBean.setNo("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleInfoBean.setTimeStamp(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_PeopleInfoSim " + json;
        HttpManager.getInstance().requestNewResultForm(getTempIP(),s1,TempPeopleInfoBean.class,new HttpManager.ResultNewCallback<TempPeopleInfoBean>() {
            @Override
            public void onSuccess(String json, TempPeopleInfoBean peopleInfoBean) throws Exception {
                ownerNo1 = peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getNo();
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

    private void getNoFromName2(String driver) {
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setName(driver);
        peopleInfoBean.setNo("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleInfoBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_PeopleInfoSim " + json;
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, TempPeopleInfoBean.class, new HttpManager.ResultNewCallback<TempPeopleInfoBean>() {
            @Override
            public void onSuccess(String json, TempPeopleInfoBean peopleInfoBean) throws Exception {
                ownerNo2 = peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getNo();
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
