
package com.cartoon.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.data.response.NewBaseListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.bangai.RecommendDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * tui  jian
 * <p/>
 *
 */

public class RecommendFragment extends BaseFragment implements   EndLessListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {


    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  swipeRefreshLayout;

    private RecommendListAdapter adapter;
    private List<NewBase>        mList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fg_recommend;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mList = new ArrayList<>();
        adapter = new RecommendListAdapter(mActivity);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
    }



    private void loadCartoonData(final int pageNum) {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_STATICFILM_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<NewBaseListResp>() {

            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onContentNull() {}
            @Override
            public void onLoadSuccess(NewBaseListResp response) {
                if (response != null) {
                    recycleView.completeLoadMore();
                    setupLoadMoreList(response);

                    if (pageNum == START_PAGE) {
                        mList.clear();
                        if (response.getList()!=null)
                        mList.addAll(response.getList());
                        adapter.setData(mList);
                    } else {
                        mList.addAll(response.getList());
                        adapter.setData(mList);
                    }
                }

            }
            @Override
            public NewBaseListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, NewBaseListResp.class);
            }
        });
    }

    private void setupLoadMoreList(NewBaseListResp response) {
        swipeRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    @Override
    public void onLoadMoreData(int i) {loadCartoonData(i);}


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadCartoonData(START_PAGE);
    }

    @Override
    public void onRefresh() {loadCartoonData(START_PAGE);}


    @Override
    public void onItemClick(View view, int id, int type) {
        String tag = (String) view.getTag();

        Intent intent =null;
        intent = new Intent(mActivity, RecommendDetailActivity.class);
        intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
       // intent.putExtra(Keys.TARGET_OBJECT, newbase);
        if ("comment".equals(tag)) {
            intent.putExtra(Keys.SHOW_KEYBOARD, true);
        }

        startActivity(intent);
    }
}
