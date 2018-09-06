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
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
import com.example.hgtxxgl.application.fragment.DetailFragment;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.R.id.iv_empty;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;
//车辆审批列表
public class MyCommissionCarFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{
    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyCommissionCarFragment";
    private String tempIP;
    SimpleListView lv;
    private String authenticationNo;

    public MyCommissionCarFragment() {

    }

    public static MyCommissionCarFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        MyCommissionCarFragment fragment = new MyCommissionCarFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<CarLeaveEntity.CarLeaveRrdBean> entityList = new ArrayList<>();
    private List<CarLeaveEntity.CarLeaveRrdBean> baseEntityList;

    ListAdapter<CarLeaveEntity.CarLeaveRrdBean> adapter = new ListAdapter<CarLeaveEntity.CarLeaveRrdBean>
            ((ArrayList<CarLeaveEntity.CarLeaveRrdBean>) entityList, R.layout.layout_commission) {
        @Override
        public void bindView(ViewHolder holder, CarLeaveEntity.CarLeaveRrdBean bean) {
            holder.setText(R.id.tv_title, "申请人:"+bean.getName());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, "申请事由:"+(bean.getContent().isEmpty()?"无":bean.getContent()));
            holder.setImageResource(R.id.image_flow,bean.getApproverNo().contains(authenticationNo)?R.drawable.ic_approved:R.drawable.ic_no_approve);
            holder.setTextColor(R.id.tv_sketch,bean.getApproverNo().contains(authenticationNo)? Color.rgb(0,128,0):Color.rgb(214,16,24));
            holder.setTextColor(R.id.tv_title,bean.getApproverNo().contains(authenticationNo)? Color.rgb(0,128,0):Color.rgb(214,16,24));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationNo = ApplicationApp.getNewLoginEntity().getApi_Add_Login().get(0).getAuthenticationNo();
        loadData(beginNum, endNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(iv_empty);
        ivEmpty.setText(R.string.no_current_record);
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
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setNo("?");//1
        carLeaveRrdBean.setAuthenticationNo(authenticationNo);//2
        carLeaveRrdBean.setIsAndroid("1");//3
        carLeaveRrdBean.setRegisterTime("?");//4
        carLeaveRrdBean.setOutTime("?");//5
        carLeaveRrdBean.setInTime("?");//6
        carLeaveRrdBean.setContent("?");//7
        carLeaveRrdBean.setModifyTime("?");//8
        carLeaveRrdBean.setProcess("?");//9
        carLeaveRrdBean.setBCancel("0");//10
        carLeaveRrdBean.setBFillup("?");//11
        carLeaveRrdBean.setNoIndex("?");//12
        carLeaveRrdBean.setResult("?");//13
        carLeaveRrdBean.setApproverNo("?");//15
        carLeaveRrdBean.setHisAnnotation("?");//16
        carLeaveRrdBean.setDestination("?");//17
        carLeaveRrdBean.setCarNo("?");
        carLeaveRrdBean.setDriverNo("?");
        carLeaveRrdBean.setDriverName("?");
        carLeaveRrdBean.setLeaderNo("?");
        carLeaveRrdBean.setLeaderName("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));//18
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));//19
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String json = new Gson().toJson(carLeaveEntity);
        final String s = "get " + json;
        L.e(TAG+"MyCommissionCarFragment",s);
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestResultForm(tempIP, s, CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                if (carLeaveEntity1 != null && carLeaveEntity1.getCarLeaveRrd().size() > 0) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    L.e(TAG+"MyCommissionCarFragment",carLeaveEntity1.toString());
                    hasMore = true;
                    entityList.addAll(carLeaveEntity1.getCarLeaveRrd());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                }
                pb.setVisibility(View.GONE);
                ivEmpty.setVisibility(View.GONE);
                lv.completeRefresh();
            }

            @Override
            public void onFailure(String msg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.completeRefresh();
                        pb.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(String response) {
                ivEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position, PageConfig.PAGE_LEAVE_APPROVE_CAR);
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
        bundle.putInt("item",position);
//        bundle.putInt("tabIndex",tabIndex);
        intent.putExtra("data", bundle);
        startActivityForResult(intent,CommonValues.MYCOMM);
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

    public MyCommissionCarFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }


    public void filter(String key) {
        if (lv == null || key == null) return;
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
            for (CarLeaveEntity.CarLeaveRrdBean bean : baseEntityList) {
                if (bean.getName().replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getApproverNo().contains(authenticationNo)?"已审批":"未审批")
                        .replace(" ","").contains(key)){
                    list.add(bean);
                }
                if ((DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss")).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if ((bean.getContent().isEmpty()?"无":bean.getContent()).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
            }
            entityList.clear();
            entityList.addAll(list);
            adapter.notifyDataSetChanged();
        } else if (entityList != null) {
            if (baseEntityList != null) {
                entityList.clear();
                entityList.addAll(baseEntityList);
                adapter.notifyDataSetChanged();
            }
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
    public void onLoadingMore() {
        if (hasMore) {
            beginNum += 10;
            endNum += 10;
            loadData(beginNum, endNum);
        }
        lv.completeRefresh();
        ivEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onScrollOutside() {

    }

}
