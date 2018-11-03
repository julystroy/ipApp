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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.CommentData;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.view.dialog.DeleteDialog;
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
public class CartoonCommentAdapter extends RecyclerView.Adapter<CartoonCommentAdapter.ViewHolder> {

    private int total;

    /**
     * 二级评论触发
     */
    public interface OnItemCommentListener {
        public void onItemCommentClick(CommentData comment, int position);

        public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position);
    }

    private Context context;
    private List<CommentData> mList;
    private OnItemCommentListener listener;

    private Activity mActivity;
    public CartoonCommentAdapter(Context context,Activity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    public void setData(List<CommentData> mList, int total) {
        this.mList = mList;
        this.total = total;
        notifyDataSetChanged();
    }

    public void addAll(List<CommentData> list,int total) {
        int size = this.mList.size();
        this.mList.addAll(list);
        this.total = total;
        notifyItemRangeInserted(size + 1, list.size());
    }


    public void setListener(OnItemCommentListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CommentData comment = mList.get(position);
        final int pos = position;
        if (mList==null||mList.size()==0){
            holder.llempty.setVisibility(View.VISIBLE);
        }else{
            holder.llempty.setVisibility(View.GONE);
        }

        holder.tvBuild.setText(total-pos+"楼");
        holder.tvName.setText(comment.getNickname());
        NicknameColorHelper.setNicknameColor(holder.tvName, comment.getUid());

        holder.tvTime.setText(comment.getCreate_time());
        holder.tvContent.setText(comment.getContent());
        if (TextUtils.equals(comment.getComment_num()+"","0"))
            holder.tvComment.setText("回复");
        else
            holder.tvComment.setText(comment.getComment_num()+"条回复");
        holder.tvFocus.setText(comment.getApprove_num());


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
       /* if (!TextUtils.isEmpty(comment.getHonorName())) {
            holder.tvSpecial.setText(comment.getHonorName());
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }*/

        holder.tvBonus.setText(comment.getLvName());

        if (comment.getIs_approve() == 1) {
            holder.tvFocus.setSelected(true);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            holder.tvFocus.setSelected(false);
            holder.tvFocus.setTextColor(Color.parseColor("#5b5c5e"));
        }
//        holder.tvAllComment.setVisibility(comment.getChildren() == null ?
//                View.GONE : (comment.getChildren().size() > 3 ? View.VISIBLE : View.GONE));
//
//        holder.tvAllComment.setText(String.format(Locale.getDefault(), "查看全部%s条评论", comment.getComment_num()));
//        holder.tvDelete.setVisibility(TextUtils.equals(comment.getUid(),
//                CartoonApp.getInstance().getUserId()) ? View.VISIBLE : View.GONE);

        if (TextUtils.equals(comment.getUid(), CartoonApp.getInstance().getUserId())) {
            holder.tvDelete.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.GONE);
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
                            deleteComment(comment.getId(), holder);
                        }
                    });
                    deleteDialog.show();

                }
            });
        } else {
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
                            ReportDialog dialog1 = new ReportDialog(context,comment.getId(),comment.getUid(),"1");
                            dialog1.show();
                        }
                    });
                    deleteDialog.show();
                }
            });
        }

//        holder.listView.setAdapter(new ChildAdapter(comment.getChildren()));
//        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CartoonCommentChild child = comment.getChildren().get(position);
//                if (child != null && listener != null) {
//                    listener.onItemCommentClick(comment.getId(), child, position);
//                }
//            }
//        });


        ImageLoaderUtils.displayRound(context,holder.ivIcon,comment.getAvatar());
       /* if (String.valueOf(comment.getUid()).equals(CartoonApp.getInstance().getUserId())) {
            //click icon
            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUserAlreadyLogin())
                        context.startActivity(new Intent(context, MyMomentActivity.class));
                    else
                        context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        } else {
            holder.id = Integer.parseInt(comment.getUid());

        }*/
        //click icon
        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAlreadyLogin()) {

                    Intent intent = new Intent(context, OthersMomentActivity.class);
                    intent.putExtra(Keys.TARGET_UID, comment.getUid());
                    intent.putExtra(Keys.TARGET_OTHERS, comment.getNickname());
                    context.startActivity(intent);
                }
                //// FIXME: 16-11-30  代入参数
                else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }

                Intent intent = new Intent(context, CartoonCommentDetailActivity.class);
                intent.putExtra(Keys.COMMENT_ID, comment.getId());
                intent.putExtra(Keys.SOURCE, "漫画");

                context.startActivity(intent);
            }
        });

        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }
                if (listener != null) {
                    listener.onItemCommentClick(comment, position);
                }
            }
        });
