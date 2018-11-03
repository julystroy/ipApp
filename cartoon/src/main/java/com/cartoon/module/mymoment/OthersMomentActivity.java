package com.cartoon.module.mymoment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.BookFriendMomentListResp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.bangai.BangaiDetailActivity;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.module.bangai.RecommendDetailActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.expound.JiNianDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.newmodules.NewBaseActivity;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartton.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 16-11-30.
 */
public class OthersMomentActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, EndLessListener {
    @BindView(R.id.bt_left)
    ImageButton         mBtLeft;
    @BindView(R.id.tv_title)
    TextView            mTvTitle;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView mRecycleView;
   /* @BindView(R.id.progressBar)
    ProgressBar         mProgressBar;*/
    /*@BindView(R.id.flipper)
    ViewFlipper         mFlipper;*/

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private OthersMomentAdapter adapter;
    private List<BookFriendMoment> mlist = new ArrayList<>();
    private String uid;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_othersmoment_layout;
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
        setUpData();
    }

    private void setUpData() {
       // mProgressBar.setVisibility(View.VISIBLE);
        uid = getIntent().getStringExtra(Keys.TARGET_UID);
        String nickName = getIntent().getStringExtra(Keys.TARGET_OTHERS);
        mBtLeft.setOnClickListener(this);
        mTvTitle.setText(nickName);//传进来的参数配置
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new OthersMomentAdapter(OthersMomentActivity.this);
        adapter.setOnClickSubViewListener(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);
        loadData(START_PAGE);

    }

    private void loadData(final int pageNum) {

        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_APP_MY_MOMENT)
                .addParams("uid", uid)
                .addParams("myid", CartoonApp.getInstance().getUserId())
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams(Keys.SORT_ORDER, sort_order)
                .build().execute(new BaseCallBack<BookFriendMomentListResp>() {

            @Override
            public void onLoadFail() {
               // mProgressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(BookFriendMomentListResp response) {

               // mProgressBar.setVisibility(View.GONE);
                if (response != null) {
                    mRecycleView.completeLoadMore();
                    setupLoadMoreList(response);

                    if (pageNum == START_PAGE) {
                        mlist.clear();
                        if (response.getList()!=null)
                            mlist.addAll(response.getList());
                        adapter.setData(mlist);
                    } else {
                        mlist.addAll(response.getList());
                        adapter.setData(mlist);
                    }
                }


              /*  mlist.addAll(response.getList());
                adapter.setData(mlist);*/
            }

            @Override
            public BookFriendMomentListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, BookFriendMomentListResp.class);
            }

        });
    }

    private void setupLoadMoreList(BookFriendMomentListResp response) {
        swipeRefreshLayout.setRefreshing(false);
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            mRecycleView.setEndLessListener(null);
        } else {
            mRecycleView.setEndLessListener(this);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                onBackPressed();
                break;
            case R.id.rl_item:
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
                //                bundle.putInt(Keys.COMMENT_TYPE, type);
                startActivity(new Intent(this, CartoonCommentDetailActivity.class).putExtras(bundle));
                break;
            case R.id.tv_comment:
                int pos = (int) v.getTag();
                BookFriendMoment mo = adapter.getItem(pos);
                int type2 = mo.getType();
                int targetId2 = mo.getId();
                int toUid2 = mo.getUid();
                Bundle bundle2 = new Bundle();
                bundle2.putString(Keys.COMMENT_ID, String.valueOf(targetId2));
                if (type2 == 4) {
                    bundle2.putInt(Keys.APPROVE_TYPE, type2);
                }
                bundle2.putInt(Keys.MOMENT_POSITION, pos);
                bundle2.putSerializable(Keys.MOMENT, mo);
                bundle2.putBoolean(Keys.SHOW_KEYBOARD, true);
                //                bundle.putInt(Keys.COMMENT_TYPE, type);
                startActivity(new Intent(this, CartoonCommentDetailActivity.class).putExtras(bundle2));
                break;
            case R.id.tv_like:
                if (!isUserAlreadyLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                final int position = (int) v.getTag();
                final BookFriendMoment moment = adapter.getItem(position);


                if (moment.getIs_approve() == 1) {
                    showErrorForDoLike("已赞");
                    return;
                }
                changeLikeStatusAndCount(moment, position);

                int type5 = 5; // 评论非书友圈的类型
                if (moment.getType() == 4) { // 4==书友圈
                    type5 = 4;
                }


                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_APPROVE)
                        .addParams("target_id", String.valueOf(moment.getId()))
                        .addParams("type", String.valueOf(type5))
                        .build().execute(new BaseCallBack<String>() {

                    @Override
                    public void onLoadFail() {
                        changeLikeStatusAndCount(moment, position);
                        showErrorForDoLike(getMessage());
                    }

                    @Override
                    public void onContentNull() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        Log.d("onLoadSuccess", "response=" + response);
                    }

                    @Override
                    public String parseNetworkResponse(String response) throws Exception {
                        return response;
                    }
                });
                break;
            case R.id.id_photo:
                int positionMoment = (int) v.getTag(R.id.position_book_friend_moment);
                int positionPhoto = (int) v.getTag(R.id.position_photo);

                startActivity(new Intent(this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                break;
            case R.id.iv_cover:
                int p = (int) v.getTag(R.id.position_book_friend_moment);


                startActivity(new Intent(this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(p).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, 0));
                break;
            case R.id.ll_cartoon:
                final int p3 = (int) v.getTag();
                //               Type:活动=0 漫画=1 听书=2 连载=3 (动态 书友圈=4 评论=5) 追听=6 次元=7 纪年=8 静态电影=9
                int momentType = adapter.getItem(p3).getType();
                if (momentType == 1) {
                  /*  Intent intent = new Intent(this, ReaderActivity.class);
                    intent.putExtra(Keys.CARTOON_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TITLE_ID, adapter.getItem(p3).getModule_title());
                    startActivity(intent);*/
                } else if (momentType == 2) {
//                    Intent intent = new Intent(this, ListenerDetailActivity.class);
//                    intent.putExtra(Keys.LISTENER_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
//                    startActivity(intent);
                } else if (momentType == 8) {
                    Intent intent = new Intent(this, JiNianDetailActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TARGET_OBJECT, "");
                    startActivity(intent);
                } else if ( momentType == 0) {

                    Intent intent = new Intent(this, NewBaseActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TARGET_OBJECT, "");
                    intent.putExtra(Keys.COMMENT_TYPE, adapter.getItem(p3).getType());
                    startActivity(intent);
                } else if (momentType == 7) {

                    Intent intent = new Intent(this, BangaiDetailActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.TARGET_OBJECT1, "");
                    intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_QMAN);
                    startActivity(intent);
                } else if (momentType == 3) {
                    Intent intent = new Intent(this, NovelDetailActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
                    intent.putExtra(Keys.URL_TYPE, 0);
                    intent.putExtra("isRead", "0");
                    startActivity(intent);
                } else if (momentType==9){
                    Intent  intent = new Intent(OthersMomentActivity.this, RecommendDetailActivity.class);
                    intent.putExtra(Keys.TARGET_ID, String.valueOf(adapter.getItem(p3).getModule_id()));
                    startActivity(intent);
                } else {
                    ToastUtils.showShort(this,"内容已经被清理");
                }
                break;
            case R.id.tv_delete:
                final int p2 = (int) v.getTag();

                DeleteDialog deleteDialog = new DeleteDialog(this,"删除");
                deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                    @Override
                    public void onButtonClickListener() {
                        BookFriendMoment moment = adapter.getItem(p2);
                        deleteMoment(moment.getId(), p2);

                    }
                });
                deleteDialog.show();

        }
    }

    public void deleteMoment(int id, final int position) {

        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", String.valueOf(id))
                .addParams("is_two", String.valueOf("1"))//// FIXME: 16-11-28 1
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {

            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(String response) {
                //adapter.deleteItem(position);
                mlist.remove(position);

                adapter.notifyDataSetChanged();
                ToastUtils.showLong(OthersMomentActivity.this, getString(R.string.success_delete));
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });

    }

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

    public void showErrorForDoLike(String msg) {
        ToastUtils.showLong(this, msg);
    }

    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onRefresh() {
        loadData(START_PAGE);
    }

    @Override
    public void onLoadMoreData(int i) {
        loadData(i);
    }
}
