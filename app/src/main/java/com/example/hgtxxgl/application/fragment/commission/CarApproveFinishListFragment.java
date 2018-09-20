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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.bean.car.CarApproveFinishBean;
import com.example.hgtxxgl.application.fragment.DetailFragment;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static com.example.hgtxxgl.application.R.id.iv_empty;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class CarApproveFinishListFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener{
    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "CarApproveFinishListFragment";
    private String tempIP;
    SimpleListView lv;
    private String authenticationNo;

    public CarApproveFinishListFragment(){

    }

    public static CarApproveFinishListFragment newInstance(Bundle bundle) {
        CarApproveFinishListFragment fragment = new CarApproveFinishListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private List<CarApproveFinishBean.ApiGetMyApproveForCarHisBean> entityList = new ArrayList<>();
    ListAdapter<CarApproveFinishBean.ApiGetMyApproveForCarHisBean> adapter = new ListAdapter<CarApproveFinishBean.ApiGetMyApproveForCarHisBean>
            ((ArrayList<CarApproveFinishBean.ApiGetMyApproveForCarHisBean>) entityList, R.layout.item_approve_people) {
        @Override
        public void bindView(ListAdapter.ViewHolder holder, CarApproveFinishBean.ApiGetMyApproveForCarHisBean bean) {
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
        loadData(beginNum, endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.people_approve_listview_libmain, null, false);
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

    public void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        CarApproveFinishBean.ApiGetMyApproveForCarHisBean carLeaveRrdBean = new CarApproveFinishBean.ApiGetMyApproveForCarHisBean();
        carLeaveRrdBean.setNo("?");
        carLeaveRrdBean.setAuthenticationNo(authenticationNo);
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setRegisterTime("?");
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
        carLeaveRrdBean.setCurrentApproverNo("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));
        carLeaveRrdBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        String json = new Gson().toJson(carLeaveRrdBean);
        String s = "Api_Get_MyApproveForCarHis " + json;
        L.e(TAG+"CarApproveFinishListFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestNewResultForm(tempIP, s, CarApproveFinishBean.class,new HttpManager.ResultNewCallback<CarApproveFinishBean>() {
            @Override
            public void onSuccess(String json, CarApproveFinishBean carApproveFinishBean) throws Exception {
                if (carApproveFinishBean != null && carApproveFinishBean.getApi_Get_MyApproveForCarHis().size() > 0 && carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(0) != null) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    for (int i = 0; i < carApproveFinishBean.getApi_Get_MyApproveForCarHis().size(); i++) {
                        if (carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(i).getApproverNo().contains(authenticationNo)){
                            L.e(TAG+"CarApproveFinishListFragment",carApproveFinishBean.toString());
                            hasMore = true;
                            entityList.add(carApproveFinishBean.getApi_Get_MyApproveForCarHis().get(i));
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
            loadData(1,10);
            adapter.notifyDataSetChanged();
        }
    }

    private DetailFragment.DataCallback callback;

    public CarApproveFinishListFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
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
    public void onLoadingMore() {
        if (hasMore) {
            beginNum += 10;
            endNum += 10;
            loadData(beginNum, endNum);
        }
        lv.completeRefresh();
    }

    @Override
    public void onScrollOutside() {

    }
}
