package com.example.hgtxxgl.application.fragment.launch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.hgtxxgl.application.bean.car.CarLeaveDetailBean;
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
import com.example.hgtxxgl.application.fragment.launch.dropdownmenu.BackHandlerHelper;
import com.example.hgtxxgl.application.fragment.launch.dropdownmenu.FragmentBackHandler;
import com.example.hgtxxgl.application.fragment.launch.dropdownmenu.GirdDropDownAdapter;
import com.example.hgtxxgl.application.utils.TimeUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class CarDetailListFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener,FragmentBackHandler {

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    SimpleListView lv;
    private String tempIP;

    private String headers[] = {"审批状态","审批结果"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter stateAdapter;
    private GirdDropDownAdapter resultAdapter;
    private String statesArray[] = {"全部", "审批结束", "待审批", "审批中", "已撤销"};
    private String resultArray[] = {"全部", "审批同意", "申请被退回", "审批拒绝"};
    private DropDownMenu mDropDownMenu;
    private String selectedArr[] = {"全部","全部"};

    private static final String TAG = "CarDetailListFragment";

    public CarDetailListFragment() {

    }

    public static CarDetailListFragment newInstance(Bundle bundle) {
        CarDetailListFragment fragment = new CarDetailListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<CarLeaveDetailBean.ApiGetMyApplyForCarBean> entityList = new ArrayList<>();

    ListAdapter<CarLeaveDetailBean.ApiGetMyApplyForCarBean> adapter = new ListAdapter<CarLeaveDetailBean.ApiGetMyApplyForCarBean>
            ((ArrayList<CarLeaveDetailBean.ApiGetMyApplyForCarBean>) entityList, R.layout.item_approve_car) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, CarLeaveDetailBean.ApiGetMyApplyForCarBean bean){
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
            holder.setText(R.id.approve_num,"车辆号牌: "+bean.getCarNo());
            holder.setText(R.id.approve_outtime,"离队时间:"+DataUtil.parseDateByFormat(bean.getOutTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_intime,"归队时间:"+DataUtil.parseDateByFormat(bean.getInTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_time,registerTime);

            //流程已结束
            if (bean.getProcess().equals("1")){
                //已完成(拒绝)/红色
                if (bean.getResult().equals("0")){
                    holder.setText(R.id.approve_state, "审批拒绝");
                    holder.setTextColor(R.id.approve_state, Color.rgb(237,142,148));
                    //已完成(同意)/绿色
                }else if (bean.getResult().equals("1")){
                    holder.setText(R.id.approve_state, "审批同意");
                    holder.setTextColor(R.id.approve_state, Color.rgb(86,197,163));
                    //已完成(被退回)/红色
                }else if (bean.getResult().equals("2")){
                    holder.setText(R.id.approve_state, "申请被退回");
                    holder.setTextColor(R.id.approve_state, Color.rgb(237,142,148));
                }
                //流程未结束
            }else if (bean.getProcess().equals("2")){
                //审批中/黄色
                holder.setText(R.id.approve_state,"审批中");
                holder.setTextColor(R.id.approve_state, Color.rgb(218,176,101));
                //流程未开始
            }else if (bean.getProcess().equals("0")){
                //未审批
                if (bean.getBCancel().equals("0")){
                    holder.setText(R.id.approve_state,"待审批");
                    holder.setTextColor(R.id.approve_state, Color.rgb(218,176,101));
                    //已撤销
                }else if (bean.getBCancel().equals("1")){
                    holder.setText(R.id.approve_state,"已撤销");
                    holder.setTextColor(R.id.approve_state, Color.rgb(48,48,48));
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData(selectedArr,beginNum, endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launch_listview_libmain, null, false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.launch_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        handToolbar.setTitle("我发起的(车辆)");
        handToolbar.setTitleSize(18);
        initPopMenu(view);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText(getString(R.string.no_current_record));
        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
        lv.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (adapter.getCount() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
                } else {
                    ivEmpty.setVisibility(View.GONE);
                }
                pb.setVisibility(View.GONE);
                super.onChanged();
            }
        });
        lv.setOnRefreshListener(this);
        lv.setOnItemClickListener(this);
        return view;
    }

    private void initPopMenu(View view) {
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        //init city menu
        final ListView stateView = new ListView(getActivity());
        stateView.setDividerHeight(0);
        stateAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(statesArray));
        stateView.setAdapter(stateAdapter);

        ListView resultView = new ListView(getActivity());
        resultView.setDividerHeight(0);
        resultAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(resultArray));
        resultView.setAdapter(resultAdapter);

        //init popupViews
        popupViews.add(stateView);
        popupViews.add(resultView);
        //add item click event
        stateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stateAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(statesArray[position]);
                selectedArr[0] = statesArray[position];
                L.e(TAG,"state="+selectedArr[0]+"-"+selectedArr[1]);
                entityList.clear();
                loadData(selectedArr,1,10);
                adapter.notifyDataSetChanged();
                mDropDownMenu.closeMenu();
            }
        });
        resultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(resultArray[position]);
                selectedArr[1] = resultArray[position];
                L.e(TAG,"type="+selectedArr[0]+"-"+selectedArr[1]);
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

    public void loadData(String[] selectedArr, final int beginNum, final int endNum) {
        String menu1 = selectedArr[0];//审批状态
        String menu2 = selectedArr[1];//审批结果
        String process = "?";
        String bCancel = "?";
        String result = "?";
        String screen = "?";
        switch (menu1){
            case "全部":
                switch (menu2){
                    case "全部":
                        process = "?";
                        bCancel = "?";
                        result = "?";
                        break;
                    case "审批同意":
                        process = "1";
                        bCancel = "0";
                        result = "1";
                        break;
                    case "申请被退回":
                        process = "1";
                        bCancel = "0";
                        result = "2";
                        break;
                    case "审批拒绝":
                        process = "1";
                        bCancel = "0";
                        result = "0";
                        break;
                }
                break;
            case "审批结束":
                process = "1";
                bCancel = "0";
                switch (menu2){
                    case "全部":
                        result = "?";
                        break;
                    case "审批同意":
                        result = "1";
                        break;
                    case "申请被退回":
                        result = "2";
                        break;
                    case "审批拒绝":
                        result = "0";
                        break;
                }
                break;
            case "待审批":
                process = "0";
                bCancel = "0";
                switch (menu2){
                    case "全部":
                        result = "?";
                        break;
                    case "审批同意":
                        result = "1";
                        break;
                    case "申请被退回":
                        result = "2";
                        break;
                    case "审批拒绝":
                        result = "2";
                        break;
                }
                break;
            case "审批中":
                process = "2";
                bCancel = "0";
                switch (menu2){
                    case "全部":
                        result = "?";
                        break;
                    case "审批同意":
                        result = "1";
                        break;
                    case "申请被退回":
                        result = "2";
                        break;
                    case "审批拒绝":
                        result = "0";
                        screen= "#$*721YR";
                        break;
                }
                break;
            case "已撤销":
                switch (menu2){
                    case "全部":
                        process = "?";
                        bCancel = "1";
                        result = "?";
                        break;
                    case "审批同意":
                        process = "?";
                        bCancel = "1";
                        result = "1";
                        break;
                    case "申请被退回":
                        process = "?";
                        bCancel = "1";
                        result = "2";
                        break;
                    case "审批拒绝":
                        process = "1";
                        bCancel = "1";
                        result = "0";
                        break;
                }
                break;
        }

        if (callback != null) {
            callback.onLoadData();
        }
        CarLeaveDetailBean.ApiGetMyApplyForCarBean carLeaveRrdBean = new CarLeaveDetailBean.ApiGetMyApplyForCarBean();
        carLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getAuthenticationNo());
        carLeaveRrdBean.setName("?");
        carLeaveRrdBean.setProcess(process);
        carLeaveRrdBean.setContent(screen);
        carLeaveRrdBean.setDestination("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));
        carLeaveRrdBean.setNoIndex("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setRegisterTime("?");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setBCancel(bCancel);
        carLeaveRrdBean.setResult(result);
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setDriverNo("?");
        carLeaveRrdBean.setLeaderNo("?");
        carLeaveRrdBean.setApproverNo("?");
        carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(carLeaveRrdBean);
        String s = "Api_Get_MyApplyForCar " + json;
        Log.e(TAG,s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, CarLeaveDetailBean.class,new HttpManager.ResultNewCallback<CarLeaveDetailBean>() {
            @Override
            public void onSuccess(String json, CarLeaveDetailBean carLeaveDetailBean) throws Exception {
                L.e(TAG+"onSuccess",json);
                if (carLeaveDetailBean != null && carLeaveDetailBean.getApi_Get_MyApplyForCar().size() > 0 && carLeaveDetailBean.getApi_Get_MyApplyForCar().get(0) != null) {
                    if (beginNum== 1 && endNum == 10){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(carLeaveDetailBean.getApi_Get_MyApplyForCar());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                }
                pb.setVisibility(View.GONE);
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

    private void loadMore() {
        if (hasMore) {
            beginNum += 10;
            endNum += 10;
            loadData(selectedArr,beginNum, endNum);
        } else {
            lv.completeRefresh();
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position, PageConfig.PAGE_LAUNCH_CAR_DETAIL);
    }

    private void checkDetail(int position, int pageApplyBleave) {
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
        bundle.putString("no", adapter.getItem(position).getNo());
        bundle.putString("outtime",adapter.getItem(position).getOutTime());
        bundle.putString("intime", adapter.getItem(position).getInTime());
        bundle.putString("content", adapter.getItem(position).getContent());
        bundle.putString("process", adapter.getItem(position).getProcess());
        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
        bundle.putString("carno",adapter.getItem(position).getCarNo());
        bundle.putString("driverno",adapter.getItem(position).getDriverNo());
        bundle.putString("leaderno",adapter.getItem(position).getLeaderNo());
        bundle.putInt("item",position);
//        bundle.putInt("tabIndex",tabIndex);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, CommonValues.MYLAUN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYLAUN){
            if (beginNum == 1 && endNum == 10){
                entityList.clear();
            }
            loadData(selectedArr,1,10);
            adapter.notifyDataSetChanged();
            loadChartTotalNum();
        }
    }

    private DetailFragment.DataCallback callback;

    public CarDetailListFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

    @Override
    public boolean onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            return false;
        }
        return BackHandlerHelper.handleBackPress(this);
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

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart11Num();
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
