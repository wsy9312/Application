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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.NewsInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CacheManger;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.GsonUtil;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.SimpleListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//原本的我的待办fragment,现在的新闻中心
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener {

    private int tabIndex;
    private int index = 1;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private TextView tvEmpty;
    private Button btnEmpty;
    private ProgressBar pb;
    public Map<String, Object> map;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }


    //1原本的外部网络数据集合变量
//    private List<MyCommissionListEntity.RetDataBean> entityList = new ArrayList<>();
//    private List<MyCommissionListEntity.RetDataBean> baseEntityList;

    //2我的待办里条目的数据绑定显示
//    ListAdapter<MyCommissionListEntity.RetDataBean> adapter =
//            new ListAdapter<MyCommissionListEntity.RetDataBean>((ArrayList<MyCommissionListEntity.RetDataBean>) entityList, R.layout.layout_my_todo_too) {
//        @Override
//        public void bindView(ViewHolder holder, MyCommissionListEntity.RetDataBean obj) {
//            holder.setText(R.id.tv_name, obj.getApplicantName());
//            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getUpdateTime(), "yyyy-MM-dd HH:mm"));
//            holder.setText(R.id.tv_message, obj.getSummary());
//            holder.setText(R.id.tv_type, obj.getProcessNameCN());
//        }
//    };

//    private List<Map<String, Object>> entityList = getData();
//    private List<Map<String, Object>> baseEntityList;

    private List<NewsInfoEntity.NewsRrdBean> entityList = getData();
    private List<NewsInfoEntity.NewsRrdBean> baseEntityList;
        ListAdapter<NewsInfoEntity.NewsRrdBean> adapter =
            new ListAdapter<NewsInfoEntity.NewsRrdBean>((ArrayList<NewsInfoEntity.NewsRrdBean>) entityList, R.layout.layout_my_todo_too) {
        @Override
        public void bindView(ViewHolder holder, NewsInfoEntity.NewsRrdBean obj) {
            holder.setText(R.id.tv_title, obj.getTitle());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getModifyTime(), "yyyy-MM-dd HH:mm:ss"));
            holder.setText(R.id.tv_sketch, obj.getContext());
        }
    };
    //填充预留的假数据
    public List<NewsInfoEntity.NewsRrdBean> getData(){
        String data = CacheManger.getInstance().getData(CommonValues.BASE_URL_NEWS_SAVE);
        NewsInfoEntity newsInfoEntity = GsonUtil.parseJsonToBean(data, NewsInfoEntity.class);
        String s = newsInfoEntity.toString();
        Log.e(TAG, s);
        boolean empty = newsInfoEntity.getNewsRrd().isEmpty();
        if (empty){
            ToastUtil.showToast(ApplicationApp.context,"为空");
        }
        String title = newsInfoEntity.getNewsRrd().get(0).getTitle();
        String context = newsInfoEntity.getNewsRrd().get(0).getContext();
        String modifyTime = newsInfoEntity.getNewsRrd().get(0).getModifyTime();
        Log.e(TAG, modifyTime);
        List<NewsInfoEntity.NewsRrdBean> list = new ArrayList<>();
        list.add(newsInfoEntity.getNewsRrd().get(0));
        return list;

//        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
//        for (int i = 0; i < 100; i++) {
//            map = new HashMap<String, Object>();
//            map.put("title", "这是一个标题"+i);
//            map.put("sketch", "这是一个简述"+i);
////            map.put("message", "内容" + i);
////            map.put("image",file);
//            map.put("date", "年月日" + i);
//            list.add(map);
//        }
//        return list;
    }
