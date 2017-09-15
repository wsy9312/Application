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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.entity.PeopleLeaveEntity;
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
import java.util.Collections;
import java.util.Comparator;
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
                    if (bean.getResult().equals("1")){
                        L.e(TAG,"123312---"+bean.getLevelNum());
                        L.e(TAG,bean.getMultiLevelResult());
                        holder.setImageResource(R.id.image_flow,R.drawable.ic_approved);
                    }else {
                        holder.setImageResource(R.id.image_flow,R.drawable.ic_no_approve);
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

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean1 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean1.setNo("?");
        peopleLeaveRrdBean1.setApprover1No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
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

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean2 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean2.setNo("?");
        peopleLeaveRrdBean2.setApprover2No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean2.setMultiLevelResult("?");
        peopleLeaveRrdBean2.setProcess("?");
        peopleLeaveRrdBean2.setLevelNum("?");
        peopleLeaveRrdBean2.setContent("?");
        peopleLeaveRrdBean2.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean2.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean2.setNoIndex("?");
        peopleLeaveRrdBean2.setModifyTime("?");
        peopleLeaveRrdBean2.setRegisterTime("?");
        peopleLeaveRrdBean2.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean2.setIsAndroid("1");
        peopleLeaveRrdBean2.setBCancel("?");
        peopleLeaveRrdBean2.setResult("?");

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean3 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean3.setNo("?");
        peopleLeaveRrdBean3.setApprover3No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean3.setMultiLevelResult("?");
        peopleLeaveRrdBean3.setProcess("?");
        peopleLeaveRrdBean3.setLevelNum("?");
        peopleLeaveRrdBean3.setContent("?");
        peopleLeaveRrdBean3.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean3.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean3.setNoIndex("?");
        peopleLeaveRrdBean3.setModifyTime("?");
        peopleLeaveRrdBean3.setRegisterTime("?");
        peopleLeaveRrdBean3.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean3.setIsAndroid("1");
        peopleLeaveRrdBean3.setBCancel("?");
        peopleLeaveRrdBean3.setResult("?");

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean4 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean4.setNo("?");
        peopleLeaveRrdBean4.setApprover4No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean4.setMultiLevelResult("?");
        peopleLeaveRrdBean4.setProcess("?");
        peopleLeaveRrdBean4.setLevelNum("?");
        peopleLeaveRrdBean4.setContent("?");
        peopleLeaveRrdBean4.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean4.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean4.setNoIndex("?");
        peopleLeaveRrdBean4.setModifyTime("?");
        peopleLeaveRrdBean4.setRegisterTime("?");
        peopleLeaveRrdBean4.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean4.setIsAndroid("1");
        peopleLeaveRrdBean4.setBCancel("?");
        peopleLeaveRrdBean4.setResult("?");

        PeopleLeaveEntity.PeopleLeaveRrdBean peopleLeaveRrdBean5 = new PeopleLeaveEntity.PeopleLeaveRrdBean();
        peopleLeaveRrdBean5.setNo("?");
        peopleLeaveRrdBean5.setApprover5No(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean5.setMultiLevelResult("?");
        peopleLeaveRrdBean5.setProcess("?");
        peopleLeaveRrdBean5.setLevelNum("?");
        peopleLeaveRrdBean5.setContent("?");
        peopleLeaveRrdBean5.setBeginNum(String.valueOf(beginNum));
        peopleLeaveRrdBean5.setEndNum(String.valueOf(endNum));
        peopleLeaveRrdBean5.setNoIndex("?");
        peopleLeaveRrdBean5.setModifyTime("?");
        peopleLeaveRrdBean5.setRegisterTime("?");
        peopleLeaveRrdBean5.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
        peopleLeaveRrdBean5.setIsAndroid("1");
        peopleLeaveRrdBean5.setBCancel("?");
        peopleLeaveRrdBean5.setResult("?");

        List<PeopleLeaveEntity.PeopleLeaveRrdBean> list = new ArrayList<>();
        list.add(0,peopleLeaveRrdBean1);
        list.add(1,peopleLeaveRrdBean2);
        list.add(2,peopleLeaveRrdBean3);
        list.add(3,peopleLeaveRrdBean4);
        list.add(4,peopleLeaveRrdBean5);
        peopleLeaveEntity.setPeopleLeaveRrd(list);
        String json = new Gson().toJson(peopleLeaveEntity);
        final String s = "get " + json;
        L.e(TAG, "loadData()查看申请记录"+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, PeopleLeaveEntity.class,new HttpManager.ResultCallback<PeopleLeaveEntity>() {
            @Override
            public void onSuccess(final String json, final PeopleLeaveEntity peopleLeaveEntity1) throws InterruptedException {
                if (peopleLeaveEntity1 != null && peopleLeaveEntity1.getPeopleLeaveRrd().size() > 0) {
                    if (beginNum == 1 && endNum == 500){
                        entityList.clear();
                    }
                    for (int i = beginNum-1; i < endNum+1; i++) {
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
                            L.e(TAG,"1名字："+s1);
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
                                                int i1 = Integer.parseInt(o1.getNoIndex());
                                                int i2 = Integer.parseInt(o2.getNoIndex());
                                                if (i1 != i2) {
                                                    return i2 - i1;
                                                }
                                                return 0;
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
                    hasMore = true;
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
                if (("申请人:"+bean.getName()).replace(" ", "").contains(key)){
                    list.add(bean);
                }
                if ((bean.getResult().equals("1")?"已审批":"未审批").replace(" ","").contains(key)){
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
        loadData(beginNum, endNum);
        adapter.notifyDataSetChanged();
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