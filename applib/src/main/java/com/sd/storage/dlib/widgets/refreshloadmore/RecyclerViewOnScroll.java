package com.sd.storage.dlib.widgets.refreshloadmore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by WuXiaolong on 2015/7/7.
 */
public class RecyclerViewOnScroll extends RecyclerView.OnScrollListener {
    private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    public RecyclerViewOnScroll(PullLoadMoreRecyclerView pullLoadMoreRecyclerView) {
        this.mPullLoadMoreRecyclerView = pullLoadMoreRecyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int lastCompletelyVisibleItem = 0;
        int firstVisibleItem = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = ((GridLayoutManager) layoutManager);
            //Position to find the final item of the current LayoutManager
            lastCompletelyVisibleItem = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            firstVisibleItem = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) layoutManager);
            lastCompletelyVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
            // since may lead to the final item has more than one StaggeredGridLayoutManager the particularity of the so here that is an array
            // this array into an array of position and then take the maximum value that is the last show the position value
            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
            lastCompletelyVisibleItem = findMax(lastPositions);
            firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
        }
        if (firstVisibleItem == 0 || firstVisibleItem == RecyclerView.NO_POSITION) {
            if (mPullLoadMoreRecyclerView.getPullRefreshEnable())
                mPullLoadMoreRecyclerView.setSwipeRefreshEnable(true);
        } else {
            mPullLoadMoreRecyclerView.setSwipeRefreshEnable(false);
        }
        if (mPullLoadMoreRecyclerView.getPushRefreshEnable() &&
                !mPullLoadMoreRecyclerView.isRefresh()
                && mPullLoadMoreRecyclerView.isHasMore()
                && (lastCompletelyVisibleItem == totalItemCount - 1)
                && !mPullLoadMoreRecyclerView.isLoadMore()
                && (dx > 0 || dy > 0)) {
            mPullLoadMoreRecyclerView.setIsLoadMore(true);
            mPullLoadMoreRecyclerView.loadMore();
        }
    }
    
//    @Override
//	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//		// 状态为0时：当前屏幕停止滚动；
//		// 状态为1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；
//		// 状态为2时：随用户的操作，屏幕上产生的惯性滑动；
//		super.onScrollStateChanged(recyclerView, newState);
//		// 根据newState状态做处理
//			if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                Glide.with(recyclerView.getContext()).resumeRequests();
//			} else {
//                Glide.with(recyclerView.getContext()).pauseRequests();
//			}
//		}

    private int findMax(int[] lastPositions) {

        int max = lastPositions[0];
        for (int value : lastPositions) {
            //       int max    = Math.max(lastPositions,value);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
