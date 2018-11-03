package com.cartoon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jinbangzhu on 6/6/14.
 */
public class CustomViewPager extends ViewPager {

    int width;
    private boolean canTouchScroll = false;

    public CustomViewPager(Context context){
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        width = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setCanTouchScroll(boolean canTouchScroll) {
        this.canTouchScroll = canTouchScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
//        Log.d("", "onInterceptTouchEvent getAction= " + event.getAction());
//        Log.d("", "onInterceptTouchEvent getX = " + event.getX());
//        Log.d("", "onInterceptTouchEvent width = " + width);

        if ((event.getX() >= width * 0.95 || event.getX() <= width * 0.05)) {
            return true;
        }
        if (event.getPointerCount() > 2)
            return true;

        if (canTouchScroll) {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d("", "rawxxxxx"+ev.getRawX());
//        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//        } else {
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
//        }
    }
}
