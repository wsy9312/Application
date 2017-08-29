package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

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
        baseHolder.add(new HandInputGroup.Holder("请假类别",true,false,"人员请假",HandInputGroup.VALUE_TYPE.BUTTONS));
        baseHolder.add(new HandInputGroup.Holder("预计外出时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("预计归来时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("请假原因",false,false,"",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("是否后补请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
        groups.add(0,new Group("基本信息", null,true,null,baseHolder));

        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"提交"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBottomButtonsClick(final String title, final List<CommonFragment.Group> groups) {
        if (title.equals("提交")){
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
                        String realValueType = getDisplayValueByKey("请假类别").getRealValue();
                        //预计外出时间
                        String realValueoutTime = getDisplayValueByKey("预计外出时间").getRealValue()+":00";
                        //预计归来时间
                        String realValueinTime = getDisplayValueByKey("预计归来时间").getRealValue()+":00";
                        //请假原因
                        String realValueContent = getDisplayValueByKey("请假原因").getRealValue();
                        //是否后补请假
                        String realValueFillup = getDisplayValueByKey("是否后补请假").getRealValue();
                        if (realValueType.equals("人员请假")){
                            PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
                            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
                            peopleLeaveRrdBean.setNo(realValueNO);
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
                            Log.e(TAG,"人员请假请假:"+s1);
                            applyStart(0,CommonValues.BASE_URL,s1);
                        }else if(realValueType.equals("车辆外出")){
                            //申请车辆号牌
                            String realValueCardNo = getDisplayValueByKey("车辆号牌").getRealValue();
                            CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
                            CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
                            carLeaveRrdBean.setNo(realValueNO);
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
                            Log.e(TAG,"车辆外出请假:"+s1);
                            applyStart(1,CommonValues.BASE_URL,s1);
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
                    Log.e(TAG,"applyStart()-onSuccess():"+json);
                }

                @Override
                public void onFailure(String msg) {
                    Log.e(TAG,"applyStart()-onFailure():"+msg);
                }

                @Override
                public void onResponse(String response) {
                    if (response.toLowerCase().contains("ok")) {
                        show("提交成功");
                    }else{
                        show("提交失败");
                    }
                }
            });
        }else if (type == 0){
            HttpManager.getInstance().requestResultForm(baseUrl, s1, PeopleLeaveEntity.class, new HttpManager.ResultCallback<PeopleLeaveEntity>() {
                @Override
                public void onSuccess(String json, final PeopleLeaveEntity peopleLeaveEntity) throws InterruptedException {
                    Log.e(TAG,"applyStart()-onSuccess():"+json);
                }

                @Override
                public void onFailure(final String msg) {
                    Log.e(TAG,"applyStart()-onFailure():"+msg);
                }

                @Override
                public void onResponse(String response) {
                    if (response.toLowerCase().contains("ok")) {
                        show("提交成功");
                    }else{
                        show("提交失败");
                    }
                }
            });
        }
    }
    public void show(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.success(getContext(),msg, Toast.LENGTH_SHORT,true).show();
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
        } else if (holder.getKey().equals("请假类别")){
            checkedButton(holder, new OnSelectedResultCallback() {
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
            if (holders.size() == 6){
                holders.add(2,new HandInputGroup.Holder("车辆号牌", true, false, "", HandInputGroup.VALUE_TYPE.TEXTFILED));
            }else if (holders.size() == 7){
                holders.remove(2);
            }
        } else {
            if (holders.size() == 7){
                holders.remove(2);
            }else if (holders.size() == 6){
                holders.add(2,new HandInputGroup.Holder("车辆号牌", true, false, "", HandInputGroup.VALUE_TYPE.TEXTFILED));
            }
        }
        notifyDataSetChanged();
    }

}