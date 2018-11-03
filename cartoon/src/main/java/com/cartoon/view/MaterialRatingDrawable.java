package com.cartoon.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.content.res.AppCompatResources;
import android.view.Gravity;

import cn.com.xuanjiezhimen.R;

public class MaterialRatingDrawable extends LayerDrawable {

    public MaterialRatingDrawable(Context context) {
        super(new Drawable[]{
                new TileDrawable(AppCompatResources.getDrawable(context, R.drawable.star_normal)),
                new ClipDrawableCompat(new TileDrawable(AppCompatResources.getDrawable(context, R.drawable.star_focus_show5)), Gravity.LEFT, ClipDrawable.HORIZONTAL),
                new ClipDrawableCompat(new TileDrawable(AppCompatResources.getDrawable(context, R.drawable.star_focus_show5)), Gravity.LEFT, ClipDrawable.HORIZONTAL)
        });

        setId(0, android.R.id.background);
        setId(1, android.R.id.secondaryProgress);
        setId(2, android.R.id.progress);
    }

    public void setProgressDrawable(Drawable drawable) {
        getTileDrawableByLayerId(android.R.id.progress).setDrawble(drawable);
    }

    public void setSecondaryProgressDrawable(Drawable drawable) {
        getTileDrawableByLayerId(android.R.id.secondaryProgress).setDrawble(drawable);
    }

    public void setBackgroundDrawable(Drawable drawable) {
        getTileDrawableByLayerId(android.R.id.background).setDrawble(drawable);
    }

    public float getTileRatio() {
        Drawable drawable = getTileDrawableByLayerId(android.R.id.progress).getDrawable();
        return (float) drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
    }

    public void setStarCount(int count) {
        getTileDrawableByLayerId(android.R.id.background).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.secondaryProgress).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.progress).setTileCount(count);
    }

    @SuppressLint("NewApi")
    private TileDrawable getTileDrawableByLayerId(int id) {
        Drawable layerDrawable = findDrawableByLayerId(id);
        switch (id) {
            case android.R.id.background:
                return (TileDrawable) layerDrawable;
            case android.R.id.secondaryProgress:
            case android.R.id.progress:
                ClipDrawableCompat clipDrawable = (ClipDrawableCompat) layerDrawable;
                return (TileDrawable) clipDrawable.getDrawable();
            default:
                // Should never reach here.
                throw new RuntimeException();
        }
    }
}
