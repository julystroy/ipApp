package com.cartoon.module.tab.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.EventRefresh;
import com.cartoon.data.Keys;
import com.cartoon.data.response.PackageListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.markets.MarketsActivity;
import com.cartoon.module.tab.adapter.MyPackageAdapter;
import com.cartoon.view.CustomItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-3.
 */
public class MyPackageActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.bt_left)
    ImageButton    mBtLeft;
    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.bt_right)
    TextView       mBtRight;

    @BindView(R.id.magic_count)
    TextView       mMagicCount;
    @BindView(R.id.bonus_count)
    TextView       mBonusCount;
    @BindView(R.id.recycle_view)
    RecyclerView   mRecycleView;

    private MyPackageAdapter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.activity_mypackage;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setUpView();
        LoadData();
    }

    private void LoadData() {

        showLoading();
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_PACKAGE_BUYITEM)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams(Keys.PAGE_NUM, String.valueOf(1))
                .addParams(Keys.PAGE_SIZE, String.valueOf(30))
                .build().execute(new BaseCallBack<PackageListResp>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(PackageListResp response) {
                hideLoading();
                adapter.setData(response.getList());
                //获取灵石数
                mBonusCount.setText(response.getStone()+"");
                mMagicCount.setText(response.getItemsNum()+"");
            }

            @Override
            public PackageListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, PackageListResp.class);
            }
        });
    }

    private void setUpView() {
        mBtLeft.setImageResource(R.mipmap.icon_back_black);
        mBtLeft.setOnClickListener(this);
        mBtRight.setOnClickListener(this);
        mTvTitle.setText(R.string.my_package);

        adapter = new MyPackageAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.addItemDecoration(new CustomItemDecoration((int) getResources().getDimension(R.dimen.dp10), 3));
        mRecycleView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.bt_right:
                startActivity(new Intent(MyPackageActivity.this, MarketsActivity.class));
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
        EventBus.getDefault().unregister(this);
        finish();
    }


    @Subscribe
    public void onEvent(EventRefresh f) {

            LoadData();

    }
}
