package com.cartoon.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  番外数据对象
 * <p/>
 * Created by David on 16/6/21.
 */
public class Bangai implements Parcelable {

    /**
     * "id": 1002,
     * "title": "第一集:渔村少年",
     * "cover_pic": "http://xxx.com/1.jpg", // 封面图
     * " preface": "序文介绍内容", //序文
     * " content": "xxx 嘻说内容xxx", //
     * " approve_num ": "99", //点赞数
     * " comment_num ":"99", //评论数
     * " is_approve ": "0", //0 未赞   1:已赞
     * " create_time ":"2015-02-01 12:11:02", //更新时间
     */
    private int id;
    private String title;
    private String cover_pic;
    private String preface;
    private String content;
    private String approve_num;
    private String comment_num;
    private int is_approve;
    private String create_time;
    private String collect;
    private int previous_id;//上一级
    private int next_id;//下一集

    public int getPreviousId() {
        return previous_id;
    }

    public void setPreviousId(int previous_id) {
        this.previous_id = previous_id;
    }

    public int getNextId() {
        return next_id;
    }

    public void setNextId(int next_id) {
        this.next_id = next_id;
    }
    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    private String catalog;

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

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(String approve_num) {
        this.approve_num = approve_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    @Override
    public String toString() {
        return "Bangai{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cover_pic='" + cover_pic + '\'' +
                ", preface='" + preface + '\'' +
                ", content='" + content + '\'' +
                ", approve_num='" + approve_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", is_approve=" + is_approve +
                ", create_time='" + create_time + '\'' +
                ", collect='" + collect + '\'' +
                ", previous_id=" + previous_id +
                ", next_id=" + next_id +
                ", catalog='" + catalog + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.cover_pic);
        dest.writeString(this.preface);
        dest.writeString(this.content);
        dest.writeString(this.approve_num);
        dest.writeString(this.comment_num);
        dest.writeInt(this.is_approve);
        dest.writeInt(this.next_id);
        dest.writeInt(this.previous_id);
        dest.writeString(this.create_time);
    }

    public Bangai() {
    }

    protected Bangai(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.cover_pic = in.readString();
        this.preface = in.readString();
        this.content = in.readString();
        this.approve_num = in.readString();
        this.comment_num = in.readString();
        this.is_approve = in.readInt();
        this.next_id = in.readInt();
        this.previous_id = in.readInt();
        this.create_time = in.readString();
    }

    public static final Creator<Bangai> CREATOR = new Creator<Bangai>() {
        @Override
        public Bangai createFromParcel(Parcel source) {
            return new Bangai(source);
        }

        @Override
        public Bangai[] newArray(int size) {
            return new Bangai[size];
        }
    };
}
