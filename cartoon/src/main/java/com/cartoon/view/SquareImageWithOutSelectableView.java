package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jinbangzhu on 1/8/16.
 */
public class SquareImageWithOutSelectableView extends ImageView {
    public SquareImageWithOutSelectableView(Context context) {
        super(context);
    }

    public SquareImageWithOutSelectableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageWithOutSelectableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
