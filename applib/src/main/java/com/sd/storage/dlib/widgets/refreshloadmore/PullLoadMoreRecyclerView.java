package com.sd.storage.dlib.widgets.refreshloadmore;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;


public class PullLoadMoreRecyclerView extends LinearLayout {
    private EmptyRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullLoadMoreListener mPullLoadMoreListener;
    private boolean hasMore = true;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean pullRefreshEnable = true;
    private boolean pushRefreshEnable = true;
    private View mFooterView;
    private Context mContext;
    private TextView loadMoreText;

    private TextView tv_return_top;

    public PullLoadMoreRecyclerView(Context context) {
        super(context);
        initView(context);
    }

    public PullLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pull_loadmore_layout, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayoutOnRefresh(this));

        tv_return_top = (TextView) view.findViewById(R.id.tv_return_top);
        tv_return_top.setOnClickListener(topClick);

        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setVerticalScrollBarEnabled(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new CustomRecyclerViewOnScroll(this));

        mRecyclerView.setOnTouchListener(new onTouchRecyclerView());

        mFooterView = view.findViewById(R.id.footerView);
        loadMoreText = (TextView) view.findViewById(R.id.loadMoreText);
        mFooterView.setVisibility(View.GONE);
        this.addView(view);

    }

    public void setEmpty(View view){
        mRecyclerView.setEmptyView(view);
    }

    private OnClickListener topClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mRecyclerView.scrollToPosition(0);
            tv_return_top.setVisibility(View.GONE);
            if(null != recyclerViewOnScrollListener){
                total = 0;
                recyclerViewOnScrollListener.onScrolledDistance(total);
            }
        }
    };

    public interface OnRecyclerViewOnScrollListener{
          void onScrolledDistance(int dy);
    }

    OnRecyclerViewOnScrollListener recyclerViewOnScrollListener;

    public void setRecyclerViewOnScrollListener(
            OnRecyclerViewOnScrollListener recyclerViewOnScrollListener) {
        this.recyclerViewOnScrollListener = recyclerViewOnScrollListener;
    }

    int total;

    private class CustomRecyclerViewOnScroll extends RecyclerViewOnScroll {

        public CustomRecyclerViewOnScroll(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
            super(pullLoadMoreRecyclerView);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int topRowVerticalPosition = (recyclerView == null || recyclerView .getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            total += dy;
            boolean isEnabled = topRowVerticalPosition >= -10;
            if(isEnabled){
                tv_return_top.setVisibility(View.GONE);
            }else{
                tv_return_top.setVisibility(View.VISIBLE);
            }
            if(null != recyclerViewOnScrollListener){
                recyclerViewOnScrollListener.onScrolledDistance(total);
            }
        }
    }

    /**
     * LinearLayoutManager
     */
    public void setLinearLayout() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * GridLayoutManager
     */

    public void setGridLayout(int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, spanCount);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * StaggeredGridLayoutManager
     */

    public void setStaggeredGridLayout(int spanCount) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void scrollToTop() {
        mRecyclerView.scrollToPosition(0);
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    public RecyclerView.Adapter getAdapter(){
        return mRecyclerView.getAdapter();
    }

    public void setPullRefreshEnable(boolean enable) {
        pullRefreshEnable = enable;
        setSwipeRefreshEnable(enable);
    }

    public boolean getPullRefreshEnable() {
        return pullRefreshEnable;
    }

    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public boolean getSwipeRefreshEnable() {
        return mSwipeRefreshLayout.isEnabled();
    }


    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);

    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setRefreshing(final boolean isRefreshing) {
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (pullRefreshEnable)
                    mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });

    }

    /**
     * Solve IndexOutOfBoundsException exception
     */
    public class onTouchRecyclerView implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isRefresh || isLoadMore) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean getPushRefreshEnable() {
        return pushRefreshEnable;
    }

    public void setPushRefreshEnable(boolean pushRefreshEnable) {
        this.pushRefreshEnable = pushRefreshEnable;
    }

    public void setFooterViewText(CharSequence text) {
        loadMoreText.setText(text);
    }

    public void setFooterViewText(int resid) {
        loadMoreText.setText(resid);
    }

    public void refresh() {
        if (mPullLoadMoreListener != null) {
            mPullLoadMoreListener.onRefresh();
        }
    }

    public void loadMore() {
        if (mPullLoadMoreListener != null && hasMore) {
            mFooterView.setVisibility(View.VISIBLE);
            invalidate();
            mPullLoadMoreListener.onLoadMore();
        }
    }

    public void setPullLoadMoreCompleted() {
        isRefresh = false;
        mSwipeRefreshLayout.setRefreshing(false);

        isLoadMore = false;
        mFooterView.setVisibility(View.GONE);

    }

    public void setOnPullLoadMoreListener(PullLoadMoreListener listener) {
        mPullLoadMoreListener = listener;
    }


    public boolean isLoadMore() {
        return isLoadMore;
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface PullLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }
}
