package com.cartoon;


public class Constants {

    public static final String DEVICE_TYPE = "android";

    public static final String PATH_CARTOON = "/cartoon";
    public static final String PATH_LISTENER = "/listener";
    public static final String PATH_BANGAI = "/bangai";



    /**
     * 点赞类型 1:漫画   2:听书   3: 嘻说   4:动态 5.评论  (+0:活动 6:追听    7:Q漫    8:番外    9:静态电影)
     *
     *
     * 活动=0 漫画=1 听书=2 嘻说=3  动态/书友圈=4
     评论=5 追听=6 Q漫=7 番外=8 静态电影=9
     书评=10 活动下的征文=11
     */
    public static final int APPROVE_CARTOON = 1;
    public static final int APPROVE_LISTENER = 2;
    public static final int APPROVE_EXPOUND = 3;
    public static final int APPROVE_MANUAL = 4;
    public static final int APPROVE_COMMENT = 5;
    public static final int APPROVE_BANGAI = 8;
    public static final int APPROVE_QMAN = 7;
    public static final int APPROVE_JINGPIN = 9;
    public static final int APPROVE_BOOK = 101;

    public static final int MAX_PAGE_HEIGHT = 1600;
    public static final int MAX_PAGE_WIDTH = 2000;

    public static final String TITLE = "title";
    public static final String RESOURCEID = "resourceid";

    public enum PageViewMode {
        ASPECT_FILL(0),
        ASPECT_FIT(1),
        FIT_WIDTH(2);

        private PageViewMode(int n) {
            native_int = n;
        }

        public final int native_int;
    }

    public static final String SETTINGS_PAGE_VIEW_MODE = "SETTINGS_PAGE_VIEW_MODE";
    public static final String SETTINGS_READING_LEFT_TO_RIGHT = "SETTINGS_READING_LEFT_TO_RIGHT";
}
