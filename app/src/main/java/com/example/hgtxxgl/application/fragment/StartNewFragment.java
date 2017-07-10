package com.example.hgtxxgl.application.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.LinearLayout;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.utils.PageConfig;


//原本的启动新流程,现在的个人中心
public class StartNewFragment extends Fragment {

    final int[] page = new int[]{PageConfig.PAGE_APPLY_BLEAVE, PageConfig.PAGE_APPLY_EXTRAWORK
            , PageConfig.PAGE_APPLY_REST, PageConfig.PAGE_APPLY_TRAVEL_OFFER,
            PageConfig.PAGE_APPLY_PAYMENT_FLOW, PageConfig.PAGE_APPLY_EXPENSE_OFFER, PageConfig.PAGE_APPLY_ENTERTAINMENT_EXPENSE,
            PageConfig.PAGE_APPLY_POST_FILE,PageConfig.PAGE_APPLY_BLEAVE,PageConfig.PAGE_APPLY_TRAVEL_OFFER,PageConfig.PAGE_APPLY_EXPENSE_OFFER};

    private LinearLayout[] buttons = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layou_post_flow, container, false);
        initButtons(view);
        return view;
    }

    private void initButtons(View v) {

//        NetworkApi api = RetrofitUtils.createApi(NetworkApi.class);
//        api.evaluateTasks(userId, status, new Callback<HomeFragmentAllTask>() {
//
//            @Override
//            public void success(HomeFragmentAllTask homeFragmentAllTask, Response response) {
//
//
//            }
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//
//        });



        buttons = new LinearLayout[]{
                (LinearLayout) v.findViewById(R.id.btn_gc),
                (LinearLayout) v.findViewById(R.id.btn_jb),
                (LinearLayout) v.findViewById(R.id.btn_qj),
                (LinearLayout) v.findViewById(R.id.btn_clbx),
                (LinearLayout) v.findViewById(R.id.btn_fklc),
                (LinearLayout) v.findViewById(R.id.btn_fybx),
                (LinearLayout) v.findViewById(R.id.btn_zdf),
                (LinearLayout) v.findViewById(R.id.btn_post_file),
                (LinearLayout) v.findViewById(R.id.but_hot1),
                (LinearLayout) v.findViewById(R.id.but_hot2),
                (LinearLayout) v.findViewById(R.id.but_hot3),
        };
        for (int i = 0; i < buttons.length; i++) {
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.animate().scaleX(0.9f).scaleY(0.9f).setInterpolator(new CycleInterpolator(1))
                            .setDuration(300).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Intent intent = new Intent(getActivity(), ItemActivity.class);
                            intent.putExtra(PageConfig.PAGE_CODE, page[finalI]);
                            startActivity(intent);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
                }
            });
        }
    }
}