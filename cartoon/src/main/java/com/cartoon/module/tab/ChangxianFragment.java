
package com.cartoon.module.tab;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Keys;
import com.cartoon.data.response.FeaturedItem;
import com.cartoon.data.response.FeaturedPaginListResponse;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.changxian.detail.DetailActivity;
import com.cartton.library.utils.DebugLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

public class ChangxianFragment extends BaseFragment implements EndLessListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    @BindView(R.id.bt_left)
    ImageButton btLeft;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.bt_right)
    ImageButton btRight;

    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChangxianListAdapter adapter;
    private List<FeaturedItem>   mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fg_changxian;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tvTitle.setText(R.string.tab_changxian);
        btLeft.setVisibility(View.GONE);
        btRight.setVisibility(View.GONE);

        mList = new ArrayList<>();
        adapter = new ChangxianListAdapter(mActivity);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
    }

    private void loadData(final boolean refresh) {
        OkHttpUtils.get().url(StaticField.URL_CHANGXIAN)
                .addParams(Keys.FIRST, refresh ? adapter.getFirstPublishAt() : "0")
                .addParams(Keys.LAST, refresh ? "0" : adapter.getLastPublishAt())
                .addParams(Keys.LIMIT, String.valueOf(3))
                .addParams(Keys.RECOMMEND, String.valueOf(1))
                .build().execute(new Callback<FeaturedPaginListResponse>() {
            @Override
            public FeaturedPaginListResponse parseNetworkResponse(Response response, int id) throws Exception {
                String data = response.body().string().trim();
                DebugLog.d("parseNetworkResponse. data = " + data);
                return JSON.parseObject(data, FeaturedPaginListResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                DebugLog.e("onError. e = " + e.toString());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(FeaturedPaginListResponse response, int id) {
                if (response != null) {
                    recycleView.completeLoadMore();
                    setupLoadMoreList(response, refresh);

                    if (refresh) {
                        if (response.data.items != null)
                            mList.addAll(0, response.data.items);
                        adapter.setData(mList);
                    } else {
                        mList.addAll(response.data.items);
                        adapter.setData(mList);
                    }
                }
            }
        });
    }

    private void setupLoadMoreList(FeaturedPaginListResponse response, boolean refresh) {
        swipeRefreshLayout.setRefreshing(false);
        if (!refresh && (response.data == null || response.data.end)) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    @Override
    public void onLoadMoreData(int page) {
        DebugLog.d("onLoadMoreData. page = " + page);
        loadData(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
        loadData(true);
    }

    @Override
    public void onRefresh() {
        DebugLog.d("onRefresh.");
        loadData(true);
    }

    @Override
    public void onItemClick(View view, int pos, int type) {
        FeaturedItem item = mList.get(pos);
        DetailActivity.launch((Activity) getContext(), item.name, item.id);
    }
}
