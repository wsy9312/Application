package com.example.hgtxxgl.application.fragment.approve;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.hgtxxgl.application.bean.car.CarApproveBean;
import com.example.hgtxxgl.application.bean.car.CarApproveDelayBean;
import com.example.hgtxxgl.application.bean.car.CarApproveFinishBean;
import com.example.hgtxxgl.application.bean.login.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleInfoBean;
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

import okhttp3.Request;

public class CarApproveDetailFragment extends CommonFragment{
    private final static String TAG = "CarApproveDetailFragment";
    private String noindex;
    private String approveState;
    private int fenNum;
    private String authenticationNo;

    public CarApproveDetailFragment(){

    }

    public static CarApproveDetailFragment newInstance(Bundle bundle) {
        CarApproveDetailFragment fragment = new CarApproveDetailFragment();
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
//        if (entity == null) return null;
        List<Group> groups = new ArrayList<>();
        int process = Integer.parseInt(Process);
        List<HandInputGroup.Holder> list = new ArrayList<>();
        if (process == 0){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "待审批", HandInputGroup.VALUE_TYPE.TEXT));
        }else if (process == 1){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT));
        }else if(process == 2){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT));
        }
        int substring = Integer.parseInt(Result);
        if (process == 1){
            setButtonsTitles(stringnull);
            switch (substring){
                case 0:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(237,142,148)));
                    break;
                case 1:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(86,197,163)));
                    break;
                case 2:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(237,142,148)));
                    break;
            }
        }else{
            if(!ApproverNo.isEmpty()){
                if (ApproverNo.contains(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo())) {
                    setButtonsTitles(stringnull);
                }
            }
            list.add(new HandInputGroup.Holder("审批结果", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(Color.rgb(218,176,101)));
        }
        if (bCancel.equals("0")){
            if (Process.equals("1")){
                setButtonsTitles(stringnull);
            }
        }

        groups.add(new Group("流程信息", null, false, null, list));

        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder("申请人", true, false, getArguments().getString("name"), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("所属单位", true, false, Unit, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("所属部门", true, false, Department, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("车辆号牌", true, false, CarNo, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("带车干部", true, false, LeaderName, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("驾驶员", true, false, DriverName, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("离队时间", true, false, OutTime, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("归队时间", true, false, InTime, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("实际离队时间", true, false, ActualOutTime, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("实际归队时间", true, false, ActualInTime, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("去向", true, false, Destination, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("事由", true, false, Content, HandInputGroup.VALUE_TYPE.TEXT));

//        holderList.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("基本信息", null, false, null, holderList));

        String split1 = HisAnnotation;
        String split2 = ApproverNo;
        String split3 = ApproverName;
        String [] arrAnnotation = split1.split(";");
        L.e("##**_Q"+"split1:"+split1+" split2:"+split2+" split3:"+split3);
        L.e("##**_W"+"审批人批注长度:"+arrAnnotation.length);
        String [] arrName = split3.split(";");
        L.e("##**_E"+"审批人名字长度:"+arrName.length);
        List<HandInputGroup.Holder> holder = new ArrayList<>();
        if (bCancel.equals("0")){
            if (process == 1){
                if (arrName.length > 0 && arrAnnotation.length == 0){
                    for (int i = 0; i < fenNum; i++) {
                        holder.add(new HandInputGroup.Holder(arrName[i], false, false, "无批注", HandInputGroup.VALUE_TYPE.TEXT));
                    }
                }else{
                    for (int i = 0; i < fenNum; i++) {
                        holder.add(new HandInputGroup.Holder(arrName[i], false, false, arrAnnotation[i], HandInputGroup.VALUE_TYPE.TEXT));
                    }
                }
                groups.add(new Group("批注信息", null, false, null, holder));
            }else if (process == 2){
                if (fenNum > 0){
                    if (arrName.length > 0 && arrAnnotation.length == 0){
                        for (int i = 0; i < fenNum; i++) {
                            holder.add(new HandInputGroup.Holder(arrName[i], false, false, "无批注", HandInputGroup.VALUE_TYPE.TEXT));
                        }
                    }else{
                        for (int i = 0; i < fenNum; i++) {
                            holder.add(new HandInputGroup.Holder(arrName[i], false, false, arrAnnotation[i], HandInputGroup.VALUE_TYPE.TEXT));
                        }
                    }
                    if (!split2.contains(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo())){
                        holder.add(new HandInputGroup.Holder("您的批注:", false, false, "/请填写", HandInputGroup.VALUE_TYPE.BIG_EDIT));
                    }
                }
                groups.add(new Group("批注信息", null, false, null, holder));
            }else if (process == 0){
                holder.add(new HandInputGroup.Holder("您的批注:", false, false, "/请填写", HandInputGroup.VALUE_TYPE.BIG_EDIT));
                groups.add(new Group("批注信息", null, false, null, holder));
            }
        }
        return groups;
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("车辆审批");
        toolbar.setTitleSize(18);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
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
        noindex = getArguments().getString("noindex");
        approveState = getArguments().getString("approveState");
        if (approveState.equals("1")){
            CarApproveFinishBean.ApiGetMyApproveForCarHisBean carLeaveRrdBean = new CarApproveFinishBean.ApiGetMyApproveForCarHisBean();
            carLeaveRrdBean.setRegisterTime("?");
            carLeaveRrdBean.setOutTime("?");
            carLeaveRrdBean.setInTime("?");
            carLeaveRrdBean.setContent("?");
            carLeaveRrdBean.setActualOutTime("?");
            carLeaveRrdBean.setActualInTime("?");
            carLeaveRrdBean.setModifyTime("?");
            carLeaveRrdBean.setProcess("?");
            carLeaveRrdBean.setBCancel("?");
            carLeaveRrdBean.setBFillup("?");
            carLeaveRrdBean.setIsAndroid("1");
            carLeaveRrdBean.setNoIndex(noindex);
            carLeaveRrdBean.setNo("?");
            carLeaveRrdBean.setOutType("?");
            carLeaveRrdBean.setAuthenticationNo(authenticationNo);
            carLeaveRrdBean.setDestination("?");
            carLeaveRrdBean.setApproverNo("?");
            carLeaveRrdBean.setHisAnnotation("?");
            carLeaveRrdBean.setResult("?");
            carLeaveRrdBean.setCarNo("?");
            carLeaveRrdBean.setDriverNo("?");
            carLeaveRrdBean.setLeaderNo("?");
            carLeaveRrdBean.setDriverName("?");
            carLeaveRrdBean.setLeaderName("?");
            carLeaveRrdBean.setCurrentApproverNo("?");
            carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
            String toJson = new Gson().toJson(carLeaveRrdBean);
            String s="Api_Get_MyApproveForCarHis "+toJson;
            L.e(TAG+"Api_Get_MyApproveForCarHis",s);
            HttpManager.getInstance().requestNewResultForm(getTempIP(), s, CarApproveFinishBean.class, new HttpManager.ResultNewCallback<CarApproveFinishBean>() {
                @Override
                public void onSuccess(String json, final CarApproveFinishBean carApproveFinishBean) throws Exception {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (carApproveFinishBean != null){
                                if (carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0).getBCancel().equals("0")){
                                    PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
                                    peopleInfoBean.setNo(carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0).getNo());
                                    peopleInfoBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
                                    peopleInfoBean.setUnit("?");
                                    peopleInfoBean.setDepartment("?");
                                    peopleInfoBean.setIsAndroid("1");
                                    peopleInfoBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
                                    String json = new Gson().toJson(peopleInfoBean);
                                    String s1 = "Api_Get_PeopleInfoSim " + json;
                                    L.e(TAG+"Api_Get_PeopleInfoSim",s1);
                                    HttpManager.getInstance().requestNewResultForm(getTempIP(),s1,TempPeopleInfoBean.class, new HttpManager.ResultNewCallback<TempPeopleInfoBean>() {
                                        @Override
                                        public void onSuccess(String json, TempPeopleInfoBean peopleInfoBean) throws Exception {
                                            if (peopleInfoBean != null){
                                                carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0).setUnit(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getUnit());
                                                carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0).setDepartment(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getDepartment());
                                                String hisAnnotation = carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0).getHisAnnotation();
                                                String str = ";";
                                                fenNum = StringUtils.method_5(hisAnnotation, str);
                                                L.e("##**分号数目_已审批:"+fenNum);
                                                CarApproveFinishBean.ApiGetMyApproveForCarHisBean showBean = carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0);
                                                setEntity(showBean.getActualInTime(),showBean.getActualOutTime(),showBean.getApproverName(),showBean.getApproverNo(),
                                                        showBean.getContent(),showBean.getCount(),showBean.getCurrentApproverName(),showBean.getCurrentApproverNo(),
                                                        showBean.getDestination(),showBean.getHisAnnotation(),showBean.getInTime(),
                                                        showBean.getModifyTime(),showBean.getName(),showBean.getNo(),showBean.getNoIndex(),showBean.getOutStatus(),
                                                        showBean.getOutTime(),showBean.getOutType(),showBean.getProcess(),showBean.getRegisterTime(),showBean.getResult(),
                                                        showBean.getBCancel(),showBean.getBFillup(),showBean.getBMessage(),
                                                        showBean.getAuthenticationNo(),showBean.getIsAndroid(),showBean.getCurResult(),showBean.getBeginNum(),showBean.getEndNum(),
                                                        showBean.getUnit(),showBean.getDepartment(),showBean.getCurannotation(),showBean.getTimeStamp(),showBean.getCarNo(),
                                                        showBean.getDriverNo(),showBean.getLeaderNo(),showBean.getDriverName(),showBean.getLeaderName());
                                                setGroup(getGroupList());
                                                setPb(false);
                                                setButtonllEnable(true);
                                                notifyDataSetChanged();
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
        }else if (approveState.equals("0")){
            CarApproveDelayBean.ApiGetMyApproveForCarBean carLeaveRrdBean = new CarApproveDelayBean.ApiGetMyApproveForCarBean();
            carLeaveRrdBean.setRegisterTime("?");
            carLeaveRrdBean.setOutTime("?");
            carLeaveRrdBean.setInTime("?");
            carLeaveRrdBean.setContent("?");
            carLeaveRrdBean.setActualOutTime("?");
            carLeaveRrdBean.setActualInTime("?");
            carLeaveRrdBean.setModifyTime("?");
            carLeaveRrdBean.setProcess("?");
            carLeaveRrdBean.setBCancel("?");
            carLeaveRrdBean.setBFillup("?");
            carLeaveRrdBean.setIsAndroid("1");
            carLeaveRrdBean.setNoIndex(noindex);
            carLeaveRrdBean.setNo("?");
            carLeaveRrdBean.setOutType("?");
            carLeaveRrdBean.setAuthenticationNo(authenticationNo);
            carLeaveRrdBean.setDestination("?");
            carLeaveRrdBean.setApproverNo("?");
            carLeaveRrdBean.setHisAnnotation("?");
            carLeaveRrdBean.setResult("?");
            carLeaveRrdBean.setCarNo("?");
            carLeaveRrdBean.setDriverNo("?");
            carLeaveRrdBean.setLeaderNo("?");
            carLeaveRrdBean.setDriverName("?");
            carLeaveRrdBean.setLeaderName("?");
            carLeaveRrdBean.setCurrentApproverNo("?");
            carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
            String toJson = new Gson().toJson(carLeaveRrdBean);
            String s="Api_Get_MyApproveForCar "+toJson;
            L.e(TAG+"Api_Get_MyApproveForCar",s);
            HttpManager.getInstance().requestNewResultForm(getTempIP(), s, CarApproveDelayBean.class, new HttpManager.ResultNewCallback<CarApproveDelayBean>() {
                @Override
                public void onSuccess(String json, final CarApproveDelayBean carApproveDelayBean) throws Exception {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (carApproveDelayBean != null){
                                if (carApproveDelayBean.getApi_Get_MyApproveForCar().get(0).getBCancel().equals("0")){
                                    PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
                                    peopleInfoBean.setNo(carApproveDelayBean.getApi_Get_MyApproveForCar().get(0).getNo());
                                    peopleInfoBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
                                    peopleInfoBean.setUnit("?");
                                    peopleInfoBean.setDepartment("?");
                                    peopleInfoBean.setIsAndroid("1");
                                    peopleInfoBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
                                    String json = new Gson().toJson(peopleInfoBean);
                                    String s1 = "Api_Get_PeopleInfoSim " + json;
                                    L.e(TAG+"Api_Get_PeopleInfoSim",s1);
                                    HttpManager.getInstance().requestNewResultForm(getTempIP(),s1,TempPeopleInfoBean.class, new HttpManager.ResultNewCallback<TempPeopleInfoBean>() {
                                        @Override
                                        public void onSuccess(String json, TempPeopleInfoBean peopleInfoBean) throws Exception {
                                            if (peopleInfoBean != null){
                                                carApproveDelayBean.getApi_Get_MyApproveForCar().get(0).setUnit(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getUnit());
                                                carApproveDelayBean.getApi_Get_MyApproveForCar().get(0).setDepartment(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getDepartment());
                                                String hisAnnotation = carApproveDelayBean.getApi_Get_MyApproveForCar().get(0).getHisAnnotation();
                                                String str = ";";
                                                fenNum = StringUtils.method_5(hisAnnotation, str);
                                                L.e("##**分号数目_未审批:"+fenNum);
                                                CarApproveDelayBean.ApiGetMyApproveForCarBean showBean = carApproveDelayBean.getApi_Get_MyApproveForCar().get(0);
                                                setEntity(showBean.getActualInTime(),showBean.getActualOutTime(),showBean.getApproverName(),showBean.getApproverNo(),
                                                        showBean.getContent(),showBean.getCount(),showBean.getCurrentApproverName(),showBean.getCurrentApproverNo(),
                                                        showBean.getDestination(),showBean.getHisAnnotation(),showBean.getInTime(),
                                                        showBean.getModifyTime(),showBean.getName(),showBean.getNo(),showBean.getNoIndex(),showBean.getOutStatus(),
                                                        showBean.getOutTime(),showBean.getOutType(),showBean.getProcess(),showBean.getRegisterTime(),showBean.getResult(),
                                                        showBean.getBCancel(),showBean.getBFillup(),showBean.getBMessage(),
                                                        showBean.getAuthenticationNo(),showBean.getIsAndroid(),showBean.getCurResult(),showBean.getBeginNum(),showBean.getEndNum(),
                                                        showBean.getUnit(),showBean.getDepartment(),showBean.getCurannotation(),
                                                        showBean.getTimeStamp(),showBean.getCarNo(),showBean.getDriverNo(),showBean.getLeaderNo(),showBean.getDriverName(),showBean.getLeaderName());
                                                setGroup(getGroupList());
                                                setPb(false);
                                                setButtonllEnable(true);
                                                notifyDataSetChanged();
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

    }

    @Override
    public void onBottomButtonsClick(String title, List<Group> groups) {
        CarApproveBean.ApiAppoveCarLeaveBean carLeaveRrdBean = new CarApproveBean.ApiAppoveCarLeaveBean();
        carLeaveRrdBean.setNoIndex(noindex);
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        switch (title){
            case "同意结束":
                carLeaveRrdBean.setCurResult("3");
                break;
            case "拒 绝":
                carLeaveRrdBean.setCurResult("0");
                break;
            case "同意上报":
                carLeaveRrdBean.setCurResult("2");
                break;
            case "退 回":
                carLeaveRrdBean.setCurResult("1");
                break;
        }
        String realValue1 = getDisplayValueByKey("您的批注:").getRealValue();
        String realValue = realValue1.isEmpty()?"无批注":realValue1;
        carLeaveRrdBean.setCurannotation(realValue);
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(carLeaveRrdBean);
        final String s1 = "Api_Appove_CarLeave " + json;
        L.e(TAG+"审批提交:",s1);
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
        HttpManager.getInstance().requestNewResultForm(baseUrl, s1, CarApproveBean.class, new HttpManager.ResultNewCallback<CarApproveBean>() {

            @Override
            public void onSuccess(String json, CarApproveBean carApproveBean) throws Exception {
                if (json.contains("ApproverIsNotFound")){
                    show("上报下级审批人失败!");
                    getActivity().finish();
                }else if (json.contains("ApplyIsDone")){
                    show("申请已取消或结束!");
                    getActivity().finish();
                }else {
                    show("审批完成!");
                    getActivity().finish();
                }
                L.e("onSuccess"+json);
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

    private String ActualInTime;
    private String ActualOutTime;
    private String ApproverName;
    private String ApproverNo;
    private String Content;
    private String Count;
    private String CurrentApproverName;
    private String CurrentApproverNo;
    private String Destination;
    private String HisAnnotation;
    private String InTime;
    private String ModifyTime;
    private String Name;
    private String No;
    private String NoIndex;
    private String OutStatus;
    private String OutTime;
    private String OutType;
    private String Process;
    private String RegisterTime;
    private String Result;
    private String bCancel;
    private String bFillup;
    private String bMessage;
    private String AuthenticationNo;
    private String IsAndroid;
    private String CurResult;
    private String BeginNum;
    private String EndNum;
    private String Unit;
    private String Department;
    private String Curannotation;
    private String TimeStamp;
    private String CarNo;
    private String DriverNo;
    private String LeaderNo;
    private String DriverName;
    private String LeaderName;

    public void setEntity(String ActualInTime,String ActualOutTime,String ApproverName,String ApproverNo,String Content,String Count,String CurrentApproverName,String CurrentApproverNo,
                          String Destination,String HisAnnotation,String InTime,String ModifyTime,String Name,String No,String NoIndex,String OutStatus,String OutTime,
                          String OutType,String Process,String RegisterTime,String Result,String bCancel,String bFillup,String bMessage,String AuthenticationNo,
                          String IsAndroid,String CurResult,String BeginNum,String EndNum,String Unit,String Department,String Curannotation,String TimeStamp,
                          String CarNo,String DriverNo,String LeaderNo,String DriverName,String LeaderName) {
        this.ActualInTime = ActualInTime;
        this.ActualOutTime = ActualOutTime;
        this.ApproverName = ApproverName;
        this.ApproverNo = ApproverNo;
        this.Content = Content;
        this.Count = Count;
        this.CurrentApproverName = CurrentApproverName;
        this.CurrentApproverNo = CurrentApproverNo;
        this.Destination = Destination;
        this.HisAnnotation = HisAnnotation;
        this.InTime = InTime;
        this.ModifyTime = ModifyTime;
        this.Name = Name;
        this.No = No;
        this.NoIndex = NoIndex;
        this.OutStatus = OutStatus;
        this.OutTime = OutTime;
        this.OutType = OutType;
        this.Process = Process;
        this.RegisterTime = RegisterTime;
        this.Result = Result;
        this.bCancel = bCancel;
        this.bFillup = bFillup;
        this.bMessage = bMessage;
        this.AuthenticationNo = AuthenticationNo;
        this.IsAndroid = IsAndroid;
        this.CurResult = CurResult;
        this.BeginNum = BeginNum;
        this.EndNum = EndNum;
        this.Unit = Unit;
        this.Department = Department;
        this.Curannotation = Curannotation;
        this.TimeStamp = TimeStamp;
        this.CarNo = CarNo;
        this.DriverNo = DriverNo;
        this.LeaderNo = LeaderNo;
        this.DriverName = DriverName;
        this.LeaderName = LeaderName;
    }
}
