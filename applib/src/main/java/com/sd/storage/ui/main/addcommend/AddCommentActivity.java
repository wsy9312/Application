package com.sd.storage.ui.main.addcommend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.AddCommentActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.stores.AddCommentStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018-09-10.
 */

public class AddCommentActivity extends BaseSCActivity implements View.OnClickListener {
//    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.et_comreview)
    EditText et_comreview;
//    @BindView(R.id.tv_count)
    TextView tv_count;
    LinearLayout im_back;
    TextView tv_submit;

    @Inject
    AddCommentActionsCreator addCommentActionsCreator;
    @Inject
    AddCommentStore addCommentStore;

    private String vegeid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        StorageApplication.getApplication().getAppComponent().inject(this);
        init();


    }


    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_comreview = (EditText) findViewById(R.id.et_comreview);
        tv_count = (TextView) findViewById(R.id.tv_count);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        im_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

        vegeid = getIntent().getStringExtra("vegeid");
        tv_title.setText(R.string.addcomment);
        et_comreview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //  tv_count.setText(charSequence.toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 300) {
                    s.delete(300, s.length());
                }
                int num = s.length();
                tv_count.setText(String.valueOf(num) + "/" + "300");
            }
        });

    }

//    @OnClick({R.id.im_back, R.id.tv_submit})
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.tv_submit) {
            setData();

        }
    }

    public void setData() {
        // http://192.168.1.111:8080/Canteen/coment/addComent.do?vegeid=1&comreview=nihao&username=1

        String s = et_comreview.getText().toString();
        if (null == s || s.length() == 0) {
            ToastUtils.showBaseToast(getString(R.string.please_input_your_commit), this);
            return;
        }
        addCommentActionsCreator.addComment(vegeid, s, UrlManager.getUSENAME());

    }

    @Override
    protected void initReturnEvent() {
        addCommentStore.toMainSubscription(AddCommentStore.AddCommentChange.class, new Action1<AddCommentStore.AddCommentChange>() {
            @Override
            public void call(AddCommentStore.AddCommentChange addCommentChange) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.add_comment_success), AddCommentActivity.this);
                et_comreview.setText("");
            }
        });

        /**
         * 请求错误
         */
        addCommentStore.toMainSubscription(AddCommentStore.AddCommentChangeError.class, new Action1<AddCommentStore.AddCommentChangeError>() {
            @Override
            public void call(AddCommentStore.AddCommentChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, AddCommentActivity.this);
            }
        });
    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{addCommentStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return addCommentActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addcomment;
    }
}
