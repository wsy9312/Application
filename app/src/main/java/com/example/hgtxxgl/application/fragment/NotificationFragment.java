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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.NewsItemActivity;
import com.example.hgtxxgl.application.entity.MyLaunchListEntity;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.DataUtil;
import com.example.hgtxxgl.application.utils.hand.ListAdapter;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.SimpleListView;
import com.example.hgtxxgl.application.view.SwipeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//原本我启动的fragment,现在的通知中心
public class NotificationFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleListView.OnRefreshListener {

//    SimpleListView lv;
    private int tabIndex;
    private int index = 1;
    private View root;
    private boolean hasMore = true;
    private ImageView ivEmpty;
    private String key = "";
    private TextView tvEmpty;
    private Button btnEmpty;
    //1原本的外部网络数据集合变量
    private List<MyLaunchListEntity.RetDataBean> entityList = new ArrayList<>();
    private List<MyLaunchListEntity.RetDataBean> baseEntityList;
    //2绑定数据的adapter
    private ListAdapter<MyLaunchListEntity.RetDataBean> adapter =//绑定集合实体和绑定条目布局
            new ListAdapter<MyLaunchListEntity.RetDataBean>((ArrayList<MyLaunchListEntity.RetDataBean>) entityList, R.layout.item_list_person) {

        HashSet<SwipeLayout> openedItems = new HashSet<>();
        SwipeLayout.OnSwipeListener onSwipeListener = new SwipeLayout.OnSwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                openedItems.remove(layout);
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                openedItems.add(layout);
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                closeAllItems();
            }
        };
    //3根据网络获取的数据设置的条目,绑定视图
        @Override
        public void bindView(final ViewHolder holder, MyLaunchListEntity.RetDataBean obj) {
//            holder.setText(R.id.tv_title, ApplicationApp.getLoginEntity().getRetData().getUserInfo().getNameCN());
            holder.setText(R.id.tv_date, DataUtil.parseDateByFormat(obj.getUpdateTime(), "yyyy-MM-dd HH:mm"));
            holder.setText(R.id.tv_context, obj.getSummary());
            holder.setText(R.id.tv_sketch, obj.getProcessNameCN());
        }
    //4得到条目视图
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            SwipeLayout sl = (SwipeLayout) view;
            sl.setSlide(tabIndex == 0);
            //点击条目进入新闻view
            view.findViewById(R.id.llayout_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    checkItem(position);
                    adapter.closeAllItems();
                }
            });
            sl.close(false);
            sl.setOnSwipeListener(onSwipeListener);
            //这是条目删除点击事件,暂时不需要
