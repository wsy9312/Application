package com.example.hgtxxgl.application.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.entity.CarLeaveEntity;
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

/**
 * Created by HGTXxgl on 2017/8/23.
 */
public class MyLaunchCarFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener{

    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyLaunchCarFragment";

    public MyLaunchCarFragment() {

    }

    public static MyLaunchCarFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        MyLaunchCarFragment fragment = new MyLaunchCarFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<CarLeaveEntity.CarLeaveRrdBean> entityList = new ArrayList<>();
    private List<CarLeaveEntity.CarLeaveRrdBean> baseEntityList;

    ListAdapter<CarLeaveEntity.CarLeaveRrdBean> adapter = new ListAdapter<CarLeaveEntity.CarLeaveRrdBean>((ArrayList<CarLeaveEntity.CarLeaveRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, CarLeaveEntity.CarLeaveRrdBean bean) {
            holder.setText(R.id.tv_title, "审批进度:"+(bean.getProcess().equals("1")?"已结束":"未结束"));
            holder.setText(R.id.tv_date, "修改时间:"+ DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, bean.getContent().isEmpty()?"请假原因:无":"请假原因:"+bean.getContent());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        carLeaveRrdBean.setProcess("?");
        carLeaveRrdBean.setContent("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));
        carLeaveRrdBean.setNoIndex("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        List<CarLeaveEntity.CarLeaveRrdBean> list = new ArrayList<>();
        list.add(carLeaveRrdBean);
        carLeaveEntity.setCarLeaveRrd(list);
        String json = new Gson().toJson(carLeaveEntity);
        final String s = "get " + json;
        Log.e(TAG, "loadData()查看申请记录"+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, CarLeaveEntity.class,new HttpManager.ResultCallback<CarLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final CarLeaveEntity carLeaveEntity1) throws InterruptedException {
                if (carLeaveEntity1 != null && carLeaveEntity1.getCarLeaveRrd().size() > 0) {
                    if (beginNum==1 && endNum == 6){
                        entityList.clear();
                    }
                    hasMore = true;
                    entityList.addAll(carLeaveEntity1.getCarLeaveRrd());
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
            beginNum += 6;
            endNum += 6;
            loadData(beginNum, endNum);
        } else {
            lv.completeRefresh();
        }
    }

    @Override
    public void onPullRefresh() {
        hasMore = true;
        beginNum = 1;
        endNum = 6;
        loadData(beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lv.getCurrentState() == 2) return;
        position -= 1;
        checkDetail(position, PageConfig.PAGE_LEAVE_DETAIL_CAR);
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
        bundle.putString("bcancel",adapter.getItem(position).getbCancel());
        bundle.putString("bfillup",adapter.getItem(position).getbFillup());
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
            if (resultCode == Activity.RESULT_OK){
                final int item = data.getExtras().getInt("item");
//                final int tabIndex = data.getExtras().getInt("tabIndex");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if (tabIndex == 0){
                        entityList.remove(item);
                        adapter.notifyDataSetChanged();
//                        }
                    }
                });

            }
        }
    }

    private DetailFragment.DataCallback callback;

    public MyLaunchCarFragment setCallback(DetailFragment.DataCallback callback) {
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
                if (("审批进度:"+(bean.getProcess().equals("1")?"已结束":"未结束")).replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getContent().isEmpty()?"请假原因:无":"请假原因:"+bean.getContent()).replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (("修改时间:"+DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss")).replace(" ", "").contains(key)) {
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