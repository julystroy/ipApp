package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.cartoon.data.Carousel;
import com.cartoon.http.StaticField;
import com.cartoon.view.AspectRatioImageView;
import com.cartoon.volley.VolleySingleton;

import cn.com.xuanjiezhimen.R;

public class SlideViewItem extends LinearLayout {
    private AspectRatioImageView mImageView;

    public SlideViewItem(Context context) {
        this(context, null, 0);
    }

    public SlideViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_poster, this, true);

        mImageView = (AspectRatioImageView) findViewById(R.id.imageview);
    }

    public void setData(Carousel data) {
        if (data != null) {
            int defaultPoster;
            Resources res = getResources();
            RelativeLayout.LayoutParams params;
            if (data.isVertival()) {
                params = new RelativeLayout.LayoutParams(res.getDimensionPixelSize(R.dimen.detail_vertical_poster_width),
                        res.getDimensionPixelSize(R.dimen.detail_vertical_poster_height));
                defaultPoster = R.drawable.default_detail_poster_vertical;
            } else {
                params = new RelativeLayout.LayoutParams(res.getDimensionPixelSize(R.dimen.detail_horizontal_poster_width),
                        res.getDimensionPixelSize(R.dimen.detail_horizontal_poster_height));
                defaultPoster = R.drawable.default_detail_poster_horizontal;
            }
            mImageView.setLayoutParams(params);

            String poster = data.getPoster();
            if (!TextUtils.isEmpty(poster)) {
                //FIXME see WrappedNetworkImageView
                if (!URLUtil.isHttpUrl(poster) || URLUtil.isHttpsUrl(poster)) {
                    // http://192.168.0.83:3000/images/apps/20170111/fc67e90fd081569b07d04d8680bcf8c6_1080_688.jpg
                    poster = StaticField.BASE_CXURL + poster;
                }
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, defaultPoster,
                        R.drawable.ic_transparent); //transparent placeholder. FIXME 0 will crash  onMeasure() did not set the measured dimension by calling setMeasuredDimension()
                VolleySingleton.getInstance(getContext()).getImageLoader().get(poster, listener);
            }
        }
    }
}
