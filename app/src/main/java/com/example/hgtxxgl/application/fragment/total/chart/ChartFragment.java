package com.example.hgtxxgl.application.fragment.total.chart;

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

public class ChartFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{
    RadioGroup group;
    RadioButton rbLeft,rbMid;
    private ArrayList<Fragment> fragments;
    private String tempIP;
    private String TAG = "ChartFragment";
    private HandToolbar chartToolbar;
    private String currentNum;
    private String totalNum;
    private String rate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadTotalData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart,container,false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        initView(view);
        return view;
    }

    private void initView(View view) {
        chartToolbar = (HandToolbar) view.findViewById(R.id.chart_handtoolbar);
        chartToolbar.setDisplayHomeAsUpEnabled(true,getActivity());
        chartToolbar.setTitle("单位态势");
        chartToolbar.setTitleSize(18);
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
        ChartFragment fragment = new ChartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void addFragment() {
        fragments.add(new UnitChartPieFragment());
        fragments.add(new UnitChartCurveFragment());
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

//    public void loadTotalData(){
//        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
//        tempIP = share.getString("tempIP", "IP address is empty");
//        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
//        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
//        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
//        countBean.setTableName("PeopleInfo");
//        countBean.setIsAndroid("1");
//        countBean.setbClosed("0");
//        String json = new Gson().toJson(countBean);
//        String request = "Api_Get_Count " + json;
//        L.e(TAG,request);
//        HttpManager.getInstance().requestNewResultForm(tempIP, request, PeopleLeaveCountBean.class, new HttpManager.ResultNewCallback<PeopleLeaveCountBean>() {
//            @Override
//            public void onSuccess(String json, final PeopleLeaveCountBean peopleLeaveCountBean) throws Exception {
//                L.e(TAG,json);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
//                            totalNum = peopleLeaveCountBean.getApi_Get_Count().get(0).getCount();
//                            chartToolbar.setTotalNum(totalNum);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String msg) throws Exception {
//
//            }
//
//            @Override
//            public void onResponse(String response) throws Exception {
//
//            }
//
//            @Override
//            public void onBefore(Request request, int id) throws Exception {
//
//            }
//
//            @Override
//            public void onAfter(int id) throws Exception {
//                loadCurrentData();
//            }
//
//            @Override
//            public void inProgress(float progress, long total, int id) throws Exception {
//
//            }
//        });
//    }

//    private void loadCurrentData() {
//        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
//        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
//        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
//        countBean.setTableName("PeopleInfo");
//        countBean.setIsAndroid("1");
//        countBean.setbClosed("0");
//        countBean.setOutStatus("0");
//        String json = new Gson().toJson(countBean);
//        String request = "Api_Get_Count " + json;
//        L.e(TAG,request);
//        HttpManager.getInstance().requestNewResultForm(tempIP, request, PeopleLeaveCountBean.class, new HttpManager.ResultNewCallback<PeopleLeaveCountBean>() {
//            @Override
//            public void onSuccess(String json, final PeopleLeaveCountBean peopleLeaveCountBean) throws Exception {
//                L.e(TAG,json);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0) {
//                            currentNum = peopleLeaveCountBean.getApi_Get_Count().get(0).getCount();
//                            chartToolbar.setCurrentNum(currentNum);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String msg) throws Exception {
//
//            }
//
//            @Override
//            public void onResponse(String response) throws Exception {
//
//            }
//
//            @Override
//            public void onBefore(Request request, int id) throws Exception {
//
//            }
//
//            @Override
//            public void onAfter(int id) throws Exception {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int num1 = Integer.parseInt(totalNum);
//                        int num2 = Integer.parseInt(currentNum);
//                        // 创建一个数值格式化对象
//                        NumberFormat numberFormat = NumberFormat.getInstance();
//                        // 设置精确到小数点后2位
//                        numberFormat.setMaximumFractionDigits(2);
//                        rate = numberFormat.format((float) num2 / (float) num1 * 100);
//                        chartToolbar.setCurrentRate(rate);
//                    }
//                });
//            }
//
//            @Override
//            public void inProgress(float progress, long total, int id) throws Exception {
//
//            }
//        });
//    }

}
