package com.cartton.library.download;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;


import com.cartton.library.download.architec.DownloadResponse;
import com.cartton.library.download.architec.DownloadStatusDelivery;
import com.cartton.library.download.architec.Downloader;
import com.cartton.library.download.db.DataBaseManager;
import com.cartton.library.download.db.ThreadInfo;
import com.cartton.library.download.impl.DownloadResponseImpl;
import com.cartton.library.download.impl.DownloadStatusDeliveryImpl;
import com.cartton.library.download.impl.DownloaderImpl;
import com.cartton.library.utils.DebugLog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by David on 16/5/10.
 * <p>
 * Sample：
 * 1、DownloadManager mDownLoadManager = DownloadManager.getInstance();
 * <p>
 * 2、DownloadRequest request = new DownloadRequest.Builder()
 * .setTitle("update.zip.tar") 存放的文件名
 * .setUri(otaPackage.getCdnUrl())  下载地址
 * .setMd5(otaPackage.getFilemd5())  md5.
 * .setDescription(otaPackage.getNotes()) 描述
 * .setFolder(new File(OTA_PACKAGE_ROOT))  下载目录
 * .setScannable(false).build();  暂时传入false 即可
 * <p>
 * 3、mDownLoadManager.download(request, Constants.DOWNLOAD_TAG_OTA_PACKAGE, this);
 **/
public class DownloadManager extends Binder implements Downloader.OnDownloaderDestroyedListener {

    /***
     * DB 存储下载任务
     ***/
    private DataBaseManager mDBManager;
    /***
     * 下载队列
     ***/
    private Map<String, Downloader> mDownloaderMap;
    /***
     * 配置信息
     ***/
    private DownloadConfiguration mConfig;
    /***
     * 并发
     ***/
    private ExecutorService mExecutorService;
    /***
     * DownloadStatus推送器
     ***/
    private DownloadStatusDelivery mDelivery;

    private static DownloadManager sDownloadManager;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                sDownloadManager = new DownloadManager();
            }
        }
        return sDownloadManager;
    }

    private DownloadManager() {
        mDownloaderMap = new LinkedHashMap<String, Downloader>();
    }

    public void init(Context context) {
        init(context, new DownloadConfiguration());
    }

    public void init(Context context, DownloadConfiguration config) {
        DebugLog.i("...init...");
        if (config.getThreadNum() > config.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread num must < max thread num");
        }
        mConfig = config;
        mDBManager = DataBaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(mConfig.getMaxThreadNum());
        mDelivery = new DownloadStatusDeliveryImpl(new Handler(Looper.getMainLooper()));
    }

    @Override
    public void onDestroyed(String key, Downloader downloader) {
        DebugLog.i("...key=" + key);
        if (mDownloaderMap.containsKey(key)) {
            mDownloaderMap.remove(key);
        }
    }

    /**
     * 下载
     *
     * @param request  下载请求
     * @param tag      下载标记， 作为再次查询下载的id.
     * @param callBack 下载回调
     */
    public void download(DownloadRequest request, String tag, CallBack callBack) {
        DebugLog.i("...request=" + request);
        if (check(tag)) {
            final String key = createKey(tag);
            DownloadResponse response = new DownloadResponseImpl(mDelivery, callBack);
            Downloader downloader = new DownloaderImpl(request, response, mExecutorService, mDBManager, key, mConfig, this);
            mDownloaderMap.put(key, downloader);
            downloader.start();
        }
    }

    public void pause(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
            mDownloaderMap.remove(key);
        }
    }

    public void cancel(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
            }
            mDownloaderMap.remove(key);
        }
    }

    public void pauseAll() {
        for (Downloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
        }
    }

    public void cancelAll() {
        for (Downloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.cancel();
                }
            }
        }
    }

    public void remove(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            downloader.cancel();
        }

        mDBManager.delete(tag);
    }


    public DownloadInfo getDownloadProgress(String tag) {
        String key = createKey(tag);
        List<ThreadInfo> infos = mDBManager.getThreadInfos(key);
        DownloadInfo downloadInfo = null;
        if (!infos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : infos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new DownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    /***
     * 是否可以添加tag的下载任务
     ***/
    public boolean check(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            Downloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    DebugLog.w("Task has been started!");
                    return false;
                } else {
                    throw new IllegalStateException("Downloader instance with same tag has not been destroyed!");
                }
            }
        }
        return true;
    }

    /***
     * 将tag作为下载任务的标识
     ***/
    private static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }
}
