package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hgtxxgl.application.QrCode.sample.MainActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.LoginEntity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;


public class PersonalFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = PersonalFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 6384;
    private Button mLogout;
    private RelativeLayout relativeLayout;
    private RelativeLayout relativeLayout1;
    private RelativeLayout rlQRcode;
    private View view;

    public PersonalFragment() {

    }

    public static PersonalFragment newInstance(int tab) {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        args.putInt(DetailFragment.ARG_TAB, tab);
        fragment.setArguments(args);
        return fragment;
    }
    private DetailFragment.DataCallback callback;
    public PersonalFragment setCallback(DetailFragment.DataCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView();
//        onClickToNewFragment();
        return view;
    }

    private void initView() {
        mLogout = (Button) view.findViewById(R.id.btn_logout);
        rlQRcode = (RelativeLayout) view.findViewById(R.id.rl_qrcode);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_number);
        relativeLayout1 = (RelativeLayout) view.findViewById(R.id.rl_name);
        mLogout.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        relativeLayout1.setOnClickListener(this);
        rlQRcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                logOut();
                break;
            case R.id.rl_number:
//                ceshi();
                break;
            case R.id.rl_name:
                ceshijson();
                break;
            case R.id.rl_qrcode:
                skipToQR();
                break;
        }
    }

    private void ceshijson() {
    }

    private void skipToQR() {
        Intent intent = new Intent(getContext(),MainActivity.class);
        startActivity(intent);
    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
//            setTitle("loading...");
            Log.e("xglonBefore","loading...");
        }

        @Override
        public void onAfter(int id)
        {
            Log.e("xglonAfter","Sample-okHttp");
//            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
//            mTv.setText("onError:" + e.getMessage());
            Log.e("xglonError",e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
//            mTv.setText("onResponse:" + response);
            Log.e("xglonResponse",response);
            switch (id)
            {
                case 100:
                    Toast.makeText(getContext(), "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(getContext(), "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void logOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginEntity loginEntity = new LoginEntity();
                LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
                loginBean.setLoginName("person1");
                loginBean.setPassword("person1");
                List<LoginEntity.LoginBean> list = new ArrayList<>();
                list.add(loginBean);
                loginEntity.setLogin(list);
                String toJson = new Gson().toJson(loginEntity);
                Log.d("test",toJson);
                String s="Login"+" "+toJson;
                String url = "http://192.168.1.137:8080/";
                try {
                    OkHttpUtils
                            .postString()
                            .url(url)
                            .mediaType(MediaType.parse("application/json; charset=utf-8"))
                            .content(s)
                            .build()
                            .readTimeOut(10000L)
                            .writeTimeOut(10000L)
                            .connTimeOut(10000L)
                            .execute(new MyStringCallback());
                   /* if (execute!=null){
                        String ResponseStr = execute.body().string();
                        if (ResponseStr != null){
                            Log.e(TAG,"ResponseStr = " + ResponseStr);
                        }else{
                            Log.e(TAG,"ResponseStr = null");
                        }
                    }else{
                        Log.e(TAG,"execute = null");
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"IOException ="+e.toString());
                }
            }
        }).start();

//        LoginEntity loginEntity = new LoginEntity();
//        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
//        loginBean.setLoginName("Admin");
//        loginBean.setPassword("123456");
//        List<LoginEntity.LoginBean> list = new ArrayList<>();
//        list.add(loginBean);
//        loginEntity.setLogin(list);
//        String toJson = new Gson().toJson(loginEntity);
//        Log.d("test",toJson);
//        String s="Login"+" "+toJson;
//        String url = "http://192.168.1.102:8080/" + "user!postString";
//        OkHttpUtils
//                .postString()
//                .url(url)
//                .mediaType(MediaType.parse("application/json; charset=utf-8"))
//                .content(s)
//                .build()
//                .execute(new MyStringCallback());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
