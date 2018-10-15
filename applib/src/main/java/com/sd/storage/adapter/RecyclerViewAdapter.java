package com.sd.storage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
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

public class RecyclerViewAdapter extends Adapter<ViewHolder> implements View.OnClickListener {


    //早餐breakfast
    private static final int TYPE_BREAKFASE = 0;
    private static final int TYPE_ITEM1 = 1;
    //午餐lunch
    private static final int TYPE_LUNCH = 2;
    private static final int TYPE_ITEM2 = 3;
    //晚餐afternoon
    private static final int TYPE_AFERNOON = 4;

    private static final int TYPE_ITEM3 = 5;


    private Context context;

    //早餐列表
    private ArrayList<VageModel> vageModels1 = new ArrayList<>();
    //午餐列表
    private ArrayList<VageModel> vageModels2 = new ArrayList<>();
    //晚餐列表
    private ArrayList<VageModel> vageModels3 = new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        this.context = context;

    }

    public void setData(ArrayList<VageModel> vage1, ArrayList<VageModel> vage2, ArrayList<VageModel> vage3) {
        this.vageModels1.clear();
        this.vageModels2.clear();
        this.vageModels3.clear();
        this.vageModels1.addAll(vage1);
        this.vageModels2.addAll(vage2);
        this.vageModels3.addAll(vage3);
        notifyDataSetChanged();

    }


    public void setNewData(int point1, int point2, int point3, int i1, int i2, int i3, int vegestate, String vegegive) {


        if (-1 != point1) {
            this.vageModels1.get(i1).setVegestate(vegestate);
            this.vageModels1.get(i1).setVegegive(vegegive);
            notifyItemChanged(point1, 0);
        }

        if (-1 != point2) {
            this.vageModels2.get(i2).setVegestate(vegestate);
            this.vageModels2.get(i2).setVegegive(vegegive);
            notifyItemChanged(point2, 0);
        }
        if (-1 != point3) {
            this.vageModels3.get(i3).setVegestate(vegestate);
            this.vageModels3.get(i3).setVegegive(vegegive);
            notifyItemChanged(point3, 0);
        }


    }

    //  删除数据
    public void removeData(int type, int tpoint, int position) {
        if (type == 1) {
            vageModels1.remove(tpoint);
        }
        if (type == 2) {
            vageModels2.remove(tpoint);
        }
        if (type == 3) {
            vageModels3.remove(tpoint);
        }
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (vageModels1.size() == 0 && vageModels2.size() == 2 && vageModels3.size() == 0) {
            return 0;
        } else {
            return 1 + vageModels1.size() + 1 + vageModels2.size() + 1 + vageModels3.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BREAKFASE;
        } else if (position <= vageModels1.size() && position > 0) {
            return TYPE_ITEM1;
        } else if (position == 1 + vageModels1.size()) {
            return TYPE_LUNCH;
        } else if (position > 1 + vageModels1.size() && position <= vageModels1.size() + 1 + vageModels2.size()) {
            return TYPE_ITEM2;
        } else if (position == (vageModels1.size() + vageModels2.size() + 2)) {
            return TYPE_AFERNOON;
        } else {
            return TYPE_ITEM3;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BREAKFASE) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_breakfast, parent,
                    false);
            return new BreakfaseHolder(view);
        }


        if (viewType == TYPE_ITEM1) {
            View view = LayoutInflater.from(context).inflate(R.layout.week_item_base, parent,
                    false);
            return new ItemFaseViewHolder(view);
        }
        if (viewType == TYPE_LUNCH) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_breakfast, parent,
                    false);
            return new LunchHolder(view);
        }
        if (viewType == TYPE_ITEM2) {
            View view = LayoutInflater.from(context).inflate(R.layout.week_item_base, parent,
                    false);
            return new ItemLunchViewHolder(view);
        }
        if (viewType == TYPE_AFERNOON) {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_breakfast, parent,
                    false);
            return new AfterNoonHolder(view);
        }

        if (viewType == TYPE_ITEM3) {
            View view = LayoutInflater.from(context).inflate(R.layout.week_item_base, parent,
                    false);
            return new ItemAfterNoonViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position, List payloads) {

        if (payloads.isEmpty()) {

            if (holder instanceof BreakfaseHolder) {
                ((BreakfaseHolder) holder).tv.setText(R.string.str_breakfast);
                ((BreakfaseHolder) holder).tv.setTag(1);
                ((BreakfaseHolder) holder).tv.setOnClickListener(this);
            }

            if (holder instanceof LunchHolder) {
                ((LunchHolder) holder).tv.setText(R.string.str_lunch);
                ((LunchHolder) holder).tv.setTag(2);
                ((LunchHolder) holder).tv.setOnClickListener(this);
            }

            if (holder instanceof AfterNoonHolder) {
                ((AfterNoonHolder) holder).tv.setText(R.string.str_afternoon);
                ((AfterNoonHolder) holder).tv.setTag(3);
                ((AfterNoonHolder) holder).tv.setOnClickListener(this);

            }


            if (holder instanceof ItemFaseViewHolder) {
                VageModel vageModel = vageModels1.get(position - 1);

                ((ItemFaseViewHolder) holder).tv_name.setText(vageModel.vegename);
                ((ItemFaseViewHolder) holder).tv_details.setText(vageModel.vegedesc);
                ((ItemFaseViewHolder) holder).tv_give.setText(vageModel.vegegive);
                if(!"".equals(vageModel.vegecomment )&& null!=vageModel.vegecomment){
                    ((ItemFaseViewHolder) holder).tv_conmment.setText(vageModel.vegecomment);
                }



                String url = vageModel.vegeimg;
                if (null != url) {
                    Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(((ItemFaseViewHolder) holder).im_image);
                }
                //赞的总数
                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemFaseViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemFaseViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }

                String heatid = vageModel.heatid;
                if (heatid.equals("0")) {
                    ((ItemFaseViewHolder) holder).tv_meunestatus.setVisibility(View.VISIBLE);
                } else {
                    ((ItemFaseViewHolder) holder).tv_meunestatus.setVisibility(View.GONE);
                }


                ((ItemFaseViewHolder) holder).lin_item.setTag(position);
                ((ItemFaseViewHolder) holder).lin_item.setOnClickListener(this);

                ((ItemFaseViewHolder) holder).im_commint.setTag(position);
                ((ItemFaseViewHolder) holder).im_commint.setOnClickListener(this);

                ((ItemFaseViewHolder) holder).im_give.setTag(position);
                ((ItemFaseViewHolder) holder).im_give.setOnClickListener(this);

                ((ItemFaseViewHolder) holder).im_delet.setTag(position);
                ((ItemFaseViewHolder) holder).im_delet.setOnClickListener(this);

                if ("1".equals(UrlManager.getLEVEl())) {
                    ((ItemFaseViewHolder) holder).im_delet.setVisibility(View.VISIBLE);
                }else{
                    ((ItemFaseViewHolder) holder).im_delet.setVisibility(View.GONE);
                }

            }

            if (holder instanceof ItemLunchViewHolder) {
                VageModel vageModel = vageModels2.get(position - 1 - vageModels1.size() - 1);

                String url = vageModel.vegeimg;
                if (null != url) {
                    Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(((ItemLunchViewHolder) holder).im_image);
                }
                ((ItemLunchViewHolder) holder).tv_name.setText(vageModel.vegename);
                ((ItemLunchViewHolder) holder).tv_details.setText(vageModel.vegedesc);
                ((ItemLunchViewHolder) holder).tv_give.setText(vageModel.vegegive);
                if(!"".equals(vageModel.vegecomment )&& null!=vageModel.vegecomment){
                ((ItemLunchViewHolder) holder).tv_conmment.setText(vageModel.vegecomment);}
                //赞的总数
                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemLunchViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemLunchViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }
                String heatid = vageModel.heatid;
                if (heatid.equals("0")) {
                    ((ItemLunchViewHolder) holder).tv_meunestatus.setVisibility(View.VISIBLE);
                } else {
                    ((ItemLunchViewHolder) holder).tv_meunestatus.setVisibility(View.GONE);
                }


                ((ItemLunchViewHolder) holder).lin_item.setTag(position);
                ((ItemLunchViewHolder) holder).lin_item.setOnClickListener(this);

                ((ItemLunchViewHolder) holder).im_commint.setTag(position);
                ((ItemLunchViewHolder) holder).im_commint.setOnClickListener(this);

                ((ItemLunchViewHolder) holder).im_give.setTag(position);
                ((ItemLunchViewHolder) holder).im_give.setOnClickListener(this);

                ((ItemLunchViewHolder) holder).im_delet.setTag(position);
                ((ItemLunchViewHolder) holder).im_delet.setOnClickListener(this);

                if ("1".equals(UrlManager.getLEVEl())) {
                    ((ItemLunchViewHolder) holder).im_delet.setVisibility(View.VISIBLE);
                }else{
                    ((ItemLunchViewHolder) holder).im_delet.setVisibility(View.GONE);
                }
            }

            if (holder instanceof ItemAfterNoonViewHolder) {
                VageModel vageModel = vageModels3.get(position - 1 - vageModels1.size() - 1 - vageModels2.size() - 1);

                String url = vageModel.vegeimg;
                if (null != url) {
                    Glide.with(context).load(UrlManager.getUrlPath() + url).error(R.drawable.err_image).into(((ItemAfterNoonViewHolder) holder).im_image);
                }
                ((ItemAfterNoonViewHolder) holder).tv_name.setText(vageModel.vegename);
                ((ItemAfterNoonViewHolder) holder).tv_details.setText(vageModel.vegedesc);
                //赞的总数
                ((ItemAfterNoonViewHolder) holder).tv_give.setText(vageModel.vegegive);
                if(!"".equals(vageModel.vegecomment )&& null!=vageModel.vegecomment){
                ((ItemAfterNoonViewHolder) holder).tv_conmment.setText(vageModel.vegecomment);}

                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemAfterNoonViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemAfterNoonViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }

                String heatid = vageModel.heatid;
                if (heatid.equals("0")) {
                    ((ItemAfterNoonViewHolder) holder).tv_meunestatus.setVisibility(View.VISIBLE);
                } else {
                    ((ItemAfterNoonViewHolder) holder).tv_meunestatus.setVisibility(View.GONE);
                }

                ((ItemAfterNoonViewHolder) holder).lin_item.setTag(position);
                ((ItemAfterNoonViewHolder) holder).lin_item.setOnClickListener(this);


                ((ItemAfterNoonViewHolder) holder).im_commint.setTag(position);
                ((ItemAfterNoonViewHolder) holder).im_commint.setOnClickListener(this);

                ((ItemAfterNoonViewHolder) holder).im_give.setTag(position);
                ((ItemAfterNoonViewHolder) holder).im_give.setOnClickListener(this);

                ((ItemAfterNoonViewHolder) holder).im_delet.setTag(position);
                ((ItemAfterNoonViewHolder) holder).im_delet.setOnClickListener(this);

                if ("1".equals(UrlManager.getLEVEl())) {
                    ((ItemAfterNoonViewHolder) holder).im_delet.setVisibility(View.VISIBLE);
                }else{
                    ((ItemAfterNoonViewHolder) holder).im_delet.setVisibility(View.GONE);
                }

            }
        } else {


            if (holder instanceof ItemFaseViewHolder) {
                VageModel vageModel = vageModels1.get(position - 1);
                ((ItemFaseViewHolder) holder).tv_give.setText(vageModel.vegegive);
                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemFaseViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemFaseViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }

            }


            if (holder instanceof ItemLunchViewHolder) {

                VageModel vageModel = vageModels2.get(position - 1 - vageModels1.size() - 1);

                ((ItemLunchViewHolder) holder).tv_give.setText(vageModel.vegegive);
                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemLunchViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemLunchViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }
            }

            if (holder instanceof ItemAfterNoonViewHolder) {
                VageModel vageModel = vageModels3.get(position - 1 - vageModels1.size() - 1 - vageModels2.size() - 1);

                ((ItemAfterNoonViewHolder) holder).tv_give.setText(vageModel.vegegive);
                int vegestate = vageModel.vegestate;
                if (vegestate == 0) {
                    ((ItemAfterNoonViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_false);
                } else {
                    ((ItemAfterNoonViewHolder) holder).im_give.setBackgroundResource(R.drawable.give_up_true);
                }

            }


        }


    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.lin_item) {
            if (null != onItemVageClickListener) {
                int point = (int) view.getTag();
                if (point > 0 && point <= vageModels1.size()) {
                    onItemVageClickListener.onItemVageClick(1, point - 1);
                } else if (point > 1 + vageModels1.size() && point <= vageModels1.size() + 1 + vageModels2.size()) {
                    onItemVageClickListener.onItemVageClick(2, point - 1 - vageModels1.size() - 1);
                } else {
                    onItemVageClickListener.onItemVageClick(3, point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1);
                }
            }

        } else if (i == R.id.im_commint) {
            if (null != onItemVageClickListener) {
                int point = (int) view.getTag();
                if (point > 0 && point <= vageModels1.size()) {
                    onItemVageClickListener.onItemAddCommentClick(1, point - 1);
                } else if (point > 1 + vageModels1.size() && point <= vageModels1.size() + 1 + vageModels2.size()) {
                    onItemVageClickListener.onItemAddCommentClick(2, point - 1 - vageModels1.size() - 1);
                } else {
                    onItemVageClickListener.onItemAddCommentClick(3, point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1);
                }
            }

        } else if (i == R.id.im_give) {
            if (null != onItemVageClickListener) {
                int point = (int) view.getTag();
                if (point > 0 && point <= vageModels1.size()) {
                    VageModel vageMode = vageModels1.get(point - 1);
                    onItemVageClickListener.onItemGiveClick2(vageMode.vegeid, vageMode.vegegive, vageMode.vegestate);
                    //  onItemVageClickListener.onItemGiveClick(1, point - 1, point);
                } else if (point > 1 + vageModels1.size() && point <= vageModels1.size() + 1 + vageModels2.size()) {
                    // onItemVageClickListener.onItemGiveClick(2, point - 1 - vageModels1.size() - 1, point);
                    VageModel vageMode = vageModels2.get(point - 1 - vageModels1.size() - 1);
                    onItemVageClickListener.onItemGiveClick2(vageMode.vegeid, vageMode.vegegive, vageMode.vegestate);

                } else {
                    // onItemVageClickListener.onItemGiveClick(3, point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1, point);
                    VageModel vageMode = vageModels3.get(point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1);
                    onItemVageClickListener.onItemGiveClick2(vageMode.vegeid, vageMode.vegegive, vageMode.vegestate);
                }
            }

        } else if (i == R.id.tv_type) {
            if (null != onItemVageClickListener) {
                if ("1".equals(UrlManager.getLEVEl())) {
                    onItemVageClickListener.onAddVegeClick(String.valueOf(view.getTag()));
                }

            }

        } else if (i == R.id.im_delet) {
            if (null != onItemVageClickListener) {
                int point = (int) view.getTag();
                if (point > 0 && point <= vageModels1.size()) {
                    onItemVageClickListener.onDeletitem(1, vageModels1.get(point - 1).foodid, point - 1, point);
                } else if (point > 1 + vageModels1.size() && point <= vageModels1.size() + 1 + vageModels2.size()) {
                    onItemVageClickListener.onDeletitem(2, vageModels2.get(point - 1 - vageModels1.size() - 1).foodid, point - 1 - vageModels1.size() - 1, point);
                } else {
                    onItemVageClickListener.onDeletitem(3, vageModels3.get(point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1).foodid, point - 1 - vageModels1.size() - 1 - vageModels2.size() - 1, point);
                }
            }

        }
    }

    public interface OnItemVageClickListener {
        void onItemVageClick(int type, int point);

        void onItemAddCommentClick(int type, int point);

        void onItemGiveClick(int type, int point, int i);

        void onItemGiveClick2(String vegeid, String vegegive, int vegestate);

        void onAddVegeClick(String typeid);

        void onDeletitem(int type, String foodid, int tpoint, int point);

    }

    private OnItemVageClickListener onItemVageClickListener;

    public void setOnVageItemClickListener(OnItemVageClickListener onItemVageClickListener) {

        this.onItemVageClickListener = onItemVageClickListener;
    }

    /**
     * 晚餐列表
     */
    public static class ItemAfterNoonViewHolder extends ViewHolder {

//        @BindView(R.id.im_image)
        ImageView im_image;
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_details)
        TextView tv_details;

