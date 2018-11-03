package com.cartoon.data.response;

import com.cartoon.data.ActionOne;

import java.util.List;

/**
 * Created by cc on 17-11-14.
 */
public class ActionOneData {


    /**
     * uid : 336
     * approve_num : 0
     * is_approve : 0
     * bonus_stone : 6894
     * nickname : 凡人哥
     * honorName : 智
     * children : [{"uid":336,"bonus_stone":6894,"two_id":3094,"to_uid":336,"nickname":"凡人哥","to_create_time":"刚刚","to_content":"嗯嗯","sect_user_rank":"乌漆嘛黑掌门","honorName":"智","to_nickname":"凡人哥","bonus_point":142760,"avatar":"http://116.228.59.173:8888/book/upload/avatar/user/2017-09/18/336_1505700533946.jpg","honor_id":2,"lvName":"元婴后期"}]
     * type : 11
     * avatar : http://116.228.59.173:8888/book/upload/avatar/user/2017-09/18/336_1505700533946.jpg
     * bonus_point : 142760
     * photo : []
     * lvName : 元婴后期
     * id : 38132
     * content : 我是凡人，我有话说，我写了《日个狗》。快来给我投票，助我成仙！
     * module_id : 129
     * sect_user_rank : 乌漆嘛黑掌门
     * create_time : 刚刚
     * comment_num : 1
     * small_pic : null
     * honor_id : 2
     * module_title : 日个狗
     */

    private int uid;
    private int                approve_num;
    private int                is_approve;
    private int                bonus_stone;
    private String             nickname;
    private String             honorName;
    private int                type;
    private String             avatar;
    private int                bonus_point;
    private String             lvName;
    private int                id;
    private String             content;
    private int                module_id;
    private String             sect_user_rank;
    private String             create_time;
    private int                comment_num;
    private Object             small_pic;
    private int                honor_id;
    private String             module_title;
    private List<ActionOne> children;
    private List<String>            photo;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public int getBonus_stone() {
        return bonus_stone;
    }

    public void setBonus_stone(int bonus_stone) {
        this.bonus_stone = bonus_stone;
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

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Object getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(Object small_pic) {
        this.small_pic = small_pic;
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

    public List<ActionOne> getChildren() {
        return children;
    }

    public void setChildren(List<ActionOne> children) {
        this.children = children;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }



}
