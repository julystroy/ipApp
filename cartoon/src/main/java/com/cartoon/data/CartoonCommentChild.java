package com.cartoon.data;

/**
 * Created by David on 16/6/27.
 */
public class CartoonCommentChild  {

    /**
     * uid : 2988
     * bonus_stone : 5559
     * two_id : 3006
     * to_uid : 2988
     * nickname : 我勒个去
     * to_content : 哈哈哈
     * to_create_time : 3天前
     * honorName : 策
     * to_nickname : 我勒个去
     * avatar : http://116.228.59.173:8888/book/upload/avatar/user/2017-09/25/2988_1506331587857.jpg
     * bonus_point : 170204
     * honor_id : 6
     * lvName : 元婴大圆满
     */

    private String uid;
    private int    bonus_stone;
    private String    two_id;
    private int    to_uid;
    private String nickname;
    private String to_content;
    private String to_create_time;
    private String honorName;
    private String to_nickname;
    private String avatar;
    private int    bonus_point;
    private int    honor_id;
    private String lvName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(int bonus_stone) {
        this.bonus_stone = bonus_stone;
    }

    public String getTwo_id() {
        return two_id;
    }

    public void setTwo_id(String two_id) {
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

    public String getTo_content() {
        return to_content;
    }

    public void setTo_content(String to_content) {
        this.to_content = to_content;
    }

    public String getTo_create_time() {
        return to_create_time;
    }

    public void setTo_create_time(String to_create_time) {
        this.to_create_time = to_create_time;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(int bonus_point) {
        this.bonus_point = bonus_point;
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
        return "CartoonCommentChild{" +
                "uid=" + uid +
                ", bonus_stone=" + bonus_stone +
                ", two_id=" + two_id +
                ", to_uid=" + to_uid +
                ", nickname='" + nickname + '\'' +
                ", to_content='" + to_content + '\'' +
                ", to_create_time='" + to_create_time + '\'' +
                ", honorName='" + honorName + '\'' +
                ", to_nickname='" + to_nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bonus_point=" + bonus_point +
                ", honor_id=" + honor_id +
                ", lvName='" + lvName + '\'' +
                '}';
    }
}
