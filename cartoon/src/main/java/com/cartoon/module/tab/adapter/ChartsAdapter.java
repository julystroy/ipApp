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
import com.cartoon.data.Charts;
import com.cartoon.data.Keys;
import com.cartoon.module.mymoment.OthersMomentActivity;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-1-5.
 */
public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ViewHolder> {


    private Context      context;
    private List<Charts> list;

    public ChartsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Charts> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_charts, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Charts charts=list.get(position);

           holder.mTvName.setTag(position);
           holder.mTvSort.setTag(position);
           holder.mTvBonus.setTag(position);


        if (charts.getRank()==1) {
            holder.mTvSort.setBackgroundResource(R.mipmap.god_medal);
            holder.mTvSort.setText("");
        }
        else if (charts.getRank() == 2) {

            holder.mTvSort.setBackgroundResource(R.mipmap.medal);
            holder.mTvSort.setText("");
        } else if (charts.getRank() == 3) {

            holder.mTvSort.setBackgroundResource(R.mipmap.bronze_medal);
            holder.mTvSort.setText("");
        } else {

            holder.mTvSort.setText(charts.getRank()+"");
            holder.mTvSort.setBackgroundResource(R.mipmap.charts_defalut);
        }

        if (TextUtils.equals(charts.getId()+"", CartoonApp.getInstance().getUserId()))
            holder.mTvName.setTextColor(Color.parseColor("#ef5f11"));
        else
            holder.mTvName.setTextColor(Color.parseColor("#2F2F2F"));

        ImageLoaderUtils.displayRound(context,holder.mIvIcon,Utils.addImageDomain(charts.getAvatar()));
        holder.mTvName.setText(charts.getNickname());
        holder.mTvBonus.setText(charts.getLvName());

       holder.mRlItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, OthersMomentActivity.class);
               intent.putExtra(Keys.TARGET_UID, charts.getId() + "");
               intent.putExtra(Keys.TARGET_OTHERS, charts.getNickname());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sort)
        TextView       mTvSort;
        @BindView(R.id.iv_icon)
        ImageView      mIvIcon;
        @BindView(R.id.tv_name)
        TextView       mTvName;
        @BindView(R.id.tv_bonus)
        TextView       mTvBonus;

        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
