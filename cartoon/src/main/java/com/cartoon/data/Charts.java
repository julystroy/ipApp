package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-5.
 */
public class Charts implements Parcelable{
    /*"nickname": "金坷垃",
            "rank": 1, //排名
	   "lvName": "神境大圆满",
            "id": 21,
            "avatar": "/upload/avatar/user/2016-12/26/21_1482739064847.jpg",
            "state": 0,
            "bonus_point": 816147*/

    private String nickname;
    private String lvName;
    private String avatar;

    private int rank;
    private int id;
    private int state;
    private int bonus_point;

    protected Charts(Parcel in) {
        nickname = in.readString();
        lvName = in.readString();
        avatar = in.readString();
        rank = in.readInt();
        id = in.readInt();
        state = in.readInt();
        bonus_point = in.readInt();
    }

    public Charts(){}
    public static final Creator<Charts> CREATOR = new Creator<Charts>() {
        @Override
        public Charts createFromParcel(Parcel in) {
            return new Charts(in);
        }

        @Override
        public Charts[] newArray(int size) {
            return new Charts[size];
        }
    };

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(int bonus_point) {
        this.bonus_point = bonus_point;
    }

    @Override
    public String toString() {
        return "Charts{" +
                "nickname='" + nickname + '\'' +
                ", lvName='" + lvName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", rank=" + rank +
                ", id=" + id +
                ", state=" + state +
                ", bonus_point=" + bonus_point +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickname);
        dest.writeString(lvName);
        dest.writeString(avatar);
        dest.writeInt(rank);
        dest.writeInt(id);
        dest.writeInt(state);
        dest.writeInt(bonus_point);
    }
}
