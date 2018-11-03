package com.cartoon.module.changxian.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.cartoon.data.Keys;
import com.cartoon.view.EmptyView;
import com.cartoon.volley.GsonRequest;
import com.cartoon.volley.VolleySingleton;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;

import java.util.List;

import cn.com.xuanjiezhimen.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by wusue on 17/6/19.
 */

public abstract class ListViewFragment extends BaseFragment {
    protected ListView mListView;
    protected PagedAdapter mAdapter;

    protected boolean mIsRefreshing = false;
    protected boolean mIsEnd = false;
    protected boolean mIsLoading = false;

    private View mFooterView;
    private boolean hasFooterView = false;

    private PtrClassicFrameLayout mPtrFrame;

    protected abstract boolean hasRefreshHeader();
    protected abstract int getLayoutId();
    protected abstract int getListViewId();
    protected abstract int getEmptyVieType();

    protected AdapterView.OnItemClickListener mClickItemListener = getClickItemListener();
    protected abstract GsonRequest getRequest(int pageCount, boolean mIsRefreshing);
    protected abstract PagedAdapter getAdapter();
    protected abstract AdapterView.OnItemClickListener getClickItemListener();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAdapter = getAdapter();
        if (mFooterView == null) {
            mFooterView = inflater.inflate(R.layout.layout_list_loading_footer, null);
        }
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter.getCount() == getEmptyCount()) {
            showEmptyView(EmptyView.STATE_INIT);
            loadData();
        }else {
            showContentView();
        }
    }

    public int getEmptyCount() {
        return 0;
    }

    @Override
    protected View getContentView() {
        return hasRefreshHeader() ? mPtrFrame : mListView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void initViews() {
        //TODO:
//        if (hasRefreshHeader()) {
//            mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
//            mPtrFrame.setPtrHandler(new PtrHandler() {
//                @Override
//                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                    return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//                }
//
//                @Override
//                public void onRefreshBegin(PtrFrameLayout frame) {
//                    DebugLog.d("onRefreshBegin");
//                    mIsEnd = false;
//                    refreshData();
//                }
//            });
//            mPtrFrame.setLastUpdateTimeKey("YYYY-MM-DD");
//            mPtrFrame.setEnabledNextPtrAtOnce(true);
//        }

        mListView = (ListView) findViewById(getListViewId());
        mListView.setAdapter(mAdapter);
        if (mClickItemListener != null) {
            mListView.setOnItemClickListener(mClickItemListener);
        }

        if (mListView != null) {
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                private boolean reachEnd;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    DebugLog.d("onScrollStateChanged. state = " + scrollState);
                    if (scrollState == SCROLL_STATE_IDLE && reachEnd) {
                        loadData();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        DebugLog.d("onScroll. end");
                        reachEnd = true;
                    } else {
                        reachEnd = false;
                    }
                }
            });
        }
    }

    protected void refreshData() {
        DebugLog.d("refresh data");
        if (mIsLoading) return;
        mIsRefreshing = true;
        mIsLoading = true;
        mIsEnd = false;
        GsonRequest gsonRequest = getRequest(mAdapter.getCount(), true);
        if (gsonRequest != null) {
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(gsonRequest);
        }
    }

    @Override
    protected void loadData() {
        if (mIsEnd || mIsLoading) {
            return;
        }
        mIsRefreshing = false;
        mIsLoading = true;
        DebugLog.d("load data");
        if (!hasFooterView) {
            hasFooterView = true;
            mListView.addFooterView(mFooterView);
        }
        GsonRequest gsonRequest = getRequest(Keys.LIST_PAGE_SIZE, false);
        if (gsonRequest != null) {
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(gsonRequest);
        }
    }

    protected void loadDataSuccess(List data, boolean isEnd) {
        if (!mIsRefreshing && mAdapter.getCount() > 0) {
            //加载下一页
            mAdapter.addNextPage(data);
            mIsEnd = isEnd;
        } else if (!hasRefreshHeader() || (hasRefreshHeader() && !mPtrFrame.isRefreshing())) {
            //非下拉刷新（返回页面更新数据等）
            mAdapter.setData(data);
            mIsEnd = isEnd;
        } else {
            //下拉刷新
            boolean refresh = data.size() > 0;
            if (refresh) {
                mAdapter.addItemsToTop(data);
            }
        }

        if (hasRefreshHeader() && mPtrFrame.isRefreshing()) {
            mPtrFrame.refreshComplete();
        }

        if (mIsEnd) {
            removeFooterView();
        }

        checkEmpty();
        mIsLoading = false;
    }

    protected void loadDataFail(VolleyError error) {
        DebugLog.d("load data fail error: " + error);
        if (mAdapter.getCount() == 0) {
            showEmptyView(EmptyView.STATE_FAIL);                // 加载失败,显示空页面
        } else if (!mIsRefreshing) {
            ToastUtils.showShort(getContext(), R.string.load_failed);
        }
        if (hasRefreshHeader()) {
            if (mPtrFrame.isRefreshing()) {
                mPtrFrame.refreshComplete();
                ToastUtils.showShort(getContext(), R.string.refresh_failed);
            }
        }
        mIsLoading = false;

        removeFooterView();
    }

    public void removeFooterView() {
        if (hasFooterView) {
            hasFooterView = false;
            mListView.removeFooterView(mFooterView);
        }
    }

    public void checkEmpty() {
        if (mAdapter.getCount() == 0 && getEmptyVieType() != EmptyView.STATE_NO_COMMENT) {
            DebugLog.d("show empty view");
            showEmptyView(getEmptyVieType() != 0 ? getEmptyVieType() : EmptyView.STATE_EMPTY);
        } else {
            showContentView();
            DebugLog.d("show content view");
        }
    }
}
