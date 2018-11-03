package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.URLUtil;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.http.StaticField;

/**
 * 处理url为: /images/apps/20170111/fc67e90fd081569b07d04d8680bcf8c6_1080_688.jpg
 * <p>
 * Created by shidu on 17/1/14.
 */

public class WrappedNetworkImageView extends NetworkImageView {
    public WrappedNetworkImageView(Context context) {
        super(context);
    }

    public WrappedNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrappedNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageUrl(String url, ImageLoader imageLoader) {
        String overrideUrl = url;
        if (!URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            // http://192.168.0.83:3000/images/apps/20170111/fc67e90fd081569b07d04d8680bcf8c6_1080_688.jpg
            overrideUrl = StaticField.BASE_CXURL + url;
        }

        super.setImageUrl(overrideUrl, imageLoader);
    }

}
