package com.cartoon.data.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cc on 17-11-10.
 */
public class CommentData implements Parcelable {

    /**
     * uid : 2988
     * approve_num : 0
     * bonus_stone : 501
     * is_approve : 0
     * nickname : 我勒个去
     * honorName : 策
     * type : 7
     * bonus_point : 31587
     * avatar : http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg
     * photo : []
     * lvName : 结丹初期
     * content : 9999999999999999999999
     * id : 38078
     * module_id : 99
     * sect_user_rank : 社会治安掌门
     * create_time : 36分钟前
     * comment_num : 0
     * honor_id : 6
     * module_title : 宠物
     */

    private String uid;
    private String     approve_num;
    private int     bonus_stone;
    private int     is_approve;
    private String  nickname;
    private String  honorName;
    private int     type;
    private int     bonus_point;
    private String  avatar;
    private String  lvName;
    private String  content;
    private String     id;
    private int     module_id;
    private String  sect_user_rank;
    private String  create_time;
    private int     comment_num;
    private int     honor_id;
    private String  module_title;
    private List<String> photo;


    public CommentData( ) {
    }

    protected CommentData(Parcel in) {
        uid = in.readString();
        approve_num = in.readString();
        bonus_stone = in.readInt();
        is_approve = in.readInt();
        nickname = in.readString();
        honorName = in.readString();
        type = in.readInt();
        bonus_point = in.readInt();
        avatar = in.readString();
        lvName = in.readString();
        content = in.readString();
        id = in.readString();
        module_id = in.readInt();
        sect_user_rank = in.readString();
        create_time = in.readString();
        comment_num = in.readInt();
        honor_id = in.readInt();
        module_title = in.readString();
        photo = in.createStringArrayList();
    }

    public static final Creator<CommentData> CREATOR = new Creator<CommentData>() {
        @Override
        public CommentData createFromParcel(Parcel in) {
            return new CommentData(in);
        }

        @Override
        public CommentData[] newArray(int size) {
            return new CommentData[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(String approve_num) {
        this.approve_num = approve_num;
    }

    public int getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(int bonus_stone) {
        this.bonus_stone = bonus_stone;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(int bonus_point) {
        this.bonus_point = bonus_point;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public String getSect_user_rank() {
        return sect_user_rank;
    }

    public void setSect_user_rank(String sect_user_rank) {
        this.sect_user_rank = sect_user_rank;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getHonor_id() {
        return honor_id;
    }

    public void setHonor_id(int honor_id) {
        this.honor_id = honor_id;
    }

    public String getModule_title() {
        return module_title;
    }

    public void setModule_title(String module_title) {
        this.module_title = module_title;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "uid='" + uid + '\'' +
                ", approve_num='" + approve_num + '\'' +
                ", bonus_stone=" + bonus_stone +
                ", is_approve=" + is_approve +
                ", nickname='" + nickname + '\'' +
                ", honorName='" + honorName + '\'' +
                ", type=" + type +
                ", bonus_point=" + bonus_point +
                ", avatar='" + avatar + '\'' +
                ", lvName='" + lvName + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", module_id=" + module_id +
                ", sect_user_rank='" + sect_user_rank + '\'' +
                ", create_time='" + create_time + '\'' +
                ", comment_num=" + comment_num +
                ", honor_id=" + honor_id +
                ", module_title='" + module_title + '\'' +
                ", photo=" + photo +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(approve_num);
        dest.writeInt(bonus_stone);
        dest.writeInt(is_approve);
        dest.writeString(nickname);
        dest.writeString(honorName);
        dest.writeInt(type);
        dest.writeInt(bonus_point);
        dest.writeString(avatar);
        dest.writeString(lvName);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeInt(module_id);
        dest.writeString(sect_user_rank);
        dest.writeString(create_time);
        dest.writeInt(comment_num);
        dest.writeInt(honor_id);
        dest.writeString(module_title);
        dest.writeStringList(photo);
    }
}
