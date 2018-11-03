package com.cartoon.module.changxian.comment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.Comment;
import com.cartoon.data.Keys;
import com.cartoon.data.LikeResponse;
import com.cartoon.data.response.EmptyResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.base.PagedAdapter;
import com.cartoon.module.changxian.comment.reply.CommentReplyActivity;
import com.cartoon.volley.GsonRequest;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.com.xuanjiezhimen.R;

public class CommentsAdapter extends PagedAdapter<Comment> {
    private Context mContext;

    private String mAppName;
    private long mAppId;

    public CommentsAdapter(Context context) {
        mContext = context;
        mItemList = new ArrayList<Comment>();
    }

    @Override
    public long getLastPublishAt() {
        if (mItemList == null || mItemList.size() == 0 || mItemList.get(mItemList.size() - 1) == null) {
            return 0;
        }

        return mItemList.get(mItemList.size() - 1).publishedAt;
    }

    public long getLastUsec() {
        if (mItemList == null || mItemList.size() == 0 || mItemList.get(mItemList.size() - 1) == null) {
            return 0;
        }

        return mItemList.get(mItemList.size() - 1).usec;
    }

    public void deleteSelfComment(long commentId) {
        DebugLog.d("delete comment id: " + commentId);
        int deletePosition = -1;
        for (int i = 0; i < mItemList.size(); i ++) {
            if (mItemList.get(i).id == commentId) {
                deletePosition = i;
                break;
            }
        }
        if (deletePosition != -1) {
            mItemList.remove(deletePosition);
            notifyDataSetChanged();
        }
    }

    private void sendDeleteSelfCommentNotification(long commentId) {
        Intent it = new Intent(Keys.SEND_DETAIL_DELETE_COMMENT_BROADCAST);
        it.putExtra(Keys.EXTRA_COMMENT_ID, commentId);
        it.putExtra(Keys.EXTRA_APP_ID, mAppId);
        getContext().sendBroadcast(it);
        DebugLog.d("send broadcast in commentsAdapter id: " + commentId);
    }

    public void setAppName (String appName) {
        mAppName = appName;
    }

    public void setAppId (long id) {
        mAppId = id;
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (!(convertView instanceof CommentItem)) {
            convertView = new CommentItem(mContext);
        }
        CommentItem commentItem = (CommentItem) convertView;
        commentItem.setTag(position);
        commentItem.setOnClickLikesListener(mOnClickLikesListener);
        commentItem.setOnClickDeleteListener(mOnClickDeleteListener);
        commentItem.setOnClickReplyListener(mOnClickReplyListener);
        commentItem.setOnClickShowAllCommentListener(mOnClickShowAllCommentListener);
        commentItem.setData(getItem(position), true);
        return convertView;
    }

    private void sendLikesUpdateNotify(long id) {
        Intent it = new Intent(Keys.SEND_DETAIL_UPDATE_LIKE_BROADCAST);
        it.putExtra(Keys.EXTRA_COMMENT_ID, id);
        getContext().sendBroadcast(it);
        DebugLog.d("send broadcast in commentsAdapter id: " + id);
    }

    protected void updateLikes(long id) {
        DebugLog.d("update likes in commentsAdapter: " + id);
        for (int i = 0; i < getCount(); i++) {
            Comment comment = getItem(i);
            if (comment != null && comment.id == id && !comment.liked) {
                comment.liked = true;
                comment.likes++;
                break;
            }
        }
        notifyDataSetChanged();
    }

    private void sendLikesRequest(long id) {
        String url = String.format("%s/api/comments/like", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("sendLikesRequest. url = " + url + ", params = " + params);
        GsonRequest<LikeResponse> request = new GsonRequest<LikeResponse>(Request.Method.POST, url, params.toString(), LikeResponse.class, new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                DebugLog.d("sendLikesRequest onResponse. commentId = " + response.data.id);
                sendLikesUpdateNotify(response.data.id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("sendLikesRequest failed. error = " + error);
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    private void deleteCommentRequest(final long id) {
        String url = String.format("%s/api/comments/delete", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("deleteComment. url = " + url + ", params = " + params);
        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
            @Override
            public void onResponse(EmptyResponse response) {
                DebugLog.d("deleteComment success");
                sendDeleteSelfCommentNotification(id);
                ToastUtils.showShort(getContext(), R.string.delete_success);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("deleteComment failed. error = " + error.toString());
                ToastUtils.showShort(getContext(), R.string.delete_fail);
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    private View.OnClickListener mOnClickLikesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = (Integer) v.getTag();
            Comment comment = getItem(pos);
            if (!comment.liked) {
                sendLikesRequest(comment.id);
            }
        }
    };

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = (Integer) v.getTag();
            String msg = getContext().getResources().getString(R.string.comment_delete_dialog_tip);
            AlertDialog alert = (new AlertDialog.Builder(getContext(), R.style.CXAlertDialogStyle)).setMessage(msg).setPositiveButton(getContext().getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Comment comment = getItem(pos);
                    if (comment.userId == AccountHelper.getUser().id) {
                        deleteCommentRequest(comment.id);
                    }
                }
            }).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
    };

    private View.OnClickListener mOnClickReplyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = (Integer) v.getTag();
            CommentReplyActivity.launch((Activity) getContext(), getItem(pos), mAppName, mAppId, true);
        }
    };

    private View.OnClickListener mOnClickShowAllCommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int pos = (Integer) v.getTag();
            CommentReplyActivity.launch((Activity) getContext(), getItem(pos), mAppName, mAppId, false);
        }
    };
}