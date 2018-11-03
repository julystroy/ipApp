package com.cartoon.module.cartoon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.CartoonComment;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.action.ActionDetailActivity;
import com.cartoon.module.bangai.BangaiDetailActivity;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.module.bangai.RecommendDetailActivity;
import com.cartoon.module.expound.JiNianDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.module.newmodules.NewBaseActivity;
import com.cartoon.module.tab.bookfriendmoment.GridImageAdapter;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.Utils;
import com.cartoon.view.CustomListView;
import com.cartoon.view.dialog.DeleteDialog;
import com.cartoon.view.ExpandGridView;
import com.cartoon.view.ReadMoreTextView;
import com.cartoon.view.dialog.ReportDialog;
import com.cartoon.view.SelectableImageView;
import com.cartton.library.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 漫画评论适配器
 * <p>
 * Created by David on 16/6/27.
 */
public class CartoonCommentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x101;
    private static final int TYPE_ITEM = 0x102;

    private Context                   context;
    private CartoonComment            comment;
    private List<CartoonCommentChild> mList;
    private int toatal;

    public void setApproveType(int approve_type) {
        this.approve_type = approve_type;
    }

    private int approve_type = Constants.APPROVE_COMMENT;

    private Activity mActivity;
    public CartoonCommentDetailAdapter(Context context,Activity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    public void setData(CartoonComment comment) {
        if (comment == null) return;
        this.comment = comment;
        this.mList = comment.getChildren().getList();

        // list = new ArrayList<>();
         this.toatal =comment.getChildren().getTotalRow();
        notifyDataSetChanged();
    }

    public void addAll(CartoonComment.ChildrenBean list) {
        int size = this.mList.size();
        this.mList.addAll(list.getList());
        this.toatal = list.getTotalRow();
        notifyItemRangeInserted(size + 1, list.getList().size());
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
        return comment == null ? 0 : (mList == null ? 1 : (mList.size() + 1));
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEAD) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_comment2, parent, false);
            return new HeadViewHolder(view);
        }

        view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_comment_detail, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof HeadViewHolder) {
            final HeadViewHolder holder = (HeadViewHolder) viewHolder;
            NicknameColorHelper.setNicknameColor(holder.tvName, comment.getUid());

            holder.tvName.setText(comment.getNickname());
            //适配此处用户名可根据标签渲染
            NicknameColorHelper.setNicknameColor(holder.tvName , holder.id);
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(comment.getCreate_time());

          //  holder.tvContent.setVisibility(TextUtils.isEmpty(comment.getContent())?View.VISIBLE:View.GONE);
            if (TextUtils.isEmpty(comment.getContent())) {
                holder.tvContent.setVisibility(View.GONE);
            } else {
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.tvContent.setText(comment.getContent());
            }
          holder.tvDelete.setVisibility(TextUtils.equals(comment.getUid()+"",
                  CartoonApp.getInstance().getUserId()) ? View.GONE : View.VISIBLE);

                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CartoonApp.getInstance().isLogin(context)) {
                            return;
                        }
                        DeleteDialog deleteDialog = new DeleteDialog(mActivity,"举报");
                        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {
                                ReportDialog dialog1 = new ReportDialog(context,comment.getUid()+"",comment.getId()+"","1");// FIXME: 17-3-30 对应id是否对
                                dialog1.show();
                            }
                        });
                        deleteDialog.show();

                    }
                });

            if (!TextUtils.isEmpty(comment.getHonorName())) {
                if (!TextUtils.equals(comment.getHonorName(),"认证")){

                    holder.tvSpecial.setText(comment.getHonorName());
                    holder.tvSpecial.setBackgroundResource(R.drawable.background_full_yellow);
                }
                else{
                    holder.tvSpecial.setText("");
                    holder.tvSpecial.setBackgroundResource(R.mipmap.official);
                }
            } else {
                holder.tvSpecial.setVisibility(View.GONE);
            }

            holder.tvBonus.setText(comment.getLvName());


            holder.ivCover.setTapListener(new SelectableImageView.ITapListener() {
                @Override
                public void onTaped() {
                    context.startActivity(new Intent(context, PreviewPhotoActivity.class)
                            .putExtra(PreviewPhotoActivity.PHOTO_URLS, comment.getPhoto().toArray(new String[0]))
                            .putExtra(PreviewPhotoActivity.POSITION, 0));
                }
            });

            if (comment.getPhoto() != null && comment.getPhoto().size() > 0) {
                if (comment.getPhoto().size() > 1) {
                    // show gridView
                    holder.gridView.setVisibility(View.VISIBLE);
                    holder.ivCover.setVisibility(View.GONE);
                    holder.gridView.setAdapter(new GridImageAdapter(new WeakReference<>(holder.gridView.getContext()), comment.getPhoto(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int positionPhoto = (int) v.getTag(R.id.position_photo);

                            context.startActivity(new Intent(context, PreviewPhotoActivity.class)
                                    .putExtra(PreviewPhotoActivity.PHOTO_URLS, comment.getPhoto().toArray(new String[0]))
                                    .putExtra(PreviewPhotoActivity.POSITION, positionPhoto));
                        }
                    }, position));
                } else {
                    // show single imageView
                    holder.gridView.setVisibility(View.GONE);
                    holder.ivCover.setVisibility(View.VISIBLE);
                    Glide.with(holder.ivCover.getContext()).load(comment.getPhoto().get(0)).centerCrop().placeholder(R.drawable.default_photo).into(holder.ivCover);
                }
            } else {
                // show single imageView
                holder.gridView.setVisibility(View.GONE);
                holder.ivCover.setVisibility(View.GONE);
            }


            if (comment.getType() == 4 ) {//4:动态
                holder.llCartoon.setVisibility(View.GONE);

                holder.llCartoon.setVisibility(View.GONE);

            } else {//1:漫画   2:听书   3: 嘻说
                holder.llCartoon.setVisibility(View.VISIBLE);
                holder.tvSmallName.setText(comment.getModule_title());

                int placeHolder = R.color.moment_icon_placeholder;
                if (comment.getType() == 2) {
                    placeHolder = R.mipmap.icon_head;
                } else if (comment.getType() == 3) {
                    placeHolder = R.mipmap.icon_jiebang;
                }
                Glide.with(holder.ivSmallCover.getContext()).load(comment.getSmall_pic()).centerCrop().placeholder(placeHolder).into(holder.ivSmallCover);
                holder.llCartoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLinkClick();
                    }
                });
            }


