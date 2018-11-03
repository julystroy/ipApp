package com.cartton.library.download.impl;

import com.cartton.library.download.DownloadConfiguration;
import com.cartton.library.download.DownloadException;
import com.cartton.library.download.DownloadInfo;
import com.cartton.library.download.DownloadRequest;
import com.cartton.library.download.architec.ConnectTask;
import com.cartton.library.download.architec.DownloadResponse;
import com.cartton.library.download.architec.DownloadStatus;
import com.cartton.library.download.architec.DownloadTask;
import com.cartton.library.download.architec.Downloader;
import com.cartton.library.download.db.DataBaseManager;
import com.cartton.library.download.db.ThreadInfo;
import com.cartton.library.utils.DebugLog;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class DownloaderImpl implements Downloader, ConnectTask.OnConnectListener, DownloadTask.OnDownloadListener {

    private DownloadRequest mRequest;

    private DownloadResponse mResponse;

    private Executor mExecutor;

    private DataBaseManager mDBManager;

    private String mTag;

    private DownloadConfiguration mConfig;

    private OnDownloaderDestroyedListener mListener;

    private int mStatus;

    private DownloadInfo mDownloadInfo;

    private ConnectTask mConnectTask;

    private List<DownloadTask> mDownloadTasks;

    public DownloaderImpl(DownloadRequest request, DownloadResponse response, Executor executor, DataBaseManager dbManager, String key, DownloadConfiguration config, OnDownloaderDestroyedListener listener) {
        mRequest = request;
        mResponse = response;
        mExecutor = executor;
        mDBManager = dbManager;
        mTag = key;
        mConfig = config;
        mListener = listener;

        init();
    }

    private void init() {
        mDownloadInfo = new DownloadInfo(mRequest.getTitle().toString(), mRequest.getUri(), mRequest.getFolder());
        mDownloadTasks = new LinkedList<>();
    }

    @Override
    public boolean isRunning() {
        return mStatus == DownloadStatus.STATUS_STARTED
                || mStatus == DownloadStatus.STATUS_CONNECTING
                || mStatus == DownloadStatus.STATUS_CONNECTED
                || mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public void start() {
        mStatus = DownloadStatus.STATUS_STARTED;
        mResponse.onStarted();
        connect();
    }

    @Override
    public void pause() {
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }
        for (DownloadTask task : mDownloadTasks) {
            task.pause();
        }
    }

    @Override
    public void cancel() {
        if (mConnectTask != null) {
            mConnectTask.cancel();
        }
        for (DownloadTask task : mDownloadTasks) {
            task.cancel();
        }
    }

    @Override
    public void onDestroy() {
        // trigger the onDestroy callback tell download manager
        mListener.onDestroyed(mTag, this);
    }

    @Override
    public void onConnecting() {
        mStatus = DownloadStatus.STATUS_CONNECTING;
        mResponse.onConnecting();
    }

    @Override
    public void onConnected(long time, long length, boolean isAcceptRanges) {
        mStatus = DownloadStatus.STATUS_CONNECTED;
        mResponse.onConnected(time, length, isAcceptRanges);

        mDownloadInfo.setAcceptRanges(isAcceptRanges);
        mDownloadInfo.setLength(length);
        download(length, isAcceptRanges);
    }

    @Override
    public void onConnectFailed(DownloadException de) {
        mStatus = DownloadStatus.STATUS_FAILED;
        mResponse.onConnectFailed(de);
        onDestroy();
    }

    @Override
    public void onConnectCanceled() {
        mStatus = DownloadStatus.STATUS_CANCELED;
        mResponse.onConnectCanceled();
        onDestroy();
    }

    @Override
    public void onDownloadConnecting() {
    }

    @Override
    public void onDownloadProgress(long finished, long length) {
        mStatus = DownloadStatus.STATUS_PROGRESS;
        // calculate percent
        final int percent = (int) (finished * 100 / length);
        mResponse.onDownloadProgress(finished, length, percent);
    }

    @Override
    public void onDownloadCompleted() {
        if (isAllComplete()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_COMPLETED;
            mResponse.onDownloadCompleted();
            onDestroy();
        }
    }

    @Override
    public void onDownloadPaused() {
        if (isAllPaused()) {
            mStatus = DownloadStatus.STATUS_PAUSED;
            mResponse.onDownloadPaused();
            onDestroy();
        }
    }

    @Override
    public void onDownloadCanceled() {
        if (isAllCanceled()) {
            deleteFromDB();
            mStatus = DownloadStatus.STATUS_CANCELED;
            mResponse.onDownloadCanceled();
            onDestroy();
        }
    }

    @Override
    public void onDownloadFailed(DownloadException de) {
        if (isAllFailed()) {
            mStatus = DownloadStatus.STATUS_FAILED;
            mResponse.onDownloadFailed(de);
            onDestroy();
        }
    }

    private void connect() {
        mConnectTask = new ConnectTaskImpl(mRequest.getUri(), this);
        mExecutor.execute(mConnectTask);
    }

    private void download(long length, boolean acceptRanges) {
        initDownloadTasks(length, acceptRanges);
        // start tasks
        for (DownloadTask downloadTask : mDownloadTasks) {
            mExecutor.execute(downloadTask);
        }
    }

    /***
     * 根据server是否支持断点续传，构建下载任务
     ***/
    private void initDownloadTasks(long length, boolean acceptRanges) {
        DebugLog.i("...length=" + length + "...acceptRanges==" + acceptRanges);
        mDownloadTasks.clear();
        if (acceptRanges) {
            List<ThreadInfo> threadInfos = getMultiThreadInfos(length);
            // init finished
            int finished = 0;
            for (ThreadInfo threadInfo : threadInfos) {
                finished += threadInfo.getFinished();
            }
            mDownloadInfo.setFinished(finished);
            for (ThreadInfo info : threadInfos) {
                mDownloadTasks.add(new MultiDownloadTask(mDownloadInfo, info, mDBManager, this));
            }
        } else {
            ThreadInfo info = getSingleThreadInfo();
            mDownloadTasks.add(new SingleDownloadTask(mDownloadInfo, info, this));
        }
    }

    /***
     * 多线程并发
     ***/
    private List<ThreadInfo> getMultiThreadInfos(long length) {
        // init threadInfo from db
        final List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(mTag);
        if (threadInfos.isEmpty()) {
            final int threadNum = mConfig.getThreadNum();
            for (int i = 0; i < threadNum; i++) {
                // calculate average
                final long average = length / threadNum;
                final long start = average * i;
                final long end;
                if (i == threadNum - 1) {
                    end = length;
                } else {
                    end = start + average - 1;
                }
                ThreadInfo threadInfo = new ThreadInfo(i, mTag, mRequest.getUri(), start, end, 0);
                threadInfos.add(threadInfo);
            }
        }
        return threadInfos;
    }

    private ThreadInfo getSingleThreadInfo() {
        return new ThreadInfo(0, mTag, mRequest.getUri(), 0);
    }

    private boolean isAllComplete() {
        boolean allFinished = true;
        for (DownloadTask task : mDownloadTasks) {
            if (!task.isComplete()) {
                allFinished = false;
                break;
            }
        }
        return allFinished;
    }

    private boolean isAllFailed() {
        boolean allFailed = true;
        for (DownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allFailed = false;
                break;
            }
        }
        return allFailed;
    }

    private boolean isAllPaused() {
        boolean allPaused = true;
        for (DownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allPaused = false;
                break;
            }
        }
        return allPaused;
    }

    private boolean isAllCanceled() {
        boolean allCanceled = true;
        for (DownloadTask task : mDownloadTasks) {
            if (task.isDownloading()) {
                allCanceled = false;
                break;
            }
        }
        return allCanceled;
    }

    private void deleteFromDB() {
        mDBManager.delete(mTag);
    }
}
