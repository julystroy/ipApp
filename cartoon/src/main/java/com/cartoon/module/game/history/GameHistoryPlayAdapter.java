package com.cartoon.module.game.history;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cartoon.account.AccountHelper;
import com.cartoon.data.game.PlayHistoryItemData;
import com.cartoon.http.StaticField;
import com.cartoon.utils.ImageLoaderUtils;
import com.cartoon.view.game.TagsContainerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * Created by wuchuchu on 2018/3/2.
 */

public class GameHistoryPlayAdapter extends RecyclerView.Adapter<GameHistoryPlayAdapter.MyViewHolder> {

    private List<PlayHistoryItemData> list;


    private View.OnClickListener onClickSubViewListener;
    private Activity activity;

    public GameHistoryPlayAdapter(Activity activity) {
        this.activity = activity;
        list = new ArrayList<>();
    }

    public void setListData(List<PlayHistoryItemData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addAll(List<PlayHistoryItemData> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addRefresh(List<PlayHistoryItemData> list) {
        this.list.addAll(0, list);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
        Log.d("GameHistory", "clearAll()");
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlayHistoryItemData data = list.get(position);
        holder.appNameTV.setText(data.name);
        ImageLoaderUtils.display(activity, holder.appIconIV, StaticField.BASE_CXURL + data.logo, 0, 0);
        holder.mRedSpotIV.setVisibility(View.GONE);
        holder.playBtn.setEnabled(true);
        holder.bottomBtn.setTag(position);
        holder.playBtn.setTag(position);
        holder.contentRL.setTag(position);
        holder.contentRL.setOnClickListener(onClickSubViewListener);
        holder.playBtn.setOnClickListener(onClickSubViewListener);
        holder.bottomBtn.setOnClickListener(onClickSubViewListener);

        if (data.playType == 2) {
            holder.playBtn.setText(R.string.mgh_download_and_play);

            holder.bottomBtn.setText(R.string.delete);
            holder.bottomBtn.setBackgroundResource(R.drawable.btn_bg_blue);
            holder.bottomBtn.setTextColor(activity.getResources().getColor(R.color.base_blue_button_color));
            holder.bottomBtn.setEnabled(true);
        } else {
            holder.playBtn.setText(R.string.mgh_play);

            if (data.userRating > 0) {
                holder.bottomBtn.setText(R.string.evaluated);
                holder.bottomBtn.setBackgroundResource(0);
                holder.bottomBtn.setTextColor(activity.getResources().getColor(R.color.subtitle_text_color));
                holder.bottomBtn.setEnabled(false);
            } else {
                if (data.duration > AccountHelper.getPlayTimeNeededForRating()) {
                    holder.mRedSpotIV.setVisibility(View.VISIBLE);
                }
                holder.bottomBtn.setText(R.string.evaluate);
                holder.bottomBtn.setBackgroundResource(R.drawable.btn_bg_blue);
                holder.bottomBtn.setTextColor(activity.getResources().getColor(R.color.base_color_blue));
                holder.bottomBtn.setEnabled(true);
            }
        }

        holder.tagsViewTC.removeAllViews();
        for (int i = 0; i < data.categories.length; i++) {
            TextView label = new TextView(activity);

            label.setText(data.categories[i]);
            label.setTextSize(11.3f);
            label.setBackgroundResource(R.drawable.btn_favoriti_tag);
            label.setMaxLines(1);
            label.setPadding(activity.getResources().getDimensionPixelSize(R.dimen.tag_left_padding),
                    activity.getResources().getDimensionPixelSize(R.dimen.tag_top_padding),
                    activity.getResources().getDimensionPixelSize(R.dimen.tag_left_padding),
                    activity.getResources().getDimensionPixelSize(R.dimen.tag_top_padding));
            holder.tagsViewTC.addView(label);
        }
        if (data.state != 0) {
            holder.playBtn.setEnabled(false);
            holder.bottomBtn.setTextColor(activity.getResources().getColor(R.color.subtitle_text_color));
            holder.bottomBtn.setEnabled(false);
            holder.mRedSpotIV.setVisibility(View.GONE);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_history_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }
    public PlayHistoryItemData getItem(int position) {
        Log.d("historyAdapter", "get item position:" + position);
        return list.get(position);
    }

    public void deleteItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public String getFirstPublishAt() {
        return (list == null || list.size() == 0 || list.get(0) == null)
                ? "0" : String.valueOf(list.get(0).updatedAt);
    }

    public String getLastPublishAt() {
        return (list == null || list.size() == 0 || list.get(list.size() - 1) == null)
                ? "0" : String.valueOf(list.get(list.size() - 1).updatedAt);
    }

    public void setOnClickSubViewListener(View.OnClickListener onClickSubViewListener) {
        this.onClickSubViewListener = onClickSubViewListener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_content)
        RelativeLayout contentRL;
        @BindView(R.id.iv_ph_logo)
        ImageView appIconIV;
        @BindView(R.id.tv_ph_title)
        TextView appNameTV;
        @BindView(R.id.layout_ph_tags)
        TagsContainerLayout tagsViewTC;
        @BindView(R.id.btn_play)
        Button playBtn;
        @BindView(R.id.btn_bottom)
        Button bottomBtn;
        @BindView(R.id.iv_rating_enable_red_spot)
        ImageView mRedSpotIV;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
