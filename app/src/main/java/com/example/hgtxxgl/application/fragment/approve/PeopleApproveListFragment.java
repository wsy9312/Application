package com.example.hgtxxgl.application.fragment.approve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;

import java.util.ArrayList;

public class PeopleApproveListFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    RadioGroup group;
    RadioButton rbLeft,rbMid;
    private ArrayList<Fragment> fragments;

    public PeopleApproveListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flow_people_approve, container, false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        initView(view);
        return view;
    }

    private void initView(View view) {
        HandToolbar handToolbar = (HandToolbar) view.findViewById(R.id.people_approve_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        handToolbar.setTitle("我审批的(人员)");
        handToolbar.setTitleSize(18);
        fragments = new ArrayList<>();
        group = (RadioGroup) view.findViewById(R.id.rg_items);
        rbLeft = (RadioButton) view.findViewById(R.id.rb_left);
        rbMid = (RadioButton) view.findViewById(R.id.rb_mid);
        addFragment();
        //设置默认被选中的RadioButton
        group.check(R.id.rb_left);
        switchFragment(0);
        //radiogroup中的radiobutton的点击事件
        group.setOnCheckedChangeListener(this);
        //radiogroup中加号的点击事件
    }

    public static Fragment newInstance(Bundle bundle) {
        PeopleApproveListFragment fragment = new PeopleApproveListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void addFragment() {
        fragments.add(new PeopleApproveDelayListFragment());
        fragments.add(new PeopleApproveFinishListFragment());
    }

    public void switchFragment(int position) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
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
}
