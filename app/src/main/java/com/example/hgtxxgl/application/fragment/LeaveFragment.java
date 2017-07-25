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
import com.example.hgtxxgl.application.utils.hand.PageConfig;

/**
 * Created by HGTXxgl on 2017/7/12.
 */
public class LeaveFragment extends Fragment{

    final int[] page = new int[]{PageConfig.PAGE_LEAVE_APPROVE,PageConfig.PAGE_LEAVE_DETAIL,PageConfig.PAGE_LEAVE_APPLY};

    private LinearLayout[] buttons = null;
    public LeaveFragment() {

    }
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
        buttons = new LinearLayout[]{
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
