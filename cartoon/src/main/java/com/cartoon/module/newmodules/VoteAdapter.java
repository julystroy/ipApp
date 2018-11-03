package com.cartoon.module.newmodules;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.data.VoteData;
import com.cartoon.data.response.VoteResponse;
import com.cartoon.module.tab.bookfriendmoment.PreviewPhotoActivity;
import com.cartoon.utils.Utils;
import com.cartoon.view.SelectableImageView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-2-14.
 * 投票
 */
public class VoteAdapter extends BaseAdapter{


    private VoteResponse voteData;
    private Context context;
    private voteItemClickListener voteClickListener;

    public VoteAdapter(Context context) {
        this.context = context;
    }

    public void setData(VoteResponse voteData) {
        this.voteData = voteData;
        notifyDataSetChanged();
    }

    public interface voteItemClickListener{
         void voteItemClickListener(int i,int j);
    }

    public void setVoteClickListener(voteItemClickListener clicklistener) {
        this.voteClickListener = clicklistener;
    }
    @Override
    public int getCount() {
        return voteData==null?0:voteData.getList().size();
    }

    @Override
    public Object getItem(int position) {
        return voteData.getList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyHolder holder;
        final VoteData vote = voteData.getList().get(position);
        if (convertView == null) {
            holder = new MyHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_vote_list, parent, false);
            holder.ivVote = (SelectableImageView) convertView.findViewById(R.id.iv_vote);
            holder.tvItemSelect = (TextView) convertView.findViewById(R.id.tv_item_select);
            holder.tvVote = (TextView) convertView.findViewById(R.id.tv_vote);
            holder.tvVoteNum = (TextView) convertView.findViewById(R.id.tv_vote_num);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }

        if (vote.getOption_pic() != null) {
            Glide.with(context)
                    .load(Utils.addImageDomain(vote.getOption_pic()))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(holder.ivVote);

            //点击图片变大
            holder.ivVote.setTapListener(new SelectableImageView.ITapListener() {
                @Override
                public void onTaped() {
                    String[] list = {Utils.addImageDomain(vote.getOption_pic())};

                    context.startActivity(new Intent(context, PreviewPhotoActivity.class)
                            .putExtra(PreviewPhotoActivity.PHOTO_URLS,list)
                            .putExtra(PreviewPhotoActivity.POSITION, 0));
                }
            });}
        else
            holder.ivVote.setVisibility(View.GONE);

        holder.tvItemSelect.setText(vote.getOption_content());
        if (!voteData.isVoted()) {
            holder.tvVote.setText("投票");

            if (Utils.formatItemOverTime(vote.getEnd_time()) > 0) {//时间过期不能点击
                holder.tvVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (voteClickListener != null) {
                            voteClickListener.voteItemClickListener(vote.getActivity_id(), vote.getOption_id());
                        }
                    }
                });
            } else {
                holder.tvVote.setText("已结束");
                holder.tvVote.setSelected(true);
            }
        } else {
            holder.tvVote.setVisibility(View.GONE);
            holder.tvVoteNum.setVisibility(View.VISIBLE);
        }

        holder.tvVoteNum.setText(vote.getTotal_votes()+"票");
        if (voteData.getOption_voted() == vote.getOption_id()) {
            holder.tvVoteNum.setTextColor(Color.parseColor("#ef5f11"));
            holder.tvVoteNum.setSelected(true);
        } else {
            holder.tvVoteNum.setTextColor(Color.parseColor("#FF161616"));
            holder.tvVoteNum.setSelected(false);
        }
        return convertView;
    }
    class MyHolder{
        private SelectableImageView ivVote;
        private TextView            tvItemSelect;
        private TextView            tvVote;
        private TextView            tvVoteNum;
    }
}
