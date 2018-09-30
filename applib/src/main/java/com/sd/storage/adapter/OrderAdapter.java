package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.model.VageModel;
import com.sd.storage.util.MyProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


    private List<VageModel> vageModels = new ArrayList<>();
    private Context context;
    private int max = 100;


    public OrderAdapter(Context context) {
        this.context = context;

    }

    public void setVegeModels(List<VageModel> vageModels, int max) {
        this.vageModels = vageModels;
        this.max = max;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VageModel vageModel = vageModels.get(position);
        holder.tv_name.setText(vageModel.vegename);
        holder.progressBar.setMax(max);
        int p = Integer.parseInt(vageModel.vegevote);
        if (p > 0) {
            holder.progressBar.setProgress(Integer.parseInt(vageModel.vegevote));
        }
        holder.progressBar.setText(Integer.parseInt(vageModel.vegevote));

    }

    @Override
    public int getItemCount() {
        if (vageModels.size() == 0) {
            return 0;
        } else {
            return vageModels.size();
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_name)
        TextView tv_name;

//        @BindView(R.id.progressBar)
        MyProgress progressBar;


        public ViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            progressBar = view.findViewById(R.id.progressBar);
//            ButterKnife.bind(this, view);
        }
    }

}
