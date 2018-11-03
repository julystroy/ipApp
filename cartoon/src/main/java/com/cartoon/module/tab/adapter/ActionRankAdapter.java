package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.CartoonApp;
import com.cartoon.data.ActionRank;
import com.cartoon.data.Keys;
import com.cartoon.module.action.ActionDetailActivity;
import com.cartoon.module.listener.ResponseListener;
import com.cartoon.utils.BonusHttp;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-5.
 */
public class ActionRankAdapter extends RecyclerView.Adapter<ActionRankAdapter.ViewHolder> {



    private Context          context;
    private List<ActionRank> list;
    private int  canVote;
    private String  type;
    public ActionRankAdapter(Context context,String type) {
        this.context = context;
        this.type = type;
    }

    public void setData(List<ActionRank> list,int canVote) {
        this.list = list;
        this.canVote = canVote;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_action_rank, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final ActionRank charts = list.get(position);
        if (charts.getRank() < 10) {
            holder.mTvSort.setText("0" + charts.getRank());
        } else {
            holder.mTvSort.setText(String.valueOf(charts.getRank()));
        }
        if (charts.getRank() == 1) {
            holder.ivGuan.setVisibility(View.VISIBLE);
            holder.mTvSort.setTextColor(Color.parseColor("#ff7272"));
        } else if (charts.getRank() == 2) {
            holder.ivGuan.setVisibility(View.GONE);
            holder.mTvSort.setTextColor(Color.parseColor("#f8b551"));
        } else if (charts.getRank() == 3) {
            holder.ivGuan.setVisibility(View.GONE);
            holder.mTvSort.setTextColor(Color.parseColor("#3399ff"));
        } else {
            holder.ivGuan.setVisibility(View.GONE);
            holder.mTvSort.setTextColor(Color.parseColor("#939393"));
        }

        if (TextUtils.equals(charts.getUid() + "", CartoonApp.getInstance().getUserId())) {

            holder.mRlItem.setBackgroundColor(Color.parseColor("#fff9f2"));
        } else {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.mTvCommentNum.setText(charts.getCommentCount()+"条评论");
        String essay_title = charts.getEssay_title();
        if (essay_title != null && essay_title.length()> 10) {
            holder.mTvRankTitle.setText("《"+ essay_title.substring(0,10)+"...》");
        }else
        holder.mTvRankTitle.setText("《"+charts.getEssay_title()+"》");
        holder.mTvVoteNum.setText(charts.getVotes()+"票");

        ImageLoaderUtils.displayRound(context,holder.mIvIcon, Utils.addBaseUrlShare(charts.getAvatar()));
        holder.mTvRankName.setText(charts.getNickname());

        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActionDetailActivity.class);
                intent.putExtra(Keys.CHART, charts.getEssay_id()+"");
                intent.putExtra(Keys.CHART_USEID, charts.getUid()+"");
                intent.putExtra(Keys.CHART_BOOLEAN, canVote+"");
                context.startActivity(intent);
            }
        });

//        String end_time = charts.getEnd_time();
//        if (end_time==null||end_time.isEmpty())
//            end_time = String.valueOf(System.currentTimeMillis()+1);
        if (canVote == -1/*|| Utils.formatItemOverTime(end_time)<System.currentTimeMillis()*/) {
            holder.mTvVote.setSelected(true);
        }else
            holder.mTvVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CartoonApp.getInstance().isLogin(context))
                        BonusHttp.ActionVote(context, charts.getEssay_id() + "", new ResponseListener() {
                            @Override
                            public void onLoadFail() {
                            }
                            @Override
                            public void onLoadSuccess(String response) {
                                if (response != null && response.equals("1")) {
                                    charts.setVotes(String.valueOf(Integer.parseInt(charts.getVotes())+1));
                                    holder.mTvVoteNum.setText((charts.getVotes()) + "票");
                                } else {
                                    //holder.mTvVote.setSelected(true);
                                    canVote = -1;
                                  //  holder.mTvVote.setEnabled(false);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                }
            });

        if (type.equals("new")) {
            holder.mTvcreatTime.setVisibility(View.VISIBLE);
            holder.mTvcreatTime.setText(charts.getCreateTime()+"发布");
        }else
            holder.mTvcreatTime.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sort)
        TextView       mTvSort;
        @BindView(R.id.iv_icon)
        ImageView      mIvIcon;
        @BindView(R.id.tv_rank_name)
        TextView       mTvRankName;
        @BindView(R.id.tv_rank_title)
        TextView       mTvRankTitle;
        @BindView(R.id.tv_vote_num)
        TextView       mTvVoteNum;
        @BindView(R.id.tv_comment_num)
        TextView       mTvCommentNum;
        @BindView(R.id.tv_vote)
        TextView      mTvVote;
        @BindView(R.id.tv_creattime)
        TextView      mTvcreatTime;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;

        @BindView(R.id.iv_guan)
        ImageView  ivGuan;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
