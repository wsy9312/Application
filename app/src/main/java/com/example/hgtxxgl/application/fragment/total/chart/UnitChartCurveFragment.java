package com.example.hgtxxgl.application.fragment.total.chart;

import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.data.PointShape;
import com.idtk.smallchart.interfaces.iData.ICurveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UnitChartCurveFragment extends Fragment implements View.OnClickListener {

    protected float[][] points = new float[][]{{1,10}, {2,13}, {3,12}, {4,38}, {5,9},{6,52}, {7,14}, {8,37}, {9,29}, {10,31},
            {11,52}, {12,13}, {13,51}, {14,20}, {15,19},{16,20}, {17,54}, {18,7}, {19,19}, {20,41},
            {21,52}, {22,13}, {23,51}, {24,20}, {25,80},{26,20}, {27,54}, {28,71}, {29,19}, {30,41}, {31,100}};
    private ArrayList<ICurveData> mDataList = new ArrayList<>();
    private CurveData mCurveData = new CurveData();
    private ArrayList<PointF> mPointArrayList = new ArrayList<>();
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00, 0xFF805677};
    private TextView tv_beginDate;

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
        tv_beginDate = (TextView) view.findViewById(R.id.tv_beginDate);
        tv_beginDate.setOnClickListener(this);

        CurveChart curveChartNum = (CurveChart) view.findViewById(R.id.curveChartNum);
        curveChartNum.setDataList(mDataList);

        CurveChart curveChartPercent = (CurveChart) view.findViewById(R.id.curveChartPercent);
        curveChartPercent.setDataList(mDataList);
    }

    private void initData() {
        for (int i = 0; i < 31; i++) {
            mPointArrayList.add(new PointF(points[i][0], points[i][1]));
        }
        mCurveData.setValue(mPointArrayList);
        mCurveData.setColor(Color.RED);

        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
        mCurveData.setDrawable(drawable);

        mCurveData.setPointShape(PointShape.CIRCLE);
        mCurveData.setPaintWidth(pxTodp(1));
        mCurveData.setTextSize(pxTodp(10));
        mDataList.add(mCurveData);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_beginDate:
                DatePickDialog datePickDialog = showDatePickDialog(DateType.TYPE_YM);
                //设置点击确定按钮回调
                datePickDialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        int year = getYearFromDate(date);
                        int month = getMonthFromDate(date);
                        String strDate = getStringDateShort(date);
                        int daysByYearMonth = getDaysByYearMonth(year, month);
                        Toast.makeText(getActivity(), ""+daysByYearMonth, Toast.LENGTH_SHORT).show();
                        tv_beginDate.setText(strDate);

                    }
                });
                break;
        }
    }

    //日期选择对话框
    private DatePickDialog showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(getActivity());
        //设置上下年分限制
        dialog.setYearLimt(40);
        //设置标题
        dialog.setTitle("选择月份");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        dialog.show();
        return dialog;
    }

    //根据date获取当前年份
    public int getYearFromDate(Date date){
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        int year = cld.get(Calendar.YEAR);
        return year;
    }

    //根据date获取当前月份
    public int getMonthFromDate(Date date){
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        int Month = cld.get(Calendar.MONTH)+1;
        return Month;
    }

    //将当前date转换成字符串
    public static String getStringDateShort(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
