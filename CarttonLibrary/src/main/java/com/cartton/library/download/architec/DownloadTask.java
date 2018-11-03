package com.cartton.library.download.architec;


import com.cartton.library.download.DownloadException;

/**
 * 下载task
 * <p/>
 * Created by David on 16/5/11.
 */
public interface DownloadTask extends Runnable {

    /***
     * 下载回调
     ***/
    interface OnDownloadListener {

        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed(DownloadException de);

    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();
}
