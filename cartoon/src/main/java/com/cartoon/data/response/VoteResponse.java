package com.cartoon.data.response;

import com.cartoon.data.VoteData;

import java.util.List;

/**
 * Created by cc on 17-2-16.
 */
public class VoteResponse {

    /**
     * voted : true
     * list : [{"option_pic":"/upload/image/20170215160958_791.png",
     * "end_time":"2017-02-26 00:00:00",
     * "title_id":1,"vote_id":1,"sort":1,
     * "bonus_point":10,"stone":1,"total_votes":0,
     * "vote_title":"你最喜欢谁呢？",
     * "activity_id":8,"option_id":1,"maxchoice":1,
     * "option_content":"座敷童子"},
     * {"option_pic":"/upload/image/20170215161138_202.jpg",
     * "end_time":"2017-02-26 00:00:00","title_id":1,
     * "vote_id":1,"sort":2,"bonus_point":10,"stone":1,
     * "total_votes":0,"vote_title":"你最喜欢谁呢？",
     * "activity_id":8,"option_id":2,"maxchoice":1,
     * "option_content":"鬼使黑"},
     * {"option_pic":"/upload/image/20170215161202_421.png",
     * "end_time":"2017-02-26 00:00:00","title_id":1,"vote_id":1,"sort":3,
     * "bonus_point":10,"stone":1,"total_votes":0,"vote_title":"你最喜欢谁呢？",
     * "activity_id":8,"option_id":3,"maxchoice":1,"option_content":"我家大狗子"},
     * {"option_pic":"/upload/image/20170215161221_741.jpg","end_time":"2017-02-26 00:00:00",
     * "title_id":1,"vote_id":1,"sort":4,"bonus_point":10,"stone":1,"total_votes":0,"vote_title":"你最喜欢谁呢？",
     * "activity_id":8,"option_id":4,"maxchoice":1,"option_content":"鸟姐"}]
     *
     * "msg":"恭喜答对！+10经验 +1灵石","option_voted":4
     */

    private boolean        voted;
    private List<VoteData> list;
    private String msg;
    private int option_voted;


    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public List<VoteData> getList() {
        return list;
    }

    public void setList(List<VoteData> list) {
        this.list = list;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOption_voted() {
        return option_voted;
    }

    public void setOption_voted(int option_voted) {
        this.option_voted = option_voted;
    }

    @Override
    public String toString() {
        return "VoteResponse{" +
                "voted=" + voted +
                ", list=" + list +
                ", msg='" + msg + '\'' +
                ", option_voted=" + option_voted +
                '}';
    }
}
