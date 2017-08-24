package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.hgtxxgl.application.utils.hand.PageConfig;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.hgtxxgl.application.R.id.ll_et_search;

public class MyLaunchFragment extends Fragment implements View.OnClickListener{
//    private int currentPage;
    private SearchView etSearch;
    private TextView tvCancel;
    private LinearLayout llEtSearch;
    private RadioButton rbLeft, rbMid, rbRight;
    private RadioGroup group;
    private static int currentTab = 1;
//    private MyLaunchFragment.SectionsPagerAdapter pagerAdapter;
    public static final String ARG_TAB = "TABS";
    private List<RadioButton> radioButtonList;
    private Fragment[] fragments;
    private FragmentManager manager;

    public interface DataCallback {
        void onLoadData();
    }

    //根据不同的pageCode实例化不同的fragment
    public static DetailFragment newInstance(int pageCode) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PageConfig.PAGE_CODE, pageCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    private DetailFragment.DataCallback callback;

    public MyLaunchFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    public static MyLaunchFragment newInstance() {
        MyLaunchFragment fragment = new MyLaunchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.flow_detail, container, false);
        initView(root);
        fragments = new Fragment[2];
        fragments[0] = MyLaunchPeopleFragment.newInstance();
        fragments[1] = MyLaunchCarFragment.newInstance();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //初始化控件并设置搜索框的配置
    private void initView(View view) {
        group = (RadioGroup) view.findViewById(R.id.rg_items);
        rbLeft = (RadioButton) view.findViewById(R.id.rb_left);
        rbMid = (RadioButton) view.findViewById(R.id.rb_mid);
        rbRight = (RadioButton) view.findViewById(R.id.rb_right);
        llEtSearch = (LinearLayout) view.findViewById(ll_et_search);
        etSearch = (SearchView) view.findViewById(R.id.et_search);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
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
        checkTabs(true);
    }

    //搜索逻辑跳转到子fragment界面进行搜索
    public void doFilter(String str){
        Fragment f = fragments[currentTab];
        if (f instanceof MyLaunchPeopleFragment) {
            ((MyLaunchPeopleFragment) f).filter(str);
        } else if (f instanceof MyLaunchCarFragment){
            ((MyLaunchCarFragment) f).filter(str);
        }
    }

    //点击搜索图标或者搜索图标旁的取消按钮来控制搜索框的显示
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

    //根据不同的int参数设置顶部导航栏的按钮数量(暂时设置GONE)
    private void checkTabs(boolean checkButtons) {
        group.setVisibility(VISIBLE);
        if (checkButtons){
            setRadioButtons("人员请假", "车辆外出");
        }
    }

    //根据顶部按钮点击跳转到三个子fragment中的子fragment当中
    public void onButtonClickListner(int radioIndex) {
        if (radioIndex >= 0 && radioIndex <= 2) {
            toggleSearchBox(false);
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            for (int i = 0; i < fragments.length; i++) {
                if (radioIndex == i) {
                    trans.show(fragments[i]);
                } else {
                    trans.hide(fragments[i]);
                }
            }
            trans.commit();
        }
    }

    /**
     * 参数顺序为导航按钮从左至右的次序，参数数量2或者3
     * @param titles
     */
    //设置顶部菜单栏的按钮
    public void setRadioButtons(final String... titles) {
        if (titles == null)
            return;
        if (titles.length < 2 || titles.length > 3) {
            throw new IllegalArgumentException("参数数量为2或者3");
        } else {
            radioButtonList = new ArrayList<>();
            if (titles.length == 2) {
                rbMid.setVisibility(GONE);
                rbLeft.setText(titles[0]);
                rbRight.setText(titles[1]);
                radioButtonList.add(rbLeft);
                radioButtonList.add(rbRight);
            } else {
                rbMid.setVisibility(VISIBLE);
                rbLeft.setText(titles[0]);
                rbMid.setText(titles[1]);
                rbRight.setText(titles[2]);
                radioButtonList.add(rbLeft);
                radioButtonList.add(rbMid);
                radioButtonList.add(rbRight);
            }
            group.setVisibility(VISIBLE);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rb_left) {
                        onButtonClickListner(0);
                    } else if (checkedId == R.id.rb_mid) {
                        onButtonClickListner(1);
                    } else if (checkedId == R.id.rb_right) {
                        if (titles.length == 2) {
                            onButtonClickListner(1);
                        } else {
                            onButtonClickListner(2);
                        }

                    }
                }
            });
        }
    }
}
