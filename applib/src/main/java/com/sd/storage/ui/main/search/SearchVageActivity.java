package com.sd.storage.ui.main.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.SearchVageActionsCreator;
import com.sd.storage.adapter.VageSearchAdapter;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.stores.SearchVageStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 搜索的页面
 * Created by Administrator on 2018-09-12.
 */

public class SearchVageActivity extends BaseSCActivity implements VageSearchAdapter.OnItemSearchClickListener, View.OnClickListener {

//    @BindView(R.id.et_search)
    EditText et_search;
//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayout im_back;
    ImageView im_search;


    @Inject
    SearchVageActionsCreator searchVageActionsCreator;
    @Inject
    SearchVageStore searchVageStore;


    private VageSearchAdapter adapter;
    private ArrayList<VageModel> vageModels = new ArrayList<>();

    private int tpoint = -1;
    private int vegegive;
    private boolean isGive = false;

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

    public void init() {
        et_search = (EditText) findViewById(R.id.et_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        im_back = (LinearLayout) findViewById(R.id.im_back);
        im_search = (ImageView) findViewById(R.id.im_search);
        im_back.setOnClickListener(this);
        im_search.setOnClickListener(this);

        adapter = new VageSearchAdapter(this);
        adapter.setOnItemSearchClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


//    @OnClick({R.id.im_back, R.id.im_search})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.im_search) {
            getData();

        }
    }


    public void getData() {

        searchVageActionsCreator.getSearchList(et_search.getText().toString(), UrlManager.getUSERID());


    }

    @Override
    protected void initReturnEvent() {

        searchVageStore.toMainSubscription(SearchVageStore.VageSearchListChange.class, new Action1<SearchVageStore.VageSearchListChange>() {
            @Override
            public void call(SearchVageStore.VageSearchListChange vageModel) {
                getDisplay().hideWaittingDialog();
                vageModels = searchVageStore.getVageModels();
                adapter.setVegeModels(vageModels);
            }
        });

        /**
         * 请求错误
         */
        searchVageStore.toMainSubscription(SearchVageStore.VageSearchListChangeError.class, new Action1<SearchVageStore.VageSearchListChangeError>() {
            @Override
            public void call(SearchVageStore.VageSearchListChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, SearchVageActivity.this);
            }
        });


        /**
         * 点赞成功
         */
        searchVageStore.toMainSubscription(SearchVageStore.GiveChange.class, new Action1<SearchVageStore.GiveChange>() {
            @Override
            public void call(SearchVageStore.GiveChange giveChange) {
                getDisplay().hideWaittingDialog();
                showPage();
            }
        });

        /**
         * 请求错误
         */
        searchVageStore.toMainSubscription(SearchVageStore.GiveChangeError.class, new Action1<SearchVageStore.GiveChangeError>() {
            @Override
            public void call(SearchVageStore.GiveChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, SearchVageActivity.this);
            }
        });


    }


    /**
     * 点赞成功 刷新单个item
     */
    public void showPage() {

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(tpoint);
        if (viewHolder != null && viewHolder instanceof VageSearchAdapter.ViewHolder) {
            VageSearchAdapter.ViewHolder itemHolder = (VageSearchAdapter.ViewHolder) viewHolder;
            if (!isGive) {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_true);
                vegegive += 1;
                itemHolder.tv_give.setText(vegegive + "");
                isGive=true;
            } else {
                itemHolder.im_give.setBackgroundResource(R.drawable.give_up_false);
                vegegive -= 1;
                itemHolder.tv_give.setText(vegegive + "");
                isGive=false;
            }

        }

    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{searchVageStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return searchVageActionsCreator;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_searchvage;
    }


    @Override
    public void onItemClick(int point) {
        getDisplay().startVageDetailsActivity(vageModels.get(point).vegeid);

    }

    @Override
    public void onItemAddCommentClick(int point) {
        getDisplay().startAddCommentActivity(vageModels.get(point).vegeid);
    }

    @Override
    public void onItetSetGiveClick(int point) {
        if (tpoint != point) {
            this.tpoint = point;
            vegegive = Integer.parseInt(vageModels.get(point).vegegive);
            if (vageModels.get(point).vegestate == 0) {
                isGive = false;
            } else {
                isGive = true;
            }
        }
        searchVageActionsCreator.setGive(vageModels.get(point).vegeid, UrlManager.getUSERID());
    }
}
