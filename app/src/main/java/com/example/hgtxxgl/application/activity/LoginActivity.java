package com.example.hgtxxgl.application.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.LoginEntity;
import com.example.hgtxxgl.application.entity.NewLoginEntity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.CommonValues;
import com.example.hgtxxgl.application.utils.hand.Fields;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.SpUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//登录界面
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    TextInputEditText etUsername;
    TextInputEditText etPassword;
    Button btnLogin;
    TextInputLayout inputLayout;
    ImageView ivAvatar;
    private boolean savePassword;
    private ProgressBar pb;
    private CheckBox savepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginEntity loginEntity = new LoginEntity();
                LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
                loginBean.setLoginName(username);
                loginBean.setPassword(password);
                loginBean.setIsAndroid("1");
                List<LoginEntity.LoginBean> list = new ArrayList<>();
                list.add(loginBean);
                loginEntity.setLogin(list);
                String toJson = new Gson().toJson(loginEntity);
                String s="Login "+toJson;
                String url = CommonValues.BASE_URL;
                HttpManager.getInstance().requestResultForm(url, s, NewLoginEntity.class, new HttpManager.ResultCallback<NewLoginEntity>() {
                    @Override
                    public void onSuccess(String json, NewLoginEntity loginEntity) throws InterruptedException {
                        if (loginEntity != null){
                            ApplicationApp.setNewLoginEntity(loginEntity);
                            Thread.sleep(300);
                            toLibMainActivity(username,password);
                        }
                        Log.e(TAG,"登录"+json);
                    }

                    @Override
                    public void onFailure(String msg) {
                        show(msg);
                    }

                    @Override
                    public void onResponse(String response) {
                    }
                });
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //个人资料
                PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                peopleInfoBean.setNo("?");
                peopleInfoBean.setName("?");
                peopleInfoBean.setCardNo("?");
                peopleInfoBean.setPosition("?");
                peopleInfoBean.setSex("?");
                peopleInfoBean.setUnit("?");
                peopleInfoBean.setArmyGroup("?");
                peopleInfoBean.setPhoneNo("?");
                peopleInfoBean.setTelNo("?");
                peopleInfoBean.setGroupName("?");
                peopleInfoBean.setLoginName(username);
                peopleInfoBean.setPassword(password);
                peopleInfoBean.setAuthority("?");
                peopleInfoBean.setModifyTime("?");
                peopleInfoBean.setRegisterTime("?");
                peopleInfoBean.setNoIndex("?");
                peopleInfoBean.setAuthenticationNo(ApplicationApp.getNewLoginEntity().getLogin().get(0).getAuthenticationNo());
                peopleInfoBean.setIsAndroid("1");
                List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                beanList.add(peopleInfoBean);
                peopleEntity.setPeopleInfo(beanList);
                String json = new Gson().toJson(peopleEntity);
                String s1 = "get " + json;
                Log.e(TAG,"个人资料获取："+s1);
                HttpManager.getInstance().requestResultForm(CommonValues.BASE_URL,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
                    @Override
                    public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                        if (peopleInfoEntity != null){
                            ApplicationApp.setPeopleInfoEntity(peopleInfoEntity);
                            Log.e(TAG,"个人资料1:"+ json);
                            Log.e(TAG,"个人资料2:"+ peopleInfoEntity.toString());
                            show("个人资料保存成功");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {

                    }

                    @Override
                    public void onResponse(String response) {

                    }
                });
            }
        }).start();

    }

    //跳转到首页activity
    private void toLibMainActivity(String username, String password) {
        LibMainActivity.startActivity(this,username,password);
    }

    //吐丝
    private void show(final String asd) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showToast(getApplicationContext(),asd);
            }
        });
    }
}
