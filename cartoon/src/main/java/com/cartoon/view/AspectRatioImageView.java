package com.cartoon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by shidu on 16/12/30.
 *
 * http://stackoverflow.com/questions/2991110/android-how-to-stretch-an-image-to-the-screen-width-while-maintaining-aspect-ra/2999707#2999707
 * http://stackoverflow.com/questions/12400113/resizing-imageview-to-fit-to-aspect-ratio
 */

public class AspectRatioImageView extends ImageView {

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getDrawable()!=null){
            if(getDrawable().getIntrinsicWidth()>=getDrawable().getIntrinsicHeight()){
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = width * getDrawable().getIntrinsicHeight()
                        / getDrawable().getIntrinsicWidth();
                setMeasuredDimension(width, height);
            }else{
                int height = MeasureSpec.getSize(heightMeasureSpec);
                int width = height * getDrawable().getIntrinsicWidth()
                        / getDrawable().getIntrinsicHeight();
                setMeasuredDimension(width, height);
            }
        }
    }
}
