package com.cartoon.module.changxian.detail.account;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cartoon.data.DetailPage;
import com.cartoon.data.GameAccount;

import java.util.ArrayList;
import java.util.List;

public class DetailAccountAdapter extends BaseAdapter {
    private Context mContext;
    private List<GameAccount> mList;
    private DetailPage mDetailPage;

    public DetailAccountAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<GameAccount>();
    }

    public void updateList(DetailPage detailPage) {
        mDetailPage = detailPage;
        if (detailPage.accounts != null && detailPage.accounts.size() > 0) {
            mList.clear();
            mList.addAll(detailPage.accounts);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public GameAccount getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new DetailAccountItem(mContext);
        }
        ((DetailAccountItem) convertView).setData(mDetailPage, getItem(position));
        return convertView;
    }
}
