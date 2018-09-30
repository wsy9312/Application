package com.sd.storage.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.widget.TextView;

import com.sd.storage.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class WaitingDialog extends Dialog {
    private String text = "";
    public TextView txtView;


    public WaitingDialog(Context context, int defStyle) {
        super(context, defStyle);
        init(context);
    }

    public WaitingDialog(@NonNull Context context, String str) {
        super(context);
        text = str;
        init(context);
    }


    public void init(Context context) {
        text = context.getString(R.string.watting_str);
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        setOnKeyListener(keylistener);
    }

    public WaitingDialog(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        setOnKeyListener(keylistener);
    }


    public void setMessage(String message) {
        txtView.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait);
        txtView = (TextView) findViewById(R.id.waiting_tv);
        txtView.setText(text);
    }

    /**
     * dialog显示的时候，监听返回键
     */
    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                dismiss();
                return true;
            } else {
                return false;
            }
        }
    };


    public void setRoundName(String text) {
        this.text = text;
        if (txtView != null) {
            txtView.setText(text);
        }
    }


}
