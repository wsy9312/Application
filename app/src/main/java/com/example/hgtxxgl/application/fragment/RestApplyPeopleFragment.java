package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
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
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        loadData();
    }

    private void loadData() {
        if (getArguments() != null) {
            String NO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
            PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
            PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean =
                    new PeopleLeaveEntity.PeopleLeaveRrdBean
                            (NO,"?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?","?");
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
                List<HandInputGroup.Holder> holders = groups.get(0).getHolders();
                String realValueNO = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo();
                String realValueoutTime = holders.get(1).getRealValue()+":00";
                String realValueinTime = holders.get(2).getRealValue()+":00";
                String realValuecontent = holders.get(3).getRealValue();
                String realValueCancel = holders.get(4).getRealValue();
                String realValueFillup = holders.get(5).getRealValue();
                ToastUtil.showToast(getContext(),realValueNO+" /"+realValueoutTime+" /"+realValueinTime+" /"+realValuecontent+" /"+realValueCancel+" /"+realValueFillup);
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