
package com.cartoon.module.home;

import android.app.Activity;
import android.content.Intent;
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
import com.cartoon.data.Cartoon;
import com.cartoon.data.Keys;
import com.cartoon.listener.ApiQuestListener;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.utils.ApiUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;

/**

 */

public class ReadListAdapter extends RecyclerView.Adapter<ReadListAdapter.ViewHolder> {

    private Activity            context;
    private List<Cartoon>       list;
    private OnItemClickListener listener;

    public ReadListAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<Cartoon> mList) {
        this.list = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<Cartoon> list) {
        int size = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cartoon_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Cartoon cartoon = list.get(position);
        if (position == 0) {
            holder.llItem.setVisibility(View.VISIBLE);
        }else holder.llItem.setVisibility(View.GONE);
        holder.tvTitle.setText(cartoon.getTitle());
        holder.tvComment.setText(cartoon.getComment_num());
        holder.tvFocus.setText(cartoon.getApprove_num());
        if (cartoon.getIs_approve() == 1) {
//            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_fcous_f, 0, 0, 0);
            holder.tvFocus.setSelected(true);
            holder.tvFocus.setTextColor(Color.parseColor("#ef5f11"));
        } else {
//            holder.tvFocus.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_ct_focus, 0, 0, 0);
            holder.tvFocus.setSelected(false);
            holder.tvFocus.setTextColor(Color.parseColor("#929292"));
        }
        holder.tvCreateTime.setText(cartoon.getShow_time());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.iconCover);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(cartoon.getCover_pic())
                    .placeholder(R.mipmap.icon_cartoon_cover)
                    .error(R.mipmap.icon_cartoon_cover)
                    .centerCrop()
                    .into(target);
        }
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onItemClick(v, Integer.parseInt(cartoon.getId()),-1);
                }
            }
        });

        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiUtils.approveTarget(cartoon.getId(), Constants.APPROVE_EXPOUND, new ApiQuestListener() {
                    @Override
                    public void onLoadFail() {

                    }

                    @Override
                    public void onLoadSuccess(String response) {
                        cartoon.setIs_approve(1);
                        int num = Integer.parseInt(cartoon.getApprove_num());
                        cartoon.setApprove_num(String.valueOf(num + 1));
                        notifyItemChanged(position);

                    }
                });
            }
        });

        holder.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NovelDetailActivity.class);
                intent.putExtra(Keys.TARGET_ID, cartoon.getId());
                intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_EXPOUND);
                context.startActivity(intent);
            }
        });


        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CartoonApp.getInstance().isLogin(context)) {
                    return;
                }
                ApiUtils.share(context, cartoon.getTitle(), cartoon.getCover_pic(), "仙界连载", cartoon.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_content)
        LinearLayout ll_content;
        @BindView(R.id.icon_cover)
        ImageView    iconCover;
        @BindView(R.id.tv_title)
        TextView     tvTitle;
        @BindView(R.id.tv_create_time)
        TextView     tvCreateTime;
        @BindView(R.id.tv_focus)
        TextView     tvFocus;
        @BindView(R.id.tv_comment)
        TextView     tvComment;
        @BindView(R.id.iv_download)
        ImageView    ivDownload;
        @BindView(R.id.iv_share)
        ImageView    ivShare;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.ll_comment)
        LinearLayout llComment;
        @BindView(R.id.line_room)
        View         llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


/**
     * 下载列表
     *//*

    private void downloadCartoon(final List<String> collects) {
        ApiUtils.requestDownloadList(collects, new BaseCallBack<List<CartoonDown>>() {
            @Override
            public void onLoadFail() {

            }
            @Override
            public void onContentNull() {

            }
            @Override
            public void onLoadSuccess(List<CartoonDown> response) {
                if (!com.cartoon.utils.CollectionUtils.isEmpty(response)) {
                    for (CartoonDown down : response) {
                        CartoonDownManager.getInstance().downLoadCartoonDown(down);
                    }
                    ToastUtils.showShort(context, "已添加到下载队列.");
                }
            }

            @Override
            public List<CartoonDown> parseNetworkResponse(String response) throws Exception {
                return JSON.parseArray(response, CartoonDown.class);
            }
        });
    }*/
}

