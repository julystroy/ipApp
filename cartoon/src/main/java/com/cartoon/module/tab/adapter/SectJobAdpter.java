package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cartoon.data.AppPoint;

import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-19.
 */
public class SectJobAdpter extends BaseAdapter {
    private Context context;

    private int selectedPosition = -1;// 选中的位置
    private List<AppPoint> list;

    public SectJobAdpter(Context context, List<AppPoint> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.sect_job_item, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppPoint ap = list.get(position);

            holder.tv.setText(ap.getRankName());
            if (ap.isCanChoose())
                holder.tv.setTextColor(Color.parseColor("#000000"));
            else
                holder.tv.setTextColor(Color.parseColor("#999999"));
            if (position % 2 == 0) {
                if (selectedPosition == position) {
                    convertView.setSelected(true);
                    convertView.setPressed(true);
                    holder.iv.setVisibility(View.VISIBLE);
                } else {
                    convertView.setSelected(false);
                    convertView.setPressed(false);
                    holder.iv.setVisibility(View.GONE);
                }
            } else {
                if (selectedPosition == position) {
                    convertView.setSelected(true);
                    convertView.setPressed(true);
                    holder.iv.setVisibility(View.VISIBLE);
                } else {
                    convertView.setSelected(false);
                    convertView.setPressed(false);
                    holder.iv.setVisibility(View.GONE);
                }
            }

        return convertView;
    }

    class ViewHolder {
        TextView  tv;
        ImageView iv;
    }

}
