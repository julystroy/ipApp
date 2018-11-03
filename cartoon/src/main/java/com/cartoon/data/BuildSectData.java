package com.cartoon.data;

/**
 * Created by cc on 17-10-23.
 */
public class BuildSectData {
    /**
     * is_show : 1
     * flag : true
     * sect_level : 1
     * sect_name : 行云流水
     * sect_id : 49
     * user_num : 20
     * last_modified_time : null
     * create_time : 2017-10-23 17:41:01
     * contribution : 0
     * create_user_id : 336
     * sect_status : 0
     * msg : 恭喜道友成为行云流水宗门掌门
     */

    private int is_show;
    private boolean flag;
    private int     sect_level;
    private String  sect_name;
    private int     sect_id;
    private int     user_num;
    private Object  last_modified_time;
    private String  create_time;
    private int     contribution;
    private int     create_user_id;
    private int     sect_status;
    private String  msg;

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getSect_level() {
        return sect_level;
    }

    public void setSect_level(int sect_level) {
        this.sect_level = sect_level;
    }

    public String getSect_name() {
        return sect_name;
    }

    public void setSect_name(String sect_name) {
        this.sect_name = sect_name;
    }

    public int getSect_id() {
        return sect_id;
    }

    public void setSect_id(int sect_id) {
        this.sect_id = sect_id;
    }

    public int getUser_num() {
        return user_num;
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public Object getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(Object last_modified_time) {
        this.last_modified_time = last_modified_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public int getSect_status() {
        return sect_status;
    }

    public void setSect_status(int sect_status) {
        this.sect_status = sect_status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