//            TextView tvDelete = (TextView) view.findViewById(R.id.tv_itrm_delete);
//            tvDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    removeTarget(entityList.get(position).getId());
//                }
//            });
            return view;
        }

        @Override
        public void closeAllItems() {
            for (SwipeLayout openedItem : openedItems) {
                openedItem.close();
            }
            openedItems.clear();
        }
    };
    private DetailFragment.DataCallback callback;
    private ProgressBar pb;

    public NotificationFragment() {

    }

    public static NotificationFragment newInstance(int tab) {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        args.putInt(DetailFragment.ARG_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    //5删除单独的条目(用途不大,暂时不需要)
//    private void removeTarget(final int id) {
//        Snackbar.make(ivEmpty,"是否确定删除？",Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                final ProgressDialog dialog = new ProgressDialog(getContext());
//                dialog.setMessage("删除中，请稍等...");
//                dialog.setCancelable(false);
//                dialog.show();
//                Map<String, Object> params = CommonValues.getCommonParams(getActivity());
//                params.put("Id", id);
//                HttpManager.getInstance().requestResultForm(CommonValues.REQ_DELETE_DRAFT, params, RemoveDraftEntity.class, new HttpManager.ResultCallback<RemoveDraftEntity>() {
//                    @Override
//                    public void onSuccess(String content, final RemoveDraftEntity entity) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                if (entity != null && entity.getCode().equals("100")) {
//                                    entityList.remove(index-1);
//                                    adapter.notifyDataSetChanged();
//                                    dialog.setMessage("删除成功："+entity.getMsg());
//                                } else {
//                                    dialog.setMessage("删除失败："+entity.getMsg());
//                                }
//                                v.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        dialog.dismiss();
//                                    }
//                                },1000);
//
//                                pb.setVisibility(View.GONE);
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(String content) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                dialog.setMessage("网络错误");
//                                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "重试", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        removeTarget(id);
//                                    }
//                                });
//                                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                                pb.setVisibility(View.GONE);
//                            }
//                        });
//                    }
//                });
//            }
//        }).setActionTextColor(Color.RED).show();
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabIndex = getArguments().getInt(DetailFragment.ARG_TAB, 0);
        loadData(tabIndex, index, 10);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_viewpage_page_content, null, false);
//        lv = (SimpleListView) root.findViewById(R.id.viewpager_listview);
//        ivEmpty = (ImageView) root.findViewById(R.id.iv_empty);
//        tvEmpty = (TextView) root.findViewById(R.id.tv_reload);
//        btnEmpty = (Button) root.findViewById(R.id.btn_reload);
//        pb = (ProgressBar) root.findViewById(R.id.mycommission_pb);
//        lv.setAdapter(adapter);
        //注册adapter数据监听器,监听条目数量是否为空,为空则显示标志空的图标
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                if (adapter.getCount() == 0) {
                    ivEmpty.setVisibility(View.VISIBLE);
//                    tvEmpty.setVisibility(View.VISIBLE);
//                    btnEmpty.setVisibility(View.VISIBLE);
                } else {
                    ivEmpty.setVisibility(View.GONE);
//                    tvEmpty.setVisibility(View.GONE);
//                    btnEmpty.setVisibility(View.GONE);
                }
                pb.setVisibility(View.GONE);
                super.onChanged();
            }
        });
        //设置自定义listview刷新和条目点击监听器
