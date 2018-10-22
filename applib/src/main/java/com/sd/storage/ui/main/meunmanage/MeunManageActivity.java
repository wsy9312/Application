package com.sd.storage.ui.main.meunmanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.AllMeunActionsCreator;
import com.sd.storage.adapter.AllMeunAdapter;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dialog.DeletSubmitDialog;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.stores.AllMeunStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Administrator on 2018-09-13.
 */

public class MeunManageActivity extends BaseSCActivity implements AllMeunAdapter.OnItemMeunClickListener, View.OnClickListener, DeletSubmitDialog.OnSureClickListener {

    @Inject
    AllMeunActionsCreator allMeunActionsCreator;
    @Inject
    AllMeunStore allMeunStore;

    private ArrayList<VageModel> vageModels = new ArrayList<>();

    //    @BindView(R.id.et_search)
    EditText et_search;

    //    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    //    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout im_back;
    ImageView im_search;
    TextView tv_edit;
    TextView tv_add_new;

    private AllMeunAdapter adapter;

    private int point;
    private DeletSubmitDialog deletSubmitDialog;
    private String vegeid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(this, R.color.mainColor_blue);
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
        allMeunActionsCreator.getAllMeunList();
    }

    public void init() {
        deletSubmitDialog = new DeletSubmitDialog();
        deletSubmitDialog.setOnSureClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_search = (ImageView) findViewById(R.id.im_search);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_add_new = (TextView) findViewById(R.id.tv_add_new);
        im_back.setOnClickListener(this);
        im_search.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_add_new.setOnClickListener(this);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        adapter = new AllMeunAdapter(this);
        adapter.setOnItemMeunClickListenerr(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_search) {//搜索页面
            //  getDisplay().startManageSearchActivity();

            //allMeunActionsCreator.addVegeOrder();
            setSearch();

        } else if (i == R.id.tv_edit) {//编辑
            getDisplay().startMansageEditActivity();

        } else if (i == R.id.tv_add_new) {//添加新菜品
            getDisplay().startAddNewVegeActivity();

        }
    }


    /**
     * 搜索
     */
    public void setSearch() {
        String vegename = et_search.getText().toString();
        allMeunActionsCreator.getSearchList(vegename);
    }

    @Override
    protected void initReturnEvent() {


        allMeunStore.toMainSubscription(AllMeunStore.AllMeunListChange.class, new Action1<AllMeunStore.AllMeunListChange>() {
            @Override
            public void call(AllMeunStore.AllMeunListChange allMeunListChange) {
                getDisplay().hideWaittingDialog();
                vageModels = allMeunStore.getVageModels();
                adapter.setAllMeunModels(vageModels);
            }
        });

        /**
         * 请求错误
         */
        allMeunStore.toMainSubscription(AllMeunStore.AllMeunListChangeError.class, new Action1<AllMeunStore.AllMeunListChangeError>() {
            @Override
            public void call(AllMeunStore.AllMeunListChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, MeunManageActivity.this);
            }
        });

        allMeunStore.toMainSubscription(AllMeunStore.AddChange.class, new Action1<AllMeunStore.AddChange>() {
            @Override
            public void call(AllMeunStore.AddChange addChange) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.add_success), MeunManageActivity.this);
            }
        });

        /**
         * 请求错误
         */
        allMeunStore.toMainSubscription(AllMeunStore.AddChangeError.class, new Action1<AllMeunStore.AddChangeError>() {
            @Override
            public void call(AllMeunStore.AddChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.have_insert), MeunManageActivity.this);
            }
        });


        /**
         * 删除
         */
        allMeunStore.toMainSubscription(AllMeunStore.DeleteChange.class, new Action1<AllMeunStore.DeleteChange>() {
            @Override
            public void call(AllMeunStore.DeleteChange addChange) {
                getDisplay().hideWaittingDialog();
                adapter.removeData(point);
            }
        });

        /**
         * 删除失败
         */
        allMeunStore.toMainSubscription(AllMeunStore.DeleteChangeError.class, new Action1<AllMeunStore.DeleteChangeError>() {
            @Override
            public void call(AllMeunStore.DeleteChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.have_insert), MeunManageActivity.this);
            }
        });


/**
 * 搜索
 */
        allMeunStore.toMainSubscription(AllMeunStore.SearhListChange.class, new Action1<AllMeunStore.SearhListChange>() {
            @Override
            public void call(AllMeunStore.SearhListChange allMeunListChange) {
                getDisplay().hideWaittingDialog();
                et_search.setText("");
                vageModels = allMeunStore.getVageModels();
                adapter.setAllMeunModels(vageModels);
            }
        });

        /**
         * 请求错误
         */
        allMeunStore.toMainSubscription(AllMeunStore.SearchListChangeError.class, new Action1<AllMeunStore.SearchListChangeError>() {
            @Override
            public void call(AllMeunStore.SearchListChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, MeunManageActivity.this);
            }
        });

    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{allMeunStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return allMeunActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meunmanage;
    }


    @Override
    public void onItemClick(int point) {
        allMeunActionsCreator.addVegeOrder(vageModels.get(point).vegeid);
        //   ToastUtils.showBaseToast(vageModels.get(point).vegename, this);
    }

    @Override
    public void onItemDeletClick(int point, String vegeid, String name) {
        this.point = point;
        this.vegeid = vegeid;

        deletSubmitDialog.show(getFragmentManager(), "");
        deletSubmitDialog.setVegeName(name + "?");
    }

    @Override
    public void onSubmitSure() {
        deletSubmitDialog.dismiss();
        allMeunActionsCreator.deleteFoodstore(vegeid);
    }
}
