package com.cartoon.module.changxian.detail.detailinfo;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cartoon.data.game.Rating;
import com.cartoon.view.RatingBar;

import java.util.Locale;

import cn.com.xuanjiezhimen.R;

public class RatingView extends LinearLayout {
    private TextView  mRating;
    private TextView  mRatingCount;
    private RatingBar mRatingBar;

    public RatingView(Context context) {
        this(context, null, 0);
    }

    public RatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_rating, this, true);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mRatingCount = (TextView) findViewById(R.id.tv_rating_count);
        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/AccidentalPresidency.ttf");
        mRating.setTypeface(typeFace);
    }

    public void setData(Rating rating) {
        if (rating != null) {
            mRating.setText(rating.rating);
            mRatingCount.setText(String.format(Locale.US, "%d人参与评分", rating.count));
            mRatingBar.setRating(new int[]{rating.count10, rating.count8, rating.count6, rating.count4, rating.count2});
        }
    }
}
