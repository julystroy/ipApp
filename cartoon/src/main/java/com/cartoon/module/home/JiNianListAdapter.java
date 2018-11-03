package com.cartoon.module.home;

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
import com.cartoon.Constants;
import com.cartoon.data.Expound;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.utils.ApiUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**
 * 嘻说数据适配器
 * <p/>
 * Created by David on 16/6/21.
 */
public class JiNianListAdapter extends RecyclerView.Adapter<JiNianListAdapter.ViewHolder> {

    private Activity context;
    private List<Expound> mList;
    private OnItemClickListener listener;

    public JiNianListAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<Expound> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<Expound> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expound_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ivDownload.setVisibility(View.INVISIBLE);
        final Expound expound = mList.get(position);
        holder.tvTitle.setText(expound.getTitle());
            holder.tvDesc.setVisibility(View.GONE);
        holder.tvCreateTime.setText(expound.getCreate_time());
        holder.tvComment.setText(expound.getComment_num());
        holder.tvFocus.setText(expound.getApprove_num());
        if (expound.getIs_approve() == 1) {
//            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_fcous_f, 0, 0, 0);
            holder.tvFocus.setSelected(true);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
//            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_focus, 0, 0, 0);
            holder.tvFocus.setTextColor(Color.parseColor("#5b5c5e"));
            holder.tvFocus.setSelected(false);
        }
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.iconCover);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(expound.getCover_pic())
                    .placeholder(R.mipmap.icon_cartoon_cover)
                    .error(R.mipmap.icon_cartoon_cover)
                    .centerCrop()
                    .into(target);
        }

        holder.llExpound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, expound.getId(),-1);
                }
            }
        });
        holder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, expound.getId(),-1);
                }
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiUtils.approveTarget(String.valueOf(expound.getId()), Constants.APPROVE_BANGAI, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {
                    }
                    @Override
                    public void onLoadSuccess(String response) {
                        expound.setIs_approve(1);
                        int num = Integer.parseInt(expound.getApprove_num());
                        expound.setApprove_num(String.valueOf(num + 1));
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
                    listener.onItemClick(v, expound.getId(),-1);
                }
            }
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }
                ApiUtils.share(context, expound.getTitle(), expound.getCover_pic(), "凡人纪年", String.valueOf(expound.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_cover)
        ImageView iconCover;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_focus)
        TextView tvFocus;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;
        @BindView(R.id.iv_download)
        ImageView ivDownload;
        @BindView(R.id.iv_share)
        ImageView ivShare;
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
