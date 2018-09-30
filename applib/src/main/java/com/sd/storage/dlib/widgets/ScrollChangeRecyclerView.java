package com.sd.storage.dlib.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by MrZhou on 2016/12/18.
 */

public class ScrollChangeRecyclerView extends RecyclerView {
    public ScrollChangeRecyclerView(Context context) {
        super(context);
    }

    public ScrollChangeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChangeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    OnScrolledDifferentViewChangeListener mOnScrolledDifferentViewChangeListener;

    public void setOnScrolledDifferentViewChangeListener(OnScrolledDifferentViewChangeListener mOnScrolledDifferentViewChangeListener) {
        this.mOnScrolledDifferentViewChangeListener = mOnScrolledDifferentViewChangeListener;
        this.setOnScrollListener(onScrollListener);
    }


    int total;
    boolean isChange;
    OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            total += dy;
            if (mOnScrolledDifferentViewChangeListener != null) {

                if (total > 5 && total < 400) {
                    mOnScrolledDifferentViewChangeListener.getChangeView().setAlpha((float) (0.0025D * total));
                    if (!isChange) {
                        mOnScrolledDifferentViewChangeListener.onChangeState();
                        isChange = true;
                    }
                }
                if (total <= 5) {
                    mOnScrolledDifferentViewChangeListener.getChangeView().setAlpha(0.0F);
                    if (isChange) {
                        mOnScrolledDifferentViewChangeListener.onDefaultState();
                        isChange = false;
                    }
                }

            }
        }
    };

    public interface OnScrolledDifferentViewChangeListener {
        View getChangeView();

        void onDefaultState();

        void onChangeState();
    }
}
