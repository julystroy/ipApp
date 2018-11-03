package com.cartoon.module.markets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.common.CommonPopDialog;
import com.cartoon.data.Markets;
import com.cartoon.module.login.LoginActivity;
import com.cartoon.utils.Utils;
import com.cartoon.view.dialog.BuyDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 漫画选集适配器
 * <p/>
 * Created by David on 16/6/27.
 */
public class MarketsAdapter extends RecyclerView.Adapter<MarketsAdapter.ViewHolder> {


    private Context       context;
    private List<Markets> mList;
    //private CommonPopDialog.OnHideListener mOnHideListener;

    public MarketsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Markets> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setOnHideListener(CommonPopDialog.OnHideListener mOnHideListener) {
        // this.mOnHideListener = mOnHideListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_markets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Markets markets = mList.get(position);
        holder.tvMarketsDesc.setText("【"+markets.getDescription()+"】");
        if (markets.getType() == 2) {
            holder.tvPayStone.setTextColor(Color.parseColor("#226498"));
            holder.tvPayStone.setBackgroundResource(R.drawable.card_bt_bgblue);
            holder.tvPayStone.setText("即将开放");
        } else {
            holder.tvPayStone.setTextColor(Color.parseColor("#3a1e07"));
            holder.tvPayStone.setBackgroundResource(R.drawable.card_bt_bgyellow);
            holder.tvPayStone.setText("灵石"+markets.getStone());
            holder.llCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CartoonApp.getInstance().getUserInfo() != null) {
                        BuyDialog buyDialog = new BuyDialog(context, markets.getId() + "", markets.getStone() + "", markets.getClick_icon(), markets.getDescription());
                        buyDialog.setCanceledOnTouchOutside(true);
                        buyDialog.show();
                    }else
                        context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }
        Glide.with(context).load(Utils.addImageDomain(markets.getIcon()))
                .placeholder(R.drawable.background_full_gray)
                .error(R.drawable.background_full_gray)
                .into(holder.ivCard);

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

       @BindView(R.id.iv_experience_card)
       ImageView      ivCard;
       @BindView(R.id.tv_markets_desc)
       TextView       tvMarketsDesc;
       @BindView(R.id.tv_pay_stone_count)
       TextView       tvPayStone;
       @BindView(R.id.ll_buy_card)
       RelativeLayout llCard;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
