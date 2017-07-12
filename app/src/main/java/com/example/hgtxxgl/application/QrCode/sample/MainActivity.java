package com.example.hgtxxgl.application.QrCode.sample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.ImgUtils;
import com.example.hgtxxgl.application.utils.StatusBarUtils;
import com.example.hgtxxgl.application.utils.ToastUtil;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Intents;
import com.mylhyl.zxing.scanner.encode.QREncode;

import java.io.ByteArrayOutputStream;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final int PICK_CONTACT = 1;
    private TextView tvResult;
    private ImageView imageView;
    private int laserMode;
    private Toolbar toolbar;
    private Bitmap bitmap;
    private static final int REQUEST_CODE_SAVE_IMG = 10;
    private static final String TAG = "MainActivity";
    private Context mContext;

    private void initTotal() {
        mContext = this;
        toolbar.inflateMenu(R.menu.base_toolbar_menu);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_item1) {
                    imageView.setDrawingCacheEnabled(true);
                    if (imageView.getDrawingCache() != null){
                        bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                        requestPermission();
                    }else{
                        ToastUtil.showToast(mContext,"图片不能为空");
                    }
                    imageView.setDrawingCacheEnabled(false);
                } else if (menuItemId == R.id.action_item2) {
                    ScannerActivity.gotoActivity(MainActivity.this, true, 1);

                }
                return true;
            }
        });
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
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(mContext, "保存图片成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    //授权结果，分发下去
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        //跳转到onPermissionsGranted或者onPermissionsDenied去回调授权结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //同意授权
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.i(TAG, "onPermissionsGranted:" + requestCode + ":" + list.size());
        saveImage();
    }

    //拒绝授权
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //打开系统设置，手动授权
            new AppSettingsDialog.Builder(this,"").build().show();
        }
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
    private HandToolbar handToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mContext = this;
        handToolbar = (HandToolbar) findViewById(R.id.qrcode_mainactivity_toolbar);
        handToolbar.setTitle("二维码名片");
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(true,this,R.mipmap.ic_more);
        handToolbar.setTitleSize(17);
        handToolbar.setButtonsClickCallback(new HandToolbar.OnButtonsClickCallback() {
            @Override
            public void onButtonClickListner(HandToolbar.VIEWS views, int radioIndex) {
               final String[] items = new String[]{"保存到相册","扫描二维码"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                            ScannerActivity.gotoActivity(MainActivity.this, true, 1);
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
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    ScannerActivity.gotoActivity(MainActivity.this, true, 1);
//                    ScannerActivity.gotoActivity(MainActivity.this, checkBox.isChecked(), laserMode);
                }
            }
        });

        final EditText editText = (EditText) findViewById(R.id.editText);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
//                Bitmap logoBitmap = BitmapFactory.decodeResource(res, R.mipmap.btn_wheelview_ok_normal);
                String qrContent = editText.getText().toString();
                Bitmap bitmap = new QREncode.Builder(MainActivity.this)
                        //二维码颜色
                        .setColor(getResources().getColor(R.color.mainColor_blue))
                        //二维码类型
                        .setParsedResultType(TextUtils.isEmpty(qrContent) ? ParsedResultType.URI : ParsedResultType.TEXT)
                        //二维码内容
                        .setContents(TextUtils.isEmpty(qrContent) ? "https://www.baidu.com" : qrContent)
//                        .setSize(100)
//                        .setLogoBitmap(logoBitmap,90)
                        .build().encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
                tvResult.setText("单击识别图中二维码");

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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setDrawingCacheEnabled(true);//step 1
                Bitmap bitmap = imageView.getDrawingCache();//step 2
                //step 3 转bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                DeCodeActivity.gotoActivity(MainActivity.this, baos.toByteArray());//step 4
                imageView.setDrawingCacheEnabled(false);//step 5
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
                    tvResult.setText(stringExtra);
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
