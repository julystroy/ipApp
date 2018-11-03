package com.cartoon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class RatingBar extends View {
    public static int[] COLORS = {0xfff86554, 0xffff9758, 0xffffba40, 0xff71dd82, 0xff66dad6};
    private static int TEXT_COLOR = 0xff999999;
    private static final int DEFAULT_BAR_THICKNESS_DIPS = 10;
    private static final int DEFAULT_PADDING_DIPS = 4;
    private static final int DEFAULT_MIN_BAR_WIDTH_DIPS = 5;
    private static final int DEFAULT_TEXT_SIZE_DIPS = 10;
    private static final int DEFAULT_TEXT_MARGIN_DIPS = 5;

    private Paint mPaint;

    private float mDensity;
    private float mBarThickness;
    private float mPadding;
    private float mMinBarWidth;
    private float mTextSize;
    private float mTextMargin;

    private int[] mRatings;

    public RatingBar(Context context) {
        super(context);

        init();
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(6f);
        mPaint.setAntiAlias(true);

        mDensity = getResources().getDisplayMetrics().density;
        mBarThickness = DEFAULT_BAR_THICKNESS_DIPS * mDensity;
        mPadding = DEFAULT_PADDING_DIPS * mDensity;
        mMinBarWidth = DEFAULT_MIN_BAR_WIDTH_DIPS * mDensity;
        mTextSize = DEFAULT_TEXT_SIZE_DIPS * mDensity;
        mTextMargin = DEFAULT_TEXT_MARGIN_DIPS * mDensity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRatings == null || mRatings.length != COLORS.length) {
            return;
        }

        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        int maxRating = max(mRatings);
        float maxTextWidth = mPaint.measureText(String.valueOf(maxRating));
        //float textHeight = getFontHeight(mTextSize);
        //float textMarginBottom = Math.max((mBarThickness - textHeight) / 2, 0);
        float width = getWidth() - maxTextWidth - mTextMargin - 5 * mDensity;
        for (int i = 0; i < COLORS.length; i++) {
            mPaint.setColor(COLORS[i]);
            float barTop = i * (mBarThickness + mPadding);
            float barBottom = i * (mBarThickness + mPadding) + mBarThickness;
            float barWidth = mMinBarWidth;
            if (maxRating > 0) {
                barWidth = Math.max(mRatings[i] * width / maxRating, mMinBarWidth);
            }
            canvas.drawRect(0, barTop, barWidth, barBottom, mPaint);

            mPaint.setColor(TEXT_COLOR);
            String text = String.valueOf(mRatings[i]);
            float textW = mPaint.measureText(text);
            canvas.drawText(text, barWidth + textW / 2 + mTextMargin, barBottom - 2 * mDensity, mPaint);
        }
    }

    public void setRating(int[] ratings) {
        mRatings = ratings;
        invalidate();
    }

    private int max(int[] array) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private int getFontHeight(float fontSize) {
        mPaint.setTextSize(fontSize);
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }
}
