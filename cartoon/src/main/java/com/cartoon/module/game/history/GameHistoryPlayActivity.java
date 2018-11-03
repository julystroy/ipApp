package com.cartoon.module.game.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.cartoon.module.changxian.detail.comment.EditCommentActivity;
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
 * Created by wuchuchu on 2018/3/8.
 */

public class GameHistoryPlayActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
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


    private final static int EDIT_REQUEST = 0x101;
    private final static int DETAIL_REQUEST = 0x102;
    public final static int EDIT_RESULT = 0x104;

    public GameHistoryPlayAdapter adapter;
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

        mTvTitle.setText(R.string.my_game_history);
        mBtLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        recycleView.setVisibleThreshold(1);
        viewEmpty.setText("没有内容");

        adapter = new GameHistoryPlayAdapter(this);
        adapter.setOnClickSubViewListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);
        DebugLog.d("onCreate loadData");
        loadData(false);
    }

    @Override
    public void onRefresh() {
        Log.d("GameHistory", "onRefresh loadData(true)");
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf(v.getTag().toString());
        final PlayHistoryItemData data = adapter.getItem(position);
        if (data.state == 1) {          //已下线
            showDeleteConfirmDialog(getResources().getString(R.string.offline_tip), data, position, this);
        } else {
            switch (v.getId()) {
                case R.id.rl_content:
                    Log.d("playHistory", "click at content" + data.name + "detail view");
                    DetailActivity.launch(this, data.name, data.id, DETAIL_REQUEST);
                    break;
                case R.id.btn_play:
                    play(data);
                    break;

                case R.id.btn_bottom:
                    DebugLog.d("click at bottom" + data.name);
                    if (data.duration > 60) {
                        DebugLog.d(data.name + "试玩超过1分钟，可评分");
                        EditCommentActivity.launch(this, data.name, data.id, true, EDIT_REQUEST);
                    } else {
                        showTryPlayTipDialog(data);
                        DebugLog.d(data.name + "试玩低于1分钟，弹框进入试玩");

                    }
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
        }, new PlaySdk.OnPlayEndListener() {
            @Override
            public void onPlayEnd() {
                DebugLog.d("data:" + data + ", reloadData()");
                reloadData();
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

        get.url(StaticField.URL_CHANGXIAN_HISTORY)
                .addParams(Keys.FIRST, refresh ? adapter.getFirstPublishAt() : "0")
                .addParams(Keys.LAST, refresh ? "0" : adapter.getLastPublishAt())
                .addParams(Keys.LIMIT, String.valueOf(10))
                .addParams(Keys.PLAY_TYPE, String.valueOf(1))
                .build().execute(new Callback<PlayHistoryDataResponse>() {
            @Override
            public PlayHistoryDataResponse parseNetworkResponse(Response response, int id) throws Exception {
                String data = response.body().string().trim();
                DebugLog.d("response:" + data);
                return JSON.parseObject(data, PlayHistoryDataResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(PlayHistoryDataResponse response, int id) {
                swipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    recycleView.completeLoadMore();
                    DebugLog.d("refresh:" + refresh + ", response:" + response.toString());

                    if (refresh) {
                        if (response.data != null && response.data.items != null) {
                            adapter.addRefresh(response.data.items);
                            viewEmpty.setVisibility(View.GONE);
                        }
                    } else {
                        if (response.data != null) {
                            adapter.addAll(response.data.items);
                        } if (adapter.getItemCount() == 0) {
                            adapter.setListData(null);
                            viewEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                    setupLoadMoreList(response, refresh);
                }
            }
        });
    }

    @Override
    public void onLoadMoreData(int i) {
        loadData(false);
    }

    private void reloadData(){
        setResult(EDIT_RESULT);
        adapter.clearAll();
        recycleView.setEndLessListener(null);
        loadData(false);
    }

    private void setupLoadMoreList(PlayHistoryDataResponse response, boolean refresh) {
        if (!refresh && (response.data == null || response.data.end)) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    private void showTryPlayTipDialog(final PlayHistoryItemData data) {
        String msg = String.format(getResources().getString(R.string.evaluate_tip_time), AccountHelper.getPlayTimeNeededForRating() / 60);

        AlertDialog alert = (new AlertDialog.Builder(this))
                .setMessage(msg)
                .setPositiveButton(getResources().getString(R.string.try_play), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        play(data);
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    private void showDeleteConfirmDialog(String msg, final PlayHistoryItemData item, final int position, final Context context) {
        (new AlertDialog.Builder(this)).setMessage(msg).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteHistory(item, position, context);
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void deleteHistory(final PlayHistoryItemData item, final int position, final Context context) {

        PostFormBuilder post = OkHttpUtils.post();

        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            post.addHeader(COOKIE_KEY, cookie);
        }

        post.url(StaticField.URL_CHANGXIAN_HISTORY_DELETE)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            reloadData();
        }
    }
}
