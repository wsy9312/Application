package com.example.hgtxxgl.application.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyLaunchFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private ProgressBar pb;

    public MyLaunchFragment() {

    }

    public static MyLaunchFragment newInstance() {
//        Bundle args = new Bundle();
//        NewFragment fragment = new NewFragment();
//        args.putInt(DetailFragment.ARG_TAB, tabIndex);
//        fragment.setArguments(args);
        MyLaunchFragment fragment = new MyLaunchFragment();
        return fragment;
    }

    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> entityList = new ArrayList<>();
    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> baseEntityList;

    ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean> adapter = new ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean>((ArrayList<PeopleLeaveEntity.PeopleLeaveRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, PeopleLeaveEntity.PeopleLeaveRrdBean bean) {
            holder.setText(R.id.tv_title, bean.getNo());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, bean.getContent());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(1,6);
    }

    SimpleListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_listview_libmain, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
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

    private static final String TAG = "MyLaunchFragment";
    void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();
        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo("?");
        peopleLeaveRrdBean.setContent("?");
        peopleLeaveRrdBean.setModifyTime("?");
        peopleLeaveRrdBean.setNoIndex("?");
        peopleLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean.setEndNum(String.valueOf(endNum));

        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(peopleLeaveRrdBean);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s = "get " + json;
//        Log.e(TAG, "loadData: "+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
                    if (beginNum==1 && endNum == 6){
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkItem(position);
//        if (lv.getCurrentState() == 2) return;
//        position -= 1;
//        checkDetail(position);
    }

    private void checkItem(int position) {
        if (lv.getCurrentState() == 2) return;
        checkDetail(position, PageConfig.PAGE_LEAVE_DETAIL_PEOPLE);
    }
    private void checkDetail(int position, int pageApplyBleave) {
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
        bundle.putString("no", adapter.getItem(position).getNo());
        bundle.putString("content", adapter.getItem(position).getContent());
        bundle.putString("modifyTime",adapter.getItem(position).getModifyTime());
        bundle.putString("outtime",adapter.getItem(position).getOutTime());
        bundle.putString("intime", adapter.getItem(position).getInTime());
        bundle.putString("bcancel",adapter.getItem(position).getBCancel());
        bundle.putString("bfillup",adapter.getItem(position).getBFillup());
        bundle.putString("noindex",adapter.getItem(position).getNoIndex());
        bundle.putString("multiLevelResult",adapter.getItem(position).getMultiLevelResult());
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

    public MyLaunchFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String key) {
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
            for (PeopleLeaveEntity.PeopleLeaveRrdBean bean : baseEntityList) {
                if (bean.getNo().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (bean.getContent().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss").replace(" ", "").contains(key)) {
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
        pb.setVisibility(View.VISIBLE);
        hasMore = true;
        beginNum = 1;
        endNum = 6;
        loadData(beginNum, endNum);
    }



    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }


}
