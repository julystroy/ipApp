package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.cartoon.data.Carousel;

import java.util.ArrayList;
import java.util.List;

public class SlideViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Carousel> mItemList;

    public SlideViewAdapter(Context context) {
        mContext = context;
        mItemList = new ArrayList<Carousel>();
    }

    public void updateData(List<Carousel> items) {
        if (items != null && items.size() > 0) {
            mItemList.clear();
            mItemList.addAll(items);
            notifyDataSetChanged();
        }
    }

    public Carousel getItem(int position) {
        return mItemList.size() == 0 ? null : mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected View getItemView(Context context) {
        return new SlideViewItem(context);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((SlideViewItem) getItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        ((ViewHolder) vh).mItemView.setData(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public SlideViewItem mItemView;

        public ViewHolder(SlideViewItem v) {
            super(v);
            mItemView = v;
        }
    }
}

