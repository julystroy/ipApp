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
import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.EventRefresh;
import com.cartoon.data.PackageList;
import com.cartoon.data.UseItem;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.utils.Utils;
import com.cartoon.utils.countdown.CountdownView;
import com.cartoon.view.DefineToast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 漫画选集适配器
 * <p>
 * Created by David on 16/6/27.
 */
public class MyPackageAdapter extends RecyclerView.Adapter<MyPackageAdapter.ViewHolder> {



    private Context           context;
    private List<PackageList> mList;
    //private CommonPopDialog.OnHideListener mOnHideListener;

    public MyPackageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<PackageList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_markets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PackageList packageList = mList.get(position);
        if (packageList.getItem_id() == 3||packageList.getItem_id()==4) {
            holder.tvPackageListDesc.setTextColor(Color.parseColor("#fc6615"));
            holder.tvOne.setTextColor(Color.parseColor("#fc6615"));
            holder.tvTwo.setTextColor(Color.parseColor("#fc6615"));
            holder.countDown.updataTextColer("#fc6615");
            holder.tvPayStone.setBackgroundResource(R.drawable.card_bt_bgred);
        } else {
            holder.tvPackageListDesc.setTextColor(Color.parseColor("#f8ea87"));
            holder.tvOne.setTextColor(Color.parseColor("#f8ea87"));
            holder.tvTwo.setTextColor(Color.parseColor("#f8ea87"));
            holder.countDown.updataTextColer("#f8ea87");
            holder.tvPayStone.setBackgroundResource(R.drawable.card_bt_bgyellow);
        }

        if (packageList.getIs_use() == 0) {
            holder.tvPayStone.setText("使用");
            holder.tvPackageListDesc.setVisibility(View.VISIBLE);
            holder.tvPackageListDesc.setText("【"+packageList.getDescription()+"】");

            holder.llRest.setVisibility(View.GONE);

            holder.tvPayStone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_PACKAGE_USE_BUYITEM)
                            .addParams("uid", CartoonApp.getInstance().getUserId())
                            .addParams("token", CartoonApp.getInstance().getToken())
                            .addParams("id", packageList.getId()+"")
                            .build().execute(new BaseCallBack<UseItem>() {

                        @Override
                        public void onLoadFail() {

                        }

                        @Override
                        public void onContentNull() {

                        }

                        @Override
                        public void onLoadSuccess(UseItem response) {
                            DefineToast.createToastConfig().ToastShow(context,response.getMsg());
                            // MyPackageAdapter.this.notifyDataSetChanged();
                            EventBus.getDefault().post(new EventRefresh());
                        }

                        @Override
                        public UseItem parseNetworkResponse(String response) throws Exception {
                            return JSON.parseObject(response, UseItem.class);
                        }
                    });

                }
            });
        } else {
            holder.tvPayStone.setText("使用中");
            holder.tvPackageListDesc.setVisibility(View.GONE);

            holder.llRest.setVisibility(View.VISIBLE);
            //倒计时
            long time = Utils.formatItemOverTime(packageList.getExpire_time());

            holder.countDown.start(time);
            holder.countDown.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    holder.countDown.stop();
                    notifyDataSetChanged();//计时结束时如果在可见界面，手动刷新
                }
            });
        }


        Glide.with(context).load(Utils.addImageDomain(packageList.getIcon()))

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
        TextView       tvPackageListDesc;
        @BindView(R.id.tvone)
        TextView       tvOne;
        @BindView(R.id.tvtwo)
        TextView       tvTwo;
        @BindView(R.id.cv_countdownView)
        CountdownView  countDown;
        @BindView(R.id.tv_pay_stone_count)
        TextView       tvPayStone;
        @BindView(R.id.ll_buy_card)
        RelativeLayout llCard;
        @BindView(R.id.ll_rest)
        LinearLayout   llRest;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
