package com.cartoon.utils;

import android.content.Context;

/**
 *
 */

public class DisplayUtil {

    /*手柄起始角度*/
    public static final float ROTATION_INIT_NEEDLE = -30;
    public static final float ROTATION_BACKGROUND = 360;

    /*截图屏幕宽高*/
    private static final float BASE_SCREEN_WIDTH = (float) 1080.0;
    private static final float BASE_SCREEN_HEIGHT = (float) 1920.0;

    /*唱针宽高、距离等比例*/
    public static final float SCALE_NEEDLE_WIDTH = (float) (276.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_MARGIN_LEFT = (float) (500.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_PIVOT_X = (float) (43.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_PIVOT_Y = (float) (43.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_HEIGHT = (float) (413.0 / BASE_SCREEN_HEIGHT);
    public static final float SCALE_NEEDLE_MARGIN_TOP = (float) (43.0 / BASE_SCREEN_HEIGHT);

    /*唱盘比例*/
    public static final float SCALE_DISC_SIZE = (float) (813.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_DISC_MARGIN_TOP = (float) (180 / BASE_SCREEN_HEIGHT);//调整唱盘top


    /*设备屏幕宽度*/
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /*设备屏幕高度*/
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
