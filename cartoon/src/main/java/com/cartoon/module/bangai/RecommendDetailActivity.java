package com.cartoon.module.bangai;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.CollectionUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.umeng.socialize.UMShareAPI;

import butterknife.BindView;
import cn.com.xuanjiezhimen.R;
import cndroid.com.smoothendlesslibrary.EndLessListener;
import cndroid.com.smoothendlesslibrary.EndLessRecyclerView;

/**
 * Created by cc on 17-11-27.
 */
public class RecommendDetailActivity extends BaseActivity implements View.OnClickListener,
        EndLessListener, RecommendDetailAdapter.OnItemCommentListener {

public static final int TYPE_HOT=0x101;
public static final int TYPE_LATEST=0x102;
private final static int REQUESTCODE=0x103; // 返回的结果码

@BindView(R.id.bt_left)
ImageButton btLeft;
@BindView(R.id.tv_title)
TextView    tvTitle;
@BindView(R.id.bt_right)
ImageButton btRight;

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
LinearLayout   llBangai;
@BindView(R.id.ll_like)
RelativeLayout llLike;


    private    NewBase newbase;
    private String  target_id;
/**
 * 被评论的评论id,uid
 */
private     String  to_uid;
private     String  comment_id;

private String url;
/**
 * 事件类型
 */
private int event_type;
private boolean isFirst;
/**
 * 排序类型
 */
private int sort_type=TYPE_LATEST;

private RecommendDetailAdapter adapter;


@Override
protected int getContentViewId(){
        return R.layout.ac_bangai_detail;
        }

@Override
protected int getFragmentContentId(){
        return 0;
        }

@TargetApi(Build.VERSION_CODES.M)
@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    newbase = getIntent().getParcelableExtra(Keys.TARGET_OBJECT);
    target_id = getIntent().getStringExtra(Keys.TARGET_ID);
    // event_type = getIntent().getIntExtra(Keys.COMMENT_TYPE, -1);
    // base_event_type = event_type;
    //判断是从书友圈进来还是别的地方进来

     adapter=new RecommendDetailAdapter(this,RecommendDetailActivity.this);
     adapter.setListener(this);
     adapter.setOnClickSubViewListener(this);

    if (newbase != null) {
        setupExpoundView();
            adapter.setNewBase(newbase);
    } else {
        loadExpoundDetail(target_id);
    }


    btLeft.setImageResource(R.mipmap.icon_back_black);
    btRight.setImageResource(R.mipmap.icon_share);

    btLeft.setOnClickListener(this);
    ivShare.setVisibility(View.GONE);
    btRight.setOnClickListener(this);
    tvLike.setOnClickListener(this);
        llLike.setOnClickListener(this);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        recycleView.setEndLessListener(this);
        etInput.setOnClickListener(this);
        }

private void loadExpoundDetail(String id){

        showLoading();
    BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_STATICFILM_CONTENT)
            .addParams("id", id)
            .build().execute(new BaseCallBack<NewBase>(){

@Override
public void onLoadFail(){
        hideLoading();
        }

@Override
public void onContentNull(){

        }

@Override
public void onLoadSuccess(NewBase response){
        hideLoading();

    newbase=response;
        adapter.setNewBase(response);

        setupExpoundView();
        // adapter.notifyDataSetChanged();
        }

@Override
public NewBase parseNetworkResponse(String response)throws Exception{
        return JSON.parseObject(response,NewBase.class);
        }
        });
        }
private void setupExpoundView(){

        target_id=newbase.getId()+"";
                tvTitle.setText("凡人精品");
        tvNum.setText(newbase.getApprove_num());
        if(newbase.getIs_approve()==1){
        tvLike.setSelected(true);
        }else{
        tvLike.setSelected(false);
        }

        loadCommentList(START_PAGE);
        }



@Override
public void onClick(View v){
        switch(v.getId()){
        case R.id.bt_left:
        finish();
        break;

        case R.id.bt_right:
        if(!CartoonApp.getInstance().isLogin(RecommendDetailActivity.this)){
        return;
        }

         if (newbase!=null)
        ApiUtils.share(RecommendDetailActivity.this,newbase.getTitle(), Utils.addBaseUrlShare(newbase.getCover_pic()),"凡人精品",String.valueOf(newbase.getId()));
        else
          ToastUtils.showShort(RecommendDetailActivity.this,"数据加载中");
                break;
        case R.id.tv_like:
        case R.id.ll_like:
        ApiUtils.approveTarget(target_id,9,new ApiQuestListener(){
@Override
public void onLoadFail(){

        }

@Override
public void onLoadSuccess(String response){
        tvLike.setSelected(true);

        int count=0;
        try{
        count=Integer.parseInt(tvNum.getText().toString());
        }catch(Exception e){
        e.printStackTrace();
        }
        count++;
        tvNum.setText(String.valueOf(count));

        }
        });
        break;


        case R.id.et_input:
        if(CartoonApp.getInstance().isLogin(RecommendDetailActivity.this)){
                if (target_id != null) {
                        Intent intent=new Intent(this,AddCommentActivity.class);
                        intent.putExtra(Constants.RESOURCEID,target_id+"");
                        intent.putExtra("type","9");
                        startActivityForResult(intent,REQUESTCODE);
                }else{
                        ToastUtils.showShort(RecommendDetailActivity.this,"加载中...");
                }
        }
        break;
        case R.id.id_photo:
        int positionMoment=(int)v.getTag(R.id.position_book_friend_moment);
        int positionPhoto=(int)v.getTag(R.id.position_photo);

        startActivity(new Intent(RecommendDetailActivity.this,PreviewPhotoActivity.class)
        .putExtra(PreviewPhotoActivity.PHOTO_URLS,adapter.getItem(positionMoment).getPhoto().toArray(new String[0]))
        .putExtra(PreviewPhotoActivity.POSITION,positionPhoto));
        break;
        case R.id.iv_cover:
        int p=(int)v.getTag(R.id.position_book_friend_moment);


        startActivity(new Intent(RecommendDetailActivity.this,PreviewPhotoActivity.class)
        .putExtra(PreviewPhotoActivity.PHOTO_URLS,adapter.getItem(p).getPhoto().toArray(new String[0]))
        .putExtra(PreviewPhotoActivity.POSITION,0));
        break;

        }
        }


