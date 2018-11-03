package com.cartoon.module.tab.bookfriendmoment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cartoon.view.SelectableImageView;
import com.cartoon.view.SquareImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.com.xuanjiezhimen.R;

/**
 * Created by jinbangzhu on 7/20/16.
 */

public class GridImageAdapter extends BaseAdapter {
    private WeakReference<Context> context;
    private List<String> list;
    private List<String> list2;
    private View.OnClickListener onClickSubViewListener;
    private int position;

    public GridImageAdapter(WeakReference<Context> context, List<String> list, View.OnClickListener onClickSubViewListener, int position) {
        this.list = list;
        this.context = context;
        this.onClickSubViewListener = onClickSubViewListener;
        this.position = position;
        list2 = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SquareImageView imageView = new SquareImageView(context.get());
        imageView.setId(R.id.id_photo);
        imageView.setSelectedColor(Color.parseColor("#00000000"));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);
        int padding = context.get().getResources().getDimensionPixelSize(R.dimen.grid_padding);

        imageView.setPadding(padding, padding, padding, padding);

        imageView.setTag(R.id.position_photo, position);
        imageView.setTag(R.id.position_book_friend_moment, this.position);
        imageView.setTapListener(new SelectableImageView.ITapListener() {
            @Override
            public void onTaped() {
                onClickSubViewListener.onClick(imageView);
            }
        });
        if (list.size() == 4) {
            list.add(2,"");
        }
        Glide.with(context.get()).load(list.get(position)).placeholder(R.drawable.default_photo).into(imageView);
        return imageView;
    }

}
