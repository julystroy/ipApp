package com.cartoon.module.changxian.download;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.cartoon.CartoonApp;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.statistics.Statistics;
import com.cartoon.utils.NetworkHelper;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.Util;
import com.cartton.library.utils.DebugLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.aigestudio.downloader.bizs.DLInfo;
import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.IDListener;
import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;

public class DownloadManager implements IDListener {
    private static DownloadManager sInstance;
    private static final List<DLInfo> TASK_RESUME = Collections.synchronizedList(new ArrayList<DLInfo>());

    private Context mContext;
    private String mSaveDir;

    private List<IDListener> mDListeners;

    private static final int NOTIFY_FORCEGROUND_ID = 1;
    private NotificationCompat.Builder mBuilder = null;
    private NotificationManager mNotificationManager;
    private String mTempAppName;
    private int mTempFileLength;

    private Activity mCurrentActivity;

    private DownloadManager(Context context) {
        mContext = context;
        mDListeners = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            mSaveDir = Environment.getExternalStorageDirectory() + "/XJZM/download/";
        }

        DLManager.getInstance(context).setMaxTask(1);
        if (BuildConfig.DEBUG) {
            DLManager.getInstance(context).setDebugEnable(true);
        }

        mBuilder = new NotificationCompat.Builder(CartoonApp.getInstance());
        mBuilder.setSmallIcon(R.mipmap.ic_fanren)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setOngoing(true);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        startAll();

