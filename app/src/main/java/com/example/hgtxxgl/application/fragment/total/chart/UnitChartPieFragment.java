package com.example.hgtxxgl.application.fragment.total.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.PeopleLeaveCountBean;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.idtk.smallchart.chart.PieChart;
import com.idtk.smallchart.data.PieData;
import com.idtk.smallchart.interfaces.iData.IPieData;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UnitChartPieFragment extends Fragment{
    private ArrayList<IPieData> mPieDataListOut = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListRest = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListSick = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListWork = new ArrayList<>();
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00, 0xFF805677};
    private PeopleLeaveCountBean.ApiGetCountBean countBean;
    private PeopleLeaveCountBean.ApiGetCountBean currentBean;
    private PeopleLeaveCountBean.ApiGetCountBean workBean;
    private PeopleLeaveCountBean.ApiGetCountBean restBean;
    private PeopleLeaveCountBean.ApiGetCountBean outBean;
    private PeopleLeaveCountBean.ApiGetCountBean sickBean;
    private PieChart pieChartOut;
    private PieChart pieChartRest;
    private PieChart pieChartSick;
    private PieChart pieChartWork;
    private TextView tvOutNum;
    private TextView tvWorkNum;
    private TextView tvSickNum;
    private TextView tvRestNum;
    private TextView tvOutRate;
    private TextView tvWorkRate;
    private TextView tvSickRate;
    private TextView tvRestRate;
    private TextView tvTotalNum;
    private TextView tvCurrentNum;
    private TextView tvCurrentRate;

    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        return valueDP;
    }

    public static UnitChartPieFragment newInstance(Bundle bundle) {
        UnitChartPieFragment fragment = new UnitChartPieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unitchart,container,false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        inintView(view);
        initChartData();
        initTextData();
        return view;
    }

    private void inintView(View view) {
        countBean = ApplicationApp.getCountBean().getApi_Get_Count().get(0);
        currentBean = ApplicationApp.getCurrentCountBean().getApi_Get_Count().get(0);
        workBean = ApplicationApp.getWorkCountBean().getApi_Get_Count().get(0);
        restBean = ApplicationApp.getRestCountBean().getApi_Get_Count().get(0);
        outBean = ApplicationApp.getOutCountBean().getApi_Get_Count().get(0);
        sickBean = ApplicationApp.getSickCountBean().getApi_Get_Count().get(0);

        tvTotalNum = (TextView) view.findViewById(R.id.tv_zrs);
        tvCurrentNum = (TextView) view.findViewById(R.id.tv_zwrs);
        tvCurrentRate = (TextView) view.findViewById(R.id.tv_zwl);

        pieChartOut = (PieChart) view.findViewById(R.id.pieChart_out);
        pieChartRest = (PieChart) view.findViewById(R.id.pieChart_rest);
        pieChartSick = (PieChart) view.findViewById(R.id.pieChart_sick);
        pieChartWork = (PieChart) view.findViewById(R.id.pieChart_work);

        tvOutNum = (TextView) view.findViewById(R.id.tv_table_outnum);
        tvWorkNum = (TextView) view.findViewById(R.id.tv_table_worknum);
        tvSickNum = (TextView) view.findViewById(R.id.tv_table_sicknum);
        tvRestNum = (TextView) view.findViewById(R.id.tv_table_restnum);

        tvOutRate = (TextView) view.findViewById(R.id.tv_table_outrate);
        tvWorkRate = (TextView) view.findViewById(R.id.tv_table_workrate);
        tvSickRate = (TextView) view.findViewById(R.id.tv_table_sickrate);
        tvRestRate = (TextView) view.findViewById(R.id.tv_table_restrate);

    }

    private void initTextData() {
        tvTotalNum.setText("总人数:"+countBean.getCount());
        tvCurrentNum.setText("在位人数:"+currentBean.getCount());
        tvCurrentRate.setText("在位率:"+getRate(currentBean.getCount())+"%");

        tvOutNum.setText(outBean.getCount()+"人");
        tvWorkNum.setText(workBean.getCount()+"人");
        tvSickNum.setText(sickBean.getCount()+"人");
        tvRestNum.setText(restBean.getCount()+"人");

        tvOutRate.setText(getRate(outBean.getCount())+"%");
        tvWorkRate.setText(getRate(workBean.getCount())+"%");
        tvSickRate.setText(getRate(sickBean.getCount())+"%");
        tvRestRate.setText(getRate(restBean.getCount())+"%");
    }

    private void initChartData() {
        PieData pieData3 = new PieData();
        pieData3.setName("事假");
        pieData3.setValue(getRate(workBean.getCount()));
        pieData3.setColor(mColors[4]);
        mPieDataListWork.add(pieData3);
        PieData pieData4 = new PieData();
        pieData4.setValue(100F-getRate(workBean.getCount()));
        pieData4.setColor(mColors[6]);
        mPieDataListWork.add(pieData4);
        pieChartWork.setDataList(mPieDataListWork);
        pieChartWork.setAxisColor(Color.WHITE);
        pieChartWork.setAxisTextSize(pxTodp(13));

        PieData pieData1 = new PieData();
        pieData1.setName("休假");
        pieData1.setValue(getRate(restBean.getCount()));
        pieData1.setColor(mColors[5]);
        mPieDataListRest.add(pieData1);
        PieData pieData2 = new PieData();
        pieData2.setValue(100F-getRate(restBean.getCount()));
        pieData2.setColor(mColors[6]);
        mPieDataListRest.add(pieData2);
        pieChartRest.setDataList(mPieDataListRest);
        pieChartRest.setAxisColor(Color.WHITE);
        pieChartRest.setAxisTextSize(pxTodp(13));

        PieData pieData5 = new PieData();
        pieData5.setName("病假");
        pieData5.setValue(getRate(sickBean.getCount()));
        pieData5.setColor(mColors[7]);
        mPieDataListSick.add(pieData5);
        PieData pieData6 = new PieData();
        pieData6.setValue(100F-getRate(sickBean.getCount()));
        pieData6.setColor(mColors[6]);
        mPieDataListSick.add(pieData6);
        pieChartSick.setDataList(mPieDataListSick);
        pieChartSick.setAxisColor(Color.WHITE);
        pieChartSick.setAxisTextSize(pxTodp(13));

        PieData pieData7 = new PieData();
        pieData7.setName("外出");
        pieData7.setValue(getRate(outBean.getCount()));
        pieData7.setColor(mColors[1]);
        mPieDataListOut.add(pieData7);
        PieData pieData8 = new PieData();
        pieData8.setValue(100F-getRate(outBean.getCount()));
        pieData8.setColor(mColors[6]);
        mPieDataListOut.add(pieData8);
        pieChartOut.setDataList(mPieDataListOut);
        pieChartOut.setAxisColor(Color.WHITE);
        pieChartOut.setAxisTextSize(pxTodp(13));
    }

    private float getRate(String string) {
        int num1 = Integer.parseInt(countBean.getCount());
        int num2 = Integer.parseInt(string);
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String rate = numberFormat.format((float) num2 / (float) num1 * 100);
        float v = Float.parseFloat(rate);
        return v;
    }


}
