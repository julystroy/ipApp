package com.cartoon.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;

public class MaterialRatingBar extends android.widget.RatingBar {
    private MaterialRatingDrawable mDrawable;
    private float mLastKnownRating;

    private OnRatingChangeListener mOnRatingChangeListener;
    public MaterialRatingBar(Context context) {
        super(context);

        init();
    }

    public MaterialRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MaterialRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mDrawable = new MaterialRatingDrawable(getContext());
        mDrawable.setStarCount(getNumStars());
        setProgressDrawable(mDrawable);
    }

    @Override
    public void setNumStars(int numStars) {
        super.setNumStars(numStars);

        // mDrawable can be null during super class initialization.
        if (mDrawable != null) {
            mDrawable.setStarCount(numStars);
        }
    }

    public void setProgressImageId(Context context, int resId) {
        mDrawable.setProgressDrawable(AppCompatResources.getDrawable(context, resId));
    }

    public void setSecondaryProgressImageId(Context context, int resId) {
        mDrawable.setSecondaryProgressDrawable(AppCompatResources.getDrawable(context, resId));
    }

    public void setBackgroundProgressImageId(Context context, int resId) {
        mDrawable.setBackgroundDrawable(AppCompatResources.getDrawable(context, resId));
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        int width = Math.round(height * mDrawable.getTileRatio() * getNumStars());
        setMeasuredDimension(ViewCompat.resolveSizeAndState(width, widthMeasureSpec, 0), height);
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        mOnRatingChangeListener = listener;
    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);

        // HACK: Check and call our listener here because this method is always called by
        // updateSecondaryProgress() from onProgressRefresh().
        float rating = getRating();
        if (mOnRatingChangeListener != null && rating != mLastKnownRating) {
            mOnRatingChangeListener.onRatingChanged(this, rating);
        }
        mLastKnownRating = rating;
    }

    public interface OnRatingChangeListener {

        /**
         * Notification that the rating has changed. This <strong>will</strong> be called
         * continuously while the user is dragging, different from framework's
         * {@link OnRatingBarChangeListener}.
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating The current rating. This will be in the range 0..numStars.
         */
        void onRatingChanged(MaterialRatingBar ratingBar, float rating);
    }


}
