package com.cartoon.module.changxian.comment;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.Comment;
import com.cartoon.data.CommentReply;
import com.cartoon.data.CommentReplyTo;
import com.cartoon.utils.Util;
import com.cartoon.view.ExpandableTextView;
import com.cartoon.volley.VolleySingleton;

import java.util.List;

import cn.com.xuanjiezhimen.R;

public class CommentItem extends LinearLayout {
    private NetworkImageView userAvatar;
    private TextView userName, mMeIcon, commentsTime, mTryPlayTV;
    private TextView mDeleteTV, mReplyTV, mShowAllCommentTV;
    private ImageView likes;
    private TextView likesCount;
    private TextView mCollapseText;
    private LinearLayout mReplyArea;
    private TextView mReply1TV, mReply2TV;
    private ExpandableTextView comments;
    private View               mLikeV;

    private Comment mComment;

    public CommentItem(Context context) {
        super(context);

        init();
    }

    public CommentItem(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.item_comment, this);

        userAvatar = (NetworkImageView) findViewById(R.id.iv_user_avatar);
        userName = (TextView) findViewById(R.id.tv_username);
        mMeIcon = (TextView) findViewById(R.id.tv_my_icon);
        commentsTime = (TextView) findViewById(R.id.tv_comments_time);
        mDeleteTV = (TextView)findViewById(R.id.tv_delete_comment);
        mTryPlayTV = (TextView)findViewById(R.id.tv_play_situation);
        likes = (ImageView) findViewById(R.id.iv_comments_likes);
        likesCount = (TextView) findViewById(R.id.tv_likes_count);
        mLikeV = findViewById(R.id.v_like);

        comments = (ExpandableTextView) findViewById(R.id.tv_comments);
        comments.setCollapseBtnVisible(false);
        comments.setExtraUI(new ExpandableTextView.ExtraUIListener() {
            @Override
            public void setCollapseText() {
                if (comments.getNeedCollapse()){
                    mCollapseText.setVisibility(View.VISIBLE);
                    if (comments.getCollapsed()){
                        mCollapseText.setText("显示全部");
                    } else {
                        mCollapseText.setText("收起");
                    }
                } else {
                    mCollapseText.setVisibility(View.GONE);
                }
            }
        });

        mCollapseText = (TextView) findViewById(R.id.expand_collapse1);
        mCollapseText.setOnClickListener(comments);

