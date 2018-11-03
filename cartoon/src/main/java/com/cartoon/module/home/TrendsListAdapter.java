
package com.cartoon.module.home;

/**//*

*/

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cartoon.data.TrendsData;
import com.cartoon.listener.OnItemClickListener;
import com.cartoon.utils.Utils;

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

public class TrendsListAdapter extends RecyclerView.Adapter<TrendsListAdapter.ViewHolder> {

    private Activity            context;
    private List<TrendsData>       mList;
    private OnItemClickListener listener;

    public TrendsListAdapter(Activity context) {
        this.context = context;
    }

    public void setData(List<TrendsData> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<TrendsData> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trends_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final TrendsData newbase = mList.get(position);
        holder.tvTitle.setText(newbase.getClause_title());
        holder.tvModule.setText(newbase.getModuleName());

        holder.tvCreateTime.setText(newbase.getTime());
        holder.tvComment.setText(newbase.getComment_num()+"评论");


        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.iconCover);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(Utils.addImageDomain(newbase.getCover_pic()))
                    .placeholder(R.mipmap.icon_cartoon_cover)
                    .error(R.mipmap.icon_cartoon_cover)
                    .centerCrop()
                    .into(target);
        }

        holder.llExpound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, newbase.getClause_id(),newbase.getModule_type());
                }
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

        @BindView(R.id.tv_comment)
        TextView     tvComment;
        @BindView(R.id.module_name)
        TextView     tvModule;
        @BindView(R.id.tv_create_time)
        TextView     tvCreateTime;

        @BindView(R.id.ll_expound)
        LinearLayout llExpound;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

