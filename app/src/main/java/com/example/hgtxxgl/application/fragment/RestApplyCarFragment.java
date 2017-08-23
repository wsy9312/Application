package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RestApplyCarFragment extends CommonFragment{

    private CarLeaveEntity entity;
    private String name;
    private String no;
    private final static String TAG = "RestApplyCarFragment";

    public RestApplyCarFragment() {
    }

    public static RestApplyCarFragment newInstance() {
        RestApplyCarFragment restApplyCarFragment = new RestApplyCarFragment();
        return restApplyCarFragment;
    }

    public static RestApplyCarFragment newInstance(Bundle bundle) {
        RestApplyCarFragment fragment = new RestApplyCarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        loadData();
    }

    private void loadData() {
        if (getArguments() != null) {
            String NO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
            CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
            CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean =
                    new CarLeaveEntity.CarLeaveRrdBean
                            (NO,"?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?");
            List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
            beanList.add(carLeaveRrdBean);
            carLeaveEntity.setCarLeaveRrd(beanList);
            String json = new Gson().toJson(carLeaveEntity);
            String s1 = "apply " + json;
            HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
                @Override
                public void onSuccess(String json, CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                    if (carLeaveEntity1 != null){
                        entity = carLeaveEntity1;
                        setGroup(getGroupList());
                        setPb(false);
                        setButtonllEnable(true);
                        notifyDataSetChanged();
                    }else{
                        show("车辆外出信息实体转换异常");
                    }
                }

                @Override
                public void onFailure(final String msg) {
                    show(msg);
                }

                @Override
                public void onResponse(String response) {

                }
            });

        }
    }

    public void show(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public List<CommonFragment.Group> getGroupList() {
        if (!ApplicationApp.getPeopleInfoEntity().getPeopleInfo().isEmpty()){
            name = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName();
            no = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
        }

        List<CommonFragment.Group> groups = new ArrayList<>();
        if (entity == null) {
            List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
            baseHolder.add(new HandInputGroup.Holder("申请人",true,false,no,HandInputGroup.VALUE_TYPE.TEXTFILED));
            baseHolder.add(new HandInputGroup.Holder("申请车辆号牌",true,false,"",HandInputGroup.VALUE_TYPE.TEXTFILED));
            baseHolder.add(new HandInputGroup.Holder("预计外出时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
            baseHolder.add(new HandInputGroup.Holder("预计归来时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
            baseHolder.add(new HandInputGroup.Holder("外出原因",false,false,"",HandInputGroup.VALUE_TYPE.TEXTFILED));
            baseHolder.add(new HandInputGroup.Holder("是否取消请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
            baseHolder.add(new HandInputGroup.Holder("是否后补请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
            groups.add(0,new CommonFragment.Group("基本信息", null,true,null,baseHolder));
        } else {
            CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = entity.getCarLeaveRrd().get(0);
            String outTime = carLeaveRrdBean.getOutTime();
            String inTime = carLeaveRrdBean.getInTime();
            String content = carLeaveRrdBean.getContent();
            String carNo = carLeaveRrdBean.getCarNo();
            String bCancel = carLeaveRrdBean.getbCancel();
            String bFillup = carLeaveRrdBean.getbFillup();
            List<HandInputGroup.Holder> subHolder1 = new ArrayList<>();
            subHolder1.add(new HandInputGroup.Holder("申请人", true, false, name, HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder1.add(new HandInputGroup.Holder("申请车辆号牌", true, false, carNo, HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder1.add(new HandInputGroup.Holder("预计外出时间", true, false, outTime, HandInputGroup.VALUE_TYPE.DATE));
            subHolder1.add(new HandInputGroup.Holder("预计归来时间", true, false, inTime, HandInputGroup.VALUE_TYPE.DATE));
            subHolder1.add(new HandInputGroup.Holder("外出原因", false, false, content, HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder1.add(new HandInputGroup.Holder("是否取消请假", false, false, bCancel.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
            subHolder1.add(new HandInputGroup.Holder("是否后补请假", false, false, bFillup.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
            groups.add(0, new CommonFragment.Group("基本信息", null, true, null, subHolder1));
        }

        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {

    }



    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"提交"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBottomButtonsClick(final String title, final List<CommonFragment.Group> groups) {
        setButtonllEnable(false);
        if (title.equals("提交")){
            String over = isOver(groups);
            if (over != null){
                ToastUtil.showToast(getContext(),"请填写" + over);
                setButtonllEnable(true);
            }else {
                ToastUtil.showToast(getContext(),"提交");
                List<HandInputGroup.Holder> holders = groups.get(0).getHolders();
                String realValueNO = holders.get(0).getRealValue();
                String realValuecardNo = holders.get(1).getRealValue();
                String realValueoutTime = holders.get(2).getRealValue()+":00";
                String realValueinTime = holders.get(3).getRealValue()+":00";
                String realValuecontent = holders.get(4).getRealValue();
                String realValueCancel = holders.get(5).getRealValue();
                String realValueFillup = holders.get(6).getRealValue();
                ToastUtil.showToast(getContext(),realValueNO+" "+realValuecardNo+" "+realValueoutTime+" "+realValueinTime+" "+realValuecontent+" "+realValueCancel+" "+realValueFillup);
                CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
                CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
                carLeaveRrdBean.setNo(realValueNO);
                carLeaveRrdBean.setCarNo(realValuecardNo);
                carLeaveRrdBean.setOutTime(realValueoutTime);
                carLeaveRrdBean.setInTime(realValueinTime);
                carLeaveRrdBean.setContent(realValuecontent);
                carLeaveRrdBean.setbCancel(realValueCancel.equals("否")?"0":"1");
                carLeaveRrdBean.setbFillup(realValueFillup.equals("否")?"0":"1");
                List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                beanList.add(carLeaveRrdBean);
                carLeaveEntity.setCarLeaveRrd(beanList);
                String json = new Gson().toJson(carLeaveEntity);
                String s1 = "apply " + json;
                Log.e(TAG,"车辆外出请求:"+s1);
                applyStart(CommonValues.BASE_URL,s1);
            }
        }
//
    }

    private void applyStart(String baseUrl, String s1) {
        HttpManager.getInstance().requestResultForm(baseUrl, s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity) throws InterruptedException {
                show(json);
            }

            @Override
            public void onFailure(final String msg) {
                show("onFailure:"+msg);
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


    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否取消请假")){
            showSelector(holder,new String[]{"是","否"});
        } else if (holder.getKey().equals("是否后补请假")){
            showSelector(holder,new String[]{"是","否"});
        }
    }


}
