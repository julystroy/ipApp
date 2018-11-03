package com.cartoon.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.com.xuanjiezhimen.R;

public class RichText extends TextView {

    private Drawable placeHolder, errorImage;
    private OnImageClickListener onImageClickListener;// 点击事件
    private HashSet<Target> targets;
    private int d_w = 100;
    private int d_h = 100;

    private DisplayMetrics dm;
    private int dwidth;

    private int padding = 0;

    public RichText(Context context) {
        this(context, null);
    }

    public RichText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        targets = new HashSet<>();

        padding = (int) getResources().getDimension(R.dimen.richText_pading);
        setPadding(padding, padding, padding, padding);
        dm = getResources().getDisplayMetrics();
        dwidth = (int) (dm.widthPixels - padding * 2); // padding left + padding
        // right

        placeHolder = getResources().getDrawable(R.mipmap.ic_launcher);
        errorImage = getResources().getDrawable(R.mipmap.ic_launcher);

        if (placeHolder == null) {
            placeHolder = new ColorDrawable(Color.GRAY);
        }
        placeHolder.setBounds(0, 0, d_w, d_h);
        if (errorImage == null) {
            errorImage = new ColorDrawable(Color.GRAY);
        }
        errorImage.setBounds(0, 0, d_w, d_h);
    }

    public void setRichText(String text) {
        targets.clear();
        Spanned spanned = Html.fromHtml(text, asyncImageGetter, null);
        SpannableStringBuilder spannableStringBuilder;
        if (spanned instanceof SpannableStringBuilder) {
            spannableStringBuilder = (SpannableStringBuilder) spanned;
        } else {
            spannableStringBuilder = new SpannableStringBuilder(spanned);
        }

        ImageSpan[] imageSpans = spannableStringBuilder.getSpans(0,
                spannableStringBuilder.length(), ImageSpan.class);
        final List<String> imageUrls = new ArrayList<>();

        for (int i = 0, size = imageSpans.length; i < size; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = spannableStringBuilder.getSpanStart(imageSpan);
            int end = spannableStringBuilder.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            final int finalI = i;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (onImageClickListener != null) {
                        onImageClickListener.imageClicked(imageUrls, finalI);
                    }
                }
            };
            ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(start, end,
                    ClickableSpan.class);
            if (clickableSpans != null && clickableSpans.length != 0) {
                for (ClickableSpan cs : clickableSpans) {
                    spannableStringBuilder.removeSpan(cs);
                }
            }
            spannableStringBuilder.setSpan(clickableSpan, start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        super.setText(spanned);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addTarget(Target target) {
        targets.add(target);
    }

    private Html.ImageGetter asyncImageGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            final URLDrawable urlDrawable = new URLDrawable();
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    int wid = dwidth;
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    Matrix matrix = new Matrix();
                    float scaleWidth = ((float) wid / width);
                    matrix.postScale(scaleWidth, scaleWidth);
                    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0,
                            width, height, matrix, true);

                    Drawable drawable = new BitmapDrawable(getResources(), newbmp);
                    drawable.setBounds(0, -newbmp.getHeight() / 2, newbmp.getWidth(), newbmp.getHeight() / 2);
                    urlDrawable.setDrawable(drawable);
                    urlDrawable.setBounds(drawable.getBounds());

                    RichText.this.invalidate();
                    RichText.this.setText(getText());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    urlDrawable.setBounds(errorDrawable.getBounds());
                    urlDrawable.setDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    urlDrawable.setBounds(placeHolderDrawable.getBounds());
                    urlDrawable.setDrawable(placeHolderDrawable);
                }
            };
            addTarget(target);
//			Glide.with(getContext()).load(source).placeholder(placeHolder).error(errorImage).into(urlDrawable);
            Picasso.with(getContext()).load(source)
                    .placeholder(placeHolder).error(errorImage)
                    .into(target);
            return urlDrawable;
        }

    };

    private static final class URLDrawable extends BitmapDrawable {

        private Drawable drawable;

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }

    public void setPlaceHolder(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        this.placeHolder.setBounds(0, 0, d_w, d_h);
    }

    public void setErrorImage(Drawable errorImage) {
        this.errorImage = errorImage;
        this.errorImage.setBounds(0, 0, d_w, d_h);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public interface OnImageClickListener {
        void imageClicked(List<String> imageUrls, int position);
    }
}
