
package com.cartoon.module.home;

/**//*

*/

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.CartoonApp;
import com.cartoon.data.NewBase;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.utils.ApiUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 推荐列表
 * <p/>
 * Created by David on 16/6/11.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ViewHolder> {

    private Activity            context;
    private List<NewBase>       mList;
    private OnItemClickListener listener;

    public RecommendListAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<NewBase> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<NewBase> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_newbase_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final NewBase newbase = mList.get(position);
        holder.tvTitle.setText(newbase.getTitle());

        holder.tvDesc.setVisibility(View.GONE);
        holder.tvCreateTime.setText(newbase.getCreate_time());
        holder.tvComment.setText(newbase.getComment_num());
        holder.tvFocus.setText(newbase.getApprove_num());
        if (newbase.getIs_approve() == 1) {
            holder.tvFocus.setSelected(true);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
            //            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_focus, 0, 0, 0);
            holder.tvFocus.setSelected(false);
            holder.tvFocus.setTextColor(Color.parseColor("#5b5c5e"));
        }
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.iconCover);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(newbase.getCover_pic())
                    .placeholder(R.mipmap.icon_cartoon_cover)
                    .error(R.mipmap.icon_cartoon_cover)
                    .centerCrop()
                    .into(target);
        }

        holder.llExpound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, newbase.getId(),newbase.getType());
                }
            }
        });
        holder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v,  newbase.getId(),newbase.getType());
                }
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiUtils.approveTarget(String.valueOf(newbase.getId()), 9, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        newbase.setIs_approve(1);
                        int num = Integer.parseInt(newbase.getApprove_num());
                        newbase.setApprove_num(String.valueOf(num + 1));
                        notifyItemChanged(position);

                    }
                });
            }
        });

        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag("comment");
                if (listener != null) {
                    listener.onItemClick(v, newbase.getId(),newbase.getType());
                }
            }
        });


            holder.ivShare.setVisibility(View.VISIBLE);
            holder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CartoonApp.getInstance().isLogin(context)) {
                        return;
                    }
                        ApiUtils.share(context, newbase.getTitle(), newbase.getCover_pic(),"凡人精品", String.valueOf(newbase.getId()));

                }
            });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_cover)
        ImageView    iconCover;
        @BindView(R.id.tv_title)
        TextView     tvTitle;
        @BindView(R.id.tv_desc)
        TextView     tvDesc;
        @BindView(R.id.tv_focus)
        TextView     tvFocus;
        @BindView(R.id.tv_comment)
        TextView     tvComment;
        @BindView(R.id.tv_create_time)
        TextView     tvCreateTime;
        @BindView(R.id.iv_download)
        ImageView    ivDownload;
        @BindView(R.id.iv_share)
        ImageView    ivShare;
        @BindView(R.id.ll_expound)
        LinearLayout llExpound;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

