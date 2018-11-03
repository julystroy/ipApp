package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.cartoon.data.FightQ;
import com.cartoon.data.UserInfo;
import com.cartoon.data.response.EventFight;
import com.cartoon.data.response.FightListRes;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.login.LicenseActivity;
import com.cartoon.module.zong.FightActivity;
import com.cartoon.utils.countdown.CountdownView;
import com.cartoon.view.dialog.FightingDialog;
import com.cartoon.view.dialog.SectFightDialog;
import com.cartoon.view.marquee.MarqueeLayout;
import com.cartoon.view.marquee.MarqueeLayoutAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-16.
 */
public class SectFightListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEAD = 0x101;
    private static final int TYPE_ITEM = 0x102;
    private Context            context;
    private List<FightListRes.ListBean> Sectlist;
    private MarqueeLayoutAdapter<String> mSrcAdapter;
    private boolean manager = false;
    private String sectionId;
    private FightListRes response;
    private List<String> mSrcList ;
    private View.OnClickListener onClickSubViewListener;
    private boolean isStop;

    public SectFightListAdpter(Context context) {
        this.context = context;
        mSrcList = new ArrayList<>();
        UserInfo userLastInfo = CartoonApp.getInstance().getUserLastInfo();
        if (userLastInfo!=null){
             manager = userLastInfo.isManager();
             sectionId = userLastInfo.getSectionId();
        }
    }

    public void setData(FightListRes fightListRes) {
        this.Sectlist = fightListRes.getList();
        this.response = fightListRes;
        if (response.getMsgList() != null && response.getMsgList().size() != 0) {
            if (mSrcList != null)
                mSrcList.clear();
            List<String> msgList = response.getMsgList();
            if (msgList.size() != 0) {
                for (int i = 0; i < msgList.size(); i++) {
                    mSrcList.add(msgList.get(i));
                }
            }

        }
        notifyDataSetChanged();
    }

    public void addData(FightListRes fightListRes) {
        int size = this.Sectlist.size();
        this.Sectlist.addAll(fightListRes.getList());
        notifyItemRangeInserted(size + 1, fightListRes.getList().size());

    }



    public void setOnClickSubViewListener(View.OnClickListener onClickSubViewListener) {
        this.onClickSubViewListener = onClickSubViewListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(context).inflate(R.layout.fight_head_sect, parent, false);
            return new HeadHolder(view);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.item_mine_sect, parent, false);
        return new ListViewHolder(view);
    }


    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, final int position) {
        if (response==null) return;
        if (viewHolder instanceof HeadHolder) {
            final HeadHolder holder = (HeadHolder) viewHolder;
            holder.mSectName.setText(response.getSectName());
            holder.mTvSectLevel.setText("LV"+response.getSectLevel());
            holder.mTvFightNum.setText("攻击:" + response.getTotalAttack()+"     防御:"+response.getTotalDefense());
            holder.mTvFightState.setText("当前状态:" + response.getWarStatus());
            if (response.getWarEndTime().trim().equals("0")) {
                holder.mSlowtimer.setVisibility(View.GONE);
                holder.mTvFightTime.setText("攻击时间:未开始攻击");
            } else {
                holder.mSlowtimer.setVisibility(View.VISIBLE);
                if (holder.mSlowtimer.getRemainTime()!=0){
                    holder.mSlowtimer.allShowZero();
                    holder.mSlowtimer.stop();
                }
                holder.mSlowtimer.start(Long.parseLong(response.getWarEndTime()));
                holder.mTvFightTime.setText("攻击时间: " );
            }
            if (mSrcList.size()==0)
                mSrcList.add("暂无宗门战状态");
            mSrcAdapter = new MarqueeLayoutAdapter<String>(mSrcList) {
                @Override
                public int getItemLayoutId() {
                    return R.layout.item_marquee_text;
                }

                @Override
                public void initView(View view, int position, String item) {
                    ((TextView) view).setText(item);
                }
            };
            holder.mMarqueeLayout.setAdapter(mSrcAdapter);
            holder.mMarqueeLayout.start();
            holder.mMarqueeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickSubViewListener != null) {
                        onClickSubViewListener.onClick(holder.mMarqueeLayout);
                    }
                }
            });


            holder.mTvFightNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LicenseActivity.class);
                    intent.putExtra("type", 0);
                    context.startActivity(intent);
                }
            });

            return;
        }


        ListViewHolder holder = (ListViewHolder) viewHolder;
        holder.mIvLeader.setVisibility(View.GONE);
        holder.mTvPupilLevel.setVisibility(View.GONE);
        holder.mTvSelfLevel.setVisibility(View.GONE);
        final FightListRes.ListBean sectList = Sectlist.get(position-1);
        if (response.isAttacking()) {
            if (position == 1) {
                holder.mIvFight.setVisibility(View.VISIBLE);
                holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FightingDialog dialog = new FightingDialog(context,sectList.getSectTotalDefense(),
                                sectList.getSect_id(),sectList.getSect_name(),sectList.getSect_level()+"");
                        dialog.setOnButtonClickListener(new FightingDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener(int sectWarId) {
                                Intent intent = new Intent(context, FightActivity.class);
                                intent.putExtra("sectWarId",sectWarId);
                                context.startActivity(intent);
                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                });
            } else {
                holder.mIvFight.setVisibility(View.GONE);
                holder.mRlItem.setClickable(false);
            }
        }else {
            holder.mIvFight.setVisibility(View.GONE);
            if (manager) {
                holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SectFightDialog dialog = new SectFightDialog(context,sectList,Integer.parseInt(response.getSectLevel()));
                        dialog.setOnButtonClickListener(new SectFightDialog.buttonClick() {
                            @Override
                            public void onButtonClickListener() {
                                BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_FIGHT_Q)
                                        .addParams("sectId",sectionId)
                                        .addParams("refSectId",sectList.getSect_id()+"")
                                        .addParams("refSectName",sectList.getSect_name())
                                        .build().execute(new BaseCallBack<FightQ>() {
                                    @Override
                                    public void onLoadFail() {

                                    }

                                    @Override
                                    public void onContentNull() {

                                    }

                                    @Override
                                    public void onLoadSuccess(FightQ response) {
                                        if (response != null) {
                                           /* if (response.getIsCanAttack()==0)
                                                ToastUtils.showShort(context,"暂时不能挑战");
                                            else*/
                                                EventBus.getDefault().post(new EventFight());
                                        }
                                    }

                                    @Override
                                    public FightQ parseNetworkResponse(String response) throws Exception {
                                        return JSON.parseObject(response,FightQ.class);
                                    }
                                });
                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                });
            }
        }
        holder.mTvSectName.setText(sectList.getSect_name());
        holder.mTvSectLevel.setText("LV"+sectList.getSect_level());
        holder.mTvSectN.setText("掌门:");
        holder.mTvSectCheif.setText(sectList.getNickname());
        holder.mTvSectL.setText("弟子:");
        holder.mTvSectPupil.setText(sectList.getNowUserNum()+"/"+sectList.getUser_num());
        holder.mTvSectFight.setText(sectList.getSectTotalAttack()+"/"+sectList.getSectTotalDefense());

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
        TextView            mSectName;
        @BindView(R.id.tv_sect_level)
        TextView            mTvSectLevel;
        @BindView(R.id.tv_fight_num)
        TextView            mTvFightNum;
        @BindView(R.id.tv_fight_state)
        TextView            mTvFightState;
        @BindView(R.id.tv_fight_time)
        TextView            mTvFightTime;
        @BindView(R.id.marquee_layout)
        MarqueeLayout       mMarqueeLayout;
        @BindView(R.id.slowtimer)
        CountdownView       mSlowtimer;
        public HeadHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
