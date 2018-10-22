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

public class VegeVoteListAdapter extends RecyclerView.Adapter<VegeVoteListAdapter.VoteViewHolder> implements View.OnClickListener {


    private List<VageModel> vageModels = new ArrayList<>();
    private Context context;


    public VegeVoteListAdapter(Context context) {
        this.context = context;

    }

    public void setAllMeunModels(List<VageModel> vageModels) {
        this.vageModels = vageModels;
        notifyDataSetChanged();
    }

    @Override
    public VoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vote_vege, parent, false);
        VoteViewHolder holder = new VoteViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(VoteViewHolder holder, int position) {
        VageModel vageModel = vageModels.get(position);
        holder.tv_name.setText(vageModel.vegename);
        holder.tv_details.setText(vageModel.vegedesc);


        holder.tv_give.setText(vageModel.vegevote);


        String url = vageModel.vegeimg;
        if (null != url) {
            Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(holder.im_image);
        }

        String vegevotestatus = vageModel.vegevotestatus;
        if ("0".equals(vegevotestatus)) {
            holder.im_give.setImageDrawable(context.getResources().getDrawable(R.drawable.vote_false));
        } else {
            holder.im_give.setImageDrawable(context.getResources().getDrawable(R.drawable.vote_true));
        }


        holder.lin_item.setTag(position);
        holder.lin_item.setOnClickListener(this);

        holder.im_give.setTag(position);
        holder.im_give.setOnClickListener(this);


    }

    @Override
    public int getItemCount() {

        return vageModels.size();

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_give) {
            if (null != onVoteItenClickListener) {
                onVoteItenClickListener.onSetVoteClick((int) view.getTag());
            }

        } else if (i == R.id.lin_item) {
            if (null != onVoteItenClickListener) {
                onVoteItenClickListener.onItemClick((int) view.getTag());
            }

        } else if (i == R.id.im_commint) {
            if (null != onVoteItenClickListener) {
                onVoteItenClickListener.onSetMessageClick((int) view.getTag());
            }

        }
    }

    public interface OnVoteItenClickListener {
        //进页面详情
        void onItemClick(int point);

        //设置评论
        void onSetMessageClick(int point);

        //设置投票
        void onSetVoteClick(int point);
    }

    private OnVoteItenClickListener onVoteItenClickListener;

    public void setOnItemVoteClickListenerr(OnVoteItenClickListener onVoteItenClickListener) {

        this.onVoteItenClickListener = onVoteItenClickListener;

    }

    public static class VoteViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.im_image)
        ImageView im_image;

//        @BindView(R.id.tv_details)
        TextView tv_details;


        public TextView tv_give;
        public ImageView im_give;


//        @BindView(R.id.lin_item)
        LinearLayout lin_item;


        public VoteViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            tv_name = view.findViewById(R.id.tv_name);
            im_image = view.findViewById(R.id.im_image);
            tv_details = view.findViewById(R.id.tv_details);
            lin_item = view.findViewById(R.id.lin_item);


            im_give = (ImageView) view.findViewById(R.id.im_give);
            tv_give = (TextView) view.findViewById(R.id.tv_give);

        }
    }

}
