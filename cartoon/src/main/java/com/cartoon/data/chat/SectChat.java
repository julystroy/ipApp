package com.cartoon.data.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by cc on 17-12-7.
 */
@Entity
public class SectChat {

    /**
     * id : 336
     * nickname : 凡人
     * rank_name : 掌门
     * user_status : 0正常状态  2禁言中
     * avatar : http://image.mopian.tv/upload/avatar/user/2017-10/31/336_1509424856222.jpg
     * lv_name : 结丹大圆满
     */
    @Id
    private Long id;
    @Property
    private String nickname;
    @Property
    private String sect_id;
    @Property
    private String rank_name;
    @Property
    private String user_status;
    @Property
    private String avatar;
    @Property
    private String lv_name;
    public String getLv_name() {
        return this.lv_name;
    }
    public void setLv_name(String lv_name) {
        this.lv_name = lv_name;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getUser_status() {
        return this.user_status;
    }
    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
    public String getRank_name() {
        return this.rank_name;
    }
    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }
    public String getSect_id() {
        return this.sect_id;
    }
    public void setSect_id(String sect_id) {
        this.sect_id = sect_id;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1841690065)
    public SectChat(Long id, String nickname, String sect_id, String rank_name,
            String user_status, String avatar, String lv_name) {
        this.id = id;
        this.nickname = nickname;
        this.sect_id = sect_id;
        this.rank_name = rank_name;
        this.user_status = user_status;
        this.avatar = avatar;
        this.lv_name = lv_name;
    }
    @Generated(hash = 1650666281)
    public SectChat() {
    }





}
