package com.example.hgtxxgl.application.fragment.detail;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.bean.people.PeopleLeaveDetailBean;
import com.example.hgtxxgl.application.fragment.DetailFragment;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class PeopleDetailListFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    SimpleListView lv;
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
            holder.setImage(R.id.approve_imgae,bean.getName());
            holder.setText(R.id.approve_name,bean.getName()+"的请假");
            holder.setText(R.id.approve_type,"请假类型: "+bean.getOutType());
            holder.setText(R.id.approve_outtime,"离队时间:"+DataUtil.parseDateByFormat(bean.getOutTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_intime,"归队时间:"+DataUtil.parseDateByFormat(bean.getInTime(), "yyyy-MM-dd"));
            holder.setText(R.id.approve_time,DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));

            //流程已结束
            if (bean.getProcess().equals("1")){
                //已完成(拒绝)/红色
                if (bean.getResult().equals("0")){
                    holder.setText(R.id.approve_state, "审批拒绝");
                    holder.setTextColor(R.id.approve_state, Color.rgb(214,16,24));
                    //已完成(同意)/绿色
                }else if (bean.getResult().equals("1")){
                    holder.setText(R.id.approve_state, "审批同意");
                    holder.setTextColor(R.id.approve_state, Color.rgb(0,128,0));
                    //已完成(被退回)/红色
                }else if (bean.getResult().equals("2")){
                    holder.setText(R.id.approve_state, "申请被退回");
                    holder.setTextColor(R.id.approve_state, Color.rgb(214,16,24));
                }
                //流程未结束
            }else if (bean.getProcess().equals("2")){
                //审批中/黄色
                holder.setText(R.id.approve_state,"审批中");
                holder.setTextColor(R.id.approve_state, Color.rgb(255,140,0));
                //流程未开始
            }else if (bean.getProcess().equals("0")){
                //未审批
                if (bean.getBCancel().equals("0")){
                    holder.setText(R.id.approve_state,"未审批");
                    holder.setTextColor(R.id.approve_state, Color.rgb(255,140,0));
                    //已撤销
                }else if (bean.getBCancel().equals("1")){
                    holder.setText(R.id.approve_state,"已撤销");
                    holder.setTextColor(R.id.approve_state, Color.rgb(255,140,0));
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
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.notification_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        handToolbar.setBackHome(true,0);
        handToolbar.setTitle("我申请的(人员)");
        handToolbar.setTitleSize(18);
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
                if (peopleLeaveDetailBean != null && peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().size() > 0) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    L.e(TAG+"onSuccess:",peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().size()+peopleLeaveDetailBean.getApi_Get_MyApplyForPeo().get(0).toString());
                    hasMore = true;
                    entityList.addAll(peopleLeaveDetailBean.getApi_Get_MyApplyForPeo());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                    ivEmpty.setVisibility(View.VISIBLE);
                }
                pb.setVisibility(View.GONE);
                lv.completeRefresh();
            }

            @Override
            public void onError(String msg) throws Exception {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.completeRefresh();
                        pb.setVisibility(GONE);
                    }
                });
            }

            @Override
            public void onResponse(String response) throws Exception {
                ivEmpty.setVisibility(View.VISIBLE);
                lv.completeRefresh();
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
}
