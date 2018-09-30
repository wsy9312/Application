package com.sd.storage.dlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView有Header适配器
 * @author MrZhou
 *
 * @param <T>
 */
public abstract class RecyclerLayoutHisHeaderAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

	private static final int ITEM_VIEW_TYPE_HEADER = 0;
	private static final int ITEM_VIEW_TYPE_ITEM = 1;

	protected Context mContext;
	
	private List<T> data;

	private LayoutInflater mInflater;

	public LayoutInflater getInflater() {
		return mInflater;
	}

	public RecyclerLayoutHisHeaderAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public void setData(List<T> data) {
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void addData(List<T> data){
		if(null == data){
			data = new ArrayList<>();
		}
		this.data.addAll(data);
		notifyDataSetChanged();
	}
	
	public T getItem(int position) {
		return data.get(position-1);
	}
	
	@Override
	public int getItemCount() {
		if(null == data){
			return 1;
		}
		return data.size() + 1;
	}
	
	public boolean isHeader(int position) {
		return position == 0;
	}
	
	@Override
	public int getItemViewType(int position) {
	   return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if(isHeader(position)){
			onBindTopViewData(holder);
			return;
		}
		onBindHolderToView(holder, position);
	}

	public abstract void onBindTopViewData(ViewHolder holder);
	
	public abstract void onBindHolderToView(ViewHolder holder, int position);

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup group, int position) {
		if (position == ITEM_VIEW_TYPE_HEADER) {
			return onCreateTopViewToHolder(group);
		}
		return onCreateViewToHolder(group, position-1);
	}
	
	public abstract ViewHolder onCreateViewToHolder(ViewGroup group , int position) ;
	public abstract ViewHolder onCreateTopViewToHolder(ViewGroup group) ;
}