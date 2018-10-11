package com.sd.storage.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sd.storage.R;
import com.sd.storage.UrlManager;
import com.sd.storage.actions.ActionsCreator;
import com.sd.storage.actions.TimeActionsCreator;
import com.sd.storage.add.StatusBarColorUtils;
import com.sd.storage.app.StorageApplication;
import com.sd.storage.dlib.store.Store;
import com.sd.storage.stores.TimeStore;
import com.sd.storage.ui.base.BaseSCFragment;
import com.sd.storage.ui.main.meunorder.MeunOrderActivity;
import com.sd.storage.ui.main.settime.SetMainActivity;
import com.sd.storage.ui.main.votemanage.VoteManageActivity;
import com.sd.storage.ui.main.weekmeun.WeekMenuActivity;
import com.sd.storage.util.ToastUtils;

import javax.inject.Inject;

import rx.functions.Action1;

public class MenuMainFragment extends BaseSCFragment implements View.OnClickListener{

    //    @BindView(R.id.lin_weekMenu)
    LinearLayout lin_weekMenu;
    //    @BindView(R.id.lin_meunVote)
    LinearLayout lin_meunVote;
    //    @BindView(R.id.lin_meunOrder)
    LinearLayout lin_meunOrder;
    //    @BindView(R.id.lin_meunMannage)
    LinearLayout lin_meunMannage;

    @Inject
    TimeActionsCreator timeActionsCreator;
    @Inject
    TimeStore timeStore;

    private String userid, username, level;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarColorUtils.setWindowStatusBarColor(getActivity(),R.color.mainColor_blue);
        StorageApplication.getApplication().getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_life1, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        lin_weekMenu = (LinearLayout) view.findViewById(R.id.life_flow_presentfood);
        lin_meunVote = (LinearLayout) view.findViewById(R.id.life_flow_toupiao);
        lin_meunOrder = (LinearLayout) view.findViewById(R.id.life_flow_paihang);
        lin_meunMannage = (LinearLayout) view.findViewById(R.id.life_flow_manage);
        lin_weekMenu.setOnClickListener(this);
        lin_meunVote.setOnClickListener(this);
        lin_meunOrder.setOnClickListener(this);
        lin_meunMannage.setOnClickListener(this);

        level="1";
        username="ren";
        userid="2";
        UrlManager.setLEVEl(level);
        UrlManager.setUSENAME(username);
        UrlManager.setUSERID(userid);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.life_flow_presentfood) {//本周菜谱
//            getDisplay().startWeekMenuActivity();
            Intent intent = new Intent(getActivity(), WeekMenuActivity.class);
            startActivity(intent);
        } else if (i == R.id.life_flow_toupiao) {//菜谱投票
            timeActionsCreator.voteTime();

        } else if (i == R.id.life_flow_paihang) {//菜谱排行
//            getDisplay().startMeunOrderActivity();
            Intent intent = new Intent(getActivity(),MeunOrderActivity.class);
            startActivity(intent);
        } else if (i == R.id.life_flow_manage) {//菜谱管理
            if ("1".equals(UrlManager.getLEVEl())) {
//                getDisplay().startSetMainActivity();
                Intent intent = new Intent(getActivity(), SetMainActivity.class);
                startActivity(intent);
            } else {
                ToastUtils.showBaseToast(R.string.you_not_have_level, getContext());
            }
        }
    }

    @Override
    protected void initReturnEvent() {
        timeStore.toMainSubscription(TimeStore.VoteTimeChange.class, new Action1<TimeStore.VoteTimeChange>() {
            @Override
            public void call(TimeStore.VoteTimeChange voteTimeChange) {
//                getDisplay().hideWaittingDialog();
                Intent intent = new Intent(getActivity(), VoteManageActivity.class);
                startActivity(intent);
//                getDisplay().startVoteManageActivity();
            }
        });

        /**
         * 请求错误
         */
        timeStore.toMainSubscription(TimeStore.VoteTimeChangeError.class, new Action1<TimeStore.VoteTimeChangeError>() {
            @Override
            public void call(TimeStore.VoteTimeChangeError changeError) {
                getDisplay().hideWaittingDialog();
                ToastUtils.showBaseToast(changeError.msge, getContext());
            }
        });
    }

    @Override
    public Store[] getStoreArray() {
        return new Store[]{timeStore};
    }

    @Override
    public ActionsCreator getActionsCreator() {
        return timeActionsCreator;
    }
}
