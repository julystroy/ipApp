package com.cartoon.module.zong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.FightList;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.login.LicenseActivity;
import com.cartoon.module.tab.adapter.FightDyListAdpter;
import com.cartoon.view.CustomItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 18-1-16.
 */
public class FightInfoActivity extends BaseActivity implements View.OnClickListener, EndLessListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton    mBtRight;

    @BindView(R.id.rl_title)
    RelativeLayout      mRlTitle;
    @BindView(R.id.recycleview)
    EndLessRecyclerView mRecycleview;

    private FightDyListAdpter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_fightinfo;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
         adapter = new FightDyListAdpter(FightInfoActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FightInfoActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mRecycleview.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp3), 1));
        mRecycleview.setAdapter(adapter);
        mRecycleview.setEndLessListener(this);
        loadFightD(START_PAGE);
    }

    private void initView() {
        mBtRight.setImageResource(R.mipmap.ic_zong_ruler);
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mTvTitle.setText("宗门战动态");
        mBtLeft.setOnClickListener(this);
    }

    //宗门战动态
    private void loadFightD(final int page) {
        String sectionId="";
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
            sectionId = userLastInfo.getSectionId();
        }
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_FIGHT_D)
                .addParams("sectionId",sectionId)
                .addParams(Keys.PAGE_NUM, String.valueOf(page))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<FightList>() {
            @Override
            public void onLoadFail() {
                if (mRecycleview!=null)
                mRecycleview.completeLoadMore();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(FightList response) {
                if (response != null) {
                    if (START_PAGE==page)
                        adapter.setData(response.getList());
                    else
                        adapter.addAll(response.getList());
                }

            }

            @Override
            public FightList parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response.toString(),FightList.class);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                Intent intent = new Intent(this, LicenseActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoadMoreData(int i) {
        loadFightD(i);
    }
}