//            if (!TextUtils.isEmpty(smallImg) && TextUtils.isEmpty(smallText)) {
//                holder.llCartoon.setVisibility(View.VISIBLE);
//            }else{
//                holder.llCartoon.setVisibility(View.GONE);
//            }


            holder.tvFocus.setText(comment.getApprove_num());
            if (comment.getIs_approve() == 1) {
                holder.tvFocus.setSelected(true);
                holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
            } else {
                holder.tvFocus.setSelected(false);
                holder.tvFocus.setTextColor(Color.parseColor("#5b5c5e"));
            }

            ImageLoaderUtils.displayRound(context,holder.ivIcon,comment.getAvatar());

            holder.tvFocus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiUtils.approveTarget(comment.getId()+"", approve_type, new ApiQuestListener() {
                        @Override
                        public void onLoadFail() {

                        }

                        @Override
                        public void onLoadSuccess(String response) {
                            comment.setIs_approve(1);
                            comment.setApprove_num((Integer.parseInt(comment.getApprove_num()) + 1)+"");

                            notifyItemChanged(position);
                            if (null != context && context instanceof CartoonCommentDetailActivity)
                                ((CartoonCommentDetailActivity) context).notifyItemChange(comment.getIs_approve(), Integer.parseInt(comment.getApprove_num()) + 1);


                        }
                    });
                }
            });
            if (mList == null || mList.size() == 0) {
                holder.llEmpty.setVisibility(View.VISIBLE);
            } else {
                holder.llEmpty.setVisibility(View.GONE);
            }



        } else if (viewHolder instanceof ItemViewHolder) {
            final ItemViewHolder holder = (ItemViewHolder) viewHolder;
            final int pos = position - 1;

            if (pos == 0) {
                holder.tvAllCommenrt.setVisibility(View.VISIBLE);
                holder.tvAllCommenrt.setText("共"+toatal+"评论");
            } else {
                holder.tvAllCommenrt.setVisibility(View.GONE);
            }


            final CartoonCommentChild item = mList.get(pos);

           // holder.tvContent.setTag(new Integer(pos));
            holder.setIsRecyclable(false);//去除复用,不然自定义控件复用(加载评论较少,否则会慢)


                holder.tvSort.setText((toatal-pos)+"楼");
            holder.tvName.setText(item.getNickname());
            //holder.tvSpecial.setText(item.geth);
            //适配此处用户名可根据标签渲染
            NicknameColorHelper.setNicknameColor(holder.tvName , item.getUid());
            holder.tvTime.setText(item.getTo_create_time());

           // holder.id = Integer.parseInt(comment.getUid());


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
            String content = item.getTo_content();
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
                holder.tvDelete.setVisibility(View.VISIBLE);
               // holder.ivDelete.setVisibility(View.GONE);
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CartoonApp.getInstance().isLogin(context)) {
                            return;
                        }
                        DeleteDialog deleteDialog = new DeleteDialog(mActivity,"删除");
                        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {

                                deleteComment(item.getTwo_id()+"", holder, pos);
                            }
                        });
                        deleteDialog.show();
                    }
                });
            } /*else {
                holder.tvDelete.setVisibility(View.GONE);
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!CartoonApp.getInstance().isLogin(context)) {
                            return;
                        }
                        DeleteDialog deleteDialog = new DeleteDialog(mActivity,"举报");
                        deleteDialog.setOnButtonClickListener(new DeleteDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {
                                ReportDialog dialog1 = new ReportDialog(context,item.getTo_uid(),item.getTwo_id(),2);// FIXME: 17-3-30 对应id是否对
                                dialog1.show();
                            }
                        });
                        deleteDialog.show();

                    }
                });
            }*/

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
    private void onLinkClick() {
 if (comment.getType() == 8) {

            Intent intent = new Intent(context, JiNianDetailActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(comment.getModule_id()));
            intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_BANGAI);
            intent.putExtra("title", String.valueOf(comment.getModule_title()));
            intent.putExtra("pic", String.valueOf(comment.getSmall_pic()));
            context.startActivity(intent);
        } else if ( comment.getType()==0) {
            Intent intent = new Intent(mActivity, NewBaseActivity.class);
            intent.putExtra(Keys.TARGET_ID, String.valueOf(comment.getModule_id()));
            intent.putExtra(Keys.TARGET_OBJECT, "");
            intent.putExtra(Keys.COMMENT_TYPE, comment.getType());
            context.startActivity(intent);
 } else if (comment.getType() == 7) {
     Intent intent = new Intent(mActivity, BangaiDetailActivity.class);
     intent.putExtra(Keys.TARGET_ID, comment.getModule_id()+"");
     intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_QMAN);
    // BangaiReadedUtil.getInstance().addReadedId(Integer.parseInt(comment.getModule_id()));
     context.startActivity(intent);
 } else if (comment.getType() == 11) {
     Intent intent = new Intent(context, ActionDetailActivity.class);
     intent.putExtra(Keys.CHART, comment.getModule_id()+"");
     intent.putExtra(Keys.CHART_USEID, comment.getUid()+"");
     intent.putExtra(Keys.CHART_BOOLEAN, "");
     context.startActivity(intent);
 } else if (comment.getType() == 3) {
     Intent intent = new Intent(context, NovelDetailActivity.class);
     intent.putExtra(Keys.TARGET_ID, comment.getModule_id()+"");
     intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
     intent.putExtra(Keys.URL_TYPE, 0);
     intent.putExtra("isRead", "0");
     context.startActivity(intent);
 }  else if (comment.getType()==9){
     Intent  intent = new Intent(mActivity, RecommendDetailActivity.class);
     intent.putExtra(Keys.TARGET_ID, String.valueOf(comment.getModule_id()));
     context.startActivity(intent);
 }else {
     ToastUtils.showShort(context, "内容被清理");
 }
    }



    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView             ivIcon;
        @BindView(R.id.tv_name)
        TextView                    tvName;
        @BindView(R.id.tv_time)
        TextView                    tvTime;
        @BindView(R.id.tv_focus)
        TextView                    tvFocus;
        @BindView(R.id.tv_comment)
        TextView                    tvComment;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.list_view)
        CustomListView              listView;
        @BindView(R.id.tv_all_comment)
        TextView                    tvAllComment;
        @BindView(R.id.tv_delete)
        TextView                    tvDelete;

        @BindView(R.id.iv_cartoon_cover)
        ImageView ivSmallCover;
        @BindView(R.id.tv_cartoon_name)
        TextView tvSmallName;
        @BindView(R.id.ll_cartoon)
        LinearLayout llCartoon;
        @BindView(R.id.gridView)
        ExpandGridView gridView;
        @BindView(R.id.iv_cover)
        SelectableImageView ivCover;
        @BindView(R.id.tv_bonus)
        TextView tvBonus;
        @BindView(R.id.tv_special_name)
        TextView tvSpecial;
        @BindView(R.id.ll_emptyview)
        LinearLayout  llEmpty;

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
        TextView         tvAllCommenrt;
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
                Utils.closeSoftKeyboard(holder.tvDelete, (CartoonCommentDetailActivity) context);
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
        void onItemClick(CartoonCommentChild item, int position);
    }
}
