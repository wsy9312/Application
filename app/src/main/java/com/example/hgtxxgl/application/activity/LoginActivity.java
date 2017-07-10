package com.example.hgtxxgl.application.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.Fields;
import com.example.hgtxxgl.application.utils.SpUtils;
import com.example.hgtxxgl.application.utils.ToastUtil;


//登录界面
public class LoginActivity extends AppCompatActivity {

    TextInputEditText etUsername;
    TextInputEditText etPassword;
    Button btnLogin;
    TextInputLayout inputLayout;
    ImageView ivAvatar;
    private boolean savePassword;
    private ProgressBar pb;
    private CheckBox savepassword;
    private RadioButton rb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        etUsername.setText(SpUtils.getString(getApplicationContext(), Fields.USERID));
        savePassword = SpUtils.getisBoolean_false(getApplicationContext(), Fields.SAVE_PASSWORD,false);
        etPassword.setText(SpUtils.getString(getApplicationContext(),Fields.PASSWORD));
        savepassword.setChecked(savePassword);
    }

    //初始化控件
    private void initView() {
        pb = (ProgressBar) findViewById(R.id.login_pb);
        etUsername = (TextInputEditText) findViewById(R.id.et_username);
        etPassword = (TextInputEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        inputLayout = (TextInputLayout) findViewById(R.id.tilyout_password);
        ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
        ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        savepassword = (CheckBox) findViewById(R.id.login_cb_savepassword);
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ivAvatar.setBackgroundResource(R.mipmap.ic_user);
                }else {
                    ivAvatar.setBackgroundResource(R.mipmap.ic_login_default);
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ivAvatar.setBackgroundResource(R.mipmap.ic_password);
                }else {
                    ivAvatar.setBackgroundResource(R.mipmap.ic_login_default);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onPasswordChanged();
            }
        });
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onUsernameChanged();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.saveisBoolean(getApplicationContext(), Fields.SAVE_PASSWORD,savepassword.isChecked());
                login(etUsername.getText().toString(), etPassword.getText().toString());
                SpUtils.saveString(getApplicationContext(), Fields.USERID,etUsername.getText().toString());
                if (savepassword.isChecked()){
                    SpUtils.saveString(getApplicationContext(),Fields.PASSWORD,etPassword.getText().toString());
                }else {
                    SpUtils.saveString(getApplicationContext(),Fields.PASSWORD,"");
                }
                pb.setVisibility(View.INVISIBLE);
                savepassword.setEnabled(true);
                btnLogin.setEnabled(true);
                etUsername.setEnabled(true);
                etPassword.setEnabled(true);
            }
        });

    }

    //用户名密码判空
    void onPasswordChanged() {
        onUsernameChanged();
    }

    void onUsernameChanged() {
        if (etUsername.length() > 0 && etPassword.length() > 0) {
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //输入用户名密码登录
    private void login(final String username, final String password) {
        //TODO 网络获取用户名密码数据
        if (username.equals("123")&&password.equals("123")){
            show("输入正确");
            toLibMainActivity(username,password);
        }
    }

    //跳转到首页activity
    private void toLibMainActivity(String username, String password) {
        LibMainActivity.startActivity(this,username,password);
    }

    //吐司
    private void show(final String asd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getApplicationContext(),asd);
            }
        });
    }
}
