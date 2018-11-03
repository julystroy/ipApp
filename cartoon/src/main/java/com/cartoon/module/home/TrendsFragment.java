
package com.cartoon.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cartoon.Constants;
import com.cartoon.data.Disappear;
import com.cartoon.data.Keys;
import com.cartoon.data.TrendsData;
import com.cartoon.data.response.TrendsList;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.action.ActionOneActivity;
import com.cartoon.module.bangai.BangaiDetailActivity;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.module.bangai.RecommendDetailActivity;
import com.cartoon.module.expound.JiNianDetailActivity;
import com.cartoon.module.newmodules.NewBaseActivity;
import com.cartoon.utils.EasySharePreference;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

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

public class TrendsFragment extends BaseFragment implements   EndLessListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {


    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  swipeRefreshLayout;

    private TrendsListAdapter adapter;
    private List<TrendsData>  mList;
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
        adapter = new TrendsListAdapter(mActivity);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
    }



    private void loadCartoonData(final int pageNum) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_TREABS)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<TrendsList>() {

            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onContentNull() {}
            @Override
            public void onLoadSuccess(TrendsList response) {
                if (response != null) {
                    recycleView.completeLoadMore();
                    setupLoadMoreList(response);
                    if (response.isPush()) {
                        EventBus.getDefault().post(new Disappear());
                        EasySharePreference.setPushActionId(getContext(),response.getPushId()+"#"+response.getPushType());
                    }

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
            public TrendsList parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, TrendsList.class);
            }
        });
    }

    private void setupLoadMoreList(TrendsList response) {
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
    public void onItemClick(View view, int id, int momentType) {
      //  Type:活动=0 漫画=1 听书=2 连载=3 (动态 书友圈=4 评论=5) 追听=6 次元=7 纪年=8 静态电影=9  11征文活动
        if (momentType == 8) {//纪年
            Intent intent = new Intent(mActivity, JiNianDetailActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
            startActivity(intent);
        } else if (momentType==9){
            Intent  intent = new Intent(mActivity, RecommendDetailActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
            startActivity(intent);
        }else if (momentType==0) {
            Intent intent = new Intent(mActivity, NewBaseActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
            intent.putExtra(Keys.COMMENT_TYPE, momentType);
            startActivity(intent);
        } else if (momentType == 7) {//次元
            Intent intent = new Intent(mActivity, BangaiDetailActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
            intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_QMAN);
            //  BangaiReadedUtil.getInstance().addReadedId(adapter.getItem(p3).getModule_id());
            startActivity(intent);
        } else if (momentType == 11) {
            Intent intent = new Intent(getContext(), ActionOneActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
            startActivity(intent);
        }else if (momentType == 3) {
            Intent intent = new Intent(getContext(), NovelDetailActivity.class);
            intent.putExtra(Keys.TARGET_ID, id+"");
            intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
            intent.putExtra(Keys.URL_TYPE, 0);
            intent.putExtra("isRead", "0");
            startActivity(intent);
        }
        else {
            ToastUtils.showShort(getContext(),"内容被清理");
        }

    }
}
