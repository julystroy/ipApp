package com.cartoon.module.tab.rank;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Charts;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.ChartsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-11-7.
 */
public class SelfRankFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycle_view)
    EndLessRecyclerView mRecycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  mSwipeRefreshLayout;
    private ChartsAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fg_sectrank;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(this);

        adapter = new ChartsAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
       // mRecycleView.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleView.setAdapter(adapter);
        loadData();
    }
    private void loadData() {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_CHARTS_USE)
                .build().execute(new BaseCallBack<List<Charts>>() {

            @Override
            public void onLoadFail() {

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(List<Charts> response) {

                if (mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
                adapter.setData(response);


            }

            @Override
            public List<Charts> parseNetworkResponse(String response) throws Exception {
                return JSON.parseArray(response, Charts.class);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }



}
