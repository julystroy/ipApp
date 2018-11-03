package com.cartoon.module.game.favor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.Keys;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.data.game.PlayHistoryItemData;
import com.cartoon.data.response.PlayHistoryDataResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.changxian.detail.DetailActivity;
import com.cartoon.sdk.PlaySdk;
import com.cartoon.utils.UserDB;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cn.idianyun.streaming.data.LaunchInfo;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wuchuchu on 2018/3/5.
 */

public class GameFavorActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, EndLessListener {
    @BindView(R.id.bt_left)
    ImageButton mBtLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_reload)
    Button btnReload;
    @BindView(R.id.ll_reload_wrap)
    LinearLayout llReloadWrap;
    @BindView(R.id.flipper)
    ViewFlipper flipper;
    @BindView(R.id.ll_loading_wrap)
    LinearLayout llLoadingWrap;
    @BindView(R.id.view_empty)
    TextView viewEmpty;

    public GameFavorAdapter adapter;
    @Override
    protected int getContentViewId() {
        return R.layout.ac_game_favor;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTvTitle.setText(R.string.my_game_favor);
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        recycleView.setVisibleThreshold(1);
        viewEmpty.setText("没有内容");

        adapter = new GameFavorAdapter(this);
        adapter.setOnClickSubViewListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        loadData(false);
    }

    @Override
    public void onRefresh() {
        DebugLog.d("onRefresh.");
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf(v.getTag().toString());
        PlayHistoryItemData data = adapter.getItem(position);
        if (data.state == 1) {          //已下线
            showDeleteConfirmDialog(getResources().getString(R.string.offline_tip), data, position, this);
        } else {
            switch (v.getId()) {
                case R.id.rl_content:
                    DebugLog.d("click at content" + data.name + "detail view");
                    DetailActivity.launch(this, data.name, data.id);
                    break;
                case R.id.btn_play:
                    play(data);
//                    if (data.playType == 1) {
//                        Log.d("playHistory", "直接玩" + data.name);
//                    } else {
//                        Log.d("playHistory", "边下边玩" + data.name);
//                    }

                    break;

                case R.id.btn_bottom:
                    showDeleteConfirmDialog(getResources().getString(R.string.delete_favor), data, position, this);
                    break;
            }
        }
    }

    private void play(final PlayHistoryItemData data) {
        LaunchInfo launchInfo = data.changeToLaunchInfo();
        DebugLog.d("data:" + data + ", change to launch info:" + launchInfo.toString());
        PlaySdk.getInstance().launch(this, (int) data.id, launchInfo, new PlaySdk.OnDownloadListener() {
            @Override
            public void onDownload(String url, String pkgName) {
                DownloadGame record = UserDB.getInstance().getGameDownload(url);
                if (record != null) {
                    record.setPackageName(pkgName);
                    record.setCxId(data.id);
                    record.setAppName(data.name);
                    record.setIconUrl(data.logo);
                    UserDB.getInstance().saveGameDownload(record);
                }
            }
        });
    }

    private static final String COOKIE_KEY = "Cookie";
    private void loadData(final boolean refresh) {
        DebugLog.d("load data, refresh:" + refresh);

        GetBuilder get = OkHttpUtils.get();

        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            get.addHeader(COOKIE_KEY, cookie);
        }

        get.url(StaticField.URL_CHANGXIAN_FAVORITES)
                .addHeader(COOKIE_KEY, cookie)
                .addParams(Keys.FIRST, refresh ? adapter.getFirstPublishAt() : "0")
                .addParams(Keys.LAST, refresh ? "0" : adapter.getLastPublishAt())
                .addParams(Keys.LIMIT, String.valueOf(10))
                .build().execute(new Callback<PlayHistoryDataResponse>() {
            @Override
            public PlayHistoryDataResponse parseNetworkResponse(Response response, int id) throws Exception {
                String data = response.body().string().trim();

                DebugLog.d("response:" + data);
                return JSON.parseObject(data, PlayHistoryDataResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                DebugLog.e("onError. e = " + e.toString());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(PlayHistoryDataResponse response, int id) {
                DebugLog.e("onResponse. response = " + response);
                if (response != null) {
                    recycleView.completeLoadMore();
                    setupLoadMoreList(response, refresh);
                    DebugLog.d("refresh:" + refresh + ", response:" + response.toString());

                    if (refresh) {
                        if (response.data != null && response.data.items != null) {
                            adapter.addRefresh(response.data.items);
                            viewEmpty.setVisibility(View.GONE);
                        }
                    } else {
                        if (response != null && response.data != null && response.data.items.size() > 0) {
                            adapter.addAll(response.data.items);
                        }
                        if (adapter.getItemCount() == 0){
                            adapter.setListData(null);
                            viewEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onLoadMoreData(int i) {
        DebugLog.d("onLoadMoreData. i =" + i);
        loadData(false);
    }

    private void setupLoadMoreList(PlayHistoryDataResponse response, boolean refresh) {
        swipeRefreshLayout.setRefreshing(false);
        DebugLog.d("setupLoadMoreList:" + refresh + ", isEnd:" + response.data.end + ", count:" + response.data.items.size());
        if (!refresh && (response.data == null || response.data.end)) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    private void showDeleteConfirmDialog(String msg, final PlayHistoryItemData item, final int position, final Context context) {
        (new AlertDialog.Builder(this)).setMessage(msg).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (item.playType == 2) {
//                    if (!LaunchingActivity.doUninstall(item.quickInfo.packageName)) {
//                        ToastUtils.showLong(getContext(), getString(R.string.delete_fail));
//                    } else {
                    deleteFavor(item, position, context);
//                    }
                }else if (item.playType == 1) {
                    deleteFavor(item, position, context);
                }
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }


    private void deleteFavor(final PlayHistoryItemData item, final int position, final Context context) {
        PostFormBuilder post = OkHttpUtils.post();

        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            post.addHeader(COOKIE_KEY, cookie);
        }
        post.url(StaticField.URL_CHANGXIAN_FAVORITES_DELETE)
                .addParams(Keys.APP_ID, String.valueOf(item.id))
                .addParams(Keys.PLAY_TYPE, String.valueOf(item.playType))
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                DebugLog.d("delete network response");
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                DebugLog.d("delete network error");
                ToastUtils.showLong(context, getString(R.string.delete_fail));
            }

            @Override
            public void onResponse(Object response, int id) {
                DebugLog.d("delete network on response");
                adapter.deleteItem(position);
                ToastUtils.showLong(context, getString(R.string.success_delete));
            }
        });
    }
}
