package com.cartoon.data;

/**
 * Created by jinbangzhu on 8/20/16.
 */

public class AppAD {


    private int id;
    private String title;
    private String ad_pric;
    private String description;
    private String create_time;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAd_pric() {
        return ad_pric;
    }

    public void setAd_pric(String ad_pric) {
        this.ad_pric = ad_pric;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