@Override
public void onLoadMoreData(int i){
        if(i==START_PAGE){
        return;
        }else{
        loadCommentList(i);
        }
        }

@Override
public void onItemCommentClick(CommentData comment, int position){
        if(!CartoonApp.getInstance().isLogin(RecommendDetailActivity.this))
        return;


        Intent intent=new Intent(this,CartoonCommentDetailActivity.class);
        intent.putExtra(Keys.COMMENT_ID,comment.getId());

        startActivity(intent);


        }

@Override
public void onItemClick(CommentData comment,int position){
        Bundle bundle=new Bundle();
        bundle.putString(Keys.COMMENT_ID,comment.getId());
        startActivity(new Intent(this,CartoonCommentDetailActivity.class).putExtras(bundle));

        }

@Override
public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position){
        if(!CartoonApp.getInstance().isLogin(RecommendDetailActivity.this))
        return;

        if(child.getUid().equals(CartoonApp.getInstance().getUserId())){
        return;
        }
        NicknameColorHelper.setNicknameColor(etInput,child.getUid());

        this.comment_id=comment_id;
        this.to_uid=child.getUid();
        event_type=Constants.APPROVE_JINGPIN;
        }


/**
 * 加载评论列表
 * <p/>
 * <p/>
 * final int TYPE_HOT = 0x101;
 * final int TYPE_LATEST = 0x102;
 */
private void loadCommentList(final int pageNum){//// FIXME: 16-12-8
        BuilderInstance.getInstance()
        .getGetBuilderInstance(StaticField.URL_COMMENT_LIST)
        .addParams(Keys.PAGE_NUM,String.valueOf(pageNum))
        .addParams(Keys.PAGE_SIZE,String.valueOf(PAGE_SIZE))
        .addParams("target_id",target_id)
        .addParams("type","9")


        .build().execute(new BaseCallBack<CartoonCommentListResp>(){

@Override
public void onLoadFail(){
        hideLoading();
        }

@Override
public void onContentNull(){

        }

@Override
public void onLoadSuccess(CartoonCommentListResp response){

        recycleView.completeLoadMore();
        setupLoadMoreList(response);
        //                tvDesc.setText("共" + response.getTotalRow() + "条评论");

        if(response!=null&&response.getList()!=null&&response.getList().size()>0){
        if(pageNum==START_PAGE){
        adapter.setData(response.getList(),String.valueOf(response.getTotalRow()));
        }else{
        adapter.addAll(response.getList(),String.valueOf(response.getTotalRow()));
        }
        }


        DebugLog.i("...response.."+response.toString());
        }

@Override
public CartoonCommentListResp parseNetworkResponse(String response)throws Exception{
        return JSON.parseObject(response,CartoonCommentListResp.class);
        }
        });
        }

private void setupLoadMoreList(CartoonCommentListResp response){
        if(CollectionUtils.isEmpty(response.getList())
        ||response.getList().size()<PAGE_SIZE){
        recycleView.setEndLessListener(null);
        }else{
        recycleView.setEndLessListener(this);
        }
        }

private MaterialDialog dialog;

public void showLoading(){
        if(dialog==null)
        dialog=new MaterialDialog.Builder(this)
        .title(R.string.notice)
        .content(R.string.please_wait)
        .progress(true,0)
        .build();

        dialog.show();
        }
public void hideLoading(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
        if(isDestroyed())
        return;
        }
        if(dialog!=null&&dialog.isShowing())
        dialog.dismiss();
        }


@Override
protected void onDestroy(){
        super.onDestroy();
        // EasySharePreference.setReadId(this,target_id);
        target_id=null;
        finish();
        }


//umeng分享，回调
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==2){
        if(requestCode==REQUESTCODE){
        recycleView.setStartPageIndex(START_PAGE);
        loadCommentList(START_PAGE);
        }
        }else{
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
        }
        }
        }