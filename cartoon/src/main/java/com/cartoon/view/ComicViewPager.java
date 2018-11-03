package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;


public class ComicViewPager extends VerticalViewPager {
    private float mStartY = 0;
    private OnSwipeOutListener mSwipeOutListener;

    public interface OnSwipeOutListener {
        public void onSwipeOutAtStart();

        public void onSwipeOutAtEnd();
    }

    public ComicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComicViewPager(Context context) {
        super(context);
    }

    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        mSwipeOutListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            float diff = ev.getY() - mStartY;

            if (diff > 0 && getCurrentItem() == 0) {
                if (mSwipeOutListener != null)
                    mSwipeOutListener.onSwipeOutAtStart();
            } else if (diff < 0 && getCurrentItem() == (getAdapter().getCount() - 1)) {
                if (mSwipeOutListener != null)
                    mSwipeOutListener.onSwipeOutAtEnd();
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mStartY = ev.getY();
        }

        return super.onInterceptTouchEvent(ev);
    }
}
