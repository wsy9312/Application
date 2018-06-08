package com.example.hgtxxgl.application.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hgtxxgl.application.QrCode.sample.ScannerActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.fragment.DetailFragment;
import com.example.hgtxxgl.application.utils.NumberFormatUtil;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.ImgUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.MyImageDialog;
import com.example.hgtxxgl.application.view.PersonalHandToolbar;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Intents;
import com.mylhyl.zxing.scanner.encode.QREncode;

import pub.devrel.easypermissions.EasyPermissions;

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
    private String no;
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
        handToolBar.setTitle(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName());
        handToolBar.setTitleSize(20);
        handToolBar.setRightButton(R.drawable.ic_action_save);
        handToolBar.setHisButton(R.drawable.ic_action_barcode);
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
        no = ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo();
        name = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName();
        cardNo = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getCardNo();
        sex = ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getSex().equals("0")?"男":"女";
        String data = "编号: "+ no + "\r\n\r\n"
                +"姓名: "+ name + "\r\n\r\n"
                +"身份证号: "+ cardNo + "\r\n\r\n"
                +"性别: "+ sex + "\r\n\r\n";
        bitmap = initshow(data);
        handToolBar.setLeftButton(bitmap);
        mNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNo());
        mName.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getName());
        mSFZnumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getCardNo());
        mPostion.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPosition());
        mSex.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getSex().equals("0")?"男":"女");
        mCompany.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getUnit());
        mDepartment.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getDepartment());
        mGDNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPhoneNo());
        mTelNumber.setText(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getTelNo());
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
                ApplicationApp.setNewLoginEntity(null);
                ApplicationApp.setPeopleInfoEntity(null);
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
                        requestPermission();
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
            if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                ScannerActivity.gotoActivity(PersonalActivity.this, true, 1);
            }
        }else if (views.equals(PersonalHandToolbar.VIEWS.LEFT_BUTTON)){
            MyImageDialog dialog = new MyImageDialog(this,0,0,0,bitmap,R.style.AnimBottom);
            dialog.show();
        }
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

    /**
     * 请求读取sd卡的权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //读取sd卡的权限
            String[] mPermissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (EasyPermissions.hasPermissions(mContext, mPermissionList)) {
                //已经同意过
                saveImage();
            } else {
                //未同意过,或者说是拒绝了，再次申请权限
                EasyPermissions.requestPermissions(
                        this,  //上下文
                        "保存图片需要读取sd卡的权限", //提示文言
                        REQUEST_CODE_SAVE_IMG, //请求码
                        mPermissionList //权限列表
                );
            }
        } else {
            saveImage();
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
