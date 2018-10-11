package com.example.hgtxxgl.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.news.NewFragment;
import com.example.hgtxxgl.application.fragment.notification.NotificationFragment;
import com.example.hgtxxgl.application.fragment.personal.PersonalFragment;
import com.example.hgtxxgl.application.fragment.total.TotalFragment;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.sd.storage.ui.main.MenuMainFragment;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
//首页四个fragment的通类
public class DetailFragment extends Fragment {
    private int currentPage;
    private RadioButton rbLeft, rbMid, rbRight;
    private RadioGroup group;
    private static int currentTab = 0;
    private DetailFragment.SectionsPagerAdapter pagerAdapter;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.flow_detail, container, false);
        initView(root);
        if (pagerAdapter == null) {
            manager = getChildFragmentManager();
            pagerAdapter = new DetailFragment.SectionsPagerAdapter(getChildFragmentManager());
            fragments = new Fragment[pagerAdapter.getCount()];
            FragmentTransaction trans = manager.beginTransaction();
            for (int i = 0; i < fragments.length; i++) {
                fragments[i] = pagerAdapter.getItem(i);
                trans.add(R.id.container, fragments[i]);
                if (i == 0) {
                    trans.show(fragments[i]);
                } else {
                    trans.hide(fragments[i]);
                }
            }
            trans.commit();
        }
        return root;
    }

    //fragmentPagerAdapter设置对应的fragment
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements DataCallback {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (currentPage == PageConfig.PAGE_NEWS) {
                return NewFragment.newInstance(position).setCallback(this);
            } else if (currentPage == PageConfig.PAGE_NOTIFICATION) {
                return NotificationFragment.newInstance(position).setCallback(this);
            } else if (currentPage == PageConfig.PAGE_TOTAL){
//                return new LaunchDetailFragment();
                return new TotalFragment();
            } else if (currentPage == PageConfig.PAGE_LIFE) {
                return new MenuMainFragment();
            } else if (currentPage == PageConfig.PAGE_ME) {
//                return new CommissionDetailFragment();
                return new PersonalFragment();
            }
            return null;
        }


        @Override
        public int getCount() {

            if (currentPage == PageConfig.PAGE_NEWS) {
                return 1;
            } else if (currentPage == PageConfig.PAGE_NOTIFICATION) {
                return 1;
            } else if (currentPage == PageConfig.PAGE_TOTAL) {
                return 1;
            } else if (currentPage == PageConfig.PAGE_ME)
                return 1;
            else
                return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position + "";
        }

        @Override
        public void onLoadData() {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = getArguments().getInt(PageConfig.PAGE_CODE);
    }

    //初始化控件并设置搜索框的配置
    private void initView(View view) {
        group = (RadioGroup) view.findViewById(R.id.rg_items);
        rbLeft = (RadioButton) view.findViewById(R.id.rb_left);
        rbMid = (RadioButton) view.findViewById(R.id.rb_mid);
        rbRight = (RadioButton) view.findViewById(R.id.rb_right);
        checkTabs(currentPage, true);
    }

    //根据不同的int参数设置顶部导航栏的按钮数量(暂时设置GONE)
    private void checkTabs(int page, boolean checkButtons) {
        group.setVisibility(GONE);
    }

    //根据顶部按钮点击跳转到三个子fragment中的子fragment当中
    public void onButtonClickListner(int radioIndex) {
        if (radioIndex >= 0 && radioIndex <= 2) {
            currentTab = radioIndex;
            FragmentTransaction trans = getChildFragmentManager().beginTransaction();
            for (int i = 0; i < fragments.length; i++) {
                if (currentTab == i) {
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
