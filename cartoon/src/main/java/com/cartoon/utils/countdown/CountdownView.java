package com.cartoon.utils.countdown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import cn.com.xuanjiezhimen.R;


public class CountdownView extends View {
    private BaseCountdown mCountdown;
    private CustomCountDownTimer mCustomCountDownTimer;
    private OnCountdownEndListener mOnCountdownEndListener;
    private OnCountdownIntervalListener mOnCountdownIntervalListener;

    private boolean isHideTimeBackground;
    private long mPreviousIntervalCallbackTime;
    private long mInterval;
    private long mRemainTime;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountdownView);
        isHideTimeBackground = ta.getBoolean(R.styleable.CountdownView_isHideTimeBackground, true);

        mCountdown = isHideTimeBackground ? new BaseCountdown() : new BackgroundCountdown();
        mCountdown.initStyleAttr(context,ta);
        ta.recycle();

        mCountdown.initialize();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int contentAllWidth = mCountdown.getAllContentWidth();
        int contentAllHeight = mCountdown.getAllContentHeight();
        int viewWidth = measureSize(1, contentAllWidth, widthMeasureSpec);
        int viewHeight = measureSize(2, contentAllHeight, heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);

        mCountdown.onMeasure(this, viewWidth, viewHeight, contentAllWidth, contentAllHeight);
    }

    /**
     * measure view Size
     * @param specType    1 width 2 height
     * @param contentSize all content view size
     * @param measureSpec spec
     * @return measureSize
     */
    private int measureSize(int specType, int contentSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(contentSize, specSize);
        } else {
            result = contentSize;

            if (specType == 1) {
                // width
                result += (getPaddingLeft() + getPaddingRight());
            } else {
                // height
                result += (getPaddingTop() + getPaddingBottom());
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCountdown.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    private void reLayout() {
        mCountdown.reLayout();
        requestLayout();
    }

    /**
     * start countdown
     * @param millisecond millisecond
     */
    public void start(long millisecond) {
        if (millisecond <= 0) return;

        mPreviousIntervalCallbackTime = 0;

        if (null != mCustomCountDownTimer) {
            mCustomCountDownTimer.stop();
            mCustomCountDownTimer = null;
        }

        long countDownInterval;
        if (mCountdown.isShowMillisecond) {
            countDownInterval = 10;
            updateShow(millisecond);
        } else {
            countDownInterval = 1000;
        }

        mCustomCountDownTimer = new CustomCountDownTimer(millisecond, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateShow(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // countdown end
                allShowZero();
                // callback
                if (null != mOnCountdownEndListener) {
                    mOnCountdownEndListener.onEnd(CountdownView.this);
                }
            }
        };
        mCustomCountDownTimer.start();
    }

    /**
     * stop countdown
     */
    public void stop() {
        if (null != mCustomCountDownTimer) mCustomCountDownTimer.stop();
    }

    /**
     * pause countdown
     */
    public void pause() {
        if (null != mCustomCountDownTimer) mCustomCountDownTimer.pause();
    }

    /**
     * pause countdown
     */
    public void restart() {
        if (null != mCustomCountDownTimer) mCustomCountDownTimer.restart();
    }

    /**
     * custom time show
     * @param isShowDay isShowDay
     * @param isShowHour isShowHour
     * @param isShowMinute isShowMinute
     * @param isShowSecond isShowSecond
     * @param isShowMillisecond isShowMillisecond
     */
    @Deprecated
    public void customTimeShow(boolean isShowDay, boolean isShowHour, boolean  isShowMinute, boolean isShowSecond, boolean isShowMillisecond) {
        mCountdown.mHasSetIsShowDay = true;
        mCountdown.mHasSetIsShowHour = true;

        boolean isModCountdownInterval = mCountdown.refTimeShow(isShowDay, isShowHour, isShowMinute, isShowSecond);

        // judgement modify countdown interval
        if (isModCountdownInterval) {
            start(mRemainTime);
        }
    }

    /**
     * set all time zero
     */
    public void allShowZero() {
        mCountdown.setTimes(0, 0, 0, 0);
        invalidate();
    }

    /**
     * set countdown end callback listener
     * @param onCountdownEndListener OnCountdownEndListener
     */
    public void setOnCountdownEndListener(OnCountdownEndListener onCountdownEndListener) {
        mOnCountdownEndListener = onCountdownEndListener;
    }

    /**
     * set interval callback listener
     * @param interval interval time
     * @param onCountdownIntervalListener OnCountdownIntervalListener
     */
    public void setOnCountdownIntervalListener(long interval, OnCountdownIntervalListener onCountdownIntervalListener) {
        mInterval = interval;
        mOnCountdownIntervalListener = onCountdownIntervalListener;
    }

    /**
     * get day
     * @return current day
     */
    public int getDay() {
        return mCountdown.mDay;
    }

    /**
     * get hour
     * @return current hour
     */
    public int getHour() {
        return mCountdown.mHour;
    }

    /**
     * get minute
     * @return current minute
     */
    public int getMinute() {
        return mCountdown.mMinute;
    }

    /**
     * get second
     * @return current second
     */
    public int getSecond() {
        return mCountdown.mSecond;
    }

    /**
     * get remain time
     * @return remain time ( millisecond )
     */
    public long getRemainTime() {
        return mRemainTime;
    }

    public void updateShow(long ms) {
        this.mRemainTime = ms;

        int day = (int)(ms / (1000 * 60 * 60 * 24));
        int hour = (int)((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        int minute = (int)((ms % (1000 * 60 * 60)) / (1000 * 60));
        int second = (int)((ms % (1000 * 60)) / 1000);

        mCountdown.setTimes(day, hour, minute, second);

        // interval callback
        if (mInterval > 0 && null != mOnCountdownIntervalListener) {
            if (mPreviousIntervalCallbackTime == 0) {
                mPreviousIntervalCallbackTime = ms;
            } else if (ms + mInterval <= mPreviousIntervalCallbackTime) {
                mPreviousIntervalCallbackTime = ms;
                mOnCountdownIntervalListener.onInterval(this, mRemainTime);
            }
        }

        if (mCountdown.handlerAutoShowTime() || mCountdown.handlerDayLargeNinetyNine()) {
            reLayout();
        } else {
            invalidate();
        }
    }

    public interface OnCountdownEndListener {
        void onEnd(CountdownView cv);
    }

    public interface OnCountdownIntervalListener {
        void onInterval(CountdownView cv, long remainTime);
    }

    public void updataTextColer(String  color1){
        mCountdown.setTimeTextColor(Color.parseColor(color1));
        mCountdown.setSuffixTextColor(Color.parseColor(color1));
    }
}
