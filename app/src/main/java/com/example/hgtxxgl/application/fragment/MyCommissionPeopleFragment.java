package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
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
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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

public class MyCommissionPeopleFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener{

    private int beginNum = 1;
    private int endNum = 500;
    private boolean hasMore = true;
    private TextView ivEmpty;
    private ProgressBar pb;
    private static final String TAG = "MyCommissionPeopleFragment";

    public MyCommissionPeopleFragment() {

    }

    public static MyCommissionPeopleFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        MyCommissionPeopleFragment fragment = new MyCommissionPeopleFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> entityList = new ArrayList<>();
    private List<PeopleLeaveEntity.PeopleLeaveRrdBean> baseEntityList;

    ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean> adapter = new ListAdapter<PeopleLeaveEntity.PeopleLeaveRrdBean>
            ((ArrayList<PeopleLeaveEntity.PeopleLeaveRrdBean>) entityList, R.layout.layout_commission) {
        @Override
        public void bindView(ViewHolder holder, PeopleLeaveEntity.PeopleLeaveRrdBean bean) {
            holder.setText(R.id.tv_title, "申请人:"+bean.getName());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, "申请事由:"+(bean.getContent().isEmpty()?"无":bean.getContent()));
            if (bean.getBCancel().equals("0")){
                if (bean.getProcess().equals("1")){
                    holder.setImageResource(R.id.image_flow,R.drawable.ic_approved);
                }else if (bean.getProcess().equals("0")){
                    if (bean.getCurrentApproveNo().equals(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo())){
                        holder.setImageResource(R.id.image_flow,R.drawable.ic_no_approve);
                    }else {
                        holder.setImageResource(R.id.image_flow,R.drawable.ic_approved);
                    }
                }
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
        PeopleLeaveEntity peopleLeaveEntity = new PeopleLeaveEntity();

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean.setNo("?");
        peopleLeaveRrdBean.setCurrentApproveNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
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
        peopleLeaveRrdBean.setResult("?");

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean1 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean1.setApprover1No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        setBaseArgument(peopleLeaveRrdBean1);

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean2 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean2.setApprover2No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        setBaseArgument(peopleLeaveRrdBean2);

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean3 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean3.setApprover3No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        setBaseArgument(peopleLeaveRrdBean3);

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean4 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean4.setApprover4No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        setBaseArgument(peopleLeaveRrdBean4);

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean5 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean5.setApprover5No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        setBaseArgument(peopleLeaveRrdBean5);

        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(0,peopleLeaveRrdBean);
        list.add(1,peopleLeaveRrdBean1);
        list.add(2,peopleLeaveRrdBean2);
        list.add(3,peopleLeaveRrdBean3);
        list.add(4,peopleLeaveRrdBean4);
        list.add(5,peopleLeaveRrdBean5);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String json = new Gson().toJson(peopleLeaveEntity);
        String s = "get " + json;
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
                    if (beginNum == 1 && endNum == 500){
                        entityList.clear();
                    }
                    for (int i = beginNum - 1; i < endNum + 1; i++) {
                        if (peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getBCancel().equals("0")){
                            PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                            PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                            peopleInfoBean.setNo(peopleLeaveEntity1.getPeopleLeaveRrd().get(i).getNo());
                            peopleInfoBean.setName("?");
                            peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                            peopleInfoBean.setIsAndroid("1");
                            List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                            beanList.add(peopleInfoBean);
                            peopleEntity.setPeopleInfo(beanList);
                            String json1 = new Gson().toJson(peopleEntity);
                            String s1 = "get " + json1;
                            final int finalI = i;
                            HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
                                @Override
                                public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                                    if (peopleInfoEntity != null){
                                        peopleLeaveEntity1.getPeopleLeaveRrd().get(finalI).setName(peopleInfoEntity.getPeopleInfo().get(0).getName());
                                        entityList.add(peopleLeaveEntity1.getPeopleLeaveRrd().get(finalI));
                                        Comparator<PeopleLeaveEntity.PeopleLeaveRrdBean> comparator = new Comparator<PeopleLeaveEntity.PeopleLeaveRrdBean>() {
                                            @Override
                                            public int compare(PeopleLeaveEntity.PeopleLeaveRrdBean o1, PeopleLeaveEntity.PeopleLeaveRrdBean o2) {
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

    private void setBaseArgument(PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean1){
        peopleLeaveRrdBean1.setNo("?");
        peopleLeaveRrdBean1.setMultiLevelResult("?");
        peopleLeaveRrdBean1.setProcess("?");
        peopleLeaveRrdBean1.setLevelNum("?");
        peopleLeaveRrdBean1.setContent("?");
        peopleLeaveRrdBean1.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean1.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean1.setNoIndex("?");
        peopleLeaveRrdBean1.setModifyTime("?");
        peopleLeaveRrdBean1.setRegisterTime("?");
        peopleLeaveRrdBean1.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean1.setIsAndroid("1");
        peopleLeaveRrdBean1.setBCancel("?");
        peopleLeaveRrdBean1.setResult("?");
        peopleLeaveRrdBean1.setCurrentApproveNo("?");
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
        checkDetail(position, PageConfig.PAGE_LEAVE_APPROVE_PEOPLE);
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

        }
    }

    private DetailFragment.DataCallback callback;

    public MyCommissionPeopleFragment setCallback(DetailFragment.DataCallback callback) {
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
                if (bean.getName().replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getCurrentApproveNo().equals(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo())?"未审批":"已审批")
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
