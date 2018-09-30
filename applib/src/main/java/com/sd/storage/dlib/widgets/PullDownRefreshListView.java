package com.sd.storage.dlib.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.sd.storage.R;
import com.sd.storage.dlib.widgets.listener.AutoLoadListener;
import com.sd.storage.dlib.widgets.listener.OnPullDownRefreshLisenter;
import com.sd.storage.dlib.widgets.listener.OnScrollTopListener;


public class PullDownRefreshListView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener{
	
	public ListView lv_view;
	
	public TextView tv_return_top;
	
	public TextView tv_empty;
	
	private OnPullDownRefreshLisenter downRefreshLisenter;
	
	private SwipeRefreshLayout swipe_refresh_layout;
	
	private OnScrollTopListener mOnScrollTopListener;
	
	public void setOnScrollTopListener(OnScrollTopListener mOnScrollTopListener) {
		this.mOnScrollTopListener = mOnScrollTopListener;
	}
	
	public PullDownRefreshListView(Context context) {
		super(context);
		init();
	}

	public PullDownRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void setOnPullDownRefreshLisenter(OnPullDownRefreshLisenter downRefreshLisenter){
		this.downRefreshLisenter = downRefreshLisenter;
	}

	private void init(){
		swipe_refresh_layout = (SwipeRefreshLayout) LayoutInflater.from(getContext()).inflate(R.layout.pull_load_refresh_list_view, null);
		this.addView(swipe_refresh_layout);
		swipe_refresh_layout.setOnRefreshListener(this);
		tv_return_top = (TextView) swipe_refresh_layout.findViewById(R.id.tv_return_top);
		lv_view = (ListView) swipe_refresh_layout.findViewById(R.id.lv_view);
		lv_view.setOnScrollListener(autoLoadListener);
		tv_empty = (TextView) swipe_refresh_layout.findViewById(R.id.tv_empty);
		tv_return_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				lv_view.setSelection(0);
			}
		});
	}
	
	public void setEmptyText(String text){
		tv_empty.setText(text);
	}
	
	public void setRefreshing(boolean isRefresh) {
		if(lv_view.getAdapter().getCount() == 0){
			tv_empty.setText(R.string.data_null);
		}
		swipe_refresh_layout.setRefreshing(isRefresh);
	}
	public void setRefreshing(boolean isRefresh, String empty) {
		if(lv_view.getAdapter().getCount() == 0){
			tv_empty.setText(empty);
		}
		swipe_refresh_layout.setRefreshing(isRefresh);
	}
	
	@Override
	public void onRefresh() {
		if(null != downRefreshLisenter){
			downRefreshLisenter.onPullRefresh();
		}
	}
	
	public void setAdapter(BaseAdapter adapter){
		lv_view.setAdapter(adapter);
		tv_empty.setText(R.string.load_more_text);
		lv_view.setEmptyView(tv_empty);
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		lv_view.setOnItemClickListener(listener);
	}
	
	/**
	 * 下拉之后加载
	 */
	 AutoLoadListener.AutoLoadCallBack callback = new AutoLoadListener.AutoLoadCallBack() {
		
		@Override
		public void execute() {
			if(null != downRefreshLisenter){
				downRefreshLisenter.onPullDown();
			}
		}
	};
	
	AutoLoadListener autoLoadListener = new AutoLoadListener(callback){
		public void onScroll(AbsListView absListView, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			boolean enable = false;
			if (lv_view != null && lv_view.getChildCount() > 0) {
				boolean firstItemVisible = lv_view.getFirstVisiblePosition() == 0;
				boolean topOfFirstItemVisible = lv_view.getChildAt(0).getTop() == 0;
				enable = firstItemVisible && topOfFirstItemVisible;
			}
			swipe_refresh_layout.setEnabled(enable);
			if (enable) {
				tv_return_top.setVisibility(View.GONE);
			} else if(lv_view.getCount() > 10) {
				tv_return_top.setVisibility(View.VISIBLE);
			}
			if(null != mOnScrollTopListener){
				mOnScrollTopListener.onIsScrollTop(enable);
			}
		};
	};
	
}
