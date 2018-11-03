package com.cartoon.view;

import android.widget.GridView;

/**
 * @author 叶盛武
 * @version V1.0
 * @date 2013-3-30 下午2:08:55
 * @description 设置不滚动
 */
public class FullyExpandedGridView extends GridView {
    public FullyExpandedGridView(android.content.Context context,
                                 android.util.AttributeSet attrs) {
        super(context, attrs);
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
