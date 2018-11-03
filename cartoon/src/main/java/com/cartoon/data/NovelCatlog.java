package com.cartoon.data;

import java.util.List;

/**
 * Created by cc on 17-11-7.
 */
public class NovelCatlog {


    /**
     * pageSize : 10
     * pageNumber : 1
     * list : [{"id":125,"title":"凡人修仙之仙界篇 第一章狐女"},{"id":124,"title":"ヾ(◍°∇°◍)ﾉﾞ"},{"id":123,"title":"小鸭子纪年"},{"id":122,"title":"天空之城"},{"id":120,"title":"天王盖地骨"},{"id":113,"title":"今天"},{"id":112,"title":"1234test"},{"id":109,"title":"测试视频"},{"id":108,"title":"hahaha "},{"id":106,"title":"322test"}]
     * firstPage : true
     * lastPage : false
     * totalRow : 77
     * totalPage : 8
     */

    private int pageSize;
    private int            pageNumber;
    private boolean        firstPage;
    private boolean        lastPage;
    private int            totalRow;
    private int            totalPage;
    private List<NovelData> list;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
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

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<NovelData> getList() {
        return list;
    }

    public void setList(List<NovelData> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "NovelCatlog{" +
                "pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", firstPage=" + firstPage +
                ", lastPage=" + lastPage +
                ", totalRow=" + totalRow +
                ", totalPage=" + totalPage +
                ", list=" + list +
                '}';
    }
}
