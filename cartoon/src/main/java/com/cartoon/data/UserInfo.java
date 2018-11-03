package com.cartoon.data;

import java.io.Serializable;

/**
 * 用户信息
 * <p/>
 * Created by David on 16/6/5.
 **/
public class UserInfo implements Serializable {

    private String id;
    private int training_days;
    private int    gender;
    private String device_id;
    private int    honor_id;
    private String mobile;
    private String book_code;
    private String avatar;
    private String    bonus_stone;
    private String    bonus_point;
    private String register_time;
    private String token;
    private String nickname;
    private String    sect_id;
    private int    state;
    private String    sectionId;
    private String rank;//排行taskNum
    private boolean isManager;
    private boolean hasSectionMsg ;
    private boolean hasTaskMsg ;
    private boolean applyMsg;
    private boolean hasSign;
    private String honorName;
    private String maxPoint;
    private String lvName;
    private String nowPoint;


    /**
     "hasTaskMsg": true,//是否有任务红点（true=有）
     "cards": [ ],//道具（["三日双倍经验卡"]）
     "gender": 1,//性别
     "device_id": null,//设备号
     "honor_id": 15,//称号id
     "mobile": "15811519834",//手机
     "book_code": "书友101308",//号码
     "avatar": "/upload/avatar/user/2016-08/22/21_1471870058879.jpg",//头像
     "sectionId": 74,//宗门id（null）
     "bonus_stone": 1150,//灵石
     "bonus_point": 123725,//经验
     "register_time": "2016-08-05 23:44:48",//注册时间
     "token": "EBAF71C9A7E1530AE02E11A990FCA64C",
     "hasSectionMsg": true,//是否有宗门消息红点（true）
     "maxPoint": 24650,//经验条后半段
     "lvType": 4,//大等级
     "nickname": "金克拉",//昵称
     "isManager": false,//是否是管理员（掌门，长老用于跳转宗门事务）
     "lvName": "元婴后期",//等级
     "id": 21,//用户id
     "state": 0,//状态
     "nowPoint": 649//经验条前半段
     applyMsg:宗门事物信息  true = 有消息 红点
     false = 没有消息
     sect_id     宗门id


     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTraining_days() {
        return training_days;
    }

    public void setTraining_days(int training_days) {
        this.training_days = training_days;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getHonor_id() {
        return honor_id;
    }

    public void setHonor_id(int honor_id) {
        this.honor_id = honor_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBook_code() {
        return book_code;
    }

    public void setBook_code(String book_code) {
        this.book_code = book_code;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(String bonus_stone) {
        this.bonus_stone = bonus_stone;
    }

    public String getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(String bonus_point) {
        this.bonus_point = bonus_point;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSect_id() {
        return sect_id;
    }

    public void setSect_id(String sect_id) {
        this.sect_id = sect_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isHasSectionMsg() {
        return hasSectionMsg;
    }

    public void setHasSectionMsg(boolean hasSectionMsg) {
        this.hasSectionMsg = hasSectionMsg;
    }

    public boolean isHasTaskMsg() {
        return hasTaskMsg;
    }

    public void setHasTaskMsg(boolean hasTaskMsg) {
        this.hasTaskMsg = hasTaskMsg;
    }

    public boolean isApplyMsg() {
        return applyMsg;
    }

    public void setApplyMsg(boolean applyMsg) {
        this.applyMsg = applyMsg;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(String maxPoint) {
        this.maxPoint = maxPoint;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public String getNowPoint() {
        return nowPoint;
    }

    public void setNowPoint(String nowPoint) {
        this.nowPoint = nowPoint;
    }

    public boolean isHasSign() {
        return hasSign;
    }

    public void setHasSign(boolean hasSign) {
        this.hasSign = hasSign;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", training_days=" + training_days +
                ", gender=" + gender +
                ", device_id='" + device_id + '\'' +
                ", honor_id=" + honor_id +
                ", mobile='" + mobile + '\'' +
                ", book_code='" + book_code + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bonus_stone='" + bonus_stone + '\'' +
                ", bonus_point='" + bonus_point + '\'' +
                ", register_time='" + register_time + '\'' +
                ", token='" + token + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sect_id='" + sect_id + '\'' +
                ", state=" + state +
                ", sectionId='" + sectionId + '\'' +
                ", rank='" + rank + '\'' +
                ", isManager=" + isManager +
                ", hasSectionMsg=" + hasSectionMsg +
                ", hasTaskMsg=" + hasTaskMsg +
                ", applyMsg=" + applyMsg +
                ", hasSign=" + hasSign +
                ", honorName='" + honorName + '\'' +
                ", maxPoint='" + maxPoint + '\'' +
                ", lvName='" + lvName + '\'' +
                ", nowPoint='" + nowPoint + '\'' +
                '}';
    }
}
