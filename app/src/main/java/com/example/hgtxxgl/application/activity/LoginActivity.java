package com.example.hgtxxgl.application.activity;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.bean.login.LoginBean;
import com.example.hgtxxgl.application.bean.login.LoginInfoBean;
import com.example.hgtxxgl.application.bean.login.PeopleInfoBean;
import com.example.hgtxxgl.application.bean.people.PeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurrentCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount10Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount11Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount12Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount8Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveCurveCount9Bean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveOutCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveRestCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveSickCountBean;
import com.example.hgtxxgl.application.bean.temp.TempPeopleLeaveWorkCountBean;
import com.example.hgtxxgl.application.utils.FileService;
import com.example.hgtxxgl.application.utils.RxPermissionsTool;
import com.example.hgtxxgl.application.utils.SysExitUtil;
import com.example.hgtxxgl.application.utils.hand.ApplicationApp;
import com.example.hgtxxgl.application.utils.hand.Fields;
import com.example.hgtxxgl.application.utils.hand.HttpManager;
import com.example.hgtxxgl.application.utils.hand.NetworkHttpManager;
import com.example.hgtxxgl.application.utils.hand.SpUtils;
import com.example.hgtxxgl.application.utils.hand.StatusBarUtil;
import com.example.hgtxxgl.application.utils.hand.ToastUtil;
import com.example.hgtxxgl.application.utils.hyutils.L;
import com.example.hgtxxgl.application.view.IPEditText;
import com.example.hgtxxgl.application.view.UrlListAdapter;
import com.example.hgtxxgl.application.view.UrlSelector;
import com.google.gson.Gson;
import com.sd.storage.UrlManager;

import java.util.Map;

import okhttp3.Request;

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
    private String DEMO_URL;
    private LoginActivity mContext;
    private String authenticationNo;
    private String timeStamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);
