package com.example.hgtxxgl.application.fragment.total;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.view.HandToolbar;

public class TotalFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_total, container, false);
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.total_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("工作");
        handToolbar.setTitleSize(18);
        return view;
    }
}