        if (Build.VERSION.SDK_INT >= 14) {
            attachActivityCallbacks();
        }
    }

    public static void init(Context context) {
        sInstance = new DownloadManager(context);
    }

    public static void fini() {
        sInstance.cancleNotifycation();
        if (Build.VERSION.SDK_INT >= 14) {
            sInstance.detachActivityCallbacks();
        }
        sInstance = null;
    }

    public static DownloadManager getInstance() {
        return sInstance;
    }

    public String getSaveDir() {
        return mSaveDir;
    }

    public void startAll() {
        List<DLInfo> dlInfoList = DLManager.getInstance(mContext).getAllDLInfo();
        if (dlInfoList != null) {
            for (DLInfo dlInfo : dlInfoList) {
                if (dlInfo.state == DLInfo.STATE_DOWNLOADING) {
                    start(dlInfo.baseUrl);
                    break;
                }
            }
            for (DLInfo dlInfo : dlInfoList) {
                if (dlInfo.state == DLInfo.STATE_WAITING) {
                    start(dlInfo.baseUrl);
                }
            }
        }
    }

    private void start(String url) {
        start(url, "");
    }

    public void start(String url, String name) {
        start(url, name, "", false);
    }

    public void start(String url, String name, final String size) {
        start(url, name, size, true);
    }

    public void start(final String url, final String name, final String size, boolean needNetCheck) {
//        LOG.debug("start. url = {}, needNetCheck = {}", url, needNetCheck);
        if (needNetCheck) {
            NetworkHelper.getInstance().accessNetwork(mCurrentActivity, new Runnable() {
                @Override
                public void run() {
                    // 下载完整性检查
                    checkDownload(url, name);
                }
            }, null, true, !TextUtils.isEmpty(size) ? String.format(mContext.getResources().getString(R.string.tips_mobile_network2), size) : mContext.getResources().getString(R.string.tips_mobile_network));
        } else {
            // 下载完整性检查
            checkDownload(url, name);
        }

    }

    private void checkDownload(String url, String name) {
        if(url.endsWith("quick")) return;  //no quickinfo http://dl.changxianapp.com/client_apks/com.blindflugstudios.firststrike.ie.ad.free-30115.apk?quick
        // 下载完整性检查
        DLInfo dlInfo = DLManager.getInstance(mContext).getDLInfo(url);
        if (dlInfo != null && !new File(dlInfo.dirPath, dlInfo.fileName).exists()) {
            //从0开始下载
            DLManager.getInstance(mContext).dlCancel(url);
        }
        DLManager.getInstance(mContext).dlStart(url, mSaveDir, name, DownloadManager.this);
    }

    public void stop(String url) {
        DLManager.getInstance(mContext).dlStop(url);
    }

    public void stopAll() {
        List<DLInfo> dlInfoList = DLManager.getInstance(mContext).getAllDLInfo();
        if (dlInfoList != null) {
            for (DLInfo dlInfo : dlInfoList) {
                if (dlInfo.state == DLInfo.STATE_DOWNLOADING || dlInfo.state == DLInfo.STATE_WAITING) {
                    DLManager.getInstance(mContext).dlStopOnly(dlInfo.baseUrl);
                    TASK_RESUME.add(dlInfo);
                }
            }
        }
    }

    public void resumeAll() {
        for (int i = 0; i < TASK_RESUME.size(); i++) {
            start(TASK_RESUME.get(i).baseUrl);
        }
        TASK_RESUME.clear();
    }

    public synchronized void addDListener(IDListener listener) {
        if (!mDListeners.contains(listener)) {
            mDListeners.add(listener);
        }
    }

    public synchronized void removeDListener(IDListener listener) {
        if (mDListeners.contains(listener)) {
            mDListeners.remove(listener);
        }
    }

    @Override
    public synchronized void onPrepare() {
        DebugLog.d("onPrepare.");

        for (IDListener listener : mDListeners) {
            listener.onPrepare();
        }
    }

    @Override
    public synchronized void onStart(String fileName, String realUrl, int fileLength) {
        for (IDListener listener : mDListeners) {
            listener.onStart(fileName, realUrl, fileLength);
        }

        this.mTempAppName = fileName;
        if (fileName.endsWith(".apk")) {
            DownloadGame record = UserDB.getInstance().findGameDownloadByPackageName(mTempAppName.substring(0, mTempAppName.length() - 4));
            if (record != null) {
                this.mTempAppName = record.getAppName();
            }
        }

        this.mTempFileLength = fileLength;
        startNotification(fileName, fileLength, 0, 0);
    }

    private void startNotification(String appName, int fileLength, int progress, int speed) {
        Notification notification = createRunningTaskNotifycation(appName, fileLength, progress, speed);
        mNotificationManager.notify(NOTIFY_FORCEGROUND_ID, notification);
    }

    private void cancleNotifycation() {
        mNotificationManager.cancel(NOTIFY_FORCEGROUND_ID);
    }

    @Override
    public synchronized void onProgress(int progress, int speeed) {
        for (IDListener listener : mDListeners) {
            listener.onProgress(progress, speeed);
        }

        startNotification(mTempAppName, mTempFileLength, progress, speeed);
    }

    @Override
    public synchronized void onStop(int progress) {
        for (IDListener listener : mDListeners) {
            listener.onStop(progress);
        }

        Notification notification = createMessageNotifycation(mContext.getString(R.string.notify_msg_waiting));
        mNotificationManager.notify(NOTIFY_FORCEGROUND_ID, notification);
    }

    @Override
    public synchronized void onFinish(File file) {
        DebugLog.d("onFinish.");

        for (IDListener listener : mDListeners) {
            listener.onFinish(file);
        }

        if (file != null && file.exists()) {
            Util.startInstall(mContext, Uri.fromFile(file));

            String appName = file.getName();
            if (appName.endsWith(".apk")) {
                DownloadGame record = UserDB.getInstance().findGameDownloadByPackageName(appName.substring(0, appName.length() - 4));
                if (record != null) {
                    Statistics.reportDownload(mContext, record.getCxId());
                }
            }
        }

        cancleNotifycation();
    }

    @Override
    public synchronized void onError(int status, String error) {
        DebugLog.e("onError. status = " + status + ", error = " + error.toString());

        for (IDListener listener : mDListeners) {
            listener.onError(status, error);
        }

        Notification notification = createMessageNotifycation(mContext.getString(R.string.notify_msg_error));
        mNotificationManager.notify(NOTIFY_FORCEGROUND_ID, notification);
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(CartoonApp.getInstance(), DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(CartoonApp.getInstance(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    public Notification createRunningTaskNotifycation(String appName, int fileLength, int progress, int speed) {
        Notification notification = null;

        mBuilder.setContentTitle(appName);
        mBuilder.setContentText(Util.convertFileSize(speed, 2) + "/s");
        mBuilder.setProgress(fileLength,
                progress, false);
        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.contentIntent = createPendingIntent();

        return notification;
    }

    public Notification createMessageNotifycation(String contentTitle) {
        Notification notification = null;

        mBuilder.setContentTitle(contentTitle);
        mBuilder.setContentText(mContext.getString(R.string.notify_msg_content));
        mBuilder.setProgress(0,
                0, false);
        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.contentIntent = createPendingIntent();

        return notification;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void attachActivityCallbacks() {
        final Application app = (Application) mContext.getApplicationContext();
        app.registerActivityLifecycleCallbacks(mLifeCallback);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void detachActivityCallbacks(){
        final Application app = (Application) mContext.getApplicationContext();
        app.unregisterActivityLifecycleCallbacks(mLifeCallback);
    }

    private Application.ActivityLifecycleCallbacks mLifeCallback = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }
    };

}