//        StatusBarUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        StatusBarUtil.fullScreen(this);
        initView();
        mContext = this;
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

        RxPermissionsTool.
                with(mContext).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                initPermission();
    }

    private void destoryView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
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
                UrlManager.setHttpRoot(tempIP);

                if (apnType == 0){
                    show(getString(R.string.current_no_network));
                }else if (TextUtils.isEmpty(tempIP)){
                    show(getString(R.string.set_ip_and_port_first));
                }else{
                    SpUtils.saveisBoolean(getApplicationContext(), Fields.SAVE_PASSWORD,savepassword.isChecked());
                    login(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
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

        final UrlListAdapter urlListAdapter = new UrlListAdapter(LoginActivity.this);
        SharedPreferences share = getSharedPreferences(SAVE_IP, MODE_PRIVATE);
        final SharedPreferences.Editor edit = share.edit();
        settingIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这一步是为了防止二次点击出现闪退
                destoryView(view);
                UrlSelector.launch(LoginActivity.this, urlListAdapter, new UrlSelector.OnUrlChangedListener() {
                    @Override
                    public void urlChanged(String url) {
                        DEMO_URL = url;
                        edit.putString("tempIP", DEMO_URL);
                        edit.commit();

                    }
                });

            }
        });
        DEMO_URL = urlListAdapter.getCheckedUrl();
        edit.putString("tempIP", DEMO_URL);
        edit.commit();
    }

    private void login(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginBean loginBean = new LoginBean();
                loginBean.setLoginName(username);
                loginBean.setPassword(password);
                loginBean.setIsAndroid("1");
                String toJson = new Gson().toJson(loginBean);
                String s="Api_Add_Login "+toJson;
                Log.e(TAG,"登录请求json:"+s);
                HttpManager.getInstance().requestNewResultForm(tempIP, s, LoginInfoBean.class, new HttpManager.ResultNewCallback<LoginInfoBean>() {
                    @Override
                    public void onSuccess(String json, LoginInfoBean loginInfoBean) throws Exception {
                        Log.e(TAG,"onSuccess:"+json);
                        if (json.contains("error")){
                            show("用户名或密码错误");
                            return;
                        }
                        if (loginInfoBean != null||loginInfoBean.getApi_Add_Login().size() == 0){
                            ApplicationApp.setLoginInfoBean(loginInfoBean);
                            authenticationNo = loginInfoBean.getApi_Add_Login().get(0).getAuthenticationNo();
                            timeStamp = loginInfoBean.getApi_Add_Login().get(0).getTimeStamp();
                            getPeopleInfo(username,password, authenticationNo,timeStamp);
                        }
                    }

                    @Override
                    public void onError(String msg) throws Exception {
                        Log.e(TAG,"onError:"+msg);
                        if (msg.contains("Failed to connect")){
                            ToastUtil.showToast(getApplicationContext(),"连接服务器失败!");
                        }
                    }

                    @Override
                    public void onResponse(String response) throws Exception {
                        Log.e(TAG,"onResponse:"+response);
                    }

                    @Override
                    public void onBefore(Request request, int id) throws Exception {
                        Log.e(TAG,"onBefore:"+request.toString());
                    }

                    @Override
                    public void onAfter(int id) throws Exception {
                        Log.e(TAG,"onAfter:"+id);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) throws Exception {
                    }
                });
            }
        }).start();
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

    private void getPeopleInfo(final String username, final String password, final String no,final String timeStamp){
        //个人资料
        PeopleInfoBean.ApiGetMyInfoSimBean peopleInfoBean = new PeopleInfoBean.ApiGetMyInfoSimBean();
        peopleInfoBean.setNo("?");
        peopleInfoBean.setName("?");
        peopleInfoBean.setCardNo("?");
        peopleInfoBean.setPosition("?");
        peopleInfoBean.setSex("?");
        peopleInfoBean.setUnit("?");
        peopleInfoBean.setDepartment("?");
        peopleInfoBean.setPhoneNo("?");
        peopleInfoBean.setTelNo("?");
        peopleInfoBean.setLoginName(username);
        peopleInfoBean.setPassword(password);
        peopleInfoBean.setAuthority("?");
        peopleInfoBean.setModifyTime("?");
        peopleInfoBean.setRegisterTime("?");
        peopleInfoBean.setNoIndex("?");
        peopleInfoBean.setAuthenticationNo(no);
        peopleInfoBean.setIsAndroid("1");
        peopleInfoBean.setTimeStamp(timeStamp);
        String json = new Gson().toJson(peopleInfoBean);
        String s1 = "Api_Get_MyInfoSim " + json;
        Log.e(TAG,"获取个人信息:"+s1);
        HttpManager.getInstance().requestNewResultForm(tempIP,s1,PeopleInfoBean.class,new HttpManager.ResultNewCallback<PeopleInfoBean>() {
            @Override
            public void onSuccess(String json, PeopleInfoBean peopleInfoBean) throws Exception {
                if (peopleInfoBean != null){
                    ApplicationApp.setPeopleInfoBean(peopleInfoBean);
                    toLibMainActivity(username,password);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartTotalNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }

        });
    }

    private void loadChartTotalNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartTotalNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartTotalNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartCurrentNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartCurrentNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("0");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartCurrentNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurrentCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurrentCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurrentCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartCurrentNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCurrentCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartWorkNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartWorkNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("事假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartWorkNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveWorkCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveWorkCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveWorkCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartWorkNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setWorkCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartSickNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartSickNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("病假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartSickNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveSickCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveSickCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveSickCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartSickNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setSickCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartRestNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartRestNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("休假申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartRestNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveRestCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveRestCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveRestCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartRestNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setRestCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadChartOutNum();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadChartOutNum() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setOutType("外出申请");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadChartOutNum",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveOutCountBean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveOutCountBean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveOutCountBean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadChartOutNum",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setOutCountBean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart8Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart8Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-08-01 00:00:00&&2018-08-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart8Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount8Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount8Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount8Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart8Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount8Bean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart9Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart9Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-09-01 00:00:00&&2018-09-30 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart9Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount9Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount9Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount9Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart9Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount9Bean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart10Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart10Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-10-01 00:00:00&&2018-10-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart10Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount10Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount10Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount10Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart10Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount10Bean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart11Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart11Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-11-01 00:00:00&&2018-11-30 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart11Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount11Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount11Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount11Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart11Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount11Bean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
                loadTempCurveChart12Num();
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

            }
        });
    }

    private void loadTempCurveChart12Num() {
        PeopleLeaveCountBean.ApiGetCountBean countBean = new PeopleLeaveCountBean.ApiGetCountBean();
        countBean.setTimeStamp(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getTimeStamp());
        countBean.setAuthenticationNo(ApplicationApp.getLoginInfoBean().getApi_Add_Login().get(0).getAuthenticationNo());
        countBean.setTableName("PeopleInfo");
        countBean.setIsAndroid("1");
        countBean.setbClosed("0");
        countBean.setOutStatus("1");
        countBean.setRegisterTime("2018-12-01 00:00:00&&2018-12-31 23:59:59");
        String json = new Gson().toJson(countBean);
        String request = "Api_Get_Count " + json;
        L.e(TAG+" loadTempCurveChart12Num",request);
        HttpManager.getInstance().requestNewResultForm(tempIP, request, TempPeopleLeaveCurveCount12Bean.class, new HttpManager.ResultNewCallback<TempPeopleLeaveCurveCount12Bean>() {
            @Override
            public void onSuccess(String json, final TempPeopleLeaveCurveCount12Bean peopleLeaveCountBean) throws Exception {
                L.e(TAG+" loadTempCurveChart12Num",json);
                if (peopleLeaveCountBean != null || peopleLeaveCountBean.getApi_Get_Count().size()>0){
                    ApplicationApp.setCount12Bean(peopleLeaveCountBean);
                }
            }

            @Override
            public void onError(String msg) throws Exception {

            }

            @Override
            public void onResponse(String response) throws Exception {

            }

            @Override
            public void onBefore(Request request, int id) throws Exception {

            }

            @Override
            public void onAfter(int id) throws Exception {
            }

            @Override
            public void inProgress(float progress, long total, int id) throws Exception {

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
