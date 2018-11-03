package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.data.FightData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-16.
 */
public class FightDyListAdpter extends RecyclerView.Adapter<FightDyListAdpter.ViewHolder> {
    private Context         context;
    private List<FightData> Sectlist;

    public FightDyListAdpter(Context context) {
        this.context = context;
    }

    public void setData(List<FightData> list) {
        this.Sectlist = list;
        notifyDataSetChanged();
    }


    public void addAll(List<FightData> list) {
        int size = this.Sectlist.size();
        this.Sectlist.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fightdy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FightData data = Sectlist.get(position);
        holder.mTvDyTitle.setText(data.getMsg());
        holder.mTvDyTime.setText(data.getSectWarTime());

        if (data.isMyself())
            holder.mLlDyout.setBackgroundColor(Color.parseColor("#fcf2c6"));
        else
            holder.mLlDyout.setBackgroundColor(Color.parseColor("#ffffff"));

    }

    @Override
    public int getItemCount() {
        return Sectlist != null ? Sectlist.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dy_title)
        TextView     mTvDyTitle;
        @BindView(R.id.tv_dy_time)
        TextView  mTvDyTime;
        @BindView(R.id.ll_dyout)
        LinearLayout mLlDyout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
