package com.cartoon.module.tab.bookfriendmoment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.BookFriendMomentListResp;
import com.cartoon.module.BaseFragment;
import com.cartoon.module.action.ActionDetailActivity;
import com.cartoon.module.bangai.BangaiDetailActivity;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.module.bangai.RecommendDetailActivity;
import com.cartoon.module.cartoon.CartoonBookDetailActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.expound.JiNianDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.newmodules.NewBaseActivity;
import com.cartoon.utils.CollectionUtils;
import com.cartoon.view.SendPopWindow;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartoon.view.dialog.ReportDialog;
import com.cartton.library.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * 书友圈
 * <p/>
 * Created by David on 16/6/5.
 */
public class BookFragment extends BaseFragment implements BookFriendMomentView,
        SwipeRefreshLayout.OnRefreshListener, EndLessListener, View.OnClickListener {

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



    private BookFriendMomentPresenter presenter;
    public  BookFriendMomentAdapter adapter;
    private static final int START_PAGE = 1;
    private static final int PAGE_SIZE = 10;
    private String sort = "new";

    public  boolean mySelfOnly;
    private MaterialDialog dialog;

   // private AnimationDrawable animationDrawable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //     EventBus.getDefault().register(this);

        presenter = new BookFriendMomentPresenterImpl(this);
        if (getArguments() != null)
            mySelfOnly = getArguments().getBoolean(Keys.MOMENT_MYSELF_ONLY);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fg_book_friend_moment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        swipeRefreshLayout.setOnRefreshListener(this);
        recycleView.setVisibleThreshold(1);
        viewEmpty.setText("没有内容");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(this.getClass().getSimpleName(), "onViewCreated");
        presenter.loadBookFriendMomentList(START_PAGE, PAGE_SIZE, sort, mySelfOnly);

    }

    @OnClick(R.id.btn_reload)
    void onClickReload() {
        presenter.loadBookFriendMomentList(START_PAGE, PAGE_SIZE, sort, mySelfOnly);
    }

    @Override
    public void renderList(BookFriendMomentListResp listResp) {
        adapter = new BookFriendMomentAdapter(listResp.getList(),getActivity());
        adapter.setOnClickSubViewListener(this);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setAdapter(adapter);
        setupLoadMoreListener(listResp);
    }

    private void setupLoadMoreListener(BookFriendMomentListResp listResp) {
        if (!listResp.isLastPage() && !CollectionUtils.isEmpty(listResp.getList())) {
            recycleView.setEndLessListener(this);
        } else {
            recycleView.setEndLessListener(null);
        }
    }

    @Override
    public void renderListMore(BookFriendMomentListResp listResp) {
        setupLoadMoreListener(listResp);
        adapter.addAll(listResp.getList());
    }

    @Override
    public void changeLikeStatusAndCount(BookFriendMoment bookFriendMoment, int position) {
        if (bookFriendMoment.getIs_approve() == 1) {
            bookFriendMoment.setIs_approve(0);
            bookFriendMoment.setApprove_num(bookFriendMoment.getApprove_num() - 1);
        } else {
            bookFriendMoment.setIs_approve(1);
            bookFriendMoment.setApprove_num(bookFriendMoment.getApprove_num() + 1);
        }
        adapter.notifyItemChanged(position);
    }

    @Override
    public void showErrorForDoLike(String msg) {
        ToastUtils.showLong(getContext(), msg);
    }

    @Override
    public void showLoadingForDeleteMoment() {
        if (dialog == null)
            dialog = new MaterialDialog.Builder(getContext())
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();

        dialog.show();
    }

    @Override
    public void hideLoadingForDeleteMoment() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void successForDeleteMoment(int position) {
        adapter.deleteItem(position);
        ToastUtils.showLong(getContext(), getString(R.string.success_delete));
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

    private void hideSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        recycleView.setStartPageIndex(START_PAGE);
        presenter.loadBookFriendMomentList(START_PAGE, PAGE_SIZE, sort, mySelfOnly);
    }

    @Override
    public void onLoadMoreData(int i) {
//        if (count != i) {
//            i=count;
//        }
            presenter.loadBookFriendMomentList(i, PAGE_SIZE, sort, mySelfOnly);
    }

    private boolean isAutoRefreshRunning = false;
   /* @Override
    public void onPause() {
        super.onPause();
        //开启延时任务自动获取下一次活动更新
        if (null != getView() && !isAutoRefreshRunning) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                    isAutoRefreshRunning = false;
                }
            }, 6000);
            isAutoRefreshRunning = true;

        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_item: {
                int po = (int) v.getTag();
                BookFriendMoment m = adapter.getItem(po);
                int type = m.getType();
                int targetId = m.getId();
                int toUid = m.getUid();
                Bundle bundle = new Bundle();
                bundle.putString(Keys.COMMENT_ID, String.valueOf(targetId));
                if (type == 4) {
                    bundle.putInt(Keys.APPROVE_TYPE, type);
                }
                bundle.putInt(Keys.MOMENT_POSITION, po);
                bundle.putSerializable(Keys.MOMENT, m);
                bundle.putBoolean(Keys.SHOW_KEYBOARD, false);

                if (type == 10) {
                    startActivity(new Intent(getContext(), CartoonBookDetailActivity.class).putExtras(bundle));
                } else {

                    startActivity(new Intent(getContext(), CartoonCommentDetailActivity.class).putExtras(bundle));
                }

            }

            break;
            case R.id.tv_content:
            case R.id.tv_comment:
                int po = (int) v.getTag();
                BookFriendMoment m = adapter.getItem(po);
                int type = m.getType();
                int targetId = m.getId();
                int toUid = m.getUid();
                Bundle bundle = new Bundle();
                bundle.putString(Keys.COMMENT_ID, String.valueOf(targetId));
                if (type == 4) {
                    bundle.putInt(Keys.APPROVE_TYPE, type);
                }
                bundle.putInt(Keys.MOMENT_POSITION, po);
                bundle.putSerializable(Keys.MOMENT, m);
                bundle.putBoolean(Keys.SHOW_KEYBOARD, true);
//                bundle.putInt(Keys.COMMENT_TYPE, type);
                if (type == 10) {
                    startActivity(new Intent(getContext(), CartoonBookDetailActivity.class).putExtras(bundle));
                } else {

                    startActivity(new Intent(getContext(), CartoonCommentDetailActivity.class).putExtras(bundle));
                }

                break;
            case R.id.tv_like:
                if (!isUserAlreadyLogin()) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }

                    int position = (int) v.getTag();
                    BookFriendMoment moment = adapter.getItem(position);
                    presenter.likeThisMoment(moment, position);

                break;

            case R.id.id_photo:
                int positionMoment = (int) v.getTag(R.id.position_book_friend_moment);
                int positionPhoto = (int) v.getTag(R.id.position_photo);

                startActivity(new Intent(getContext(), PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                Log.d("onclick", "positionMoment=" + positionMoment + " positionPhoto=" + positionPhoto);
                break;
            case R.id.iv_cover:
                int p = (int) v.getTag(R.id.position_book_friend_moment);


                startActivity(new Intent(getContext(), PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(p).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, 0));
                break;
            case R.id.bt_left:
                if (mySelfOnly) {
                    getActivity().finish();
                }
                break;
            case R.id.bt_right:
                SendPopWindow pw = new SendPopWindow(getContext());
                pw.showPopupWindow(v);

                break;
            case R.id.tv_delete:
                if (isUserAlreadyLogin()) {

                    final int p2 = (int) v.getTag();

                    DeleteDialog deleteDialog = new DeleteDialog(mActivity,"删除");
                    deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                        @Override
                        public void onButtonClickListener() {
                            BookFriendMoment moment = adapter.getItem(p2);
                            if (moment.getType()!=11){
                                presenter.deleteMoment(moment.getId(), p2);
                                adapter.notifyDataSetChanged();
                            }else{
                                ToastUtils.showShort(getContext(),"请到活动征文中删除");
                            }

                        }
                    });
                    deleteDialog.show();
                    }

                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                // TODO: 7/24/16 delete bookFriendMoment
                break;
            case R.id.iv_delete:
                if (isUserAlreadyLogin()) {
                    final int p2 = (int) v.getTag();
                    final BookFriendMoment moment2 = adapter.getItem(p2);
                    DeleteDialog deleteDialog = new DeleteDialog(mActivity,"举报");
                    deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                        @Override
                        public void onButtonClickListener() {
                            ReportDialog dialog1 = new ReportDialog(getContext(),moment2.getUid()+"",moment2.getId()+"","1");// FIXME: 17-3-30 对应id是否对
                            dialog1.show();
                        }
                    });
                    deleteDialog.show();

                }
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.ll_cartoon:
                final int p3 = (int) v.getTag();
//                 Type:活动=0 漫画=1 听书=2 连载=3 (动态 书友圈=4 评论=5) 追听=6 次元=7 纪年=8 静态电影=9
                int momentType = adapter.getItem(p3).getType();
                 if (momentType == 8) {//纪年

                    Intent intent = new Intent(mActivity, JiNianDetailActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TARGET_OBJECT, "");
                   // intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_BANGAI);
                    startActivity(intent);
                } else if (momentType==9){
                   Intent  intent = new Intent(mActivity, RecommendDetailActivity.class);
                     intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                     startActivity(intent);
                 }else if (momentType==0) {
                    Intent intent = new Intent(mActivity, NewBaseActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TARGET_OBJECT, "");
                    intent.putExtra(Keys.COMMENT_TYPE, adapter.getItem(p3).getType());
                    startActivity(intent);
                 } else if (momentType == 7) {//次元
                     Intent intent = new Intent(mActivity, BangaiDetailActivity.class);
                     intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                     intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_QMAN);
                   //  BangaiReadedUtil.getInstance().addReadedId(adapter.getItem(p3).getModule_id());
                     startActivity(intent);
                 } else if (momentType == 11) {
                     Intent intent = new Intent(getContext(), ActionDetailActivity.class);
                     intent.putExtra(Keys.CHART, adapter.getItem(p3).getModule_id()+"");
                     intent.putExtra(Keys.CHART_USEID, adapter.getItem(p3).getUid()+"");
                   //  intent.putExtra(Keys.CHART_BOOLEAN, adapter.getItem(p3).getCanVote()+"");
                   //  EasySharePreference.setActionId(getContext(),adapter.getItem(p3).getActivityId());
                     startActivity(intent);
                 }else if (momentType == 3) {
                     Intent intent = new Intent(getContext(), NovelDetailActivity.class);
                     intent.putExtra(Keys.TARGET_ID, adapter.getItem(p3).getModule_id()+"");
                     intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
                     intent.putExtra(Keys.URL_TYPE, 0);
                     intent.putExtra("isRead", "0");
                     startActivity(intent);
                 }
                 else {
                     ToastUtils.showShort(getContext(),"内容被清理");
                 }
                break;
        }
    }

    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }


  /*  @Subscribe
    public void onEvent(EventMomentItemChanged item) {
        if (null != item.moment && null != adapter && adapter.getItemCount() > 0) {
            adapter.getItem(item.position).setApprove_num(item.moment.getApprove_num());
            adapter.getItem(item.position).setIs_approve(item.moment.getIs_approve());
            adapter.getItem(item.position).setComment_num(item.moment.getComment_num());

            adapter.notifyItemChanged(item.position);
        }
    }*/
}