//        holder.tvAllComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!CartoonApp.getInstance().isLogin(context)) {
//                    return;
//                }
//
//                Intent intent = new Intent(context, CartoonCommentDetailActivity.class);
//                intent.putExtra(Keys.COMMENT_ID, comment.getId());
//                intent.putExtra(Keys.SOURCE, "漫画");
//
//                context.startActivity(intent);
//            }
//        });


        holder.tvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiUtils.approveTarget(comment.getId(), Constants.APPROVE_COMMENT, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        comment.setIs_approve(1);
                        int num = Integer.parseInt(comment.getApprove_num());
                        comment.setApprove_num(String.valueOf(num + 1));
                        notifyItemChanged(position);
                    }
                });
            }
        });
    }
    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 删除评论
     * <p>
     * //is_two.用户删除评论的类型  一级评论为针对于漫画听书..    二级评论为针对于一级评论的回复评论
     * //根据参数不同操作的表也不同 ,
     * is_two =1 :删除一级评论 id参数为一级评论的唯一id值
     * is_two=2 :删除二级评论  id参数为二级评论的唯一id值
     */
    private void deleteComment(String comment_id, final ViewHolder holder) {
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", comment_id)
                .addParams("is_two", "1")
                .build().execute(new BaseCallBack() {
            @Override
            public void onLoadFail() {
                ToastUtils.showShort(context, "删除失败");
            }
            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Object response) {
                ToastUtils.showShort(context, "删除成功");
                mList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, Object.class);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView             ivIcon;
        @BindView(R.id.tv_name)
        TextView                    tvName;
        @BindView(R.id.tv_time)
        TextView  tvTime;
        @BindView(R.id.tv_focus)
        TextView  tvFocus;
        @BindView(R.id.tv_comment)
        TextView  tvComment;
        @BindView(R.id.tv_content)
        TextView  tvContent;
//        @BindView(R.id.list_view)
//        CustomListView              listView;
//        @BindView(R.id.tv_all_comment)
//        TextView                    tvAllComment;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_delete)
        TextView tvDelete;
//        @BindView(R.id.view_split)
//        View                        viewSplit;
        @BindView(R.id.tv_bonus)
        TextView  tvBonus;
        @BindView(R.id.tv_special_name)
        TextView  tvSpecial;
        @BindView(R.id.tv_build)
        TextView  tvBuild;
        @BindView(R.id.ll_empty)
        TextView llempty;

        private int id;
        private String bonusPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChildAdapter extends BaseAdapter {

        private List<CartoonCommentChild> list;

        public ChildAdapter(List<CartoonCommentChild> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : (list.size() > 3 ? 3 : list.size());
        }

        @Override
        public CartoonCommentChild getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder;
            if (convertView == null) {
                holder = new MyHolder();
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.item_child_list, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(holder);
            } else {
                holder = (MyHolder) convertView.getTag();
            }
            CartoonCommentChild item = list.get(position);

            String content = item.getTo_content();
            if (!TextUtils.isEmpty(content) && content.length() > 0 && content.charAt(0) == '@' && content.contains(":")) {

                String name = content.substring(1, content.indexOf(":"));
                String real_content = content.substring(content.indexOf(":") + 1, content.length());

                holder.textView.setText(Html.fromHtml(
                        "<font color=#3e649b>" + item.getNickname() + "@</font>" +
                                "<font color=#3e649b>" + name + ":</font>" +
                                real_content));
            } else {

                holder.textView.setText(Html.fromHtml(
                        "<font color=#3e649b>" + item.getNickname() + ":</font>" + item.getTo_content()));
            }

            return convertView;
        }
    }

    class MyHolder {
        private TextView textView;
    }
}
