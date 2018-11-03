package com.cartoon.module.changxian.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.account.AccountHelper;
import com.cartoon.utils.UIHelper;
import com.cartoon.volley.VolleySingleton;

import cn.com.xuanjiezhimen.R;

public class CommentsHeaderItem extends LinearLayout {

    private NetworkImageView mPoster;

    public CommentsHeaderItem(Context context) {
        this(context, null);
    }

    public CommentsHeaderItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentsHeaderItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_comments_header, this, true);

        mPoster = (NetworkImageView) findViewById(R.id.iv_avatar);

        findViewById(R.id.iv_comment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //jump to comment detail
                UIHelper.showToast(getContext(), "jump to comment detail", Toast.LENGTH_SHORT);
            }
        });
    }

    public void setData() {
        mPoster.setDefaultImageResId(R.drawable.default_user_avatar);
        mPoster.setImageUrl(AccountHelper.getUser().avatar, VolleySingleton.getInstance(getContext()).getImageLoader());
    }

    public void setOnClickCommentBarListener(OnClickListener listener) {
        findViewById(R.id.rlayout_comment_bar).setOnClickListener(listener);
    }
}
