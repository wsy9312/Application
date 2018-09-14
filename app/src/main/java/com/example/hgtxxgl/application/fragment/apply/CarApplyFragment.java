package com.example.hgtxxgl.application.fragment.apply;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.hgtxxgl.application.bean.LoginInfoBean;
import com.example.hgtxxgl.application.bean.PeopleInfoBean;
import com.example.hgtxxgl.application.entity.CarInfoEntity;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.rest.CommonFragment;
import com.example.hgtxxgl.application.rest.HandInputGroup;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

//申请
public class CarApplyFragment extends CommonFragment {

    private final static String TAG = "CarApplyFragment";
    private String name;
    private String unit;
    private String department;
    private String[] carNoArray;
    private String[] carOwnerNameArray;
    private String ownerNo1 = "";
    private String ownerNo2 = "";
    private String[] arrayName;
    private LoginInfoBean.ApiAddLoginBean loginBean;

    public CarApplyFragment() {
    }

    public static CarApplyFragment newInstance() {
        CarApplyFragment fragment = new CarApplyFragment();
        return fragment;
    }

    public static CarApplyFragment newInstance(Bundle bundle) {
        CarApplyFragment fragment = new CarApplyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBean = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0);
        loadDraftData();
        loadNames();
    }

    @Override
    public List<CommonFragment.Group> getGroupList() {
        if (!ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().isEmpty()){
            name = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName();
            unit = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getUnit();
            department = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getDepartment();
        }
        List<CommonFragment.Group> groups = new ArrayList<>();
        List<HandInputGroup.Holder> baseHolder = new ArrayList<>();
        baseHolder.add(new HandInputGroup.Holder("申请人",true,false,name,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属单位",true,false,unit,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("所属部门",true,false,department.isEmpty()?" ":department,HandInputGroup.VALUE_TYPE.TEXTFILED).setEditable(false).setColor(Color.rgb(170,170,170)));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("车辆号牌",true,false,"/请选择",HandInputGroup.VALUE_TYPE.SELECT));
        baseHolder.add(new HandInputGroup.Holder("带车干部",false,false,"/请选择",HandInputGroup.VALUE_TYPE.SELECT));
        baseHolder.add(new HandInputGroup.Holder("驾驶员",false,false,"/请选择",HandInputGroup.VALUE_TYPE.SELECT));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("离队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("归队时间",true,false,"/请选择",HandInputGroup.VALUE_TYPE.DATE));
        baseHolder.add(new HandInputGroup.Holder("去向",true,false,"/请输入去向",HandInputGroup.VALUE_TYPE.TEXTFILED));
        baseHolder.add(new HandInputGroup.Holder("",false,false,"", HandInputGroup.VALUE_TYPE.EMPTY_SPACE));
        baseHolder.add(new HandInputGroup.Holder("事由",true,false,"/请输入事由",HandInputGroup.VALUE_TYPE.BIG_EDIT));
//        baseHolder.add(new HandInputGroup.Holder("是否后补申请",false,false,"否",HandInputGroup.VALUE_TYPE.SELECT).setColor(Color.rgb(170,170,170)));
        groups.add(0,new Group("基本信息", null,true,null,baseHolder));
        return groups;
    }

    @Override
    public void setToolbar(HandToolbar toolbar) {
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle("车辆申请");
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
                        ToastUtil.showToast(getContext(), over + "不能为空!");
                        setButtonllEnable(true);
                    }else {
                        String realValueNO = loginBean.getAuthenticationNo();
                        String proposer = getDisplayValueByKey("申请人").getRealValue();
                        String unit = getDisplayValueByKey("所属单位").getRealValue();
                        String driver = getDisplayValueByKey("驾驶员").getRealValue();
                        String leader = getDisplayValueByKey("带车干部").getRealValue();
                        String car = getDisplayValueByKey("车辆号牌").getRealValue();
                        String leaveTime = getDisplayValueByKey("离队时间").getRealValue();
                        String returnTime  = getDisplayValueByKey("归队时间").getRealValue();
                        String content = getDisplayValueByKey("事由").getRealValue();
                        String goDirection = getDisplayValueByKey("去向").getRealValue();
//                        String bFillup = getDisplayValueByKey("是否后补申请").getRealValue();
                        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
                        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
                        carLeaveRrdBean.setNo(realValueNO);//
                        carLeaveRrdBean.setAuthenticationNo(realValueNO);//
                        carLeaveRrdBean.setContent(content);//
                        carLeaveRrdBean.setDestination(goDirection);//
                        carLeaveRrdBean.setOutTime(leaveTime);//
                        carLeaveRrdBean.setInTime(returnTime);//
                        carLeaveRrdBean.setIsAndroid("1");//
//                        carLeaveRrdBean.setBFillup(bFillup.equals("否")?"0":"1");
                        if (!ownerNo1.isEmpty()){
                            carLeaveRrdBean.setDriverNo(ownerNo1);
                        }
                        if (!ownerNo2.isEmpty()){
                            carLeaveRrdBean.setLeaderNo(ownerNo2);
                        }
                        carLeaveRrdBean.setCarNo(car);
                        List<CarLeaveEntity.CarLeaveRrdBean> beanList = new ArrayList<>();
                        beanList.add(carLeaveRrdBean);
                        carLeaveEntity.setCarLeaveRrd(beanList);
                        String json = new Gson().toJson(carLeaveEntity);
                        String s1 = "apply " + json;
                        L.e(TAG+"CarApplyFragment",s1);
                        HttpManager.getInstance().requestResultForm(getTempIP(), s1, CarLeaveEntity.class, new HttpManager.ResultCallback<CarLeaveEntity>() {
                            @Override
                            public void onSuccess(String json, final CarLeaveEntity carLeaveEntity) throws InterruptedException {
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

    private void getNoFromName1(String driver) {
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setName(driver);
        peopleInfoBean.setNo("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getNo());
        peopleInfoBean.setTimeStamp(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_MyInfoSim " + json;
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleInfoBean.class, new HttpManager.ResultNewCallback<PeopleInfoBean>() {
            @Override
            public void onSuccess(String json, PeopleInfoBean entity) throws InterruptedException {
                ownerNo1 = entity.getApi_Get_MyInfoSim().get(0).getNo();
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) {

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

    private void getNoFromName2(String driver) {
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setName(driver);
        peopleInfoBean.setNo("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getNo());
        peopleInfoBean.setTimeStamp(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_MyInfoSim " + json;
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleInfoBean.class, new HttpManager.ResultNewCallback<PeopleInfoBean>() {
            @Override
            public void onSuccess(String json, PeopleInfoBean entity) throws InterruptedException {
                ownerNo2 = entity.getApi_Get_MyInfoSim().get(0).getNo();
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) {

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
        } else if (holder.getKey().equals("是否后补申请")){
            showSelector(holder,new String[]{"是","否"});
        } else if (holder.getKey().equals("车辆号牌")){
            if (!getDisplayValueByKey("驾驶员").getRealValue().isEmpty()){
                getDisplayValueByKey("驾驶员").setDispayValue("/请选择");
            }
            if (!getDisplayValueByKey("带车干部").getRealValue().isEmpty()){
                getDisplayValueByKey("带车干部").setDispayValue("/请选择");
            }
            if (carNoArray != null) {
                showSelector(holder,carNoArray);
              /*  showSelector(holder, carNoArray, new OnSelectedResultCallback() {
                    @Override
                    public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                        String realValue = holder.getRealValue();
                        loadDraftData(realValue);
                    }
                });*/
            } else {
                ToastUtil.showToast(getContext(),"拉取失败");
            }
        } else if (holder.getKey().equals("驾驶员")||holder.getKey().equals("带车干部")){
            if (!getDisplayValueByKey("车辆号牌").getRealValue().isEmpty()) {
                    if (arrayName!= null){
                    showSelector(holder, arrayName, new OnSelectedResultCallback() {
                        @Override
                        public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                            String str1 = ownGroup.getHolderByKey("驾驶员").getRealValue();
                            String str2 = ownGroup.getHolderByKey("带车干部").getRealValue();
                            for (int i = 0; i < ownGroup.getHolders().size(); i++) {
                                if(ownGroup.getHolders().get(i).getKey().equals("驾驶员")){
                                    getNoFromName1(str1);
                                }else if (ownGroup.getHolders().get(i).getKey().equals("带车干部")){
                                    getNoFromName2(str2);
                                }
                            }
                        }
                    });
                }
            }else{
                show("请先选择车辆号牌!");
            }

       /*     if (!getDisplayValueByKey("车辆号牌").getRealValue().isEmpty()){
                if (carOwnerNameArray != null) {
                    showSelector(holder, carOwnerNameArray, new OnSelectedResultCallback() {
                        @Override
                        public void onSelected(Group ownGroup, HandInputGroup.Holder holder, int mainIndex, int itemIndex) {
                            String str1 = ownGroup.getHolderByKey("驾驶员").getRealValue();
                            String str2 = ownGroup.getHolderByKey("带车干部").getRealValue();
                            for (int i = 0; i < ownGroup.getHolders().size(); i++) {
                                if(ownGroup.getHolders().get(i).getKey().equals("驾驶员")){
                                    getNoFromName1(str1);
                                }else if (ownGroup.getHolders().get(i).getKey().equals("带车干部")){
                                    getNoFromName2(str2);
                                }
                            }
                        }
                    });
                } else {
                    ToastUtil.showToast(getContext(),"拉取失败");
                }
            }else{
                show("请先选择车辆号牌!");
            }*/

        }
    }

    private void loadNames(){
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setName("?");
        peopleInfoBean.setNo("?");
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getNo());
        peopleInfoBean.setTimeStamp(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_MyInfoSim " + json;
        HttpManager.getInstance().requestNewResultForm(getTempIP(), s1, PeopleInfoBean.class, new HttpManager.ResultNewCallback<PeopleInfoBean>() {
            @Override
            public void onSuccess(String json, PeopleInfoBean entity) throws InterruptedException {
                int size = entity.getApi_Get_MyInfoSim().size();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    list.add(i,entity.getApi_Get_MyInfoSim().get(i).getName());
                }
                arrayName = list.toArray(new String[list.size()]);
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) {

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

    private void loadDraftData() {
        CarInfoEntity entity = new CarInfoEntity();
        CarInfoEntity.CarInfoBean bean = new CarInfoEntity.CarInfoBean();
        bean.setNo("?");
        bean.setOwner1No("?");
        bean.setOwner2No("?");
        bean.setIsAndroid("1");
        bean.setAuthenticationNo(loginBean.getAuthenticationNo());
        List<CarInfoEntity.CarInfoBean> list = new ArrayList<>();
        list.add(bean);
        entity.setCarInfo(list);
        String requestStr = "get "+new Gson().toJson(entity);
        L.e(TAG+"CarApplyFragment",requestStr);
        HttpManager.getInstance().requestResultForm(getTempIP(), requestStr, CarInfoEntity.class, new HttpManager.ResultCallback<CarInfoEntity>() {
            @Override
            public void onSuccess(String json, CarInfoEntity carInfoEntity) throws InterruptedException {
                int size = carInfoEntity.getCarInfo().size();
                List<String> carNoList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    carNoList.add(i,carInfoEntity.getCarInfo().get(i).getNo());
                }
                carNoArray = carNoList.toArray(new String[carNoList.size()]);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    private void loadDraftData(String realValue) {
        CarInfoEntity entity = new CarInfoEntity();
        CarInfoEntity.CarInfoBean bean = new CarInfoEntity.CarInfoBean();
        bean.setNo(realValue);
        bean.setOwner1No("?");
        bean.setOwner2No("?");
        bean.setIsAndroid("1");
        bean.setAuthenticationNo(loginBean.getAuthenticationNo());
        List<CarInfoEntity.CarInfoBean> list = new ArrayList<>();
        list.add(bean);
        entity.setCarInfo(list);
        String requestStr = "get "+new Gson().toJson(entity);
        L.e(TAG+"CarApplyFragment",requestStr);
        HttpManager.getInstance().requestResultForm(getTempIP(), requestStr, CarInfoEntity.class, new HttpManager.ResultCallback<CarInfoEntity>() {
            @Override
            public void onSuccess(String json, CarInfoEntity carInfoEntity) throws InterruptedException {
                List<String> carOwnerNameList = new ArrayList<>();
                int num = 0;
                if (!carInfoEntity.getCarInfo().get(0).getOwner1Name().isEmpty()){
                    num++;
                }
                if (!carInfoEntity.getCarInfo().get(0).getOwner2Name().isEmpty()){
                    num++;
                }
                if (num == 1){
                    carOwnerNameList.add(0,carInfoEntity.getCarInfo().get(0).getOwner1Name());
                }else if(num == 2){
                    carOwnerNameList.add(0,carInfoEntity.getCarInfo().get(0).getOwner1Name());
                    carOwnerNameList.add(1,carInfoEntity.getCarInfo().get(0).getOwner2Name());
                }
                carOwnerNameArray = carOwnerNameList.toArray(new String[carOwnerNameList.size()]);
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
    public void onDataChanged(HandInputGroup.Holder holder) throws ParseException {
        Group group = getGroup().get(0);
        if (holder.getKey().equals("离队时间")){
            HandInputGroup.Holder holder1 = group.getHolderByKey("归队时间");
            if (!holder1.getRealValue().isEmpty()){
                String leave = holder.getRealValue();
                String returnt = holder1.getRealValue();
                int getday = getday(leave, returnt);
                if (getday == -1) {
                    holder.setDispayValue("");
                    ToastUtil.showToast(getContext(),"请正确选择离队、归队时间");
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
                    ToastUtil.showToast(getContext(),"请正确选择离队、归队时间");
                }
            }
        }
    }

}