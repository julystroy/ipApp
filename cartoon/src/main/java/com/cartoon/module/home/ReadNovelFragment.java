
package com.cartoon.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.Constants;
import com.cartoon.common.CommonPopDialog;
import com.cartoon.data.Cartoon;
import com.cartoon.data.Keys;
import com.cartoon.data.response.CartoonListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.bangai.NovelDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

public class ReadNovelFragment extends BaseFragment implements  OnItemClickListener,
        View.OnClickListener, EndLessListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_desc)
    TextView            tvDesc;
    @BindView(R.id.tv_anthology)
    TextView            tvAnthology;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  swipeRefreshLayout;


    private CommonPopDialog dialog;
    private List<Cartoon>   mList;
    private ReadListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fg_cartoon;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mList = new ArrayList<>();
        tvAnthology.setVisibility(View.GONE);
        tvAnthology.setOnClickListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        adapter = new ReadListAdapter(getActivity());
        adapter.setListener(this);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
    }



    private void loadCartoonData(final int pageNum) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_BANGAI_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams(Keys.SORT_ORDER, sort_order)
                .build().execute(new BaseCallBack<CartoonListResp>() {

            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(CartoonListResp response) {
                recycleView.completeLoadMore();
                mList = response.getList();
                tvDesc.setText("共" + response.getTotalRow() + "章");
                setupLoadMoreList(response);

                if (pageNum == START_PAGE) {
                    adapter.setData(response.getList());
                } else {
                    adapter.addAll(response.getList());
                }
            }

            @Override
            public CartoonListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, CartoonListResp.class);
            }
        });
    }

    private void setupLoadMoreList(CartoonListResp response) {
        swipeRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    @Override
    public void onLoadMoreData(int i) {
        loadCartoonData(i);
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadCartoonData(START_PAGE);
    }

    @Override
    public void onRefresh() {
        loadCartoonData(START_PAGE);
    }

    @Override
    public void onItemClick(View view, int id,int type) {
        Intent intent = new Intent(getContext(), NovelDetailActivity.class);
        intent.putExtra(Keys.TARGET_ID, id+"");
        intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
        intent.putExtra(Keys.URL_TYPE, 0);
        intent.putExtra("isRead", "0");
        startActivity(intent);
    }
}
