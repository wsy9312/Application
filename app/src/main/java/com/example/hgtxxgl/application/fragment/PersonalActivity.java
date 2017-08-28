package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.QrCode.sample.MainActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LoginActivity;
import com.example.hgtxxgl.application.utils.NumberFormatUtil;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;

//个人资料首页
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = PersonalActivity.class.getSimpleName();
    private Button mLogout;
    private RelativeLayout rlQRcode;
    private View view;
    private TextView mNumber;
    private TextView mName;
    private TextView mSFZnumber;
    private TextView mPostion;
    private TextView mSex;
    private TextView mCompany;
    private TextView mDepartment;
    private TextView mGDNumber;
    private TextView mTelNumber;

    public PersonalActivity() {

    }

    private DetailFragment.DataCallback callback;
    public PersonalActivity setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_center);
        HandToolbar handToolBar = (HandToolbar) findViewById(R.id.personal_handtoolbar);
        handToolBar.setTitle("我的");
        handToolBar.setTitleSize(20);
        handToolBar.setDisplayHomeAsUpEnabled(true,this);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(this);
        initView();
    }

    private void initView() {
        mLogout = (Button)findViewById(R.id.btn_logout);
        rlQRcode = (RelativeLayout)findViewById(R.id.rl_qrcode);
        //编号
        mNumber = (TextView)findViewById(R.id.tv_message_number);
        //姓名
        mName = (TextView)findViewById(R.id.tv_message_name);
        //身份证
        mSFZnumber = (TextView)findViewById(R.id.tv_message_soldier);
        //职务
        mPostion = (TextView)findViewById(R.id.tv_message_sex);
        //性别
        mSex = (TextView)findViewById(R.id.tv_message_birth);
        //所属单位
        mCompany = (TextView)findViewById(R.id.tv_message_nationality);
        //所属部门
        mDepartment = (TextView)findViewById(R.id.tv_message_maritalstatus);
        //固定电话
        mGDNumber = (TextView)findViewById(R.id.tv_message_joindate);
        //手机号码
        mTelNumber = (TextView)findViewById(R.id.tv_message_militaryrank);
        rlQRcode.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        showData();
    }

    private void showData() {
        mNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        mName.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName());
        mSFZnumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getCardNo());
        mPostion.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPosition());
        mSex.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getSex().equals("0")?"男":"女");
        mCompany.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit());
        mDepartment.setText(getDepartmentData(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getArmyGroup()));
        mGDNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPhoneNo());
        mTelNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getTelNo());
    }

    public String getDepartmentData(String string){
        String a = string.substring(0,1);
        String b = string.substring(1,2);
        String c = string.substring(2,3);
        String d = string.substring(3,4);
        if (Integer.parseInt(a.trim()) == 0){
            return "";
        } else if (Integer.parseInt(b.trim()) == 0){
            return NumberFormatUtil.formatInteger(Integer.parseInt(a.trim()))+"营";
        } else if (Integer.parseInt(c.trim()) == 0){
            return NumberFormatUtil.formatInteger(Integer.parseInt(a.trim()))+"营"+NumberFormatUtil.formatInteger(Integer.parseInt(b.trim()))+"连";
        } else if (Integer.parseInt(d.trim()) == 0){
            return NumberFormatUtil.formatInteger(Integer.parseInt(a.trim()))+"营"+NumberFormatUtil.formatInteger(Integer.parseInt(b.trim()))+"连"+
                    NumberFormatUtil.formatInteger(Integer.parseInt(c.trim()))+"排";
        } else {
            return NumberFormatUtil.formatInteger(Integer.parseInt(a.trim()))+"营"+NumberFormatUtil.formatInteger(Integer.parseInt(b.trim()))+"连"+
                    NumberFormatUtil.formatInteger(Integer.parseInt(c.trim()))+"排"+NumberFormatUtil.formatInteger(Integer.parseInt(d.trim()))+"班";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_qrcode:
                skipToQR();
                break;
            case R.id.btn_logout:
                logOut();
                break;
        }
    }

    private void skipToQR() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void logOut() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("是否确认退出当前账户?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ApplicationApp.setNewLoginEntity(null);
                ApplicationApp.setPeopleInfoEntity(null);
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                SysExitUtil.exit();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


}
