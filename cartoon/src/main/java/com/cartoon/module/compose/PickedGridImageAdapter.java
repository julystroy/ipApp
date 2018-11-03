package com.cartoon.module.compose;

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

public class PickedGridImageAdapter extends BaseAdapter {
    private WeakReference<Context> context;
    private List<String> list = new ArrayList<>(9);
    private View.OnClickListener onClickSubViewListener;

    public PickedGridImageAdapter(WeakReference<Context> context, View.OnClickListener onClickSubViewListener) {
        this.context = context;
        this.onClickSubViewListener = onClickSubViewListener;
    }

    public void addItem(String path) {
        list.add(path);
    }

    public void clear() {
        list.clear();
    }


    @Override
    public int getCount() {
        if (list.size() == 9) return 9;
        return list.size() + 1;
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
        imageView.setSelectedColor(Color.parseColor("#88000000"));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setBackgroundResource(R.drawable.add_photo);
        imageView.setImageResource(R.mipmap.ic_add);
        int padding = context.get().getResources().getDimensionPixelSize(R.dimen.grid_padding);

        imageView.setPadding(padding, padding, padding, padding);
        imageView.setTag(R.id.position_photo, position);

        imageView.setTapListener(new SelectableImageView.ITapListener() {
            @Override
            public void onTaped() {
                onClickSubViewListener.onClick(imageView);
            }
        });
        if (position == list.size() && list.size() != 9) {
            imageView.setImageResource(R.mipmap.ic_add);

        } else {
            Glide.with(context.get()).load(list.get(position)).placeholder(R.drawable.default_photo).centerCrop().into(imageView);
        }
        return imageView;
    }

}
