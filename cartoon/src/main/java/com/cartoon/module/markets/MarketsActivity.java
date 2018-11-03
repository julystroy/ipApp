package com.cartoon.module.markets;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.RefreshActivity;
import com.cartoon.data.response.MarketsListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.tab.mine.MyPackageActivity;
import com.cartoon.view.CustomItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-3.
 */
public class MarketsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    TextView       mBtRight;
    @BindView(R.id.rl_title)
    RelativeLayout mRlTitle;
    @BindView(R.id.bonus_count)
    TextView       mBonusCount;
    @BindView(R.id.recycle_view)
    RecyclerView   mRecycleView;

    private MarketsAdapter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_markets;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setUpView();
        loadData();
    }
//下载数据
    private void loadData() {
        showLoading();
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_MARKETS_LIST)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .build().execute(new BaseCallBack<MarketsListResp>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(MarketsListResp response) {
                hideLoading();
                adapter.setData(response.getList());
                adapter.notifyDataSetChanged();
                //获取灵石数
                mBonusCount.setText(response.getStone()+"");
            }

            @Override
            public MarketsListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, MarketsListResp.class);
            }
        });
    }

    private void setUpView() {
        mTvTitle.setText(R.string.markets);
        mBtRight.setText(R.string.my_package);
        mBtRight.setOnClickListener(this);
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtLeft.setOnClickListener(this);
        adapter = new MarketsAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp10), 3));
        mRecycleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                if (CartoonApp.getInstance().isLogin(MarketsActivity.this))
                    startActivity(new Intent(MarketsActivity.this, MyPackageActivity.class));
                else
                    startActivity(new Intent(MarketsActivity.this, LoginActivity.class));

                break;
        }
    }

    private MaterialDialog dialog;

    public void showLoading() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(this)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }
    public void hideLoading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed())
                return;
        }
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEvent(RefreshActivity refresh) {
        if (refresh.f) {
            loadData();
        }

    }
}
