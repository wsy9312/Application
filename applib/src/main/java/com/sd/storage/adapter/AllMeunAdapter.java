package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.model.VageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/18.
 */

public class AllMeunAdapter extends RecyclerView.Adapter<AllMeunAdapter.ViewHolder> implements View.OnClickListener {


    private List<VageModel> vageModels = new ArrayList<>();
    private Context context;


    public AllMeunAdapter(Context context) {
        this.context = context;

    }

    public void setAllMeunModels(List<VageModel> vageModels) {
        this.vageModels = vageModels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_store, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VageModel vageModel = vageModels.get(position);
        holder.tv_name.setText(vageModel.vegename);
        holder.tv_details.setText(vageModel.vegedesc);


        String heatid = vageModel.heatid;
        if ("0".equals(heatid)) {
            holder.tv_meunestatus.setVisibility(View.VISIBLE);
        } else {
            holder.tv_meunestatus.setVisibility(View.GONE);
        }


        String url = vageModel.vegeimg;
        if (null != url) {
            Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(holder.im_image);
        }

        holder.im_add.setTag(position);
        holder.im_add.setOnClickListener(this);

        holder.im_delet.setTag(position);
        holder.im_delet.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return vageModels.size();

    }

    //  删除数据
    public void removeData(int position) {
        vageModels.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_add) {
            if (null != onItemMeunClickListener) {
                onItemMeunClickListener.onItemClick((int) view.getTag());
            }

        } else if (i == R.id.im_delet) {
            if (null != onItemMeunClickListener) {
                int tpoint = (int) view.getTag();
                onItemMeunClickListener.onItemDeletClick(tpoint, vageModels.get(tpoint).vegeid);
            }

        }
    }

    public interface OnItemMeunClickListener {
        void onItemClick(int point);

        void onItemDeletClick(int point,String vegeid);

    }

    private OnItemMeunClickListener onItemMeunClickListener;

    public void setOnItemMeunClickListenerr(OnItemMeunClickListener onItemMeunClickListener) {

        this.onItemMeunClickListener = onItemMeunClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_name)
        TextView tv_name;

//        @BindView(R.id.tv_details)
        TextView tv_details;


//        @BindView(R.id.im_add)
        ImageView im_add;

//        @BindView(R.id.im_delet)
        ImageView im_delet;

//        @BindView(R.id.im_image)
        ImageView im_image;

//        @BindView(R.id.tv_meunestatus)
        TextView tv_meunestatus;


        public ViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_details = view.findViewById(R.id.tv_details);
            im_add = view.findViewById(R.id.im_add);
            im_delet = view.findViewById(R.id.im_delet);
            im_image = view.findViewById(R.id.im_image);
            tv_meunestatus = view.findViewById(R.id.tv_meunestatus);
//            ButterKnife.bind(this, view);
        }
    }


}
