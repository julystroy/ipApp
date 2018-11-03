package com.cartoon.module.bangai;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.Constants;
import com.cartoon.bean.Bangai;
import com.cartoon.data.CartoonComment;
import com.cartoon.data.CartoonCommentChild;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.module.BaseActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.ApiUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.view.CustomListView;
import com.cartton.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 16-12-8.
 */
public class BangaiCommentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int sort_type = BangaiDetailActivity.TYPE_LATEST;

    /**
     * 二级评论触发
     */
    public interface OnItemCommentListener {
        public void onItemCommentClick(CartoonComment comment, int position);

        public void onItemClick(CartoonComment comment, int position);

        public void onItemCommentClick(String comment_id, CartoonCommentChild child, int position);

    }

    public interface OnItemCommentTypeChange {
        public void OnItemCommentTypeChange(int type);
    }

    private Context                 context;
    private Bangai                  expound;
    private String                  totalNum;
    private List<CartoonComment>    mList;
    private OnItemCommentListener   listener;
    private OnItemCommentTypeChange changeListener;

    private Activity mActivity;
    public BangaiCommentDetailAdapter(Context context, Activity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    public void setExpound(Bangai expound) {
        this.expound = expound;
    }

    public void setData(List<CartoonComment> mList, String total) {
        this.mList = mList;
        this.totalNum = total;
        notifyDataSetChanged();
    }

    public void addAll(List<CartoonComment> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemCommentListener listener) {
        this.listener = listener;
    }

    public void setChangeListener(OnItemCommentTypeChange changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_comment, parent, false);

        return new CMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final int pos = position ;
        final CMViewHolder holder = (CMViewHolder) viewHolder;
        final CartoonComment comment = mList.get(pos);

        holder.tvName.setText(comment.getNickname());
        NicknameColorHelper.setNicknameColor(holder.tvName, comment.getUid());

        holder.tvTime.setText(comment.getCreate_time());
        holder.tvContent.setText(comment.getContent());
        holder.tvComment.setText(comment.getComment_num());
        holder.tvFocus.setText(comment.getApprove_num());

        if (!TextUtils.isEmpty(comment.getHonorName())) {
            if (!TextUtils.equals(comment.getHonorName(),"认证")){

                holder.tvSpecial.setText(comment.getHonorName());
                holder.tvSpecial.setBackgroundResource(R.drawable.transparent_background);
            }
            else{
                holder.tvSpecial.setText("");
                holder.tvSpecial.setBackgroundResource(R.mipmap.official);
            }
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }
        /*if (!TextUtils.isEmpty(comment.getHonorName())) {

            holder.tvSpecial.setText(comment.getHonorName());
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }*/
        holder.tvBonus.setText(comment.getLvName());

        if (comment.getIs_approve() == 1) {
            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_fcous_f, 0, 0, 0);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_focus, 0, 0, 0);
            holder.tvFocus.setTextColor(Color.parseColor("#929292"));
        }

        if (comment.getChildren() != null && (comment.getChildren().getList().size() > 3)) {
            holder.tvAllComment.setVisibility(View.VISIBLE);
            holder.tvAllComment.setText(String.format(Locale.getDefault(), "查看全部%s条回复", comment.getComment_num()));
        } else {
            holder.tvAllComment.setVisibility(View.GONE);
        }


        holder.tvDelete.setVisibility(TextUtils.equals(comment.getUid(),
                CartoonApp.getInstance().getUserId()) ? View.VISIBLE : View.GONE);
        holder.listView.setAdapter(new ChildAdapter(comment.getChildren().getList()));
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CartoonCommentChild child = comment.getChildren().getList().get(position);
                if (child != null && listener != null) {
                    listener.onItemCommentClick(comment.getId(), child, position);
                }
            }
        });
        Glide.with(context)
                .load(comment.getAvatar())
                .placeholder(R.mipmap.icon_user_default)
                .error(R.mipmap.icon_user_default)
                .centerCrop()
                .into(holder.ivIcon);
        /*if (TextUtils.equals(comment.getUid(), CartoonApp.getInstance().getUserId())) {
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
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }

                if (listener != null) {
                    listener.onItemCommentClick(comment, pos);
                }
            }
        });
        holder.tvAllComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }

                Intent intent = new Intent(context, CartoonCommentDetailActivity.class);
                intent.putExtra(Keys.COMMENT_ID, comment.getId());
                intent.putExtra(Keys.SOURCE, "番外");
                context.startActivity(intent);
            }
        });

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(comment, pos);
                }
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }
                deleteComment(comment.getId(), holder, pos);
            }
        });

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

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }


    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }



    /**
     * 删除评论
     * <p/>
     * //is_two.用户删除评论的类型  一级评论为针对于漫画听书..    二级评论为针对于一级评论的回复评论
     * //根据参数不同操作的表也不同 ,
     * is_two =1 :删除一级评论 id参数为一级评论的唯一id值
     * is_two=2 :删除二级评论  id参数为二级评论的唯一id值
     */
    private void deleteComment(String comment_id, final CMViewHolder holder, final int position) {
        ((BaseActivity) context).showLoadingDialog();
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_DEL_COMMENT)
                .addParams("id", comment_id)
                .addParams("is_two", "1")
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

    class CMViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView      ivIcon;
        @BindView(R.id.tv_name)
        TextView       tvName;
        @BindView(R.id.tv_time)
        TextView       tvTime;
        @BindView(R.id.tv_focus)
        TextView       tvFocus;
        @BindView(R.id.tv_comment)
        TextView       tvComment;
        @BindView(R.id.tv_content)
        TextView       tvContent;
        @BindView(R.id.list_view)
        CustomListView listView;
        @BindView(R.id.tv_all_comment)
        TextView       tvAllComment;
        @BindView(R.id.tv_delete)
        TextView       tvDelete;
        @BindView(R.id.ll_item)
        LinearLayout   llItem;
        @BindView(R.id.tv_bonus)
        TextView       tvBonus;
        @BindView(R.id.tv_special_name)
        TextView       tvSpecial;

        private int id;
        private String bonusPoint;
        public CMViewHolder(View itemView) {
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

    private int getPosition(ArrayList<String> list, String item) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(item)) {
                    return i;
                }
            }
        }

        return 0;
    }


}
