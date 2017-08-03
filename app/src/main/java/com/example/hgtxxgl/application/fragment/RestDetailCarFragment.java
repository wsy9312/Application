package com.example.hgtxxgl.application.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.rest.AttachmentListEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.rest.RestDetailBean;
import com.example.hgtxxgl.application.rest.UserPhotoEntity;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RestDetailCarFragment extends CommonFragment {


    private String SN;
    private String photo;
    private String TAG = "RestDetailCarFragment";

    public RestDetailCarFragment(){
    }

    private RestDetailBean.RetDataBean entity = null;

    public static RestDetailCarFragment newInstance(Bundle bundle) {
        RestDetailCarFragment fragment = new RestDetailCarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public List<Group> getGroupList() {
        if (entity == null) return new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> list = new ArrayList<>();
        list.add(new HandInputGroup.Holder(this.getString(R.string.Summary), true, false, entity.getDetailData().getSummary(), HandInputGroup.VALUE_TYPE.TEXT));
        List<RestDetailBean.RetDataBean.DetailDataBean.LeaveRequestDetailBean> leaveRequestDetail = entity.getDetailData().getLeaveRequestDetail();
        if (entity.getDetailData().isIsSelf()){
            list.add(new HandInputGroup.Holder("请假/调休天数", false, false, entity.getDetailData().getLeaveDays(), HandInputGroup.VALUE_TYPE.TEXT));
        }else{
            for (RestDetailBean.RetDataBean.DetailDataBean.LeaveRequestDetailBean bean : leaveRequestDetail) {
                if (bean.getLeaveTpyeName().equals("调休")){
                    list.add(new HandInputGroup.Holder(bean.getLeaveTpyeName(), false, false, bean.getLeaveTime() +"小时   " + bean.getStartTime(), HandInputGroup.VALUE_TYPE.TEXT));
                }else {
                    list.add(new HandInputGroup.Holder(bean.getLeaveTpyeName(), false, false, bean.getLeaveTime().split(" ")[0] + "天   " + bean.getStartTime().split(" ")[0], HandInputGroup.VALUE_TYPE.TEXT));
                }
            }
        }
        list.add(new HandInputGroup.Holder(this.getString(R.string.Total2), true, false, entity.getDetailData().getTotalLeaveTime() + "天", HandInputGroup.VALUE_TYPE.TEXT));
        Group group = new Group("流程摘要-摘要内容", null, true, null, list).setrl(true).setv1(entity.getEmpData().getNameCN() + "(" + entity.getEmpData().getNameEN() + ")").setv2(entity.getEmpData().getPositionNameCN() + " " + entity.getEmpData().getEid());
        if (photo != null && !photo.isEmpty()){
            group.setDrawable(photo);
        }
        groups.add(group);
        groups.add(new Group("流程摘要-摘要",null,true,null,null).setValue(entity.getHisData()).setrl(false));
        List<HandInputGroup.Holder> holderList = new ArrayList<>();
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Summary),true,false,entity.getDetailData().getSummary(),HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Company_Name), false, false, entity.getEmpData().getCompNameCN(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Department), false, false, entity.getEmpData().getDeptNameCN(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Applicant), false, false, entity.getEmpData().getNameCN(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Position), false, false, entity.getEmpData().getPositionNameCN(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Employee_ID), false, false, entity.getEmpData().getEid(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Lave_Year_Days), false, false, entity.getDetailData().getLaveYearDays(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Lave_Hours), false, false, entity.getDetailData().getLaveHours(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Cumulative_number_of_antenatal_examination), false, false, entity.getDetailData().getTotalNumber(), HandInputGroup.VALUE_TYPE.TEXT));
        holderList.add(new HandInputGroup.Holder(this.getString(R.string.Total_Days), false, false, entity.getDetailData().getTotalDays(), HandInputGroup.VALUE_TYPE.TEXT));
        groups.add(new Group("详细信息-" + this.getString(R.string.Basic_Information), null, false, null, holderList).setrl(false));
        for (int i = 0; i < leaveRequestDetail.size(); i++) {
            RestDetailBean.RetDataBean.DetailDataBean.LeaveRequestDetailBean bean = leaveRequestDetail.get(i);
            List<HandInputGroup.Holder> holderList1 = new ArrayList<>();
            holderList1.add(new HandInputGroup.Holder(this.getString(R.string.Rest_Leave_Type), false, false, bean.getLeaveTpyeName(), HandInputGroup.VALUE_TYPE.TEXT));
            holderList1.add(new HandInputGroup.Holder(this.getString(R.string.Starting_Time), false, false, bean.getStartTime(), HandInputGroup.VALUE_TYPE.TEXT));
            holderList1.add(new HandInputGroup.Holder(this.getString(R.string.Ending_Time), false, false, bean.getEndTime(), HandInputGroup.VALUE_TYPE.TEXT));
            holderList1.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Time), false, false, bean.getLeaveTime()+ (bean.getLeaveTpyeName().equals("调休")?"小时":"天"), HandInputGroup.VALUE_TYPE.TEXT));
            holderList1.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Causes), false, false, bean.getLeaveCauses(), HandInputGroup.VALUE_TYPE.TEXT));
            groups.add(new Group("详细信息-" + this.getString(R.string.Details_Information), null, false, null, holderList1).setrl(false));
        }
        List<HandInputGroup.Holder> holderTotal = new ArrayList<>();
        groups.add(new Group("详细信息-" + this.getString(R.string.Total), null, false, entity.getDetailData().getTotalLeaveTime(), holderTotal).setrl(false));
        
        List<AttachmentListEntity> attachList = entity.getAttchList();
        if (attachList != null && attachList.size() > 0) {
            for  ( int  i  =   0 ; i  <  attachList.size()  -   1 ; i ++ )   {
                for  ( int  j  =  attachList.size()  -   1 ; j  >  i; j -- )   {
                    if  (attachList.get(j).getFileName().equals(attachList.get(i).getFileName()))   {
                        attachList.remove(j);
                        notifyDataSetChanged();
                    }
                }
            }
            for (AttachmentListEntity entity : attachList) {
                List<HandInputGroup.Holder> temp = new ArrayList<>();
                temp.add(new HandInputGroup.Holder(this.getString(R.string.Attachment_Type), false, false, entity.getFileGroupName(), HandInputGroup.VALUE_TYPE.TEXT));
                temp.add(new HandInputGroup.Holder<HashSet<Uri>>(this.getString(R.string.Select_Attachments), false, false, entity.getFileGroupName()+ DataUtil.getCurrentDate() +entity.getFileExtension().substring(entity.getFileExtension().indexOf(".")), HandInputGroup.VALUE_TYPE.SELECT)
                        .setColor(Color.BLUE).setDrawableRight(-1).setValue(entity));
                groups.add(new Group("详细信息-" + this.getString(R.string.Attachment_Info), null, false, null, temp).setrl(false));
            }
        }
        return groups;
    }

    @Override
    public void onClickItemContentSetter(HandInputGroup.Holder holder) {
        if (holder.getType() == HandInputGroup.VALUE_TYPE.SELECT) {
            if (holder.getKey().equals(this.getString(R.string.Select_Attachments))) {
                AttachmentListEntity entity = (AttachmentListEntity) holder.getValue();
//                lookAttachmentInDetailFragment(entity);
            }
        }
    }

    public void setToolbar(HandToolbar toolbar) {
        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        toolbar.setTitle("请假明细");
    }

    @Override
    public String[] getBottomButtonsTitles() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SN = getArguments().getString("SN");
        loadData();
    }

    public void loadData() {
        Map<String, Object> param = CommonValues.getCommonParams(getActivity());
        param.put("userId",getArguments().getString("userId"));
        param.put("barCode", getArguments().getString("barCode"));
        param.put("workflowType", getArguments().getString("workflowType"));
        HttpManager.getInstance().requestResultForm(CommonValues.REQ_REST_DETAIL, param, RestDetailBean.class, new HttpManager.ResultCallback<RestDetailBean>() {
            @Override
            public void onSuccess(String content, final RestDetailBean restDetailBean) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (restDetailBean != null && restDetailBean.getRetData() != null) {
                            if (restDetailBean.getCode().equals("100")) {
                                setEntity(restDetailBean.getRetData());
                                setGroup(getGroupList());
                                setPb(false);
                                setButtonllEnable(true);
                                setDisplayTabs(true);
                                notifyDataSetChanged();
                                return;

                            }
                        }else {
                            ToastUtil.showToast(getContext(),restDetailBean.getMsg());
                        }
                    }
                });
            }

            @Override
            public void onFailure(String content) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getContext(),"请检查网络");
                    }
                });

            }

            @Override
            public void onResponse(String response) {

            }
        });
        param.put("uid",getArguments().getString("SubmitBy"));
        HttpManager.getInstance().requestResultForm(CommonValues.GET_USER_PHOTO, param, UserPhotoEntity.class, new HttpManager.ResultCallback<UserPhotoEntity>() {
            @Override
            public void onSuccess(String content, final UserPhotoEntity entity) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (entity != null && entity.getRetData() != null) {
                            if (entity.getCode().equals("100")) {
                                photo = entity.getRetData();
                                if (getGroup().size() > 0){
                                    getGroup().get(0).setDrawable(photo);
                                    notifyGroupChanged(0,1);
                                }
                                return;
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(String content) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    public RestDetailBean.RetDataBean getEntity() {
        return entity;
    }

    public void setEntity(RestDetailBean.RetDataBean entity) {
        this.entity = entity;
    }

    public String getSN(){
        return SN;
    }
}
