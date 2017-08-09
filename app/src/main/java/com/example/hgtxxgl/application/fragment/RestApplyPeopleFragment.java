package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class RestApplyPeopleFragment extends CommonFragment {

    private PeopleLeaveEntity entity;
    private String name;
    private String no;
    private final static String TAG = "RestApplyCarFragment";

    public RestApplyPeopleFragment() {
    }

    public static RestApplyPeopleFragment newInstance() {
        RestApplyPeopleFragment restApplyPeopleFragment = new RestApplyPeopleFragment();
        return restApplyPeopleFragment;
    }

    public static RestApplyPeopleFragment newInstance(Bundle bundle) {
        RestApplyPeopleFragment fragment = new RestApplyPeopleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if (getArguments() != null) {
            String NO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
            PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean =
                    new PeopleLeaveEntity.PeopleLeaveRrdBean
                            (NO,"?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?");
            List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
            beanList.add(peopleLeaveRrdBean);
            peopleLeaveEntity.setPeopleLeaveRrd(beanList);
            String json = new Gson().toJson(peopleLeaveEntity);
            String s1 = "get " + json;
            Log.e(TAG,"loadData()查看个人申请记录状态:"+s1);
            HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
                @Override
                public void onSuccess(String json, PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                    if (peopleLeaveEntity1 != null){
                        entity = peopleLeaveEntity1;
                        setGroup(getGroupList());
                        setPb(false);
                        setButtonllEnable(true);
                        notifyDataSetChanged();
                    }else{
                        show("人员请假信息实体转换异常");
                    }
                }

                @Override
                public void onFailure(final String msg) {
                    Log.e(TAG,"loadData()-onFailure():"+msg);
                }

                @Override
                public void onResponse(String response) {
                    Log.e(TAG,"loadData()-onFailure():"+response);
                }
            });

        }
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
            baseHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED));
            baseHolder.add(new HandInputGroup.Holder("预计外出时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
            baseHolder.add(new HandInputGroup.Holder("预计归来时间",true,false,"",HandInputGroup.VALUE_TYPE.DATE));
            baseHolder.add(new HandInputGroup.Holder("请假原因",false,false,"",HandInputGroup.VALUE_TYPE.TEXTFILED));
            baseHolder.add(new HandInputGroup.Holder("是否取消请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
            baseHolder.add(new HandInputGroup.Holder("是否后补请假",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT));
            groups.add(0,new CommonFragment.Group("基本信息", null,true,null,baseHolder));
        } else {
            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = entity.getPeopleLeaveRrd().get(0);
            String outTime = peopleLeaveRrdBean.getOutTime();
            String inTime = peopleLeaveRrdBean.getInTime();
            String content = peopleLeaveRrdBean.getContent();
            String bCancel = peopleLeaveRrdBean.getBCancel();
            String bFillup = peopleLeaveRrdBean.getBFillup();
            List<HandInputGroup.Holder> subHolder1 = new ArrayList<>();
            subHolder1.add(new HandInputGroup.Holder("申请人", true, false, name, HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder1.add(new HandInputGroup.Holder("预计外出时间", true, false, outTime, HandInputGroup.VALUE_TYPE.DATE));
            subHolder1.add(new HandInputGroup.Holder("预计归来时间", true, false, inTime, HandInputGroup.VALUE_TYPE.DATE));
            subHolder1.add(new HandInputGroup.Holder("请假原因", false, false, content, HandInputGroup.VALUE_TYPE.TEXTFILED));
            subHolder1.add(new HandInputGroup.Holder("是否取消请假", false, false, bCancel.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
            subHolder1.add(new HandInputGroup.Holder("是否后补请假", false, false, bFillup.equals("0") ? "否" : "是", HandInputGroup.VALUE_TYPE.SELECT));
            groups.add(0, new CommonFragment.Group("基本信息", null, true, null, subHolder1));
        }
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
        setButtonllEnable(false);
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
                        List<HandInputGroup.Holder> holders = groups.get(0).getHolders();
                        String realValueNO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
                        String realValueoutTime = holders.get(1).getRealValue()+":00";
                        String realValueinTime = holders.get(2).getRealValue()+":00";
                        String realValuecontent = holders.get(3).getRealValue();
                        String realValueCancel = holders.get(4).getRealValue();
                        String realValueFillup = holders.get(5).getRealValue();
//                        ToastUtil.showToast(getContext(),realValueNO+" /"+realValueoutTime+" /"+realValueinTime+" /"+realValuecontent+" /"+realValueCancel+" /"+realValueFillup);
                        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
                        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
                        peopleLeaveRrdBean.setNo(realValueNO);
                        peopleLeaveRrdBean.setOutTime(realValueoutTime);
                        peopleLeaveRrdBean.setInTime(realValueinTime);
                        peopleLeaveRrdBean.setContent(realValuecontent);
                        peopleLeaveRrdBean.setBCancel(realValueCancel.equals("否")?"0":"1");
                        peopleLeaveRrdBean.setBFillup(realValueFillup.equals("否")?"0":"1");
                        List<PeopleLeaveEntity.PeopleLeaveRrdBean> beanList = new ArrayList<>();
                        beanList.add(peopleLeaveRrdBean);
                        peopleLeaveEntity.setPeopleLeaveRrd(beanList);
                        String json = new Gson().toJson(peopleLeaveEntity);
                        String s1 = "apply " + json;
                        Log.e(TAG,"人员请假请求:"+s1);
                        applyStart(CommonValues.BASE_URL,s1);
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

    private void applyStart(String baseUrl, String s1) {
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
    public void show(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
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

//    @Override
//    public void onHolderTextChanged(int main, int index, HandInputGroup.Holder holder) {
//        final String key = holder.getKey();
//        if (key.equals(this.getString(R.string.Leave_Time))){
//            double sum = 0;
//            List<CommonFragment.Group> groupsByTitle = getGroupsByTitle(this.getString(R.string.Details_Information));
//            for (CommonFragment.Group group : groupsByTitle) {
//                double v = 0;
//                if (group.getHolders().get(0).getRealValue().equals("调休")){
//                    v = Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty() ? "0" : group.getHolders().get(3).getRealValue())/8.0;
//                }else {
//                    v = Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty() ? "0" : group.getHolders().get(3).getRealValue());
//                }
//                sum += v;
//            }
//            getGroupsByTitle(this.getString(R.string.Total)).get(0).setGroupTopRightTitle(sum + "天");
//            notifyGroupChanged(getGroup().size()-2, 1);
//        }
//    }

//    @Override
//    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {
//        int getitemnum = getitemnum(holder);
//        CommonFragment.Group group = getGroup().get(getitemnum);
//        if (holder.getKey().equals(this.getString(R.string.Starting_Time))){
//            HandInputGroup.Holder holder1 = group.getHolders().get(2);
//            if (!holder1.getRealValue().isEmpty()){
//                String starttime = holder.getRealValue();
//                String overtime = holder1.getRealValue();
//                int getday = getday(starttime, overtime);
//                if (getday == -1){
//                    holder.setDispayValue("/" + this.getString(R.string.Please_Select));
//                    ToastUtil.showToast(getContext(),"请正确选择第" + getitemnum+ "条明细信息中的开始、结束时间");
//                }
//            }
//        }else if (holder.getKey().equals(this.getString(R.string.Ending_Time))){
//            HandInputGroup.Holder holder1 = group.getHolders().get(1);
//            if (!holder1.getRealValue().isEmpty()){
//                String starttime = holder1.getRealValue();
//                String overtime = holder.getRealValue();
//                int getday = getday(starttime, overtime);
//                if (getday == -1){
//                    holder.setDispayValue("/" + this.getString(R.string.Please_Select));
//                    ToastUtil.showToast(getContext(),"请正确选择第" + getitemnum+ "条明细信息中的开始、结束时间");
//                }
//            }
//        }
//    }

}