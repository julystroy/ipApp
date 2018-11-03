package com.cartoon.module.newmodules;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSON;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.data.response.NewBaseListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.action.ActionOneActivity;
import com.cartoon.utils.EasySharePreference;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by debuggerx on 16-11-11.
 */
public class NewBaseFragment extends BaseFragment implements OnItemClickListener, EndLessListener, SwipeRefreshLayout.OnRefreshListener {

    private NewBasePopDialog dialog;

    @BindView(R.id.bt_right)
    ImageButton   btRight;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<NewBase>      mList;
    private NewBaseListAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fg_newbase;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mList = new ArrayList<>();
        adapter = new NewBaseListAdapter(this);
        adapter.setListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
        btRight.setVisibility(View.GONE);

    }

    private void loadNewBaseData(final int pageNum) {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_ACTIVITY_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams(Keys.SORT_ORDER, sort_order)
                .build().execute(new BaseCallBack<NewBaseListResp>() {

            @Override
            public void onLoadFail() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(NewBaseListResp response) {
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
    public void onItemClick(View view, int id,int type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "book");
        map.put("title", "活动");
        MobclickAgent.onEvent(getContext(), "newbase", map);


        String tag = (String) view.getTag();
     //   NewBase newbase = mList.get(position);
        Intent intent =null;
        if (type == 1) {
             intent = new Intent(mActivity, ActionOneActivity.class);
        } else {
             intent = new Intent(mActivity, NewBaseActivity.class);
        }

        EasySharePreference.setActionId(getContext(),String.valueOf(id));//记录进入活动的id
        intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
       // intent.putExtra(Keys.TARGET_OBJECT, newbase);
        if ("comment".equals(tag)) {
            intent.putExtra(Keys.SHOW_KEYBOARD, true);
        }

        startActivity(intent);
    }

    @Override
    public void onLoadMoreData(int i) {
        loadNewBaseData(i);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadNewBaseData(START_PAGE);
    }


    @Override
    public void onRefresh() {
        loadNewBaseData(START_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
