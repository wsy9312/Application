package com.example.hgtxxgl.application.QrCode.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.mylhyl.zxing.scanner.common.Intents;
//import com.mylhyl.zxing.scanner.sample.BasicActivity;
//import com.mylhyl.zxing.scanner.sample.R;

/**
 * 纯文本显示
 */
public class TextActivity extends AppCompatActivity {
    private HandToolbar handToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        handToolbar = (HandToolbar) findViewById(R.id.activity_text_qr_toolbar);
        handToolbar.setTitle("识别结果");
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(false,this,0);
        handToolbar.setTitleSize(20);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            ((TextView) findViewById(R.id.textView3)).setText(extras.getString(Intents.Scan.RESULT));
    }

    public static void gotoActivity(Activity activity, Bundle bundle) {
        activity.startActivity(new Intent(activity, TextActivity.class).putExtras(bundle));
    }
}
