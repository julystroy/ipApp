package com.cartoon.module.cartoon;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.CartoonComment;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.CollectionUtils;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * q漫画评论详情
 * <p/>
 * Created by David on 16/6/27.
 */
public class CartoonCommentDetailActivity extends BaseActivity implements View.OnClickListener, CartoonCommentDetailAdapter.OnClickListener,  EndLessListener {

    @BindView(R.id.bt_left)
    ImageButton         btLeft;
    @BindView(R.id.tv_title)
    TextView            tvTitle;
    @BindView(R.id.bt_right)
    ImageButton         btRight;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.et_input)
    EditText            etInput;
    //    @BindView(R.id.iv_share)
//    ImageView ivShare;
    @BindView(R.id.send_bt)
    Button              sendBt;
    @BindView(R.id.ll_comment_detail_activity)
    LinearLayout        llCommentDetailActivity;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    /**
     * 评论ID
     */
    private String comment_id;

    private int momentPosition;
    private BookFriendMoment moment;
    private CartoonComment comment;
    private CartoonCommentDetailAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_cartoon_comment_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressBar.setVisibility(View.VISIBLE);
        //把评论拿出来
        comment_id = getIntent().getStringExtra(Keys.COMMENT_ID);
        moment = (BookFriendMoment) getIntent().getSerializableExtra(Keys.MOMENT);
        momentPosition = getIntent().getIntExtra(Keys.MOMENT_POSITION, 0);
        int approveType = getIntent().getIntExtra(Keys.APPROVE_TYPE, Constants.APPROVE_COMMENT);


       // boolean showKeyBoard = getIntent().getBooleanExtra(Keys.SHOW_KEYBOARD, false);

        tvTitle.setText("评论详情");
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setVisibility(View.GONE);

        btLeft.setOnClickListener(this);
        sendBt.setOnClickListener(this);
        adapter = new CartoonCommentDetailAdapter(this,CartoonCommentDetailActivity.this);
        adapter.setOnClickListener(this);
        adapter.setApproveType(approveType);

        recycleView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(layoutManager);
      //  recycleView.setEndLessListener(this);

      //触摸软键盘外部，软键盘退出
        etInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

       /* etInput.setImeOptions(EditorInfo.IME_ACTION_SEND);//软键盘获取焦点
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!CartoonApp.getInstance().isLogin(getBaseContext())) {
                    return true;
                }

                String message = etInput.getEditableText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(CartoonCommentDetailActivity.this, getString(R.string.pleaes_input_comment_context));
                    return true;
                }
                if (!TextUtils.isEmpty(message)
                        && (actionId == EditorInfo.IME_ACTION_SEND || (event != null
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    sendCartonComment(comment.getId(), message);
                    return true;
                }
                return false;
            }
        });*/

       /* if (showKeyBoard) {
            etInput.post(new Runnable() {
                @Override
                public void run() {
                    //etInput.requestFocus();
                   // Utils.openSoftKeyboard(CartoonCommentDetailActivity.this, etInput);
                    Utils.closeSoftKeyboard(etInput, CartoonCommentDetailActivity.this);

                }
            });
        }*/

        loadCommentDetail(START_PAGE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_left:
                finish();
                break;
            case R.id.send_bt:
                if (!CartoonApp.getInstance().isLogin(getBaseContext())) {
                    return;
                }
                String message = etInput.getEditableText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(CartoonCommentDetailActivity.this, getString(R.string.pleaes_input_comment_context));
                    return;
                }
                if (comment!=null&&comment.getId()!=null)
                sendCartonComment(comment.getId()+"", message);
                else
                ToastUtils.showShort(CartoonCommentDetailActivity.this,"数据未加载完毕");
                break;

        }
    }

    private String lastComment;

    /**
     * 发表评论
     *
     * @param r_id
     * @param message
     */
    private void sendCartonComment(String r_id, String message) {
        if (message.equals(lastComment)) return;
        lastComment = message;
        showLoadingDialog();
        PostFormBuilder builder = BuilderInstance.getInstance()
                .getPostBuilderInstance(StaticField.URL_ADD_COMMENT)
                .addParams("target_id", r_id)
                .addParams("type", Constants.APPROVE_COMMENT + "")
                .addParams("content", message)
                .addParams("to_uid", isReply ? to_uid+"" : comment.getUid()+"");

        builder.build().execute(new BaseCallBack() {

            @Override
            public void onLoadFail() {
                lastComment = null;
                hideLoadingDialog();
                ToastUtils.showShort(getBaseContext(), "评论失败");
            }

            @Override
            public void onContentNull() {
                lastComment = null;
                hideLoadingDialog();
                ToastUtils.showShort(CartoonApp.getInstance(), R.string.content_null);
            }

            @Override
            public void onLoadSuccess(Object response) {
                hideLoadingDialog();
                isReply = false;

                to_uid = "";
                etInput.setText("");

                Utils.closeSoftKeyboard(etInput, CartoonCommentDetailActivity.this);
                loadCommentDetail(START_PAGE);
                ToastUtils.showShort(getBaseContext(), "发送成功");
                if (null != moment) {
                    moment.setComment_num(moment.getComment_num() + 1);
                  //  EventBus.getDefault().post(new EventMomentItemChanged(moment, momentPosition));
                }

            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }



    /**
     * 加载评论列表
     */
    private void loadCommentDetail(final int pageNum) {
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_COMMENT_DETAIL_PAGE)
                .addParams("id", comment_id)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<CartoonComment>() {

            @Override
            public void onLoadFail() {
                Toast.makeText(CartoonCommentDetailActivity.this , "内容不存在或已删除"  , Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(CartoonComment response) {
                mProgressBar.setVisibility(View.GONE);
                recycleView.completeLoadMore();
                setupLoadMoreList(response.getChildren());
                comment = response;
//                Log.i("xxx", "comment.getModule_id()----" + comment.getModule_id());
//                if (TextUtils.isEmpty(comment.getModule_id())) {
//                    if (null != ivShare) ivShare.setVisibility(View.GONE);
//                }
                if (START_PAGE == pageNum) {
                    adapter.setData(comment);
                } else {
                    adapter.addAll(response.getChildren());
                }

            }

            @Override
            public CartoonComment parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, CartoonComment.class);
            }
        });

    }

    private void setupLoadMoreList(CartoonComment.ChildrenBean response) {
        if (CollectionUtils.isEmpty(response.getList())
                || response.isLastPage()) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
    }

    public void notifyItemChange(int isApprove, int approveCount) {

        if (null != moment) {
            moment.setIs_approve(isApprove);
            moment.setApprove_num(approveCount);
          //  EventBus.getDefault().post(new EventMomentItemChanged(moment, momentPosition));
        }
    }


    private boolean isReply;
    /**
     * 被评论的评论id,uid
     */
    private String to_uid;

    @Override
    public void onItemClick(CartoonCommentChild child, int position) {
        if (!CartoonApp.getInstance().isLogin(CartoonCommentDetailActivity.this)) return;

        if (String.valueOf(child.getUid()).equals(CartoonApp.getInstance().getUserId())) {
            return;
        }
        //etInput.requestFocus();//获取焦点
        Utils.openSoftKeyboard(this, etInput);
        etInput.setText("@" + child.getNickname() + ": ");
        NicknameColorHelper.setNicknameColor(etInput , child.getUid());
        etInput.setSelection(etInput.getText().length() - 1);
        etInput.setHint("说点什么...");
        this.to_uid = String.valueOf(child.getUid());
        isReply = true;
       // sendCartonComment(to_uid, etInput.getText().toString());
    }



    @Override
    public void onLoadMoreData(int i) {
        loadCommentDetail(i);
    }
}
