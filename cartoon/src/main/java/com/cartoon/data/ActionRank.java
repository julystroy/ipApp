package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-8-24.
 */
public class ActionRank implements Parcelable{

    /**
     * words_account : 1
     * create_time : 2017-08-09 19:39:59
     * end_time : 2017-08-09 19:39:59
     * avatar : /upload/avatar/user/2017-08/04/336_1501838449604.jpg
     * commentCount : 0
     * uid : 336
     * start_time : 2017-08-09 19:39:59
     * essay_id : 2
     * createTime : 08-09
     *
     * activity_id : 0
     * nickname : 凡人哥
     * essay_content : asdfdsfsdfass
     * rank : 2
     * votes : 10
     * id : 336
     * essay_title : asfasdfds
     * share_num : null
     * status : 1
     */

    private int words_account;
    private String create_time;
    private String end_time;
    private String avatar;
    private int    commentCount;
    private int    uid;
    private String start_time;
    private int    essay_id;
    private String createTime;
    private String activity_id;
    private String nickname;
    private String essay_content;
    private int rank;
    private String votes;
    private String id;
    private String essay_title;
    private String share_num;
    private String status;

    public ActionRank() {
    }

    protected ActionRank(Parcel in) {
        words_account = in.readInt();
        create_time = in.readString();
        end_time = in.readString();
        avatar = in.readString();
        commentCount = in.readInt();
        uid = in.readInt();
        start_time = in.readString();
        essay_id = in.readInt();
        createTime = in.readString();
        activity_id = in.readString();
        nickname = in.readString();
        essay_content = in.readString();
        rank = in.readInt();
        votes = in.readString();
        id = in.readString();
        essay_title = in.readString();
        share_num = in.readString();
        status = in.readString();
    }

    public static final Creator<ActionRank> CREATOR = new Creator<ActionRank>() {
        @Override
        public ActionRank createFromParcel(Parcel in) {
            return new ActionRank(in);
        }

        @Override
        public ActionRank[] newArray(int size) {
            return new ActionRank[size];
        }
    };

    public int getWords_account() {
        return words_account;
    }

    public void setWords_account(int words_account) {
        this.words_account = words_account;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public int getEssay_id() {
        return essay_id;
    }

    public void setEssay_id(int essay_id) {
        this.essay_id = essay_id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEssay_content() {
        return essay_content;
    }

    public void setEssay_content(String essay_content) {
        this.essay_content = essay_content;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEssay_title() {
        return essay_title;
    }

    public void setEssay_title(String essay_title) {
        this.essay_title = essay_title;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ActionRank{" +
                "words_account=" + words_account +
                ", create_time='" + create_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", avatar='" + avatar + '\'' +
                ", commentCount=" + commentCount +
                ", uid=" + uid +
                ", start_time='" + start_time + '\'' +
                ", essay_id=" + essay_id +
                ", createTime='" + createTime + '\'' +
                ", activity_id='" + activity_id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", essay_content='" + essay_content + '\'' +
                ", rank='" + rank + '\'' +
                ", votes='" + votes + '\'' +
                ", id='" + id + '\'' +
                ", essay_title='" + essay_title + '\'' +
                ", share_num='" + share_num + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(words_account);
        dest.writeString(create_time);
        dest.writeString(end_time);
        dest.writeString(avatar);
        dest.writeInt(commentCount);
        dest.writeInt(uid);
        dest.writeString(start_time);
        dest.writeInt(essay_id);
        dest.writeString(createTime);
        dest.writeString(activity_id);
        dest.writeString(nickname);
        dest.writeString(essay_content);
        dest.writeInt(rank);
        dest.writeString(votes);
        dest.writeString(id);
        dest.writeString(essay_title);
        dest.writeString(share_num);
        dest.writeString(status);
    }
}
