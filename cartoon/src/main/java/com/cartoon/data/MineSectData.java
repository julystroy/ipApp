package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-10-17.
 */
public class MineSectData implements Parcelable{

    /**
     * user_status : 0
     * rank_id : 1
     * rank_name : 掌门
     * last_modified_time : 2017-09-22 17:02:00
     * create_time : 2017-09-22 17:02:00
     * dailyContribution : null
     * isMyself : false
     * contribution : 0
     * user_id : 38
     * sect_id : 13
     * nickname : 黑冰
     * lvName : 筑基初期
     * user_sect_id : 11
     * "avatar": "/upload/avatar/user/2016-09/18/25_1474185128201.jpg", //头像
     *
     * msg:"加入了宗门"
     *  "defense":16,//防御值
     "attack":16,//攻击值
     *
     */

    private int user_status;
    private int    rank_id;
    private String rank_name;
    private String last_modified_time;
    private String create_time;
    private String dailyContribution;
    private boolean isMyself;
    private int    contribution;
    private int    user_id;
    private int    sect_id;
    private String nickname;
    private String lvName;
    private String avatar;
    private String msg;
    private int    user_sect_id;
    private int    defense;
    private int    attack;

    public MineSectData(){}

    protected MineSectData(Parcel in) {
        user_status = in.readInt();
        rank_id = in.readInt();
        rank_name = in.readString();
        last_modified_time = in.readString();
        create_time = in.readString();
        dailyContribution = in.readString();
        isMyself = in.readByte() != 0;
        contribution = in.readInt();
        user_id = in.readInt();
        sect_id = in.readInt();
        nickname = in.readString();
        lvName = in.readString();
        avatar = in.readString();
        msg = in.readString();
        user_sect_id = in.readInt();
        defense = in.readInt();
        attack = in.readInt();
    }

    public static final Creator<MineSectData> CREATOR = new Creator<MineSectData>() {
        @Override
        public MineSectData createFromParcel(Parcel in) {
            return new MineSectData(in);
        }

        @Override
        public MineSectData[] newArray(int size) {
            return new MineSectData[size];
        }
    };

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getRank_id() {
        return rank_id;
    }

    public void setRank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
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

    public String getDailyContribution() {
        return dailyContribution;
    }

    public void setDailyContribution(String dailyContribution) {
        this.dailyContribution = dailyContribution;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUser_sect_id() {
        return user_sect_id;
    }

    public void setUser_sect_id(int user_sect_id) {
        this.user_sect_id = user_sect_id;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    @Override
    public String toString() {
        return "MineSectData{" +
                "user_status=" + user_status +
                ", rank_id=" + rank_id +
                ", rank_name='" + rank_name + '\'' +
                ", last_modified_time='" + last_modified_time + '\'' +
                ", create_time='" + create_time + '\'' +
                ", dailyContribution='" + dailyContribution + '\'' +
                ", isMyself=" + isMyself +
                ", contribution=" + contribution +
                ", user_id=" + user_id +
                ", sect_id=" + sect_id +
                ", nickname='" + nickname + '\'' +
                ", lvName='" + lvName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", msg='" + msg + '\'' +
                ", user_sect_id=" + user_sect_id +
                ", defense=" + defense +
                ", attack=" + attack +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_status);
        dest.writeInt(rank_id);
        dest.writeString(rank_name);
        dest.writeString(last_modified_time);
        dest.writeString(create_time);
        dest.writeString(dailyContribution);
        dest.writeByte((byte) (isMyself ? 1 : 0));
        dest.writeInt(contribution);
        dest.writeInt(user_id);
        dest.writeInt(sect_id);
        dest.writeString(nickname);
        dest.writeString(lvName);
        dest.writeString(avatar);
        dest.writeString(msg);
        dest.writeInt(user_sect_id);
        dest.writeInt(defense);
        dest.writeInt(attack);
    }
}
