package com.cartoon.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import cn.com.xuanjiezhimen.R;

/**
 * Created by shidu on 17/4/17.
 */

public class VDHLayoutLite extends FrameLayout {
    private ViewDragHelper mDragger;
    private View mDragView;

    public VDHLayoutLite(Context context) {
        this(context, (AttributeSet) null);
    }

    public VDHLayoutLite(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VDHLayoutLite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initViews();
    }

    private void initViews() {
        this.mDragger = ViewDragHelper.create(this, 1.0F, new ViewDragHelper.Callback() {
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView;
            }

            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int leftBound = getPaddingLeft();
                int rightBound = getWidth() - child.getWidth() - leftBound;
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - child.getHeight() - topBound;
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            public void onViewReleased(View child, float xvel, float yvel) {
                moveToEdge(child);
            }

            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            }

            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

        });
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.mDragger.shouldInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            this.mDragger.processTouchEvent(event);
        } catch (IllegalArgumentException var3) {
        }

        return false;
    }

    public void computeScroll() {
        if (this.mDragger.continueSettling(true)) {
            this.postInvalidateOnAnimation();
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    private void moveToEdge(final View v) {
        int width = this.getMeasuredWidth();
        int centerX = (v.getLeft() + v.getRight()) / 2;
        float toXDelta;
        if (centerX <= width / 2) {
            toXDelta = (float) (-v.getLeft()) + v.getPaddingLeft();
        } else {
            toXDelta = (float) (width - v.getRight()) - v.getPaddingRight();
        }

        final int offsetX = (int) toXDelta;
        TranslateAnimation animation = new TranslateAnimation(0.0F, toXDelta, 0.0F, 0.0F);
        animation.setDuration(200L);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                v.clearAnimation();
                v.layout(v.getLeft() + offsetX, v.getTop(), v.getRight() + offsetX, v.getBottom());
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        v.startAnimation(animation);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDragView = this.findViewById(R.id.btn_logo_float);
    }

    public View getDragView() {
        return this.mDragView;
    }
}
