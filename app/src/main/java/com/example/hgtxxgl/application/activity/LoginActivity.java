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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.hgtxxgl.application.utils.FileService;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.Fields;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.NetworkHttpManager;
import com.example.hgtxxgl.application.utils.hand.SpUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtils;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.view.IPEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public FileService fileService;
    private EditText edit1;
    private EditText edit2;
    private EditText edit3;
    private EditText edit4;
    private EditText edit5;
    private View view;
    private IPEditText iptext;

    //创建播放视频的控件对象
//    private CustomVideoView videoview;

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

        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        view = inflater.inflate(R.layout.layout_iptext, null, true);
        iptext = (IPEditText) view.findViewById(R.id.iptext);
        edit1 = (EditText) iptext.findViewById(R.id.edit1);
        edit2 = (EditText) iptext.findViewById(R.id.edit2);
        edit3 = (EditText) iptext.findViewById(R.id.edit3);
        edit4 = (EditText) iptext.findViewById(R.id.edit4);
        edit5 = (EditText) iptext.findViewById(R.id.edit5);

        fileService=new FileService(this);
        Map<String, Integer> map=fileService.readFile("private.txt");
        if(map!=null){
            edit1.setText(map.get("ip1")+"");
            edit2.setText(map.get("ip2")+"");
            edit3.setText(map.get("ip3")+"");
            edit4.setText(map.get("ip4")+"");
            edit5.setText(map.get("ip5")+"");
        }
    }

    private void destoryView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
    }

    //返回重启加载
//    @Override
//    protected void onRestart() {
//        initView();
//        super.onRestart();
//    }

    //防止锁屏或者切出的时候，音乐在播放
//    @Override
//    protected void onStop() {
//        videoview.stopPlayback();
//        super.onStop();
//    }

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
        //加载视频资源控件
//        videoview = (CustomVideoView) findViewById(videoview);
        //设置播放加载路径
//        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
//        videoview.start();
        //循环播放
//        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                videoview.start();
//            }
//        });
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
                if (apnType == 0){
                    show(getString(R.string.current_no_network));
                }else if (TextUtils.isEmpty(tempIP)){
                    show(getString(R.string.set_ip_and_port_first));
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
                //这一步是为了防止二次点击出现闪退
                destoryView(view);
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.please_set_ip_and_port)
                        .setView(view)
                        .setPositiveButton(R.string.make_sure, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String ip = "http://"+ iptext.getText().toString()+"/";
                                if (edit1.getText().toString().trim().isEmpty()|| edit2.getText().toString().trim().isEmpty()
                                    || edit3.getText().toString().trim().isEmpty()|| edit4.getText().toString().trim().isEmpty()
                                    || edit5.getText().toString().trim().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "请输入完整的地址!", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                /*if(iptext.getText().toString().equals("")){
                                    Toast.makeText(getApplicationContext(), R.string.address_can_not_null, Toast.LENGTH_LONG).show();
                                }else*/else{
                                    SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
                                    SharedPreferences.Editor edit = share.edit();
                                    edit.putString("tempIP", ip);
                                    edit.commit();
                                    LoginActivity.this.fileService.saveToRom(ip, "private.txt");
                                    Toast.makeText(getApplicationContext(), "设置成功!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel,null)
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
                Log.e(TAG,"登录:"+s);
//                String url = CommonValues.BASE_URL;
//                String url = ApplicationApp.getIP();
                HttpManager.getInstance().requestResultForm(tempIP, s, NewLoginEntity.class, new HttpManager.ResultCallback<NewLoginEntity>() {
                    @Override
                    public void onSuccess(String json, NewLoginEntity loginEntity) throws InterruptedException {
                        if (loginEntity != null){
                            ApplicationApp.setNewLoginEntity(loginEntity);
                            String no = loginEntity.getLogin().get(0).getAuthenticationNo();
                            getPeopleInfo(username,password,no);
                            if (username.equals("Admin")){
                                show(getString(R.string.name_or_password_error));
                            }
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        show(msg);
                    }

                    @Override
                    public void onResponse(String response) {
                        if (response.contains("error")){
                            show(getString(R.string.name_or_password_error));
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
        peopleInfoBean.setDepartment("?");
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
        Log.e(TAG,"获取个人信息:"+s1);
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
                    peopleInfoBean.setDepartment("");
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
                    show(getString(R.string.personal_data_error));
                }
            }
        });
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
