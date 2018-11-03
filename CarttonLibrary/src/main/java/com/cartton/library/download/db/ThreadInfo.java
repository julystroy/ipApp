package com.cartton.library.download.db;

/**
 * 下载task实体
 * Created by David on 16/5/10.
 **/
public class ThreadInfo {

    private int id;
    /***
     * 下载标识
     ***/
    private String tag;
    /***
     * 下载地址
     ***/
    private String uri;
    /***
     * 开始读取位置
     ***/
    private long start;
    /***
     * 读取结束位置
     ***/
    private long end;
    /***
     * 完成度
     ***/
    private long finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String tag, String uri, long finished) {
        this.id = id;
        this.tag = tag;
        this.uri = uri;
        this.finished = finished;
    }

    public ThreadInfo(int id, String tag, String uri, long start, long end, long finished) {
        this.id = id;
        this.tag = tag;
        this.uri = uri;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", uri='" + uri + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                '}';
    }
}
