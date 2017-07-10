package com.example.hgtxxgl.application.QrCode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.view.HandToolbar;
/**
 * Created by HGTXxgl on 2017/7/7.
 */
public class QRCodeActivity extends AppCompatActivity implements HandToolbar.OnButtonsClickCallback{

    private ImageView qrcode;
    private HandToolbar qrToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qrcode);
        initview();
    }

    private void initview() {
        qrcode = (ImageView) findViewById(R.id.image_qrcode);
//        qrcode.setImageBitmap(QRCode.createQRCode("123你好123123大家好12312你神经病啊3"));
        qrToolbar = (HandToolbar) findViewById(R.id.qr_toolbar);
        qrToolbar.setTitle("二维码名片");
        qrToolbar.setDisplayHomeAsUpEnabled(true,this);
        qrToolbar.setRightButton(R.mipmap.ic_more);
        qrToolbar.setTitleSize(18);
        qrToolbar.setButtonsClickCallback(this);
//        String[] Items={"保存到手机","扫描二维码"};
//        qrToolbar.setRightButtonDialog(QRCodeActivity.this,Items);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onButtonClickListner(HandToolbar.VIEWS views, int radioIndex) {

    }


}
