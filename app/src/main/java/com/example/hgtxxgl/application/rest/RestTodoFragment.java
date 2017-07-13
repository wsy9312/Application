package com.example.hgtxxgl.application.rest;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.hgtxxgl.application.fragment.DetailFragment;

/**
 * Created by HGTXxgl on 2017/7/13.
 */
public class RestTodoFragment extends Fragment{

    public RestTodoFragment() {
    }

    public static RestTodoFragment newInstance(int tabIndex) {
        Bundle args = new Bundle();
        RestTodoFragment fragment = new RestTodoFragment();
        args.putInt(DetailFragment.ARG_TAB, tabIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(Bundle data) {
        return null;
    }

}
