package com.cartoon.module.mymessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.Keys;
import com.cartoon.data.MyMessage;
import com.cartoon.data.MyMessageResource;
import com.cartoon.data.UserInfo;
import com.cartoon.module.cartoon.CartoonBookDetailActivity;
import com.cartoon.module.cartoon.CartoonCommentDetailActivity;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.view.CircleImageView;
import com.cartoon.view.DialogToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/22/16.
 */

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyViewHolder> {

    private final Context context;
    private List<MyMessage> list;

    public MyMessageAdapter(List<MyMessage> list, Context context) {
        this.list = list;
        this.context=context;
    }

    public void addAll(List<MyMessage> list) {
        int size = this.list.size();
        for (int i = 0; i < list.size(); i++) {
            this.list.add(list.get(i));
            notifyItemInserted(size + i);
        }
    }

    public void removeItem(int position) {
        /*list.remove(position);
        notifyItemRemoved(position);*/
        notifyItemRangeChanged(position,list.size());
        if (list.size() == 1) {
            list.remove(0);//防止数组越界
        } else {

            list.remove(position);
        }
        notifyItemRemoved(position+1);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyMessage message = list.get(position);

        if (message.getU_id() == 1 || message.getType() == 0 || message.getRes_id() == 1) {// admin or editor
            NicknameColorHelper.setNicknameColor(holder.tvNickname, 1);
        } else {
            NicknameColorHelper.setNicknameColor(holder.tvNickname, message.getU_id());
        }

        if (message.getType() == 2) {
            holder.ivApprove.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.ivApprove.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.VISIBLE);
        }

        holder.tvNickname.setText(message.getNickname());

        NicknameColorHelper.setNicknameColor(holder.tvNickname , message.getOtherId());

        holder.tvDate.setText(message.getCreate_time());


        holder.tvContent.setText(message.getContent());
        MyMessageResource resource = message.getResource();
        if (resource != null) {
            holder.mFrameLayout.setVisibility(View.VISIBLE);
            if (resource.getType() == 10) {
                String[] photo = resource.getPhoto();
                if (photo != null && photo.length>0) {
                    holder.ivMyCover.setVisibility(View.VISIBLE);
                    //holder.tvContent.setText(resource.getContent());
                    holder.tvMyMoment.setVisibility(View.GONE);
                    Glide.with(holder.ivMyCover.getContext()).load(photo[0]).centerCrop().placeholder(R.drawable.default_photo).into(holder.ivMyCover);
                }else{
                    holder.ivMyCover.setVisibility(View.GONE);
                    holder.tvMyMoment.setVisibility(View.VISIBLE);
                   // holder.tvMyMoment.setText(RegularUtils.getContent(resource.getContent()));
                    holder.tvMyMoment.setText(resource.getModule_title());
                }
            }
            else if ( isURL(resource.getContent())) {// type=4 content为图片链接
                holder.ivMyCover.setVisibility(View.VISIBLE);
                //holder.tvContent.setText(resource.getContent());
                holder.tvMyMoment.setVisibility(View.GONE);
                Glide.with(holder.ivMyCover.getContext()).load(resource.getContent()).centerCrop().placeholder(R.drawable.default_photo).into(holder.ivMyCover);

            } else {
                holder.ivMyCover.setVisibility(View.GONE);
                holder.tvMyMoment.setVisibility(View.VISIBLE);
                //String reg = "[^\u4e00-\u9fa5]";
               // holder.tvMyMoment.setText(resource.getContent().replaceAll(reg, ""));
                if (resource.getType()==11)
                    holder.tvMyMoment.setText(resource.getModule_title());
                else
                    holder.tvMyMoment.setText(resource.getContent());
            }
        } else {
            holder.mFrameLayout.setVisibility(View.GONE);
            //holder.tvMyMoment.setVisibility(View.GONE);
        }

        holder.llMymessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != callBack) callBack.onItemLongClick(message, position);
                return false;
            }
        });

        holder.llMymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = message.getType();
                //"type":" 1 "//:0.系统消息 1.评论   2.点赞 3, // 我的消息-类型 3=宗门消息4, //我的消息-类型 4=章节答题

                if (type == 0) {

                } else if (type == 1 || type == 2) {// 评论
                    String source = "";
                    //                Type:活动=0 漫画=1 听书=2 连载=3 (动态 书友圈=4 评论=5) 追听=6 次元=7 纪年=8 静态电影=9
                    if (message.getResource() == null) {
                        DialogToast.createToastConfig().ToastShow(context,"内容已被删除",2);
                        return;
                    }

                    int resType = message.getResource().getType();

                    if (resType == 2) {
                        source = "玄门听书";
                    }
                    if (resType == 3) {
                        source = "仙界连载";
                    }else if (resType == 8) {
                        source = "凡人纪年";
                    } else if (resType == 1) {
                        source = "漫画";
                    } else if (0==resType ) {
                        source =  "活动";
                    } else if (7 == resType) {
                        source = "凡人次元";
                    }

                    int targetId = message.getResource().getId();
                    Bundle bundle = new Bundle();
                    bundle.putString(Keys.COMMENT_ID, String.valueOf(targetId));
                    BookFriendMoment moment = new BookFriendMoment();
                    moment.setId(targetId);
                    moment.setUid(message.getU_id());
                    moment.setNickname(message.getNickname());
                    bundle.putSerializable(Keys.MOMENT, moment);


                    bundle.putString(Keys.SOURCE, source);

                    if (message.getResource().getType()==10)
                        holder.llMymessage.getContext().startActivity(
                                new Intent(holder.llMymessage.getContext(), CartoonBookDetailActivity.class).putExtras(bundle));
                    else
                    holder.llMymessage.getContext().startActivity(
                            new Intent(holder.llMymessage.getContext(), CartoonCommentDetailActivity.class).putExtras(bundle));
                }
            }
        });
        Glide.with(holder.ivAvatar.getContext()).load(message.getAvatar()).centerCrop().placeholder(R.mipmap.icon_head).into(holder.ivAvatar);
        /*if (String.valueOf(message.getOtherId()).equals(CartoonApp.getInstance().getUserId())) {
            //click icon
            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUserAlreadyLogin())
                        context.startActivity(new Intent(context, MyMomentActivity.class));
                    else
                        context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        } else {

        }*/
        //click icon
        holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserAlreadyLogin()) {
                    Intent intent = new Intent(context, OthersMomentActivity.class);
                    intent.putExtra(Keys.TARGET_UID, message.getOtherId()+ "");
                    intent.putExtra(Keys.TARGET_OTHERS, message.getNickname());
                    context.startActivity(intent);
                    //// FIXME: 16-11-30  代入参数
                }else{

                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            }
        });
        holder.tvBonus.setTag(position);
        holder.tvSpecial.setTag(position);

        if (!TextUtils.isEmpty(message.getHonorName())) {
            if (!TextUtils.equals(message.getHonorName(),"认证")){

                holder.tvSpecial.setText(message.getHonorName());
                holder.tvSpecial.setBackgroundResource(R.drawable.background_full_yellow);
            }
            else{
                holder.tvSpecial.setText("");
                holder.tvSpecial.setBackgroundResource(R.mipmap.official);
            }
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(message.getLvName()))
        holder.tvBonus.setText(message.getLvName());
        else
            holder.tvBonus.setVisibility(View.GONE);
    }
    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }
    private boolean isURL(String content) {
        if (TextUtils.isEmpty(content)) return false;

        if (content.contains("http")) {
            return true;
        }

        return false;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        CircleImageView             ivAvatar;
        @BindView(R.id.tv_nickname)
        TextView                    tvNickname;
        @BindView(R.id.tv_date)
        TextView                    tvDate;
        @BindView(R.id.tv_delete)
        TextView                    tvDelete;
        @BindView(R.id.tv_content)
        TextView tvContent;

        @BindView(R.id.fl_momment)
        FrameLayout  mFrameLayout;
        @BindView(R.id.tv_my_moment)
        TextView                    tvMyMoment;
        @BindView(R.id.iv_my_cover)
        ImageView                   ivMyCover;
        @BindView(R.id.ll_mymessage)
        LinearLayout                llMymessage;
        @BindView(R.id.iv_approve)
        TextView                    ivApprove;
        @BindView(R.id.tv_bonus)
        TextView                    tvBonus;
        @BindView(R.id.tv_special_name)
        TextView                    tvSpecial;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;

    public interface CallBack {
        void onItemLongClick(MyMessage message, int position);
    }

}
