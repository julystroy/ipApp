package com.cartoon.module.game.history;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.cartoon.module.BaseFragment;
import com.cartoon.module.changxian.detail.DetailActivity;
import com.cartoon.module.changxian.detail.comment.EditCommentActivity;
import com.cartoon.sdk.PlaySdk;
import com.cartoon.utils.UserDB;
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
 * Created by wuchuchu on 2018/3/2.
 */

public class GameHistoryPlayFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        EndLessListener, View.OnClickListener {
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

    public GameHistoryPlayAdapter adapter;
    private int playType;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        swipeRefreshLayout.setOnRefreshListener(this);
        recycleView.setVisibleThreshold(1);
        viewEmpty.setText("没有内容");

        adapter = new GameHistoryPlayAdapter(getActivity());
        adapter.setOnClickSubViewListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);
        loadData(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fg_mgh_play;
    }

    @Override
    public void onRefresh() {
        recycleView.setStartPageIndex(START_PAGE);
        loadData(true);
    }

    @Override
    public void onClick(View v) {
        int position = Integer.valueOf(v.getTag().toString());
        final PlayHistoryItemData data = adapter.getItem(position);
        if (data.state == 1) {          //已下线
            showDeleteConfirmDialog( getContext().getResources().getString(R.string.offline_tip), data, position);
        } else {
            switch (v.getId()) {
                case R.id.rl_content:
                    Log.d("playHistory", "click at content" + data.name + "detail view");
                    DetailActivity.launch((Activity) getContext(), data.name, data.id);
                    break;
                case R.id.btn_play:
                    play(data);
                    break;

                case R.id.btn_bottom:
                    Log.d("playHistory", "click at bottom" + data.name);
                    if (data.duration > 60) {
                        Log.d("playHistory", data.name + "试玩超过1分钟，可评分");
                        EditCommentActivity.launch((Activity)getContext(), data.name, data.id, true);
                    } else {
                        showTryPlayTipDialog(data);
                        Log.d("playHistory", data.name + "试玩低于1分钟，弹框进入试玩");

                    }
                    break;
            }
        }
    }

    private void play(final PlayHistoryItemData data) {
        LaunchInfo launchInfo = data.changeToLaunchInfo();
        Log.d("historyPlay", "data:" + data + ", change to launch info:" + launchInfo.toString());
        PlaySdk.getInstance().launch(getContext(), (int) data.id, launchInfo, new PlaySdk.OnDownloadListener() {
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

    private void showTryPlayTipDialog(final PlayHistoryItemData data) {
        String msg = String.format(getContext().getResources().getString(R.string.evaluate_tip_time), AccountHelper.getPlayTimeNeededForRating() / 60);

        AlertDialog alert = (new AlertDialog.Builder(getContext()))
                .setMessage(msg)
                .setPositiveButton(getContext().getResources().getString(R.string.try_play), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        play(data);
                    }
                }).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    public void setHistoryType(int type) {
        playType = type;
        Log.d("MineGame", "play type:" + playType);
    }

    private static final String COOKIE_KEY = "Cookie";
    private void loadData(final boolean refresh) {

        Log.d("MineGame", "load data, refresh:" + refresh + ", play type:" + playType);
        GetBuilder get = OkHttpUtils.get();

        String cookie = AccountHelper.getCookie();
        if (!TextUtils.isEmpty(cookie)) {
            get.addHeader(COOKIE_KEY, cookie);
        }

        get.url(StaticField.URL_CHANGXIAN_HISTORY)
                .addParams(Keys.FIRST, refresh ? adapter.getFirstPublishAt() : "0")
                .addParams(Keys.LAST, refresh ? "0" : adapter.getLastPublishAt())
                .addParams(Keys.LIMIT, String.valueOf(10))
                .addParams(Keys.PLAY_TYPE, String.valueOf(playType))
                .build().execute(new Callback<PlayHistoryDataResponse>() {
            @Override
            public PlayHistoryDataResponse parseNetworkResponse(Response response, int id) throws Exception {
                String data = response.body().string().trim();
                Log.d("GameDownloadActivity", "response:" + data);
                return JSON.parseObject(data, PlayHistoryDataResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
//                if (DEBUG) Log.e("TAG", "onError. e = " + e.toString());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(PlayHistoryDataResponse response, int id) {
                if (response != null) {
                    recycleView.completeLoadMore();
                    setupLoadMoreList(response, refresh);
                    Log.d("GameDownloadActivity", "refresh:" + refresh + ", response:" + response.toString());

                    if (refresh) {
                        if (response.data != null && response.data.items != null) {

                            adapter.addRefresh(response.data.items);
                            viewEmpty.setVisibility(View.GONE);
                        }
                        else if (adapter.getItemCount() == 0){
                            adapter.setListData(null);
                            viewEmpty.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (response.data != null) {
                            adapter.addAll(response.data.items);
                        }

                    }
                }
            }
        });
    }

    private void setupLoadMoreList(PlayHistoryDataResponse response, boolean refresh) {
        swipeRefreshLayout.setRefreshing(false);
        if (!refresh && (response.data == null || response.data.end)) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    @Override
    public void onLoadMoreData(int i) {
        loadData(false);
    }

    private void showDeleteConfirmDialog(String msg, final PlayHistoryItemData item, final int position) {
        (new AlertDialog.Builder(getContext())).setMessage(msg).setPositiveButton(getContext().getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (item.playType == 2) {
//                    if (!LaunchingActivity.doUninstall(item.quickInfo.packageName)) {
//                        ToastUtils.showLong(getContext(), getString(R.string.delete_fail));
//                    } else {
                        deleteHistory(item, position);
//                    }
                }else if (item.playType == 1) {
                    deleteHistory(item, position);
                }
            }
        }).setNegativeButton(getContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    private void deleteHistory(final PlayHistoryItemData item, final int position) {

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
                Log.d("GameDownloadActivity", "delete network response");
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("GameDownloadActivity", "delete network error");
                ToastUtils.showLong(getContext(), getString(R.string.delete_fail));
            }

            @Override
            public void onResponse(Object response, int id) {
                Log.d("GameDownloadActivity", "delete network on response");
                adapter.deleteItem(position);
                ToastUtils.showLong(getContext(), getString(R.string.success_delete));
            }
        });
    }
}
