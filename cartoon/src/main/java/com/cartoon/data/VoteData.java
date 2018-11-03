package com.cartoon.data;

import java.io.Serializable;

/**
 * Created by cc on 17-2-16.
 */
public class VoteData implements Serializable{
    /**
     * option_pic : /upload/image/20170215160958_791.png
     * end_time : 2017-02-26 00:00:00
     * title_id : 1
     * vote_id : 1
     * sort : 1
     * bonus_point : 10
     * stone : 1
     * total_votes : 0
     * vote_title : 你最喜欢谁呢？
     * activity_id : 8
     * option_id : 1
     * maxchoice : 1
     * option_content : 座敷童子
     */

    private String option_pic;
    private String end_time;
    private int    title_id;
    private int    vote_id;
    private int    sort;
    private int    bonus_point;
    private int    stone;
    private int    total_votes;
    private String vote_title;
    private int    activity_id;
    private int    option_id;
    private int    maxchoice;
    private String option_content;

    private String  type;  //1 投票   2   福利
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption_pic() {
        return option_pic;
    }

    public void setOption_pic(String option_pic) {
        this.option_pic = option_pic;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getBonus_point() {
        return bonus_point;
    }

    public void setBonus_point(int bonus_point) {
        this.bonus_point = bonus_point;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getTotal_votes() {
        return total_votes;
    }

    public void setTotal_votes(int total_votes) {
        this.total_votes = total_votes;
    }

    public String getVote_title() {
        return vote_title;
    }

    public void setVote_title(String vote_title) {
        this.vote_title = vote_title;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public int getMaxchoice() {
        return maxchoice;
    }

    public void setMaxchoice(int maxchoice) {
        this.maxchoice = maxchoice;
    }

    public String getOption_content() {
        return option_content;
    }

    public void setOption_content(String option_content) {
        this.option_content = option_content;
    }

    @Override
    public String toString() {
        return "VoteData{" +
                "option_pic='" + option_pic + '\'' +
                ", end_time='" + end_time + '\'' +
                ", title_id=" + title_id +
                ", vote_id=" + vote_id +
                ", sort=" + sort +
                ", bonus_point=" + bonus_point +
                ", stone=" + stone +
                ", total_votes=" + total_votes +
                ", vote_title='" + vote_title + '\'' +
                ", activity_id=" + activity_id +
                ", option_id=" + option_id +
                ", maxchoice=" + maxchoice +
                ", option_content='" + option_content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
