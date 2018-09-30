package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sd.storage.dlib.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/1/18.
 */

public abstract class BaseReAdapter<T> extends RecyclerView.Adapter<BaseReAdapter.ViewHolder> {


    private List<T> _data;
    protected Context mContext;
    private LayoutInflater mInflater;

    public BaseReAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        if (null == _data || ListUtils.isEmpty(_data)) {
            return 0;
        }
        return _data.size();
    }



    protected LayoutInflater getLayoutInflater() {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(mContext);
        }
        return mInflater;
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

    public void clear() {
        if (_data!=null) {
            _data.clear();
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public View itemView;
        public ViewHolder(View view) {
            super(view);
            itemView=view;
            ButterKnife.bind(this,view);
        }
    }

    protected abstract View onCreateItemView(ViewGroup parent);

    protected abstract ViewHolder onCreateItemViewHolder(View view);

    protected abstract void onBindItemViewHolder(ViewHolder holder, int position);


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindItemViewHolder(holder,position);

    }
/*    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weekmeun_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeekMeunModel weekMeunModel = weekMeunModels.get(position);
        holder.lin_item.setTag(position);
        holder.tv_name.setTag(position);
        holder.tv_name.setText(weekMeunModel.name);
        holder.lin_item.setOnClickListener(this);
        holder.tv_name.setOnClickListener(this);

    }*/


}
