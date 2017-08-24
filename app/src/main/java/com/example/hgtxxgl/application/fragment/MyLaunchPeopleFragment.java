package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hgtxxgl.application.R;

/**
 * Created by HGTXxgl on 2017/8/23.
 */
public class MyLaunchPeopleFragment extends Fragment {
    public static MyLaunchPeopleFragment newInstance() {
        return null;
    }
    private MyLaunchFragment.DataCallback callback;

    public MyLaunchPeopleFragment setCallback(MyLaunchFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public void filter(String str) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.my_layout_people, container, false);
        return root;
    }
}
