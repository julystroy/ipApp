package com.cartoon.data;

import java.io.Serializable;

public class User implements Serializable {
    public String name;
    public String avatar;
    public long id;

    //默认为游客
    private boolean visitor;

    //默认非VIP
    private boolean isVip;
    private long vipExpiryDate;

    // 本地缓存多余信息
    private String cookie;

    //未评分数
    public int noRatingCount;

    public User() {
        this.visitor = true;
        this.isVip = false;
        this.vipExpiryDate = 0;
        this.name = "";
        this.avatar = "";
        this.id = 0;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setVisitor(boolean visitor) {
        this.visitor = visitor;
    }

    public boolean isVisitor() {
        return this.visitor;
    }

    public void setVIP(boolean isVip) {
        this.isVip = isVip;
    }

    public boolean isVIP() {
        return this.isVip;
    }

    public void setVipExpiryDate(long expiryDate) {
        this.vipExpiryDate = expiryDate;
    }

    public long getVipExpiryDate() {
        return vipExpiryDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "visitor=" + visitor +
                ", name='" + name + '\'' +
                ", isVip='" + isVip + '\'' +
                ", vipExpiryDate='" + vipExpiryDate + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}

