package com.cartoon.data;

/**
 * Created by cc on 17-3-30.
 */
public class ReportData {

    /**
     * bad_uid : 2988
     * msg : 举报成功！我们会尽快核实！
     * uid : 2973
     * core_id : 1935
     * time : 2017-02-21
     * state : 未处理
     * id : 3
     * type : 2
     * comment_level : 2
     * content : 毛
     */

    private int bad_uid;
    private String msg;
    private int    uid;
    private int    core_id;
    private String time;
    private String state;
    private int    id;
    private int    type;
    private int    comment_level;
    private String content;

    public int getBad_uid() {
        return bad_uid;
    }

    public void setBad_uid(int bad_uid) {
        this.bad_uid = bad_uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCore_id() {
        return core_id;
    }

    public void setCore_id(int core_id) {
        this.core_id = core_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getComment_level() {
        return comment_level;
    }

    public void setComment_level(int comment_level) {
        this.comment_level = comment_level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
