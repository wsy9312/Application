package com.sd.storage.ui.main.weekmeun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.WeekMeunActionsCreator;
import com.sd.storage.adapter.RecyclerViewAdapter;
import com.sd.storage.adapter.WeekMeunAdapter;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dialog.AddPopuwindow;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.model.WeekMeunModel;
import com.sd.storage.model.WeekModel;
import com.sd.storage.stores.WeekMeunStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.DateUtil;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 本周菜谱
 * Created by Administrator on 2018-09-05.
 */

public class WeekMenuActivity extends BaseSCActivity implements WeekMeunAdapter.OnClickItemListener, AddPopuwindow.OnSelectClickListener, RecyclerViewAdapter.OnItemVageClickListener, View.OnClickListener {

//    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.im_search)
    ImageView im_search;


//    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
//    @BindView(R.id.im_select)
    ImageView im_select;


//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
//    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout im_back;

    @Inject
    WeekMeunActionsCreator weekMeunActionsCreator;
    @Inject
    WeekMeunStore weekMeunStore;


    private WeekMeunAdapter weekMeunAdapter;
    private ArrayList<WeekModel> arrayList = new ArrayList<>();
    private String dayid;

    private AddPopuwindow popuwindow;


    private RecyclerViewAdapter adapter;
    private ArrayList<WeekMeunModel> weekMeunModelArrayList = new ArrayList<>();


    //早餐列表
    private ArrayList<VageModel> vageModels1 = new ArrayList<>();
    //午餐列表
    private ArrayList<VageModel> vageModels2 = new ArrayList<>();
    //晚餐列表
    private ArrayList<VageModel> vageModels3 = new ArrayList<>();


    private int tpoint = -1;

    private int vegegive = 0;
    private boolean isGive = false;


    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageApplication.getApplication().getAppComponent().inject(this);
        initView();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ivegeid ="-1";
        getData(dayid);

    }

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        im_search = (ImageView) findViewById(R.id.im_search);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        im_select = (ImageView) findViewById(R.id.im_select);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_back.setOnClickListener(this);
        im_select.setOnClickListener(this);
        im_search.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        adapter = new RecyclerViewAdapter(this);
        adapter.setOnVageItemClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //   adapter.setData(vageModels1, vageModels2, vageModels3);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
            }
        });

    }


    public void init() {
        tv_title.setText(R.string.weekMenu);
        dayid = DateUtil.getWeek();
        popuwindow = new AddPopuwindow(WeekMenuActivity.this);
        popuwindow.setOnSelectClickListener(this);
        String[] weekname = getResources().getStringArray(R.array.week_name);
        String[] dayid_list = getResources().getStringArray(R.array.dayid_list);
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //设置RecyclerView 布局
        recycler_view.setLayoutManager(layoutmanager);
        //设置Adapter
        for (int i = 0; i < weekname.length; i++) {

            arrayList.add(new WeekModel(weekname[i], dayid_list[i]));
        }

        weekMeunAdapter = new WeekMeunAdapter(arrayList, this, Integer.parseInt(dayid) - 1);
        weekMeunAdapter.setOnClickListener(this);
        recycler_view.setAdapter(weekMeunAdapter);
        if (Integer.parseInt(dayid) > 4) {
            smoothMoveToPosition(recycler_view, 6);
        }


    }


