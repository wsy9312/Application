package com.sd.storage.dlib.widgets.listener;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;


/**
 * 滚动至列表底部，读取下一页数据
 */
public class AutoLoadListener implements OnScrollListener {

	public interface AutoLoadCallBack {
		void execute();
	}

	private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
	private AutoLoadCallBack mCallback;

	public AutoLoadListener(AutoLoadCallBack callback) {
		this.mCallback = callback;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
//				Picasso.with(view.getContext()).resumeTag(DApplication.RECYCLER_TAG);
				// 滚动到底部
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					View v =  view.getChildAt(view.getChildCount() - 1);
					if(null == v){
						return;
					}
					int[] location = new int[2];
					v.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
					int y = location[1];

					if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y) {
						if(null != mCallback){
							mCallback.execute();
						}
					}
				}
				break;

			default:
//				Picasso.with(view.getContext()).pauseTag(DApplication.RECYCLER_TAG);
				break;
		}

	}

	public void onScroll(AbsListView arg0, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
	}
}