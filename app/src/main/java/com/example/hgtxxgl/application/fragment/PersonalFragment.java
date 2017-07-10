package com.example.hgtxxgl.application.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.hgtxxgl.application.QrCode.QRCodeActivity;
import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.LoginActivity;
import com.example.hgtxxgl.application.network.NetworkApi;
import com.example.hgtxxgl.application.network.RetrofitUtils;
import com.example.hgtxxgl.application.utils.ToastUtil;
import java.util.HashMap;
import java.util.Map;
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
                ceshi();
                break;
            case R.id.rl_name:
                ceshijson();
                break;
            case R.id.rl_qrcode:
                skipToQR();
                break;
        }
    }

    private void skipToQR() {
        Intent intent = new Intent(getContext(),QRCodeActivity.class);
        startActivity(intent);
    }

    private void ceshijson() {
        ToastUtil.showToast(getContext(),"测试json");
        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
        if (api == null){
            return;
        }
        String json = "";
        api.loginFromWJ(json, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                ToastUtil.showToast(getContext(),"response = "+response+"response2 = "+response2);
            }

            @Override
            public void failure(RetrofitError error) {
                ToastUtil.showToast(getContext(),"失败");
            }
        });

    }

    private void logOut() {
        ToastUtil.showToast(getContext(),"退出");
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void ceshi() {
        ToastUtil.showToast(getContext(),"测试");
        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
        if (api == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("LoginName", "23423424234");
        params.put("Password", "123456");
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
