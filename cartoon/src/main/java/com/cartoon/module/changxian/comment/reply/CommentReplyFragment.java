package com.cartoon.module.changxian.comment.reply;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.Comment;
import com.cartoon.data.CommentInfo;
import com.cartoon.data.CommentReply;
import com.cartoon.data.CommentReplyListDataResponse;
import com.cartoon.data.Keys;
import com.cartoon.data.LikeResponse;
import com.cartoon.data.PagedList;
import com.cartoon.data.response.EmptyResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.base.ListViewFragment;
import com.cartoon.module.changxian.base.PagedAdapter;
import com.cartoon.module.changxian.comment.CommentItem;
import com.cartoon.uploadlog.UploadLog;
import com.cartoon.view.EmptyView;
import com.cartoon.volley.GsonRequest;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.umeng.socialize.utils.SocializeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.xuanjiezhimen.R;

/**
 * Created by wusue on 17/5/15.
 */

public class CommentReplyFragment extends ListViewFragment {
    private CommentItem mCommentReplyHeader;
    private Comment mTopicComment;
    private LinearLayout mReplyToArea;
    private TextView mReplyToNameTV;
    private Button mSendBtn;
    private EditText mReplyContentET;

    private CommentReplyAdapter mAdapter;

    private String mReplyContent;
    private long mAppId;

    private boolean mShowKeyBoard;
    private boolean mIsFirstLoad = true;

