package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-10-16.
 */
public class SectList implements Parcelable {

    /**
     * create_user_id : 2988
     * last_modified_time : null
     * create_time : 2017-10-25 19:16:19
     * userSize : 1
     * maxUserNum : 20
     * num : 1/20
     * user_num : 20
     * sect_level : 1
     * userFull : false
     * is_show : 1
     * contribution : 0
     * sect_id : 71
     * nickname : 我勒个去
     * hasApplied : false
     * sect_name : 妹子天才
     * sect_status : 0
     */

    private int create_user_id;
    private String  last_modified_time;
    private String  create_time;
    private int     userSize;
    private int     maxUserNum;
    private String  num;
    private int     user_num;
    private int     sect_level;
    private boolean userFull;
    private int     is_show;
    private int     contribution;
    private int     sect_id;
    private String  nickname;
    private boolean  hasApplied;
    private String  sect_name;
    private int     sect_status;

    public SectList() {

    }

    protected SectList(Parcel in) {
        create_user_id = in.readInt();
        last_modified_time = in.readString();
        create_time = in.readString();
        userSize = in.readInt();
        maxUserNum = in.readInt();
        num = in.readString();
        user_num = in.readInt();
        sect_level = in.readInt();
        userFull = in.readByte() != 0;
        is_show = in.readInt();
        contribution = in.readInt();
        sect_id = in.readInt();
        nickname = in.readString();
        hasApplied = in.readByte() != 0;
        sect_name = in.readString();
        sect_status = in.readInt();
    }

    public static final Creator<SectList> CREATOR = new Creator<SectList>() {
        @Override
        public SectList createFromParcel(Parcel in) {
            return new SectList(in);
        }

        @Override
        public SectList[] newArray(int size) {
            return new SectList[size];
        }
    };

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(String last_modified_time) {
        this.last_modified_time = last_modified_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getUserSize() {
        return userSize;
    }

    public void setUserSize(int userSize) {
        this.userSize = userSize;
    }

    public int getMaxUserNum() {
        return maxUserNum;
    }

    public void setMaxUserNum(int maxUserNum) {
        this.maxUserNum = maxUserNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public int getSect_level() {
        return sect_level;
    }

    public void setSect_level(int sect_level) {
        this.sect_level = sect_level;
    }

    public boolean isUserFull() {
        return userFull;
    }

    public void setUserFull(boolean userFull) {
        this.userFull = userFull;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getSect_id() {
        return sect_id;
    }

    public void setSect_id(int sect_id) {
        this.sect_id = sect_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isHasApplied() {
        return hasApplied;
    }

    public void setHasApplied(boolean hasApplied) {
        this.hasApplied = hasApplied;
    }

    public String getSect_name() {
        return sect_name;
    }

    public void setSect_name(String sect_name) {
        this.sect_name = sect_name;
    }

    public int getSect_status() {
        return sect_status;
    }

    public void setSect_status(int sect_status) {
        this.sect_status = sect_status;
    }

    @Override
    public String toString() {
        return "SectList{" +
                "create_user_id=" + create_user_id +
                ", last_modified_time='" + last_modified_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", userSize=" + userSize +
                ", maxUserNum=" + maxUserNum +
                ", num='" + num + '\'' +
                ", user_num=" + user_num +
                ", sect_level=" + sect_level +
                ", userFull=" + userFull +
                ", is_show=" + is_show +
                ", contribution=" + contribution +
                ", sect_id=" + sect_id +
                ", nickname='" + nickname + '\'' +
                ", hasApplied=" + hasApplied +
                ", sect_name='" + sect_name + '\'' +
                ", sect_status=" + sect_status +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(create_user_id);
        dest.writeString(last_modified_time);
        dest.writeString(create_time);
        dest.writeInt(userSize);
        dest.writeInt(maxUserNum);
        dest.writeString(num);
        dest.writeInt(user_num);
        dest.writeInt(sect_level);
        dest.writeByte((byte) (userFull ? 1 : 0));
        dest.writeInt(is_show);
        dest.writeInt(contribution);
        dest.writeInt(sect_id);
        dest.writeString(nickname);
        dest.writeByte((byte) (hasApplied ? 1 : 0));
        dest.writeString(sect_name);
        dest.writeInt(sect_status);
    }
}
