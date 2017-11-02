package com.example.hgtxxgl.application.fragment;

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
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class MyLaunchPeopleFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private int beginNum = 1;
    private int endNum = 10;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyLaunchPeopleFragment";

    public MyLaunchPeopleFragment() {

    }

    public static MyLaunchPeopleFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        MyLaunchPeopleFragment fragment = new MyLaunchPeopleFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> entityList = new ArrayList<>();
    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> baseEntityList;

    ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean> adapter = new ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean>
            ((ArrayList<PeopleLeaveEntity.PeopleLeaveRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, PeopleLeaveEntity.PeopleLeaveRrdBean bean) {
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, "申请事由:"+(bean.getContent().isEmpty()?"无":bean.getContent()));
            if (bean.getBCancel().equals("0")){
                if (bean.getProcess().equals("1")){
                    holder.setImageResource(R.id.image_flow,R.drawable.ic_done);
                    holder.setTextColor(R.id.tv_sketch, Color.rgb(0,128,0));
                }else{
                    holder.setImageResource(R.id.image_flow,R.drawable.ic_running);
                    holder.setTextColor(R.id.tv_sketch, Color.rgb(214,16,24));
                }
            }else{
                holder.setImageResource(R.id.image_flow,R.drawable.ic_canceled);
                holder.setTextColor(R.id.tv_sketch, Color.rgb(0,103,174));
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(beginNum, endNum);
    }

    SimpleListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (TextView) view.findViewById(R.id.iv_empty);
        ivEmpty.setText("当前无记录");
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

    void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        peopleLeaveRrdBean.setMultiLevelResult("?");
        peopleLeaveRrdBean.setProcess("?");
        peopleLeaveRrdBean.setLevelNum("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setRegisterTime("?");
        peopleLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean.setIsAndroid("1");
        peopleLeaveRrdBean.setBCancel("?");
        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s = "get " + json;
        //        String url = CommonValues.BASE_URL;
//        String url = ApplicationApp.getIP();
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "IP address is empty");
        HttpManager.getInstance().requestResultForm(tempIP, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
                    if (beginNum == 1 && endNum == 10){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(peopleLeaveEntity1.getPeopleLeaveRrd());
                    adapter.notifyDataSetChanged();
                } else {
                    hasMore = false;
                }
                pb.setVisibility(View.GONE);
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
                lv.completeRefresh();
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
        checkDetail(position, PageConfig.PAGE_LEAVE_DETAIL_PEOPLE);
    }

    private void checkDetail(int position, int pageApplyBleave) {
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
        bundle.putString("no", adapter.getItem(position).getNo());
        bundle.putString("outtime",adapter.getItem(position).getOutTime());
        bundle.putString("intime", adapter.getItem(position).getInTime());
        bundle.putString("content", adapter.getItem(position).getContent());
        bundle.putString("levelnum", adapter.getItem(position).getLevelNum());
        bundle.putString("process", adapter.getItem(position).getProcess());
        bundle.putString("multiLevelResult",adapter.getItem(position).getMultiLevelResult());
        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
        bundle.putInt("item",position);
//        bundle.putInt("tabIndex",tabIndex);
        intent.putExtra("data", bundle);
        startActivityForResult(intent,CommonValues.MYLAUN);
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

    public MyLaunchPeopleFragment setCallback(DetailFragment.DataCallback callback) {
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
            List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
            for (PeopleLeaveEntity.PeopleLeaveRrdBean bean : baseEntityList) {
                if ((bean.getContent().isEmpty()?"无":bean.getContent()).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if ((DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss")).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if ((bean.getProcess().equals("1")?"已完成":"未完成").replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getBCancel().equals("1")?"已取消":"").replace(" ", "").contains(key)){
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
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

}
