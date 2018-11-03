package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.ActionOne;
import com.cartoon.data.FanRen;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.ActionOneData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.action.ActionDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartoon.view.CustomListView;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartoon.view.ReadMoreTextView;
import com.cartoon.view.dialog.ReportDialog;
import com.cartton.library.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 漫画评论适配器
 * <p>
 * Created by David on 16/6/27.
 */
public class ActionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x101;
    private static final int TYPE_ITEM = 0x102;

    private Context         context;
    private List<ActionOne> mList;
    private String          essay_id;
    private int             toatal;

    private boolean ss =false;

    public ActionDetailAdapter(Context context, String essay_id ) {
        this.context = context;
        this.essay_id = essay_id;
    }



    public void setData(ActionOneData comment) {
        if (comment == null) {
            this.mList =null;
            return;
        }
        this.mList = comment.getChildren();
        // list = new ArrayList<>();
        this.toatal =comment.getChildren().size();
        notifyDataSetChanged();
    }

    public void addAll(ActionOneData list) {
        int size = this.mList.size();
        this.mList.addAll(list.getChildren());
        this.toatal = list.getChildren().size();
        notifyItemRangeInserted(size + 1, list.getChildren().size());
    }

    public void setVoteNum(){
        ss = true;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return  (mList == null ? 1 : (mList.size() + 1));
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEAD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_book_webview, parent, false);
            return new HeadViewHolder(view);
        }

        view = LayoutInflater.from(context).inflate(R.layout.item_action_comment_detail, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof HeadViewHolder) {
            final HeadViewHolder holder = (HeadViewHolder) viewHolder;
            WebSettings webSettings =   holder.mWebView.getSettings();
            //  webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
            //  webSettings.setLoadWithOverviewMode(true);
            webSettings.setJavaScriptEnabled(true);
            String userId = CartoonApp.getInstance().getUserId();
            if (userId==null)
                userId =" ";
            holder.mWebView.loadUrl(StaticField.HTML_BASE_URL + "/essay/getDetail?essayId="+essay_id+ "&userId="
                    +userId);
            holder.mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    //加载完成
                    holder.mProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    //加载开始
                    holder.mProgressBar.setVisibility(View.VISIBLE);
                }
            });

            FanRen FanRen = new FanRen(context);
            holder.mWebView.addJavascriptInterface(FanRen, "FanRen");

