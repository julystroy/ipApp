package com.cartoon.data.response;

import com.cartoon.data.ComicsImg;

import java.util.List;

/**
 * 漫画对象
 * <p/>
 * Created by David on 16/7/2.
 */
public class ComicsImgResp {
    /**
     * "previous_id":1,   //上一集ID 如可能为空
     * "next_id":3,	//下一集ID如可能为空
     * <p/>
     * "approve_num":1,"is_approve":0,"previous_id":4,"comment_num":1,"
     */
    private String previous_id;
    private String next_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int approve_num;
    private int is_approve;
    private int comment_num;
    private String collect;

    private List<ComicsImg> list;

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

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public List<ComicsImg> getList() {
        return list;
    }

    public void setList(List<ComicsImg> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ComicsImgResp{" +
                "previous_id='" + previous_id + '\'' +
                ", next_id='" + next_id + '\'' +
                ", approve_num=" + approve_num +
                ", is_approve=" + is_approve +
                ", comment_num=" + comment_num +
                ", collect='" + collect + '\'' +
                ", list=" + list +
                '}';
    }
}