//    @OnClick({R.id.im_back, R.id.im_select, R.id.im_search})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_select) {
            popuwindow.showPopuwindow(im_select);

        } else if (i == R.id.im_search) {
            getDisplay().startSearchVageActivity();

        }
    }

    @Override
    protected void initReturnEvent() {

        weekMeunStore.toMainSubscription(WeekMeunStore.MeunTypeChange.class, new Action1<WeekMeunStore.MeunTypeChange>() {
            @Override
            public void call(WeekMeunStore.MeunTypeChange meunTypeChange) {
                getDisplay().hideWaittingDialog();
                weekMeunModelArrayList = weekMeunStore.getWeekMeunModels();
                vageModels1.clear();
                vageModels2.clear();
                vageModels3.clear();
                if (null != weekMeunModelArrayList && weekMeunModelArrayList.size() > 0) {
                    for (int i = 0; i < weekMeunModelArrayList.size(); i++) {
                        String typeid = weekMeunModelArrayList.get(i).typeid;
                        if ("1".equals(typeid)) {
                            vageModels1 = weekMeunModelArrayList.get(i).veges;
                        } else if ("2".equals(typeid)) {
                            vageModels2 = weekMeunModelArrayList.get(i).veges;
                        } else if ("3".equals(typeid)) {
                            vageModels3 = weekMeunModelArrayList.get(i).veges;
                        }
                    }
                }
                setData();
            }
        });

        /**
         * 请求错误
         */
        weekMeunStore.toMainSubscription(WeekMeunStore.MeunTypeChangeError.class, new Action1<WeekMeunStore.MeunTypeChangeError>() {
            @Override
            public void call(WeekMeunStore.MeunTypeChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, WeekMenuActivity.this);
            }
        });


        /**
         * 点赞成功
         */
        weekMeunStore.toMainSubscription(WeekMeunStore.GiveChange.class, new Action1<WeekMeunStore.GiveChange>() {
            @Override
            public void call(WeekMeunStore.GiveChange giveChange) {
                getDisplay().hideWaittingDialog();

                if (!isGive) {
                    isGive = true;
                    vegegive += 1;
                } else {
                    isGive = false;
                    vegegive -= 1;
                }
                setItem();

            }
        });


        /**
         * 请求错误
         */
        weekMeunStore.toMainSubscription(WeekMeunStore.GiveChangeError.class, new Action1<WeekMeunStore.GiveChangeError>() {
            @Override
            public void call(WeekMeunStore.GiveChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, WeekMenuActivity.this);
            }
        });

        /**
         * 删除
         */
        weekMeunStore.toMainSubscription(WeekMeunStore.DeletChange.class, new Action1<WeekMeunStore.DeletChange>() {
            @Override
            public void call(WeekMeunStore.DeletChange deletChange) {
                getDisplay().hideWaittingDialog();
                adapter.removeData(dType, dTPoint, dPoint);
                Ivegeid = "-1";
                deletArr();
               /* point1 = -1;
                point2 = -1;
                point3 = -1;
                i1 = -1;
                i2 = -1;
                i3 = -1;*/

            }
        });

        /**
         * 请求错误
         */
        weekMeunStore.toMainSubscription(WeekMeunStore.DeleteChangeError.class, new Action1<WeekMeunStore.DeleteChangeError>() {
            @Override
            public void call(WeekMeunStore.DeleteChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, WeekMenuActivity.this);
            }
        });
    }


    @Override
    public Store[] getStoreArray() {
        return new Store[]{weekMeunStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return weekMeunActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weekmeun;
    }

    /**
     * 周天选择器
     *
     * @param item
     */
    @Override
    public void onItemOnClick(WeekModel item) {
        // tv_text.setText(item.getName());
        weekMeunAdapter.setPoint(Integer.valueOf(item.getDayid()).intValue() - 1);
        this.dayid = item.dayid;
        Ivegeid = "-1";
        getData(item.dayid);
        //
    }


    /**
     * 早中晚的接口的调试
     *
     * @param typeid
     */
    @Override
    public void onSelectClickListener(int typeid) {

        if (typeid == 1) {
            smoothMoveToPosition(recyclerView, 0);
        } else if (typeid == 2) {
            smoothMoveToPosition(recyclerView, 1 + vageModels1.size());
        } else {
            smoothMoveToPosition(recyclerView, 1 + vageModels1.size() + 1 + vageModels2.size());
        }

    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            boolean isEnd = true;

            mToPosition = position;
            mShouldScroll = true;
        }
    }

    public void setData() {
        adapter.setData(vageModels1, vageModels2, vageModels3);

    }


    public void getData(String id) {
        getDisplay().showWaittingDialog();
        weekMeunActionsCreator.getMeunType(id, UrlManager.getUSERID());
        // weekMeunActionsCreator.getMeunType(dayid, UrlManager.getUSERID());
    }

    /**
     * 进入页面详情
     *
     * @param type
     * @param point
     */
    @Override
    public void onItemVageClick(int type, int point) {

        switch (type) {
            case 1:
                getDisplay().startVageDetailsActivity(vageModels1.get(point).vegeid);
                break;
            case 2:
                getDisplay().startVageDetailsActivity(vageModels2.get(point).vegeid);
                break;
            case 3:
                getDisplay().startVageDetailsActivity(vageModels3.get(point).vegeid);
                break;
        }
    }

    @Override
    public void onItemAddCommentClick(int type, int point) {

        switch (type) {
            case 1:
                getDisplay().startAddCommentActivity(vageModels1.get(point).vegeid);
                break;
            case 2:
                getDisplay().startAddCommentActivity(vageModels2.get(point).vegeid);
                break;
            case 3:
                getDisplay().startAddCommentActivity(vageModels3.get(point).vegeid);
                break;
        }

    }


    /**
     * 设置点赞
     *
     * @param point
     */
    @Override
    public void onItemGiveClick(int type, int point, int i) {

        switch (type) {
            case 1:
                weekMeunActionsCreator.setGive(vageModels1.get(point).vegeid, UrlManager.getUSERID());
                break;
            case 2:
                weekMeunActionsCreator.setGive(vageModels2.get(point).vegeid, UrlManager.getUSERID());
                break;
            case 3:
                weekMeunActionsCreator.setGive(vageModels3.get(point).vegeid, UrlManager.getUSERID());
                break;
        }

        if (tpoint != i) {
            this.tpoint = i;
            switch (type) {
                case 1:
                    this.vegegive = Integer.parseInt(vageModels1.get(point).vegegive);
                    if (vageModels1.get(point).vegestate == 0) {
                        isGive = false;
                    } else {
                        isGive = true;
                    }
                    break;
                case 2:
                    this.vegegive = Integer.parseInt(vageModels2.get(point).vegegive);
                    if (vageModels2.get(point).vegestate == 0) {
                        isGive = false;
                    } else {
                        isGive = true;
                    }
                    break;
                case 3:
                    this.vegegive = Integer.parseInt(vageModels3.get(point).vegegive);
                    if (vageModels3.get(point).vegestate == 0) {
                        isGive = false;
                    } else {
                        isGive = true;
                    }
                    break;
            }

        }


    }


    private String Ivegeid = "-1";
    private int point1 = -1, point2 = -1, point3 = -1;
    private int i1 = -1, i2 = -1, i3 = -1;

    @Override
    public void onItemGiveClick2(String vegeid, String vegegive, int vegestate) {
        weekMeunActionsCreator.setGive(vegeid, UrlManager.getUSERID());

        if (!vegeid.equals(Ivegeid)) {
            this.Ivegeid = vegeid;
            this.vegegive = Integer.parseInt(vegegive);
            if (vegestate == 0) {
                isGive = false;
            } else {
                isGive = true;
            }
            point1 = -1;
            point2 = -1;
            point3 = -1;
//记录三个point值
            if (vageModels1.size() > 0) {
                for (int i = 0; i < vageModels1.size(); i++) {
                    VageModel vageModel = vageModels1.get(i);
                    if (Ivegeid.equals(vageModel.vegeid)) {
                        point1 = i + 1;
                        i1 = i;
                    }
                }
            }

            if (vageModels2.size() > 0) {
                for (int i = 0; i < vageModels2.size(); i++) {
                    VageModel vageModel = vageModels2.get(i);
                    if (Ivegeid.equals(vageModel.vegeid)) {
                        point2 = i + 1 + vageModels1.size() + 1;
                        i2 = i;
                    }
                }
            }

            if (vageModels3.size() > 0) {
                for (int i = 0; i < vageModels3.size(); i++) {
                    VageModel vageModel = vageModels3.get(i);
                    if (Ivegeid.equals(vageModel.vegeid)) {
                        point3 = i + 1 + vageModels1.size() + 1 + vageModels2.size() + 1;
                        i3 = i;
                    }
                }
            }
        }
    }


    public void setItem() {


        if (isGive) {
            adapter.setNewData(point1, point2, point3, i1, i2, i3, 1, vegegive + "");
        } else {
            adapter.setNewData(point1, point2, point3, i1, i2, i3, 0, vegegive + "");
        }


      /*  RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(point);

        if (viewHolder != null && viewHolder instanceof RecyclerViewAdapter.ItemFaseViewHolder) {
            RecyclerViewAdapter.ItemFaseViewHolder itemHolder = (RecyclerViewAdapter.ItemFaseViewHolder) viewHolder;

            if (isGive) {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_true);
                itemHolder.tv_give.setText(vegegive + "");
            } else {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_false);
                itemHolder.tv_give.setText(vegegive + "");
            }

        }
        if (viewHolder != null && viewHolder instanceof RecyclerViewAdapter.ItemLunchViewHolder) {
            RecyclerViewAdapter.ItemLunchViewHolder itemHolder = (RecyclerViewAdapter.ItemLunchViewHolder) viewHolder;
            if (isGive) {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_true);
                itemHolder.tv_give.setText(vegegive + "");

            } else {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_false);
                itemHolder.tv_give.setText(vegegive + "");
            }

        }
        if (viewHolder != null && viewHolder instanceof RecyclerViewAdapter.ItemAfterNoonViewHolder) {
            RecyclerViewAdapter.ItemAfterNoonViewHolder itemHolder = (RecyclerViewAdapter.ItemAfterNoonViewHolder) viewHolder;

            if (isGive) {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_true);
                itemHolder.tv_give.setText(vegegive + "");
            } else {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_false);
                itemHolder.tv_give.setText(vegegive + "");
            }

        }*/
    }

    /**
     * 添加新菜到菜谱
     *
     * @param typeid
     */
    @Override
    public void onAddVegeClick(String typeid) {
        getDisplay().startAddWeekMeunActivity(dayid, typeid);
        //ToastUtils.showBaseToast("dayid=" + dayid + "typeid=" + typeid, this);

    }


    private int dType;
    private int dTPoint;
    private int dPoint;

    /**
     * 管理员删除 item
     *
     * @param foodid
     * @param point
     */
    @Override
    public void onDeletitem(int type, String foodid, int tpoint, int point) {
        this.dType = type;
        this.dTPoint = tpoint;
        this.dPoint = point;
        weekMeunActionsCreator.deleteVege(foodid);

    }


    public void deletArr() {

        switch (dType) {
            case 1:
                vageModels1.remove(dTPoint);
                break;
            case 2:
                vageModels2.remove(dTPoint);
                break;
            case 3:
                vageModels3.remove(dTPoint);
                break;

        }
    }
}
