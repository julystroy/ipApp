package com.cartoon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * http://blog.csdn.net/jdsjlzx/article/details/50616227
 *
 * 可伸展的文本显示布局
 * @author jan
 */
public class StretchyTextView extends LinearLayout implements View.OnClickListener {
    //默认显示的最大行数
    private static final int DEFAULT_MAX_LINE_COUNT = 2;
    //当前展开标志显示的状态
    private static final int SPREADTEXT_STATE_NONE = 0;
    private static final int SPREADTEXT_STATE_RETRACT = 1;
    private static final int SPREADTEXT_STATE_SPREAD = 2;

    private TextView contentText;
//    private TextView operateText;
    private ImageView operateImageview;
    private LinearLayout bottomTextLayout;

    private LinearLayout mOthersLayout;
    private TextView mStrategyText,mDownloadCountText,mTryCountText;

    private int mState;
    private boolean flag = false;
    private int maxLineCount = DEFAULT_MAX_LINE_COUNT;
    private InnerRunnable runable;

    public StretchyTextView(Context context) {
        this(context, null);
    }

    public StretchyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_stretchy_text, this);
        view.setPadding(0, -1, 0, 0);
        contentText = (TextView) view.findViewById(R.id.content_textview);
        operateImageview = (ImageView) view.findViewById(R.id.iv_bottom_op);
        bottomTextLayout = (LinearLayout) view.findViewById(R.id.bottom_text_layout);
        setBottomTextGravity(Gravity.LEFT);
        operateImageview.setOnClickListener(this);
        runable = new InnerRunnable();

        mOthersLayout = (LinearLayout) view.findViewById(R.id.layout_others);
        mStrategyText = (TextView) view.findViewById(R.id.tv_strategy);
        mDownloadCountText = (TextView) view.findViewById(R.id.tv_downloadcount);
        mTryCountText = (TextView) view.findViewById(R.id.tv_trycount);
    }

    @Override
    public void onClick(View v) {
        flag = false;
        requestLayout();
    }

    public final void setContent(CharSequence charSequence) {
        contentText.setText(charSequence, TextView.BufferType.NORMAL);
        mState = SPREADTEXT_STATE_SPREAD;
        flag = false;
        requestLayout();
    }

    public final void setContentWithOthers(CharSequence charSequence,String strategy, long tryCount, long downloadCount){
        setContent(charSequence);
        if (TextUtils.isEmpty(strategy)){
            mStrategyText.setText(getContext().getString(R.string.advise_placeholder), TextView.BufferType.NORMAL);
        }else {
            mStrategyText.setText(strategy, TextView.BufferType.NORMAL);
        }

        mTryCountText.setText(String.valueOf(tryCount), TextView.BufferType.NORMAL);
        mDownloadCountText.setText(String.valueOf(downloadCount), TextView.BufferType.NORMAL);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!flag) {
            flag = !flag;
            if (contentText.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
                mState = SPREADTEXT_STATE_NONE;
                operateImageview.setVisibility(View.GONE);

                mOthersLayout.setVisibility(View.GONE);

                contentText.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
            }else {
                post(runable);
            }
        }
    }

    class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mState == SPREADTEXT_STATE_SPREAD) {
                contentText.setMaxLines(maxLineCount);
                operateImageview.setVisibility(View.VISIBLE);
                operateImageview.setImageResource(R.drawable.icon_spread);
                mState = SPREADTEXT_STATE_RETRACT;

                mOthersLayout.setVisibility(View.GONE);

            } else if (mState == SPREADTEXT_STATE_RETRACT) {
                contentText.setMaxLines(Integer.MAX_VALUE);
                operateImageview.setVisibility(View.VISIBLE);
                operateImageview.setImageResource(R.drawable.icon_shrink);
                mState = SPREADTEXT_STATE_SPREAD;

                mOthersLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public void setContentTextColor(int color){
        this.contentText.setTextColor(color);
    }

    public void setContentTextSize(float size){
        this.contentText.setTextSize(size);
    }

    /**
     * 内容字体加粗
     */
    public void setContentTextBold(){
        TextPaint textPaint = contentText.getPaint();
        textPaint.setFakeBoldText(true);
    }
    /**
     * 设置展开标识的显示位置
     * @param gravity
     */
    public void setBottomTextGravity(int gravity){
        bottomTextLayout.setGravity(gravity);
    }
}