package com.cartoon.module.tab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by cc on 17-10-12.
 */
public class MineGradViewAdpter extends BaseAdapter{
    private Context context;
    private int[] imageList = {R.mipmap.ic_mine_zomeng,R.mipmap.ic_mine_task,R.mipmap.ic_mine_dynamic,R.mipmap.ic_mine_store2,R.mipmap.ic_mine_charts,R.mipmap.ic_mine_package2};
    private String[] textList = {"宗门","每日任务","每日答题","店铺","排行榜","储物袋"};
    private String[] descList = {"加入宗门 一起high","互动评论 天道酬勤","忆往昔峥嵘岁月","购买法宝 加快升级","修仙路难 唯我独尊","it's my precious"};
    private boolean sect;
    private boolean task;

    public MineGradViewAdpter(Context context){
        this.context = context;

    }
    @Override
    public int getCount() {
        return imageList.length;
    }

    @Override
    public Object getItem(int position) {
        return imageList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void isNewMessage(boolean sect,boolean task){
        this.sect =sect;
        this.task = task;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gradview_mine, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_item_name);
            viewHolder.desc = (TextView) convertView.findViewById(R.id.tv_item_desc);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.newMessage = (ImageView) convertView.findViewById(R.id.message_new);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
           convertView.setPadding(1,17,1,17);
           viewHolder.title.setText(textList[position]);
           viewHolder.desc.setText(descList[position]);
           viewHolder.image.setImageResource(imageList[position]
                );
        if (position == 0) {
                viewHolder.newMessage.setVisibility(sect?View.VISIBLE:View.GONE);

        } else if (position == 1) {
            viewHolder.newMessage.setVisibility(task?View.VISIBLE:View.GONE);

        } else {
            viewHolder.newMessage.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder{
        public TextView title;
        public TextView desc;
        public ImageView image;
        public ImageView newMessage;
    }
}
