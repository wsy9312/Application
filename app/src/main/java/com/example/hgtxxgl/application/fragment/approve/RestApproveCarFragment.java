package com.example.hgtxxgl.application.fragment.approve;

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
import com.example.hgtxxgl.application.utils.hand.StringUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
//车辆外出审批
public class RestApproveCarFragment extends CommonFragment {

    private final static String TAG = "RestApproveCarFragment";
    private String noindex;
    private String no;
    private int fenNum;
    private String authenticationNo;

    public RestApproveCarFragment(){

    }

    private CarLeaveEntity.CarLeaveRrdBean entity = null;

    public static RestApproveCarFragment newInstance(Bundle bundle) {
        RestApproveCarFragment fragment = new RestApproveCarFragment();
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
        list.add(new HandInputGroup.Holder("流程内容", true, false, "车辆申请", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        list.add(new HandInputGroup.Holder("审批状态", true, false, process == 0?"审批中":"审批结束", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
        int substring = Integer.parseInt(entity.getResult());
        if (process == 1){
            setButtonsTitles(stringnull);
            switch (substring){
                case 0:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "不同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 1:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "同意", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
                    break;
                case 2:
                    list.add(new HandInputGroup.Holder("审批结果", true, false, "被退回", HandInputGroup.VALUE_TYPE.TEXT).setColor(entity.getApproverNo().contains(authenticationNo)?Color.rgb(0,128,0):Color.rgb(214,16,24)));
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
        holderList.add(new HandInputGroup.Holder("车辆号牌", true, false, entity.getCarNo(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("驾驶员", true, false, entity.getDriverName(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("带车干部", true, false, entity.getLeaderName(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("离队时间", true, false, entity.getOutTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("归队时间", true, false, entity.getInTime(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("事由", true, false, entity.getContent(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("去向", true, false, entity.getDestination(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder("是否后补申请", true, false, entity.getBFillup().equals("0")?"否":"是", HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("基本信息", null, false, null, holderList));

        String split1 = entity.getHisAnnotation();
        String split3 = entity.getApproverName();
        L.e(TAG,split1);
        L.e(TAG,split3);
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
        toolbar.setTitle("车辆审批");
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
        String process = getArguments().getString("process");
        String modifyTime = getArguments().getString("modifyTime");
        String bcancel = getArguments().getString("bcancel");
        String bfillup = getArguments().getString("bfillup");
        noindex = getArguments().getString("noindex");
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setRegisterTime("?");//1
        carLeaveRrdBean.setOutTime("?");//2
        carLeaveRrdBean.setInTime("?");//3
        carLeaveRrdBean.setContent("?");//4
        carLeaveRrdBean.setActualOutTime("?");//5
        carLeaveRrdBean.setActualInTime("?");//6
        carLeaveRrdBean.setModifyTime("?");//7
        carLeaveRrdBean.setProcess("?");//8
        carLeaveRrdBean.setBCancel("?");//9
        carLeaveRrdBean.setBFillup("?");//10
        carLeaveRrdBean.setIsAndroid("1");//11
        carLeaveRrdBean.setNoIndex(noindex);//12
        carLeaveRrdBean.setNo(no);//13
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());//15
        carLeaveRrdBean.setDestination("?");//16
        carLeaveRrdBean.setApproverNo("?");//17
        carLeaveRrdBean.setHisAnnotation("?");//18
        carLeaveRrdBean.setResult("?");//19
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setDriverNo("?");
        carLeaveRrdBean.setLeaderNo("?");
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String toJson = new Gson().toJson(carLeaveEntity);
        String s="get "+toJson;
        L.e(TAG+"RestApproveCarFragment",s);
        HttpManager.getInstance().requestResultForm(getTempIP(), s, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (carLeaveEntity1 != null){
                            if (carLeaveEntity1.getCarLeaveRrd().get(0).getBCancel().equals("0")){

                                PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                                PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                                peopleInfoBean.setNo(no);
                                peopleInfoBean.setUnit("?");
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
                                            carLeaveEntity1.getCarLeaveRrd().get(0).setUnit(peopleInfoEntity.getPeopleInfo().get(0).getUnit());
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
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setNoIndex(noindex);
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
        String realValue1 = getDisplayValueByKey("当前审批人批注:").getRealValue();
        String realValue = realValue1.isEmpty()?"无批注":realValue1;
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setCurannotation(realValue);
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
                    show("审批已完成,正在等待其他人审批");
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
