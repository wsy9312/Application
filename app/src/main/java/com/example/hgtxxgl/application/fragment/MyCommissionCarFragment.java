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
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.utils.DateUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.hgtxxgl.application.R.id.iv_empty;

public class MyCommissionCarFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{
    private int beginNum = 1;
    private int endNum = 500;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyCommissionCarFragment";

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
            if (bean.getbCancel().equals("0")){
                holder.setImageResource(R.id.image_flow,bean.getProcess().equals("1")?R.drawable.ic_approved:R.drawable.ic_no_approve);
            }
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
        ivEmpty = (TextView) view.findViewById(iv_empty);
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

    public void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        CarLeaveEntity carLeaveEntity = new CarLeaveEntity();
        CarLeaveEntity.CarLeaveRrdBean carLeaveRrdBean = new CarLeaveEntity.CarLeaveRrdBean();
        carLeaveRrdBean.setbCancel("?");
        carLeaveRrdBean.setNo("?");
        carLeaveRrdBean.setApproverNo(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        carLeaveRrdBean.setProcess("?");
        carLeaveRrdBean.setContent("?");
        carLeaveRrdBean.setBeginNum(String.valueOf(beginNum));
        carLeaveRrdBean.setEndNum(String.valueOf(endNum));
        carLeaveRrdBean.setNoIndex("?");
        carLeaveRrdBean.setModifyTime("?");
        carLeaveRrdBean.setRegisterTime("?");
        carLeaveRrdBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        carLeaveRrdBean.setIsAndroid("1");
        carLeaveRrdBean.setResult("?");
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
                    if (beginNum == 1 && endNum == 500){
                        entityList.clear();
                    }
                    for (int i = beginNum-1; i < endNum+1; i++) {
                        if (carLeaveEntity1.getCarLeaveRrd().get(i).getbCancel().equals("0")){
                            PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                            PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                            peopleInfoBean.setNo(carLeaveEntity1.getCarLeaveRrd().get(i).getNo());
                            peopleInfoBean.setName("?");
                            peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                            peopleInfoBean.setIsAndroid("1");
                            List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                            beanList.add(peopleInfoBean);
                            peopleEntity.setPeopleInfo(beanList);
                            String json1 = new Gson().toJson(peopleEntity);
                            String s1 = "get " + json1;
                            Log.e(TAG,"1名字："+s1);
                            final int finalI = i;
                            HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
                                @Override
                                public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                                    if (peopleInfoEntity != null){
                                        carLeaveEntity1.getCarLeaveRrd().get(finalI).setName(peopleInfoEntity.getPeopleInfo().get(0).getName());
                                        entityList.add(carLeaveEntity1.getCarLeaveRrd().get(finalI));
                                        Comparator<CarLeaveEntity.CarLeaveRrdBean> comparator = new Comparator<CarLeaveEntity.CarLeaveRrdBean>() {
                                            @Override
                                            public int compare(CarLeaveEntity.CarLeaveRrdBean o1, CarLeaveEntity.CarLeaveRrdBean o2) {
                                                Date date1 = DateUtil.stringToDate(o1.getRegisterTime());
                                                Date date2 = DateUtil.stringToDate(o2.getRegisterTime());
                                                if (date1.before(date2)) {
                                                    return 1;
                                                }
                                                return -1;
                                            }
                                        } ;
                                        Collections.sort(entityList, comparator);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(String msg) {

                                }

                                @Override
                                public void onResponse(String response) {

                                }
                            });
                        }
                    }

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
            }
        });
    }

    private void loadMore() {
//        if (hasMore) {
//            beginNum += 10;
//            endNum += 10;
////            loadData(beginNum, endNum);
//            lv.completeRefresh();
//        } else {
//            lv.completeRefresh();
//        }
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
        bundle.putString("bcancel",adapter.getItem(position).getbCancel());
        bundle.putString("bfillup",adapter.getItem(position).getbFillup());
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
            if (beginNum == 1 && endNum == 500){
                entityList.clear();
            }
            loadData(1,500);
            adapter.notifyDataSetChanged();
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
                if ((bean.getProcess().equals("1")?"已审批":"未审批").replace(" ","").contains(key)){
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
        if (beginNum == 1 && endNum == 500){
            entityList.clear();
        }
        loadData(beginNum, endNum);
        lv.completeRefresh();
    }

    @Override
    public void onLoadingMore() {
        lv.completeRefresh();
//        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }
}
