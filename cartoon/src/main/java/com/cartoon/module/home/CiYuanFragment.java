package com.cartoon.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.cartoon.Constants;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.data.response.NewBaseListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.bangai.BangaiDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 16-12-2.
 */
public class CiYuanFragment extends BaseFragment implements OnItemClickListener, EndLessListener, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout  swipeRefreshLayout;


    public  List<NewBase> mList;

    private CiYuanListAdapter adapter;


   // File    downLoadPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/BangaiDL/");

    //      Qman
    @Override
    protected int getLayoutId() {
        return R.layout.fg_bangai;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mList = new ArrayList<>();
        adapter = new CiYuanListAdapter(getActivity());
        adapter.setListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);

    }



    private void loadExpoundData(final int pageNum) {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_QMAN_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
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
            //    tvDesc.setText("共" + response.getTotalRow() + "话");
                setupLoadMoreList(response);

                if (pageNum == START_PAGE) {
                    if (mList != null) {

                        mList.clear();
                    }

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

   /* @Override
    public void onPause() {
        super.onPause();
        tvDownload.setVisibility(View.VISIBLE);
    }*/

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
    public void onItemClick(View view, int id,int tpe) {
        String tag = (String) view.getTag();
        Intent intent = new Intent(getContext(), BangaiDetailActivity.class);
        intent.putExtra(Keys.TARGET_ID, String.valueOf(id));
        intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_QMAN);
        if ("comment".equals(tag)) {
            intent.putExtra(Keys.SHOW_KEYBOARD, true);
        }
        startActivity(intent);
    }

    @Override
    public void onLoadMoreData(int i) {
        loadExpoundData(i);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        loadExpoundData(START_PAGE);
    }

    @Override
    public void onRefresh() {
        loadExpoundData(START_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
