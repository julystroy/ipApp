package com.cartoon.module.tab.rank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Keys;
import com.cartoon.data.response.SectListRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.SectRankListAdpter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-11-7.
 */
public class SectRankFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, EndLessListener {


    @BindView(R.id.recycle_view)
    EndLessRecyclerView mRecycleview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  mSwipeRefreshLayout;
    private SectRankListAdpter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_sectrank;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new SectRankListAdpter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.setAdapter(adapter);
        mRecycleview.setEndLessListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        loadSect(START_PAGE);
    }

    private void loadSect(final int page) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTLIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(page))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<SectListRes>() {
            @Override
            public void onLoadFail() {
                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(SectListRes response) {

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);

                setupLoadMoreListener(response);
                if (response != null) {
                    if (page==START_PAGE)
                    adapter.setData(response.getList());
                    else
                        adapter.addData(response.getList());

                }

                mRecycleview.completeLoadMore();


            }
            @Override
            public SectListRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,SectListRes.class);
            }
        });
    }
    private void setupLoadMoreListener(SectListRes listResp) {
        if (listResp ==null||CollectionUtils.isEmpty(listResp.getList())&&listResp.getList().size()<10) {
            mRecycleview.setEndLessListener(null);
        } else {
            mRecycleview.setEndLessListener(this);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private boolean refresh = false;
    private int count;
    @Override
    public void onRefresh() {
        refresh = true;
        count = 1;
        loadSect(START_PAGE);
    }

    @Override
    public void onLoadMoreData(int i) {
        if (refresh) {
            loadSect(count++);
        }else
        loadSect(i);
    }
}