    @Override
    protected PagedAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommentReplyAdapter(getContext());
        }
        return mAdapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_reply;
    }

    @Override
    protected int getListViewId() {
        return R.id.lv_comments_replies;
    }

    @Override
    protected boolean hasRefreshHeader() {
        return false;
    }

    @Override
    protected AdapterView.OnItemClickListener getClickItemListener() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        mTopicComment = (Comment) intent.getSerializableExtra(Keys.EXTRA_COMMENT);
        String appName = intent.getStringExtra(Keys.EXTRA_APP_NAME);
        mAppId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
        mShowKeyBoard = intent.getBooleanExtra(Keys.EXTRA_SHOW_KEYBOARD, false);
        setTitle(appName);
    }

    @Override
    protected int getEmptyVieType() {
        return EmptyView.STATE_NO_COMMENT;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mCommentReplyHeader = new CommentItem(getContext());
        mCommentReplyHeader.setBackgroundResource(R.color.white);
        mCommentReplyHeader.setCommentsLines(Integer.MAX_VALUE);
        mCommentReplyHeader.setOnClickReplyListener(mOnClickReplyListener);
        mListView.addHeaderView(mCommentReplyHeader);

        mReplyToArea = (LinearLayout) findViewById(R.id.ll_not_reply_to_host);
        mReplyToNameTV = (TextView) findViewById(R.id.tv_reply_to_name);

        mReplyContentET = (EditText) findViewById(R.id.et_comment_reply);

        mSendBtn = (Button) findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReplyContent = mReplyContentET.getText().toString();
                sendReply();
            }
        });
        setData();
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                focusOnEditText(false);
                return false;
            }
        });
    }

    private void setData() {
        mCommentReplyHeader.setData(mTopicComment, false);
        setReplyTo(mTopicComment);
        mCommentReplyHeader.setOnClickDeleteListener(mOnClickDeleteListener);
        mCommentReplyHeader.setOnClickLikesListener(mOnClickLikeListener);
        mCommentReplyHeader.hideSeparator(true);
        mAdapter.setReplyCount(mTopicComment.replyCount);
        mAdapter.setTopicCommentId(mTopicComment.id);
        mAdapter.callBack = new CommentReplyAdapter.ReplyListener() {
            @Override
            public void replyToCommentChange(CommentInfo commentInfo) {
                setReplyTo(commentInfo);
                focusOnEditText(true);
            }

            @Override
            public void sendLikesComment(long commentId) {
                sendLikesRequest(commentId);
            }
        };
    }

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String msg = getContext().getResources().getString(R.string.comment_delete_dialog_tip);
            AlertDialog alert = (new AlertDialog.Builder(getContext(), R.style.CXAlertDialogStyle)).setMessage(msg).setPositiveButton(getContext().getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mTopicComment.userId == AccountHelper.getUser().id)
                        deleteCommentRequest(mTopicComment.id);

                }
            }).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
    };

    private View.OnClickListener mOnClickLikeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mTopicComment.liked)
                sendLikesRequest(mTopicComment.id);
        }
    };

    public void sendLikesRequest(long id) {
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
                long commentId = response.data.id;
                if (commentId == mTopicComment.id) {
                    mTopicComment.liked = true;
                    mTopicComment.likes++;
                    mCommentReplyHeader.setData(mTopicComment, false);
                    sendLikesUpdateNotify(response.data.id);
                } else {
                    mAdapter.updateLikes(commentId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("sendLikesRequest failed. error = " + error);
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void sendLikesUpdateNotify(long id) {
        Intent it = new Intent(Keys.SEND_DETAIL_UPDATE_LIKE_BROADCAST);
        it.putExtra(Keys.EXTRA_COMMENT_ID, id);
        getContext().sendBroadcast(it);
        DebugLog.d("send broadcast in commentsAdapter id: " + id);
    }

    private View.OnClickListener mOnClickReplyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            focusOnEditText(true);
            setReplyTo(mTopicComment);
        }
    };

    private void focusOnEditText(boolean focus) {
        if (mReplyContentET == null) return;

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focus) {
            if (!mReplyContentET.isFocused()) {
                mReplyContentET.requestFocus();
            }
            imm.showSoftInput(mReplyContentET, 0);
        } else {
            if (mReplyContentET.isFocused()) {
                mReplyContentET.clearFocus();
            }
            imm.hideSoftInputFromWindow(mReplyContentET.getWindowToken(), 0);
        }
    }

    private void deleteCommentRequest(final long id) {
        String url = String.format("%s/api/comments/delete", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("deleteComment. url = " + url + " params = " + params);
        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
            @Override
            public void onResponse(EmptyResponse response) {
                DebugLog.d("deleteComment success");
                sendDeleteSelfCommentNotification(id);
                ToastUtils.showShort(getContext(), R.string.delete_success);
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("deleteComment failed. error = " + error);
                ToastUtils.showShort(getContext(), R.string.delete_fail);
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void sendDeleteSelfCommentNotification(long commentId) {
        Intent it = new Intent(Keys.SEND_DETAIL_DELETE_COMMENT_BROADCAST);
        it.putExtra(Keys.EXTRA_COMMENT_ID, commentId);
        if (mAppId != -1) {
            it.putExtra(Keys.EXTRA_APP_ID, mAppId);
        }
        getContext().sendBroadcast(it);
        DebugLog.d("send broadcast in commentsAdapter id: " + commentId);
    }

    private void setReplyTo(CommentInfo commentInfo) {
        mAdapter.setReplyToComment(commentInfo);
        if (commentInfo.id != mTopicComment.id) {
            mReplyToArea.setVisibility(View.VISIBLE);
            mReplyToNameTV.setText(commentInfo.name);
        } else {
            mReplyToArea.setVisibility(View.GONE);
        }
    }

    private void sendReply() {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        SocializeUtils.safeShowDialog(dialog);
        String url = String.format("%s/api/comments/reply", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        long replyToId = mAdapter.getReplyToComment().id;
        try {
            params.put("replyId", replyToId);
            params.put("content", mReplyContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("reply id: " + mAdapter.getReplyToComment().id + " reply content: " + mReplyContent);
        GsonRequest<LikeResponse> request = new GsonRequest<LikeResponse>(Request.Method.POST, url, params.toString(), LikeResponse.class, new Response.Listener<LikeResponse>() {
            @Override
            public void onResponse(LikeResponse response) {
                long commentReplyId = response.data.id;
                DebugLog.d("comment reply success. id: " + commentReplyId);
                dialog.dismiss();
                addReply();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("comment reply failed. e = " + error.toString());
                ToastUtils.showShort(getContext(), getResources().getString(R.string.comment_reply_fail));
                dialog.dismiss();
            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void addReply() {
        ToastUtils.showShort(getContext(), getResources().getString(R.string.comment_reply_success));
        mReplyContentET.setText("");
        focusOnEditText(false);
        UploadLog.uploadPageActionCompleteLog(UploadLog.REPLY_BUTTON, mAppId, getActivity());
        //TODO:更新本地评论数，最好通过refreshData服务器返回
//        mTopicComment.replyCount ++;
//        mAdapter.setReplyCount(mTopicComment.replyCount);
        refreshData();
    }

    private String getURL(int pageCount, boolean mIsRefreshing) {
        DebugLog.d("isRefreshing:" + mIsRefreshing + ", page count:" + pageCount);
        String url = String.format("%s/api/comments/list?id=%d&last=%d&usec=%d&limit=%d", StaticField.BASE_CXURL,
                mTopicComment.id, mIsRefreshing ? 0 : mAdapter.getLastPublishAt(), mIsRefreshing ? 0 : mAdapter.getLastUsec(), pageCount);
        return url;
    }

    @Override
    protected GsonRequest getRequest(int pageCount, boolean mIsRefreshing) {
        String url = getURL(pageCount, mIsRefreshing);
        DebugLog.d("get comment replies url:" + url);
        GsonRequest<CommentReplyListDataResponse> request = new GsonRequest<CommentReplyListDataResponse>(Request.Method.GET, url, CommentReplyListDataResponse.class, new Response.Listener<CommentReplyListDataResponse>() {
            @Override
            public void onResponse(CommentReplyListDataResponse response) {
                PagedList<CommentReply> phPage = response.data;
                    loadDataSuccess(phPage.items, phPage.end);
                    DebugLog.d("response count:" + phPage.count);

                if (phPage.count > 0) {
                    DebugLog.d("response count:" + phPage.count);
                    mAdapter.setReplyCount(phPage.count);
                    mTopicComment.replyCount = phPage.count;
                }

                if (mIsFirstLoad) {
                    mIsFirstLoad = false;
                    if (mShowKeyBoard) {
                        DebugLog.d("enter reply view and show keyboard");
                        focusOnEditText(true);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mIsFirstLoad) {
                    mIsFirstLoad = false;
                    if (mShowKeyBoard) {
                        focusOnEditText(true);
                    }
                }
                ToastUtils.showShort(getContext(), getResources().getString(R.string.comment_reply_load_list_fail));
            }
        });
        return request;
    }
}
