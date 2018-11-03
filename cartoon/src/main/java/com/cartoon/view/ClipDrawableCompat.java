package com.cartoon.view;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class ClipDrawableCompat extends ClipDrawable {
    private Drawable mDrawable;

    private DummyConstantState mConstantState = new DummyConstantState();

    public ClipDrawableCompat(Drawable drawable, int gravity, int orientation) {
        super(drawable, gravity, orientation);

        mDrawable = drawable;
    }

    @Nullable
    @Override
    public Drawable getDrawable() {
        return mDrawable;
    }

    // Workaround LayerDrawable.ChildDrawable which calls getConstantState().newDrawable()
    // without checking for null.
    // We are never inflated from XML so the protocol of ConstantState does not apply to us. In
    // order to make LayerDrawable happy, we return ourselves from DummyConstantState.newDrawable().

    @Override
    public ConstantState getConstantState() {
        return mConstantState;
    }

    private class DummyConstantState extends ConstantState {

        @Override
        public int getChangingConfigurations() {
            return 0;
        }

        @NonNull
        @Override
        public Drawable newDrawable() {
            return ClipDrawableCompat.this;
        }
    }
}
