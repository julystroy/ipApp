package com.cartoon.module.zong;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.MineSectData;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.MineSectRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.SectEventListAdpter;
import com.cartoon.view.CustomItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-9-25.
 */
public class EventSectFragment extends BaseFragment implements EndLessListener {
    @BindView(R.id.recycle_view)
    EndLessRecyclerView mRecycleview;

   private List<MineSectData> list ;
   private List<MineSectData> list2;
   private List<MineSectData> list3;
    private SectEventListAdpter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fg_sect_event;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        list = new ArrayList<>();
        list2 = new ArrayList<>();;
        list3 = new ArrayList<>();;

        adapter = new SectEventListAdpter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleview.setAdapter(adapter);
        mRecycleview.setEndLessListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadNews();
    }

    private void loadNews() {
        String sectionId="";
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
             sectionId = userLastInfo.getSectionId();
        }

        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTAPPLYLIST)
                .addParams("sectionId",sectionId)
                .addParams("pageSize", "100")
                .build().execute(new BaseCallBack<MineSectRes>() {
            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(MineSectRes response) {
                    list2.addAll(response.getList());
                loadHistoryNews(START_PAGE);
            }

            @Override
            public MineSectRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,MineSectRes.class);
            }
        });
    }

    private boolean refresh =false;
    private int count ;
    private void loadHistoryNews(final int page) {
        String sectionId="";
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
            sectionId = userLastInfo.getSectionId();
        }
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTAPPLY_HISTORY)
                .addParams("sectionId",sectionId)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize","10")
                .build().execute(new BaseCallBack<MineSectRes>() {
            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(MineSectRes response) {
                if (page == START_PAGE) {
                    count = response.getTotalPage();
                    if (list2 != null && list2.size() != 0) {
                        list.addAll(list2);
                    }
                    list.addAll(response.getList());
                    adapter.setData(list);
                } else {
                    adapter.addAll(response.getList());
                }

                setupLoadMoreListener(response);
                mRecycleview.completeLoadMore();

            }

            @Override
            public MineSectRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,MineSectRes.class);
            }
        });
    }

    private void setupLoadMoreListener(MineSectRes listResp) {
        if (listResp ==null|| CollectionUtils.isEmpty(listResp.getList())&&listResp.getList().size()<10) {
            mRecycleview.setEndLessListener(null);
        } else {
            mRecycleview.setEndLessListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }



    int page = 1;
    @Override
    public void onLoadMoreData(int i) {
        if (i<=count) {
            loadHistoryNews(i);
        }else
        loadHistoryNews(page++);
    }


}
