package com.example.hgtxxgl.application.fragment.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.PersonalActivity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;

import circletextimage.viviant.com.circletextimagelib.view.CircleTextImage;

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout personCenter;
    private CircleTextImage personImage;
    private TextView tvName;
    private RelativeLayout about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_me, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        personCenter = (RelativeLayout) view.findViewById(R.id.rl_personalcenter);
        personImage = (CircleTextImage) view.findViewById(R.id.presonal_headpic);
        tvName = (TextView) view.findViewById(R.id.tv_person_name);
        about = (RelativeLayout) view.findViewById(R.id.rl_about);
        personCenter.setOnClickListener(this);
        tvName.setOnClickListener(this);
        personImage.setOnClickListener(this);
        about.setOnClickListener(this);
        personImage.setText4CircleImage(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName());
        tvName.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personalcenter:
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                this.startActivity(intent);
                break;
            case R.id.presonal_headpic:
                Intent intent1 = new Intent(getActivity(), PersonalActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.tv_person_name:
                Intent intent2 = new Intent(getActivity(), PersonalActivity.class);
                this.startActivity(intent2);
                break;
            case R.id.rl_about:
                Intent intent3 = new Intent(getActivity(), AboutActivity.class);
                this.startActivity(intent3);
                break;
        }
    }
}
