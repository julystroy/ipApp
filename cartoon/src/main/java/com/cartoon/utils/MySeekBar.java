package com.cartoon.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

/**
 * Created by cc on 16-12-21.
 */
public class MySeekBar extends SeekBar {

    private Context context;
    public MySeekBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
