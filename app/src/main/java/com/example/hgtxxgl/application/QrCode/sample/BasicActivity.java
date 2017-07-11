package com.example.hgtxxgl.application.QrCode.sample;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.StatusBarUtils;

public class BasicActivity extends AppCompatActivity {
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
    }
}
