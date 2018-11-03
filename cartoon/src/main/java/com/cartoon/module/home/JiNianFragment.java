package com.cartoon.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Expound;
import com.cartoon.data.Keys;
import com.cartoon.data.response.ExpoundListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.expound.JiNianDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 *番外列表  type 8
 * <p/>
 * Created by David on 16/6/5.
 */
public class JiNianFragment extends BaseFragment implements OnItemClickListener, EndLessListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progressBar)
    ProgressBar pb;
    private List<Expound>     mList;
    private JiNianListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_expound;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mList = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);
        pb.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setEndLessListener(this);
        adapter = new JiNianListAdapter(getActivity());
        recycleView.setAdapter(adapter);
        adapter.setListener(this);
        loadExpoundData(START_PAGE);
    }



    private void loadExpoundData(final int pageNum) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_JINIAN_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams(Keys.SORT_ORDER, sort_order)
                .build().execute(new BaseCallBack<ExpoundListResp>() {

            @Override
            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onContentNull() {
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onLoadSuccess(ExpoundListResp response) {
                pb.setVisibility(View.GONE);
                recycleView.completeLoadMore();
                setupLoadMoreList(response);

                if (pageNum == START_PAGE) {
                    mList.clear();
                    mList.addAll(response.getList());
                    adapter.setData(mList);
                } else {
                    adapter.addAll(response.getList());
                }

            }

            @Override
            public ExpoundListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, ExpoundListResp.class);
            }
        });
    }

    private void setupLoadMoreList(ExpoundListResp response) {
        swipeRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    @Override
    public void onItemClick(View view, int id,int type) {
       // Expound expound = mList.get(position);
        Intent intent = new Intent(getContext(), JiNianDetailActivity.class);
        intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
        startActivity(intent);
    }

    @Override
    public void onLoadMoreData(int i) {
        loadExpoundData(i);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onRefresh() {
        loadExpoundData(START_PAGE);
    }

}
