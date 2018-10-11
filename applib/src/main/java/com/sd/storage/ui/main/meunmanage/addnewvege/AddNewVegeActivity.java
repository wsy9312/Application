package com.sd.storage.ui.main.meunmanage.addnewvege;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.AddMeunActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.stores.AddMeunStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.ui.main.picture.PictureFragmentDialogV2;
import com.sd.storage.util.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 新增菜品
 * Created by Administrator on 2018-09-13.
 */

public class AddNewVegeActivity extends BaseSCActivity implements PictureFragmentDialogV2.OnResultPicturPathListener, View.OnClickListener {

//    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.tv_showtype)
    TextView tv_showtype;
//    @BindView(R.id.im_select_type)
    ImageView im_select_type;

//    @BindView(R.id.im_get)
    ImageView im_get;

//    @BindView(R.id.et_name)
    EditText et_name;

//    @BindView(R.id.et_desc)
    EditText et_desc;

    LinearLayout im_back;
    ImageView im_set;
    TextView tv_submit;


    @Inject
    AddMeunActionsCreator addMeunActionsCreator;
    @Inject
    AddMeunStore addMeunStore;


    private boolean isMajor = true;
    private PictureFragmentDialogV2 pictureFragmentDialogV2;


    private String heatid = "0";


    private File file;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        StorageApplication.getApplication().getAppComponent().inject(this);
        init();
    }


    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_showtype = (TextView) findViewById(R.id.tv_showtype);
        im_select_type = (ImageView) findViewById(R.id.im_select_type);
        im_get = (ImageView) findViewById(R.id.im_get);
        et_name = (EditText) findViewById(R.id.et_name);
        et_desc = (EditText) findViewById(R.id.et_desc);

        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_set = (ImageView) findViewById(R.id.im_set);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        im_back.setOnClickListener(this);
        im_set.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        im_select_type.setOnClickListener(this);

        tv_title.setText(R.string.add_new_vege);
        pictureFragmentDialogV2 = PictureFragmentDialogV2.getInstance(true, 300, 300, 1, 1);
    }


//    @OnClick({R.id.im_back, R.id.im_select_type, R.id.im_set, R.id.tv_submit})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_select_type) {
            if (isMajor) {
                isMajor = false;
                tv_showtype.setText(R.string.vege_minor);
                //   im_select_type.se(getResources().getDrawable(R.drawable.close));
                im_select_type.setImageDrawable(getResources().getDrawable(R.drawable.close));
            } else {
                isMajor = true;
                tv_showtype.setText(R.string.vege_major);
                im_select_type.setImageDrawable(getResources().getDrawable(R.drawable.open));
            }

        } else if (i == R.id.im_set) {
            pictureFragmentDialogV2.show(getSupportFragmentManager(), "");

        } else if (i == R.id.tv_submit) {//提交
            setData();

        }
    }


    @Override
    protected void initReturnEvent() {
        /**
         * 请求错误
         */
        addMeunStore.toMainSubscription(AddMeunStore.AddChange.class, new Action1<AddMeunStore.AddChange>() {
            @Override
            public void call(AddMeunStore.AddChange addChange) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(R.string.success_vege, AddNewVegeActivity.this);
            }
        });
        addMeunStore.toMainSubscription(AddMeunStore.AddChangeError.class, new Action1<AddMeunStore.AddChangeError>() {
            @Override
            public void call(AddMeunStore.AddChangeError addChangeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(addChangeError.msge, AddNewVegeActivity.this);
            }
        });
    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{addMeunStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return addMeunActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_newvege;
    }

    @Override
    public void onResultPicturePathClipBitmap(DialogFragment dialogFragment, String picturePath, Bitmap bitmap) {
        pictureFragmentDialogV2.dismiss();
        im_get.setImageBitmap(bitmap);
        file = saveBitmapFile(bitmap, picturePath);
    }


    public void setData() {
        String name = et_name.getText().toString();

        if (null == name || name.length() == 0) {
            ToastUtils.showBaseToast(getResources().getString(R.string.please_input_vegename), this);
            return;
        }
        String vegedesc = et_desc.getText().toString();

        if (null == vegedesc || vegedesc.length() == 0) {
            ToastUtils.showBaseToast(getResources().getString(R.string.please_input_vegedese), this);
            return;
        }
        if (isMajor) {
            heatid = "0";
        } else {
            heatid = "1";
        }
        if (null == file) {
            ToastUtils.showBaseToast(getResources().getString(R.string.please_select_picture), this);
            return;
        }
        addMeunActionsCreator.uploadImage(name, vegedesc, heatid, file);
    }


    public static File saveBitmapFile(Bitmap bitmap, String filepath) {
        File file = new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
