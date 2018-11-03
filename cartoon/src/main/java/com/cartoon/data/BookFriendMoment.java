package com.cartoon.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jinbangzhu on 7/20/16.
 */
//        " id": 10,	//评论ID
//        " uid ": 1001,
//        "nickname": "昵称",
//        "avatar": "http://xxx.com/1.jpg", //头像
//        " create_time ": "12:11:02",//评论时间
//        " content": "评论内容, 评论内容, 评论内容, 评论内容.",
//        " module_id ": "1"
//        "type":" 1 "//:1:漫画   2:听书   3: 嘻说  4:动态    //11 ：活动征文专属
//        " comment_num ":"99", //评论数
//        " approve_num ": "99", //点赞数
//        " is_approve ": "0", //0 未赞   1:已赞
//        " is_recomm ": "0", //0 不显示置顶   1:显示置顶

//        "honorName": null, //称号
//
//        "bonus_stone": 0, //灵石
//
//        "bonus_point": 1, //积分
//
//        "lvName": "武徒一层", //等级
//        "canVote": -1,  //-1=不能投票  0=可以投票
//        "sect_user_rank   //宗门职称
//        "activityId":"155"  活动征文id



public class BookFriendMoment implements Serializable {

    private int approve_num;
    private String nickname;
    private String create_time;
    private String content;
    private String avatar;
    private String small_pic;
    private String is_recomm;
    private String honorName;
    private String lvName;
    private String activityId;


    private String module_title;
    private String sect_user_rank;
    private int comment_num;
    private int is_approve;
    private int id;
    private int uid;
    private int module_id;
    private int type;
    private int canVote;

    public String getIs_recomm() {
        return is_recomm;
    }

    public void setIs_recomm(String is_recomm) {
        this.is_recomm = is_recomm;
    }

    public String getHonorName() {
        return honorName;
    }

    public void setHonorName(String honorName) {
        this.honorName = honorName;
    }

    public String getLvName() {
        return lvName;
    }

    public void setLvName(String lvName) {
        this.lvName = lvName;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    private List<String> photo;


    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(int is_approve) {
        this.is_approve = is_approve;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getModule_title() {
        return module_title;
    }

    public void setModule_title(String module_title) {
        this.module_title = module_title;
    }

    public int getCanVote() {
        return canVote;
    }

    public void setCanVote(int canVote) {
        this.canVote = canVote;
    }

    public String getSect_user_rank() {
        return sect_user_rank;
    }

    public void setSect_user_rank(String sect_user_rank) {
        this.sect_user_rank = sect_user_rank;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
