package com.cartton.library.download.architec;

/**
 * 下载Controller
 * <p/>
 * Created by David on 16/5/11.
 **/
public interface Downloader {

    /***
     * 当下载取消，通知download manager
     ***/
    public interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, Downloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

}
