package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.hgtxxgl.application.R.id.ll_et_search;
//申请合列表
public class LaunchDetailFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {
    RadioGroup group;
    RadioButton rbLeft,rbMid;
    private ArrayList<Fragment> fragments;

    public LaunchDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private LinearLayout llEtSearch;
    private SearchView etSearch;
    private TextView tvCancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.flow_launch_detail, container, false);
        //创建fragment实例并把他们加入集合
        fragments = new ArrayList<>();
        group = (RadioGroup) root.findViewById(R.id.rg_items);
        rbLeft = (RadioButton) root.findViewById(R.id.rb_left);
        rbMid = (RadioButton) root.findViewById(R.id.rb_mid);
        llEtSearch = (LinearLayout) root.findViewById(ll_et_search);
        etSearch = (SearchView) root.findViewById(R.id.et_search);
        tvCancel = (TextView) root.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        etSearch.setSubmitButtonEnabled(true);
        etSearch.setIconifiedByDefault(false);
        etSearch.setFocusableInTouchMode(false);
        etSearch.setFocusable(false);
        etSearch.setOnClickListener(this);
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doFilter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doFilter(newText);
                return true;
            }
        });
        addFragment();
        //设置默认被选中的RadioButton
        group.check(R.id.rb_left);
        switchFragment(0);
        //radiogroup中的radiobutton的点击事件
        group.setOnCheckedChangeListener(this);
        //radiogroup中加号的点击事件
        return root;
    }

    public void doFilter(String str){
        Fragment f1 = fragments.get(0);
        Fragment f2 = fragments.get(1);
        if (f1 instanceof MyLaunchPeopleFragment) {
            ((MyLaunchPeopleFragment) f1).filter(str);
        }
        if (f2 instanceof MyLaunchCarFragment) {
            ((MyLaunchCarFragment) f2).filter(str);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_search) {
            etSearch.setFocusable(true);
            toggleSearchBox(true);
        } else if (v.getId() == R.id.tv_cancel) {
            toggleSearchBox(false);
        }
    }

    //搜索框开关
    private void toggleSearchBox(boolean enable) {
        if (enable) {
            etSearch.setFocusableInTouchMode(true);
            etSearch.requestFocus();
            tvCancel.setVisibility(VISIBLE);
        } else {
            //  etSearch.setText("");
            tvCancel.setVisibility(GONE);
            etSearch.setFocusableInTouchMode(false);
            etSearch.clearFocus();
        }
    }

    private void addFragment() {
        fragments.add(new MyLaunchPeopleFragment());
        fragments.add(new MyLaunchCarFragment());
    }

    public void switchFragment(int position) {
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        for (int i = 0; i <fragments.size() ; i++) {
            Fragment fragment = fragments.get(i);
            if (i==position){
                if (fragment.isAdded()){
                    fragmentTransaction.show(fragment);
                }else{
                    fragmentTransaction.add(R.id.container,fragment);
                }
            }else{
                if (fragment.isAdded()){
                    fragmentTransaction.hide(fragment);
                }
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_left:
                switchFragment(0);
                break;
            case R.id.rb_mid:
                switchFragment(1);
                break;
        }
    }

    public static Fragment newInstance(int pageTodoTotal) {
        LaunchDetailFragment fragment = new LaunchDetailFragment();
        return fragment;
    }

}
