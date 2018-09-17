package com.example.hgtxxgl.application.fragment.approve;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.example.hgtxxgl.application.bean.people.PeopleApproveBean;
import com.example.hgtxxgl.application.bean.people.PeopleApproveDelayBean;
import com.example.hgtxxgl.application.bean.people.PeopleApproveFinishBean;
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

//人员外出审批
public class PeopleApproveDetailFragment extends CommonFragment {
    private final static String TAG = "PeopleApproveDetailFragment";
    private String noindex;
    private String approveState;
    private int fenNum;
    private String authenticationNo;

    public PeopleApproveDetailFragment(){

    }

    public static PeopleApproveDetailFragment newInstance(Bundle bundle) {
        PeopleApproveDetailFragment fragment = new PeopleApproveDetailFragment();
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
        list.add(new HandInputGroup.Holder("申请类型", true, false, OutType, HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        if (process == 0){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "未审批", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        }else if (process == 1){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        }else if(process == 2){
            list.add(new HandInputGroup.Holder("审批状态", true, false, "审批中", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        }
        int substring = Integer.parseInt(Result);
        if (process == 1){
            setButtonsTitles(stringnull);
            switch (substring){
                case 0:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已拒绝", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 1:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 2:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "已退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
            }
        }else{
            if(!ApproverNo.isEmpty()){
                if (ApproverNo.contains(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo())) {
                    setButtonsTitles(stringnull);
                }
            }
            list.add(new HandInputGroup.Holder("审批结果", true, false, "暂无", HandInputGroup.VALUE_TYPE.TEXT).setColor(ApproverNo.contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
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
        holderList.add(new HandInputGroup.Holder("离队时间", true, false, OutTime, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("归队时间", true, false, InTime, HandInputGroup.VALUE_TYPE.TEXT));
        if (!TextUtils.equals("-9999",VacationDays)){
            holderList.add(new HandInputGroup.Holder("假期天数", true, false, VacationDays, HandInputGroup.VALUE_TYPE.TEXT));
        }
        if (!TextUtils.equals("-9999",JourneyDays)){
            holderList.add(new HandInputGroup.Holder("路途天数", true, false, JourneyDays, HandInputGroup.VALUE_TYPE.TEXT));
        }
        holderList.add(new HandInputGroup.Holder("去向", true, false, Destination, HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("事由", true, false, Content, HandInputGroup.VALUE_TYPE.TEXT));
        if (!TextUtils.isEmpty(VacationAddr)){
            holderList.add(new HandInputGroup.Holder("疗养地址", true, false, VacationAddr, HandInputGroup.VALUE_TYPE.TEXT));
        }

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
        toolbar.setTitle("请假审批");
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
            PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean peopleLeaveRrdBean = new PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean();
            peopleLeaveRrdBean.setRegisterTime("?");
            peopleLeaveRrdBean.setOutTime("?");
            peopleLeaveRrdBean.setInTime("?");
            peopleLeaveRrdBean.setContent("?");
            peopleLeaveRrdBean.setActualOutTime("?");
            peopleLeaveRrdBean.setActualInTime("?");
            peopleLeaveRrdBean.setModifyTime("?");
            peopleLeaveRrdBean.setProcess("?");
            peopleLeaveRrdBean.setBCancel("?");
            peopleLeaveRrdBean.setBFillup("?");
            peopleLeaveRrdBean.setIsAndroid("1");
            peopleLeaveRrdBean.setNoIndex(noindex);
            peopleLeaveRrdBean.setNo("?");
            peopleLeaveRrdBean.setOutType("?");
            peopleLeaveRrdBean.setAuthenticationNo(authenticationNo);
            peopleLeaveRrdBean.setDestination("?");
            peopleLeaveRrdBean.setApproverNo("?");
            peopleLeaveRrdBean.setHisAnnotation("?");
            peopleLeaveRrdBean.setResult("?");
            peopleLeaveRrdBean.setCurrentApproverNo("?");
            peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
            String toJson = new Gson().toJson(peopleLeaveRrdBean);
            String s="Api_Get_MyApproveForPeoHis "+toJson;
            L.e(TAG+"Api_Get_MyApproveForPeoHis",s);
            HttpManager.getInstance().requestNewResultForm(getTempIP(), s, PeopleApproveFinishBean.class, new HttpManager.ResultNewCallback<PeopleApproveFinishBean>() {
                @Override
                public void onSuccess(String json, final PeopleApproveFinishBean peopleApproveFinishBean) throws Exception {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (peopleApproveFinishBean != null){
                                if (peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0).getBCancel().equals("0")){
                                    PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
                                    peopleInfoBean.setNo(peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0).getNo());
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
                                                peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0).setUnit(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getUnit());
                                                peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0).setDepartment(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getDepartment());
                                                String hisAnnotation = peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0).getHisAnnotation();
                                                String str = ";";
                                                fenNum = StringUtils.method_5(hisAnnotation, str);
                                                L.e("##**分号数目_已审批:"+fenNum);
                                                PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean showBean = peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0);
                                                setEntity(showBean.getActualInTime(),showBean.getActualOutTime(),showBean.getApproverName(),showBean.getApproverNo(),
                                                        showBean.getContent(),showBean.getCount(),showBean.getCurrentApproverName(),showBean.getCurrentApproverNo(),
                                                        showBean.getDestination(),showBean.getEnclosure(),showBean.getHisAnnotation(),showBean.getInTime(),showBean.getJourneyDays(),
                                                        showBean.getModifyTime(),showBean.getName(),showBean.getNo(),showBean.getNoIndex(),showBean.getOutStatus(),
                                                        showBean.getOutTime(),showBean.getOutType(),showBean.getProcess(),showBean.getRegisterTime(),showBean.getResult(),
                                                        showBean.getVacationAddr(),showBean.getVacationDays(),showBean.getBCancel(),showBean.getBFillup(),showBean.getBMessage(),
                                                        showBean.getAuthenticationNo(),showBean.getIsAndroid(),showBean.getCurResult(),showBean.getBeginNum(),showBean.getEndNum(),
                                                        showBean.getUnit(),showBean.getDepartment(),showBean.getCurannotation());
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
            PeopleApproveDelayBean.ApiGetMyApproveForPeoBean peopleLeaveRrdBean = new PeopleApproveDelayBean.ApiGetMyApproveForPeoBean();
            peopleLeaveRrdBean.setRegisterTime("?");
            peopleLeaveRrdBean.setOutTime("?");
            peopleLeaveRrdBean.setInTime("?");
            peopleLeaveRrdBean.setContent("?");
            peopleLeaveRrdBean.setActualOutTime("?");
            peopleLeaveRrdBean.setActualInTime("?");
            peopleLeaveRrdBean.setModifyTime("?");
            peopleLeaveRrdBean.setProcess("?");
            peopleLeaveRrdBean.setBCancel("?");
            peopleLeaveRrdBean.setBFillup("?");
            peopleLeaveRrdBean.setIsAndroid("1");
            peopleLeaveRrdBean.setNoIndex(noindex);
            peopleLeaveRrdBean.setNo("?");
            peopleLeaveRrdBean.setOutType("?");
            peopleLeaveRrdBean.setAuthenticationNo(authenticationNo);
            peopleLeaveRrdBean.setDestination("?");
            peopleLeaveRrdBean.setApproverNo("?");
            peopleLeaveRrdBean.setHisAnnotation("?");
            peopleLeaveRrdBean.setResult("?");
            peopleLeaveRrdBean.setCurrentApproverNo("?");
            peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
            String toJson = new Gson().toJson(peopleLeaveRrdBean);
            String s="Api_Get_MyApproveForPeo "+toJson;
            L.e(TAG+"Api_Get_MyApproveForPeo",s);
            HttpManager.getInstance().requestNewResultForm(getTempIP(), s, PeopleApproveDelayBean.class, new HttpManager.ResultNewCallback<PeopleApproveDelayBean>() {
                @Override
                public void onSuccess(String json, final PeopleApproveDelayBean peopleApproveDelayBean) throws Exception {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (peopleApproveDelayBean != null){
                                if (peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0).getBCancel().equals("0")){
                                    PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
                                    peopleInfoBean.setNo(peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0).getNo());
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
                                                peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0).setUnit(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getUnit());
                                                peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0).setDepartment(peopleInfoBean.getApi_Get_PeopleInfoSim().get(0).getDepartment());
                                                String hisAnnotation = peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0).getHisAnnotation();
                                                String str = ";";
                                                fenNum = StringUtils.method_5(hisAnnotation, str);
                                                L.e("##**分号数目_未审批:"+fenNum);
                                                PeopleApproveDelayBean.ApiGetMyApproveForPeoBean showBean = peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0);
                                                setEntity(showBean.getActualInTime(),showBean.getActualOutTime(),showBean.getApproverName(),showBean.getApproverNo(),
                                                        showBean.getContent(),showBean.getCount(),showBean.getCurrentApproverName(),showBean.getCurrentApproverNo(),
                                                        showBean.getDestination(),showBean.getEnclosure(),showBean.getHisAnnotation(),showBean.getInTime(),showBean.getJourneyDays(),
                                                        showBean.getModifyTime(),showBean.getName(),showBean.getNo(),showBean.getNoIndex(),showBean.getOutStatus(),
                                                        showBean.getOutTime(),showBean.getOutType(),showBean.getProcess(),showBean.getRegisterTime(),showBean.getResult(),
                                                        showBean.getVacationAddr(),showBean.getVacationDays(),showBean.getBCancel(),showBean.getBFillup(),showBean.getBMessage(),
                                                        showBean.getAuthenticationNo(),showBean.getIsAndroid(),showBean.getCurResult(),showBean.getBeginNum(),showBean.getEndNum(),
                                                        showBean.getUnit(),showBean.getDepartment(),showBean.getCurannotation());
                                                setGroup(getGroupList());
                                                setPb(false);
                                                setButtonllEnable(true);
                                                notifyDataSetChanged();
                /*String ActualInTime,String ActualOutTime,String ApproverName,String ApproverNo,String Content,String Count,String CurrentApproverName,String CurrentApproverNo,
                        String Destination,String Enclosure,String HisAnnotation,String InTime,String JourneyDays,String ModifyTime,String Name,String No,
                        String NoIndex,String OutStatus,String OutTime,String OutType,String Process,String RegisterTime,String Result,String VacationAddr,
                        String VacationDays,String bCancel,String bFillup,String bMessage,String AuthenticationNo,String IsAndroid,String CurResult,String BeginNum,
                        String EndNum,String Unit,String Department,String Curannotation*/
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
        PeopleApproveBean.ApiAppovePeopleLeaveBean peopleLeaveRrdBean = new PeopleApproveBean.ApiAppovePeopleLeaveBean();
        peopleLeaveRrdBean.setNoIndex(noindex);
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
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
        String realValue1 = getDisplayValueByKey("您的批注:").getRealValue();
        String realValue = realValue1.isEmpty()?"无批注":realValue1;
        peopleLeaveRrdBean.setCurannotation(realValue);
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleLeaveRrdBean);
        final String s1 = "Api_Appove_PeopleLeave " + json;
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
        HttpManager.getInstance().requestNewResultForm(baseUrl, s1, PeopleApproveBean.class, new HttpManager.ResultNewCallback<PeopleApproveBean>() {

            @Override
            public void onSuccess(String json, PeopleApproveBean peopleApproveBean) throws Exception {
//                if (peopleApproveBean.getApi_Appove_PeopleLeave().get(0) == null){
//                    show("审批成功");
//                    getActivity().finish();
//                }
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
    private String Enclosure;
    private String HisAnnotation;
    private String InTime;
    private String JourneyDays;
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
    private String VacationAddr;
    private String VacationDays;
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

    public void setEntity(String ActualInTime,String ActualOutTime,String ApproverName,String ApproverNo,String Content,String Count,String CurrentApproverName,String CurrentApproverNo,
                          String Destination,String Enclosure,String HisAnnotation,String InTime,String JourneyDays,String ModifyTime,String Name,String No,
                          String NoIndex,String OutStatus,String OutTime,String OutType,String Process,String RegisterTime,String Result,String VacationAddr,
                          String VacationDays,String bCancel,String bFillup,String bMessage,String AuthenticationNo,String IsAndroid,String CurResult,String BeginNum,
                          String EndNum,String Unit,String Department,String Curannotation) {
        this.ActualInTime = ActualInTime;
        this.ActualOutTime = ActualOutTime;
        this.ApproverName = ApproverName;
        this.ApproverNo = ApproverNo;
        this.Content = Content;
        this.Count = Count;
        this.CurrentApproverName = CurrentApproverName;
        this.CurrentApproverNo = CurrentApproverNo;
        this.Destination = Destination;
        this.Enclosure = Enclosure;
        this.HisAnnotation = HisAnnotation;
        this.InTime = InTime;
        this.JourneyDays = JourneyDays;
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
        this.VacationAddr = VacationAddr;
        this.VacationDays = VacationDays;
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
    }
}
