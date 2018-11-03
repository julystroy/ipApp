package com.cartoon.module.changxian.detail.detailinfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.cartoon.data.Comment;
import com.cartoon.data.DetailPage;
import com.cartoon.data.Keys;
import com.cartoon.module.changxian.base.BaseFragment;
import com.cartoon.module.changxian.comment.CommentsAdapter;
import com.cartoon.module.changxian.comment.reply.CommentReplyActivity;
import com.cartoon.module.changxian.detail.DetailFragment;
import com.cartton.library.utils.DebugLog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import cn.com.xuanjiezhimen.R;

public class TabDetailInfoFragment extends BaseFragment {
    private DetailInfoView mDetailInfoView;
    private DetailPage mDetailPage;

    private ObservableListView mListView;
    private View mEmptyCommentView;
    private CommentsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_info, container, false);

        mListView = (ObservableListView) view.findViewById(R.id.scroll);

        mDetailInfoView = new DetailInfoView(container.getContext());
        mDetailInfoView.setOnClickCommentBarListener(mOnClickCommentBarListener);
        mListView.addHeaderView(mDetailInfoView);

        mAdapter = new CommentsAdapter(getContext());
        mListView.setAdapter(mAdapter);
        if (mListView != null) {
            DebugLog.d("gvIcon not null, listener should be available");
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  final int pos = (Integer) view.getTag();
                                                  DebugLog.d("click comment item, should turn to reply page");
                                                  CommentReplyActivity.launch((Activity) getContext(), mAdapter.getItem(pos), mDetailPage.name, mDetailPage.id, false);
                                              }
                                          }
            );
        }

        LayoutInflater factory = LayoutInflater.from(getContext());
        mEmptyCommentView = factory.inflate(R.layout.layout_comment_empty, null);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            mListView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.root));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                mListView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mDetailPage != null) {
            DebugLog.d("onViewCreated mDetailInfoView setData");
            setOrRefreshData(false);
        }

        DebugLog.d("registerReceiver");
        IntentFilter counterActionFilter = new IntentFilter(Keys.SEND_DETAIL_UPDATE_LIKE_BROADCAST);
        getContext().registerReceiver(mNeedReloadReceiver, counterActionFilter);
        IntentFilter deleteCommentFilter = new IntentFilter(Keys.SEND_DETAIL_DELETE_COMMENT_BROADCAST);
        getContext().registerReceiver(mDeleteCommentReceiver, deleteCommentFilter);
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(mNeedReloadReceiver);
        getContext().unregisterReceiver(mDeleteCommentReceiver);
        super.onDestroyView();
    }

    public void setData(DetailPage detailPage) {
        mDetailPage = detailPage;
        if (mDetailPage != null && mDetailInfoView != null) {
            DebugLog.d("set data mDetailPage: " + mDetailPage.toString() + ",count: " + mDetailPage.comments.size());
//            mDetailInfoView.setData(mDetailPage);
            setOrRefreshData(true);
        }
    }

    private View.OnClickListener mOnClickCommentBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DebugLog.d("mOnClickCommentBarListener");
            Fragment parentFragment = getParentFragment();
            if (parentFragment instanceof DetailFragment) {
                ((DetailFragment) parentFragment).selectCommentTab();
            }
        }
    };


    private BroadcastReceiver mNeedReloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long likeId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            if (likeId != -1) {
                updateLikes(likeId);
            }
        }
    };

    protected void updateLikes(long id) {
        for (int i = 0; i < mDetailPage.comments.size(); i++) {
            Comment comment = mDetailPage.comments.get(i);
            if (comment != null && comment.id == id && !comment.liked) {
                comment.liked = true;
                comment.likes++;
            }
        }

        setOrRefreshData(true);
    }

    private BroadcastReceiver mDeleteCommentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long appId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
            long commentId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            if (appId != mDetailPage.id) {
                DebugLog.d("appId: " + appId + ", mDetailPage.id: " + mDetailPage.id);
                return;
            }
            if (commentId != -1) {
                DebugLog.d("receive delete broadcast in tab detail info fragment");
                int deleteItem = -1;
                for (int i = 0; i < mDetailPage.comments.size(); i++) {
                    Comment comment = mDetailPage.comments.get(i);
                    if (comment != null && comment.id == commentId) {
                        deleteItem = i;
                        break;
                    }
                }
                if (deleteItem != -1) {
                    DebugLog.d("deleteItem:" + deleteItem);
                    mDetailPage.comments.remove(deleteItem);

                    setOrRefreshData(true);
                }
            }
        }
    };

    private void setOrRefreshData(boolean needFresh) {
        if(needFresh){
            mAdapter.cleanData();
        }else {
            mDetailInfoView.setData(mDetailPage);
        }
        mAdapter.setAppName(mDetailPage.name);
        mAdapter.setData(mDetailPage.comments);
        checkCommentEmpty();
    }

    public void checkCommentEmpty() {
        if (mAdapter.getCount() == 0) {
            if (mListView.getFooterViewsCount() == 0){
                mListView.addFooterView(mEmptyCommentView);
            }
        }else if (mListView.getFooterViewsCount()>0) {
            mListView.removeFooterView(mEmptyCommentView);
        }
    }
}
