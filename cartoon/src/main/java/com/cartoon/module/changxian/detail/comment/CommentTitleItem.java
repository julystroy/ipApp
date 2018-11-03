package com.cartoon.module.changxian.detail.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

public class CommentTitleItem extends LinearLayout {
    private TextView mTitle;

    public CommentTitleItem(Context context) {
        super(context);

        init();
    }

    public CommentTitleItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.item_comment_title, this);

        mTitle = (TextView) findViewById(R.id.tv_title);
    }

    public void setData(String data) {
        mTitle.setText(data);
    }
}
