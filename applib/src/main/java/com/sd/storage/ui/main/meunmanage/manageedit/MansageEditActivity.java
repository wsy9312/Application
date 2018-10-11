package com.sd.storage.ui.main.meunmanage.manageedit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.EditMeunListCreator;
import com.sd.storage.adapter.SelectMeunAdapter;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.stores.EditMeunStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 编辑
 * Created by Administrator on 2018-09-13.
 */

public class MansageEditActivity extends BaseSCActivity implements SelectMeunAdapter.OnItemMeunSelectClickListener, View.OnClickListener {
    @Inject
    EditMeunListCreator editMeunListCreator;
    @Inject
    EditMeunStore editMeunStore;

//    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout im_back;
    TextView tv_clear;

    private ArrayList<VageModel> vageModels = new ArrayList<>();
    private SelectMeunAdapter adapter;

    private int point;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this,R.color.mainColor_blue);
        StorageApplication.getApplication().getAppComponent().inject(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    public void getData() {
        getDisplay().showWaittingDialog();
        editMeunListCreator.getVegeMeunList();
    }

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        tv_clear = (TextView) findViewById(R.id.tv_clear);

        im_back.setOnClickListener(this);
        tv_clear.setOnClickListener(this);

        tv_title.setText(R.string.str_edit);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        adapter = new SelectMeunAdapter(this);
        adapter.setOnItemMeunSelectClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


//    @OnClick({R.id.im_back, R.id.tv_clear})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.tv_clear) {//清空


        }
    }

    @Override
    protected void initReturnEvent() {

        editMeunStore.toMainSubscription(EditMeunStore.SelectMeunListChange.class, new Action1<EditMeunStore.SelectMeunListChange>() {
            @Override
            public void call(EditMeunStore.SelectMeunListChange selectMeunListChange) {
                getDisplay().hideWaittingDialog();
                vageModels = editMeunStore.getVageModels();
                adapter.setAllMeunModels(vageModels);
            }
        });

        /**
         * 请求错误
         */
        editMeunStore.toMainSubscription(EditMeunStore.SelectMeunListChangeError.class, new Action1<EditMeunStore.SelectMeunListChangeError>() {
            @Override
            public void call(EditMeunStore.SelectMeunListChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, MansageEditActivity.this);
            }
        });


        editMeunStore.toMainSubscription(EditMeunStore.DeleteChange.class, new Action1<EditMeunStore.DeleteChange>() {
            @Override
            public void call(EditMeunStore.DeleteChange deleteChange) {
                getDisplay().hideWaittingDialog();
                //删除成功
                adapter.deleteItem(point);

            }
        });

        /**
         * 请求错误
         */
        editMeunStore.toMainSubscription(EditMeunStore.DeleteChangeError.class, new Action1<EditMeunStore.DeleteChangeError>() {
            @Override
            public void call(EditMeunStore.DeleteChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, MansageEditActivity.this);
            }
        });

        editMeunStore.toMainSubscription(EditMeunStore.AddChange.class, new Action1<EditMeunStore.AddChange>() {
            @Override
            public void call(EditMeunStore.AddChange deleteChange) {
                getDisplay().hideWaittingDialog();
                //添加成功
                deletItem();
               // adapter.deleteItem(point);

            }
        });

        /**
         * 请求错误
         */
        editMeunStore.toMainSubscription(EditMeunStore.AddChangeError.class, new Action1<EditMeunStore.AddChangeError>() {
            @Override
            public void call(EditMeunStore.AddChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getResources().getString(R.string.have_insert), MansageEditActivity.this);
            }
        });

    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{editMeunStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return editMeunListCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manageeite;
    }

    @Override
    public void onAddClick(int point) {
        this.point = point;
        //添加
        editMeunListCreator.addVegeOrder(vageModels.get(point).vegeid);
        //  ToastUtils.showBaseToast(vageModels.get(point).vegename,this);
    }

    @Override
    public void onDeletClick(int point) {
        this.point = point;
        deletItem();
    }

    public void deletItem() {
        //删除
        editMeunListCreator.deleteVegeOrder(vageModels.get(point).vegeid);
    }
}
