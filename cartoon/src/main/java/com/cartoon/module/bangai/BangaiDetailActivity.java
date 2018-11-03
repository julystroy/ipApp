package com.cartoon.module.bangai;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.bean.Bangai;
import com.cartoon.common.AddCommentActivity;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.response.CartoonCommentListResp;
import com.cartoon.data.response.CommentData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.builder.GetBuilder;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * 嘻说详情页
 * <p/>
 * Created by David on 16/7/24.
 */
public class BangaiDetailActivity extends BaseActivity implements View.OnClickListener,
        BangaiDetailAdapter.OnItemCommentListener, EndLessListener, BangaiDetailAdapter.OnItemCommentTypeChange/*, TextWatcher*/ {

    public static final int TYPE_HOT    = 0x101;
    public static final int TYPE_LATEST = 0x102;
    private final static int REQUESTCODE = 0x103; // 返回的结果码

    @BindView(R.id.bt_left)
    ImageButton         btLeft;
    @BindView(R.id.tv_title)
    TextView            tvTitle;
    @BindView(R.id.bt_right)
    ImageButton         btRight;

    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.et_input)
    TextView            etInput;
    @BindView(R.id.iv_share)
    ImageView           ivShare;
    @BindView(R.id.tv_like)
    ImageView           tvLike;
    @BindView(R.id.tv_comment_num)
    TextView            tvNum;

    @BindView(R.id.ll_bangai_activity)
    LinearLayout        llBangai;
    @BindView(R.id.ll_like)
    RelativeLayout      llLike;




    private Bangai expound;
    private String target_id;
    private  boolean booleanExtra;
    /**
     * 被评论的评论id,uid
     */
    private String to_uid;
    private String comment_id;

    /**
     * 事件类型
     */
    private int event_type;
    /**
     * 排序类型
     */
    private int sort_type = TYPE_LATEST;

    private BangaiDetailAdapter adapter;


    @Override
    protected int getContentViewId() {
        return R.layout.ac_bangai_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ivShare.setVisibility(View.GONE);
        target_id = getIntent().getStringExtra(Keys.TARGET_ID);
        event_type = getIntent().getIntExtra(Keys.COMMENT_TYPE, -1);
        if (target_id != null) {
            loadExpoundDetail(target_id);
        } else {
            loadCommentList(START_PAGE, TYPE_LATEST);
        }
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setImageResource(R.mipmap.icon_share);
        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);

        ivShare.setOnClickListener(this);
        tvLike.setOnClickListener(this);



        adapter = new BangaiDetailAdapter(this, BangaiDetailActivity.this);
        adapter.setListener(this);
        adapter.setChangeListener(this);
        adapter.setOnClickReadListener(this);
        adapter.setOnClickSubViewListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
        etInput.setOnClickListener(this);
    }


    private void setupExpoundView() {

        //        tvTitle.setText(expound.getTitle());
        tvNum.setText(expound.getApprove_num());
        if (expound.getIs_approve() == 1) {
            tvLike.setSelected(true);
            //tvNum.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            tvLike.setSelected(false);
            //tvNum.setTextColor(Color.parseColor("#929292"));
        }
    }

    @Override
    public void OnItemCommentTypeChange(int type) {
        switch (type) {
            case TYPE_LATEST:
                sort_type = TYPE_LATEST;
                loadCommentList(START_PAGE, TYPE_LATEST);
                break;
            case TYPE_HOT:
                sort_type = TYPE_HOT;
                loadCommentList(START_PAGE, TYPE_HOT);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                finish();
                break;

            case  R.id.bt_right:
                if (!CartoonApp.getInstance().isLogin(BangaiDetailActivity.this)) {
                    return;
                }
                if (expound!=null)
                ApiUtils.share(BangaiDetailActivity.this, expound.getTitle(), Utils.addBaseUrlShare(expound.getCover_pic()), "凡人次元", String.valueOf(expound.getId()));
                else
                    ToastUtils.showShort(BangaiDetailActivity.this,"数据加载中");
                break;
            case R.id.tv_like:
                ApiUtils.approveTarget(target_id, event_type, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        tvLike.setSelected(true);
                       // tvNum.setTextColor(Color.parseColor("#ef5f11"));

                        int count = 0;
                        try {
                            count = Integer.parseInt(tvNum.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        count++;
                        tvNum.setText(String.valueOf(count));

                    }
                });
                break;


            case R.id.et_input:
                if (CartoonApp.getInstance().isLogin(BangaiDetailActivity.this)) {
                    if (target_id != null) {
                        Intent intent = new Intent(this, AddCommentActivity.class);
                        intent.putExtra(Constants.RESOURCEID, target_id + "");
                        intent.putExtra("type", "7");
                        startActivityForResult(intent, REQUESTCODE);
                    } else{
                        ToastUtils.showShort(BangaiDetailActivity.this,"加载中...");
                    }
                }
                break;
            case R.id.id_photo:
                int positionMoment = (int) v.getTag(R.id.position_book_friend_moment);
                int positionPhoto = (int) v.getTag(R.id.position_photo);

                startActivity(new Intent(BangaiDetailActivity.this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                Log.d("onclick", "positionMoment=" + positionMoment + " positionPhoto=" + positionPhoto);
                break;
            case R.id.iv_cover:
                int p = (int) v.getTag(R.id.position_book_friend_moment);


                startActivity(new Intent(BangaiDetailActivity.this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(p).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, 0));
                break;
        }
    }


    @Override
    public void onLoadMoreData(int i) {
        loadCommentList(i, sort_type);
    }

    @Override
    public void onItemCommentClick(CommentData comment, int position) {
        if (!CartoonApp.getInstance().isLogin(BangaiDetailActivity.this))
            return;


        Intent intent = new Intent(this, CartoonCommentDetailActivity.class);
        intent.putExtra(Keys.COMMENT_ID, comment.getId());
        intent.putExtra(Keys.SOURCE, "番外");
        intent.putExtra(Keys.SHOW_KEYBOARD, true);

        startActivity(intent);


    }

    @Override
    public void onItemClick(CommentData comment, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.COMMENT_ID, comment.getId()+"");
        bundle.putString(Keys.SOURCE, "番外");


        startActivity(new Intent(this, CartoonCommentDetailActivity.class).putExtras(bundle));

    }

    @Override
    public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position) {
        if (!CartoonApp.getInstance().isLogin(BangaiDetailActivity.this))
            return;

        if (child.getUid().equals(CartoonApp.getInstance().getUserId())) {
            return;
        }
        NicknameColorHelper.setNicknameColor(etInput, child.getUid());

        this.comment_id = comment_id;
        this.to_uid = child.getUid();
        event_type = Constants.APPROVE_COMMENT;
    }

    private void loadExpoundDetail(String id) {
        showLoading();
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_QMAN_CONTENT)
                .addParams("id", id)
                .build().execute(new BaseCallBack<Bangai>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Bangai response) {
                hideLoading();

                expound = response;
                adapter.setExpound(expound);
                loadCommentList(START_PAGE, TYPE_LATEST);
                setupExpoundView();
                // adapter.notifyDataSetChanged();
            }

            @Override
            public Bangai parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Bangai.class);
            }
        });
    }

    /**
     * 加载评论列表
     *
     * @param sort_type 排序类型
     *                  <p/>
     *                  final int TYPE_HOT = 0x101;
     *                  final int TYPE_LATEST = 0x102;
     */
    private void loadCommentList(final int pageNum, int sort_type) {//// FIXME: 16-12-8

        GetBuilder builder = BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_COMMENT_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("target_id", target_id)
                .addParams("type",  "7");

        if (sort_type == TYPE_HOT) {
            builder.addParams("sort_name", "hot");
        } else {
            builder.addParams("sort_name", "new");
        }

        builder.build().execute(new BaseCallBack<CartoonCommentListResp>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(CartoonCommentListResp response) {

                recycleView.completeLoadMore();
                setupLoadMoreList(response);
                //                tvDesc.setText("共" + response.getTotalRow() + "条评论");

                if (response!=null&&response.getList()!=null&&response.getList().size()>0) {
                    if (pageNum == START_PAGE) {
                        adapter.setData(response.getList(), String.valueOf(response.getTotalRow()));
                    } else {
                        adapter.addAll(response.getList(), String.valueOf(response.getTotalRow()));
                    }
                }


                DebugLog.i("...response.." + response.toString());
            }

            @Override
            public CartoonCommentListResp parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, CartoonCommentListResp.class);
            }
        });
    }

    private void setupLoadMoreList(CartoonCommentListResp response) {
        if (CollectionUtils.isEmpty(response.getList())
                || response.getList().size() < PAGE_SIZE) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
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
       // EventBus.getDefault().unregister(this);
    }



    //umeng分享，回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                recycleView.setStartPageIndex(START_PAGE);
                loadCommentList(START_PAGE, sort_type);
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }
}