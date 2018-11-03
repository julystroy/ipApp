package com.cartoon.module.action;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.ActionOne;
import com.cartoon.data.Keys;
import com.cartoon.data.RankHot;
import com.cartoon.data.SplashUi;
import com.cartoon.data.response.ActionOneData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.listener.ResponseListener;
import com.cartoon.module.tab.adapter.ActionDetailAdapter;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.BonusHttp;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-8-24.
 */
public class ActionDetailActivity extends BaseActivity implements View.OnClickListener, ActionDetailAdapter.OnClickListener, EndLessListener {
    @BindView(R.id.bt_left)
    ImageButton         btLeft;
    @BindView(R.id.tv_title)
    TextView            mTvTitle;
    @BindView(R.id.bt_right)
    ImageButton         btRight;
    @BindView(R.id.rl_title)
    RelativeLayout      mRlTitle;
    @BindView(R.id.progressBar)
    ProgressBar         mProgressBar;
    @BindView(R.id.recycle_view)
    EndLessRecyclerView recycleView;
    @BindView(R.id.et_input)
    EditText            etInput;
    @BindView(R.id.send_bt)
    TextView              sendBt;
    @BindView(R.id.vote_bt)
    TextView              voteBt;
    @BindView(R.id.ll_comment)
    LinearLayout        mLlComment;
    @BindView(R.id.ll_comment_detail_activity)
    LinearLayout        mLlCommentDetailActivity;

    /**
     * 评论ID
     */


    private String           comment_id;
    private String           canVote;
    private String           auth_id;
    private ActionDetailAdapter adapter;
    private  String avatar;
    @Override
    protected int getContentViewId() {
        return R.layout.ac_action_comment_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mProgressBar.setVisibility(View.VISIBLE);
        initView();
        //把评论拿出来
         comment_id = getIntent().getStringExtra(Keys.CHART);
         auth_id = getIntent().getStringExtra(Keys.CHART_USEID);
//         canVote = getIntent().getStringExtra(Keys.CHART_BOOLEAN);
//        if (canVote != null && canVote.equals("-1")) {//控制投票键状态
//            voteBt.setSelected(true);
//        } else {
//            voteBt.setOnClickListener(this);
//        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recycleView.setLayoutManager(layoutManager);
         adapter = new ActionDetailAdapter(ActionDetailActivity.this, comment_id);
        adapter.setOnClickListener(this);

        recycleView.setAdapter(adapter);

        sendBt.setVisibility(View.VISIBLE);
        voteBt.setVisibility(View.GONE);
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

//        etInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s != null && !s.toString().isEmpty()) {
//                    sendBt.setVisibility(View.VISIBLE);
//                    voteBt.setVisibility(View.GONE);
//                } else {
//                    sendBt.setVisibility(View.GONE);
//                    voteBt.setVisibility(View.VISIBLE);
//                }
//            }
//        });
       loadCommentDetail(START_PAGE);

    }

    private void initView() {
        mTvTitle.setText("文章详情");
        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setImageResource(R.mipmap.icon_share);

        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
        sendBt.setOnClickListener(this);

    }

