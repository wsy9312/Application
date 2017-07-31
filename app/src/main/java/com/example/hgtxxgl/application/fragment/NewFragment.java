package com.example.hgtxxgl.application.fragment;

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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment implements SimpleListView.OnRefreshListener, AdapterView.OnItemClickListener {
//    private int tabIndex;
    private int beginNum = 1;
    private int endNum = 6;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private ProgressBar pb;

    public NewFragment() {

    }

    public static NewFragment newInstance() {
//        Bundle args = new Bundle();
//        NewFragment fragment = new NewFragment();
//        args.putInt(DetailFragment.ARG_TAB, tabIndex);
//        fragment.setArguments(args);
        NewFragment fragment = new NewFragment();
        return fragment;
    }

    private List<NewsInfoEntity.NewsRrdBean> entityList = new ArrayList<>();
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;

    ListAdapter<NewsInfoEntity.NewsRrdBean> adapter = new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean bean) {
            holder.setText(R.id.tv_title, bean.getTitle());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, bean.getContent());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        for (int i = 0; i < 2; i++) {
            if (entityList.isEmpty()){
                loadData(1,6);
            }else{
                return;
            }
        }

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

    private static final String TAG = "NewFragment";
    void loadData(final int beginNum, final int endNum) {
        if (callback != null) {
            callback.onLoadData();
        }
        NewsInfoEntity newsInfoEntity = new NewsInfoEntity();
        NewsInfoEntity.NewsRrdBean newsRrdBean = new NewsInfoEntity.NewsRrdBean();
        newsRrdBean.setTitle("?");
        newsRrdBean.setContent("?");
        newsRrdBean.setModifyTime("?");
        newsRrdBean.setPicture1("?");
//        newsRrdBean.setPicture2("?");
//        newsRrdBean.setPicture3("?");
//        newsRrdBean.setPicture4("?");
//        newsRrdBean.setPicture5("?");
        newsRrdBean.setBeginNum(beginNum+"");
        newsRrdBean.setEndNum(endNum+"");
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsRrdBean);
        newsInfoEntity.setNewsRrd(list);
        String json = new Gson().toJson(newsInfoEntity);
        String s = "get " + json;
        Log.e(TAG, "loadData: "+s);
        String url = CommonValues.BASE_URL;
        HttpManager.getInstance().requestResultForm(url, s, NewsInfoEntity.class, "",new HttpManager.ResultCallback<NewsInfoEntity>() {
                    @Override
                    public void onSuccess(final String json, final NewsInfoEntity newsInfoEntity) throws InterruptedException {
                        if (newsInfoEntity != null && newsInfoEntity.getNewsRrd().size() > 0) {
                            if (beginNum == 1 && endNum == 6){
                            entityList.clear();
                            }
                            hasMore = true;
                            entityList.addAll(newsInfoEntity.getNewsRrd());
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
        position -= 1;
        checkDetail(position);

    }
        private void checkDetail(int position) {
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
//        Bundle bundle = new Bundle();
        intent.putExtra("title", adapter.getItem(position).getTitle());
        intent.putExtra("content", adapter.getItem(position).getContent());
        intent.putExtra("modifyTime",adapter.getItem(position).getModifyTime());
//        intent.putExtra("picture1", adapter.getItem(position).getPicture1());
//        intent.putExtra("picture2", adapter.getItem(position).getPicture2());
//        intent.putExtra("picture3", adapter.getItem(position).getPicture3());
//        intent.putExtra("picture4", adapter.getItem(position).getPicture4());
//        intent.putExtra("picture5", adapter.getItem(position).getPicture5());
//        intent.putExtra("data", bundle);
        startActivity(intent);
    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (lv.getCurrentState() == 2) return;
//        position -= 1;
//        MyCommissionListEntity.RetDataBean item = adapter.getItem(position);
//        String procName = item.getProcessName();
//        String activityNameEN = item.getActivityNameEN();
//        boolean isRework = activityNameEN.equals("Rework");
//        if (isRework && tabIndex == 0) {
//            Toast.makeText(getActivity(), "退回的申请，请重新填写", Toast.LENGTH_LONG).show();
//        }
//        if (tabIndex == 0) {
//            if (procName.equals("PaymentRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_PAYMENT_FLOW, true,tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_PAYMENT_FLOW, false, tabIndex);
//                }
//            } else if (procName.equals("ExpenseRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_EXPENSE_OFFER, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_EXPENSE_OFFER, false, tabIndex);
//                }
//            } else if (procName.equals("BusinessTripRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_BLEAVE, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_BLEAVE, false, tabIndex);
//                }
//            } else if (procName.equals("EntertainmentExpenseRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_ENTERTAINMENT_EXPENSE, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_ENTER_EXPENSE, false, tabIndex);
//
//                }
//            } else if (procName.equals("OverTimeRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_EXTRAWORK, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_EXTRAWORK, false, tabIndex);
//                }
//            } else if (procName.equals("LeaveRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_REST, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_REST, false, tabIndex);
//                }
//
//            } else if (procName.equals("TravelExpenseRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_APPLY_TRAVEL_OFFER, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_TRAVEL, false, tabIndex);
//                }
//
//            }else if (procName.equals("FileRequest")) {
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_DISPLAY_POST_FILE, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_POST_FILE, false, tabIndex);
//                }
//
//            }else{
//                if (isRework) {
//                    checkDetail(position, PageConfig.PAGE_DISPLAY_UNIFIED, true, tabIndex);
//                } else {
//                    checkDetail(position, PageConfig.PAGE_APPROVE_UNIFIED, false, tabIndex);
//                }
//            }
//
//        } else if (tabIndex == 1) {
//            if (procName.equals("BusinessTripRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_BLEAVE, false, tabIndex);
//
//                //加班明细
//            } else if (procName.equals("OverTimeRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_OVERTIME, false, tabIndex);
//
//                //请假明细
//            } else if (procName.equals("LeaveRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_REST, false, tabIndex);
//
//            } else if (procName.equals("ExpenseRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_EXPENSE, false, tabIndex);
//
//            }//付款明细
//            else if (procName.equals("PaymentRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_PAYMENT, false, tabIndex);
//
//            }//差旅
//            else if (procName.equals("TravelExpenseRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_TRAVEL, false, tabIndex);
//            }//招待费
//            else if (procName.equals("EntertainmentExpenseRequest")) {
//
//                checkDetail(position, PageConfig.PAGE_DISPLAY_EXPENSE_OFFER, false, tabIndex);
//
//            }//发文明细
//            else if(procName.equals("FileRequest")){
//                checkDetail(position, PageConfig.PAGE_DISPLAY_POST_FILE,false, tabIndex);
//            }else {
//                checkDetail(position, PageConfig.PAGE_DISPLAY_UNIFIED, false, tabIndex);
//            }
//        }
//    }

//    private void checkDetail(int position, int pageCode, boolean remak, int tabIndex) {
//        Intent intent = new Intent(getActivity(), ItemActivity.class);
//        intent.putExtra(PageConfig.PAGE_CODE, pageCode);
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", GeelyApp.getLoginEntity().getUserId());
//        bundle.putString("barCode", adapter.getItem(position).getBarCode());
//        bundle.putString("SubmitBy",adapter.getItem(position).getSubmitBy());
//        bundle.putString("workflowType", adapter.getItem(position).getProcessName());
//        bundle.putString("ProcessNameCN",adapter.getItem(position).getProcessNameCN());
//        bundle.putString("WorkflowIdentifier",adapter.getItem(position).getWorkflowIdentifier());
//        bundle.putBoolean("Remak",remak);
//        bundle.putInt("item",position);
//        bundle.putInt("tabIndex",tabIndex);
//        bundle.putString("SN", adapter.getItem(position).getProcInstID() + "_" + adapter.getItem(position).getActInstDestID());
//        intent.putExtra("data", bundle);
//        startActivityForResult(intent,CommonValues.MYCOMM);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CommonValues.MYCOMM){
//            if (resultCode == Activity.RESULT_OK){
//                final int item = data.getExtras().getInt("item");
//                final int tabIndex = data.getExtras().getInt("tabIndex");
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (tabIndex == 0){
//                            entityList.remove(item);
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//
//            }
//        }
//    }

    private DetailFragment.DataCallback callback;

    public NewFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String key) {
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
            for (NewsInfoEntity.NewsRrdBean bean : baseEntityList) {
                if (bean.getTitle().replace(" ", "").contains(key)) {
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