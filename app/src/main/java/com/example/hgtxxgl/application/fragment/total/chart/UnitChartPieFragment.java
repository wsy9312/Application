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

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.idtk.smallchart.chart.PieChart;
import com.idtk.smallchart.data.PieData;
import com.idtk.smallchart.interfaces.iData.IPieData;

import java.util.ArrayList;

public class UnitChartPieFragment extends Fragment/* implements View.OnClickListener */{
    private ArrayList<IPieData> mPieDataListOut = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListRest = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListSick = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListWork = new ArrayList<>();
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00, 0xFF805677};

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

        PieData pieData3 = new PieData();
        pieData3.setName("事假");
        pieData3.setValue(80F);
        pieData3.setColor(mColors[4]);
        mPieDataListWork.add(pieData3);
        PieData pieData4 = new PieData();
        pieData4.setValue(100F-80F);
        pieData4.setColor(mColors[6]);
        mPieDataListWork.add(pieData4);

        PieChart pieChartOut = (PieChart) view.findViewById(R.id.pieChart_out);
        PieChart pieChartRest = (PieChart) view.findViewById(R.id.pieChart_rest);
        PieChart pieChartSick = (PieChart) view.findViewById(R.id.pieChart_sick);
        PieChart pieChartWork = (PieChart) view.findViewById(R.id.pieChart_work);

        pieChartWork.setDataList(mPieDataListWork);
        pieChartWork.setAxisColor(Color.WHITE);
        pieChartWork.setAxisTextSize(pxTodp(14));

        pieChartOut.setVisibility(View.INVISIBLE);
        pieChartRest.setVisibility(View.INVISIBLE);
        pieChartSick.setVisibility(View.INVISIBLE);

//        initData();
//        initView(view);
        return view;
    }

    private void initData() {
//        PieData pieData1 = new PieData();
//        pieData1.setName("休假");
//        pieData1.setValue(90.8F);
//        pieData1.setColor(mColors[5]);
//        mPieDataListRest.add(pieData1);
//        PieData pieData2 = new PieData();
//        pieData2.setValue(9.2F);
//        pieData2.setColor(mColors[6]);
//        mPieDataListRest.add(pieData2);

//        PieData pieData3 = new PieData();
//        pieData3.setName("事假");
//        pieData3.setValue(80F);
//        pieData3.setColor(mColors[4]);
//        mPieDataListWork.add(pieData3);
//        PieData pieData4 = new PieData();
//        pieData4.setValue(20F);
//        pieData4.setColor(mColors[6]);
//        mPieDataListWork.add(pieData4);

//        PieData pieData5 = new PieData();
//        pieData5.setName("病假");
//        pieData5.setValue(70F);
//        pieData5.setColor(mColors[7]);
//        mPieDataListSick.add(pieData5);
//        PieData pieData6 = new PieData();
//        pieData6.setValue(30F);
//        pieData6.setColor(mColors[6]);
//        mPieDataListSick.add(pieData6);

//        PieData pieData7 = new PieData();
//        pieData7.setName("外出");
//        pieData7.setValue(60F);
//        pieData7.setColor(mColors[1]);
//        mPieDataListOut.add(pieData7);
//        PieData pieData8 = new PieData();
//        pieData8.setValue(40F);
//        pieData8.setColor(mColors[6]);
//        mPieDataListOut.add(pieData8);

    }

    private void initView(View view) {
//        PieChart pieChartOut = (PieChart) view.findViewById(R.id.pieChart_out);
//        pieChartOut.setDataList(mPieDataListOut);
//        pieChartOut.setAxisColor(Color.WHITE);
//        pieChartOut.setAxisTextSize(pxTodp(14));
//
//        PieChart pieChartRest = (PieChart) view.findViewById(R.id.pieChart_rest);
//        pieChartRest.setDataList(mPieDataListRest);
//        pieChartRest.setAxisColor(Color.WHITE);
//        pieChartRest.setAxisTextSize(pxTodp(14));
//
//        PieChart pieChartSick = (PieChart) view.findViewById(R.id.pieChart_sick);
//        pieChartSick.setDataList(mPieDataListSick);
//        pieChartSick.setAxisColor(Color.WHITE);
//        pieChartSick.setAxisTextSize(pxTodp(14));
//
//        PieChart pieChartWork = (PieChart) view.findViewById(R.id.pieChart_work);
//        pieChartWork.setDataList(mPieDataListWork);
//        pieChartWork.setAxisColor(Color.WHITE);
//        pieChartWork.setAxisTextSize(pxTodp(14));

    }


}
