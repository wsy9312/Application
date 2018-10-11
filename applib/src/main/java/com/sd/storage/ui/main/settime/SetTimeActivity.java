package com.sd.storage.ui.main.settime;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.TimeActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VoteTimeModel;
import com.sd.storage.stores.TimeStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018-09-21.
 */

public class SetTimeActivity extends BaseSCActivity implements View.OnClickListener {
//    @BindView(R.id.tv_title)
    TextView tv_title;


//    @BindView(R.id.tv_startday)
    TextView tv_startday;
//    @BindView(R.id.tv_starttime)
    TextView tv_starttime;

//    @BindView(R.id.tv_endday)
    TextView tv_endday;
//    @BindView(R.id.tv_endtime)
    TextView tv_endtime;

//    @BindView(R.id.tv_submit)
    TextView tv_submit;
    LinearLayout im_back;

    Calendar calendar = Calendar.getInstance();


    @Inject
    TimeActionsCreator timeActionsCreator;
    @Inject
    TimeStore timeStore;

    private VoteTimeModel voteTimeModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        StorageApplication.getApplication().getAppComponent().inject(this);
        init();
    }


    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_startday = (TextView) findViewById(R.id.tv_startday);
        tv_starttime = (TextView) findViewById(R.id.tv_starttime);
        tv_endday = (TextView) findViewById(R.id.tv_endday);
        tv_endtime = (TextView) findViewById(R.id.tv_endtime);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_back.setOnClickListener(this);
        tv_startday.setOnClickListener(this);
        tv_starttime.setOnClickListener(this);
        tv_endday.setOnClickListener(this);
        tv_endtime.setOnClickListener(this);
        tv_submit.setOnClickListener(this);


        tv_title.setText(R.string.setting_time);
        timeActionsCreator.selectAllVoteTime();
    }

//    @OnClick({R.id.im_back, R.id.tv_startday, R.id.tv_starttime, R.id.tv_endday, R.id.tv_endtime, R.id.tv_submit})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.tv_startday) {
            showDatePickerDialog(tv_startday, calendar);

        } else if (i == R.id.tv_starttime) {
            showTimePickerDialog(tv_starttime, calendar);

        } else if (i == R.id.tv_endday) {
            showDatePickerDialog(tv_endday, calendar);

        } else if (i == R.id.tv_endtime) {
            showTimePickerDialog(tv_endtime, calendar);

        } else if (i == R.id.tv_submit) {
            setTime();

        }
    }


    public void setTime() {

        String startday = tv_startday.getText().toString();
        if (null == startday || startday.length() == 0) {
            ToastUtils.showBaseToast(R.string.time_is_null, this);
            return;
        }
        String starttime = tv_starttime.getText().toString();
        if (null == starttime || starttime.length() == 0) {
            ToastUtils.showBaseToast(R.string.time_is_null, this);
            return;
        }
        String endday = tv_endday.getText().toString();
        if (null == endday || endday.length() == 0) {
            ToastUtils.showBaseToast(R.string.time_is_null, this);
            return;
        }
        String endtime = tv_endtime.getText().toString();
        if (null == endtime || endtime.length() == 0) {
            ToastUtils.showBaseToast(R.string.time_is_null, this);
            return;
        }

        String start = startday + " "+starttime;
        String end = endday + " "+endtime;
        time(start, end);

    }


    public void time(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(start.toString());//开始时间
            Date date2 = dateFormat.parse(end);//结束时间

            if (date2.getTime() < date1.getTime()) {
                ToastUtils.showBaseToast(getString(R.string.finishtime_is_err), this);
                return;
            } else if (date2.getTime() == date1.getTime()) {
                ToastUtils.showBaseToast(getString(R.string.finishtime_is_err2), this);
                return;
            } else if (date2.getTime() > date1.getTime()) {
                timeActionsCreator.setTime(start, end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initReturnEvent() {

        timeStore.toMainSubscription(TimeStore.SetTimeChange.class, new Action1<TimeStore.SetTimeChange>() {
            @Override
            public void call(TimeStore.SetTimeChange setTimeChange) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(R.string.add_success, SetTimeActivity.this);
            }
        });

        /**
         * 请求错误
         */
        timeStore.toMainSubscription(TimeStore.SetTimeChangeError.class, new Action1<TimeStore.SetTimeChangeError>() {
            @Override
            public void call(TimeStore.SetTimeChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, SetTimeActivity.this);
            }
        });


        timeStore.toMainSubscription(TimeStore.SelectTimeChange.class, new Action1<TimeStore.SelectTimeChange>() {
            @Override
            public void call(TimeStore.SelectTimeChange selectTimeChangeError) {
                getDisplay().hideWaittingDialog();
                voteTimeModel = timeStore.getVoteTimeModel();
                setPageShow();
            }
        });

        /**
         * 请求错误
         */
        timeStore.toMainSubscription(TimeStore.SelectTimeChangeError.class, new Action1<TimeStore.SelectTimeChangeError>() {
            @Override
            public void call(TimeStore.SelectTimeChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, SetTimeActivity.this);
            }
        });
    }


    public void setPageShow() {

        if (null != voteTimeModel) {
            String starttime = voteTimeModel.vtstart;
            tv_startday.setText(starttime.substring(0, 10));
            tv_starttime.setText(starttime.substring(10, starttime.length()));
            String endttime = voteTimeModel.vtend;
            tv_endday.setText(endttime.substring(0, 10));
            tv_endtime.setText(endttime.substring(10, endttime.length()));

        }
    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{timeStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return timeActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settime;
    }


    public void showDatePickerDialog(final TextView tv, Calendar calendar) {

        new DatePickerDialog(SetTimeActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作

                String month = String.valueOf(monthOfYear + 1);
                if (month.length() == 1) {
                    month = "0" + month;
                }
                String day = String.valueOf(dayOfMonth);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                tv.setText(year + "-" + month + "-" + day );

            }
        }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void showTimePickerDialog(final TextView tv, Calendar calendar) {

        new TimePickerDialog(SetTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String hour = String.valueOf(hourOfDay);
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                String min = String.valueOf(minute);
                if (min.length() == 1) {
                    min = "0" + min;
                }
                tv.setText(hour + ":" + min);
            }
        }
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                , true).show();

    }


}
