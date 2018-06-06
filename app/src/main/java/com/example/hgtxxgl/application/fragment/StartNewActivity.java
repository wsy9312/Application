package com.example.hgtxxgl.application.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.LinearLayout;

import com.example.hgtxxgl.application.R;
import com.example.hgtxxgl.application.activity.ItemActivity;
import com.example.hgtxxgl.application.utils.hand.PageConfig;

/**
 * Created by zhy on 2016/10/27.
 */
public class StartNewActivity extends AppCompatActivity {
//    intent.putExtra(PageConfig.PAGE_CODE, PAGE_APPLY_PEOPLE_OUT);
    final int[] page = new int[]{PageConfig.PAGE_APPLY_PEOPLE_OUT};

    private LinearLayout[] buttons = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layou_post_flow);
        initButtons();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.layou_post_flow, container, false);
//        initButtons(view);
//        return view;
//    }

    private void initButtons() {
        buttons = new LinearLayout[]{
                (LinearLayout)findViewById(R.id.btn_gc),
                (LinearLayout)findViewById(R.id.btn_jb),
                (LinearLayout)findViewById(R.id.btn_qj),
                (LinearLayout)findViewById(R.id.btn_clbx),
                (LinearLayout)findViewById(R.id.btn_fklc),
                (LinearLayout)findViewById(R.id.btn_fybx),
                (LinearLayout)findViewById(R.id.btn_zdf),
                (LinearLayout)findViewById(R.id.btn_post_file),
                (LinearLayout)findViewById(R.id.but_hot1),
                (LinearLayout)findViewById(R.id.but_hot2),
                (LinearLayout)findViewById(R.id.but_hot3),
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
                            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
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