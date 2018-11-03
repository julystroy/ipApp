package com.cartoon.module.tab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.xuanjiezhimen.R;

/**
 * Created by jiang on 2016/7/10.
 */
public class MineInfoListAdapter extends BaseAdapter{

    private int []titleArr;
    private int [] imgId;
    private LayoutInflater inflater;
    private Context context;

    public MineInfoListAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        titleArr=new int[]{R.string.my_message,
                R.string.my_download,
                R.string.my_state,
                R.string.user_feedback,
                R.string.clear_cache,
                R.string.my_upadte};
        imgId=new int[]{
                R.mipmap.icon_mine_state,
                R.mipmap.icon_mine_cache,
                R.mipmap.icon_mine_feedback,
                R.mipmap.icon_mine_update};
    }

    @Override
    public int getCount() {
        return imgId.length;
    }

    @Override
    public Object getItem(int position) {
        return titleArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_mine_list,null,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= ((ViewHolder) convertView.getTag());
        }
        holder.icon.setImageResource(imgId[position]);
        holder.title.setText(titleArr[position]);
        holder.rightArrow.setVisibility(View.VISIBLE);
        holder.info.setVisibility(View.GONE);

        if (position==4){
            holder.rightArrow.setVisibility(View.GONE);
            holder.info.setText("50"+"K");
            holder.info.setVisibility(View.VISIBLE);
        }
        if (position==5){
            holder.rightArrow.setVisibility(View.GONE);
            holder.info.setText("当前版本为");
            holder.info.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


    private class ViewHolder{
        private ImageView icon;
        private TextView title;
        private TextView info;
        private ImageView rightArrow;
        private TextView dividerLine;

        public ViewHolder(View view) {
            icon= ((ImageView) view.findViewById(R.id.iv_image_container));
            title= ((TextView) view.findViewById(R.id.tv_text_container));
            info=((TextView) view.findViewById(R.id.tv_info));
            rightArrow= ((ImageView) view.findViewById(R.id.iv_mine_right_narrow));
           // dividerLine= ((TextView) view.findViewById(R.id.tv_mine_item_divider));
        }
    }
}
