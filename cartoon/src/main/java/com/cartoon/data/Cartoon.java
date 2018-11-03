package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 漫画对象
 * <p/>
 * Created by David on 16/6/7.
 */
public class Cartoon implements Parcelable {
    /**
     * approve_num : null
     * remote_connect : od.qingting.fm/vod/00/00/0000000000000000000024699153_24.m4a?deviceid=867066026509771
     * is_approve  : 0
     * book_id : null
     * listener_num : null
     * size : 3.81M
     * id : 123
     * title : 凶宅笔记第三部_048
     * cover_pic : null
     * time_num : 1302
     * local_connect : null
     * domain : od.qingting.fm
     * create_time : 2016-06-15 19:12:31
     * comment_num : null
     * program_id : 2112921
     * small_pic : null
     * collect : null
     * "show_time": "刚刚", //发布时间
     *
     *

     */
    private String approve_num;
    private String remote_connect;
    private int is_approve;
    private String book_id;
    private String listener_num;
    private String size;
    private String id;
    private String title;
    private String cover_pic;
    private long time_num;
    private String local_connect;
    private String domain;
    private String create_time;
    private String show_time;
    private String comment_num;
    private String program_id;
    private String small_pic;
    private String collect;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    private String catalog;

    public String getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(String approve_num) {
        this.approve_num = approve_num;
    }

    public String getRemote_connect() {
        return remote_connect;
    }

    public void setRemote_connect(String remote_connect) {
        this.remote_connect = remote_connect;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getListener_num() {
        return listener_num;
    }

    public void setListener_num(String listener_num) {
        this.listener_num = listener_num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public long getTime_num() {
        return time_num;
    }

    public void setTime_num(long time_num) {
        this.time_num = time_num;
    }

    public String getLocal_connect() {
        return local_connect;
    }

    public void setLocal_connect(String local_connect) {
        this.local_connect = local_connect;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    @Override
    public String toString() {
        return "Cartoon{" +
                "approve_num='" + approve_num + '\'' +
                ", remote_connect='" + remote_connect + '\'' +
                ", is_approve=" + is_approve +
                ", book_id='" + book_id + '\'' +
                ", listener_num='" + listener_num + '\'' +
                ", size='" + size + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", cover_pic='" + cover_pic + '\'' +
                ", time_num=" + time_num +
                ", local_connect='" + local_connect + '\'' +
                ", domain='" + domain + '\'' +
                ", create_time='" + create_time + '\'' +
                ", show_time='" + show_time + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", program_id='" + program_id + '\'' +
                ", small_pic='" + small_pic + '\'' +
                ", collect='" + collect + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.approve_num);
        dest.writeString(this.remote_connect);
        dest.writeInt(this.is_approve);
        dest.writeString(this.book_id);
        dest.writeString(this.listener_num);
        dest.writeString(this.size);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.cover_pic);
        dest.writeLong(this.time_num);
        dest.writeString(this.local_connect);
        dest.writeString(this.domain);
        dest.writeString(this.create_time);
        dest.writeString(this.show_time);
        dest.writeString(this.comment_num);
        dest.writeString(this.program_id);
        dest.writeString(this.small_pic);
        dest.writeString(this.collect);
    }

    public Cartoon() {
    }

    protected Cartoon(Parcel in) {
        this.approve_num = in.readString();
        this.remote_connect = in.readString();
        this.is_approve = in.readInt();
        this.book_id = in.readString();
        this.listener_num = in.readString();
        this.size = in.readString();
        this.id = in.readString();
        this.title = in.readString();
        this.cover_pic = in.readString();
        this.time_num = in.readLong();
        this.local_connect = in.readString();
        this.domain = in.readString();
        this.create_time = in.readString();
        this.show_time = in.readString();
        this.comment_num = in.readString();
        this.program_id = in.readString();
        this.small_pic = in.readString();
        this.collect = in.readString();
    }

    public static final Creator<Cartoon> CREATOR = new Creator<Cartoon>() {
        @Override
        public Cartoon createFromParcel(Parcel source) {
            return new Cartoon(source);
        }

        @Override
        public Cartoon[] newArray(int size) {
            return new Cartoon[size];
        }
    };
}

