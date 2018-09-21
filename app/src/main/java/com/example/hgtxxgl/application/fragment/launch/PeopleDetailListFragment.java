package com.example.hgtxxgl.application.fragment.launch;

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
import com.example.hgtxxgl.application.bean.people.PeopleLeaveDetailBean;
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

public class PeopleDetailListFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener,FragmentBackHandler {

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    SimpleListView lv;

    private String headers[] = {"审批状态","申请类型"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter stateAdapter;
    private GirdDropDownAdapter typeAdapter;
    private String statesArray[] = {"全部", "审批结束", "待审批", "审批中", "已撤销"};
    private String typesArray[] = {"全部", "事假申请", "病假申请", "休假申请", "外出申请"};
    private DropDownMenu mDropDownMenu;

    private static final String TAG = "PeopleDetailListFragment";

    public PeopleDetailListFragment() {

    }

    public static PeopleDetailListFragment newInstance(Bundle bundle) {
        PeopleDetailListFragment fragment = new PeopleDetailListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean> entityList = new ArrayList<>();

    ListAdapter<PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean> adapter = new ListAdapter<PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean>
            ((ArrayList<PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean>) entityList, R.layout.item_approve_people) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean bean) {
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
//        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(beginNum, endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.launch_listview_libmain, null, false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.launch_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        handToolbar.setTitle("我发起的(人员)");
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
        ListView stateView = new ListView(getActivity());
        stateView.setDividerHeight(0);
        stateAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(statesArray));
        stateView.setAdapter(stateAdapter);

        ListView typeView = new ListView(getActivity());
        typeView.setDividerHeight(0);
        typeAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(typesArray));
        typeView.setAdapter(typeAdapter);

        //init popupViews
        popupViews.add(stateView);
        popupViews.add(typeView);
        //add item click event
        stateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stateAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(statesArray[position]);
                mDropDownMenu.closeMenu();
            }
        });
        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(typesArray[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        LinearLayout contentView = new LinearLayout(getActivity());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    public void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean peopleLeaveRrdBean = new PeopleLeaveDetailBean.ApiGetMyApplyForPeoBean();
        peopleLeaveRrdBean.setNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setProcess("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setRegisterTime("?");
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setBCancel("?");
        peopleLeaveRrdBean.setResult("?");
        peopleLeaveRrdBean.setDestination("?");
        peopleLeaveRrdBean.setApproverNo("?");
        peopleLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleLeaveRrdBean);
        final String s = "Api_Get_MyApplyForPeo " + json;
        L.e(TAG+"PeopleDetailListFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, PeopleLeaveDetailBean.class,new HttpManager.ResultNewCallback<PeopleLeaveDetailBean>() {
            @Override
            public void onSuccess(String json, PeopleLeaveDetailBean peopleLeaveDetailBean) throws Exception {
                if (peopleLeaveDetailBean != null && peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().size() > 0 && peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0) != null) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(peopleLeaveDetailBean.getApi_Get_MyApplyForPeo());
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
            loadData(beginNum, endNum);
        } else {
            lv.completeRefresh();
        }
    }

    @Override
    public void onPullRefresh() {
        hasMore = true;
        beginNum = 1;
        endNum = 10;
        loadData(beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position, PageConfig.PAGE_LAUNCH_PEOPLE_DETAIL);
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
            loadData(1,10);
            adapter.notifyDataSetChanged();
        }
    }

    private DetailFragment.DataCallback callback;

    public PeopleDetailListFragment setCallback(DetailFragment.DataCallback callback) {
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
}
