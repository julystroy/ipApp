package com.cartoon.module.expound;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
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
import com.cartoon.common.AddCommentActivity;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Expound;
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
 * 番外详情页  纪年
 * <p/>
 * Created by David on 16/7/24.
 */
public class JiNianDetailActivity extends BaseActivity implements View.OnClickListener,
        JiNianDetailAdapter.OnItemCommentListener, EndLessListener, JiNianDetailAdapter.OnItemCommentTypeChange {
//ViewTreeObserver.OnGlobalLayoutListener,
    public static final int TYPE_HOT    = 0x101;
    public static final int TYPE_LATEST = 0x102;
    private final static int REQUESTCODE = 0x103; // 返回的结果码

    @BindView(R.id.bt_left)
    ImageButton         btLeft;
    @BindView(R.id.tv_title)
    TextView            tvTitle;
    @BindView(R.id.bt_right)
    ImageButton         btRight;
    //    @BindView(R.id.tv_desc)
    //    TextView tvDesc;
    //    @BindView(R.id.tv_latest)
    //    TextView tvHot;
    //    @BindView(R.id.tv_hotest)
    //    TextView tvNew;
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

    @BindView(R.id.ll_expound_detail)
    LinearLayout        llExpound;
    @BindView(R.id.ll_like)
    RelativeLayout      llLike;

    private String  target_id;
    /**
     * 被评论的评论id,uid
     */
    private String  to_uid;
    private String  comment_id;
    private Expound expound;

    /**
     * 事件类型
     */
    private int event_type;
    /**
     * 排序类型
     */
    private int sort_type = TYPE_LATEST;

    private JiNianDetailAdapter adapter;


    @Override
    protected int getContentViewId() {
        return R.layout.ac_expound_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        WebView obj = new WebView(this);
        //        obj.clearCache(true);


        expound = getIntent().getParcelableExtra(Keys.TARGET_OBJECT);
        target_id = getIntent().getStringExtra(Keys.TARGET_ID);
     //   event_type = getIntent().getIntExtra(Keys.COMMENT_TYPE, -1);

        if (expound != null) {
            setupExpoundView();
            loadCommentList(START_PAGE);
        } else {
            loadExpoundDetail(target_id);
        }


        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setImageResource(R.mipmap.icon_share);

        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
        //        tvHot.setOnClickListener(this);
        //        tvNew.setOnClickListener(this);
        ivShare.setVisibility(View.GONE);
        tvLike.setOnClickListener(this);
       // llExpound.getViewTreeObserver().addOnGlobalLayoutListener(this);
//llExpound.getViewTreeObserver().addOnScrollChangedListener(this);
        adapter = new JiNianDetailAdapter(this, JiNianDetailActivity.this,target_id);
       // adapter.setExpound(expound);
        adapter.setListener(this);
        adapter.setChangeListener(this);
        adapter.setOnClickSubViewListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);

       etInput.setOnClickListener(this);

    }

    @Override//也可以看监听音量键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
            adapter.stopAudio();
        return super.onKeyDown(keyCode, event);
    }

    private void setupExpoundView() {
       // tvTitle.setText(expound.getTitle());
        //        tvTitle.setText(expound.getTitle());
        if (expound.getApproveNum() != null) {
            expound.setApprove_num(expound.getApproveNum());
            expound.setIs_approve(expound.getIsLiked()==null?0:Integer.parseInt(expound.getIsLiked()));
        }
        tvNum.setText(expound.getApprove_num());
        if (expound.getIs_approve() == 1) {
            tvLike.setSelected(true);
        } else {
            tvLike.setSelected(false);
        }
    }

    @Override
    public void OnItemCommentTypeChange(int type) {
        switch (type) {
//            case TYPE_LATEST:
//                sort_type = TYPE_LATEST;
//                //                tvHot.setSelected(true);
//                //                tvNew.setSelected(false);
//                loadCommentList(START_PAGE, TYPE_LATEST);
//                break;
//            case TYPE_HOT:
//                sort_type = TYPE_HOT;
//                //                tvHot.setSelected(false);
//                //                tvNew.setSelected(true);
//                loadCommentList(START_PAGE, TYPE_HOT);
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                adapter.stopAudio();
               onBackPressed();
                break;
            case R.id.bt_right:
                if (!CartoonApp.getInstance().isLogin(JiNianDetailActivity.this)) {
                    return;
                }
                Utils.closeSoftKeyboard(etInput, this);
              ApiUtils.share(this, expound.getTitle(), expound.getCover_pic(), "凡人纪年", String.valueOf(expound.getId()));
                break;

            case R.id.tv_like:
                ApiUtils.approveTarget(target_id, Constants.APPROVE_BANGAI, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        tvLike.setSelected(true);
                      //  tvNum.setTextColor(Color.parseColor("#ef5f11"));

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
                if (CartoonApp.getInstance().isLogin(JiNianDetailActivity.this)) {
                    if (target_id != null) {

                        Intent intent = new Intent(this, AddCommentActivity.class);
                        intent.putExtra(Constants.RESOURCEID,target_id+"");
                        intent.putExtra("type",Constants.APPROVE_BANGAI+"");
                        startActivityForResult(intent,REQUESTCODE);
                    }else{
                        ToastUtils.showShort(JiNianDetailActivity.this,"加载中...");
                    }
                }

                break;
            case R.id.id_photo:
                int positionMoment = (int) v.getTag(R.id.position_book_friend_moment);
                int positionPhoto = (int) v.getTag(R.id.position_photo);

                startActivity(new Intent(JiNianDetailActivity.this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                Log.d("onclick", "positionMoment=" + positionMoment + " positionPhoto=" + positionPhoto);
                break;
            case R.id.iv_cover:
                int p = (int) v.getTag(R.id.position_book_friend_moment);


                startActivity(new Intent(JiNianDetailActivity.this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(p).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, 0));
                break;
        }
    }

    @Override
    public void onLoadMoreData(int i) {
        loadCommentList(i);
    }

    @Override
    public void onItemCommentClick(CommentData comment, int position) {
        if (!CartoonApp.getInstance().isLogin(JiNianDetailActivity.this))
            return;
        Intent intent = new Intent(this, CartoonCommentDetailActivity.class);
        intent.putExtra(Keys.COMMENT_ID, comment.getId());
        intent.putExtra(Keys.SOURCE, "凡人纪年");
        intent.putExtra(Keys.SHOW_KEYBOARD, true);

        startActivity(intent);
    }

    @Override
    public void onItemClick(CommentData comment, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Keys.COMMENT_ID, comment.getId());
        bundle.putString(Keys.SOURCE, "凡人纪年");

        //                bundle.putString(Keys.COMMENT_TO_UID, String.valueOf(toUid));
        //                bundle.putInt(Keys.COMMENT_TYPE, type);
        startActivity(new Intent(this, CartoonCommentDetailActivity.class).putExtras(bundle));

    }

    @Override
    public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position) {
        if (!CartoonApp.getInstance().isLogin(JiNianDetailActivity.this))
            return;

        if (child.getUid().equals(CartoonApp.getInstance().getUserId())) {
            return;
        }
      /*  etInput.requestFocus();
        Utils.openSoftKeyboard(this, etInput);
        etInput.setText("@" + child.getNickname() + ": ");
        NicknameColorHelper.setNicknameColor(etInput, child.getUid());
        etInput.setSelection(etInput.getText().length() - 1);
        etInput.setHint("说点什么...");
        this.comment_id = comment_id;
        this.to_uid = child.getUid();
        event_type = Constants.APPROVE_COMMENT;*/
    }




    private void loadExpoundDetail(String id) {
        showLoading();
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_JINIAN_DETAIL)
                .addParams("dataId", id)
                .addParams("userId",CartoonApp.getInstance().getUserId())
                .build().execute(new BaseCallBack<Expound>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Expound response) {
                hideLoading();
                expound = response;
                setupExpoundView();
                loadCommentList(START_PAGE);
            }

            @Override
            public Expound parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Expound.class);
            }
        });
    }

    /**
     * 加载评论列表
     *
     *                  <p/>
     *                  final int TYPE_HOT = 0x101;
     *                  final int TYPE_LATEST = 0x102;
     */
    private void loadCommentList(final int pageNum) {

        GetBuilder builder = BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_COMMENT_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("target_id", target_id)
                .addParams("type", Constants.APPROVE_BANGAI + "");
//
//        if (sort_type == TYPE_HOT) {
//            builder.addParams("sort_name", "hot");
//        } else {
//            builder.addParams("sort_name", "new");
//        }

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

                if (response!=null&&response.getList().size()>0) {
                    if (pageNum == START_PAGE) {
                        adapter.setData(response.getList(), String.valueOf(response.getTotalRow()));
                    } else {
                        adapter.addAll(response.getList(),String.valueOf(response.getTotalRow()));
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

    //umeng分享，回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                recycleView.setStartPageIndex(START_PAGE);
                loadCommentList(START_PAGE);
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }
}
