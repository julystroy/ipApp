package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-11-14.
 */
public class ActionOne implements Parcelable{
    /**
     * uid : 336
     * bonus_stone : 6894
     * two_id : 3094
     * to_uid : 336
     * nickname : 凡人哥
     * to_create_time : 刚刚
     * to_content : 嗯嗯
     * sect_user_rank : 乌漆嘛黑掌门
     * honorName : 智
     * to_nickname : 凡人哥
     * bonus_point : 142760
     * avatar : http://116.228.59.173:8888/book/upload/avatar/user/2017-09/18/336_1505700533946.jpg
     * honor_id : 2
     * lvName : 元婴后期
     */

    private int uid;
    private int    bonus_stone;
    private int    two_id;
    private int    to_uid;
    private String nickname;
    private String to_create_time;
    private String to_content;
    private String sect_user_rank;
    private String honorName;
    private String to_nickname;
    private int    bonus_point;
    private String avatar;
    private int    honor_id;
    private String lvName;

    public ActionOne() {
    }

    protected ActionOne(Parcel in) {
        uid = in.readInt();
        bonus_stone = in.readInt();
        two_id = in.readInt();
        to_uid = in.readInt();
        nickname = in.readString();
        to_create_time = in.readString();
        to_content = in.readString();
        sect_user_rank = in.readString();
        honorName = in.readString();
        to_nickname = in.readString();
        bonus_point = in.readInt();
        avatar = in.readString();
        honor_id = in.readInt();
        lvName = in.readString();
    }

    public static final Creator<ActionOne> CREATOR = new Creator<ActionOne>() {
        @Override
        public ActionOne createFromParcel(Parcel in) {
            return new ActionOne(in);
        }

        @Override
        public ActionOne[] newArray(int size) {
            return new ActionOne[size];
        }
    };

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(int bonus_stone) {
        this.bonus_stone = bonus_stone;
    }

    public int getTwo_id() {
        return two_id;
    }

    public void setTwo_id(int two_id) {
        this.two_id = two_id;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTo_create_time() {
        return to_create_time;
    }

    public void setTo_create_time(String to_create_time) {
        this.to_create_time = to_create_time;
    }

    public String getTo_content() {
        return to_content;
    }

    public void setTo_content(String to_content) {
        this.to_content = to_content;
    }

    public String getSect_user_rank() {
        return sect_user_rank;
    }

    public void setSect_user_rank(String sect_user_rank) {
        this.sect_user_rank = sect_user_rank;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
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

    public int getHonor_id() {
        return honor_id;
    }

    public void setHonor_id(int honor_id) {
        this.honor_id = honor_id;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    @Override
    public String toString() {
        return "ActionOne{" +
                "uid=" + uid +
                ", bonus_stone=" + bonus_stone +
                ", two_id=" + two_id +
                ", to_uid=" + to_uid +
                ", nickname='" + nickname + '\'' +
                ", to_create_time='" + to_create_time + '\'' +
                ", to_content='" + to_content + '\'' +
                ", sect_user_rank='" + sect_user_rank + '\'' +
                ", honorName='" + honorName + '\'' +
                ", to_nickname='" + to_nickname + '\'' +
                ", bonus_point=" + bonus_point +
                ", avatar='" + avatar + '\'' +
                ", honor_id=" + honor_id +
                ", lvName='" + lvName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeInt(bonus_stone);
        dest.writeInt(two_id);
        dest.writeInt(to_uid);
        dest.writeString(nickname);
        dest.writeString(to_create_time);
        dest.writeString(to_content);
        dest.writeString(sect_user_rank);
        dest.writeString(honorName);
        dest.writeString(to_nickname);
        dest.writeInt(bonus_point);
        dest.writeString(avatar);
        dest.writeInt(honor_id);
        dest.writeString(lvName);
    }
}
