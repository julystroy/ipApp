package com.cartoon.bean;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import java.io.Serializable;

/**
 * Entity mapped to table "LISTENER".
 */
public class Listener implements Serializable {

    /**
     * 听书ID
     */
    private String id;
    private String approve_num;
    private String remote_connect;
    private Integer is_approve;
    private String book_id;
    private String listener_num;
    private String size;
    private String title;
    private String cover_pic;
    private Integer time_num;
    private Integer local_connect;
    private String domain;
    private String create_time;
    private String comment_num;
    /**
     * 点播节目ID
     */
    private String program_id;
    private String small_pic;
    private String collect;
    private String data;
    private String previous_id;
    private String next_id;
    /**
     * 下载状态
     */
    private Integer state;
    /**
     * 下载进度
     */
    private Integer progress;
    /**
     * 下载路径
     */
    private String path;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Listener() {
    }

    public Listener(String id) {
        this.id = id;
    }

    public Listener(String id, String approve_num, String remote_connect, Integer is_approve, String book_id, String listener_num, String size, String title, String cover_pic, Integer time_num, Integer local_connect, String domain, String create_time, String comment_num, String program_id, String small_pic, String collect, String data, String previous_id, String next_id, Integer state, Integer progress, String path) {
        this.id = id;
        this.approve_num = approve_num;
        this.remote_connect = remote_connect;
        this.is_approve = is_approve;
        this.book_id = book_id;
        this.listener_num = listener_num;
        this.size = size;
        this.title = title;
        this.cover_pic = cover_pic;
        this.time_num = time_num;
        this.local_connect = local_connect;
        this.domain = domain;
        this.create_time = create_time;
        this.comment_num = comment_num;
        this.program_id = program_id;
        this.small_pic = small_pic;
        this.collect = collect;
        this.data = data;
        this.previous_id = previous_id;
        this.next_id = next_id;
        this.state = state;
        this.progress = progress;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(String approve_num) {
        this.approve_num = approve_num;
    }

    public String getRemote_connect() {
        return remote_connect;
    }

    public void setRemote_connect(String remote_connect) {
        this.remote_connect = remote_connect;
    }

    public Integer getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(Integer is_approve) {
        this.is_approve = is_approve;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getListener_num() {
        return listener_num;
    }

    public void setListener_num(String listener_num) {
        this.listener_num = listener_num;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public Integer getTime_num() {
        return time_num;
    }

    public void setTime_num(Integer time_num) {
        this.time_num = time_num;
    }

    public Integer getLocal_connect() {
        return local_connect;
    }

    public void setLocal_connect(Integer local_connect) {
        this.local_connect = local_connect;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getSmall_pic() {
        return small_pic;
    }

    public void setSmall_pic(String small_pic) {
        this.small_pic = small_pic;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrevious_id() {
        return previous_id;
    }

    public void setPrevious_id(String previous_id) {
        this.previous_id = previous_id;
    }

    public String getNext_id() {
        return next_id;
    }

    public void setNext_id(String next_id) {
        this.next_id = next_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}