//        lv.setOnRefreshListener(this);
//        lv.setOnItemClickListener(this);
        return root;
    }

    //搜索框具体功能实现
    public void filter(String key) {
//        if (lv == null || key == null) return;
        this.key = key;
        if (entityList != null && !key.equals("")) {
            if (baseEntityList == null) {
                baseEntityList = new ArrayList<>();
                baseEntityList.addAll(entityList);
            }
            List<MyLaunchListEntity.RetDataBean> list = new ArrayList<>();
            for (MyLaunchListEntity.RetDataBean bean : baseEntityList) {
                if (bean.getProcessNameCN().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (bean.getSummary().replace(" ", "").contains(key)) {
                    list.add(bean);
                }
                if (DataUtil.parseDateByFormat(bean.getUpdateTime(), "yyyy-MM-dd HH:mm").replace(" ", "").contains(key)) {
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

    public NotificationFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    //根据顶部菜单按钮点击切换到对应的功能模块
    public void loadData(int tabIndex, final int pageIndex, int pageSize) {
    /*    if (callback != null) {
            callback.onLoadData();
        }
        HashMap<String, Object> map = (HashMap<String, Object>) CommonValues.getCommonParams(getActivity());
        //6运行出错,设定假数据
        map.put("userId", "123");
        String url = CommonValues.REQ_I_LAUNCH;
        if (tabIndex == 0) {
            url = CommonValues.REQ_I_LAUNCH_UNCOMMITTED;
            map.put("status", "07");  //保存未提交
        } else if (tabIndex == 1) {
            map.put("status", "01;06;04"); //未完成
        } else if (tabIndex == 2) {
            map.put("status", "03"); //已完成
        }
        map.put("keyWord", key);
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageSize);
        if (map.get("userId") != null){
            HttpManager.getInstance().requestResultForm(url, map, MyLaunchListEntity.class, new HttpManager.ResultCallback<MyLaunchListEntity>() {
                @Override
                public void onSuccess(String content, final MyLaunchListEntity myLaunchListEntity) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (myLaunchListEntity != null && myLaunchListEntity.getRetData().size() > 0) {
                                hasMore = true;
                                if (pageIndex == 1){
                                    entityList.clear();
                                }
                                entityList.addAll(myLaunchListEntity.getRetData());
                                lv.setVisibility(View.VISIBLE);
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
                        }
                    });
                }
            });
        }else {
            ToastUtil.showToast(getContext(),"登陆信息超时，请重新登陆");
        }
*/
    }

    //加载更多
    private void loadMore() {
        if (hasMore) {
            index += 1;
            loadData(tabIndex, index, 10);
        } else {
//            lv.completeRefresh();
        }

    }

    //根据条目在listview中的位置选择进入对应的详情页
    private void checkItem(int position){
//        if (lv.getCurrentState() == 2) return;
        String procName = adapter.getItem(position).getProcessName();
        //公出明细
        if (procName.equals("BusinessTripRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_BLEAVE,tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_BLEAVE, tabIndex);
            }
            //加班明细
        } else if (procName.equals("OverTimeRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_EXTRAWORK, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_OVERTIME, tabIndex);
            }
            //请假明细
        } else if (procName.equals("LeaveRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_REST, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_REST, tabIndex);
            }
            //费用明细
        } else if (procName.equals("ExpenseRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_EXPENSE_OFFER, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_EXPENSE, tabIndex);
            }

        }//付款明细
        else if (procName.equals("PaymentRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_PAYMENT_FLOW, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_PAYMENT, tabIndex);
            }
        }//差旅
        else if (procName.equals("TravelExpenseRequest")) {
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_TRAVEL_OFFER, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_TRAVEL, tabIndex);
            }
        }//招待费
        else if(procName.equals("EntertainmentExpenseRequest")){
            if (tabIndex == 0) {
                checkDetail(position, PageConfig.PAGE_APPLY_ENTERTAINMENT_EXPENSE, tabIndex);
            } else {
                checkDetail(position, PageConfig.PAGE_DISPLAY_EXPENSE_OFFER, tabIndex);
            }
        }//发文
        else if(procName.equals("FileRequest")){
//            if (tabIndex == 0) {
//                checkDetail(position, PageConfig.PAGE_APPLY_POST_FILE);
//            } else {
            checkDetail(position, PageConfig.PAGE_DISPLAY_POST_FILE, tabIndex);
//            }
        }else {
            checkDetail(position, PageConfig.PAGE_DISPLAY_UNIFIED, tabIndex);
        }

    }

    //传递意图带上参数进入对应的详情页
    private void checkDetail(int position, int pageApplyBleave, int tabIndex) {
        Intent intent = new Intent(getActivity(), NewsItemActivity.class);
        intent.putExtra(PageConfig.PAGE_CODE, pageApplyBleave);
        Bundle bundle = new Bundle();
//        bundle.putString("userId", ApplicationApp.getLoginEntity().getUserId());
        bundle.putString("barCode", adapter.getItem(position).getBarCode());
        bundle.putString("SubmitBy",adapter.getItem(position).getSubmitBy());
        bundle.putString("SN",adapter.getItem(position).getProcInstID() + "_" + adapter.getItem(position).getActInstDestID());
        bundle.putString("workflowType", adapter.getItem(position).getProcessName());
        bundle.putString("ProcessNameCN",adapter.getItem(position).getProcessNameCN());
        bundle.putInt("item",position);
        bundle.putInt("tabIndex",tabIndex);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, CommonValues.MYLAUN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonValues.MYLAUN){
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        checkItem(position);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        index = 1;
//        hasMore = true;
//        loadData(tabIndex, index, 10);
//    }

    //下拉刷新
    @Override
    public void onPullRefresh() {
        pb.setVisibility(View.VISIBLE);
        index = 1;
        hasMore = true;
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
