package com.example.hgtxxgl.application.fragment;

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

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.entity.JsonRootBean;
import com.example.hgtxxgl.application.entity.Login;
import com.example.hgtxxgl.application.entity.LoginEntity;
import com.example.hgtxxgl.application.network.NetworkApi;
import com.example.hgtxxgl.application.network.RetrofitUtils;
import com.example.hgtxxgl.application.utils.ToastUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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
//        ToastUtil.showToast(getContext(),"测试json");
//        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
//        if (api == null){
//            return;
//        }
//        String json = "123123";
//        api.loginFromWJ(json, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//                ToastUtil.showToast(getContext(),"response = "+response+"response2 = "+response2);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ToastUtil.showToast(getContext(),"失败");
//            }
//        });

    }

    private void skipToQR() {
        /*Intent intent = new Intent(getContext(),MainActivity.class);
        startActivity(intent);*/
        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
//        LoginEntity loginEntity = new LoginEntity();
//        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
//        loginBean.setLoginName("111111");
//        loginBean.setPassword("22222");
//        List<LoginEntity.LoginBean> list = new ArrayList<>();
//        list.add(loginBean);
//        loginEntity.setLogin(list);
//        System.out.println(loginEntity);
        JsonRootBean jsonRootBean = new JsonRootBean();
        Login login = new Login();
        login.setLoginName("111111");
        login.setPassword("2222");
        List<Login> list = new ArrayList<>();
        list.add(login);
        jsonRootBean.setLogin(list);
        Map<String, Object> params = new HashMap<>();
        params.put("Login",/*loginEntity*/jsonRootBean);
        api.postCeShiFromWJ(params, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                ToastUtil.showToast(getContext(),"成功");
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.showToast(getContext(),"失败");
            }
        });

    }
    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
//            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
//            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
//            mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
//            mTv.setText("onResponse:" + response);

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

    List list=new ArrayList();
    private void logOut() {
        LoginEntity loginEntity = new LoginEntity();
        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
        loginBean.setLoginName("Admin");
        loginBean.setPassword("123456");
        List<LoginEntity.LoginBean> list = new ArrayList<>();
        list.add(loginBean);
        loginEntity.setLogin(list);

       /* JsonRootBean jsonRootBean = new JsonRootBean();
        Login login = new Login();
        login.setLoginName("Admin");
        login.setPassword("123456");
        list.add(login);
        jsonRootBean.setLogin(list);*/
        String toJson = new Gson().toJson(loginEntity);
        Log.e("test",toJson);
        String s="Login"+" "+toJson;
        String url = "http://192.168.1.109:8080/" + "user!postString";
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(s)
                .build()
                .execute(new MyStringCallback());
/*
        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
        Map<String, Object> params = new HashMap<>();
        params.put("Admin",1111);
        params.put("PassWord",2222);
        api.postCeShiFromWJ(params, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/
        //json字符串格式正确,表单名缺
        /*NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
        LoginEntity loginEntity = new LoginEntity();
        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
        loginBean.setLoginName("111111");
        loginBean.setPassword("22222");
        List<LoginEntity.LoginBean> list = new ArrayList<>();
        list.add(loginBean);
        loginEntity.setLogin(list);
        api.postString(loginEntity, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/
       /* NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
        LoginEntity loginEntity = new LoginEntity();
        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
        loginBean.setLoginName("Admin");
        loginBean.setPassword("123456");
        List<LoginEntity.LoginBean> list = new ArrayList<>();
        list.add(loginBean);
        loginEntity.setLogin(list);
        System.out.println(loginEntity);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(loginEntity);
        System.out.println(json);
        String str = "{\"Login\":[{\"LoginName\":\"Admin\",\"Password\":\"123456\"}]}";
        System.out.println(str);
        api.postString1("Login "+json, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });*/


//        LoginEntity loginEntity = new LoginEntity();
//        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
//        loginBean.setLoginName("111111");
//        loginBean.setPassword("22222");
//        List<LoginEntity.LoginBean> list = new ArrayList<>();
//        list.add(loginBean);
//        loginEntity.setLogin(list);
//        System.out.println(loginEntity);
////        JsonRootBean jsonRootBean = new JsonRootBean();
////        Login login = new Login();
////        login.setLoginName("111111");
////        login.setPassword("2222");
////        List<Login> list = new ArrayList<>();
////        list.add(login);
////        jsonRootBean.setLogin(list);
//        Map<String, Object> params = new HashMap<>();
//        params.put("Login",loginEntity);
//        api.postCeShiFromWJ(params, new Callback<Response>() {
//            @Override
//            public void success(Response response, Response response2) {
//                ToastUtil.showToast(getContext(),"成功");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ToastUtil.showToast(getContext(),"失败");
//            }
//        });
//        LoginEntity loginEntity = new LoginEntity();
//        LoginEntity.LoginBean loginBean = new LoginEntity.LoginBean();
//        loginBean.setLoginName("Admin");
//        loginBean.setPassword("123456");
//        List<LoginEntity.LoginBean> list = new ArrayList<>();
//        list.add(loginBean);
//        loginEntity.setLogin(list);
//        System.out.println(loginEntity);
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        Gson gson = gsonBuilder.create();
//        String json = gson.toJson(loginEntity);
//        System.out.println(json);
//        String str = "{\"Login\":[{\"LoginName\":\"Admin\",\"Password\":\"123456\"}]}";
//        System.out.println(str);
//        RequestBody body= RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
//        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
//        api.postFlyRoute(body, new Callback<LoginEntity>() {
//            @Override
//            public void success(LoginEntity loginEntity, Response response) {
//                ToastUtil.showToast(getContext(),"成功");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ToastUtil.showToast(getContext(),"失败");
//            }
//        });
//    }
//
//    private void ceshi() {
//        ToastUtil.showToast(getContext(),"测试");
//        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
//        if (api == null) {
//            return;
//        }
//        Map<String, Object> params = new HashMap<>();
//        params.put("LoginName", "Admin");
//        params.put("Password", "123456");
//        api.postCeShiFromWJ(params, new Callback<Response>() {
//
//            @Override
//            public void success(Response response, Response response2) {
//                ToastUtil.showToast(getContext(),"成功");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                ToastUtil.showToast(getContext(),"失败");
//            }
//
//        });
    }

    /*private void onClickToNewFragment() {
        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showToast(getContext(),"头像");
                showChooser();
            }
        });
        mQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"二维码");
            }
        });
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"名字");
            }
        });
        mAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"出生年月日");
            }
        });
        mHometown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"出生地");
            }
        });
        mPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"电话号码");
            }
        });
        mSerialNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"部队编号");
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(getContext(),"退出");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.e("data---",data.toString());
                        Log.e("uri---",uri.toString());
                        try {
                            final String path = FileUtils.getPath(getContext(), uri);
                            File file = new File(path);
                            Log.e("path---",path.toString());
                            if(file.exists()){
                                Bitmap bm = BitmapFactory.decodeFile(path);
                                image.setImageBitmap(bm);

                            }
                            *//*Toast.makeText(FileChooserExampleActivity.this,
                                    "File Selected: " + path, Toast.LENGTH_LONG).show();*//*
                        } catch (Exception e) {
                            Log.e("Exception---","");
                        }
                    }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
