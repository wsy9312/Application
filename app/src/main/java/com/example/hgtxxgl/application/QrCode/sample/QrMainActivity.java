package com.example.hgtxxgl.application.QrCode.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.ImgUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Intents;
import com.mylhyl.zxing.scanner.encode.QREncode;

import pub.devrel.easypermissions.EasyPermissions;

import static org.litepal.LitePalApplication.getContext;

//二维码首页
public class QrMainActivity extends AppCompatActivity{
    private static final int PICK_CONTACT = 1;
    private TextView tvResult;
    private ImageView imageView;
    private int laserMode;
    private Toolbar toolbar;
    private Bitmap bitmap;
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private static final String TAG = "QrMainActivity";
    private Context mContext;
    private String authenticationNo;
    private String name;
    private String cardNo;
    private String position;
    private String sex;
    private String unit;
    private String armyGroup;
    private RelativeLayout llqrimage;
    private HandToolbar handToolbar;

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
                    ToastUtil.showToast(getContext(),"保存成功");
                } else {
                    ToastUtil.showToast(getContext(),"保存失败");
                }
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
//            //拒绝授权后，从系统设置了授权后，返回APP进行相应的操作
//            Log.i(TAG, "onPermissionsDenied:------>自定义设置授权后返回APP");
//            saveImage();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menumain);
        SysExitUtil.activityList.add(QrMainActivity.this);
        mContext = this;
        handToolbar = (HandToolbar) findViewById(R.id.qrcode_mainactivity_toolbar);
        handToolbar.setTitle("二维码名片");
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(true,R.mipmap.ic_more);
        handToolbar.setTitleSize(20);
        handToolbar.setButtonsClickCallback(new HandToolbar.OnButtonsClickCallback() {
            @Override
            public void onButtonClickListner(HandToolbar.VIEWS views, int radioIndex) {
               final String[] items = new String[]{"保存到相册","扫描二维码"};
                AlertDialog.Builder builder = new AlertDialog.Builder(QrMainActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("保存到相册")){
                            imageView.setDrawingCacheEnabled(true);
                            if (imageView.getDrawingCache() != null){
                                bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                                requestPermission();
                            }else{
                                ToastUtil.showToast(mContext,"图片不能为空");
                            }
                            imageView.setDrawingCacheEnabled(false);
                        }else{
                            if (ContextCompat.checkSelfPermission(QrMainActivity.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //权限还没有授予，需要在这里写申请权限的代码
                                ActivityCompat.requestPermissions(QrMainActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
                            } else {
                                //权限已经被授予，在这里直接写要执行的相应方法即可
                                ScannerActivity.gotoActivity(QrMainActivity.this, true, 1);
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        initTotal();
        //
        tvResult = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        llqrimage = (RelativeLayout) findViewById(R.id.ll_qr_image);
        authenticationNo = ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo();
        name = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getName();
        cardNo = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getCardNo();
//        position = ApplicationApp.getPeopleInfoBean().getPeopleInfo().get(0)
        sex = ApplicationApp.getPeopleInfoBean().getApi_Get_MyInfoSim().get(0).getSex().equals("0")?"男":"女";
        /*//所属单位
        unit = ApplicationApp.getPeopleInfoBean().getPeopleInfo().get(0)
        //所属部门
        armyGroup = ApplicationApp.getPeopleInfoBean().getPeopleInfo().get(0)*/
        String data = "编号: "+ authenticationNo + "\r\n\r\n"
                +"姓名: "+ name + "\r\n\r\n"
                +"身份证号: "+ cardNo + "\r\n\r\n"
//                +"职务: "+ position + "\r\n"
                +"性别: "+ sex + "\r\n\r\n";
//                +"所属单位: "+ unit + "\r\n"
//                +"所属部门: "+ armyGroup + "\r\n";
        initshow(data);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_0;
                        break;
                    case R.id.radioButton2:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_1;
                        break;
                    case R.id.radioButton3:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_2;
                        break;
                }
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(QrMainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(QrMainActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    ScannerActivity.gotoActivity(QrMainActivity.this, true, 1);
//                    ScannerActivity.gotoActivity(QrMainActivity.this, checkBox.isChecked(), laserMode);
                }
            }
        });

//        final EditText editText = (EditText) findViewById(editText);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Resources res = getResources();
//                Bitmap logoBitmap = BitmapFactory.decodeResource(res, R.mipmap.btn_wheelview_ok_normal);
//                String qrContent = editText.getText().toString();
//                Bitmap bitmap = new QREncode.Builder(QrMainActivity.this)
//                        //二维码颜色
//                        .setColor(getResources().getColor(R.color.mainColor_blue))
//                        //二维码类型
//                        .setParsedResultType(TextUtils.isEmpty(qrContent) ? ParsedResultType.URI : ParsedResultType.TEXT)
//                        //二维码内容
//                        .setContents(TextUtils.isEmpty(qrContent) ? "https://www.baidu.com" : qrContent)
////                        .setSize(100)
////                        .setLogoBitmap(logoBitmap,90)
//                        .build().encodeAsBitmap();
//                imageView.setImageBitmap(bitmap);
//                tvResult.setText("单击识别图中二维码");

            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
// 单击二维码识别暂时注释
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView.setDrawingCacheEnabled(true);//step 1
//                Bitmap bitmap = imageView.getDrawingCache();//step 2
//                //step 3 转bytes
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//
//                DeCodeActivity.gotoActivity(QrMainActivity.this, baos.toByteArray());//step 4
//                imageView.setDrawingCacheEnabled(false);//step 5
//            }
//        });
    }

    private void initshow(String peopleInfoEntity) {
        String qrContent = peopleInfoEntity;
        if (qrContent!=null){
            Bitmap bitmap = new QREncode.Builder(QrMainActivity.this)
                    //二维码颜色
                    .setColor(getResources().getColor(R.color.mainColor_blue))
                    //二维码类型
                    .setParsedResultType(TextUtils.isEmpty(qrContent) ? ParsedResultType.URI : ParsedResultType.TEXT)
                    //二维码内容
                    .setContents(TextUtils.isEmpty(qrContent) ? "当前信息显示错误" : qrContent)
//                        .setSize(100)
//                        .setLogoBitmap(logoBitmap,90)
                    .build().encodeAsBitmap();
            imageView.setImageBitmap(bitmap);
//            tvResult.setText("单击识别图中二维码");
        }else{
            tvResult.setText("系统无法生成二维码");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == ScannerActivity.REQUEST_CODE_SCANNER) {
                if (data != null) {
                    String stringExtra = data.getStringExtra(Intents.Scan.RESULT);
//                    tvResult.setText(stringExtra);
//                    tvResult.setTextSize(20);
//                    imageView.setVisibility(View.GONE);

                    AlertDialog alertDialog = new AlertDialog.Builder(QrMainActivity.this).create();
                    alertDialog.show();
                    Window window = alertDialog.getWindow();
                    window.setContentView(R.layout.layout_qrcode_result);
                    TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                    tv_title.setText("人员基本信息");
                    TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
                    tv_message.setText(stringExtra);
                    /*ViewGroup.LayoutParams para = imageView.getLayoutParams();
                    para.height = 0;
                    imageView.setLayoutParams(para);
                    imageView.setImageBitmap(null);
                    imageView.setVisibility(View.GONE);*/
                }
            } else if (requestCode == PICK_CONTACT) {
                // Data field is content://contacts/people/984
                showContactAsBarcode(data.getData());
            }
        }
    }

    /**
     * @param contactUri content://contacts/people/17
     */
    private void showContactAsBarcode(Uri contactUri) {
        //可以自己组装bundle;
//        ParserUriToVCard parserUriToVCard = new ParserUriToVCard();
//        Bundle bundle = parserUriToVCard.parserUri(this, contactUri);
//        if (bundle != null) {
//            Bitmap bitmap = QREncode.encodeQR(new QREncode.Builder(this)
//                    .setParsedResultType(ParsedResultType.ADDRESSBOOK)
//                    .setBundle(bundle).build());
//            imageView.setImageBitmap(bitmap);
//            tvResult.setText("单击二维码图片识别");
//        } else tvResult.setText("联系人Uri错误");

        //只传Uri
        Bitmap bitmap = new QREncode.Builder(this)
                .setParsedResultType(ParsedResultType.ADDRESSBOOK)
                .setAddressBookUri(contactUri).build().encodeAsBitmap();
        imageView.setImageBitmap(bitmap);
        tvResult.setText("单击二维码图片识别");
    }

}
