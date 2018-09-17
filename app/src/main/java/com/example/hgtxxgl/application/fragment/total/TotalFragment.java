package com.example.hgtxxgl.application.fragment.total;

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
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.utils.hand.PageConfig;
import com.example.hgtxxgl.application.view.HandToolbar;

public class TotalFragment extends Fragment {

    final int[] page = new int[]{PageConfig.PAGE_APPLY_PEOPLE_SHI,PageConfig.PAGE_APPLY_PEOPLE_BING,
            PageConfig.PAGE_APPLY_PEOPLE_XIU,PageConfig.PAGE_APPLY_PEOPLE_WAI,
            PageConfig.PAGE_APPLY_CAR,PageConfig.PAGE_APPLY_PEOPLE_WU,
            PageConfig.PAGE_COMMISSION_PEOPLE_TOTAL,PageConfig.PAGE_LAUNCH_PEOPLE_LIST,
            PageConfig.PAGE_CAR_APPROVE,PageConfig.PAGE_CAR_DETAIL,
            PageConfig.PAGE_XINGZHENG,PageConfig.PAGE_YIJIAN,PageConfig.PAGE_CHART};

    private LinearLayout[] buttons = null;
    private HandToolbar handToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_total, container, false);
        initToolbar(view);
        initButtons(view);
        return view;
    }

    private void initToolbar(View view) {
        handToolbar = (HandToolbar) view.findViewById(R.id.total_handtoolbar);
        handToolbar.setDisplayHomeAsUpEnabled(false, getActivity());
        handToolbar.setBackHome(false,0);
        handToolbar.setTitle("工作");
        handToolbar.setTitleSize(18);
    }

    private void initButtons(View view) {
        buttons = new LinearLayout[]{
                (LinearLayout)view.findViewById(R.id.flow_apply_shijia),
                (LinearLayout)view.findViewById(R.id.flow_apply_bingjia),
                (LinearLayout)view.findViewById(R.id.flow_apply_xiujia),
                (LinearLayout)view.findViewById(R.id.flow_apply_waichu),
                (LinearLayout)view.findViewById(R.id.flow_apply_cheliang),
                (LinearLayout)view.findViewById(R.id.flow_apply_wuzi),
                (LinearLayout)view.findViewById(R.id.flow_approve_people),
                (LinearLayout)view.findViewById(R.id.flow_launch_people),
                (LinearLayout)view.findViewById(R.id.flow_approve_car),
                (LinearLayout)view.findViewById(R.id.flow_launch_car),
                (LinearLayout)view.findViewById(R.id.flow_xingzheng_manage),
                (LinearLayout)view.findViewById(R.id.flow_ideas_box),
                (LinearLayout)view.findViewById(R.id.flow_unit_chart),
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
                            Intent intent = new Intent(getContext(), ItemActivity.class);
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
