package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.actions.CommentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/1/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{


    private List<CommentModel> commentModels=new ArrayList<>();
    private Context context;



    public CommentAdapter(Context context) {
        this.context=context;

    }

    public void setCommentModels(List<CommentModel> commentModels) {
        this.commentModels = commentModels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentModel commentModel= commentModels.get(position);
        holder.tv_usernem.setText(context.getResources().getString(R.string.userName,commentModel.username));
        holder.tv_comreview.setText(commentModel.comreview);
        holder.tv_time.setText(commentModel.comtime);


    }

    @Override
    public int getItemCount() {
        if(commentModels.size()==0){
            return 0;
        }else{
            return commentModels.size();
        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_usernem)
        TextView tv_usernem;

//        @BindView(R.id.tv_comreview)
        TextView tv_comreview;

//        @BindView(R.id.tv_time)
        TextView tv_time;



        public ViewHolder(View view) {
            super(view);
            tv_usernem = view.findViewById(R.id.tv_usernem);
            tv_comreview = view.findViewById(R.id.tv_comreview);
            tv_time = view.findViewById(R.id.tv_time);
//            ButterKnife.bind(this,view);
        }
    }



}