    private void loadCommentDetail(final int pageNum) {
        BuilderInstance.getInstance()
                .getPostBuilderInstance(StaticField.URL_ACTION_COMMENT_LIST)
                .addParams("id", comment_id)
                .addParams("type","11")
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .build().execute(new BaseCallBack<ActionOneData>() {
            @Override
            public void onLoadFail() {
                Toast.makeText(ActionDetailActivity.this , "内容不存在或已删除,请刷新", Toast.LENGTH_SHORT).show();
              //  finish();
                mProgressBar.setVisibility(View.GONE);
//
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(ActionOneData response) {
                mProgressBar.setVisibility(View.GONE);
                recycleView.completeLoadMore();
                setupLoadMoreList(response);
                if (response == null) {
                    return;
                }
                 avatar = response.getAvatar();

                if (START_PAGE == pageNum) {
                    adapter.setData(response);
                } else {
                    adapter.addAll(response);
                }
                DebugLog.i("...response.." + response.toString());
            }

            @Override
            public ActionOneData parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, ActionOneData.class);
            }
        });

    }

    private void setupLoadMoreList(ActionOneData response) {
        if (response.getChildren()!=null
                || response.getChildren().size()<10) {
            recycleView.setEndLessListener(null);
        } else {
            recycleView.setEndLessListener(this);
        }
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
                    ToastUtils.showShort(ActionDetailActivity.this, getString(R.string.pleaes_input_comment_context));
                    return;
                }
                sendActionComment(comment_id, message);
                break;
            case R.id.vote_bt:
                if (CartoonApp.getInstance().isLogin(ActionDetailActivity.this))
                BonusHttp.ActionVote(this, comment_id, new ResponseListener() {
                    @Override
                    public void onLoadFail() {
                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        if (response != null && !response.equals("1")) {
                            voteBt.setSelected(true);
                            voteBt.setEnabled(false);
                        }else
                            adapter.setVoteNum();
                        EventBus.getDefault().post(new RankHot());
                    }
                });
                break;
            case R.id.bt_right:
                if (CartoonApp.getInstance().isLogin(ActionDetailActivity.this)) {
                    if (avatar == null) {
                        return;
                    }
                    else
                    ApiUtils.share(ActionDetailActivity.this,"征文活动",Utils.addBaseUrlShare(avatar),"征文",comment_id);
                }
                break;

        }
    }
    private String lastComment;

   /*    *
        * 发表评论
        *
        * @param message*/

    private void sendActionComment(String id, String message) {
        if (message.equals(lastComment)) return;
        lastComment = message;
        showLoadingDialog();
        PostFormBuilder builder = BuilderInstance.getInstance()
                .getPostBuilderInstance(StaticField.URL_ADD_ACTION_COMMENT)
                .addParams("token",CartoonApp.getInstance().getToken())
                .addParams("target_id", id)
                .addParams("type",  "11")  //添加征文
                .addParams("content", message)
                .addParams("to_uid", isReply ? to_uid : auth_id);

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

                Utils.closeSoftKeyboard(etInput, ActionDetailActivity.this);
                loadCommentDetail(START_PAGE);
                ToastUtils.showShort(getBaseContext(), "发送成功");

            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }

    private boolean isReply;
   /* *
     * 被评论的评论id,uid
     */
    private String to_uid;

    @Override
    public void onItemClick(ActionOne child, int position) {
        if (!CartoonApp.getInstance().isLogin(ActionDetailActivity.this)) return;
        if (String.valueOf(child.getUid()).equals(CartoonApp.getInstance().getUserId())) {
            return;
        }
        //etInput.requestFocus();//获取焦点
        Utils.openSoftKeyboard(this, etInput);
        etInput.setText("@" + child.getNickname() + ": ");
        NicknameColorHelper.setNicknameColor(etInput , child.getUid());
        etInput.setSelection(etInput.getText().length() - 1);
        etInput.setHint("说点什么...");
        this.to_uid = child.getUid()+"";
        isReply = true;
    }


    private void deleteDetail(){
        showLoadingDialog();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_DELETE_ACTION_DETAIL)
                .addParams("uid",CartoonApp.getInstance().getUserId())
                .addParams("essayId",comment_id)
                .build().execute(new BaseCallBack<String>() {
            @Override
            public void onLoadFail() {
                hideLoadingDialog();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                hideLoadingDialog();
                EventBus.getDefault().post(new RankHot());
                ToastUtils.showShort(ActionDetailActivity.this,"删除成功");
                finish();
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }
    @Subscribe
    public void onEvent(SplashUi d) {
        deleteDetail();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onLoadMoreData(int i) {
        loadCommentDetail(i);
    }
}
