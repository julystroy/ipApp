package com.cartoon.module.tab.bookfriendmoment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.BookFriendMoment;
import com.cartoon.data.Keys;
import com.cartoon.data.UserInfo;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.NicknameColorHelper;
import com.cartoon.utils.RegularUtils;
import com.cartoon.view.ExpandGridView;
import com.cartoon.view.SelectableImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public class BookFriendMomentAdapter extends RecyclerView.Adapter<BookFriendMomentAdapter.MyViewHolder> {

    private List<BookFriendMoment> list;


    private View.OnClickListener onClickSubViewListener;

    private  Activity activity;

    public BookFriendMomentAdapter(List<BookFriendMoment> list, Activity activity) {
        this.activity=activity;
        this.list = list;
    }

    public void addAll(List<BookFriendMoment> list) {
        this.list.addAll(list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_frend_moment_item, parent, false);
        return new MyViewHolder(v);
    }


    public BookFriendMoment getItem(int position) {
        return list.get(position);
        //class com.cartoon.data.EventMomentItemChanged to subscribing class class com.cartoon.module.tab.BookCommentFragment
       // java.lang.IndexOutOfBoundsException: Index: 14, Size: 10
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BookFriendMoment moment = list.get(position);
        holder.tvNickname.setText(moment.getNickname());
        NicknameColorHelper.setNicknameColor(holder.tvNickname, moment.getUid());

        holder.tvDate.setText(moment.getCreate_time());
        if (moment.getSect_user_rank()!=null)
            holder.tvSectName.setText(moment.getSect_user_rank());
        else
            holder.tvSectName.setText("散修");
        if (!TextUtils.isEmpty(moment.getContent()) && !TextUtils.isEmpty(moment.getContent().trim())) {
            holder.tvContent.setVisibility(View.VISIBLE);
            if (moment.getType() == 10) {
                String content = RegularUtils.getContent(moment.getContent());
                if (content!=null&&!content.isEmpty())
                holder.tvContent.setText(content);
                else
                    holder.tvContent.setVisibility(View.GONE);
            } else {

                holder.tvContent.setText(moment.getContent());
            }
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(moment.getIs_recomm()) && !TextUtils.isEmpty(moment.getIs_recomm().trim())) {
            Log.i("xxx", moment.getIs_recomm());
            if (moment.getIs_recomm().equals("1")){
                holder.tvStick.setVisibility(View.VISIBLE);
                holder.tvDate.setVisibility(View.GONE);}
            else {
                holder.tvStick.setVisibility(View.GONE);
                holder.tvDate.setVisibility(View.VISIBLE);
            }
        } else {
            Log.i("xxx", moment.getIs_recomm());
        }
        holder.tvLike.setText(String.valueOf(moment.getApprove_num()));
        holder.tvComment.setText(String.valueOf(moment.getComment_num()));
        holder.tvLike.setTag(position);
        holder.tvComment.setTag(position);
        holder.tvContent.setTag(position);
        holder.rlItem.setTag(position);
        holder.tvStick.setTag(position);
        holder.llCartoon.setTag(position);
        holder.tvBonus.setTag(position);
        holder.tvSpecial.setTag(position);
        holder.tvDelete.setTag(position);
        holder.textViewDelte.setTag(position);
        holder.ivCover.setTag(R.id.position_book_friend_moment, position);
        holder.tvLike.setOnClickListener(onClickSubViewListener);
        holder.tvComment.setOnClickListener(onClickSubViewListener);
        holder.tvContent.setOnClickListener(onClickSubViewListener);
        holder.llCartoon.setOnClickListener(onClickSubViewListener);
        holder.ivCover.setTapListener(new SelectableImageView.ITapListener() {
            @Override
            public void onTaped() {
                onClickSubViewListener.onClick(holder.ivCover);
            }
        });
        holder.rlItem.setOnClickListener(onClickSubViewListener);

        if (String.valueOf(moment.getUid()).equals(CartoonApp.getInstance().getUserId())) {
            holder.textViewDelte.setVisibility(View.VISIBLE);
            holder.tvDelete.setVisibility(View.GONE);
            holder.textViewDelte.setOnClickListener(onClickSubViewListener);
        } else {
            holder.tvDelete.setVisibility(View.VISIBLE);
            holder.textViewDelte.setVisibility(View.GONE);
            holder.tvDelete.setOnClickListener(onClickSubViewListener);
        }
        if (!TextUtils.isEmpty(moment.getLvName())) {
            holder.tvBonus.setText(moment.getLvName());
        } else {
            holder.tvBonus.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(moment.getHonorName())) {
            if (!TextUtils.equals(moment.getHonorName(),"认证")){

                holder.tvSpecial.setText(moment.getHonorName());
                holder.tvSpecial.setBackgroundResource(R.drawable.background_full_yellow);
            }
            else{
                holder.tvSpecial.setText("");
                holder.tvSpecial.setBackgroundResource(R.mipmap.official);
            }
        } else {
            holder.tvSpecial.setVisibility(View.GONE);
        }


            holder.ivAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUserAlreadyLogin()) {
                        Intent intent = new Intent(activity, OthersMomentActivity.class);
                        intent.putExtra(Keys.TARGET_UID, moment.getUid() + "");
                        intent.putExtra(Keys.TARGET_OTHERS, moment.getNickname());
                        activity.startActivity(intent);
                        //// FIXME: 16-11-30  代入参数
                    }else
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            });

        if (1 == moment.getIs_approve()) {
            holder.tvLike.setSelected(true);
            holder.tvLike.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            holder.tvLike.setSelected(false);
            holder.tvLike.setTextColor(Color.parseColor("#5b5c5e"));
        }

        ImageLoaderUtils.displayRound(activity,holder.ivAvatar,moment.getAvatar());

        if (moment.getPhoto() != null && moment.getPhoto().size() > 0) {
            if (moment.getPhoto().size() > 1) {
                // show gridView
                holder.gridView.setVisibility(View.VISIBLE);
                holder.ivCover.setVisibility(View.GONE);
                holder.gridView.setAdapter(new GridImageAdapter(new WeakReference<>(holder.gridView.getContext()), moment.getPhoto(), onClickSubViewListener, position));
            } else {
                // show single imageView
                holder.gridView.setVisibility(View.GONE);
                holder.ivCover.setVisibility(View.VISIBLE);
                Glide.with(holder.ivCover.getContext()).load(moment.getPhoto().get(0)).centerCrop().placeholder(R.drawable.default_photo).into(holder.ivCover);
            }
        } else {
            // show single imageView
            holder.gridView.setVisibility(View.GONE);
            holder.ivCover.setVisibility(View.GONE);
        }


        if (moment.getType() == 4||moment.getType()==10) {//4:动态
            if (moment.getType() == 10&&moment.getModule_title()!=null) {
                holder.tvBookcommentTitle.setVisibility(View.VISIBLE);
                holder.tvBookcommentTitle.setText(moment.getModule_title());
            }else holder.tvBookcommentTitle.setVisibility(View.GONE);

            holder.llCartoon.setVisibility(View.GONE);

        } else {//1:漫画   2:听书   3: 嘻说

            holder.llCartoon.setVisibility(View.VISIBLE);
           // int placeHolder = R.color.moment_icon_placeholder;

         //   if (moment.getType() == 2) {
            int    placeHolder = R.mipmap.ic_launcher;
          //  }
            Glide.with(holder.ivCartoonCover.getContext()).load(moment.getSmall_pic()).centerCrop().placeholder(placeHolder).into(holder.ivCartoonCover);
            holder.tvCartoonName.setText(moment.getModule_title());
        }

    }
    private boolean isUserAlreadyLogin() {
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        return userInfo != null;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickSubViewListener(View.OnClickListener onClickSubViewListener) {
        this.onClickSubViewListener = onClickSubViewListener;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_nickname)
        TextView                         tvNickname;
        @BindView(R.id.tv_date)
        TextView                         tvDate;
        @BindView(R.id.tv_delete)
        TextView                         textViewDelte;
        @BindView(R.id.iv_delete)
        ImageView                        tvDelete;
        @BindView(R.id.tv_stick)
        ImageView                        tvStick;
        @BindView(R.id.tv_content)
        TextView                 tvContent;
        @BindView(R.id.gridView)
        ExpandGridView                   gridView;
        @BindView(R.id.iv_cover)
        SelectableImageView              ivCover;
        @BindView(R.id.iv_cartoon_cover)
        ImageView                        ivCartoonCover;
        @BindView(R.id.tv_cartoon_name)
        TextView                         tvCartoonName;
        @BindView(R.id.ll_cartoon)
        LinearLayout                     llCartoon;
        @BindView(R.id.view_line)
        View                             viewLine;
        @BindView(R.id.tv_like)
        TextView                         tvLike;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.rl_item)
        RelativeLayout rlItem;
        @BindView(R.id.tv_bonus)
        TextView tvBonus;
        @BindView(R.id.tv_special_name)
        TextView tvSpecial;
        @BindView(R.id.tv_sect_name)
        TextView tvSectName;
        @BindView(R.id.bookcomment_title)
        TextView  tvBookcommentTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
