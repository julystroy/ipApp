package com.cartoon.module.action;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.ActionRank;
import com.cartoon.data.EventPause;
import com.cartoon.data.Keys;
import com.cartoon.data.RankHot;
import com.cartoon.data.response.ActionRankRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.ActionRankAdapter;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-8-22.
 */
public class RankFragment extends BaseFragment implements EndLessListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rl_title)
    RelativeLayout      mRlTitle;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  swipeRefreshLayout;
    @BindView(R.id.ll_action)
    LinearLayout        mLlAction;

    @BindView(R.id.tv_rank_desc)
    TextView            tvRankDesc;
    @BindView(R.id.tv_rank)
    TextView            tvRank;
    @BindView(R.id.bt_left)
    ImageButton         mBtLeft;
    @BindView(R.id.tv_title)
    TextView            mTvTitle;
//    @BindView(R.id.tv_rank_num)
//    TextView            mTvRankNum;
    @BindView(R.id.ll_rank)
    LinearLayout        mLlRank;
    @BindView(R.id.bt_join)
    TextView            mBtJoin;
    @BindView(R.id.bt_show)
    TextView            mBtShow;
    private List<ActionRank>  mList;
    private ActionRankAdapter adapter;
    private int myEssayId;
    private boolean isJoin = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_charts;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRlTitle.setVisibility(View.GONE);
        

        mList = new ArrayList<>();
        LoadRank(START_PAGE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        adapter = new ActionRankAdapter(getContext(),"hot");
        recycleView.setAdapter(adapter);

        mBtJoin.setOnClickListener(this);
        mBtShow.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void LoadRank(final int pageNum) {
        String userId = CartoonApp.getInstance().getUserId();
        String actionId = EasySharePreference.getActionId(getContext());
        if (actionId==null)  return;

        if (userId == null)
            userId = "";
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_ACTION_RANK)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("sort_name", "hot")
                .addParams("uid", userId)
                .addParams("activityId", actionId)
                .build().execute(new BaseCallBack<ActionRankRes>() {

            @Override
            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(ActionRankRes response) {
                if (response == null)
                    return;
                else {
                     myEssayId = response.getMyEssayId();

                    if (response.getEndTime() != null &&  Utils.formatItemOverTime(response.getEndTime()) <= 0) {
                        EventBus.getDefault().post(new EventPause());//去掉参加活动的图标
                        isJoin =true;
                        mBtJoin.setText("活动已结束");
                    }
                    if (response.getMyVote() != -1 || response.getRank() != -1) {
                        EventBus.getDefault().post(new EventPause());//去掉参加活动的图标
                        mLlRank.setVisibility(View.VISIBLE);
                        mBtShow.setVisibility(View.VISIBLE);
                        mBtJoin.setVisibility(View.GONE);
                        tvRank.setText("" + response.getRank());
                      //  mTvRankNum.setText("" + response.getMyVote());
                        tvRankDesc.setVisibility(View.GONE);
                    } else {
                        tvRankDesc.setVisibility(View.VISIBLE);
                        mBtJoin.setVisibility(View.VISIBLE);
                        mBtShow.setVisibility(View.GONE);
                        mLlRank.setVisibility(View.GONE);
                    }
                }
                recycleView.completeLoadMore();
                //    tvDesc.setText("共" + response.getTotalRow() + "话");
                setupLoadMoreList(response);
                if (pageNum == START_PAGE) {
                    if (mList != null) {
                        mList.clear();
                    }
                    mList.addAll(response.getList());
                    adapter.setData(mList,response.getCanVote());
                } else {
                    mList.addAll(response.getList());
                    adapter.setData(mList,response.getCanVote());
                }

            }

            @Override
            public ActionRankRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, ActionRankRes.class);
            }
        });
    }


    private void setupLoadMoreList(ActionRankRes response) {
        swipeRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }


    @Override//可见时刷新UI
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
            onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onLoadMoreData(int i) {
        LoadRank(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:
                if (CartoonApp.getInstance().isLogin(getContext())&&!isJoin)
                    startActivity(new Intent(getContext(), ActionNoteActivity.class));
                break;
            case R.id.bt_show:
                if (CartoonApp.getInstance().isLogin(getContext())) {
                    Intent intent = new Intent(getContext(), ActionDetailActivity.class);
                    intent.putExtra(Keys.CHART, myEssayId+"");
                    intent.putExtra(Keys.CHART_USEID, CartoonApp.getInstance().getUserId()+"");
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void onRefresh() {
        LoadRank(START_PAGE);
    }


    @Subscribe
    public void onEvent(RankHot d) {
        onRefresh();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
