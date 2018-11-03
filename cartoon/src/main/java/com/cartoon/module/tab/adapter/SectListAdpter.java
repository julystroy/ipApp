package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.data.SectList;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.view.DialogToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-16.
 */
public class SectListAdpter extends RecyclerView.Adapter<SectListAdpter.ViewHolder> {

    private Context context;
    private List<SectList> Sectlist;

    public SectListAdpter(Context context) {
        this.context = context;
    }

    public void setData(List<SectList> list){
        this.Sectlist = list;
        notifyDataSetChanged();
    }
    public void addData(List<SectList> list){
        int size = this.Sectlist.size();
        this.Sectlist.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sect, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTvSelfLevel.setVisibility(View.GONE);
        holder.mTvPupilLevel.setVisibility(View.GONE);
        holder.mIvIcon.setVisibility(View.GONE);
        holder.mIvLeader.setVisibility(View.GONE);
        final SectList sectList = Sectlist.get(position);
        holder.mTvSectName.setText(sectList.getSect_name());
        holder.mTvSectLevel.setText("LV"+sectList.getSect_level());
        holder.mTvSectCheif.setText(sectList.getNickname());
        holder.mTvSectPupil.setText(sectList.getNum());
        if (sectList.isUserFull()) {
            holder.mTvOk.setText("宗门已满");
            holder.mTvOk.setSelected(true);
        } else if (sectList.isHasApplied()) {
            holder.mTvOk.setText("申请中");
            holder.mTvOk.setSelected(true);
        } else {
            holder.mTvOk.setText("申请加入");
            holder.mTvOk.setSelected(false);
        holder.mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sectList.isUserFull()) {
                    BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SECTJOIN)
                            .addParams("sectionId", Sectlist.get(position).getSect_id() + "")
                            .build().execute(new BaseCallBack<String>() {
                        @Override
                        public void onLoadFail() {
                            // ToastUtils.showShort(context,"申请失败，请重试");
                        }

                        @Override
                        public void onContentNull() {

                        }

                        @Override
                        public void onLoadSuccess(String response) {
                            DialogToast.createToastConfig().ToastShow(context, response.toString(), 4);
                            holder.mTvOk.setText("申请中");
                            sectList.setHasApplied(false);
                            holder.mTvOk.setSelected(true);
                        }

                        @Override
                        public String parseNetworkResponse(String response) throws Exception {
                            return response;
                        }
                    });
                } else {
                    DialogToast.createToastConfig().ToastShow(context,"宗门满员，请加入其他宗门",2);
                }

            }
        });
        }

    }

    @Override
    public int getItemCount() {
        return Sectlist!=null?Sectlist.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView      mIvIcon;
        @BindView(R.id.iv_bang)
        ImageView           mIvBang;
        @BindView(R.id.tv_sect_name)
        TextView       mTvSectName;
        @BindView(R.id.tv_sect_level)
        TextView            mTvSectLevel;
        @BindView(R.id.tv_self_level)
        TextView            mTvSelfLevel;
        @BindView(R.id.tv_pupil_level)
        TextView            mTvPupilLevel;
        @BindView(R.id.tv_sect_n)
        TextView            mTvSectN;
        @BindView(R.id.tv_sect_cheif)
        TextView            mTvSectCheif;
        @BindView(R.id.tv_sect_l)
        TextView            mTvSectL;
        @BindView(R.id.tv_sect_pupil)
        TextView            mTvSectPupil;
        @BindView(R.id.ll_build_sect)
        LinearLayout   mLlBuildSect;
        @BindView(R.id.tv_sect_message)
        TextView            mTvSectMessage;
        @BindView(R.id.tv_ok)
        TextView            mTvOk;
        @BindView(R.id.iv_leader)
        ImageView           mIvLeader;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
