package com.example.hgtxxgl.application.fragment.total.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class UnitChartCurveFragment extends Fragment{

    private float month8num;
    private float month9num;
    private float month10num;
    private float month11num;
    private float month12num;
    private float rate8;
    private float rate9;
    private float rate10;
    private float rate11;
    private float rate12;

    public static UnitChartCurveFragment newInstance(Bundle bundle) {
        UnitChartCurveFragment fragment = new UnitChartCurveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private LineChartView chart;
    private LineChartView chart1;
    private LineChartData data;
    private LineChartData data1;
    private int numberOfPoints = 13;
    private int numberOfPoints1 = 13;

    float[][] randomNumbersTab = new float[1][numberOfPoints];
    float[][] randomNumbersTab1 = new float[1][numberOfPoints1];

    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = true;
    private boolean hasLabels = true;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private boolean pointsHaveDifferentColor;
    private boolean hasGradientToTransparent = false;

    private boolean hasLines1 = true;
    private boolean hasPoints1 = true;
    private ValueShape shape1 = ValueShape.CIRCLE;
    private boolean isFilled1 = true;
    private boolean hasLabels1 = true;
    private boolean isCubic1 = false;
    private boolean hasLabelForSelected1 = true;
    private boolean pointsHaveDifferentColor1;
    private boolean hasGradientToTransparent1 = false;

    public UnitChartCurveFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_unitchart1, container, false);
        initData();
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart1 = (LineChartView) rootView.findViewById(R.id.chart1);
        chart.setOnValueTouchListener(new UnitChartCurveFragment.ValueTouchListener());
        chart1.setOnValueTouchListener(new UnitChartCurveFragment.ValueTouchListener1());
        // Generate some random values.
        generateValues();
        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);
        chart1.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(chart.getMaximumViewport());
        Viewport v1 = new Viewport(chart1.getMaximumViewport());
        resetViewport(chart,v,-5,50,0,12);
        resetViewport(chart1,v1,-5,50,0,12);

        return rootView;
    }

    private void initData() {
        String count = ApplicationApp.getCountBean().getApi_Get_Count().get(0).getCount();
        String count8 = ApplicationApp.getCount8Bean().getApi_Get_Count().get(0).getCount();
        String count9 = ApplicationApp.getCount9Bean().getApi_Get_Count().get(0).getCount();
        String count10 = ApplicationApp.getCount10Bean().getApi_Get_Count().get(0).getCount();
        String count11 = ApplicationApp.getCount11Bean().getApi_Get_Count().get(0).getCount();
        String count12 = ApplicationApp.getCount12Bean().getApi_Get_Count().get(0).getCount();
        month8num = Float.parseFloat(count8);
        month9num = Float.parseFloat(count9);
        month10num = Float.parseFloat(count10);
        month11num = Float.parseFloat(count11);
        month12num = Float.parseFloat(count12);

        rate8 = getRate(count, count8);
        rate9 = getRate(count, count9);
        rate10 = getRate(count, count10);
        rate11 = getRate(count, count11);
        rate12 = getRate(count, count12);
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

    private void generateValues() {
            randomNumbersTab[0][0] = 0;
            randomNumbersTab[0][1] = 0;
            randomNumbersTab[0][2] = 0;
            randomNumbersTab[0][3] = 0;
            randomNumbersTab[0][4] = 0;
            randomNumbersTab[0][5] = 0;
            randomNumbersTab[0][6] = 0;
            randomNumbersTab[0][7] = 0;
            randomNumbersTab[0][8] = month8num;
            randomNumbersTab[0][9] = month9num;
            randomNumbersTab[0][10] = month10num;
            randomNumbersTab[0][11] = month11num;
            randomNumbersTab[0][12] = month12num;

            randomNumbersTab1[0][0] = 0;
            randomNumbersTab1[0][1] = 0;
            randomNumbersTab1[0][2] = 0;
            randomNumbersTab1[0][3] = 0;
            randomNumbersTab1[0][4] = 0;
            randomNumbersTab1[0][5] = 0;
            randomNumbersTab1[0][6] = 0;
            randomNumbersTab1[0][7] = 0;
            randomNumbersTab1[0][8] = rate8;
            randomNumbersTab1[0][9] = rate9;
            randomNumbersTab1[0][10] = rate10;
            randomNumbersTab1[0][11] = rate11;
            randomNumbersTab1[0][12] = rate12;

    }

    private void resetViewport(LineChartView chart,Viewport v,int bottom,int top,int left,int right) {
        // Reset viewport height range to (0,100)
        v.bottom = bottom;
        v.top = top;
        v.left = left;
        v.right = right;
        chart.setCurrentViewport(v);
        // You have to set max and current viewports separately.
        chart.setMaximumViewport(v);
        // I changing current viewport with animation in this case.
        chart.setCurrentViewportWithAnimation(v);
    }

    private void generateData() {
        //人数
        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int j = 1; j < numberOfPoints; ++j) {
            values.add(new PointValue(j, randomNumbersTab[0][j]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLORS[0]);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        line.setPointRadius(4);
        line.setStrokeWidth(1);
//        line.setHasGradientToTransparent(hasGradientToTransparent);
        if (pointsHaveDifferentColor){
            line.setPointColor(ChartUtils.COLORS[0 % ChartUtils.COLORS.length]);
        }
        lines.add(line);

        data = new LineChartData(lines);

        Axis axisX = new Axis().setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("月份");
        axisY.setName("人数(人)");
        axisX.setTextColor(Color.rgb(105,105,105));
        axisY.setTextColor(Color.rgb(105,105,105));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

        //百分比
        if (hasLabels1) {
            hasLabelForSelected1 = false;
            chart1.setValueSelectionEnabled(hasLabelForSelected1);
        }
        List<Line> lines1 = new ArrayList<Line>();
        List<PointValue> values1 = new ArrayList<PointValue>();
        for (int j = 1; j < numberOfPoints1; ++j) {
            values1.add(new PointValue(j, randomNumbersTab1[0][j]));
        }

        Line line1 = new Line(values1);
        line1.setColor(ChartUtils.COLORS[3]);
        line1.setShape(shape1);
        line1.setCubic(isCubic1);
        line1.setFilled(isFilled1);
        line1.setHasLabels(hasLabels1);
        line1.setHasLabelsOnlyForSelected(hasLabelForSelected1);
        line1.setHasLines(hasLines1);
        line1.setHasPoints(hasPoints1);
        line1.setPointRadius(4);
        line1.setStrokeWidth(1);
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(1);
        line1.setFormatter(chartValueFormatter);//显示小数点
//        line.setHasGradientToTransparent(hasGradientToTransparent);
        if (pointsHaveDifferentColor1){
            line.setPointColor(ChartUtils.COLORS[3 % ChartUtils.COLORS.length]);
        }
        lines1.add(line1);

        data1 = new LineChartData(lines1);

        Axis axisX1 = new Axis().setHasLines(true);
        Axis axisY1 = new Axis().setHasLines(true);
        axisX1.setName("月份");
        axisY1.setName("百分比(%)");
        axisX1.setTextColor(Color.rgb(105,105,105));
        axisY1.setTextColor(Color.rgb(105,105,105));
        data1.setAxisXBottom(axisX1);
        data1.setAxisYLeft(axisY1);

        data1.setBaseValue(Float.NEGATIVE_INFINITY);
        chart1.setLineChartData(data1);

    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), (int)value.getX()+"月的请假人数是: " + (int)value.getY()+"人", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

    private class ValueTouchListener1 implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), (int)value.getX()+"月的请假占比是: " + value.getY()+"%", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }

}
