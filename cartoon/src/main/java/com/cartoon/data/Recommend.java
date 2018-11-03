package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-2-23.
 */
public class Recommend implements Parcelable{

    /**
     "comment_num": 4, //评论数
     "module_type": 0, //所属模块类型（0=活动 1=漫画...）
     "clause_id": 12, //条目的id（活动列表中id=12的那个）
     "cover_pic": "/upload/image/20170216191013_687.png", //封面
     "is_del": null, //是否删除（目前不用）
     "id": 2, //在推荐模块中的id
     "module_name": "activity",//所属模块的名称
     "moduleName": "activity",//所属模块的名称
     "time": "1天前", //时间
     "clause_title": "投票：你最喜欢的游戏?", //标题
     "is_show": null, //是否发布（目前不用）
     "approve_num": 2//评论数
     */

    private int comment_num;
    private int    module_type;
    private int    clause_id;
    private String cover_pic;
    private Object is_del;
    private int    id;
    private String moduleName;
    private String module_name;
    private String time;
    private String clause_title;
    private Object is_show;
    private int    approve_num;
    private int    hasVote;

    public Recommend(){}

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getModule_type() {
        return module_type;
    }

    public void setModule_type(int module_type) {
        this.module_type = module_type;
    }

    public int getClause_id() {
        return clause_id;
    }

    public void setClause_id(int clause_id) {
        this.clause_id = clause_id;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public Object getIs_del() {
        return is_del;
    }

    public void setIs_del(Object is_del) {
        this.is_del = is_del;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClause_title() {
        return clause_title;
    }

    public void setClause_title(String clause_title) {
        this.clause_title = clause_title;
    }

    public Object getIs_show() {
        return is_show;
    }

    public void setIs_show(Object is_show) {
        this.is_show = is_show;
    }

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
    }

    public int getHasVote() {
        return hasVote;
    }

    public void setHasVote(int hasVote) {
        this.hasVote = hasVote;
    }

    protected Recommend(Parcel in) {
        comment_num = in.readInt();
        module_type = in.readInt();
        clause_id = in.readInt();
        cover_pic = in.readString();
        id = in.readInt();
        moduleName = in.readString();
        module_name = in.readString();
        time = in.readString();
        clause_title = in.readString();
        approve_num = in.readInt();
        hasVote = in.readInt();
    }

    public static final Creator<Recommend> CREATOR = new Creator<Recommend>() {
        @Override
        public Recommend createFromParcel(Parcel in) {
            return new Recommend(in);
        }

        @Override
        public Recommend[] newArray(int size) {
            return new Recommend[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(comment_num);
        dest.writeInt(module_type);
        dest.writeInt(clause_id);
        dest.writeString(cover_pic);
        dest.writeInt(id);
        dest.writeString(moduleName);
        dest.writeString(module_name);
        dest.writeString(time);
        dest.writeString(clause_title);
        dest.writeInt(approve_num);
        dest.writeInt(hasVote);
    }
}
