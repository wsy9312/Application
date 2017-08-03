package com.example.hgtxxgl.application.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.entity.DataListEntity;
import com.example.hgtxxgl.application.rest.AttachmentListEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.DictionaryEntity;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.rest.LeaveDaysOrHoursBean;
import com.example.hgtxxgl.application.rest.RestDetailBean;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by HGTXxgl on 2017/8/2.
 */

public class RestApplyCarFragment extends CommonFragment{
    private String uuid;
    private RestDetailBean bean;
    private LeaveDaysOrHoursBean leaveDaysBean;
    private String barCode;
    private List<DataListEntity> draftRestType, draftRestDayCount, draftAttachmentType;
    private Map<String, HashSet<Uri>> fileUri;
    private String mCompNameCN, mDeptNameCN, mNameCN, mPositionNameCN, mEid;
    private String id;
    private String date;
    private List<AttachmentListEntity> attachList;
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
        loadDraftData();
    }

    private void loadData() {
        if (getArguments() != null) {
            String loginName = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getLoginName();
            String password = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPassword();
            String NO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
            CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
            CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean =
                    new CarLeaveEntity.CarLeaveRrdBean
                            (NO,"?","?","?","?","?","?","?","?","?","?","?","?","?","?");
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

//        mCompNameCN = "";
//        mDeptNameCN = "";
//        mNameCN = "";
//        mPositionNameCN = "";
//        mEid = "";
        List<CommonFragment.Group> groups = new ArrayList<>();
        if (bean == null) {
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
            String bCancel = carLeaveRrdBean.getBCancel();
            String bFillup = carLeaveRrdBean.getBFillup();
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
//            RestDetailBean.RetDataBean.DetailDataBean dataBean = bean.getRetData().getDetailData();
//            List<RestDetailBean.RetDataBean.DetailDataBean.LeaveRequestDetailBean> leaveRequestDetail = dataBean.getLeaveRequestDetail();
//            List<HandInputGroup.Holder> subHolder = new ArrayList<>();
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Company_Name), false, false, mCompNameCN, HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Department), false, false, mDeptNameCN, HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Applicant), false, false, mNameCN, HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Position), false, false, mPositionNameCN, HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Employee_ID), false, false, mEid, HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Lave_Year_Days), false, false, TextUtils.isEmpty(dataBean.getLaveYearDays()) ? "0" : dataBean.getLaveYearDays(), HandInputGroup.VALUE_TYPE.DOUBLE));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Lave_Hours), false, false, TextUtils.isEmpty(dataBean.getLaveHours()) ? "0" : dataBean.getLaveHours(), HandInputGroup.VALUE_TYPE.DOUBLE));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Cumulative_number_of_antenatal_examination), false, false, TextUtils.isEmpty(dataBean.getTotalNumber()) ? "0" : dataBean.getTotalNumber(), HandInputGroup.VALUE_TYPE.DOUBLE));
//            subHolder.add(new HandInputGroup.Holder(this.getString(R.string.Total_Days), false, false, TextUtils.isEmpty(dataBean.getTotalDays()) ? "0" : dataBean.getTotalDays(), HandInputGroup.VALUE_TYPE.DOUBLE));
//            groups.add(0, new CommonFragment.Group(this.getString(R.string.Basic_Information), null, true, null, subHolder));
//            for (int i = 0; i < leaveRequestDetail.size(); i++) {
//                List<HandInputGroup.Holder> subDetail = new ArrayList<>();
//                subDetail.add(new HandInputGroup.Holder(this.getString(R.string.Rest_Leave_Type), true, false, leaveRequestDetail.get(i).getLeaveTpyeName(), HandInputGroup.VALUE_TYPE.SELECT));
//                subDetail.add(new HandInputGroup.Holder(this.getString(R.string.Starting_Time), true, false, leaveRequestDetail.get(i).getStartTime(), HandInputGroup.VALUE_TYPE.DATE));
//                subDetail.add(new HandInputGroup.Holder(this.getString(R.string.Ending_Time), true, false, leaveRequestDetail.get(i).getEndTime(), HandInputGroup.VALUE_TYPE.DATE));
//                subDetail.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Time), true, false, leaveRequestDetail.get(i).getLeaveTime() + "", HandInputGroup.VALUE_TYPE.DOUBLE).setColor(Color.rgb(229,0,17)));
//                subDetail.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Causes), false, false, leaveRequestDetail.get(i).getLeaveCauses() + leaveRequestDetail.get(i).getRemark(), HandInputGroup.VALUE_TYPE.TEXTFILED));
//                if (i == leaveRequestDetail.size() - 1) {
//                    groups.add(new CommonFragment.Group(this.getString(R.string.Details_Information), R.mipmap.add_detail3x, true, null, subDetail).sethasDelete(true));
//                } else {
//                    groups.add(new CommonFragment.Group(this.getString(R.string.Details_Information), null, true, null, subDetail).sethasDelete(true));
//                }
//            }
//            List<HandInputGroup.Holder> subDetailTotoal = new ArrayList<>();
//            groups.add(new CommonFragment.Group(this.getString(R.string.Total), null, false, dataBean.getTotalLeaveTime(), subDetailTotoal));
//            attachList = bean.getRetData().getAttchList();
//            if (attachList != null && attachList.size() > 0) {
//                if (fileUri == null) {
//                    fileUri = new HashMap<>();
//                }
//                for (AttachmentListEntity entity : attachList) {
//                    String groupName = entity.getFileGroupName();
//                    if (!fileUri.containsKey(groupName)) {
//                        List<HandInputGroup.Holder> subAddNoNull = new ArrayList<>();
//                        fileUri.put(groupName, new HashSet<Uri>());
//                        subAddNoNull.add(new HandInputGroup.Holder(this.getString(R.string.Attachment_Type), false, false, entity.getFileGroupName(), HandInputGroup.VALUE_TYPE.SELECT));
//                        subAddNoNull.add(new HandInputGroup.Holder<HashSet<Uri>>(this.getString(R.string.Select_Attachments), false, false, entity.getFileGroupName()+ DataUtil.getCurrentDate() +entity.getFileExtension().substring(entity.getFileExtension().indexOf(".")), HandInputGroup.VALUE_TYPE.FILES_UPLOAD)
//                                .setColor(Color.BLUE).setDrawableRight(-1).setValue(fileUri.get(groupName)).setTag(this).setName(groupName));
//                        groups.add(new CommonFragment.Group(this.getString(R.string.Attachment_Info), null, false, null, subAddNoNull));
//                    }
//                    loadRemoteFiles(entity);
//                }
//            } else {
//                List<HandInputGroup.Holder> subAddNull = new ArrayList<>();
//                subAddNull.add(new HandInputGroup.Holder(this.getString(R.string.Attachment_Type), false, false, "/" + this.getString(R.string.Please_Select), HandInputGroup.VALUE_TYPE.SELECT));
//                if (fileUri == null) {
//                    fileUri = new HashMap<>();
//                    fileUri.put("", new HashSet<Uri>());
//                }
//                subAddNull.add(new HandInputGroup.Holder(this.getString(R.string.Select_Attachments), false, false, "/" + this.getString(R.string.Please_Select), HandInputGroup.VALUE_TYPE.FILES_UPLOAD).setValue(fileUri.get("")));
//                groups.add(new CommonFragment.Group(this.getString(R.string.Attachment_Info), null, false, null, subAddNull));
//            }
//        }
        return groups;
    }

