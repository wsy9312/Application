package com.example.hgtxxgl.application.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.QrCode.sample.MainActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LoginActivity;
import com.example.hgtxxgl.application.utils.NumberFormatUtil;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;

//个人资料首页
public class PersonalFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = PersonalFragment.class.getSimpleName();
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

    public PersonalFragment() {

    }

    public static PersonalFragment newInstance(int tab) {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        args.putInt(DetailFragment.ARG_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }

    private DetailFragment.DataCallback callback;
    public PersonalFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView();
        return view;
    }

    private void initView() {
        mLogout = (Button) view.findViewById(R.id.btn_logout);
        rlQRcode = (RelativeLayout) view.findViewById(R.id.rl_qrcode);
        //编号
        mNumber = (TextView) view.findViewById(R.id.tv_message_number);
        //姓名
        mName = (TextView) view.findViewById(R.id.tv_message_name);
        //身份证
        mSFZnumber = (TextView) view.findViewById(R.id.tv_message_soldier);
        //职务
        mPostion = (TextView) view.findViewById(R.id.tv_message_sex);
        //性别
        mSex = (TextView) view.findViewById(R.id.tv_message_birth);
        //所属单位
        mCompany = (TextView) view.findViewById(R.id.tv_message_nationality);
        //所属部门
        mDepartment = (TextView) view.findViewById(R.id.tv_message_maritalstatus);
        //固定电话
        mGDNumber = (TextView) view.findViewById(R.id.tv_message_joindate);
        //手机号码
        mTelNumber = (TextView) view.findViewById(R.id.tv_message_militaryrank);
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
        if (Integer.parseInt(a) == 0){
            return null;
        }else{
            a = NumberFormatUtil.formatInteger(Integer.parseInt(a))+"营";
        }

        if (Integer.parseInt(b) == 0){
            b = "";
            c = "";
            d = "";
        }else{
            b = NumberFormatUtil.formatInteger(Integer.parseInt(b))+"连";
        }

        if (Integer.parseInt(c) == 0){
            c = "";
            d = "";
        }else{
            c = NumberFormatUtil.formatInteger(Integer.parseInt(c))+"排";
        }

        if (Integer.parseInt(d) == 0){
            d = "";
        }else{
            d = NumberFormatUtil.formatInteger(Integer.parseInt(d))+"班";
        }
        return a+b+c+d;
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
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private void logOut() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("是否确认退出当前账户?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(getContext(), LoginActivity.class));
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
