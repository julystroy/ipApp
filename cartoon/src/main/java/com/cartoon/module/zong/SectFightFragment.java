package com.cartoon.module.zong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.Keys;
import com.cartoon.data.RateData;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.EventFight;
import com.cartoon.data.response.FightListRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.tab.adapter.SectFightListAdpter;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.StringUtils;
import com.cartoon.view.CustomItemDecoration;
import com.cartoon.view.dialog.ResultDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 18-1-11.
 */
public class SectFightFragment extends BaseFragment implements View.OnClickListener, EndLessListener {
    @BindView(R.id.recycleview)
    EndLessRecyclerView mRecycleview;

    @BindView(R.id.progressBar)
    ProgressBar         mProgressBar;



    private SectFightListAdpter adapter;
    private String sectionId="";
    private boolean first =false;
    @Override
    protected int getLayoutId() {
        return R.layout.fg_fight_sect;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new SectFightListAdpter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleview.setAdapter(adapter);
        adapter.setOnClickSubViewListener(this);
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
            sectionId = userLastInfo.getSectionId();
        }
        //清楚战绩
        long sectQuestionTime = EasySharePreference.getSectQuestionTime(getContext());
        if (sectQuestionTime != 0) {
            String nowSystemTime = StringUtils.getNowSystemTime(sectQuestionTime);
            if (!nowSystemTime.equals(StringUtils.getNowSystemTime(System.currentTimeMillis()))) {
                clearSP();
            }
        }

        first = true;
    }

    private void clearSP() {
        EasySharePreference.setFightQuestion(getContext(),null);
        EasySharePreference.setSectHaveAnswered(getContext(),false);
        EasySharePreference.setSectQuestionTime(getContext(),0);
        EasySharePreference.setSectPositionNum(getContext(),0);
    }




    private void correctRate() {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_FIGHT_RATE)
                .addParams("sectionId",sectionId)
                .build().execute(new BaseCallBack<RateData>() {
            @Override
            public void onLoadFail() {
            }
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(RateData response) {
                if (response != null && response.getIsWin()!=null) {
                    boolean b = response.getIsWin().equals("1") || response.getIsWin().equals("2");
                    if (b) {
                        clearSP();
                        ResultDialog dialog = new ResultDialog(getContext(),response);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                }
            }
            @Override
            public RateData parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,RateData.class);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadFightL(START_PAGE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (first) {
                correctRate();
                first = !first;
            }
        }
    }

    private int count ;
    //宗门站列表
    private void loadFightL(final int page) {
        if (mProgressBar!=null&&mProgressBar.getVisibility()==View.GONE)
        mProgressBar.setVisibility(View.VISIBLE);
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_FIGHT_L)
                .addParams(Keys.PAGE_NUM, String.valueOf(page))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("sectionId",sectionId)
                .build().execute(new BaseCallBack<FightListRes>() {
            @Override
            public void onLoadFail() {
                if (mProgressBar!=null&&mProgressBar.getVisibility()==View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(FightListRes response) {
                if (mProgressBar!=null&&mProgressBar.getVisibility()==View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);
                count = response.getTotalPage();
                if (response != null) {
                    if (START_PAGE == page) {
                        adapter.setData(response);
                    }
                    else
                        adapter.addData(response);

                }
                mRecycleview.completeLoadMore();
                setupLoadMoreListener(response);
            }

            @Override
            public FightListRes parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response.toString(),FightListRes.class);
            }
        });

    }

    private void setupLoadMoreListener(FightListRes listResp) {
        if (listResp ==null|| CollectionUtils.isEmpty(listResp.getList())&&listResp.getList().size()<10) {
            mRecycleview.setEndLessListener(null);
        } else {
            mRecycleview.setEndLessListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.marquee_layout:
                startActivity(new Intent(getContext(),FightInfoActivity.class));
                break;
        }
    }

    int page =1;
    @Override
    public void onLoadMoreData(int i) {
        if (i<=count) {
            loadFightL(i);
        }else
        loadFightL(page++);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getActivity().finish();
    }

    @Subscribe
    public void onEvent(EventFight t){
        loadFightL(START_PAGE);
    }
}
