
package com.cartoon.module.tab;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.data.response.FeaturedItem;
import com.cartoon.http.StaticField;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

public class ChangxianListAdapter extends RecyclerView.Adapter<ChangxianListAdapter.ViewHolder> {

    private Activity            context;
    private List<FeaturedItem>  mList;
    private OnItemClickListener listener;

    public ChangxianListAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<FeaturedItem> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<FeaturedItem> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public String getFirstPublishAt() {
        return (mList == null || mList.size() == 0 || mList.get(0) == null)
                ? "0" : String.valueOf(mList.get(0).publishedAt);
    }

    public String getLastPublishAt() {
        return (mList == null || mList.size() == 0 || mList.get(mList.size() - 1) == null)
                ? "0" : String.valueOf(mList.get(mList.size() - 1).publishedAt);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_changxian_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.posterLayout.getLayoutParams();
        params.height = (int) (Utils.getScreenPxWidth(context) * 143.3 / 360);

        final FeaturedItem data = mList.get(position);
        holder.tvTitle.setText(data.name);
        holder.tvShortIntro.setText(data.shortIntro);
        if (data.playCount != 0) {
            holder.tvPlayCount.setVisibility(View.VISIBLE);
            holder.ivPlayCount.setVisibility(View.VISIBLE);
            holder.tvPlayCount.setText(String.valueOf(data.playCount));
        } else {
            holder.tvPlayCount.setVisibility(View.GONE);
            holder.ivPlayCount.setVisibility(View.GONE);
        }
        if (data.rating != 0) {
            holder.tvRating.setVisibility(View.VISIBLE);
            holder.ivRating.setVisibility(View.VISIBLE);
            holder.tvRating.setText(String.valueOf(data.rating));
        } else {
            holder.tvRating.setVisibility(View.GONE);
            holder.ivRating.setVisibility(View.GONE);
        }
        if (data.commentCount != 0) {
            holder.tvCommentCount.setVisibility(View.VISIBLE);
            holder.ivCommentCount.setVisibility(View.VISIBLE);
            holder.tvCommentCount.setText(String.valueOf(data.commentCount));
        } else {
            holder.tvCommentCount.setVisibility(View.GONE);
            holder.ivCommentCount.setVisibility(View.GONE);
        }

        final WeakReference<ImageView> appLogoReference = new WeakReference<>(holder.appLogo);
        ImageView appLogo = appLogoReference.get();
        if (appLogo != null) {
            Glide.with(context)
                    .load(StaticField.BASE_CXURL + data.logo)
                    .fitCenter()
                    .into(appLogo);
        }
        final WeakReference<ImageView> posterReference = new WeakReference<>(holder.poster);
        ImageView poster = posterReference.get();
        if (poster != null) {
            Glide.with(context)
                    .load(StaticField.BASE_CXURL + data.poster)
//                    .placeholder(R.mipmap.icon_cartoon_cover)
//                    .error(R.mipmap.icon_cartoon_cover)
                    .centerCrop()
                    .into(poster);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position, 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_changxian)
        LinearLayout layout;

        @BindView(R.id.iv_app_logo)
        ImageView appLogo;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.iv_poster)
        ImageView poster;

        @BindView(R.id.rlayout_video)
        RelativeLayout posterLayout;

        @BindView(R.id.tv_short_intro)
        TextView tvShortIntro;

        @BindView(R.id.iv_rating)
        ImageView ivRating;

        @BindView(R.id.tv_rating)
        TextView tvRating;

        @BindView(R.id.iv_comments_count)
        ImageView ivCommentCount;

        @BindView(R.id.tv_comments_count)
        TextView tvCommentCount;

        @BindView(R.id.iv_play_count)
        ImageView ivPlayCount;

        @BindView(R.id.tv_play_count)
        TextView tvPlayCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

