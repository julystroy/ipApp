package com.cartoon.module.changxian.detail.comment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.cartoon.data.Comment;
import com.cartoon.data.CommentGroup;
import com.cartoon.module.changxian.comment.CommentsAdapter;
import com.cartton.library.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

public class CommentGroupAdapter extends CommentsAdapter {
    private List<Comment> mTopItems;

    public CommentGroupAdapter(Context context) {
        super(context);

        mTopItems = new ArrayList<Comment>();
    }

    public void addData(CommentGroup commentGroup) {
        if (commentGroup != null) {
            if (commentGroup.topItems != null && commentGroup.topItems.size() > 0) {
                if (mTopItems != null) {
                    updateFolded(mTopItems, commentGroup.topItems);
                }
                mTopItems.clear();
                mTopItems.addAll(commentGroup.topItems);
            }
            if (commentGroup.items != null && commentGroup.items.size() > 0) {
                addNextPage(commentGroup.items);
            }
            notifyDataSetChanged();
        }
    }

    public void resetData(CommentGroup commentGroup) {
        if (mTopItems != null) {
            updateFolded(mTopItems, commentGroup.topItems);
        }
        if (mItemList != null) {
            updateFolded(mItemList, commentGroup.items);
        }
        clearGroupData();
        addData(commentGroup);
    }

    private void updateFolded(List<Comment> oldData, List<Comment> newData) {
        for (int i = 0; i < newData.size(); i ++) {
            for (int j = 0; j < oldData.size(); j++) {
                if (newData.get(i).id == oldData.get(j).id) {
                    newData.get(i).isFold = oldData.get(j).isFold;
                    break;
                }
            }
        }
    }

    @Override
    public void deleteSelfComment(long commentId) {
        DebugLog.d("delete comment id: " + commentId);
        int deletePosition = -1;
        for (int i = 0; i < mTopItems.size(); i ++) {
            if (mTopItems.get(i).id == commentId) {
                deletePosition = i;
                break;
            }
        }
        if (deletePosition != -1) {
            mTopItems.remove(deletePosition);
            notifyDataSetChanged();
        }
        super.deleteSelfComment(commentId);
    }

    public void clearGroupData() {
        mTopItems.clear();
        cleanData();
    }

    private int getTopItemsCount() {
        return mTopItems.size();
    }

    @Override
    public int getCount() {
        if(mTopItems.size() == 0) return super.getCount() + 1;

        return super.getCount() + mTopItems.size() + 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Comment getItem(int position) {
        if (mTopItems.size() > 0 && position > 0) {
            if (getCount() > 2) {
                position -= 1;
                if (position >= 0 && position < mTopItems.size()) {
                    return mTopItems.get(position);
                }
                position -= (mTopItems.size() + 1);
                if (position >= 0 && position < super.getCount()) {
                    return super.getItem(position);
                }
            }
        }else {
            if (getCount() > 1 && position > 0) {
                position -=1;
                return super.getItem(position);
            }
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (mTopItems.size() > 0) {
            if (position == 0 || position == getTopItemsCount() + 1){
                CommentTitleItem titleItem = new CommentTitleItem(getContext());
                if (position == 0) {
                    titleItem.setData(getContext().getResources().getString(R.string.top_comments));
                } else {
                    titleItem.setData(getContext().getResources().getString(R.string.new_comments));
                }
                convertView = titleItem;
            }else {
                convertView = getCommentView(position, convertView, parent);
            }

        } else if (position == 0) {
            CommentTitleItem titleItem = new CommentTitleItem(getContext());
            titleItem.setData(getContext().getResources().getString(R.string.new_comments));
            convertView = titleItem;
        } else {
            convertView = getCommentView(position, convertView, parent);
        }

        return convertView;
    }

    public View getCommentView(int position, View convertView, final ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        return convertView;
    }

    @Override
    public void updateLikes(long id) {
        for (int i = 0; i < mTopItems.size(); i++) {
            Comment comment = mTopItems.get(i);
            if (comment != null && comment.id == id && !comment.liked) {
                comment.liked = true;
                comment.likes++;
            }
        }
        super.updateLikes(id);
    }
}
