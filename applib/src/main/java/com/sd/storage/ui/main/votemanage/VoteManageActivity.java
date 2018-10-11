package com.sd.storage.ui.main.votemanage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.VoteCreator;
import com.sd.storage.adapter.VegeVoteListAdapter;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.model.VageModel;
import com.sd.storage.stores.VoteStore;
import com.sd.storage.ui.base.BaseSCActivity;
import com.sd.storage.util.ToastUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * 投票列表
 * Created by Administrator on 2018-09-13.
 */

public class VoteManageActivity extends BaseSCActivity implements VegeVoteListAdapter.OnVoteItenClickListener, View.OnClickListener {

//    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.tv_major)
    TextView tv_major;
//    @BindView(R.id.tv_minor)
    TextView tv_minor;

    @Inject
    VoteCreator voteCreator;
    @Inject
    VoteStore voteStore;

//    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

//    @BindView(R.id.SwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout im_back;

    private VegeVoteListAdapter adapter;

    private ArrayList<VageModel> vageModels = new ArrayList<>();
    private String heatid = "0";

    private int tpoint = -1;
    private int allVote = 0;
    private boolean isVote = false;

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

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_major = (TextView) findViewById(R.id.tv_major);
        tv_minor = (TextView) findViewById(R.id.tv_minor);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        im_back = (LinearLayout) findViewById(R.id.im_back);

        im_back.setOnClickListener(this);
        tv_major.setOnClickListener(this);
        tv_minor.setOnClickListener(this);


        tv_title.setText(R.string.menu_vote);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        adapter = new VegeVoteListAdapter(this);
        adapter.setOnItemVoteClickListenerr(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }

//    @OnClick({R.id.im_back, R.id.tv_major, R.id.tv_minor})
    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.im_back) {
            finish();

        } else if (i == R.id.tv_major) {
            tv_major.setTextColor(getResources().getColor(R.color.c8cc35a));
            tv_minor.setTextColor(getResources().getColor(R.color.c9f9f9f));
            heatid = "0";
            getData();

        } else if (i == R.id.tv_minor) {
            tv_major.setTextColor(getResources().getColor(R.color.c9f9f9f));
            tv_minor.setTextColor(getResources().getColor(R.color.c8cc35a));
            heatid = "1";
            getData();

        }
    }

    public void getData() {
        voteCreator.getVoteListList(UrlManager.getUSERID(), heatid);
    }

    @Override
    protected void initReturnEvent() {

        voteStore.toMainSubscription(VoteStore.VegeVoteListChange.class, new Action1<VoteStore.VegeVoteListChange>() {
            @Override
            public void call(VoteStore.VegeVoteListChange voteListChange) {
                getDisplay().hideWaittingDialog();
                vageModels = voteStore.getVageModels();
                adapter.setAllMeunModels(vageModels);
                tpoint=-1;
            }
        });

        /**
         * 请求错误
         */
        voteStore.toMainSubscription(VoteStore.VegeVoteListChangeeError.class, new Action1<VoteStore.VegeVoteListChangeeError>() {
            @Override
            public void call(VoteStore.VegeVoteListChangeeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, VoteManageActivity.this);
            }
        });

        /**
         * 投票成功
         */
        voteStore.toMainSubscription(VoteStore.VoteChange.class, new Action1<VoteStore.VoteChange>() {
            @Override
            public void call(VoteStore.VoteChange voteChange) {
                getDisplay().hideWaittingDialog();
                setItem();

            }
        });


        /**
         * 请求错误
         */
        voteStore.toMainSubscription(VoteStore.VoteChangeError.class, new Action1<VoteStore.VoteChangeError>() {
            @Override
            public void call(VoteStore.VoteChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(getString(R.string.finish_vote), VoteManageActivity.this);
            }
        });

    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{voteStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {

        return voteCreator;
    }

    @Override
    protected int getLayoutId() {

        return R.layout.activity_votemanage;

    }

    /**
     * 点击进详情
     *
     * @param point
     */
    @Override
    public void onItemClick(int point) {

        getDisplay().startVageDetailsActivity(vageModels.get(point).vegeid);
    }

    /**
     * 发表评论
     *
     * @param point
     */
    @Override
    public void onSetMessageClick(int point) {
        getDisplay().startAddCommentActivity(vageModels.get(point).vegeid);
    }

    /**
     * 设置点赞
     *
     * @param point
     */
    @Override
    public void onSetVoteClick(int point) {
        if (tpoint != point) {
            this.tpoint = point;
            this.allVote = Integer.parseInt(vageModels.get(point).vegevote);
            if ("0".equals(vageModels.get(point).vegevotestatus)) {
                isVote = false;
            } else {
                isVote = true;
            }
        }
        voteCreator.setVote(vageModels.get(point).vegeid, UrlManager.getUSERID());
        // ToastUtils.showBaseToast("投票",this);
    }


    public void setItem() {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(tpoint);

        if (viewHolder != null && viewHolder instanceof VegeVoteListAdapter.VoteViewHolder) {
            VegeVoteListAdapter.VoteViewHolder itemHolder = (VegeVoteListAdapter.VoteViewHolder) viewHolder;

            if (!isVote) {
                itemHolder.im_give.setImageDrawable(getResources().getDrawable(R.drawable.vote_true));
                allVote += 1;
                itemHolder.tv_give.setText(allVote + "");
                isVote = true;
            } else {
                itemHolder.im_give.setImageDrawable(getResources().getDrawable(R.drawable.vote_false));
                allVote -= 1;
                itemHolder.tv_give.setText(allVote + "");
                isVote = false;
            }

        }
    }
}
