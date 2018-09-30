package com.sd.storage.dlib.comm;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.sd.storage.R;
import com.sd.storage.dlib.utils.StringUtils;

/**
 * 提示Dialog
 */
public class CommonMessageDialog extends Dialog {

    public TextView tv_massage;

    public Button bt_ok;

    public CommonMessageDialog(Context context, String msge){
        this(context, msge, null);
    }

    public CommonMessageDialog(Context context, String text, View.OnClickListener click) {
        super(context, R.style.loading_dialog);
        setContentView(R.layout.dialog_common_message);
        tv_massage = (TextView) findViewById(R.id.tv_massage);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        setCancelable(false);
        if(null != click){
            bt_ok.setOnClickListener(click);
        }else{
            bt_ok.setOnClickListener(cancelClick);
        }

        if(!StringUtils.isBlank(text)){
            tv_massage.setText(text);
        }
    }

    public void setOKText(CharSequence title){
        bt_ok.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            dismiss();
        }
    };
}
