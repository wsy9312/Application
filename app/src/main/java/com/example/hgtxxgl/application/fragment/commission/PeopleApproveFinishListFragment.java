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
import com.example.hgtxxgl.application.bean.people.PeopleApproveFinishBean;
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

public class PeopleApproveFinishListFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "PeopleApproveFinishListFragment";
    private String tempIP;
    SimpleListView lv;
    private String authenticationNo;

    private String headers[] = {"申请类型","审批结果","申请时间"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter typeAdapter;
    private GirdDropDownAdapter resultAdapter;
    private GirdDropDownAdapter dateAdapter;
    private String typesArray[] = {"全部", "事假申请", "病假申请", "休假申请", "外出申请"};
    private String resultArray[] = {"全部", "审批同意", "审批退回", "审批拒绝", "审批上报"};
    private String datesArray[] = {"全部", "一天内", "一周内", "一月内"};
    private DropDownMenu mDropDownMenu;
    private String selectedArr[] = {"全部","全部","全部"};

    public PeopleApproveFinishListFragment(){

    }

    public static PeopleApproveFinishListFragment newInstance(Bundle bundle) {
        PeopleApproveFinishListFragment fragment = new PeopleApproveFinishListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean> entityList = new ArrayList<>();
    ListAdapter<PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean> adapter = new ListAdapter<PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean>
            ((ArrayList<PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean>) entityList, R.layout.item_approve_people) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean bean) {
            String registerTime;
            String dateStr = DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss");
            boolean isToday = TimeUtil.IsToday(dateStr);
            if (isToday){
                registerTime = DataUtil.parseDateByFormat(bean.getRegisterTime(), "HH:mm");
            }else{
                registerTime = DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy.MM.dd");
            }
            holder.setImage(R.id.approve_imgae,bean.getName());
            holder.setText(R.id.approve_name,bean.getName()+"的请假");
            holder.setText(R.id.approve_type,"请假类型: "+bean.getOutType());
            holder.setText(R.id.approve_outtime,"离队时间:"+DataUtil.parseDateByFormat(bean.getOutTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_intime,"归队时间:"+DataUtil.parseDateByFormat(bean.getInTime(), "yyyy-MM-dd"));

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
                    holder.setText(R.id.approve_state, "审批退回");
                    holder.setTextColor(R.id.approve_state, Color.rgb(237,142,148));
                }
                //流程未结束
            }else if (bean.getProcess().equals("2")){
                //审批中/黄色
                holder.setText(R.id.approve_state,"审批上报");
                holder.setTextColor(R.id.approve_state, Color.rgb(86,197,163));
            }

            holder.setText(R.id.approve_time,registerTime);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
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
        //init city menu
        ListView typeView = new ListView(getActivity());
        typeView.setDividerHeight(0);
        typeAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(typesArray));
        typeView.setAdapter(typeAdapter);

        ListView resultView = new ListView(getActivity());
        resultView.setDividerHeight(0);
        resultAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(resultArray));
        resultView.setAdapter(resultAdapter);

        final ListView dateView = new ListView(getActivity());
        dateView.setDividerHeight(0);
        dateAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(datesArray));
        dateView.setAdapter(dateAdapter);

        //init popupViews
        popupViews.add(typeView);
        popupViews.add(resultView);
        popupViews.add(dateView);
        //add item click event

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(typesArray[position]);
                selectedArr[0] = typesArray[position];
                L.e(TAG,"type="+selectedArr[0]+"-"+selectedArr[1]+"-"+selectedArr[2]);
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
                L.e(TAG,"type="+selectedArr[0]+"-"+selectedArr[1]+"-"+selectedArr[2]);
                entityList.clear();
                loadData(selectedArr,1,10);
                adapter.notifyDataSetChanged();
                mDropDownMenu.closeMenu();
            }
        });
        dateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dateAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(datesArray[position]);
                selectedArr[2] = datesArray[position];
                L.e(TAG,"state="+selectedArr[0]+"-"+selectedArr[1]+"-"+selectedArr[2]);
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
        String menu1 = selectedArr[0];//申请类型
        String menu2 = selectedArr[1];//审批结果
        String menu3 = selectedArr[2];//申请时间
        String type = "?";
        String process = "?";
        String result = "?";
        String registertime = "?";
        String screen = "?";
        switch (menu1){
            case "全部":
                type = "?";
                break;
            case "事假申请":
                type = "事假申请";
                break;
            case "病假申请":
                type = "病假申请";
                break;
            case "休假申请":
                type = "休假申请";
                break;
            case "外出申请":
                type = "外出申请";
                break;
        }
        switch (menu2){
            case "全部":
                process = "?";
                result = "?";
                break;
            case "审批同意":
                process = "1";
                result = "1";
                break;
            case "审批退回":
                process = "1";
                result = "2";
                break;
            case "审批拒绝":
                process = "1";
                result = "0";
                break;
            case "审批上报":
                process = "2";
                result = "?";
                break;
        }
        switch (menu3){
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
        PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean peopleLeaveRrdBean = new PeopleApproveFinishBean.ApiGetMyApproveForPeoHisBean();
        peopleLeaveRrdBean.setNo("?");
        peopleLeaveRrdBean.setAuthenticationNo(authenticationNo);
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setRegisterTime(registertime);
        peopleLeaveRrdBean.setOutTime("?");
        peopleLeaveRrdBean.setInTime("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setProcess(process);
        peopleLeaveRrdBean.setBCancel("0");
        peopleLeaveRrdBean.setBFillup("?");
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setResult(result);
        peopleLeaveRrdBean.setOutType(type);
        peopleLeaveRrdBean.setApproverNo("?");
        peopleLeaveRrdBean.setHisAnnotation("?");
        peopleLeaveRrdBean.setDestination("?");
        peopleLeaveRrdBean.setCurrentApproverNo("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleLeaveRrdBean);
        String s = "Api_Get_MyApproveForPeoHis " + json;
        L.e(TAG+"PeopleApproveFinishListFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, PeopleApproveFinishBean.class,new HttpManager.ResultNewCallback<PeopleApproveFinishBean>() {
            @Override
            public void onSuccess(String json, PeopleApproveFinishBean peopleApproveFinishBean) throws Exception {
                if (peopleApproveFinishBean != null && peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().size() > 0 && peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(0) != null) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    for (int i = 0; i < peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().size(); i++) {
                        if (peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(i).getApproverNo().contains(authenticationNo)){
                            L.e(TAG+"PeopleApproveFinishListFragment",peopleApproveFinishBean.toString());
                            hasMore = true;
                            entityList.add(peopleApproveFinishBean.getApi_Get_MyApproveForPeoHis().get(i));
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    hasMore = false;
                }
                pb.setVisibility(GONE);
                ivEmpty.setVisibility(GONE);
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
        checkDetail(position, PageConfig.PAGE_COMMISSION_PEOPLE_DETAIL);
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
        bundle.putString("approveState","1");
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
        }
    }

    private DetailFragment.DataCallback callback;

    public PeopleApproveFinishListFragment setCallback(DetailFragment.DataCallback callback) {
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
        ivEmpty.setVisibility(GONE);
    }

    @Override
    public void onScrollOutside() {

    }
}