//        @BindView(R.id.tv_conmment)
        TextView tv_conmment;
//        @BindView(R.id.tv_meunestatus)
        TextView tv_meunestatus;


        public TextView tv_give;
        public ImageView im_give;
//        @BindView(R.id.im_commint)
        ImageView im_commint;

//        @BindView(R.id.im_delet)
        ImageView im_delet;

//        @BindView(R.id.lin_item)
        LinearLayout lin_item;

        public ItemAfterNoonViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            im_image = (ImageView) view.findViewById(R.id.im_image);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_details = (TextView) view.findViewById(R.id.tv_details);
            tv_conmment = (TextView) view.findViewById(R.id.tv_conmment);
            tv_meunestatus = (TextView) view.findViewById(R.id.tv_meunestatus);
            im_commint = (ImageView) view.findViewById(R.id.im_commint);
            im_delet = (ImageView) view.findViewById(R.id.im_delet);
            lin_item = (LinearLayout) view.findViewById(R.id.lin_item);

            im_give = (ImageView) view.findViewById(R.id.im_give);
            tv_give = (TextView) view.findViewById(R.id.tv_give);

        }
    }


    /**
     * 午餐列表
     */
    public static class ItemLunchViewHolder extends ViewHolder {
//        @BindView(R.id.im_image)
        ImageView im_image;
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_details)
        TextView tv_details;

