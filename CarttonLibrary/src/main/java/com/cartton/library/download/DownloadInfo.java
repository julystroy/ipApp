package com.cartton.library.download;

import java.io.File;

public class DownloadInfo {
    /***
     * 文件名
     ***/
    private String name;
    /***
     * 下载地址
     ***/
    private String uri;
    /***
     * 下载路径
     ***/
    private File dir;
    /***
     * 百分比
     ***/
    private int progress;
    /***
     * 文件长度
     ***/
    private long length;
    /***
     * 完成度
     ***/
    private long finished;
    /***
     * 是否支持断点续传
     ***/
    private boolean acceptRanges;
    /***
     * 下载状态
     ***/
    private int status;


    public DownloadInfo() {
    }

    public DownloadInfo(String name, String uri, File dir) {
        this.name = name;
        this.uri = uri;
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

}
