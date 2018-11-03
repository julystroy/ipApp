package com.cartton.library.download.architec;


import com.cartton.library.download.CallBack;
import com.cartton.library.download.DownloadException;

/**
 * Created by David on 16/5/11.
 **/
public class DownloadStatus {

    public static final int STATUS_STARTED = 101;
    public static final int STATUS_CONNECTING = 102;
    public static final int STATUS_CONNECTED = 103;
    public static final int STATUS_PROGRESS = 104;
    public static final int STATUS_COMPLETED = 105;
    public static final int STATUS_PAUSED = 106;
    public static final int STATUS_CANCELED = 107;
    public static final int STATUS_FAILED = 108;

    /***
     * 状态
     ***/
    private int status;
    /***
     * 连接时长
     ***/
    private long time;
    /***
     * 文件大小
     ***/
    private long length;
    /***
     * 完成度
     ***/
    private long finished;
    /***
     * 百分比
     ***/
    private int percent;
    /***
     * 是否断点续传
     ***/
    private boolean acceptRanges;
    /***
     * 异常
     ***/
    private DownloadException exception;
    /***
     * 回调
     ***/
    private CallBack callBack;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(DownloadException exception) {
        this.exception = exception;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
