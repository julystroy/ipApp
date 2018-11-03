package com.cartoon.data;

import java.util.List;

public class DetailPage extends CXAppInfo {
    public static final int PLAY_TYPE_NONE = -1;
    public static final int PLAY_TYPE_BOTH = PLAY_TYPE_NONE + 1;
    public static final int PLAY_TYPE_PLAY = PLAY_TYPE_BOTH + 1;
    public static final int PLAY_TYPE_INSTALL = PLAY_TYPE_PLAY + 1;

    public String strategy;
    public long commentCount;
    public long playCount;
    public long downloadCount;
    public String size;
    public String manufacturer;
    public DetailUserInfo user;

    public List<AppFeature> features;
    public List<Carousel> carousels;
    public List<GameAccount> accounts;

    public Recommender recommended;
    public List<Comment> comments;
}