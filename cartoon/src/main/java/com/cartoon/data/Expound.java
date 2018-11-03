package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  嘻说数据对象
 * <p/>
 * Created by David on 16/6/21.
 */
public class Expound implements Parcelable {

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
     * ,"approveNum":558,"isLiked":0
     *
     * {"content":"<p>番外内容<\/p>","id":1,"isLiked":0,"showTime":"2017-04-24 15:36:45",
     * "approveNum":5,"title":"番外标题",
     * "coverPic":"\/upload\/image\/20170421194126_844.jpg"}
     *
     * isDisable
     */
    private int id;
    private int isDisable;
    private int isTop ;
    private int isFoot;
    private String title;
    private String cover_pic;
    private String coverPic;
    private String preface;
    private String content;
    private String approve_num;
    private String comment_num;
    private int is_approve;
    private String create_time;
    private String show_time;
    private String showTime;
    private String approveNum;
    private String isLiked;

    protected Expound(Parcel in) {
        id = in.readInt();
        isTop  = in.readInt();
        isFoot = in.readInt();
        isDisable = in.readInt();
        title = in.readString();
        cover_pic = in.readString();
        coverPic = in.readString();
        preface = in.readString();
        content = in.readString();
        approve_num = in.readString();
        comment_num = in.readString();
        is_approve = in.readInt();
        create_time = in.readString();
        show_time = in.readString();
        showTime = in.readString();
        approveNum = in.readString();
        isLiked = in.readString();
    }

    public static final Creator<Expound> CREATOR = new Creator<Expound>() {
        @Override
        public Expound createFromParcel(Parcel in) {
            return new Expound(in);
        }

        @Override
        public Expound[] newArray(int size) {
            return new Expound[size];
        }
    };

    public String getApproveNum() {
        return approveNum;
    }

    public void setApproveNum(String approveNum) {
        this.approveNum = approveNum;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

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


    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public Expound() {
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsFoot() {
        return isFoot;
    }

    public void setIsFoot(int isFoot) {
        this.isFoot = isFoot;
    }

    public int getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(int isDisable) {
        this.isDisable = isDisable;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    @Override
    public String toString() {
        return "Expound{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cover_pic='" + cover_pic + '\'' +
                ", coverPic='" + coverPic + '\'' +
                ", preface='" + preface + '\'' +
                ", content='" + content + '\'' +
                ", approve_num='" + approve_num + '\'' +
                ", comment_num='" + comment_num + '\'' +
                ", is_approve=" + is_approve +
                ", create_time='" + create_time + '\'' +
                ", approveNum='" + approveNum + '\'' +
                ", isLiked='" + isLiked + '\'' +
                ", isDisable='" + isDisable + '\'' +
                ", isTop ='" + isTop  + '\'' +
                ", isFoot='" + isFoot + '\'' +
                ", show_time='" + show_time + '\'' +
                ", showTime='" + showTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(isTop);
        dest.writeInt(isFoot);
        dest.writeInt(isDisable);
        dest.writeString(title);
        dest.writeString(cover_pic);
        dest.writeString(coverPic);
        dest.writeString(preface);
        dest.writeString(content);
        dest.writeString(approve_num);
        dest.writeString(comment_num);
        dest.writeInt(is_approve);
        dest.writeString(create_time);
        dest.writeString(show_time);
        dest.writeString(showTime);
        dest.writeString(approveNum);
        dest.writeString(isLiked);
    }
}
