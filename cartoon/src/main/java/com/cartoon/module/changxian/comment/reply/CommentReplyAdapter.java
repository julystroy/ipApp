package com.cartoon.module.changxian.comment.reply;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cartoon.data.CommentInfo;
import com.cartoon.data.CommentReply;
import com.cartoon.module.changxian.base.PagedAdapter;
import com.cartton.library.utils.DebugLog;

import java.util.ArrayList;

/**
 * Created by wusue on 17/5/15.
 */

public class CommentReplyAdapter extends PagedAdapter<CommentReply> {
    private Context mContext;
    private int mReplyCount;
    private long mTopicCommentId;
    private CommentInfo mReplyToComment;
    public ReplyListener callBack;

    public CommentReplyAdapter(Context context) {
        mContext = context;
        mItemList = new ArrayList<CommentReply>();
    }

    @Override
    protected long getLastPublishAt() {
        if (mItemList == null || mItemList.size() == 0) {
            return 0;
        } else {
            return mItemList.get(mItemList.size() - 1).publishedAt;
        }
    }

    public long getLastUsec() {
        if (mItemList == null || mItemList.size() == 0) {
            return 0;
        } else {
            return mItemList.get(mItemList.size() - 1).usec;
        }
    }

    public CommentInfo getReplyToComment() {
        return mReplyToComment;
    }

    public void setReplyToComment(CommentInfo commentInfo) {
        mReplyToComment = commentInfo;
    }

    public void setTopicCommentId(long id) {
        mTopicCommentId = id;
    }

    public void setReplyCount(int count) {
        mReplyCount = count;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return (mItemList == null || mItemList.size() == 0) ? 0 : mItemList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public CommentReply getItem(int position) {
        if (position > 0 && mItemList.size() > 0) {
            return mItemList.get(position - 1);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (position == 0) {
            CommentReplyTitleItem titleItem = new CommentReplyTitleItem(getContext());
            titleItem.setReplyCount(mReplyCount);
            convertView = titleItem;
        } else {
            if (!(convertView instanceof CommentReplyItem)) {
                convertView = new CommentReplyItem(mContext);
            }
            CommentReplyItem item = (CommentReplyItem) convertView;
            item.setData(getItem(position), mTopicCommentId);
            item.setTag(position);
            item.setOnClickReplyListener(mOnClickReplyListener);
            item.setOnClickLikesListener(mOnClickLikesListener);
        }

        return convertView;
    }

    protected void updateLikes(long id) {
        DebugLog.d("update likes in commentsAdapter: " + id);
        for (int i = 0; i < getCount(); i++) {
            CommentReply comment = getItem(i);
            if (comment != null && comment.id == id && !comment.liked) {
                comment.liked = true;
                comment.likes++;
                break;
            }
        }
        notifyDataSetChanged();
    }

    private View.OnClickListener mOnClickReplyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (Integer) v.getTag();
            CommentReply comment = getItem(pos);
            callBack.replyToCommentChange(comment);
        }
    };

    private View.OnClickListener mOnClickLikesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (Integer) v.getTag();
            CommentReply comment = getItem(pos);
            if (!comment.liked) {
                callBack.sendLikesComment(comment.id);
            }
        }
    };

    public interface ReplyListener {
        void replyToCommentChange(CommentInfo commentInfo);

        void sendLikesComment(long commentId);
    }
}
