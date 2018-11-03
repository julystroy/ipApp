package com.cartton.library.download.architec;


import com.cartton.library.download.DownloadException;

/**
 * 下载Task CallBack
 * <p/>
 * Created by David on 16/5/11.
 **/
public interface DownloadResponse {

    void onStarted();

    void onConnecting();

    void onConnected(long time, long length, boolean acceptRanges);

    void onConnectFailed(DownloadException e);

    void onConnectCanceled();

    void onDownloadProgress(long finished, long length, int percent);

    void onDownloadCompleted();

    void onDownloadPaused();

    void onDownloadCanceled();

    void onDownloadFailed(DownloadException e);
}
