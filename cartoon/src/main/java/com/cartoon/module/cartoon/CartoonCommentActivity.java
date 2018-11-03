package com.cartoon.module.cartoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.bean.Listener;
import com.cartoon.data.Cartoon;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.response.CartoonCommentListResp;
import com.cartoon.data.response.CommentData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * 漫画评论列表
 * <p>
 * Created by David on 16/6/27.
 */
public class CartoonCommentActivity extends BaseActivity implements View.OnClickListener,
        CartoonCommentAdapter.OnItemCommentListener, EndLessListener {

    final int TYPE_HOT    = 0x101;
    final int TYPE_LATEST = 0x102;
    @BindView(R.id.bt_left)
    ImageButton         btLeft;
    @BindView(R.id.tv_title)
    TextView            tvTitle;
    @BindView(R.id.bt_right)
    ImageButton         btRight;
    @BindView(R.id.rl_title)
    RelativeLayout      rlTitle;
    @BindView(R.id.tv_desc)
    TextView            tvDesc;
    @BindView(R.id.tv_hot)
    TextView            tvHotest;
    @BindView(R.id.tv_new)
    TextView            tvLatest;
    @BindView(R.id.rl_summary)
    RelativeLayout      rlSummary;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.et_input)
    EditText            etInput;
    @BindView(R.id.send_bt)
    Button              sendBt;
    @BindView(R.id.ll_cartoon)
    LinearLayout        mLlCartoon;


    private String target_id;
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

    private CartoonCommentAdapter adapter;

    private Listener listener;
    private Cartoon  cartoon;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_cartoon_comment;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        target_id = getIntent().getStringExtra(Keys.TARGET_ID);
        event_type = getOriginEventType();

        listener = (Listener) getIntent().getSerializableExtra(Keys.LISTENER);
        cartoon = getIntent().getParcelableExtra(Keys.CARTOON);


        String likeCount = getIntent().getStringExtra(Keys.LIKE_COUNT);
        boolean isLike = getIntent().getBooleanExtra(Keys.IS_LIKE, false);
        if (TextUtils.isEmpty(likeCount))
            likeCount = "0";

        setupView(likeCount, isLike);

        loadCommentList(START_PAGE, TYPE_LATEST);

    }

    private void setupView(String likeCount, boolean isLike) {
        tvTitle.setText("评论");
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setVisibility(View.GONE);
        btLeft.setOnClickListener(this);
        tvLatest.setOnClickListener(this);
        tvHotest.setOnClickListener(this);
        //        ivShare.setOnClickListener(this);
        //        tvLike.setOnClickListener(this);
        sendBt.setOnClickListener(this);
        //        sendBt.setOnClickListener(this);
        tvHotest.setSelected(true);
        tvLatest.setSelected(false);


        //tvNum.setText(likeCount);
        //        if (isLike) {
        //           // tvNum.setTextColor(Color.parseColor("#ef5f11"));
        //            tvLike.setSelected(true);
        //        } else {
        //            //tvNum.setTextColor(Color.parseColor("#929292"));
        //            tvLike.setSelected(false);
        //        }

        adapter = new CartoonCommentAdapter(this, CartoonCommentActivity.this);
        adapter.setListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
/*
        etInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String message = etInput.getEditableText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(CartoonCommentActivity.this, getString(R.string.pleaes_input_comment_context));
                    return true;
                }
                if (!TextUtils.isEmpty(message)
                        && (actionId == EditorInfo.IME_ACTION_SEND || (event != null
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    if (!CartoonApp.getInstance().isLogin(getBaseContext())) {
                        return true;
                    }
                    if (event_type == Constants.APPROVE_CARTOON || event_type == Constants.APPROVE_MANUAL || event_type == Constants.APPROVE_LISTENER) {
                        sendCartonComment(target_id, event_type, message);
                    } else if (event_type == Constants.APPROVE_COMMENT) {
                        sendCartonComment(comment_id, event_type, message);
                    }
                    return true;
                }
                return false;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                finish();
                break;
            case R.id.tv_hot:
                sort_type = TYPE_HOT;
                tvLatest.setSelected(true);
                tvHotest.setSelected(false);
                loadCommentList(START_PAGE, TYPE_HOT);
                break;
            case R.id.tv_new:
                sort_type = TYPE_LATEST;
                tvLatest.setSelected(false);
                tvHotest.setSelected(true);
                loadCommentList(START_PAGE, TYPE_LATEST);
                break;
            case R.id.iv_share:
                if (!CartoonApp.getInstance().isLogin(CartoonCommentActivity.this)) {
                    return;
                }
                Utils.closeSoftKeyboard(etInput, this);
                if (null != cartoon) {
                    ApiUtils.share(this, cartoon.getTitle(), cartoon.getCover_pic(), "漫画", cartoon.getId());
                } else if (null != listener) {
                    ApiUtils.share(this, listener.getTitle(), listener.getCover_pic(), "听书", listener.getId(), listener.getRemote_connect());
                }
                break;
            //            case R.id.tv_like:
            //                if (!CartoonApp.getInstance().isLogin(getBaseContext())) {
            //                    return;
            //                }
            //                ApiUtils.approveTarget(target_id, event_type, new ApiQuestListener() {
            //                    @Override
            //                    public void onLoadFail() {
            //
            //                    }
            //
            //                    @Override
            //                    public void onLoadSuccess(String response) {
            ////                        tvLike.setSelected(true);
            //
            //                        int count = 0;
            //                        try {
            //                            count = Integer.parseInt(tvNum.getText().toString());
            //                        } catch (Exception e) {
            //                            e.printStackTrace();
            //                        }
            //                        count++;
            //                        tvNum.setText(String.valueOf(count));
            //                        tvNum.setTextColor(Color.parseColor("#ef5f11"));
            //
            //                    }
            //                });
            //                break;
            case R.id.send_bt:
                String message = etInput.getEditableText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(CartoonCommentActivity.this, getString(R.string.pleaes_input_comment_context));
                    return;
                } else {
                    if (!CartoonApp.getInstance().isLogin(getBaseContext())) {
                        return;
                    }
                    if (event_type == Constants.APPROVE_CARTOON || event_type == Constants.APPROVE_MANUAL || event_type == Constants.APPROVE_LISTENER) {
                        sendCartonComment(target_id, event_type, message);
                    } else if (event_type == Constants.APPROVE_COMMENT) {
                        sendCartonComment(comment_id, event_type, message);
                    }
                }

                break;
        }
    }

    @Override
    public void onLoadMoreData(int i) {
        loadCommentList(i, sort_type);
    }

    @Override
    public void onItemCommentClick(CommentData comment, int position) {
        //        if (!CartoonApp.getInstance().isLogin(CartoonCommentActivity.this)) return;


        Intent intent = new Intent(this, CartoonCommentDetailActivity.class);
        intent.putExtra(Keys.COMMENT_ID, comment.getId());
        intent.putExtra(Keys.SHOW_KEYBOARD, true);
        intent.putExtra(Keys.SOURCE, "漫画");

        startActivity(intent);


        //        etInput.requestFocus();
        //        Utils.openSoftKeyboard(this, etInput);
        //        etInput.setHint("@" + comment.getNickname());
        //        this.comment_id = comment.getId();
        //        this.to_uid = comment.getUid();
        //        event_type = Constants.APPROVE_COMMENT;
    }

    private String lastComment;

    @Override
    public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position) {
        if (!CartoonApp.getInstance().isLogin(CartoonCommentActivity.this))
            return;

        if (child.getUid().equals(CartoonApp.getInstance().getUserId())) {
            return;
        }
        etInput.requestFocus();
        Utils.openSoftKeyboard(this, etInput);
        etInput.setText("@" + child.getNickname() + ": ");
        NicknameColorHelper.setNicknameColor(etInput, child.getUid());
        etInput.setSelection(etInput.getText().length() - 1);
        etInput.setHint("说点什么...");
        this.comment_id = comment_id;
        this.to_uid = child.getUid();
        event_type = Constants.APPROVE_COMMENT;
    }

    private void sendCartonComment(String r_id, int type, String message) {
        if (message.equals(lastComment))
            return;

        showLoadingDialog();
        lastComment = message;

        PostFormBuilder builder = BuilderInstance.getInstance()
                .getPostBuilderInstance(StaticField.URL_ADD_COMMENT)
                .addParams("target_id", r_id)
                .addParams("type", type + "")
                .addParams("content", message);

        /**
         * 二级评论需添加to_uid
         */
        if (type == Constants.APPROVE_COMMENT) {
            builder.addParams("to_uid", to_uid);
        }

        builder.build().execute(new BaseCallBack() {

            @Override
            public void onLoadFail() {
                hideLoadingDialog();
                lastComment = null;
                ToastUtils.showShort(getBaseContext(), "发送失败");
            }

            @Override
            public void onContentNull() {
                hideLoadingDialog();
                lastComment = null;
                ToastUtils.showShort(CartoonApp.getInstance(), R.string.content_null);
            }

            @Override
            public void onLoadSuccess(Object response) {
                ToastUtils.showShort(getBaseContext(), "发送成功");
                hideLoadingDialog();
                lastComment = null;

                etInput.setHint("说点什么...");
                etInput.setText("");
                event_type = getOriginEventType();

                loadCommentList(START_PAGE, sort_type);


            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }


    private int getOriginEventType() {
        return getIntent().getIntExtra(Keys.COMMENT_TYPE, -1);
    }

    /**
     * 加载评论列表
     *
     * @param sort_type 排序类型
     *                  <p>
     *                  final int TYPE_HOT = 0x101;
     *                  final int TYPE_LATEST = 0x102;
     */
    private void loadCommentList(final int pageNum, int sort_type) {
        GetBuilder builder = BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_COMMENT_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("target_id", target_id)
                .addParams("type", getOriginEventType() + "");

        if (sort_type == TYPE_HOT) {
            builder.addParams("sort_name", "hot");
        } else {
            builder.addParams("sort_name", "new");
        }

        builder.build().execute(new BaseCallBack<CartoonCommentListResp>() {

            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(CartoonCommentListResp response) {
                recycleView.completeLoadMore();
                setupLoadMoreList(response);
                tvDesc.setText("共" + response.getTotalRow() + "条评论");

                if (pageNum == START_PAGE) {
                    adapter.setData(response.getList(), response.getTotalRow());
                } else {
                    adapter.addAll(response.getList(), response.getTotalRow());
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


}
