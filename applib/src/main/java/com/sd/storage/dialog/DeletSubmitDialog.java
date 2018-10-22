package com.sd.storage.dialog;


import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sd.storage.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/1/18.
 */

public class DeletSubmitDialog extends DialogFragment implements View.OnClickListener {


    private String vegeName;
    private TextView textView,tv_sure,tv_cancel;
    public View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view = inflater.inflate(R.layout.dialog_submit_activity, container, false);
        textView = (TextView) view.findViewById(R.id.tv_content);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_sure.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        textView.setText(vegeName);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            // dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), 400);
        }
    }


    public void setVegeName(String vegeName) {
        this.vegeName = vegeName;
       // textView.setText(vegeName);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_sure) {
            if (null != onSureClickListener) {
                onSureClickListener.onSubmitSure();
            }
        } else if (i == R.id.tv_cancel) {
            this.dismiss();

        }
    }

    private OnSureClickListener onSureClickListener;

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        this.onSureClickListener = onSureClickListener;
    }


    public interface OnSureClickListener {
        void onSubmitSure();

    }


}
