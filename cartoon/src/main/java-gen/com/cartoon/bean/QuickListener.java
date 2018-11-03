package com.cartoon.bean;

import java.io.Serializable;

/**
 * Created by apple on 2016/10/10.
 */

public class QuickListener implements Serializable{



    /**
     * id : 24
     * title : del2
     * remote_connect : o8e95vq7x.bkt.clouddn.com/http://o8e95vq7x.bkt.clouddn.com/20161010112621_127.mp3
     * cover_pic : http://o8e95vq7x.bkt.clouddn.com/20161010112621_117.jpg
     * click_num : 1
     * create_time : 2016-10-10 13:26:32
     * is_publish : 0
     * collect : null
     * size : null
     */

    private int    id;
    private String title;
    private String remote_connect;
    private String cover_pic;
    private int    click_num;
    private String create_time;
    private Object collect;
    private String duration;
    private long size;



    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemote_connect() {
        return remote_connect;
    }

    public void setRemote_connect(String remote_connect) {
        this.remote_connect = remote_connect;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


    public Object getCollect() {
        return collect;
    }

    public void setCollect(Object collect) {
        this.collect = collect;
    }

    public QuickListener( int id, String title, String remote_connect, String cover_pic, int click_num, String create_time, Object collect) {
        this.id = id;
        this.title = title;
        this.remote_connect = remote_connect;
        this.cover_pic = cover_pic;
        this.click_num = click_num;
        this.create_time = create_time;
        this.collect = collect;
    }

    public QuickListener(){}

    public QuickListener(int  id) {
        this.id = id;
    }
}