//            if (ss) {
//                holder.mWebView.loadUrl("javascript:document.getElementById('document.getElementById('voteNum').innerText').innerText=parseInt(document.getElementById('voteNum').innerText)+1;");
//            }


            if (mList == null || mList.size() == 0) {
                holder.tvAllCommenrt.setVisibility(View.VISIBLE);
                holder.llEmpty.setVisibility(View.VISIBLE);
            } else {
                holder.tvAllCommenrt.setVisibility(View.GONE);
                holder.llEmpty.setVisibility(View.GONE);
            }
        } else if (viewHolder instanceof ItemViewHolder) {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final int pos = position - 1;

            if (pos == 0) {
                holder.tvAllCommenrt.setVisibility(View.VISIBLE);
                holder.tvAllCommenrt.setText("共"+mList.size()+"条评论");
            } else {
                holder.tvAllCommenrt.setVisibility(View.GONE);
            }




            final ActionOne item = mList.get(pos);

            // holder.tvContent.setTag(new Integer(pos));
          //  holder.setIsRecyclable(false);//去除复用,不然自定义控件复用(加载评论较少,否则会慢)


                holder.tvSort.setText((toatal-pos)+"楼");
            holder.tvName.setText(item.getNickname());
            //holder.tvSpecial.setText(item.geth);
            //适配此处用户名可根据标签渲染
            NicknameColorHelper.setNicknameColor(holder.tvName , item.getUid());
            holder.tvTime.setText(item.getTo_create_time());




            if (!TextUtils.isEmpty(item.getHonorName())) {
                if (!TextUtils.equals(item.getHonorName(),"认证")){

                    holder.tvSpecial.setText(item.getHonorName());
                    holder.tvSpecial.setBackgroundResource(R.drawable.background_full_yellow);
                }
                else{
                    holder.tvSpecial.setText("");
                    holder.tvSpecial.setBackgroundResource(R.mipmap.official);
                }
            } else {
                holder.tvSpecial.setVisibility(View.GONE);
            }

            holder.tvBonus.setText(item.getLvName());
            final String content = item.getTo_content();
            if (!TextUtils.isEmpty(content) && content.length() > 0 && content.charAt(0) == '@' && content.contains(":")) {

                String name = content.substring(1, content.indexOf(":"));
                String real_content = content.substring(content.indexOf(":") + 1, content.length());
                // holder.tvContent.setText(Html.fromHtml("<font color=#406599>"+"@" + name +":"+ "</font>" )+ real_content);
                holder.tvContent.setText(Html.fromHtml("<font color=#3e649b>@" + name + ":</font>" + real_content));
            } else {
                holder.tvContent.setText(content);
            }
            // holder.tvContent.resetState(item.isShrink());

            ImageLoaderUtils.displayRound(context,holder.ivIcon,item.getAvatar());

            //click icon
            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUserAlreadyLogin()) {
                        Intent intent = new Intent(context, OthersMomentActivity.class);
                        intent.putExtra(Keys.TARGET_UID, item.getUid());
                        intent.putExtra(Keys.TARGET_OTHERS, item.getNickname());
                        context.startActivity(intent);
                    }
                    else {
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                }
            });
            holder.tvFocus.setVisibility(View.GONE);

            //            holder.tvDelete.setVisibility(TextUtils.equals(item.getUid(),
            //                    CartoonApp.getInstance().getUserId()) ? View.VISIBLE : View.GONE);

           /* holder.tvTime.setVisibility(TextUtils.equals(item.getUid(),
                    CartoonApp.getInstance().getUserId()) ? View.GONE : View.VISIBLE);*/


            if (TextUtils.equals(item.getUid()+"", CartoonApp.getInstance().getUserId())) {
                holder.tvDelete.setText("删除");
                // holder.ivDelete.setVisibility(View.GONE);
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CartoonApp.getInstance().isLogin(context)) {
                            return;
                        }
                        DeleteDialog deleteDialog = new DeleteDialog(context,"删除");
                        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {
                                deleteComment(item.getTwo_id()+"", holder, pos);
                            }
                        });
                        deleteDialog.show();
                    }
                });
            }else{
                holder.tvDelete.setText("举报");
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CartoonApp.getInstance().isLogin(context)) {
                            return;
                        }
                        DeleteDialog deleteDialog = new DeleteDialog(context,"举报");
                        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {
                                ReportDialog dialog1 = new ReportDialog(context,item.getTo_uid()+"",item.getTwo_id()+"","2");// FIXME: 17-3-30 对应id是否对
                                dialog1.show();
                            }
                        });
                        deleteDialog.show();
                    }
                });
            }

            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onItemClick(item, pos);
                    }
                }
            });

        }
    }
    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }



    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.webView)
        WebView mWebView;
        @BindView(R.id.tv_all_comment)
        TextView  tvAllCommenrt;
        @BindView(R.id.ll_empty)
        TextView llEmpty;
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        int id;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView  ivIcon;
        @BindView(R.id.tv_name)
        TextView         tvName;
        @BindView(R.id.tv_all_comment)
        TextView  tvAllCommenrt;
        @BindView(R.id.tv_time)
        TextView         tvTime;
        @BindView(R.id.tv_focus)
        TextView         tvFocus;
        @BindView(R.id.tv_content)
        ReadMoreTextView tvContent;// FIXME: 17-3-22 no   show
        @BindView(R.id.list_view)
        CustomListView   listView;
        @BindView(R.id.tv_delete)
        TextView         tvDelete;
       /* @BindView(R.id.iv_delete)
        ImageView ivDelete;*/

        @BindView(R.id.ll_item)
        LinearLayout llItem;
        @BindView(R.id.tv_bonus)
        TextView tvBonus;
        @BindView(R.id.tv_special_name)
        TextView tvSpecial;

        @BindView(R.id.tv_sort)
        TextView tvSort;

        private int  id;

        private String bonusPoint;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //            rlBottom.setVisibility(View.GONE);
        }
    }

    /**
     * 删除评论
     * <p>
     * //is_two.用户删除评论的类型  一级评论为针对于漫画听书..    二级评论为针对于一级评论的回复评论
     * //根据参数不同操作的表也不同 ,
     * is_two =1 :删除一级评论 id参数为一级评论的唯一id值
     * is_two=2 :删除二级评论  id参数为二级评论的唯一id值
     */
    private void deleteComment(String comment_id, final ItemViewHolder holder, final int position) {
        ((BaseActivity) context).showLoadingDialog();
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", comment_id)
                .addParams("is_two", "2")
                .build().execute(new BaseCallBack() {
            @Override
            public void onLoadFail() {
                ((BaseActivity) context).hideLoadingDialog();
                ToastUtils.showShort(context, "...fail..");
            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(Object response) {
                ((BaseActivity) context).hideLoadingDialog();
                ToastUtils.showShort(context, "删除成功");
                notifyDataSetChanged();
                Utils.closeSoftKeyboard(holder.tvDelete, (ActionDetailActivity) context);
                notifyItemRangeChanged(position,mList.size());
                if (mList.size() == 1) {
                    mList.remove(0);//防止数组越界
                } else {

                    mList.remove(position);
                }
                notifyItemRemoved(position+1);
            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onItemClick(ActionOne item, int position);
    }
}
