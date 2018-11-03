package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-12-26.
 */
public class TrendsData implements Parcelable{

    /**
     * comment_num : 0
     * show_time : 2017-12-20 12:02:58
     * is_recom : 0
     * module_type : 11
     * cover_pic : /upload/image/20171220112952_425.jpg
     * is_show : 1
     * is_video : null
     * recom_time : null
     * clause_id : 183
     * is_del : 0
     * is_read_chapter : false
     * id : 525
     * module_name : activity
     * time : 23小时前
     * clause_title : 征文 春天!!!
     * approve_num : 0
     */

    private int comment_num;
    private String  show_time;
    private int     is_recom;
    private int     module_type;
    private String  cover_pic;
    private int     is_show;
    private boolean  is_video;
    private String  recom_time;
    private int     clause_id;
    private int     is_del;
    private boolean is_read_chapter;
    private int     id;
    private String  module_name;
    private String  moduleName;
    private String  time;
    private String  clause_title;
    private int     approve_num;

    public TrendsData() {
    }


    protected TrendsData(Parcel in) {
        comment_num = in.readInt();
        show_time = in.readString();
        is_recom = in.readInt();
        module_type = in.readInt();
        cover_pic = in.readString();
        is_show = in.readInt();
        is_video = in.readByte() != 0;
        recom_time = in.readString();
        clause_id = in.readInt();
        is_del = in.readInt();
        is_read_chapter = in.readByte() != 0;
        id = in.readInt();
        module_name = in.readString();
        moduleName = in.readString();
        time = in.readString();
        clause_title = in.readString();
        approve_num = in.readInt();
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    public int getIs_recom() {
        return is_recom;
    }

    public void setIs_recom(int is_recom) {
        this.is_recom = is_recom;
    }

    public int getModule_type() {
        return module_type;
    }

    public void setModule_type(int module_type) {
        this.module_type = module_type;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public boolean is_video() {
        return is_video;
    }

    public void setIs_video(boolean is_video) {
        this.is_video = is_video;
    }

    public String getRecom_time() {
        return recom_time;
    }

    public void setRecom_time(String recom_time) {
        this.recom_time = recom_time;
    }

    public int getClause_id() {
        return clause_id;
    }

    public void setClause_id(int clause_id) {
        this.clause_id = clause_id;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public boolean is_read_chapter() {
        return is_read_chapter;
    }

    public void setIs_read_chapter(boolean is_read_chapter) {
        this.is_read_chapter = is_read_chapter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public String toString() {
        return "TrendsData{" +
                "comment_num=" + comment_num +
                ", show_time='" + show_time + '\'' +
                ", is_recom=" + is_recom +
                ", module_type=" + module_type +
                ", cover_pic='" + cover_pic + '\'' +
                ", is_show=" + is_show +
                ", is_video=" + is_video +
                ", recom_time='" + recom_time + '\'' +
                ", clause_id=" + clause_id +
                ", is_del=" + is_del +
                ", is_read_chapter=" + is_read_chapter +
                ", id=" + id +
                ", module_name='" + module_name + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", time='" + time + '\'' +
                ", clause_title='" + clause_title + '\'' +
                ", approve_num=" + approve_num +
                '}';
    }

    public static final Creator<TrendsData> CREATOR = new Creator<TrendsData>() {
        @Override
        public TrendsData createFromParcel(Parcel in) {
            return new TrendsData(in);
        }

        @Override
        public TrendsData[] newArray(int size) {
            return new TrendsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(comment_num);
        dest.writeString(show_time);
        dest.writeInt(is_recom);
        dest.writeInt(module_type);
        dest.writeString(cover_pic);
        dest.writeInt(is_show);
        dest.writeByte((byte) (is_video ? 1 : 0));
        dest.writeString(recom_time);
        dest.writeInt(clause_id);
        dest.writeInt(is_del);
        dest.writeByte((byte) (is_read_chapter ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(module_name);
        dest.writeString(moduleName);
        dest.writeString(time);
        dest.writeString(clause_title);
        dest.writeInt(approve_num);
    }
}
