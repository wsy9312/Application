package com.example.hgtxxgl.application.QrCode.sample;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hgtxxgl.application.R;
import com.mylhyl.cygadapter.CygAdapter;
import com.mylhyl.cygadapter.CygViewHolder;

import java.util.List;

/**
 * 照片浏览
 */
class PickPictureAdapter extends CygAdapter<String> {

    public PickPictureAdapter(Context context, List<String> datas) {
        super(context, R.layout.activity_pick_picture_grid_item, datas);
    }

    @Override
    public void onBindData(CygViewHolder viewHolder, String item, int position) {
        ImageView imageView = viewHolder.findViewById(R.id.activity_pick_picture_grid_item_image);
        Glide.with(mContext).load(item).into(imageView);
    }
}
