package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by shidu on 17/1/9.
 * 默认全部展开的ListView
 */

public class FullyExpandedListView extends ListView {
    public FullyExpandedListView(Context context) {
        super(context);
    }

    public FullyExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullyExpandedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
