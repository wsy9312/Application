package com.example.hgtxxgl.application.QrCode.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hgtxxgl.application.R;
import com.mylhyl.zxing.scanner.common.Intents;
//import com.mylhyl.zxing.scanner.sample.BasicActivity;
//import com.mylhyl.zxing.scanner.sample.R;

/**
 * 纯文本显示
 */
public class TextActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            ((TextView) findViewById(R.id.textView3)).setText(extras.getString(Intents.Scan.RESULT));
    }

    public static void gotoActivity(Activity activity, Bundle bundle) {
        activity.startActivity(new Intent(activity, TextActivity.class).putExtras(bundle));
    }
}
