package com.cartton.library.download.architec;


import com.cartton.library.download.DownloadException;

/**
 * 连接Task  http/https server.
 * <p/>
 * Created by David on 16/5/11.
 **/
public interface ConnectTask extends Runnable {
    /***
     * 连接Task的回调
     ***/
    public interface OnConnectListener {
        /***
         * 正在连接
         ***/
        void onConnecting();

        /***
         * 已连接
         *
         * @param time           连接时间
         * @param length         文件长度
         * @param isAcceptRanges 支持断点续传
         ***/
        void onConnected(long time, long length, boolean isAcceptRanges);

        /***
         * 连接已取消
         ***/
        void onConnectCanceled();

        /***
         * 连接失败
         *
         * @param exception 异常
         ***/
        void onConnectFailed(DownloadException exception);
    }

    /***
     * 取消连接
     ***/
    void cancel();

    /***
     * 是否正在连接
     ***/
    boolean isConnecting();

    /***
     * 是否已连接
     ***/
    boolean isConnected();

    /***
     * 是否已取消
     ***/
    boolean isCanceled();

    /***
     * 是否连接失败
     ***/
    boolean isFailed();
}
