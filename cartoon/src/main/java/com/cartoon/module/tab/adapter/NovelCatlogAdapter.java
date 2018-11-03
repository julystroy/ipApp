package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.Constants;
import com.cartoon.data.Keys;
import com.cartoon.data.NovelData;
import com.cartoon.module.bangai.NovelDetailActivity;
import com.cartoon.utils.EasySharePreference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.xuanjiezhimen.R;


public class NovelCatlogAdapter extends RecyclerView.Adapter<NovelCatlogAdapter.ViewHolder> {

    private Context         context;
    private List<NovelData> mList;

    private String readId;
    public NovelCatlogAdapter(Context context) {
        this.context = context;
        readId = EasySharePreference.getReadId(context);
    }




    public void setData(List<NovelData> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAll(List<NovelData> list) {
        int size = this.mList.size();
        this.mList.addAll(list);
        notifyItemRangeInserted(size + 1, list.size());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bangai_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NovelData cartoon = mList.get(position);
        holder.tvCollect.setText(cartoon.getTitle());
        if (readId!=null&&readId.equals(cartoon.getId()+"")) {
            holder.tvCollect.setSelected(true);
        } else {
            if (cartoon.getIsRead().equals("0"))
                holder.tvCollect.setTextColor(Color.parseColor("#b1b1b1"));
            else
                holder.tvCollect.setSelected(false);
        }
        holder.llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NovelDetailActivity.class);
                intent.putExtra(Keys.TARGET_ID, String.valueOf(cartoon.getId()));
                intent.putExtra("isRead", cartoon.getIsRead());
                intent.putExtra(Keys.URL_TYPE, 0);
                intent.putExtra(Keys.COMMENT_TYPE, Constants.APPROVE_BANGAI);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_collect_bang)
        TextView     tvCollect;
        @BindView(R.id.ll_colect_bangai)
        LinearLayout llCollect;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
