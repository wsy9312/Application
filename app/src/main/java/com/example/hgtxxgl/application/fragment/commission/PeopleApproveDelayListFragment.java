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
import com.example.hgtxxgl.application.bean.login.LoginInfoBean;
import com.example.hgtxxgl.application.bean.people.PeopleApproveDelayBean;
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

public class PeopleApproveDelayListFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "PeopleApproveDelayListFragment";
    private String tempIP;
    SimpleListView lv;

    private String headers[] = {"申请类型","申请时间"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter typeAdapter;
    private GirdDropDownAdapter dateAdapter;
    private String typesArray[] = {"全部", "事假申请", "病假申请", "休假申请", "外出申请"};
    private String datesArray[] = {"全部", "一天内", "一周内", "一月内"};
    private DropDownMenu mDropDownMenu;
    private String selectedArr[] = {"全部","全部"};

    private LoginInfoBean.ApiAddLoginBean loginBean;

    public PeopleApproveDelayListFragment(){

    }

    public static PeopleApproveDelayListFragment newInstance(Bundle bundle) {
        PeopleApproveDelayListFragment fragment = new PeopleApproveDelayListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<PeopleApproveDelayBean.ApiGetMyApproveForPeoBean> entityList = new ArrayList<>();
    ListAdapter<PeopleApproveDelayBean.ApiGetMyApproveForPeoBean> adapter = new ListAdapter<PeopleApproveDelayBean.ApiGetMyApproveForPeoBean>
            ((ArrayList<PeopleApproveDelayBean.ApiGetMyApproveForPeoBean>) entityList, R.layout.item_approve_people) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, PeopleApproveDelayBean.ApiGetMyApproveForPeoBean bean) {
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
        //init city menu
        ListView typeView = new ListView(getActivity());
        typeView.setDividerHeight(0);
        typeAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(typesArray));
        typeView.setAdapter(typeAdapter);

        final ListView dateView = new ListView(getActivity());
        dateView.setDividerHeight(0);
        dateAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(datesArray));
        dateView.setAdapter(dateAdapter);

        //init popupViews
        popupViews.add(typeView);
        popupViews.add(dateView);

        //add item click event
        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(typesArray[position]);
                selectedArr[0] = typesArray[position];
                L.e(TAG,"type="+selectedArr[0]+"-"+selectedArr[1]);
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
                selectedArr[1] = datesArray[position];
                L.e(TAG,"state="+selectedArr[0]+"-"+selectedArr[1]);
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
        String menu1 = selectedArr[0];//申请类型
        String menu2 = selectedArr[1];//申请时间
        String type = "?";
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
        PeopleApproveDelayBean.ApiGetMyApproveForPeoBean peopleLeaveRrdBean = new PeopleApproveDelayBean.ApiGetMyApproveForPeoBean();
        peopleLeaveRrdBean.setNo("?");
        peopleLeaveRrdBean.setAuthenticationNo(loginBean.getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setRegisterTime(registertime);
        peopleLeaveRrdBean.setOutTime("?");
        peopleLeaveRrdBean.setInTime("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setProcess("?");
        peopleLeaveRrdBean.setBCancel("0");
        peopleLeaveRrdBean.setBFillup("?");
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setResult("?");
        peopleLeaveRrdBean.setOutType(type);
        peopleLeaveRrdBean.setApproverNo("?");
        peopleLeaveRrdBean.setHisAnnotation("?");
        peopleLeaveRrdBean.setDestination("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean.setCurrentApproverNo("?");
        peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleLeaveRrdBean);
        String s = "Api_Get_MyApproveForPeo " + json;
        L.e(TAG+"PeopleApproveDelayListFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, PeopleApproveDelayBean.class,new HttpManager.ResultNewCallback<PeopleApproveDelayBean>() {
            @Override
            public void onSuccess(String json, PeopleApproveDelayBean peopleApproveDelayBean) throws Exception {
                if (peopleApproveDelayBean != null && peopleApproveDelayBean.getApi_Get_MyApproveForPeo().size() > 0 && peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(0) != null) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    for (int i = 0; i < peopleApproveDelayBean.getApi_Get_MyApproveForPeo().size(); i++) {
                        if (!peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(i).getApproverNo().contains(loginBean.getAuthenticationNo())){
                            L.e(TAG+"PeopleApproveDelayListFragment",peopleApproveDelayBean.toString());
                            hasMore = true;
                            entityList.add(peopleApproveDelayBean.getApi_Get_MyApproveForPeo().get(i));
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
        }
    }

    private DetailFragment.DataCallback callback;

    public PeopleApproveDelayListFragment setCallback(DetailFragment.DataCallback callback) {
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

}
