package com.cartoon.module.changxian.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by wusue on 17/6/19.
 */

public abstract class PagedAdapter<T> extends BaseAdapter {

    protected List<T> mItemList;

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
    protected abstract long getLastPublishAt();

    public void setData(List<T> itemList) {
        mItemList = itemList;
        notifyDataSetChanged();
    }

    public void addItemToTop(T topItem) {
        mItemList.add(0, topItem);
        notifyDataSetChanged();
    }

    public void addItemsToTop(List<T> topItems) {
        mItemList.addAll(0, topItems);
        notifyDataSetChanged();
    }

    public void addNextPage(List<T> nextPage) {
        mItemList.addAll(nextPage);
        notifyDataSetChanged();
    }

    public void cleanData() {
        mItemList.clear();
    }

    @Override
    public int getCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        if (mItemList == null || mItemList.size() < 1) return null;
        return mItemList.get(position);
    }
}
