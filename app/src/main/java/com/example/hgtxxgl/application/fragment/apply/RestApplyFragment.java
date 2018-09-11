package com.example.hgtxxgl.application.fragment.apply;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.example.hgtxxgl.application.bean.PeopleApplyBean;
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
import java.util.regex.Pattern;

import okhttp3.Request;

//申请
public class RestApplyFragment extends CommonFragment {

    private final static String TAG = "RestApplyFragment";
    private String name;
    private String unit;
    private String department;

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
    public List<Group> getGroupList() {
        if (!ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().isEmpty()){
            name = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName();
            unit = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getUnit();
            department = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getDepartment();
        }
        List<CommonFragment.Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
        baseHolder.add(new HandInputGroup.Holder("申请人",false,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属单位",false,false,unit,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属部门",false,false,department,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("申请类型",false,false,"休假申请",HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("假期天数",true,false,"/请输入假期天数",HandInputGroup.VALUE_TYPE.DOUBLE));
        baseHolder.add(new HandInputGroup.Holder("路途天数",true,false,"/请输入路途天数",HandInputGroup.VALUE_TYPE.DOUBLE));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("去向",true,false,"/请输入去向",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("事由",true,false,"/请输入休假申请事由",HandInputGroup.VALUE_TYPE.BIG_EDIT));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        groups.add(0,new CommonFragment.Group("基本信息", null,true,null,baseHolder));
        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("休假申请");
        toolbar.setTitleSize(16);
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return new String[]{"提 交"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBottomButtonsClick(final String title, final List<CommonFragment.Group> groups) {
        if (title.equals("提 交")){
            final String vacationDays = getDisplayValueByKey("假期天数").getRealValue();
            final String journeyDays = getDisplayValueByKey("路途天数").getRealValue();
            final boolean isNumVa = isInteger(vacationDays);
            final boolean isNumJo = isInteger(journeyDays);
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
            builder.setMessage("是否确认提交?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String over = isOver(groups);
                    if (over != null){
                        show(over+"不能为空!");
                        setButtonllEnable(true);
                    } else if (!isNumVa){
                        show("假期天数"+"输入错误!");
                        setButtonllEnable(true);
                    } else if (!isNumJo){
                        show("路途天数"+"输入错误!");
                        setButtonllEnable(true);
                    } else {
                        //申请人ID
                        String realValueNO = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
                        String name = getDisplayValueByKey("申请人").getRealValue();
                        String unit = getDisplayValueByKey("所属单位").getRealValue();
                        String department = getDisplayValueByKey("所属部门").getRealValue();
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
                        peopleApplyBean.setVacationDays(vacationDays);
                        peopleApplyBean.setJourneyDays(journeyDays);
//                        peopleLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        peopleApplyBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
                        peopleApplyBean.setIsAndroid("1");
                        peopleApplyBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
                        String json = new Gson().toJson(peopleApplyBean);
                        String s1 = "Api_Apply_PeopleLeave " + json;
                        Log.e(TAG,s1);
                        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleApplyBean.class, new HttpManager.ResultNewCallback<PeopleApplyBean>() {

                            @Override
                            public void onSuccess(String json, PeopleApplyBean peopleApplyBean) throws Exception {
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

    @Override
    public void onClickItemContentSetter(final HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.DATE) {
            showDateTimePicker(holder,true);
        } else if (holder.getKey().equals("是否取消请假")){
            showSelector(holder,new String[]{"是","否"});
        } /*else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        }*/
    }

    @Override
    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {
        CommonFragment.Group group = getGroup().get(0);
        if (holder.getKey().equals("离队时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("归队时间");
            if (!holder1.getRealValue().isEmpty()){
                String leave = holder.getRealValue();
                String returnt = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"归队时间不能在离队时间之前!");
                }
            }
        }else if (holder.getKey().equals("归队时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("离队时间");
            if (!holder1.getRealValue().isEmpty()){
                String returnt = holder.getRealValue();
                String leave = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"归队时间不能在离队时间之前!");
                }
            }
        }
    }


    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();

    }
}