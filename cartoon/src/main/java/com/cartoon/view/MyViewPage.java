package com.cartoon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by cc on 17-1-18.
 */
public class MyViewPage extends ViewPager {

//01-18 08:20:17.518 30356-30356/cn.com.xuanjiezhimen I/Choreographer: Skipped 32 frames!  The application may be doing too much work on its main thread.
    public MyViewPage(Context context) {
        super(context);
    }

    public MyViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

            return false;

    }


}
