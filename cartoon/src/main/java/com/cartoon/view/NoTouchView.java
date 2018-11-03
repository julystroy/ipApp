package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cc on 17-8-29.
 */
public class NoTouchView extends View {
    public NoTouchView(Context context) {
        super(context);
    }
    public NoTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
