package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RestApplyFragment extends CommonFragment {

    private String name;
    private final static String TAG = "RestApplyCarFragment";

    public RestApplyFragment() {
    }

    public static RestApplyFragment newInstance() {
        RestApplyFragment restApplyFragment = new RestApplyFragment();
        return restApplyFragment;
    }

    public static RestApplyFragment newInstance(Bundle bundle) {
        RestApplyFragment fragment = new RestApplyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public List<CommonFragment.Group> getGroupList() {
        if (!ApplicationApp.getPeopleInfoEntity().getPeopleInfo().isEmpty()){
            name = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName();
        }
        List<CommonFragment.Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
        baseHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
        baseHolder.add(new HandInputGroup.Holder("申请类别",true,false,"人员请假",HandInputGroup.VALUE_TYPE.SELECT));
        baseHolder.add(new HandInputGroup.Holder("申请类型",true,false,"因公申请",HandInputGroup.VALUE_TYPE.SELECT));
        baseHolder.add(new HandInputGroup.Holder("预计外出时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("预计归来时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("申请事由",false,false,"/请填写",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("是否后补请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
        groups.add(0,new Group("基本信息", null,true,null,baseHolder));
        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("申请");
        toolbar.setTitleSize(18);
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"提 交"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBottomButtonsClick(final String title, final List<CommonFragment.Group> groups) {
        if (title.equals("提 交")){
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
                        //申请类型
                        String realValueType = getDisplayValueByKey("申请类别").getRealValue();
                        //预计外出时间
                        String realValueoutTime = getDisplayValueByKey("预计外出时间").getRealValue()+":00";
                        //预计归来时间
                        String realValueinTime = getDisplayValueByKey("预计归来时间").getRealValue()+":00";
                        //请假原因
                        String realValueContent = getDisplayValueByKey("申请事由").getRealValue();
                        //是否后补请假
                        String realValueFillup = getDisplayValueByKey("是否后补请假").getRealValue();
                        //因公或因私外出/请假
                        String realValuetype = getDisplayValueByKey("申请类型").getRealValue();
                        //        String url = CommonValues.BASE_URL;
//                        String url = ApplicationApp.getIP();
//                        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
//                        String tempIP = share.getString("tempIP", "IP address is empty");
                        if (realValueType.equals("人员请假")){
                            PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
                            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
                            peopleLeaveRrdBean.setNo(realValueNO);
                            peopleLeaveRrdBean.setOnduty(realValuetype.equals("因公申请")?"1":"0");
                            peopleLeaveRrdBean.setOutTime(realValueoutTime);
                            peopleLeaveRrdBean.setInTime(realValueinTime);
                            peopleLeaveRrdBean.setContent(realValueContent);
                            peopleLeaveRrdBean.setBFillup(realValueFillup.equals("否")?"0":"1");
                            peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                            peopleLeaveRrdBean.setIsAndroid("1");
                            List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
                            beanList.add(peopleLeaveRrdBean);
                            peopleLeaveEntity.setPeopleLeaveRrd(beanList);
                            String json = new Gson().toJson(peopleLeaveEntity);
                            String s1 = "apply " + json;
                            applyStart(0,getTempIP(),s1);
                        }else if(realValueType.equals("车辆外出")){
                            //申请车辆号牌
                            String realValueCardNo = getDisplayValueByKey("车辆号牌").getRealValue();
                            CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
                            CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
                            carLeaveRrdBean.setNo(realValueNO);
                            carLeaveRrdBean.setOnduty(realValuetype.equals("因公申请")?"1":"0");
                            carLeaveRrdBean.setCarNo(realValueCardNo);
                            carLeaveRrdBean.setOutTime(realValueoutTime);
                            carLeaveRrdBean.setInTime(realValueinTime);
                            carLeaveRrdBean.setContent(realValueContent);
                            carLeaveRrdBean.setbFillup(realValueFillup.equals("否")?"0":"1");
                            carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                            carLeaveRrdBean.setIsAndroid("1");
                            List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                            beanList.add(carLeaveRrdBean);
                            carLeaveEntity.setCarLeaveRrd(beanList);
                            String json = new Gson().toJson(carLeaveEntity);
                            String s1 = "apply " + json;
                            applyStart(1,getTempIP(),s1);
                        }

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

    private void applyStart(int type, String baseUrl, String s1) {
        if (type == 1){
            HttpManager.getInstance().requestResultForm(baseUrl, s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
                @Override
                public void onSuccess(String json, CarLeaveEntity carLeaveEntity) throws InterruptedException {
                }

                @Override
                public void onFailure(String msg) {
                }

                @Override
                public void onResponse(String response) {
                    if (response.toLowerCase().contains("ok")) {
                        show("提交成功");
                        getActivity().finish();
                    }else{
                        show("提交失败,请核实车辆信息无误后提交");
                        getActivity().finish();
                    }
                }
            });
        }else if (type == 0){
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

    public void show(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getContext(),msg);
            }
        });
    }

    private boolean mIsDomestic;

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否取消请假")){
            showSelector(holder,new String[]{"是","否"});
        } else if (holder.getKey().equals("是否后补请假")){
            showSelector(holder,new String[]{"是","否"});
        } else if (holder.getKey().equals("申请类型")){
            showSelector(holder,new String[]{"因公申请","因私申请"});
        }
        if (holder.getKey().equals("申请类别")){
            showSelector(holder, new String[]{"人员请假", "车辆外出"}, new OnSelectedResultCallback() {
                @Override
                public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                    if (holder.getRealValue().equals("人员请假")) {
                        mIsDomestic = true;
                        insertItems(mIsDomestic);
                    } else if (holder.getRealValue().equals("车辆外出")){
                        mIsDomestic = false;
                        insertItems(mIsDomestic);
                    }
                }
            });
        }
    }

    private void insertItems(boolean mIsDomestic) {
        List<HandInputGroup.Holder> holders = getGroup().get(0).getHolders();
        if (mIsDomestic) {
            if (holders.size() == 7){
                holders.add(2,new HandInputGroup.Holder("车辆号牌", true, false, "/请填写", HandInputGroup.VALUE_TYPE.TEXTFILED));
            }else if (holders.size() == 8){
                holders.remove(2);
            }
        } else {
            if (holders.size() == 8){
                holders.remove(2);
            }else if (holders.size() == 7){
                holders.add(2,new HandInputGroup.Holder("车辆号牌", true, false, "/请填写", HandInputGroup.VALUE_TYPE.TEXTFILED));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {
        Group group = getGroup().get(0);
        if (holder.getKey().equals("预计外出时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("预计归来时间");
            if (!holder1.getRealValue().isEmpty()){
                String leave = holder.getRealValue();
                String returnt = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"请正确选择外出、归来时间");
                }
            }
        }else if (holder.getKey().equals("预计归来时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("预计外出时间");
            if (!holder1.getRealValue().isEmpty()){
                String returnt = holder.getRealValue();
                String leave = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"请正确选择外出、归来时间");
                }
            }
        }
    }

}