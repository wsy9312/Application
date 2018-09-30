package com.sd.storage.ui.main.meunmanage.addweekmeun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sd.storage.R;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.AllMeunActionsCreator;
import com.sd.storage.adapter.StoreAddMeunAdapter;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.stores.AllMeunStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

;

/**
 * Created by Administrator on 2018-09-13.
 */

public class AddWeekMeunActivity extends BaseSCActivity implements StoreAddMeunAdapter.OnItemMeunClickListener, View.OnClickListener {

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

    private StoreAddMeunAdapter adapter;


    private String dayid;
    private String typeid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        et_search = (EditText) findViewById(R.id.et_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_search = (ImageView) findViewById(R.id.im_search);

        im_back.setOnClickListener(this);
        im_search.setOnClickListener(this);

        Intent intent = getIntent();
        dayid = intent.getStringExtra("dayid");
        typeid = intent.getStringExtra("typeid");

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        adapter = new StoreAddMeunAdapter(this);
        adapter.setOnItemMeunClickListenerr(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    @OnClick({R.id.im_back, R.id.im_search})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_search) {//搜索页面
            setSearch();

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
                ToastUtils.showBaseToast(changeError.msge, AddWeekMeunActivity.this);
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
                ToastUtils.showBaseToast(changeError.msge, AddWeekMeunActivity.this);
            }
        });





        allMeunStore.toMainSubscription(AllMeunStore.AddVegeChange.class, new Action1<AllMeunStore.AddVegeChange>() {
            @Override
            public void call(AllMeunStore.AddVegeChange addVegeChange) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.add_success), AddWeekMeunActivity.this);
            }
        });

        /**
         * 请求错误
         */
        allMeunStore.toMainSubscription(AllMeunStore.AddVegeChangeError.class, new Action1<AllMeunStore.AddVegeChangeError>() {
            @Override
            public void call(AllMeunStore.AddVegeChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, AddWeekMeunActivity.this);
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
        return R.layout.activity_addweekmeun;
    }


    @Override
    public void onItemClick(int point) {
        VageModel vageModel = vageModels.get(point);
        //添加到本周菜谱
        // public void addVegeWeek(String vegeid,String vegename,String vegedesc,String vegeimg,String heatid,String dayid,String typeid
        allMeunActionsCreator.addVegeWeek(vageModel.vegeid, vageModel.vegename, vageModel.vegedesc, vageModel.vegeimg, vageModel.heatid, dayid, typeid);


    }

}
