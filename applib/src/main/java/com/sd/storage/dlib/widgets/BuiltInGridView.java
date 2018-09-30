package com.sd.storage.dlib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class BuiltInGridView extends GridView {

	public BuiltInGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public BuiltInGridView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
	}

	public BuiltInGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
