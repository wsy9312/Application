package com.example.hgtxxgl.application.fragment.commission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.bean.car.CarApproveDelayBean;
import com.example.hgtxxgl.application.bean.login.LoginInfoBean;
import com.example.hgtxxgl.application.bean.people.PeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurrentCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount10Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount11Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount12Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount8Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount9Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveOutCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveRestCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveSickCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveWorkCountBean;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.fragment.launch.dropdownmenu.GirdDropDownAdapter;
import com.example.hgtxxgl.application.utils.TimeUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static com.example.hgtxxgl.application.R.id.iv_empty;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class CarApproveDelayListFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "CarApproveDelayListFragment";
    private String tempIP;
    SimpleListView lv;
    private LoginInfoBean.ApiAddLoginBean loginBean;

    private String headers[] = {"申请时间"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter dateAdapter;
    private String datesArray[] = {"全部", "一天内", "一周内", "一月内"};
    private DropDownMenu mDropDownMenu;
    private String selectedArr[] = {"全部"};

    public CarApproveDelayListFragment(){

    }

    public static CarApproveDelayListFragment newInstance(Bundle bundle) {
        CarApproveDelayListFragment fragment = new CarApproveDelayListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<CarApproveDelayBean.ApiGetMyApproveForCarBean> entityList = new ArrayList<>();
    ListAdapter<CarApproveDelayBean.ApiGetMyApproveForCarBean> adapter = new ListAdapter<CarApproveDelayBean.ApiGetMyApproveForCarBean>
            ((ArrayList<CarApproveDelayBean.ApiGetMyApproveForCarBean>) entityList, R.layout.item_approve_people) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, CarApproveDelayBean.ApiGetMyApproveForCarBean bean) {
            String registerTime;
            String dateStr = DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss");
            boolean isToday = TimeUtil.IsToday(dateStr);
            if (isToday){
                registerTime = DataUtil.parseDateByFormat(bean.getRegisterTime(), "HH:mm");
            }else{
                registerTime = DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy.MM.dd");
            }
            holder.setImage(R.id.approve_imgae,bean.getName());
            holder.setText(R.id.approve_name,bean.getName()+"的车辆申请");
            holder.setText(R.id.approve_type,"车辆号牌: "+bean.getCarNo());
            holder.setText(R.id.approve_outtime,"离队时间:"+ DataUtil.parseDateByFormat(bean.getOutTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_intime,"归队时间:"+DataUtil.parseDateByFormat(bean.getInTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_state,"待审批");
            holder.setTextColor(R.id.approve_state, Color.rgb(218,176,101));
            holder.setText(R.id.approve_time,registerTime);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBean = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0);
        loadData(selectedArr,beginNum, endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_approve_listview_libmain, null, false);
        initPopMenu(view);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(iv_empty);
        ivEmpty.setText(getString(R.string.no_current_record));
        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
        lv.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (adapter.getCount() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
                } else {
                    ivEmpty.setVisibility(GONE);
                }
                pb.setVisibility(GONE);
                super.onChanged();
            }
        });
        lv.setOnRefreshListener(this);
        lv.setOnItemClickListener(this);
        return view;
    }

    private void initPopMenu(View view) {
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);

        final ListView dateView = new ListView(getActivity());
        dateView.setDividerHeight(0);
        dateAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(datesArray));
        dateView.setAdapter(dateAdapter);

        //init popupViews
        popupViews.add(dateView);

        //add item click event
        dateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(datesArray[position]);
                selectedArr[0] = datesArray[position];
                L.e(TAG,"date="+selectedArr[0]);
                entityList.clear();
                loadData(selectedArr,1,10);
                adapter.notifyDataSetChanged();
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        LinearLayout contentView = new LinearLayout(getActivity());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    public void loadData(String[] selectedArr,final int beginNum, final int endNum) {
        String menu1 = selectedArr[0];//
        String registertime = "?";
        switch (menu1){
            case "全部":
                registertime = "?";
                break;
            case "一天内":
                registertime = TimeUtil.getCurrentDate()+" 00:00:00&&"+TimeUtil.getCurrentTime();
                break;
            case "一周内":
                registertime = TimeUtil.getThisWeekMonday()+" 00:00:00&&"+TimeUtil.getCurrentTime();
                break;
            case "一月内":
                registertime = TimeUtil.getThisMouthFirstday()+" 00:00:00&&"+TimeUtil.getCurrentTime();
                break;
        }
        if (callback != null) {
            callback.onLoadData();
        }
        CarApproveDelayBean.ApiGetMyApproveForCarBean carLeaveRrdBean = new CarApproveDelayBean.ApiGetMyApproveForCarBean();
        carLeaveRrdBean.setNo("?");
        carLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setRegisterTime(registertime);
        carLeaveRrdBean.setOutTime("?");
        carLeaveRrdBean.setInTime("?");
        carLeaveRrdBean.setContent("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setProcess("?");
        carLeaveRrdBean.setBCancel("0");
        carLeaveRrdBean.setBFillup("?");
        carLeaveRrdBean.setNoIndex("?");
        carLeaveRrdBean.setResult("?");
        carLeaveRrdBean.setOutType("?");
        carLeaveRrdBean.setApproverNo("?");
        carLeaveRrdBean.setHisAnnotation("?");
        carLeaveRrdBean.setDestination("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));
        carLeaveRrdBean.setCurrentApproverNo("?");
        carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(carLeaveRrdBean);
        String s = "Api_Get_MyApproveForCar " + json;
        L.e(TAG+"CarApproveDelayListFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, CarApproveDelayBean.class,new HttpManager.ResultNewCallback<CarApproveDelayBean>() {
            @Override
            public void onSuccess(String json, CarApproveDelayBean carApproveDelayBean) throws Exception {
                if (carApproveDelayBean != null && carApproveDelayBean.getApi_Get_MyApproveForCar().size() > 0 && carApproveDelayBean.getApi_Get_MyApproveForCar().get(0) != null) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    for (int i = 0; i < carApproveDelayBean.getApi_Get_MyApproveForCar().size(); i++) {
                        if (!carApproveDelayBean.getApi_Get_MyApproveForCar().get(i).getApproverNo().contains(loginBean.getAuthenticationNo())){
                            L.e(TAG+"CarApproveDelayListFragment",carApproveDelayBean.toString());
                            hasMore = true;
                            entityList.add(carApproveDelayBean.getApi_Get_MyApproveForCar().get(i));
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    hasMore = false;
                }
                pb.setVisibility(GONE);
                lv.completeRefresh();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position, PageConfig.PAGE_COMMISSION_CAR_DETAIL);
    }

    private void checkDetail(int position, int pageApplyBleave) {
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
        bundle.putString("no", adapter.getItem(position).getNo());
        bundle.putString("name", adapter.getItem(position).getName());
        bundle.putString("outtime",adapter.getItem(position).getOutTime());
        bundle.putString("intime", adapter.getItem(position).getInTime());
        bundle.putString("content", adapter.getItem(position).getContent());
        bundle.putString("process", adapter.getItem(position).getProcess());
        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
        bundle.putString("approveState","0");
        bundle.putInt("item",position);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, CommonValues.MYCOMM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYCOMM){
            if (beginNum == 1 && endNum == 10){
                entityList.clear();
            }
            loadData(selectedArr,1,10);
            adapter.notifyDataSetChanged();
            loadChartTotalNum();
        }
    }

    private DetailFragment.DataCallback callback;

    public CarApproveDelayListFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onPullRefresh() {
        hasMore = true;
        beginNum = 1;
        endNum = 10;
        loadData(selectedArr,beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        if (hasMore) {
            beginNum += 10;
            endNum += 10;
            loadData(selectedArr,beginNum, endNum);
        }
        lv.completeRefresh();
    }

    @Override
    public void onScrollOutside() {

    }
    private void loadChartTotalNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartTotalNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartTotalNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCountBean(peopleLeaveCountBean);
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
                loadChartCurrentNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartCurrentNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("0");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartCurrentNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurrentCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurrentCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurrentCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartCurrentNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCurrentCountBean(peopleLeaveCountBean);
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
                loadChartWorkNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartWorkNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("事假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartWorkNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveWorkCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveWorkCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveWorkCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartWorkNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setWorkCountBean(peopleLeaveCountBean);
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
                loadChartSickNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartSickNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("病假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartSickNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveSickCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveSickCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveSickCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartSickNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setSickCountBean(peopleLeaveCountBean);
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
                loadChartRestNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartRestNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("休假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartRestNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveRestCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveRestCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveRestCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartRestNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setRestCountBean(peopleLeaveCountBean);
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
                loadChartOutNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartOutNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("外出申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartOutNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveOutCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveOutCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveOutCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartOutNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setOutCountBean(peopleLeaveCountBean);
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
                loadTempCurveChart8Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart8Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-08-01 00:00:00&&2018-08-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart8Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount8Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount8Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount8Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart8Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount8Bean(peopleLeaveCountBean);
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
                loadTempCurveChart9Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart9Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-09-01 00:00:00&&2018-09-30 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart9Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount9Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount9Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount9Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart9Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount9Bean(peopleLeaveCountBean);
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
                loadTempCurveChart10Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart10Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-10-01 00:00:00&&2018-10-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart10Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount10Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount10Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount10Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart10Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount10Bean(peopleLeaveCountBean);
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
                loadTempCurveChart11Num();
            }

            @Override
            public void onAfter(int id) throws Exception {
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart11Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-11-01 00:00:00&&2018-11-30 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart11Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount11Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount11Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount11Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart11Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount11Bean(peopleLeaveCountBean);
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
                loadTempCurveChart12Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart12Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-12-01 00:00:00&&2018-12-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart12Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount12Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount12Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount12Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart12Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount12Bean(peopleLeaveCountBean);
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