//    private void loadRemoteFiles(final AttachmentListEntity entity) {
//        Map<String, Object> param = CommonValues.getCommonParams(getActivity());
//        param.put("attachmentId", entity.getId());
//        HttpManager.getInstance().requestResultForm(CommonValues.CLICK_LOOK_DATA, param, AttachmentSingleEntity.class, new HttpManager.ResultCallback<AttachmentSingleEntity>() {
//            @Override
//            public void onSuccess(final String content, final AttachmentSingleEntity attachmentListEntity) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (attachmentListEntity != null && attachmentListEntity.getRetData() != null) {
//                            if (attachmentListEntity.getCode().equals("100")) {
//                                String attachment = attachmentListEntity.getRetData().getAttachment();
//                                String attachType = attachmentListEntity.getRetData().getType();
//                                String substring = null;
//                                substring = DataUtil.getExtensionFromIME(attachType);
//                                if (substring == null) {
//                                    Toast.makeText(getContext(), "未知的文件类型！", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                File file = DataUtil.base64ToFileWithName(attachment, substring, entity.getFileName());
//                                Uri uri = Uri.fromFile(file);
//                                fileUri.get(entity.getFileGroupName()).add(uri);
//                                entity.setLocalFileUri(uri);
//                                notifyDataSetChanged();
//                            }
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//            }
//        });
//    }

