package com.example.hgtxxgl.application.fragment.personal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.view.HandToolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personal_center_about);
        HandToolbar handToolbar = (HandToolbar)findViewById(R.id.about_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(true, this);
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("关于智营");
        handToolbar.setTitleSize(18);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
    }
}
