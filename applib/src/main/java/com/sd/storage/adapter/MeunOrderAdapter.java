package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MeunOrderAdapter extends RecyclerView.Adapter<MeunOrderAdapter.ViewHolder> implements View.OnClickListener {


    private List<VageModel> vageModels = new ArrayList<>();
    private Context context;
    private String type = "3";

    public MeunOrderAdapter(Context context) {
        this.context = context;

    }

    public void setVegeModels(List<VageModel> vageModels, String type) {
        this.type = type;
        this.vageModels = vageModels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vege_search, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VageModel vageModel = vageModels.get(position);

        String url = vageModel.vegeimg;
        if (null != url) {
            Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(holder.im_image);
        }
        holder.tv_name.setText(vageModel.vegename);
        holder.tv_details.setText(vageModel.vegedesc);
        String comment=vageModel.vegecomment;
        if(null!=comment && !"".equals(comment)){
            holder.tv_conmment.setText(vageModel.vegecomment);
        }


        //赞的总数
        if ("1".equals(type)) {
            holder.tv_give.setText(vageModel.vegegive);
            holder.im_give.setImageDrawable(context.getResources().getDrawable(R.drawable.give_up_false));

            holder.tv_give.setVisibility(View.VISIBLE);
            holder.im_give.setVisibility(View.VISIBLE);
            holder.tv_conmment.setVisibility(View.GONE);
            holder.im_commint.setVisibility(View.GONE);
        }

        //投票
        if ("3".equals(type)) {
            holder.tv_give.setText(vageModel.vegevote);
            holder.im_give.setImageDrawable(context.getResources().getDrawable(R.drawable.vote_false));

            holder.tv_give.setVisibility(View.VISIBLE);
            holder.im_give.setVisibility(View.VISIBLE);
            holder.tv_conmment.setVisibility(View.GONE);
            holder.im_commint.setVisibility(View.GONE);
        }
        //评论总数
        if ("2".equals(type)) {

            holder.tv_give.setVisibility(View.GONE);
            holder.im_give.setVisibility(View.GONE);
            holder.tv_conmment.setVisibility(View.VISIBLE);
            holder.im_commint.setVisibility(View.VISIBLE);
        }


        String heatid = vageModel.heatid;
        if ("0".equals(heatid)) {
            holder.tv_meunestatus.setVisibility(View.VISIBLE);
        } else {
            holder.tv_meunestatus.setVisibility(View.GONE);
        }

        holder.lin_item.setTag(position);
        holder.lin_item.setOnClickListener(this);
        holder.im_commint.setTag(position);
        holder.im_commint.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {
        if (vageModels.size() == 0) {
            return 0;
        } else {
            return vageModels.size();
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.lin_item) {
            if (null != onItemOrderClickListener) {
                onItemOrderClickListener.onItemClick((int) view.getTag());
            }

        } else if (i == R.id.im_commint) {
            if (null != onItemOrderClickListener) {
                onItemOrderClickListener.onItemAddCommentClick((int) view.getTag());
            }

        }
    }

    public interface OnItemOrderClickListener {
        void onItemClick(int point);

        void onItemAddCommentClick(int point);


    }

    private OnItemOrderClickListener onItemOrderClickListener;

    public void setOnItemSearchClickListener(OnItemOrderClickListener onItemOrderClickListener) {

        this.onItemOrderClickListener = onItemOrderClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_details)
        TextView tv_details;
//        @BindView(R.id.tv_conmment)
        TextView tv_conmment;
//        @BindView(R.id.tv_meunestatus)
        TextView tv_meunestatus;

//        @BindView(R.id.im_give)
        ImageView im_give;
//        @BindView(R.id.tv_give)
        TextView tv_give;
//        @BindView(R.id.im_commint)
        ImageView im_commint;
//        @BindView(R.id.im_image)
        ImageView im_image;

//        @BindView(R.id.lin_item)
        LinearLayout lin_item;

        public ViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_details = view.findViewById(R.id.tv_details);
            tv_conmment = view.findViewById(R.id.tv_conmment);
            tv_meunestatus = view.findViewById(R.id.tv_meunestatus);
            im_give = view.findViewById(R.id.im_give);
            tv_give = view.findViewById(R.id.tv_give);
            im_commint = view.findViewById(R.id.im_commint);
            im_image = view.findViewById(R.id.im_image);
            lin_item = view.findViewById(R.id.lin_item);
//            ButterKnife.bind(this, view);
        }
    }

}
