package com.cartoon.data.response;

import com.cartoon.data.ActionRank;

import java.util.List;

/**
 * Created by cc on 17-8-24.
 */
public class ActionRankRes {

    /**
     * totalRow : 3
     * pageNumber : 1
     * firstPage : true
     * lastPage : true
     * totalPage : 1
     * pageSize : 10
     * myVote : 0
     * rank : 1
     *
     *   "canVote": -1,//排行榜显示是否能投票  -1：已经投过3次，不能投票 0=可以投票
     "myEssayId": 3, //我写的文章的id
     endTime  :2017-09-23 22；22 ：22活动结束时间

     */

    private int              totalRow;
    private int              pageNumber;
    private boolean          firstPage;
    private boolean          lastPage;
    private int              totalPage;
    private int              pageSize;
    private int              myVote;
    private int              canVote;
    private int              myEssayId;
    private int              rank;
    private List<ActionRank> list;
    private String endTime;

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMyVote() {
        return myVote;
    }

    public void setMyVote(int myVote) {
        this.myVote = myVote;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<ActionRank> getList() {
        return list;
    }

    public void setList(List<ActionRank> list) {
        this.list = list;
    }

    public int getMyEssayId() {
        return myEssayId;
    }

    public void setMyEssayId(int myEssayId) {
        this.myEssayId = myEssayId;
    }

    public int getCanVote() {
        return canVote;
    }

    public void setCanVote(int canVote) {
        this.canVote = canVote;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "ActionRankRes{" +
                "totalRow=" + totalRow +
                ", pageNumber=" + pageNumber +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", totalPage=" + totalPage +
                ", pageSize=" + pageSize +
                ", myVote=" + myVote +
                ", canVote=" + canVote +
                ", myEssayId=" + myEssayId +
                ", rank=" + rank +
                ", list=" + list +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