//   /* public void setToolbar(HandToolbar toolbar) {
//        toolbar.setTitle("请假申请");
//        toolbar.setTitleSize(20);
//        toolbar.setDisplayHomeAsUpEnabled(true, getActivity());
//    }*/

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
                carLeaveRrdBean.setBCancel(realValueCancel.equals("否")?"0":"1");
                carLeaveRrdBean.setBFillup(realValueFillup.equals("否")?"0":"1");
                List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                beanList.add(carLeaveRrdBean);
                carLeaveEntity.setCarLeaveRrd(beanList);
                String json = new Gson().toJson(carLeaveEntity);
                String s1 = "apply " + json;
                Log.e(TAG,"车辆外出请求:"+s1);
                applyStart(CommonValues.BASE_URL,s1);
            }
        }
//        if (!title.equals("保存") && over != null){
//            ToastUtil.showToast(getContext(),this.getString(R.string.Please_Fill) + over);
//            setButtonllEnable(true);
//        }else {
//            List<CommonFragment.Group> groupsByTitle = getGroupsByTitle(this.getString(R.string.Details_Information));
//            for (CommonFragment.Group group : groupsByTitle) {
//                int getday = getday(group.getHolders().get(1).getRealValue().isEmpty() ? "0000-00-00 00:00" : group.getHolders().get(1).getRealValue(), group.getHolders().get(2).getRealValue().isEmpty() ? "0000-00-00 00:00" : group.getHolders().get(2).getRealValue());
//                if (group.getHolders().get(0).getRealValue().equals("调休")){
//                    if(Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty()?"0":group.getHolders().get(3).getRealValue())%4 != 0){
//                        ToastUtil.showToast(getContext(),"调休时长必须是4的整数倍！");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    if (getday< Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty()?"0":group.getHolders().get(3).getRealValue())){
//                        ToastUtil.showToast(getContext(),"调休时长数不能超过所选时间！");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                }else {
//                    if(Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty()?"0":group.getHolders().get(3).getRealValue())*4%2 != 0){
//                        ToastUtil.showToast(getContext(),"请假时长必须是0.5的整数倍！");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    if (getday< Double.parseDouble(group.getHolders().get(3).getRealValue().isEmpty()?"0":group.getHolders().get(3).getRealValue())*8){
//                        ToastUtil.showToast(getContext(),"请假时长数不能超过所选时间！");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                }
//
//            }
//            Map<String, Object> body = CommonValues.getCommonParams(getActivity());
//            Map<String, Object> mainData = new HashMap<String, Object>();
//            ArrayList<Map> details = new ArrayList<>();
////            LoginEntity.RetDataBean.UserInfoBean userInfo = GeelyApp.getLoginEntity().getRetData().getUserInfo();
//            if (bean == null) {
//                uuid = UUID.randomUUID().toString();
//                barCode = "";
//                date = DescripUtil.formatDate(new Date());
//                id = "0";
//                body.put("SN", "");
//
//            } else {
//                uuid = bean.getRetData().getDetailData().getIdentifier();
//                barCode = getArguments().getString("barCode");
//                id = bean.getRetData().getDetailData().getId();
//                date = bean.getRetData().getDetailData().getCreateTime();
//                mainData.put("Id",id);//1
//                mainData.put("BarCode", barCode);//2
//                body.put("SN", getArguments().getString("SN"));
//            }
//
//            mainData.put("Identifier", uuid);//3
//            mainData.put("Company", "");//5
//            mainData.put("Position", "");//6
//            mainData.put("EmployeeID", "");//7
//            mainData.put("Department", "");//8
//            mainData.put("SubmitBy", "");//9
//            mainData.put("UpdateBy","");
//            String rightTitle = getGroupsByTitle(this.getString(R.string.Total)).get(0).getGroupTopRightTitle();
//            if (rightTitle.endsWith("小时")){
//                mainData.put("TotalLeaveTime",rightTitle.substring(0,rightTitle.length()-2));//10
//            }else if (rightTitle.endsWith("天")){
//                mainData.put("TotalLeaveTime",rightTitle.substring(0,rightTitle.length()-1));
//            }else {
//                mainData.put("TotalLeaveTime",rightTitle);//10
//            }
//            mainData.put("Applicant", "");//11
//            mainData.put("UpdateTime", DescripUtil.formatDate(new Date()));//12
//            mainData.put("CreateTime",date);//13
//            String s = date.split(" ")[0];
////            mainData.put("Summary", s.split("-")[0] + s.split("-")[1] + s.split("-")[2] + "-" +
////                    GeelyApp.getLoginEntity().getRetData().getUserInfo().getDeptNameCN() +
////                    GeelyApp.getLoginEntity().getRetData().getUserInfo().getNameCN()
//            mainData.put("Summary", s.split("-")[0] + s.split("-")[1] + s.split("-")[2] + "-" +
//                    "" +
//                    ""
//                    + rightTitle + "请假/调休");//14
//            mainData.put("UpdateBy", "");//15
//            mainData.put("IsSelf",false);//16
//            List<HandInputGroup.Holder> holders = groups.get(0).getHolders();
//            if (holders.get(5).getRealValue().isEmpty() || Double.parseDouble(holders.get(5).getRealValue()) == 0){
//                mainData.put("laveYearDays","0");
//            }else {
//                mainData.put("laveYearDays", holders.get(5).getRealValue());//调休剩余小时数//19
//            }
//            if (holders.get(6).getRealValue().isEmpty() || Double.parseDouble(holders.get(6).getRealValue()) == 0){
//                mainData.put("laveHours","0");
//            }else {
//                mainData.put("laveHours", holders.get(6).getRealValue());
//            }
//            if (holders.get(7).getRealValue().isEmpty() || Double.parseDouble(holders.get(7).getRealValue()) == 0){
//                mainData.put("totalNumber","0");
//            }else {
//                mainData.put("totalNumber", holders.get(7).getRealValue());//累积产前检查次数//19
//            }
//            if (holders.get(8).getRealValue().isEmpty() || Double.parseDouble(holders.get(8).getRealValue()) == 0){
//                mainData.put("totalDays","0");
//            }else {
//                mainData.put("totalDays", holders.get(8).getRealValue());//累计事假天数//19
//            }
//            double num = 0;
//            double sum = 0;
//            if (bean != null){
//                for (int i = 1; i < groups.size() - 1-1; i++) {
//                    Map<String, Object> detail = new HashMap<>();
//                    if (groups.get(i).getTitle().equals(this.getString(R.string.Details_Information))){
//                        detail.put("LeaveTpyeName",groups.get(i).getHolders().get(0).getRealValue());
//                    }
//                    if (bean.getRetData().getDetailData().getLeaveRequestDetail().isEmpty()){
//                        detail.put("Id","0");
//                    }else{
//                        if(i < bean.getRetData().getDetailData().getLeaveRequestDetail().size() + 1){
//                            detail.put("Id",bean.getRetData().getDetailData().getLeaveRequestDetail().get(i-1).getId());//1
//                        }else {
//                            detail.put("Id","0");
//                        }
//                    }
//                    detail.put("WorkflowIdentifier", uuid);//2
//                    if (groups.get(i).getTitle().endsWith(this.getString(R.string.Details_Information))){
//                        String type = groups.get(i).getHolders().get(0).getRealValue();
//                        if (type.equals("调休")){
//                            sum += Double.parseDouble(groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());
//                        }else if (type.equals("年假请假")){
//                            num += Double.parseDouble(groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());
//                        }
//                        detail.put("LeaveType", DataUtil.getDicodeByDescr(draftRestType, type));//请假类别//3
//                        String fromDate = groups.get(i).getHolders().get(1).getRealValue();
//                        detail.put("StartTime", fromDate);//开始时间//4
//                        String toDate = groups.get(i).getHolders().get(2).getRealValue();
//                        detail.put("EndTime", toDate);//结束时间//5
//                        detail.put("LeaveTime", groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());//请假时数//6
//                        detail.put("LeaveCauses", groups.get(i).getHolders().get(4).getRealValue().equals("") ? "" : groups.get(i).getHolders().get(4).getRealValue());//请假原因//7
//                    }
//                    detail.put("Remark","");//8
//                    detail.put("IsPostToPs", false);//9
//                    if (sum > Double.parseDouble(getDisplayValueByKey(this.getString(R.string.Lave_Hours)).getRealValue().isEmpty()?"0":getDisplayValueByKey(this.getString(R.string.Lave_Hours)).getRealValue())){
//                        ToastUtil.showToast(getContext(),"调休时间不能大于调休剩余小时数");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    if (num > Double.parseDouble(getDisplayValueByKey(this.getString(R.string.Lave_Year_Days)).getRealValue().isEmpty()?"0":getDisplayValueByKey(this.getString(R.string.Lave_Year_Days)).getRealValue())){
//                        ToastUtil.showToast(getContext(),"请年假时间不能大于剩余年假天数");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    details.add(detail);
//                }
//            }else{
//                for (int i = 1; i < groups.size() - 2; i++) {
//                    Map<String, Object> detail = new HashMap<>();
//                    if (groups.get(i).getTitle().equals(this.getString(R.string.Details_Information))){
//                        detail.put("LeaveTpyeName",groups.get(i).getHolders().get(0).getRealValue());
//                    }
//                    detail.put("Id","0");
//                    detail.put("WorkflowIdentifier", uuid);//2
//                    if (groups.get(i).getTitle().endsWith(this.getString(R.string.Details_Information))){
//                        String type = groups.get(i).getHolders().get(0).getRealValue();
//                        if (type.equals("调休")){
//                            sum += Double.parseDouble(groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());
//                        }else if (type.equals("年假请假")){
//                            num += Double.parseDouble(groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());
//                        }
//                        detail.put("LeaveType", DataUtil.getDicodeByDescr(draftRestType, type));//请假类别//3
//                        String fromDate = groups.get(i).getHolders().get(1).getRealValue();
//                        detail.put("StartTime", fromDate);//开始时间//4
//                        String toDate = groups.get(i).getHolders().get(2).getRealValue();
//                        detail.put("EndTime", toDate);//结束时间//5
//                        detail.put("LeaveTime", groups.get(i).getHolders().get(3).getRealValue().equals("") ? "0" : groups.get(i).getHolders().get(3).getRealValue());//请假时数//6
//                        detail.put("LeaveCauses", groups.get(i).getHolders().get(4).getRealValue().equals("") ? "" : groups.get(i).getHolders().get(4).getRealValue());//请假原因//7
//                    }
//                    detail.put("Remark","");//8
//                    detail.put("IsPostToPs", false);//9
//                    if (sum > Double.parseDouble(getDisplayValueByKey(this.getString(R.string.Lave_Hours)).getRealValue().isEmpty()?"0":getDisplayValueByKey(this.getString(R.string.Lave_Hours)).getRealValue())){
//                        ToastUtil.showToast(getContext(),"调休时间不能大于调休剩余小时数");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    if (num > Double.parseDouble(getDisplayValueByKey(this.getString(R.string.Lave_Year_Days)).getRealValue().isEmpty()?"0":getDisplayValueByKey(this.getString(R.string.Lave_Year_Days)).getRealValue())){
//                        ToastUtil.showToast(getContext(),"请年假时间不能大于剩余年假天数");
//                        setButtonllEnable(true);
//                        return;
//                    }
//                    details.add(detail);
//                }
//            }
//            body.put("mainData", new Gson().toJson(mainData));
//            body.put("detailData", new Gson().toJson(details));
//            body.put("transmissionId", "");
//            String groupName = getDisplayValueByKey(this.getString(R.string.Attachment_Type)).getRealValue();
//            if (!groupName.equals("")) {
//                if(fileUri.get(groupName) == null){
//                    uploadFileAndData(fileUri.get(""), title, body, uuid, CommonValues.WORKFLOW_REST, groupName, DataUtil.getDicIdByDescr(draftAttachmentType, groupName), CommonValues.REQ_REST_APPLY);
//                }else {
//                    uploadFileAndData(fileUri.get(groupName), title, body, uuid, CommonValues.WORKFLOW_REST, groupName, DataUtil.getDicIdByDescr(draftAttachmentType, groupName), CommonValues.REQ_REST_APPLY);
//                    for (int i = 0; i < attachList.size(); i++) {
//                        for (Uri uri : fileUri.get(groupName)){
//                            String path = FileUtils.getPath(getActivity(), uri);
//                            String name = path.substring(path.lastIndexOf("/") + 1);
//                            String fileName = name.substring(0, name.lastIndexOf("."));
//                            if (fileName.equals(attachList.get(i).getFileName())){
////                                onDeleteRemoteFile(attachList.get(i).getLocalFileUri());
//                            }
//                        }
//                    }
//                }
//            }else{
//                applySaveOrStart(CommonValues.REQ_REST_APPLY, body, title);
//            }
//        }
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

    private void loadDraftData() {
        HttpManager manager = HttpManager.getInstance();
        Map<String, Object> params = CommonValues.getCommonParams(getActivity());
        //请假类别
        params.put("code", "2124");
        manager.requestResultForm(CommonValues.GET_DICTIONARY_LIST, params, DictionaryEntity.class, new HttpManager.ResultCallback<DictionaryEntity>() {
            @Override
            public void onSuccess(String content, final DictionaryEntity selectDraftListEntity) {
                draftRestType = selectDraftListEntity.getRetData().get(0).getDataList();
            }
            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onResponse(String response) {

            }
        });
        //请假天数
        params.put("code", "3176");
        manager.requestResultForm(CommonValues.GET_DICTIONARY_LIST, params, DictionaryEntity.class, new HttpManager.ResultCallback<DictionaryEntity>() {
            @Override
            public void onSuccess(String content, final DictionaryEntity selectDraftListEntity) {
                draftRestDayCount = selectDraftListEntity.getRetData().get(0).getDataList();
            }
            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onResponse(String response) {

            }
        });
        //附件类型
        params.put("code", "2169");
        manager.requestResultForm(CommonValues.GET_DICTIONARY_LIST, params, DictionaryEntity.class, new HttpManager.ResultCallback<DictionaryEntity>() {
            @Override
            public void onSuccess(String content, final DictionaryEntity selectDraftListEntity) {
                draftAttachmentType = selectDraftListEntity.getRetData().get(0).getDataList();
            }
            @Override
            public void onFailure(String content) {
            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

//    @Override
//    public void onOneItemBottomDrawableResClick(int index) {
//        List<HandInputGroup.Holder> subHolder1 = new ArrayList<>();
//        subHolder1.add(new HandInputGroup.Holder(this.getString(R.string.Rest_Leave_Type), true, false, "/" + this.getString(R.string.Please_Select), HandInputGroup.VALUE_TYPE.SELECT));
//        subHolder1.add(new HandInputGroup.Holder(this.getString(R.string.Starting_Time), true, false, "/" + this.getString(R.string.Please_Select), HandInputGroup.VALUE_TYPE.DATE));
//        subHolder1.add(new HandInputGroup.Holder(this.getString(R.string.Ending_Time), true, false, "/" + this.getString(R.string.Please_Select), HandInputGroup.VALUE_TYPE.DATE));
//        subHolder1.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Time),true, false, "/天", HandInputGroup.VALUE_TYPE.DOUBLE).setColor(Color.rgb(229,0,17)));
//        subHolder1.add(new HandInputGroup.Holder(this.getString(R.string.Leave_Causes), false, false, "/" + this.getString(R.string.Please_Fill), HandInputGroup.VALUE_TYPE.TEXTFILED));
//        addGroup(index + 1, new CommonFragment.Group(this.getString(R.string.Details_Information), R.mipmap.add_detail3x, true, null, subHolder1).sethasDelete(true));
//        getGroup().get(index).setDrawableRes(null);
//        notifyDataSetChanged();
//    }

}
