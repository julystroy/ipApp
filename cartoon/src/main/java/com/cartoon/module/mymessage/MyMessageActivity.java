package com.cartoon.module.mymessage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.data.EventNewMessage;
import com.cartoon.data.MyMessage;
import com.cartoon.data.response.MyMessageResp;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.CollectionUtils;
import com.cartoon.utils.EasySharePreference;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class MyMessageActivity extends BaseActivity implements MyMessageView, EndLessListener, SwipeRefreshLayout.OnRefreshListener, MyMessageAdapter.CallBack {
    @BindView(R.id.bt_left)
    ImageButton btLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.bt_right)
    ImageButton btRight;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ll_loading_wrap)
    LinearLayout llLoadingWrap;
    @BindView(R.id.btn_reload)
    Button btnReload;
    @BindView(R.id.ll_reload_wrap)
    LinearLayout llReloadWrap;
    @BindView(R.id.view_empty)
    TextView viewEmpty;
    @BindView(R.id.flipper)
    ViewFlipper flipper;

    private MyMessagePresenter presenter;
    private static final int START_PAGE = 1;
    private static final int PAGE_SIZE = 10;
    MyMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView();

        presenter = new MyMessagePresenterImpl(this);
        presenter.loadMyMessage(START_PAGE, PAGE_SIZE);
    }

    private void setupView() {
        recycleView.setVisibleThreshold(1);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvTitle.setText("我的消息");
        btRight.setVisibility(View.GONE);
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.my_message;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    public void renderList(MyMessageResp resp) {
        EasySharePreference.setHaveNoNewMessage(this);
        EasySharePreference.setMyMessageCount(this,0);
        EventBus.getDefault().post(new EventNewMessage(false));

        adapter = new MyMessageAdapter(resp.getList(),MyMessageActivity.this);
        adapter.setCallBack(this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(adapter);

        setupLoadMoreListener(resp);
    }

    @Override
    public void renderListMore(MyMessageResp resp) {
        setupLoadMoreListener(resp);
        adapter.addAll(resp.getList());
    }

    @Override
    public void showDeleteWaitDialog() {
        showLoadingDialog();
    }

    @Override
    public void hideDeleteWaitDialog() {
        hideLoadingDialog();
    }

    @Override
    public void successDeleteMessage(int position) {
        if (null != adapter) adapter.removeItem(position);
        ToastUtils.showLong(this, getString(R.string.success_delete));
    }

    private void setupLoadMoreListener(MyMessageResp listResp) {
        if (!listResp.isLastPage() && !CollectionUtils.isEmpty(listResp.getList())) {
            recycleView.setEndLessListener(this);
        } else {
            recycleView.setEndLessListener(null);
        }
    }


    @OnClick(R.id.btn_reload)
    void onClickReload() {
        presenter.loadMyMessage(START_PAGE, PAGE_SIZE);
    }

    @Override
    public void showLoadDataView() {
        showView(llLoadingWrap);
    }

    @Override
    public void hideLoadDataView() {
        showView(swipeRefreshLayout);
    }

    @Override
    public void showLoadMoreFailedView() {
        recycleView.showRetryView();
    }

    @Override
    public void showErrorView() {
        showView(llReloadWrap);
    }

    @Override
    public void shoeEmptyView() {
        showView(viewEmpty);
    }

    private void showView(View view) {
        hideSwipeRefresh();
        recycleView.completeLoadMore();
        flipper.setDisplayedChild(flipper.indexOfChild(view));
    }

    private void  hideSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreData(int i) {
        presenter.loadMyMessage(i, PAGE_SIZE);

    }

    @Override
    public void onRefresh() {
        recycleView.setStartPageIndex(START_PAGE);
        presenter.loadMyMessage(START_PAGE, PAGE_SIZE);

    }

    @Override
    public void onItemLongClick(MyMessage message, int position) {
        int type = message.getType();
        //"type":" 1 "//:0.系统消息 1.评论   2.点赞

        if (type == 0) {
        } else {
            showDeleteDialog(message, position);
        }
    }

    private void showDeleteDialog(final MyMessage message, final int position) {
        new MaterialDialog.Builder(this).title("提示").content("确定要删除吗?")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        presenter.deleteMyMessage(message.getId(), position);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                }).show();
    }
}
