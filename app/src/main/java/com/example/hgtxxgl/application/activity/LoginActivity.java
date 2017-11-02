package com.example.hgtxxgl.application.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.LoginEntity;
import com.example.hgtxxgl.application.entity.NewLoginEntity;
import com.example.hgtxxgl.application.entity.PeopleInfoEntity;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.Fields;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.NetworkHttpManager;
import com.example.hgtxxgl.application.utils.hand.SpUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.hgtxxgl.application.utils.hand.Fields.SAVE_IP;

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
    private ImageView settingIP;
    private String tempIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        initView();
        SysExitUtil.activityList.add(LoginActivity.this);
        etUsername.setText(SpUtils.getString(getApplicationContext(), Fields.USERID));
        savePassword = SpUtils.getisBoolean_false(getApplicationContext(), Fields.SAVE_PASSWORD,false);
        etPassword.setText(SpUtils.getString(getApplicationContext(),Fields.PASSWORD));
        savepassword.setChecked(savePassword);
    }

    //初始化控件
    private void initView() {
        pb = (ProgressBar) findViewById(R.id.login_pb);
        settingIP = (ImageView) findViewById(R.id.setting_ip);
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
                int apnType = NetworkHttpManager.getAPNType(getApplicationContext());
                SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
                tempIP = share.getString("tempIP", "");
                if (apnType != 1){
                    show("当前无可用网络");
                }else if (TextUtils.isEmpty(tempIP)){
                    show("首次登录请设置IP地址及端口号");
                }else{
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
            }
        });

        settingIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(LoginActivity.this);
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("请设置IP地址及端口号")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String ip = "http://"+ et.getText().toString()+"/";
                                if (ip.equals("")){
                                    Toast.makeText(getApplicationContext(), "地址不能为空!", Toast.LENGTH_LONG).show();
                                }else{
//                                    ApplicationApp.setIP(ip);
                                    SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
                                    SharedPreferences.Editor edit = share.edit();
                                    edit.putString("tempIP", ip);
                                    edit.commit();
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
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
//                String url = CommonValues.BASE_URL;
//                String url = ApplicationApp.getIP();
                HttpManager.getInstance().requestResultForm(tempIP, s, NewLoginEntity.class, new HttpManager.ResultCallback<NewLoginEntity>() {
                    @Override
                    public void onSuccess(String json, NewLoginEntity loginEntity) throws InterruptedException {
                        if (loginEntity != null){
                            ApplicationApp.setNewLoginEntity(loginEntity);
                            String no = loginEntity.getLogin().get(0).getAuthenticationNo();
                            getPeopleInfo(username,password,no);
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        show(msg);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.contains("error")){
                            show("用户名或密码错误!");
                            return;
                        }
                    }
                });


            }
        }).start();

    }

    private void getPeopleInfo(final String username, final String password, final String no){
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
        peopleInfoBean.setAuthenticationNo(no);
        peopleInfoBean.setIsAndroid("1");
        List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
        beanList.add(peopleInfoBean);
        peopleEntity.setPeopleInfo(beanList);
        String json = new Gson().toJson(peopleEntity);
        String s1 = "get " + json;
        HttpManager.getInstance().requestResultForm(tempIP,s1,PeopleInfoEntity.class,new HttpManager.ResultCallback<PeopleInfoEntity>() {
            @Override
            public void onSuccess(String json, PeopleInfoEntity peopleInfoEntity) throws InterruptedException {
                if (peopleInfoEntity != null){
                    ApplicationApp.setPeopleInfoEntity(peopleInfoEntity);
                    toLibMainActivity(username,password);
                }
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onResponse(String response) {
                if (response.contains("error")){
                    PeopleInfoEntity peopleEntity = new PeopleInfoEntity();
                    PeopleInfoEntity.PeopleInfoBean peopleInfoBean = new PeopleInfoEntity.PeopleInfoBean();
                    peopleInfoBean.setNo("");
                    peopleInfoBean.setName("");
                    peopleInfoBean.setCardNo("");
                    peopleInfoBean.setPosition("");
                    peopleInfoBean.setSex("");
                    peopleInfoBean.setUnit("");
                    peopleInfoBean.setArmyGroup("");
                    peopleInfoBean.setPhoneNo("");
                    peopleInfoBean.setTelNo("");
                    peopleInfoBean.setGroupName("");
                    peopleInfoBean.setLoginName(username);
                    peopleInfoBean.setPassword(password);
                    peopleInfoBean.setAuthority("");
                    peopleInfoBean.setModifyTime("");
                    peopleInfoBean.setRegisterTime("");
                    peopleInfoBean.setNoIndex("");
                    peopleInfoBean.setAuthenticationNo("");
                    peopleInfoBean.setIsAndroid("1");
                    List<PeopleInfoEntity.PeopleInfoBean> beanList = new ArrayList<>();
                    beanList.add(peopleInfoBean);
                    peopleEntity.setPeopleInfo(beanList);
                    ApplicationApp.setPeopleInfoEntity(peopleEntity);
                    show("个人资料获取错误");
                }
            }
        });
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
