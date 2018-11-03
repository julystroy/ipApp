package com.cartoon.data.response;

import com.cartoon.data.MineSectData;

import java.util.List;

/**
 * Created by cc on 17-10-17.
 */
public class MineSectRes {

    /**
     * sectionNum : 4/20
     * totalRow : 4
     * boss : true
     * pageNumber : 1
     * lastPage : true
     * totalPage : 1
     * pageSize : 10
     * startLevel : 1
     * title : 一级宗门：Java
     * endLevel : 2
     * firstPage : true
     * progressBar : 0/1000
     * myRankId  ;1
     * list : [{"user_status":0,"rank_id":1,"rank_name":"掌门","last_modified_time":"2017-09-22 17:02:00","create_time":"2017-09-22 17:02:00","dailyContribution":"null","isMyself":"false","contribution":0,"user_id":38,"sect_id":13,"nickname":"黑冰","lvName":"筑基初期","user_sect_id":11}]
     */

    private String sectionNum;
    private int            totalRow;
    private boolean        boss;
    private int            pageNumber;
    private boolean        lastPage;
    private int            totalPage;
    private int            pageSize;
    private int            myRankId;
    private String         startLevel;
    private String         title;
    private String         endLevel;
    private boolean        firstPage;
    private String         progressBar;
    private List<MineSectData> list;
    private int start;
    private int end;

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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

    public String getStartLevel() {
        return startLevel;
    }

    public void setStartLevel(String startLevel) {
        this.startLevel = startLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndLevel() {
        return endLevel;
    }

    public void setEndLevel(String endLevel) {
        this.endLevel = endLevel;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public String getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(String progressBar) {
        this.progressBar = progressBar;
    }

    public List<MineSectData> getList() {
        return list;
    }

    public void setList(List<MineSectData> list) {
        this.list = list;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getMyRankId() {
        return myRankId;
    }

    public void setMyRankId(int myRankId) {
        this.myRankId = myRankId;
    }

    @Override
    public String toString() {
        return "MineSectRes{" +
                "sectionNum='" + sectionNum + '\'' +
                ", totalRow=" + totalRow +
                ", boss=" + boss +
                ", pageNumber=" + pageNumber +
                ", lastPage=" + lastPage +
                ", totalPage=" + totalPage +
                ", pageSize=" + pageSize +
                ", startLevel='" + startLevel + '\'' +
                ", title='" + title + '\'' +
                ", endLevel='" + endLevel + '\'' +
                ", firstPage=" + firstPage +
                ", progressBar='" + progressBar + '\'' +
                ", list=" + list +
                ", start=" + start +
                ", end=" + end +
                ", myRankId=" + myRankId +
                '}';
    }
}
