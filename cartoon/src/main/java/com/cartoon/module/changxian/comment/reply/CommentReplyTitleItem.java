package com.cartoon.module.changxian.comment.reply;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by wusue on 17/5/16.
 */

public class CommentReplyTitleItem extends LinearLayout {
    private TextView mReplyCountTV;

    public CommentReplyTitleItem(Context context) {
        super(context);

        init();
    }

    public CommentReplyTitleItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.layout_comment_reply_first_item, this);

        mReplyCountTV = (TextView) findViewById(R.id.tv_comment_replies_count);
    }

    public void setReplyCount(int count) {
        mReplyCountTV.setText(String.format("(%d)", count));
    }
}