//    ListAdapter<Map<String, Object>> adapter = new ListAdapter<Map<String, Object>>((ArrayList<Map<String, Object>>) entityList, R.layout.layout_my_todo_too){
//
//        @Override
//        public void bindView(ViewHolder holder, Map<String, Object> map) {
//            holder.setText(R.id.tv_title, map.get("title").toString());
//            holder.setText(R.id.tv_date, map.get("date").toString());
////            holder.setImageResource(R.id.iv_avatar, BitmapFactory.decodeFile(map.get("image").toString()));
//
////            holder.setText(R.id.tv_message, map.get("message").toString());
//            holder.setText(R.id.tv_sketch, map.get("sketch").toString());
//        }
//    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB);
        loadData(tabIndex, index, 10);
    }
    SimpleListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_viewpage_page_content, null, false);
        lv = (SimpleListView) view.findViewById(R.id.viewpager_listview);
        ivEmpty = (ImageView) view.findViewById(R.id.iv_empty);
        tvEmpty = (TextView) view.findViewById(R.id.tv_reload);
        btnEmpty = (Button) view.findViewById(R.id.btn_reload);
        pb = (ProgressBar) view.findViewById(R.id.mycommission_pb);
        //3设置adapter从2
        lv.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (adapter.getCount() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
//                    tvEmpty.setVisibility(View.VISIBLE);
//                    btnEmpty.setVisibility(View.VISIBLE);
//                    btnEmpty.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            boolean networkConnected = NetworkHttpManager.isNetworkConnected(getContext());
//                            boolean mobileConnected = NetworkHttpManager.isMobileConnected(getContext());
//                            boolean wifiConnected = NetworkHttpManager.isWifiConnected(getContext());
//                            if (networkConnected){
//                                Log.e("NET", "有网");
//                            }
//                            if (mobileConnected){
//                                Log.e("NET", "手机有网");
//                            }
//                            if (wifiConnected){
//                                Log.e("NET", "wifi");
//                            }
//                        }
//                    });
                } else {
                    ivEmpty.setVisibility(View.GONE);
//                    tvEmpty.setVisibility(View.GONE);
//                    btnEmpty.setVisibility(View.GONE);
                }
                pb.setVisibility(View.GONE);
                super.onChanged();
            }
        });
        lv.setOnRefreshListener(this);
        lv.setOnItemClickListener(this);
        return view;
    }

    //根据顶部菜单按钮点击切换到对应的功能模块
    void loadData(int tabIndex, final int pageIndex, int pageSize) {
/*
        //4获取网络数据到指定的条目当中
        if (callback != null) {
            callback.onLoadData();
        }
        HashMap<String, Object> map = (HashMap<String, Object>) CommonValues.getCommonParams(getActivity());
        //10运行出错,设定假数据
        map.put("userId", "123");
        map.put("keyWord", "");
        String url = null;
        if (tabIndex == 0) {
            url = CommonValues.REQ_WAITING_TODO_NOT_FINSHED;
        } else if (tabIndex == 1) {
            url = CommonValues.REQ_WAITING_TODO_FINSHED;
        }
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        if (map.get("userId") != null){
            HttpManager.getInstance().requestResultForm(url, map, MyCommissionListEntity.class, new HttpManager.ResultCallback<MyCommissionListEntity>() {
                @Override
                public void onSuccess(String content, final MyCommissionListEntity entity) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (entity != null && entity.getRetData().size() > 0) {
                                if (pageIndex == 1){
                                    entityList.clear();
                                }
                                hasMore = true;
                                entityList.addAll(entity.getRetData());
                                adapter.notifyDataSetChanged();
                            } else {
                                hasMore = false;
                            }
                            pb.setVisibility(View.GONE);
                            lv.completeRefresh();
                        }
                    });
                }

                @Override
                public void onFailure(String content) {
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
*/

    }

    //加载更多
    private void loadMore() {
        if (hasMore) {
            index += 1;
            loadData(tabIndex, index, 10);
        } else {
            lv.completeRefresh();
        }
    }

    //根据条目在listview中的位置选择进入对应的详情页
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(getContext(), NewsItemActivity.class);
//        intent.putExtra("index",position);
//        intent.putExtra("title",map.get(position).toString());
//        startActivity(intent);
//        getnews();


//        Map<String, Object> item = adapter.getItem(position-1);
//        String title = item.get("title").toString();
        NewsInfoEntity.NewsRrdBean item = adapter.getItem(position - 1);
        String title = item.getTitle();

//        Toast.makeText(ApplicationApp.context, "单击的标题是"+title,
//                Toast.LENGTH_LONG).show();
        Intent intent1 = new Intent();
        intent1.setClass(getContext(), NewsItemActivity.class);

        intent1.putExtra("title", title);
        startActivity(intent1);

//        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
//        intent.putExtra(PageConfig.PAGE_CODE, pageCode);
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", ApplicationApp.getLoginEntity().getUserId());
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


//        if (lv.getCurrentState() == 2) return;
        position -= 1;
        //5从实体bean类中获得对应的参数
//        MyCommissionListEntity.RetDataBean item = adapter.getItem(position);
//        String procName = item.getProcessName();
//        String activityNameEN = item.getActivityNameEN();
        //6根据不同的参数点击条目进入到不同的界面
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
//
//        }
    }
    public static final String TAG = "NewsFragment";

    //7检索不同的字段参数跳转到对应的条目界面
//    private void checkDetail(int position, int pageCode, boolean remak, int tabIndex) {
//        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
//        intent.putExtra(PageConfig.PAGE_CODE, pageCode);
//        Bundle bundle = new Bundle();
//        bundle.putString("userId", ApplicationApp.getLoginEntity().getUserId());
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

    //8从对应的条目界面得到参数返回到我的待办中进行回调修改
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYCOMM){
            if (resultCode == Activity.RESULT_OK){
                final int item = data.getExtras().getInt("item");
                final int tabIndex = data.getExtras().getInt("tabIndex");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tabIndex == 0){
                            entityList.remove(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }
    }

    private DetailFragment.DataCallback callback;

    public NewsFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    //9搜索框过滤数据重新排列显示
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
                if (bean.getContext().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (DataUtil.parseDateByFormat(bean.getModifyTime(), "yyyy-MM-dd HH:mm").replace(" ", "").contains(key)) {
                    list.add(bean);
                }

            }
//            List<Map<String, Object>> list = new ArrayList<>();
//            for (Map<String, Object> bean : baseEntityList) {
//                if (bean.get("title").toString().replace(" ", "").contains(key)) {
//                    list.add(bean);
//                }
//                if (bean.get("sketch").toString().replace(" ", "").contains(key)) {
//                    list.add(bean);
//                }
//                if (DataUtil.parseDateByFormat(bean.get("date").toString(), "yyyy-MM-dd HH:mm").replace(" ", "").contains(key)) {
//                    list.add(bean);
//                }
////                if (bean.get("message").toString().replace(" ", "").contains(key)) {
////                    list.add(bean);
////                }
//            }
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

    //下拉刷新
    @Override
    public void onPullRefresh() {
        pb.setVisibility(View.GONE);
        hasMore = true;
        index = 1;
        loadData(tabIndex, index, 10);
    }

    //加载更多
    @Override
    public void onLoadingMore() {
        loadMore();
    }

    @Override
    public void onScrollOutside() {

    }

}
