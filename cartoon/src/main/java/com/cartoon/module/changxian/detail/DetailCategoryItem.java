package com.cartoon.module.changxian.detail;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.cartoon.data.DetailPage;
import com.cartoon.data.game.Rating;
import com.cartoon.volley.VolleySingleton;

import cn.com.xuanjiezhimen.R;

public class DetailCategoryItem extends LinearLayout {
    private static final int[] TAG_BGS = {R.drawable.detail_tag_bg01, R.drawable.detail_tag_bg02, R.drawable.detail_tag_bg03,
            R.drawable.detail_tag_bg04, R.drawable.detail_tag_bg05, R.drawable.detail_tag_bg06};
    private NetworkImageView mPoster;
    private TextView         mTitle, mManufacturerTV, mSizeTV, mPlayCountTV, mRatingTV, mRatingCountTV;
    private LinearLayout mCategories;

    public DetailCategoryItem(Context context) {
        this(context, null);
    }

    public DetailCategoryItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailCategoryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_detail_category, this, true);

        mPoster = (NetworkImageView) findViewById(R.id.iv_app_logo);
        mTitle = (TextView) findViewById(R.id.tv_app_name);
        mCategories = (LinearLayout) findViewById(R.id.layout_categories);
        mManufacturerTV = (TextView) findViewById(R.id.tv_manufacturer);
        mSizeTV = (TextView) findViewById(R.id.tv_size);
        mPlayCountTV = (TextView) findViewById(R.id.tv_play_count);
        mRatingTV = (TextView) findViewById(R.id.tv_rating);
        mRatingCountTV = (TextView) findViewById(R.id.tv_rating_count);

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/AccidentalPresidency.ttf");
        mRatingTV.setTypeface(typeFace);
//        findViewById(R.id.iv_selection).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null){
//                    mListener.onSelectionClick();
//                }
//            }
//        });
    }

    public void setData(DetailPage detailPage) {
        mPoster.setImageUrl(detailPage.logo, VolleySingleton.getInstance(getContext()).getImageLoader());
        mTitle.setText(detailPage.name);
        if (detailPage.categories != null) {
            mCategories.removeAllViews();
            for (int i = 0; i < detailPage.categories.length; i++) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.detail_tag, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                textView.setBackgroundResource(TAG_BGS[i % TAG_BGS.length]);
                textView.setText(detailPage.categories[i]);
                mCategories.addView(view);
            }
        }
        LinearLayout layout = (LinearLayout)findViewById(R.id.ll_firm);
        if (detailPage.manufacturer.equals("") || detailPage.manufacturer == null) {
            layout.setVisibility(GONE);
        }else {
            layout.setVisibility(VISIBLE);
            mManufacturerTV.setText(detailPage.manufacturer);
        }
        layout = (LinearLayout)findViewById(R.id.ll_size);
        if (detailPage.size.equals("") || detailPage.size == null) {
            layout.setVisibility(GONE);
        }else {
            layout.setVisibility(VISIBLE);
            mSizeTV.setText(detailPage.size);
        }
        mRatingTV.setText(detailPage.ratingDetail.rating);
        mRatingCountTV.setText(String.format(getResources().getString(R.string.rating_count), detailPage.ratingDetail.count));
        mPlayCountTV.setText(String.format(getResources().getString(R.string.play_count), detailPage.playCount));
    }

    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
    }

    public void setRating(Rating newRating) {
        mRatingTV.setText(newRating.rating);
        mRatingCountTV.setText(String.format(getResources().getString(R.string.rating_count), newRating.count));
    }

    interface onSelectionListener {
        void onSelectionClick();
    }

    private onSelectionListener mListener;

    public void setOnSelectionListener(onSelectionListener listener) {
        mListener = listener;
    }
}