//        @BindView(R.id.tv_conmment)
        TextView tv_conmment;
//        @BindView(R.id.tv_meunestatus)
        TextView tv_meunestatus;


        public TextView tv_give;
        public ImageView im_give;
//        @BindView(R.id.im_commint)
        ImageView im_commint;

//        @BindView(R.id.im_delet)
        ImageView im_delet;

//        @BindView(R.id.lin_item)
        LinearLayout lin_item;

        public ItemLunchViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            im_image = view.findViewById(R.id.im_image);
            tv_name = view.findViewById(R.id.tv_name);
            tv_details = view.findViewById(R.id.tv_details);
            tv_conmment = view.findViewById(R.id.tv_conmment);
            tv_meunestatus = view.findViewById(R.id.tv_meunestatus);
            im_commint = view.findViewById(R.id.im_commint);
            im_delet = view.findViewById(R.id.im_delet);
            lin_item = view.findViewById(R.id.lin_item);

            im_give = (ImageView) view.findViewById(R.id.im_give);
            tv_give = (TextView) view.findViewById(R.id.tv_give);

        }
    }

    /**
     * 早餐列表
     */
    public static class ItemFaseViewHolder extends ViewHolder {
//        @BindView(R.id.im_image)
        ImageView im_image;
//        @BindView(R.id.tv_name)
        TextView tv_name;
//        @BindView(R.id.tv_details)
        TextView tv_details;
//        @BindView(R.id.tv_conmment)
        TextView tv_conmment;
//        @BindView(R.id.tv_meunestatus)
        TextView tv_meunestatus;


        public TextView tv_give;
        public ImageView im_give;
//        @BindView(R.id.im_commint)
        ImageView im_commint;

//        @BindView(R.id.lin_item)
        LinearLayout lin_item;

//        @BindView(R.id.im_delet)
        ImageView im_delet;


        public ItemFaseViewHolder(View view) {
            super(view);
//            ButterKnife.bind(this, view);
            im_delet = view.findViewById(R.id.im_delet);
            lin_item = view.findViewById(R.id.lin_item);
            im_commint = view.findViewById(R.id.im_commint);
            tv_meunestatus = view.findViewById(R.id.tv_meunestatus);
            tv_conmment = view.findViewById(R.id.tv_conmment);
            tv_details = view.findViewById(R.id.tv_details);
            tv_name = view.findViewById(R.id.tv_name);
            im_image = view.findViewById(R.id.im_image);

            im_give = (ImageView) view.findViewById(R.id.im_give);
            tv_give = (TextView) view.findViewById(R.id.tv_give);

        }
    }

    /**
     * 晚饭
     */
    static class AfterNoonHolder extends ViewHolder {
        TextView tv;

        public AfterNoonHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_type);
        }
    }

    /**
     * 午饭
     */
    static class LunchHolder extends ViewHolder {
        TextView tv;

        public LunchHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_type);
        }
    }

    /**
     * 早餐
     */
    static class BreakfaseHolder extends ViewHolder {
        TextView tv;

        public BreakfaseHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_type);
        }


    }


    static class FootViewHolder extends ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }


}