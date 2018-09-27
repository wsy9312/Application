package com.example.hgtxxgl.application.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hgtxxgl.application.QrCode.sample.ScannerActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.login.PeopleInfoBean;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ImgUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.MyDialog;
import com.example.hgtxxgl.application.view.MyImageDialog;
import com.example.hgtxxgl.application.view.PersonalHandToolbar;
import com.google.gson.Gson;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Intents;
import com.mylhyl.zxing.scanner.encode.QREncode;

import okhttp3.Request;

import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

//个人资料首页
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener, PersonalHandToolbar.OnButtonsClickCallback {

    public static final String TAG = PersonalActivity.class.getSimpleName();
    private Button mLogout;
    private TextView mNumber;
    private TextView mName;
    private TextView mSFZnumber;
    private TextView mPostion;
    private TextView mSex;
    private TextView mCompany;
    private TextView mDepartment;
    private TextView mGDNumber;
    private TextView mTelNumber;
    private Bitmap bitmap;
    private PersonalHandToolbar handToolBar;
    private String authenticationNo;
    private String name;
    private String cardNo;
    private String sex;
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private Context mContext;

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
        mContext = this;
        handToolBar = (PersonalHandToolbar) findViewById(R.id.personal_handtoolbar);
        handToolBar.setTitle(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName());
        handToolBar.setTitleSize(20);
        handToolBar.setDisplayHomeAsUpEnabled(true,this);
        handToolBar.setRightButton(R.drawable.ic_action_save);
        handToolBar.setHisButton(R.drawable.ic_action_barcode);
        handToolBar.setChangeButton(R.drawable.ic_action_change);
        handToolBar.setVisibility(View.VISIBLE);
        handToolBar.setButtonsClickCallback(this);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        SysExitUtil.activityList.add(this);
        initView();
    }

    private void initView() {
        mLogout = (Button)findViewById(R.id.btn_logout);
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
        mLogout.setOnClickListener(this);
        showData();
    }

    private void showData() {
        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
        name = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName();
        cardNo = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getCardNo();
        sex = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getSex().equals("0")?"男":"女";
        String data = "编号: "+ authenticationNo + "\r\n\r\n"
                +"姓名: "+ name + "\r\n\r\n"
                +"证件号: "+ cardNo + "\r\n\r\n"
                +"性别: "+ sex + "\r\n\r\n";
        bitmap = initshow(data);
        handToolBar.setLeftButton(bitmap);
        mNumber.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getNo());
        mName.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName());
        mSFZnumber.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getCardNo());
        mPostion.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getPosition());
        mSex.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getSex().equals("0")?"男":"女");
        mCompany.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getUnit());
        mDepartment.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getDepartment());
        mGDNumber.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getPhoneNo());
        mTelNumber.setText(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTelNo());
    }

    private Bitmap initshow(String peopleInfoEntity) {
        String qrContent = peopleInfoEntity;
        if (qrContent != null){
            Bitmap bitmap = new QREncode.Builder(PersonalActivity.this)
                    //二维码颜色
                    .setColor(getResources().getColor(R.color.mainColor_blue))
                    //二维码类型
                    .setParsedResultType(TextUtils.isEmpty(qrContent) ? ParsedResultType.URI : ParsedResultType.TEXT)
                    //二维码内容
                    .setContents(TextUtils.isEmpty(qrContent) ? "当前信息显示错误" : qrContent)
                    .build().encodeAsBitmap();
            return bitmap;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                logOut();
                break;
        }
    }

    private void logOut() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_exit_current_account);
        builder.setPositiveButton(getString(R.string.make_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ApplicationApp.setLoginInfoBean(null);
                ApplicationApp.setPeopleInfoBean(null);
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                SysExitUtil.exit();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onButtonClickListner(PersonalHandToolbar.VIEWS views, int radioIndex) {
        if (views.equals(PersonalHandToolbar.VIEWS.RIGHT_BUTTON)){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage(R.string.save_qrcode_pic);
            builder.setPositiveButton(getString(R.string.make_sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ImageView tvRightBao = handToolBar.getTvLeft();
                    tvRightBao.setDrawingCacheEnabled(true);
                    if (tvRightBao.getDrawingCache() != null){
                        bitmap = Bitmap.createBitmap(tvRightBao.getDrawingCache());
//                        requestPermission();
                        saveImage();
                    }else{
                        ToastUtil.showToast(mContext,getString(R.string.pic_can_no_empty));
                    }
                    tvRightBao.setDrawingCacheEnabled(false);
                }
            });
            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else if (views.equals(PersonalHandToolbar.VIEWS.HISTORY_BUTTON)){
//            if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
//                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
//            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                ScannerActivity.gotoActivity(PersonalActivity.this, true, 1);
//            }
        }else if (views.equals(PersonalHandToolbar.VIEWS.LEFT_BUTTON)){
            MyImageDialog dialog = new MyImageDialog(this,0,0,0,bitmap,R.style.AnimBottom);
            dialog.show();
        }else if (views.equals(PersonalHandToolbar.VIEWS.BACK_BUTTON)){
            this.onBackPressed();
        }else if (views.equals(PersonalHandToolbar.VIEWS.CHANGE_BUTTON)){
            final Dialog dialogx = new MyDialog(this, R.style.setttinguserpassworddialog);
            dialogx.setContentView(R.layout.setting_user_password_dialog);
            final TextInputEditText prePassword = (TextInputEditText) dialogx.findViewById(R.id.prePassword);
            final TextInputEditText newPassword = (TextInputEditText) dialogx.findViewById(R.id.newPassword);
            final TextInputEditText confirmPassword = (TextInputEditText) dialogx.findViewById(R.id.confirmPassword);
            Button submit = (Button) dialogx.findViewById(R.id.dialog_button_ok);
            Button cancel = (Button) dialogx.findViewById(R.id.dialog_button_cancel);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String prepassword = prePassword.getText().toString();
                    String newpassword = newPassword.getText().toString();
                    String confirmpassword = confirmPassword.getText().toString();
                    if (!TextUtils.isEmpty(prepassword)){
                        if (prepassword.equals(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getPassword())){
                            if (!TextUtils.isEmpty(newpassword) && !TextUtils.isEmpty(confirmpassword)){
                                if (newpassword.equals(confirmpassword)){
                                    modify(newpassword);
                                    dialogx.dismiss();
                                }else{
                                    show("新密码输入不一致,请重新输入!");
                                }
                            }else{
                                show("新密码不能为空!");
                            }
                        }else{
                            show("原密码输入错误,请重新输入");
                        }
                    }else{
                        show("原密码不能为空!");
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogx.dismiss();
                }
            });
            dialogx.show();
        }
    }

    private void modify(final String newpassword) {
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setPassword(newpassword);
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setAuthenticationNo(authenticationNo);
        peopleInfoBean.setNoIndex(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getNoIndex());
        peopleInfoBean.setTimeStamp(ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getTimeStamp());
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Edit_PeopleInfo " + json;
        Log.e(TAG,"修改密码:"+s1);
        SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "");
        HttpManager.getInstance().requestNewResultForm(tempIP,s1,PeopleInfoBean.class,new HttpManager.ResultNewCallback<PeopleInfoBean>() {
            @Override
            public void onSuccess(String json, PeopleInfoBean peopleInfoBean) throws Exception {

            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) {
                if (response.contains("error")){
                    show(response);
                }else if (response.contains("ok")){
                    ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).setPassword(newpassword);
                    show("修改成功!");
                    startActivity(new Intent(PersonalActivity.this,LoginActivity.class));
                }
            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {

            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void show(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getApplicationContext(),msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == ScannerActivity.REQUEST_CODE_SCANNER) {
                if (data != null) {
                    String stringExtra = data.getStringExtra(Intents.Scan.RESULT);
                    AlertDialog alertDialog = new AlertDialog.Builder(PersonalActivity.this).create();
                    alertDialog.show();
                    Window window = alertDialog.getWindow();
                    window.setContentView(R.layout.layout_qrcode_result);
                    TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                    tv_title.setText("人员基本信息");
                    TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
                    tv_message.setText(stringExtra);
                }
            }
        }
    }

    //保存图片
    private void saveImage() {
        final boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSaveSuccess) {
                    ToastUtil.showToast(getApplicationContext(),getString(R.string.save_success));
                } else {
                    ToastUtil.showToast(getApplicationContext(),getString(R.string.save_failure));
                }
            }
        });

    }
}
