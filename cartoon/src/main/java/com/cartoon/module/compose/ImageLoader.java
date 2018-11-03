package com.cartoon.module.compose;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cartoon.CartoonApp;
import com.cartton.library.utils.ToastUtils;
import com.cndroid.pickimagelib.PickupImageDisplay;

/**
 * Created by jinbangzhu on 7/25/16.
 */

public class ImageLoader extends PickupImageDisplay {
    @Override
    public void displayImage(ImageView imageView, String fullImagePath) {
        Glide.with(imageView.getContext())
                .load(Uri.parse("file://" + fullImagePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .into(imageView);
    }

    @Override
    public void displayCameraImage(ImageView imageView) {

    }


    @Override
    public void showTipsForLimitSelect(int limit) {
        ToastUtils.showLong(CartoonApp.getInstance(), "最多可以选择" + limit + "张图片");
    }
}
