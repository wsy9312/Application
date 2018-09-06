package com.example.hgtxxgl.application.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

/**
 * Created by HGTXxgl on 2018/6/22.
 */

public class MyDialog extends Dialog implements View.OnClickListener {
    private EditText prePassword;
    private EditText newPassword;
    private EditText confirmPassword;
    private Button submit;
    private Button cancel;
    Context context;
    private String prepassword;
    private String newpassword;
    private String confirmpassword;


    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.setting_user_password_dialog);
//        prePassword = (EditText) findViewById(R.id.prePassword);
//        newPassword = (EditText) findViewById(R.id.newPassword);
//        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
//        submit = (Button) findViewById(R.id.dialog_button_ok);
//        cancel = (Button) findViewById(R.id.dialog_button_cancel);
//        submit.setOnClickListener(this);
//        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        prepassword = prePassword.getText().toString();
//        L.e(prepassword+"山鸡");
//        newpassword = newPassword.getText().toString();
//        L.e(prepassword+"车内");
//        confirmpassword = confirmPassword.getText().toString();
//        L.e(prepassword+"天机");
//        if (v.equals(submit)) {
//            if (!TextUtils.isEmpty(prepassword)){
//                if (prepassword.equals(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getPassword())){
//                    if (!TextUtils.isEmpty(newpassword) && !TextUtils.isEmpty(confirmpassword)){
//                        if (newpassword.equals(confirmpassword)){
//                            modify(newpassword);
//                        }else{
//                            ToastUtil.showToast(context,"新密码输入不一致,请重新输入!");
//                        }
//                    }else{
//                        ToastUtil.showToast(context,"新密码不能为空!");
//                    }
//                }else{
//                    ToastUtil.showToast(context,"原密码输入错误,请重新输入");
//                }
//            }else{
//                ToastUtil.showToast(context,"原密码不能为空!");
//            }
//        } else if (v.equals(cancel)) {
//            return;
//        }
    }

    private void modify(final String newpassword) {
        PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
        PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
        peopleInfoBean.setPassword(newpassword);
        peopleInfoBean.setNoIndex(ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).getNoIndex());
        peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getApi_Add_Login().get(0).getAuthenticationNo());
        peopleInfoBean.setIsAndroid("1");
        List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
        beanList.add(peopleInfoBean);
        peopleEntity.setPeopleInfo(beanList);
        String json = new Gson().toJson(peopleEntity);
        String s1 = "modify " + json;
        SharedPreferences share = context.getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        String tempIP = share.getString("tempIP", "");
        HttpManager.getInstance().requestResultForm(tempIP,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
            @Override
            public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                if (peopleInfoEntity != null){
//                    show("123");
                    ToastUtil.showToast(context,"修改成功!");

                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {
                if (response.contains("error")){
//                    show(response);
                }else if (response.contains("ok")){
                    ApplicationApp.getPeopleInfoEntity().getPeopleInfo().get(0).setPassword(newpassword);
                }
            }
        });
    }

//    private void show(final String msg) {
//        getContext()
//        context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ToastUtil.showToast(getApplicationContext(),msg);
//            }
//        });
//    }

}
