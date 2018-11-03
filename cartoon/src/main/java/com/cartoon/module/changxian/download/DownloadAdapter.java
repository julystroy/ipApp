package com.cartoon.module.changxian.download;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cartoon.data.game.DownloadGame;

import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

public class DownloadAdapter extends BaseAdapter {
    public OnDownloadEmptyListener mEmptyListener;
    private Context mContext;
    private List<DownloadGame> mItems;

    public DownloadAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<DownloadGame>();
    }

    public void setData(List<DownloadGame> datas) {
        if (datas != null && datas.size() > 0) {
            mItems.addAll(datas);
            notifyDataSetChanged();
        }
    }

    protected void addData(List<DownloadGame> datas) {
        if (datas != null && datas.size() > 0) {
            mItems.addAll(datas);
        }
    }

    private void deleteData(int pos) {
        if (pos < getCount()) {
            mItems.remove(pos);
            notifyDataSetChanged();
            if (mItems.size() == 0) {
                mEmptyListener.showEmpty();
            }
        }
    }

    protected Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public DownloadGame getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (!(convertView instanceof DownloadItem)) {
            convertView = new DownloadItem(mContext);
        }
        DownloadItem item = (DownloadItem) convertView;
        item.setTag(position);
        item.setOnClickDeleteListener(mOnClickDeleteListener);
        item.setData(getItem(position));
        return convertView;
    }

    private void showDeleteAlertDialog(final View v) {
        new AlertDialog.Builder(mContext, R.style.CXAlertDialogStyle)
                .setMessage(mContext.getResources().getString(R.string.delete_download_tips))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((DownloadItem) v).delete();
                        deleteData((int) v.getTag());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDeleteAlertDialog(v);
        }
    };

    public interface OnDownloadEmptyListener {
        void showEmpty();
    }
}