        mReplyTV = (TextView)findViewById(R.id.tv_reply);
        mShowAllCommentTV = (TextView)findViewById(R.id.tv_show_all_comments);
        mReplyArea = (LinearLayout)findViewById(R.id.llayout_comment_replies);
        mReply1TV = (TextView)findViewById(R.id.tv_comment_reply_1);
        mReply2TV = (TextView)findViewById(R.id.tv_comment_reply_2);
    }

    @Override
    public void setTag(Object tag) {
        super.setTag(tag);
        if (mLikeV != null) {
            mLikeV.setTag(tag);
        }
        if (mDeleteTV != null) {
            mDeleteTV.setTag(tag);
        }
        if (mReplyTV != null) {
            mReplyTV.setTag(tag);
        }
        if (mShowAllCommentTV != null) {
            mShowAllCommentTV.setTag(tag);
        }
    }

    public void setData(final Comment comment, boolean needFold) {
        mComment = comment;

        userName.setText(comment.name);
        setIsMine(comment.userId);
        commentsTime.setText(Util.friendly_time(comment.publishedAt));

        setRatingAndPlayTime(comment.rating, comment.duration);

        if (needFold) {
            comments.setText(comment.content, comment.isFold);

            comments.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
                @Override
                public void onExpandStateChanged(TextView textView, boolean isExpanded) {
                    if (!comments.getNeedCollapse()) return;

                    if (isExpanded){
                        mCollapseText.setText("收起");
                        mComment.isFold = false;
                    } else {
                        mCollapseText.setText("显示全部");
                        mComment.isFold = true;
                    }
                }
            });
            setCommentReplies(comment.replies, comment.replyCount, comment.id);
        }else {
            comments.setText(comment.content);
            hideReplayArea();
            mCollapseText.setVisibility(View.GONE);
        }

        userAvatar.setDefaultImageResId(R.drawable.default_user_avatar);
        userAvatar.setImageUrl(comment.avatar, VolleySingleton.getInstance(getContext()).getImageLoader());
        userAvatar.setOnClickListener(mOnClickAvatarListener);
        findViewById(R.id.iv_vip_logo).setVisibility(comment.isVip ? VISIBLE : GONE);

        likesCount.setText(String.valueOf(comment.likes));
        likesCount.setTag(comment.likes);

        likes.setSelected(comment.liked);
        likesCount.setSelected(comment.liked);
    }

    private void setRatingAndPlayTime(int rating, long playTime) {
        RelativeLayout infoView = (RelativeLayout)findViewById(R.id.rlayout_rating_bar);
        if (rating < 1 && playTime == 0) {
            infoView.setVisibility(GONE);
        }else {
            infoView.setVisibility(VISIBLE);
            if (playTime == 0) {
                mTryPlayTV.setVisibility(GONE);
            }else {
                mTryPlayTV.setVisibility(VISIBLE);
                mTryPlayTV.setText(String.format(getResources().getString(R.string.comment_try_play_time), playTime / 60));
            }
            setRating(rating/2);
        }
    }

    private void hideReplayArea() {
        mReplyArea.setVisibility(GONE);
        hideSeparator(false);
    }

    private void setCommentReplies(List<CommentReply> replies, int replyCount, long topicCommentId) {
        if (replies == null || replies.size() == 0) {
            hideReplayArea();
        }else {
            hideSeparator(true);
            mReplyArea.setVisibility(VISIBLE);
            if (replyCount < 3) {
                mShowAllCommentTV.setVisibility(GONE);
            }else {
                mShowAllCommentTV.setVisibility(VISIBLE);
                mShowAllCommentTV.setText(String.format(getResources().getString(R.string.show_all_comment), replyCount));
            }

            mReply1TV.setText(getReplyContent(replies.get(0), topicCommentId));
            if (replies.size() == 1) {
                mReply2TV.setVisibility(GONE);
            }else {
                mReply2TV.setVisibility(VISIBLE);
                mReply2TV.setText(getReplyContent(replies.get(1), topicCommentId));
            }
        }
    }

    private SpannableStringBuilder getReplyContent(CommentReply reply, long topicId) {
        String name = reply.name;
        CommentReplyTo replyTo = reply.replyTo;
        String content = reply.content;
        SpannableStringBuilder spannableStringBuilder1;
        if (reply.replyTo.id == topicId) {
            spannableStringBuilder1 = new SpannableStringBuilder(name + "：");
        }else {
            String replyTitle = name + getResources().getString(R.string.comment_reply) + replyTo.name + "：";
            spannableStringBuilder1 = new SpannableStringBuilder(replyTitle);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.button_text_a5));
            spannableStringBuilder1.setSpan(foregroundColorSpan, name.length(), replyTitle.length() - replyTo.name.length() - ":".length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        spannableStringBuilder1.setSpan(new StyleSpan(Typeface.BOLD),0, spannableStringBuilder1.length()-1,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableStringBuilder1.append(content);
        return spannableStringBuilder1;
    }

    private void setIsMine (long userId) {
        if (userId == AccountHelper.getUser().id) {
            mMeIcon.setVisibility(VISIBLE);
            mDeleteTV.setVisibility(VISIBLE);
        }else {
            mMeIcon.setVisibility(GONE);
            mDeleteTV.setVisibility(GONE);
        }
    }

    private void setRating(int star) {
        LinearLayout ratingView = (LinearLayout)findViewById(R.id.ll_ratingView);
        if (star < 1) {
            ratingView.setVisibility(GONE);
        }else {
            ratingView.setVisibility(VISIBLE);
        }

        ratingView.removeAllViews();
        int imageResource = R.drawable.comment_star2;
        switch (star) {
            case 1:
                imageResource = R.drawable.comment_star1;
                break;
            case 2:
                imageResource = R.drawable.comment_star2;
                break;
            case 3:
                imageResource = R.drawable.comment_star3;
                break;
            case 4:
                imageResource = R.drawable.comment_star4;
                break;
            case 5:
                imageResource = R.drawable.comment_star5;
                break;
        }

        for (int i = 0; i < star; i++) {
            ImageView ratingStar = new ImageView(getContext());
            ratingStar.setImageResource(imageResource);
            ratingView.addView(ratingStar);
        }
    }

    public void setCommentsLines(int lines) {
        comments.setMaxCollapsedLines(lines);
    }

    public void hideSeparator(boolean hide) {
        View separator = findViewById(R.id.v_separator);
        if (hide)
            separator.setVisibility(GONE);
        else
            separator.setVisibility(VISIBLE);
    }

    public void setOnClickLikesListener(OnClickListener listener) {
        mLikeV.setOnClickListener(listener);
    }

    public void setOnClickDeleteListener(OnClickListener listener) {
        mDeleteTV.setOnClickListener(listener);
    }

    public void setOnClickReplyListener(OnClickListener listener) {
        mReplyTV.setOnClickListener(listener);
    }

    public void setOnClickShowAllCommentListener(OnClickListener listener) {
        mShowAllCommentTV.setOnClickListener(listener);
    }

    private OnClickListener mOnClickAvatarListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //UIHelper.showToast(getContext(), "avatar,to user = " + item.name, Toast.LENGTH_SHORT);
        }
    };
}
