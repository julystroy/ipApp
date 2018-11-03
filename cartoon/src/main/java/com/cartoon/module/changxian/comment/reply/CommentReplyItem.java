package com.cartoon.module.changxian.comment.reply;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.data.CommentReply;
import com.cartoon.data.CommentReplyTo;
import com.cartoon.utils.Util;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;

import cn.com.xuanjiezhimen.R;

public class CommentReplyItem extends RelativeLayout {
    private TextView mReplyBtn;
    private TextView mUserNameTV, mCommentReplyContentTV, mLikesCountTV, mCommentReplyTimeTV;
    private View mLikeBtn;
    private NetworkImageView mUserAvatar;
    private ImageView mLikesIV;

    public CommentReplyItem(Context context) {
        super(context);

        init();
    }

    public CommentReplyItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.item_comment_reply, this);
        mReplyBtn = (TextView) findViewById(R.id.tv_reply);
        mCommentReplyContentTV = (TextView) findViewById(R.id.tv_comments);
        mUserNameTV = (TextView) findViewById(R.id.tv_username);
        mLikesCountTV = (TextView) findViewById(R.id.tv_likes_count);
        mCommentReplyTimeTV = (TextView) findViewById(R.id.tv_comments_time);
        mUserAvatar = (NetworkImageView) findViewById(R.id.iv_user_avatar);
        mLikesIV = (ImageView) findViewById(R.id.iv_comments_likes);
        mLikeBtn = findViewById(R.id.v_like);
    }

    public void setData(CommentReply commentReply, long topicCommentId) {
        mUserAvatar.setDefaultImageResId(R.drawable.default_user_avatar);
        mUserAvatar.setImageUrl(commentReply.avatar, VolleySingleton.getInstance(getContext()).getImageLoader());
        findViewById(R.id.iv_vip_logo).setVisibility(commentReply.isVip ? VISIBLE : GONE);

        setName(commentReply.name, commentReply.replyTo, topicCommentId);
        mCommentReplyContentTV.setText(commentReply.content);

        mLikesCountTV.setText(String.valueOf(commentReply.likes));
        mLikesCountTV.setTag(commentReply.likes);

        mLikesIV.setSelected(commentReply.liked);
        mLikesCountTV.setSelected(commentReply.liked);

        mCommentReplyTimeTV.setText(Util.friendly_time(commentReply.publishedAt));
        DebugLog.d("comment reply published at:" + commentReply.publishedAt);
    }

    private void setName(String name, CommentReplyTo replyTo, long topicCommentId) {
        if (replyTo.id == topicCommentId) {
            mUserNameTV.setText(name);
        } else {
            String replyTitle = name + getResources().getString(R.string.comment_reply) + replyTo.name;
            SpannableStringBuilder spannableStringBuilder1 = new SpannableStringBuilder(replyTitle);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.button_text_a5));
            spannableStringBuilder1.setSpan(foregroundColorSpan, name.length(), replyTitle.length() - replyTo.name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            mUserNameTV.setText(spannableStringBuilder1);
        }
    }

    @Override
    public void setTag(Object tag) {
        mReplyBtn.setTag(tag);
        mLikeBtn.setTag(tag);
        super.setTag(tag);
    }

    public void setOnClickReplyListener(OnClickListener listener) {
        mReplyBtn.setOnClickListener(listener);
    }

    public void setOnClickLikesListener(OnClickListener listener) {
        mLikeBtn.setOnClickListener(listener);
    }
}
