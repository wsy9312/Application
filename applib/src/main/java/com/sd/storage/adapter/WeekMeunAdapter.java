package com.sd.storage.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.model.WeekModel;

import java.util.List;

/**
 * Created by lenovo on 2018/1/18.
 */

public class WeekMeunAdapter extends RecyclerView.Adapter<WeekMeunAdapter.ViewHolder> implements View.OnClickListener{


    private List<WeekModel> weekModels;
    private   int  point=0;
    private Context context;



    public WeekMeunAdapter(List<WeekModel> weekModels, Context context,int point) {
        this.weekModels = weekModels;
        this.context=context;
        this.point=point;
    }

    public void setPoint(int point) {
        this.point = point;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weekmeun_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeekModel weekModel= weekModels.get(position);
        holder.lin_item.setTag(position);
        holder.tv_name.setTag(position);
        holder.tv_name.setText(weekModel.name);
        holder.lin_item.setOnClickListener(this);
        holder.tv_name.setOnClickListener(this);
        if(position==point){
            holder.lin_item.setBackgroundColor(context.getResources().getColor(R.color.cffffff));
          //  holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.cf00));

        }else{
            holder.lin_item.setBackgroundColor(context.getResources().getColor(R.color.cefefef));
          //  holder.tv_line.setBackgroundColor(context.getResources().getColor(R.color.cffffff));
        }


    }

    @Override
    public int getItemCount() {
        return weekModels.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_line)
        TextView tv_line;
//        @BindView(R.id.lin_item)
        LinearLayout lin_item;


        public ViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this,view);
            tv_name = view.findViewById(R.id.tv_name);
            tv_line = view.findViewById(R.id.tv_line);
            lin_item = view.findViewById(R.id.lin_item);
        }
    }





    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lin_item || i == R.id.tv_name) {
            if (null != onClickItemListener) {
                int position = (int) v.getTag();
                onClickItemListener.onItemOnClick(weekModels.get(position));
            }

        }
    }



    private OnClickItemListener onClickItemListener;

    public void setOnClickListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }


    public interface OnClickItemListener {
        void onItemOnClick(WeekModel item);
    }

}
