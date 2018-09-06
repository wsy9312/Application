package com.example.hgtxxgl.application.fragment.total;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.gson.Gson;
import com.idtk.smallchart.chart.CurveChart;
import com.idtk.smallchart.chart.PieChart;
import com.idtk.smallchart.data.CurveData;
import com.idtk.smallchart.data.PieData;
import com.idtk.smallchart.data.PointShape;
import com.idtk.smallchart.interfaces.iData.ICurveData;
import com.idtk.smallchart.interfaces.iData.IPieData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

public class UnitChartFragment extends Fragment implements View.OnClickListener {
    protected float[][] points = new float[][]{{1,10}, {2,13}, {3,12}, {4,38}, {5,9},{6,52}, {7,14}, {8,37}, {9,29}, {10,31},
                                               {11,52}, {12,13}, {13,51}, {14,20}, {15,19},{16,20}, {17,54}, {18,7}, {19,19}, {20,41},
                                               {21,52}, {22,13}, {23,51}, {24,20}, {25,80},{26,20}, {27,54}, {28,71}, {29,19}, {30,41}, {31,100}};
    protected float[][] points2 = new float[][]{{1,52}, {2,13}, {3,51}, {4,20}, {5,19},{6,20}, {7,54}, {8,7}, {9,19}, {10,41}};
    private ArrayList<IPieData> mPieDataListOut = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListRest = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListSick = new ArrayList<>();
    private ArrayList<IPieData> mPieDataListWork = new ArrayList<>();
    private ArrayList<ICurveData> mDataList = new ArrayList<>();
    private CurveData mCurveData = new CurveData();
    private ArrayList<PointF> mPointArrayList = new ArrayList<>();
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00, 0xFF805677};
    private HandToolbar handToolbar;
    private TextView tv_beginDate;
    private ArrayList<CurveTotalNumBean> curveTotalNumList;

    protected float pxTodp(float value){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float valueDP= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,metrics);
        return valueDP;
    }

    public static UnitChartFragment newInstance(Bundle bundle) {
        UnitChartFragment fragment = new UnitChartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        loadCurrentTotalNum();
    }

    private void loadCurrentTotalNum() {
        SharedPreferences share = getActivity().getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "IP address is empty");
        PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
        PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleInfoBean.setIsAndroid("1");
        List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
        beanList.add(peopleInfoBean);
        peopleEntity.setPeopleInfo(beanList);
        String json = new Gson().toJson(peopleEntity);
        String s1 = "get " + json;
        Log.e("123","获取个人信息:"+s1);
        HttpManager.getInstance().requestResultForm(tempIP,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
            @Override
            public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unitchart,container,false);
        initToolbar(view);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        PieData pieData1 = new PieData();
        pieData1.setName("休假");
        pieData1.setValue(90.8F);
        pieData1.setColor(mColors[5]);
        mPieDataListRest.add(pieData1);
        PieData pieData2 = new PieData();
        pieData2.setValue(9.2F);
        pieData2.setColor(mColors[6]);
        mPieDataListRest.add(pieData2);

        PieData pieData3 = new PieData();
        pieData3.setName("事假");
        pieData3.setValue(80F);
        pieData3.setColor(mColors[4]);
        mPieDataListWork.add(pieData3);
        PieData pieData4 = new PieData();
        pieData4.setValue(20F);
        pieData4.setColor(mColors[6]);
        mPieDataListWork.add(pieData4);

        PieData pieData5 = new PieData();
        pieData5.setName("病假");
        pieData5.setValue(70F);
        pieData5.setColor(mColors[7]);
        mPieDataListSick.add(pieData5);
        PieData pieData6 = new PieData();
        pieData6.setValue(30F);
        pieData6.setColor(mColors[6]);
        mPieDataListSick.add(pieData6);

        PieData pieData7 = new PieData();
        pieData7.setName("外出");
        pieData7.setValue(60F);
        pieData7.setColor(mColors[1]);
        mPieDataListOut.add(pieData7);
        PieData pieData8 = new PieData();
        pieData8.setValue(40F);
        pieData8.setColor(mColors[6]);
        mPieDataListOut.add(pieData8);

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

    private void initView(View view) {
        PieChart pieChartOut = (PieChart) view.findViewById(R.id.pieChart_out);
        pieChartOut.setDataList(mPieDataListOut);
        pieChartOut.setAxisColor(Color.WHITE);
        pieChartOut.setAxisTextSize(pxTodp(14));

        PieChart pieChartRest = (PieChart) view.findViewById(R.id.pieChart_rest);
        pieChartRest.setDataList(mPieDataListRest);
        pieChartRest.setAxisColor(Color.WHITE);
        pieChartRest.setAxisTextSize(pxTodp(14));

        PieChart pieChartSick = (PieChart) view.findViewById(R.id.pieChart_sick);
        pieChartSick.setDataList(mPieDataListSick);
        pieChartSick.setAxisColor(Color.WHITE);
        pieChartSick.setAxisTextSize(pxTodp(14));

        PieChart pieChartWork = (PieChart) view.findViewById(R.id.pieChart_work);
        pieChartWork.setDataList(mPieDataListWork);
        pieChartWork.setAxisColor(Color.WHITE);
        pieChartWork.setAxisTextSize(pxTodp(14));

        tv_beginDate = (TextView) view.findViewById(R.id.tv_beginDate);
        tv_beginDate.setOnClickListener(this);

        CurveChart curveChartNum = (CurveChart) view.findViewById(R.id.curveChartNum);
        curveChartNum.setDataList(mDataList);

        CurveChart curveChartPercent = (CurveChart) view.findViewById(R.id.curveChartPercent);
        curveChartPercent.setDataList(mDataList);
    }

    private void initToolbar(View view) {
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.mainColor_blue);
        handToolbar = (HandToolbar) view.findViewById(R.id.chart_toolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, getActivity());
        handToolbar.setBackHome(true, 0);
        handToolbar.setTitle("单位态势");
        handToolbar.setTitleSize(18);
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
