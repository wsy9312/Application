package com.sd.storage.dlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sd.storage.dlib.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class DBaseAdapter<T> extends BaseAdapter {

	private LayoutInflater mInflater;

	protected List<T> _data;

	private IUIListener mUIlistener;
	
	protected Context mContext;
	
	public DBaseAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		if (null == _data || ListUtils.isEmpty(_data)) {
			return 0;
		}
		return _data.size();
	}

	@Override
	public T getItem(int position) {
		return _data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder vh;
		if (convertView == null) {// 没有缓存
			view = onCreateItemView(parent);
			vh = onCreateItemViewHolder(view);
			view.setTag(vh);
		} else {
			view = convertView;
			vh = (ViewHolder) view.getTag();
		}
		// 绑定数据
		onBindItemViewHolder(vh, position);
		return view;
	}

	protected abstract View onCreateItemView(ViewGroup parent);

	protected abstract ViewHolder onCreateItemViewHolder(View view);

	protected abstract void onBindItemViewHolder(ViewHolder holder, int position);

	protected LayoutInflater getLayoutInflater() {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(mContext);
		}
		return mInflater;
	}
	
	public static class ViewHolder {

		public View itemView;

		public ViewHolder(View v) {
			super();
			itemView = v;
		}
	}

	public List<T> getData() {
		return _data == null ? (_data = new ArrayList<T>()) : _data;
	}

	public void addData(List<T> data) {
		if (_data == null) {
			_data = new ArrayList<T>();
		}
		_data.addAll(data);
		notifyDataSetChanged();
	}
	
	public void setData(List<T> data){
		_data = data;
		notifyDataSetChanged();
	}

	public void addItem(T t) {
		if (_data == null) {
			_data = new ArrayList<T>();
		}
		_data.add(t);
		notifyDataSetChanged();
	}

	public void addItem(int pos, T obj) {
		if (_data == null) {
			_data = new ArrayList<T>();
		}
		_data.add(pos, obj);
		notifyDataSetChanged();
	}

	public void removeItem(T obj) {
		_data.remove(obj);
		notifyDataSetChanged();
	}

	public void clear() {
		if (_data!=null) {
			_data.clear();
			notifyDataSetChanged();
		}
	}


	public IUIListener getmUIlistener() {
		return mUIlistener;
	}

	public void setmUIlistener(IUIListener mUIlistener) {
		this.mUIlistener = mUIlistener;
	}
	public interface IUIListener{
		void onEmpty();
	}
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (ListUtils.isEmpty(_data)) {
			if (mUIlistener!=null) {
				mUIlistener.onEmpty();
			}
		}
	}

}
