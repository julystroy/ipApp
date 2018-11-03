package com.cartoon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cartoon.listener.SeekBarImagePageCallback;

/**
 * Created by David on 16/6/13.
 */
public class HackyViewPager extends ViewPager {

    private boolean isLocked;
    private SeekBarImagePageCallback listener;

    public HackyViewPager(Context context) {
        super(context);
        this.isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public void setListener(SeekBarImagePageCallback listener) {
        this.listener = listener;
    }
}
