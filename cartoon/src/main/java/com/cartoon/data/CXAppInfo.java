package com.cartoon.data;

import com.cartoon.data.game.Rating;
import com.cartoon.http.StaticField;

import cn.idianyun.streaming.data.LaunchInfo;

/**
 * Created by wuchuchu on 2017/7/26.
 */

public abstract class CXAppInfo {
    public long      id;
    public long      appId;
    public int       playType;
    public String    logo;
    public String    name;
    public String    intro;
    public String    shortIntro;
    public Rating    ratingDetail;
    public String    rating;
    public String    androidDownloadUrl;
    public String[]  categories;
    public QuickInfo quickInfo;

    public LaunchInfo changeToLaunchInfo() {
        if ((rating == "" || rating == null) && ratingDetail != null) {
            rating = ratingDetail.rating;
        }
        LaunchInfo launchInfo = new LaunchInfo();
        launchInfo.appId = (int) this.appId;
        launchInfo.appName = this.name;
        launchInfo.logo = String.format("%s%s", StaticField.BASE_CXURL, this.logo);
        launchInfo.intro = this.intro;
        launchInfo.shortIntro = this.shortIntro;
        launchInfo.rating = this.rating;
        launchInfo.downloadUrl = this.androidDownloadUrl;
        return launchInfo;
    }
}
