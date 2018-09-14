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
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.data.PointShape;
import com.idtk.smallchart.interfaces.iData.ICurveData;

import java.text.NumberFormat;
import java.util.ArrayList;

public class UnitChartCurveFragment extends Fragment{

    protected float[][] points = new float[][]{{1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,0}, {9,0}, {10,0}, {11,0}, {12,0}};
    protected float[][] pointrates = new float[][]{{1,0}, {2,0}, {3,0}, {4,0}, {5,0}, {6,0}, {7,0}, {8,8}, {9,0}, {10,0}, {11,0}, {12,0}};
    private ArrayList<ICurveData> mDataList = new ArrayList<>();
    private ArrayList<ICurveData> mDatarateList = new ArrayList<>();
    private CurveData mCurveData = new CurveData();
    private CurveData mCurverateData = new CurveData();
    private ArrayList<PointF> mPointArrayList = new ArrayList<>();
    private ArrayList<PointF> mPointArrayrateList = new ArrayList<>();
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initView(view);
        return view;
    }

    private void initView(View view) {

        CurveChart curveChartNum = (CurveChart) view.findViewById(R.id.curveChartNum);
        curveChartNum.setDataList(mDataList);

        CurveChart curveChartPercent = (CurveChart) view.findViewById(R.id.curveChartPercent);
        curveChartPercent.setDataList(mDatarateList);

    }

    private void initData() {
        L.e("UnitChartCurveFragment8", ApplicationApp.getCount8Bean().getApi_Get_Count().get(0).getCount());
        L.e("UnitChartCurveFragment9", ApplicationApp.getCount9Bean().getApi_Get_Count().get(0).getCount());
        L.e("UnitChartCurveFragment10", ApplicationApp.getCount10Bean().getApi_Get_Count().get(0).getCount());
        String count = ApplicationApp.getCountBean().getApi_Get_Count().get(0).getCount();
        String count8 = ApplicationApp.getCount8Bean().getApi_Get_Count().get(0).getCount();
        String count9 = ApplicationApp.getCount9Bean().getApi_Get_Count().get(0).getCount();
        String count10 = ApplicationApp.getCount10Bean().getApi_Get_Count().get(0).getCount();
        float month8num = Float.parseFloat(count8);
        float month9num = Float.parseFloat(count9);
        float month10num = Float.parseFloat(count10);
        points[7][1] = month8num;
        points[8][1] = month9num;
        points[9][1] = month10num;

        float rate8 = getRate(count, count8);
        float rate9 = getRate(count, count9);
        float rate10 = getRate(count, count10);
        pointrates[7][1] = rate8;
        pointrates[8][1] = rate9;
        pointrates[9][1] = rate10;

        //人数曲线图
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

        //百分比曲线图
        for (int i = 0; i < 12; i++) {
            mPointArrayrateList.add(new PointF(pointrates[i][0], pointrates[i][1]));
        }
        mCurverateData.setValue(mPointArrayrateList);
        mCurverateData.setColor(mColors[10]);
        Drawable drawablerate = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
        mCurverateData.setDrawable(drawablerate);

        mCurverateData.setPointShape(PointShape.CIRCLE);
        mCurverateData.setPaintWidth(pxTodp(1));
        mCurverateData.setTextSize(pxTodp(10));
        mDatarateList.add(mCurverateData);
    }

    private float getRate(String string1,String string2) {
        int num1 = Integer.parseInt(string1);
        int num2 = Integer.parseInt(string2);
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String rate = numberFormat.format((float) num2 / (float) num1 * 100);
        float v = Float.parseFloat(rate);
        return v;
    }
}
