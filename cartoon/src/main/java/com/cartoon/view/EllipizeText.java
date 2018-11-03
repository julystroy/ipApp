package com.cartoon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-4-21.
 */
public class EllipizeText extends TextView {
    private Paint    mPaint;
    private int      mViewWidth;
    private String[] mString;
    private String ellipsize = "...";
    private String mText;
    private int mMaxLine;
    private int mCurrentLine = 0;
    public EllipizeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LimitLineTextView);
        //因为获取maxline属性要sdk = 16及以上版本才支持,所以自定义一个获取最大行数属性，以适配所有机型
        mMaxLine = typedArray.getInt(R.styleable.LimitLineTextView_maxLine, 5);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint      = getPaint();
        int x = getPaddingLeft();
        int y = (int) (getPaddingTop() - mPaint.ascent());

        if(!TextUtils.isEmpty(mText)){
            mString = mText.split("\n");
            mViewWidth  = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            for(int  i = 0; i < mString.length; i ++){
                String targetStr = mString[i];

                for(int j = 0; j < mMaxLine; j ++){

                    if(!TextUtils.isEmpty(targetStr)){
                        mCurrentLine = mCurrentLine + 1;
                        if(mCurrentLine == mMaxLine){
                            int secondCharCount = mPaint.breakText(ellipsize + targetStr, true, mViewWidth, null);
                            if(targetStr.length() > secondCharCount - 3){
                                targetStr = targetStr.substring(0, secondCharCount - 3) + ellipsize;
                            }
                            canvas.drawText(targetStr, x, y, mPaint);
                        }else{
                            int charCount = mPaint.breakText(targetStr, true, mViewWidth, null);

                            canvas.drawText(targetStr.substring(0, charCount), x, y, mPaint);
                            y = y + getLineHeight();
                            if(targetStr.length() > charCount){
                                String strLeave = targetStr.substring(charCount, targetStr.length());
                                if(strLeave.length() > 0){
                                    targetStr = strLeave;
                                }
                            }else{
                                targetStr = "";
                            }
                        }
                        if(mCurrentLine == mMaxLine) return;
                    }
                }
            }
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        mText = text.toString();
    }


    //避免本身的
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
