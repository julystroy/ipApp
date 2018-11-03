package com.cartoon.module.changxian.detail.comment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.data.CommentGroupResponse;
import com.cartoon.data.DetailPage;
import com.cartoon.data.Keys;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.base.ListViewFragment;
import com.cartoon.module.changxian.base.PagedAdapter;
import com.cartoon.module.changxian.comment.reply.CommentReplyActivity;
import com.cartoon.sdk.PlaySdk;
import com.cartoon.utils.UserDB;
import com.cartoon.view.EmptyView;
import com.cartoon.volley.GsonRequest;
import com.cartton.library.utils.DebugLog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.Locale;

import cn.com.xuanjiezhimen.R;
import cn.idianyun.streaming.data.LaunchInfo;

public class TabCommentFragment extends ListViewFragment {
    private CommentGroupAdapter mAdapter;
    private CommentRatingHeader mCommentHeader;
    private DetailPage mDetailPage;
    private View mEmptyCommentView;
    private CommentListener listener;

    @Override
    protected PagedAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new CommentGroupAdapter(getContext());
        }
        return mAdapter;
    }

    @Override
    protected boolean hasRefreshHeader() {
        return false;
    }

    @Override
    protected int getEmptyVieType() {
        return EmptyView.STATE_NO_COMMENT;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    protected int getListViewId() {
        return R.id.scroll;
    }

    @Override
    public int getEmptyCount() {
        return 1;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
        DebugLog.d("onResume view");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DebugLog.d("registerReceiver");
        IntentFilter updateLikeFilter = new IntentFilter(Keys.SEND_DETAIL_UPDATE_LIKE_BROADCAST);
        getContext().registerReceiver(mUpdateLikeReceiver, updateLikeFilter);
        IntentFilter durationFilter = new IntentFilter(Keys.SEND_PLAY_END_WITH_DURATION_BROADCAST);
        getContext().registerReceiver(mUpdatePlayDurationReceiver, durationFilter);
        IntentFilter deleteCommentFilter = new IntentFilter(Keys.SEND_DETAIL_DELETE_COMMENT_BROADCAST);
        getContext().registerReceiver(mDeleteCommentReceiver, deleteCommentFilter);
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(mUpdateLikeReceiver);
        getContext().unregisterReceiver(mUpdatePlayDurationReceiver);
        getContext().unregisterReceiver(mDeleteCommentReceiver);
        super.onDestroyView();
    }

    @Override
    protected void initViews() {
        super.initViews();
        LayoutInflater factory = LayoutInflater.from(getContext());
        mEmptyCommentView = factory.inflate(R.layout.layout_comment_empty, null);

        mCommentHeader = new CommentRatingHeader(getContext());
        mListView.addHeaderView(mCommentHeader);
        mCommentHeader.setTabCommentFragmentCallBack(new CommentRatingHeader.RatingViewListener() {
            @Override
            public void callEditRatingView() {
                EditCommentActivity.launch(getActivity(), mDetailPage.name, mDetailPage.id, true);
            }

            @Override
            public void beginTryPlay() {
//                if (mDetailPage.playType == 2) {
//                    LaunchingActivity.launch(getContext(), mDetailPage.id, mDetailPage.name, mDetailPage.quickInfo);
//                } else {
                    LaunchInfo launchInfo = mDetailPage.changeToLaunchInfo();
                    PlaySdk.getInstance().launch(getContext(), (int) mDetailPage.id, launchInfo, new PlaySdk.OnDownloadListener() {
                        @Override
                        public void onDownload(String url, String pkgName) {
                            DownloadGame record = UserDB.getInstance().getGameDownload(url);
                            record.setPackageName(pkgName);
                            record.setCxId(mDetailPage.id);
                            record.setAppName(mDetailPage.name);
                            record.setIconUrl(mDetailPage.logo);
                            UserDB.getInstance().saveGameDownload(record);
                        }
                    });
//                }
            }
        });

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();

        ObservableListView listView = (ObservableListView) mListView;
        if (viewGroup != null) {
            listView.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.root));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                listView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }

        mAdapter.setAppName(mDetailPage.name);
        mAdapter.setAppId(mDetailPage.id);

        if (mDetailPage != null) {
            mCommentHeader.setData(mDetailPage.ratingDetail, mDetailPage.user, mDetailPage.playType);
        }
    }

    @Override
    protected AdapterView.OnItemClickListener getClickItemListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view.getTag() == null) return;
                final int pos = (Integer) view.getTag();
                CommentReplyActivity.launch((Activity) getContext(), mAdapter.getItem(pos), mDetailPage.name, mDetailPage.id, false);
            }
        };
    }

    public void setData(DetailPage detailPage) {
        mDetailPage = detailPage;
        if (mCommentHeader != null && mDetailPage != null) {
            mCommentHeader.setData(mDetailPage.ratingDetail, mDetailPage.user, mDetailPage.playType);
        }
    }

    public void refreshRating() {
        mCommentHeader.setData(mDetailPage.ratingDetail, mDetailPage.user, mDetailPage.playType);
    }

    private String getURL(int pageCount, boolean mIsRefreshing) {
        long lastPublishAt = mIsRefreshing ? 0 : mAdapter.getLastPublishAt();
        long lastUsec = mIsRefreshing ? 0 : mAdapter.getLastUsec();
        String url = String.format(Locale.US, "%s/api/comments/list?appId=%d&last=%d&limit=%d&usec=%d",
                StaticField.BASE_CXURL, mDetailPage.id, lastPublishAt, pageCount, lastUsec);
        return url;
    }

    @Override
    protected GsonRequest getRequest(int pageCount, final boolean mIsRefreshing) {
        String url = getURL(pageCount, mIsRefreshing);
        DebugLog.d("loadCommentList. url = " + url);
        GsonRequest<CommentGroupResponse> gsonRequest = new GsonRequest<CommentGroupResponse>(url, CommentGroupResponse.class, new Response.Listener<CommentGroupResponse>() {

            @Override
            public void onResponse(CommentGroupResponse response) {
                boolean isEnd = response.data.end;
                if (!mIsRefreshing && mAdapter.getCount() > 0) {
                    //加载下一页
                    mAdapter.addData(response.data);
                    DebugLog.d("add next page");
                } else {
                    //非下拉刷新（返回页面更新数据等）
                    mAdapter.resetData(response.data);
                    DebugLog.d("reset data");
                }
                DebugLog.d("update comment count, response.data.count: " + response.data.count);

                if (response.data.count > 0) {
                    mDetailPage.commentCount = response.data.count;
                    if (listener != null) {
                        DebugLog.d("update comment count: " + mDetailPage.commentCount);
                        listener.updateCommentCount();
                    }
                }

                mIsEnd = isEnd;

                if (isEnd) {
                    removeFooterView();
                }
                checkEmpty();
                mIsLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadDataFail(error);
            }
        });
        return gsonRequest;
    }

    @Override
    public void checkEmpty() {
        showContentView();
        if (mAdapter.getCount() == 1) {
            if (mListView.getFooterViewsCount() == 0) {
                mListView.addFooterView(mEmptyCommentView);
            }
        } else {
            if (mListView.getFooterViewsCount() > 0) {
                mListView.removeFooterView(mEmptyCommentView);
            }
        }
    }

    private BroadcastReceiver mUpdateLikeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long likeId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            DebugLog.d("receive broadcast in tab comment fragment, id: " + likeId);
            if (likeId != -1) {
                DebugLog.d("update likes in mCommentGroupAdapter: " + likeId);
                mAdapter.updateLikes(likeId);
            }
        }
    };

    private BroadcastReceiver mUpdatePlayDurationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long appId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
            long duration = intent.getLongExtra(Keys.EXTRA_DURATION, -1);
            int playType = intent.getIntExtra(Keys.EXTRA_PLAY_TYPE, -1);
            if (appId != -1 && playType > 0) {
                if (playType == 1) {
                    mDetailPage.user.durationPlay += duration;
                }else if (playType == 2) {
                    mDetailPage.user.durationQuick += duration;
                }
                mCommentHeader.setData(mDetailPage.ratingDetail, mDetailPage.user, mDetailPage.playType);
            }
        }
    };

    private BroadcastReceiver mDeleteCommentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long commentId = intent.getLongExtra(Keys.EXTRA_COMMENT_ID, -1);
            long appId = intent.getLongExtra(Keys.EXTRA_APP_ID, -1);
            DebugLog.d("receive delete broadcast in tab comment info fragment");
            if (appId == mDetailPage.id) {
                mAdapter.deleteSelfComment(commentId);
            }
        }
    };

    public interface CommentListener {
        void updateCommentCount();
    }

    public void setCommentListener(CommentListener listener) {
        this.listener = listener;
    }
}
