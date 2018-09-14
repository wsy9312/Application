package com.example.hgtxxgl.application.fragment.total.chart;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.data.PointShape;
import com.idtk.smallchart.interfaces.iData.ICurveData;

import java.util.ArrayList;

public class UnitChartCurveFragment extends Fragment{

    protected float[][] points = new float[][]{{1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}, {9,5}, {10,0}, {11,0}, {12,0}};
    private ArrayList<ICurveData> mDataList = new ArrayList<>();
    private CurveData mCurveData = new CurveData();
    private ArrayList<PointF> mPointArrayList = new ArrayList<>();
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00, 0xFF805677, 0xFF4EEE94};

    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        return valueDP;
    }

    public static UnitChartCurveFragment newInstance(Bundle bundle) {
        UnitChartCurveFragment fragment = new UnitChartCurveFragment();
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
        View view = inflater.inflate(R.layout.fragment_unitchart1,container,false);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        initData();
        initView(view);
        return view;
    }

    private void initView(View view) {

        CurveChart curveChartNum = (CurveChart) view.findViewById(R.id.curveChartNum);
        curveChartNum.setDataList(mDataList);

        CurveChart curveChartPercent = (CurveChart) view.findViewById(R.id.curveChartPercent);
        curveChartPercent.setDataList(mDataList);
    }

    private void initData() {
        for (int i = 0; i < 12; i++) {
            mPointArrayList.add(new PointF(points[i][0], points[i][1]));
        }
        mCurveData.setValue(mPointArrayList);
        mCurveData.setColor(mColors[10]);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
        mCurveData.setDrawable(drawable);

        mCurveData.setPointShape(PointShape.CIRCLE);
        mCurveData.setPaintWidth(pxTodp(1));
        mCurveData.setTextSize(pxTodp(10));
        mDataList.add(mCurveData);
    }

}
