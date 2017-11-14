package com.hk.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/12/6.
 * onMeasure只重写该方法，达到使ListView适应ScrollView的效果
 */

public class WrapListView extends ListView {
    public WrapListView(Context context) {
        super(context);
    }

    public WrapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode())
            setMinimumHeight(100);
    }

    public WrapListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        if (isInEditMode())
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
