package com.example.hgtxxgl.application.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by HGTXxgl on 2018/6/22.
 */

public class MyDialog extends Dialog {

    Context context;

    public MyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
