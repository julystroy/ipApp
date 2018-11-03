package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.AppPoint;
import com.cartoon.data.MineSectData;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.MineSectRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.MySeekBar;
import com.cartoon.view.dialog.SectJobDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-16.
 */
public class MineSectListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x101;
    private static final int TYPE_ITEM = 0x102;

    private Context            context;
    private List<MineSectData> Sectlist;
    private MineSectRes response;

    private ButtonClick onClickSubViewListener;
    public MineSectListAdpter(Context context) {
        this.context = context;
    }

    public void setData(MineSectRes response) {
        if (this.Sectlist!=null)this.Sectlist.clear();
        this.response =response;
        this.Sectlist = response.getList();
        notifyDataSetChanged();
    }

    public void addData(MineSectRes response) {
        int size = this.Sectlist.size();
        this.Sectlist.addAll(response.getList());
        notifyItemRangeInserted(size + 1, response.getList().size());

    }

    public interface ButtonClick{
         void buttonClick(View view,boolean isBoss);
    }

    public void setOnClickSubViewListener(ButtonClick onClickSubViewListener) {
        this.onClickSubViewListener = onClickSubViewListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(context).inflate(R.layout.head_sect, parent, false);
            return new HeadHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_mine_sect, parent, false);

        return new ListViewHolder(view);
    }


    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, final int position) {
        if (response ==null) return;
        if (viewHolder instanceof HeadHolder) {
            final HeadHolder headHolder = (HeadHolder) viewHolder;
            headHolder.mSectName.setText(response.getTitle());
            headHolder.mSbBar.setMax(response.getEnd());
            headHolder.mSbBar.setProgress(response.getStart());

            headHolder.mSectUpdataExperience.setText(response.getProgressBar());
            headHolder.mSectPeopleCount.setText("成员 " + response.getSectionNum());


            headHolder.mSectLevel.setText("LV" + response.getStartLevel());

            if (response.isBoss()) {
                headHolder.mClickBtn.setText("解散宗门");
            } else
                headHolder.mClickBtn.setText("退出宗门");

            headHolder.mClickBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickSubViewListener.buttonClick(headHolder.mClickBtn,response.isBoss());
                }
            });
           return;
        }

        final ListViewHolder holder = (ListViewHolder) viewHolder;
        holder.mTvSectLevel.setVisibility(View.GONE);

        final MineSectData sectList = Sectlist.get(position-1);
        holder.mTvSectName.setText(sectList.getNickname());
        String dailyContribution = sectList.getDailyContribution();
        if (dailyContribution!=null)
        holder.mTvSectCheif.setText(dailyContribution);
        holder.mTvSectPupil.setText(sectList.getContribution() + "");
        holder.mTvSelfLevel.setText(sectList.getLvName());
        holder.mTvSectFight.setText(sectList.getAttack()+"/"+sectList.getDefense());

        if (sectList.isMyself())
            holder.mRlItem.setBackgroundColor(Color.parseColor("#fcf2c6"));
        else
            holder.mRlItem.setBackgroundColor(Color.parseColor("#ffffff"));

        if (sectList.getRank_id() == 1 || sectList.getRank_id() == 2) {
            holder.mIvLeader.setVisibility(View.VISIBLE);
            holder.mTvPupilLevel.setVisibility(View.GONE);
            if (sectList.getRank_id() == 1) {
                holder.mIvLeader.setImageResource(R.mipmap.icon_leader);
            } else {
                holder.mIvLeader.setImageResource(R.mipmap.icon_leader_two);
            }

        } else {
            holder.mIvLeader.setVisibility(View.GONE);
            holder.mTvPupilLevel.setVisibility(View.VISIBLE);
            holder.mTvPupilLevel.setText(sectList.getRank_name());
        }

        if (response.getMyRankId() == 1 || response.getMyRankId() == 2) {
            if (!sectList.isMyself() && sectList.getRank_id() != 1/*&&sectList.getRank_id() !=2*/) {
                holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (response.getMyRankId()==sectList.getRank_id())return;
                        holder.mRlItem.setClickable(false);
                        String sectionId="";
                        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
                        if (userLastInfo!=null&&userLastInfo.getSectionId()!=null) {
                            sectionId = userLastInfo.getSectionId();
                        }
                        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTPOINTLIST)
                                .addParams("myId", CartoonApp.getInstance().getUserId())
                                .addParams("token",CartoonApp.getInstance().getToken())
                                .addParams("memberId",sectList.getUser_id()+"")
                                .addParams("sectionId",sectionId)
                                .build().execute(new BaseCallBack<List<AppPoint>>() {
                            @Override
                            public void onLoadFail() {
                                holder.mRlItem.setClickable(true);
                            }
                            @Override
                            public void onContentNull() {
                            }
                            @Override
                            public void onLoadSuccess(List<AppPoint> response) {
                                holder.mRlItem.setClickable(true);
                                SectJobDialog dialog = new SectJobDialog(context,sectList.getUser_id()+"", response);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.setOnSureClickListener(new SectJobDialog.onSureClickListener() {
                                    @Override
                                    public void onSureClickListener(int rankid, String rankName) {
                                        if (rankid > 0) {
                                            sectList.setRank_id(rankid);
                                            sectList.setRank_name(rankName);
                                            notifyItemChanged(position);
                                        } else {
                                            //移除
                                            if (position <= Sectlist.size()) {
                                                notifyItemRangeChanged(position, Sectlist.size());
                                                Sectlist.remove(position-1);
                                                notifyItemRemoved(position);
                                            }
                                        }
                                    }
                                });
                                dialog.show();
                            }

                            @Override
                            public List<AppPoint> parseNetworkResponse(String response) throws Exception {
                                return JSON.parseArray(response,AppPoint.class);
                            }
                        });

                    }
                });
            }
        }else{
            holder.mRlItem.setClickable(false);
        }


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
        return Sectlist != null ? Sectlist.size()+1 : 1;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sect_name)
        TextView       mTvSectName;
        @BindView(R.id.tv_sect_level)
        TextView       mTvSectLevel;
        @BindView(R.id.tv_self_level)
        TextView       mTvSelfLevel;
        @BindView(R.id.tv_pupil_level)
        TextView       mTvPupilLevel;
        @BindView(R.id.tv_sect_n)
        TextView       mTvSectN;
        @BindView(R.id.tv_sect_cheif)
        TextView       mTvSectCheif;
        @BindView(R.id.tv_sect_l)
        TextView       mTvSectL;
        @BindView(R.id.tv_sect_pupil)
        TextView       mTvSectPupil;
        @BindView(R.id.tv_sect_fight)
        TextView       mTvSectFight;
        @BindView(R.id.ll_build_sect)
        LinearLayout   mLlBuildSect;
        @BindView(R.id.iv_leader)
        ImageView      mIvLeader;
        @BindView(R.id.iv_fight)
        ImageView      mIvFight;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.sect_name)
        TextView  mSectName;
        @BindView(R.id.tv_sect_level)
        TextView  mSectLevel;
        @BindView(R.id.sect_people_count)
        TextView  mSectPeopleCount;
        @BindView(R.id.click_btn)
        TextView  mClickBtn;
        @BindView(R.id.sb_bonus_point)
        MySeekBar mSbBar;
        @BindView(R.id.sect_updata_experience)
        TextView  mSectUpdataExperience;
        public HeadHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
