package com.cartoon.http;


/**
 * response 基类
 * <p/>
 * Created by David on 16/3/3.
 */
public class BaseResponse {

    /**
     * pageSize : 5
     * pageNumber : 1
     * totalRow : 2
     * totalPage : 1
     * list : [{"id":1002,"title":"标题"," collect ":"2"," cover_pic ":"http://xxx.com/1.jpg"," approve_num ":"99"," comment_num ":"99"," is_approve ":"0"," create_time ":"2015-02-01"},{"id":1001,"title":"标题"," collect ":"1"," cover_pic ":"http://xxx.com/1.jpg"," approve_num ":"99"," comment_num ":"99"," is_approve ":0," create_time ":"2015-02-01"}]
     */
    private int pageSize;
    private int pageNumber;
    private int totalRow;
    private int totalPage;

    private boolean lastPage;
    private boolean firstPage;

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

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", totalRow=" + totalRow +
                ", totalPage=" + totalPage +
                '}';
    }
}
