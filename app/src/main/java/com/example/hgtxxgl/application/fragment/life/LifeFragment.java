package com.example.hgtxxgl.application.fragment.life;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.LinearLayout;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.HandToolbar;
import com.sd.storage.ui.MenuMainActivity;

public class LifeFragment extends Fragment implements View.OnClickListener{

    final int[] page = new int[]{PageConfig.PAGE_FLOW_PRESENTFOOD,PageConfig.PAGE_FLOW_TOUPIAO,
            PageConfig.PAGE_FLOW_PAIHANG,PageConfig.PAGE_FLOW_MANAGE,
            PageConfig.PAGE_FLOW_VIDEO,PageConfig.PAGE_FLOW_BOOK,
            PageConfig.PAGE_FLOW_TINENG};

    private LinearLayout[] buttons = null;
    private HandToolbar handToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_life, container, false);
        initToolbar(view);
        initButtons(view);
        return view;
    }

    private void initToolbar(View view) {
        handToolbar = (HandToolbar) view.findViewById(R.id.life_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("学习");
        handToolbar.setTitleSize(18);
        handToolbar.setOnClickListener(this);
    }

    private void initButtons(View view) {
        buttons = new LinearLayout[]{
                view.findViewById(R.id.life_flow_presentfood),
                (LinearLayout)view.findViewById(R.id.life_flow_toupiao),
                (LinearLayout)view.findViewById(R.id.life_flow_paihang),
                (LinearLayout)view.findViewById(R.id.life_flow_manage),
                (LinearLayout)view.findViewById(R.id.life_flow_video),
                (LinearLayout)view.findViewById(R.id.life_flow_book),
                (LinearLayout)view.findViewById(R.id.life_flow_tineng),
        };
        for (int i = 0; i < buttons.length; i++) {
            if (i == 0){
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
//                                Intent intent = new Intent(getActivity(), ItemActivity.class);
//                                intent.putExtra(PageConfig.PAGE_CODE, page[finalI]);
//                                startActivity(intent);
                                Intent intent = new Intent(getActivity(), MenuMainActivity.class);
//                                intent.putExtra(PageConfig.PAGE_CODE, page[finalI]);
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

    @Override
    public void onClick(View v) {

    }
}
