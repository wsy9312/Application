package com.example.hgtxxgl.application.fragment.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.PersonalActivity;

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout personCenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_me, container, false);
//        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.personal_headarea);
//        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
//        handToolbar.setBackHome(false,0);
        initview(view);
        return view;
    }

    private void initview(View view) {
        personCenter = (RelativeLayout) view.findViewById(R.id.rl_personalcenter);
        personCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personalcenter:
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                this.startActivity(intent);
                break;
        }
    }
}
