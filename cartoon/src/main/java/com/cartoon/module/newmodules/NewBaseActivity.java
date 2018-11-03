package com.cartoon.module.newmodules;

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
import com.cartoon.common.AddCommentActivity;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.NewBase;
import com.cartoon.data.response.CartoonCommentListResp;
import com.cartoon.data.response.CommentData;
import com.cartoon.data.response.VoteResponse;
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
 * Created by debuggerx on 16-11-15.
 */
public class NewBaseActivity extends BaseActivity implements View.OnClickListener,
        NewBaseDetailAdapter.OnItemCommentListener, EndLessListener, NewBaseDetailAdapter.OnItemCommentTypeChange {

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
    ImageView      tvLike;
    @BindView(R.id.tv_comment_num)
    TextView            tvNum;

    @BindView(R.id.ll_nesbase)
    LinearLayout        llNewsbase;
    @BindView(R.id.ll_like)
    RelativeLayout llLike;


    private NewBase newbase;
    private String  target_id;
    /**
     * 被评论的评论id,uid
     */
    private String  to_uid;
    private String  comment_id;

    /**
     *
     * 事件类型
     */
  //  private int event_type;
    private int base_event_type;
    /**
     * 排序类型
     */
    private int sort_type = TYPE_LATEST;

    private NewBaseDetailAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.ac_newbase_detail;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newbase = getIntent().getParcelableExtra(Keys.TARGET_OBJECT);
        target_id = getIntent().getStringExtra(Keys.TARGET_ID);
       // event_type = getIntent().getIntExtra(Keys.COMMENT_TYPE, -1);
       // base_event_type = event_type;
//判断是从书友圈进来还是别的地方进来
       if (newbase != null) {

            loadNewBaseDetail(newbase.getId() + "");
        } else {
            loadNewBaseDetail(target_id);
        }


        btLeft.setImageResource(R.mipmap.icon_back_black);
        btRight.setImageResource(R.mipmap.icon_share);

        btLeft.setOnClickListener(this);
        //        tvHot.setOnClickListener(this);
        //        tvNew.setOnClickListener(this);
        ivShare.setVisibility(View.GONE);
        btRight.setOnClickListener(this);
        tvLike.setOnClickListener(this);

        adapter = new NewBaseDetailAdapter(this, NewBaseActivity.this);
        adapter.setNewBase(newbase);
        adapter.setListener(this);
        adapter.setChangeListener(this);
        adapter.setOnClickSubViewListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
/*
        etInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        etInput.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        etInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!CartoonApp.getInstance().isLogin(NewBaseActivity.this)) {
                    return true;
                }
                String message = etInput.getEditableText().toString().trim();

                if (TextUtils.isEmpty(message)) {
                    ToastUtils.showShort(NewBaseActivity.this, getString(R.string.pleaes_input_comment_context));
                    return true;
                }
                if (!TextUtils.isEmpty(message) && (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    if (event_type == Constants.APPROVE_COMMENT) {
                        sendNewBaseComment(comment_id, event_type, message);
                    } else {
                        sendNewBaseComment(target_id, event_type, message);
                    }
                    return true;
                }
                return false;
            }
        });*/
                etInput.setOnClickListener(this);
        
        //投票请求
         LoadVoteData();
    }

    private boolean voteHas = true;
    private void LoadVoteData() {
       BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_VOTE_LIST)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("activity_id", target_id)
                .build().execute(new BaseCallBack<VoteResponse>() {
            @Override
            public void onLoadFail() {
            }

            @Override
            public void onContentNull() {
            }

            @Override
            public void onLoadSuccess(VoteResponse response) {

                if (response != null) {
                    if (response.getList().size() != 0){
                        btRight.setVisibility(View.GONE);
                        voteHas = false;
                    }
                    else{
                        btRight.setVisibility(View.VISIBLE);
                        voteHas =  true;
                    }
                    adapter.setVoteData(response);
                }

            }

            @Override
            public VoteResponse parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,VoteResponse.class);
            }
        });

    }

    private void setupNewBaseView() {
        tvTitle.setText("活动");
        //        tvTitle.setText(expound.getTitle());
        tvNum.setText(newbase.getApprove_num());
        if (newbase.getIs_approve() == 1) {
            tvLike.setSelected(true);
           // tvNum.setTextColor(Color.parseColor("#ef5f11"));
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

            case R.id.bt_right:
                if (!CartoonApp.getInstance().isLogin(NewBaseActivity.this)) {
                    return;
                }
               // Utils.closeSoftKeyboard(etInput, this);

                ApiUtils.share(this, newbase.getTitle(), Utils.addBaseUrlShare(newbase.getCover_pic()), "活动", String.valueOf(newbase.getId()));

                break;
            case R.id.tv_like:
                ApiUtils.approveTarget(target_id, 0, new ApiQuestListener() {
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
            case  R.id.et_input:
                if (CartoonApp.getInstance().isLogin(NewBaseActivity.this)) {
                    if (target_id == null) {
                        ToastUtils.showShort(NewBaseActivity.this,"加载中...");
                        return;
                    }
                    Intent intent = new Intent(this, AddCommentActivity.class);
                    //intent.putExtra(Constants.TITLE,newbase.getTitle());
                    intent.putExtra(Constants.RESOURCEID,target_id+"");
                    intent.putExtra("type","0");
                    startActivityForResult(intent,REQUESTCODE);
                }
                break;
            case R.id.id_photo:
                int positionMoment = (int) v.getTag(R.id.position_book_friend_moment);
                int positionPhoto = (int) v.getTag(R.id.position_photo);

                startActivity(new Intent(NewBaseActivity.this, PreviewPhotoActivity.class)
                        .putExtra(PreviewPhotoActivity.PHOTO_URLS, adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
                        .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                Log.d("onclick", "positionMoment=" + positionMoment + " positionPhoto=" + positionPhoto);
                break;
            case R.id.iv_cover:
                int p = (int) v.getTag(R.id.position_book_friend_moment);


                startActivity(new Intent(NewBaseActivity.this, PreviewPhotoActivity.class)
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
        if (!CartoonApp.getInstance().isLogin(NewBaseActivity.this))
            return;


        Intent intent = new Intent(this, CartoonCommentDetailActivity.class);
        intent.putExtra(Keys.COMMENT_ID, comment.getId());
        intent.putExtra(Keys.SOURCE, "活动");
        intent.putExtra(Keys.SHOW_KEYBOARD, true);

        startActivity(intent);


        //        etInput.setHint("@" + comment.getNickname());
        //        this.comment_id = comment.getId();
        //        this.to_uid = comment.getUid();
        //        event_type = Constants.APPROVE_COMMENT;

    }

    @Override
    public void onItemClick(CommentData comment, int position) {
        if (Utils.isFastDoubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putString(Keys.COMMENT_ID, comment.getId());
            bundle.putString(Keys.SOURCE,  "活动");
            startActivity(new Intent(this, CartoonCommentDetailActivity.class).putExtras(bundle));
        }
    }

    @Override
    public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position) {
        if (!CartoonApp.getInstance().isLogin(NewBaseActivity.this))
            return;

        if (child.getUid().equals(CartoonApp.getInstance().getUserId())) {
            return;
        }

    }

    private void loadNewBaseDetail(String id) {
        showLoading();
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_ACTIVITY_CONTENT)
                .addParams("id", id)
                .build().execute(new BaseCallBack<NewBase>() {

            @Override
            public void onLoadFail() {
                hideLoading();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(NewBase response) {
                hideLoading();

                newbase = response;
                adapter.setNewBase(newbase);

                setupNewBaseView();
                adapter.notifyDataSetChanged();
                loadCommentList(START_PAGE, TYPE_LATEST);
            }

            @Override
            public NewBase parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, NewBase.class);
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
    private void loadCommentList(final int pageNum, int sort_type) {

        GetBuilder builder = BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_COMMENT_LIST)
                .addParams(Keys.PAGE_NUM, String.valueOf(pageNum))
                .addParams(Keys.PAGE_SIZE, String.valueOf(PAGE_SIZE))
                .addParams("target_id", target_id)
                .addParams("type", "0");

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

                if (!com.cartoon.utils.CollectionUtils.isEmpty(response.getList())) {
                    if (pageNum == START_PAGE) {
                        adapter.setData(response.getList(), String.valueOf(response.getTotalRow()));
                    } else {
                        adapter.addAll(response.getList(),String.valueOf(response.getTotalRow()));
                    }
                    adapter.notifyDataSetChanged();
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
                loadCommentList(START_PAGE, sort_type);
            }
        } else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }


}

