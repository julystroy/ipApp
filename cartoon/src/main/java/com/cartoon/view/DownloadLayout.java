package com.cartoon.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cartoon.CartoonApp;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.module.changxian.download.DownloadManager;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.Util;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;

import java.io.File;
import java.util.Locale;

import cn.aigestudio.downloader.bizs.DLInfo;
import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.IDListener;
import cn.com.xuanjiezhimen.R;

public abstract class DownloadLayout extends RelativeLayout implements IDListener {
    private Handler mHandler;
    private String mDownloadUrl;
    private DownloadGame mDownloadRecord;
    private DLInfo mDLInfo;

    public DownloadLayout(Context context) {
        this(context, null);
    }

    public DownloadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        DownloadManager.getInstance().removeDListener(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    protected void init() {
        mHandler = new Handler();
    }

    public void setDownloadInfo(DownloadGame record) {
        DownloadManager.getInstance().removeDListener(this);
        if (record == null || TextUtils.isEmpty(record.getDownloadUrl())) {
            update();
            return;
        }

        mDownloadRecord = record;
        mDownloadUrl = record.getDownloadUrl();
        update();
        if (mDLInfo != null && mDLInfo.state < DLInfo.STATE_COMPLETED) {
            DownloadManager.getInstance().addDListener(this);
        }
    }

    public DownloadGame getDownloadRecord() {
        return mDownloadRecord;
    }

    public void addDListener() {
        DownloadManager.getInstance().addDListener(this);
    }

    protected int getState() {
        int state = DLInfo.STATE_UNKNOWN;
        if (mDLInfo != null) {
            state = mDLInfo.state;
        } else if (isAppInstalled()) {
            state = DLInfo.STATE_INSTALLED;
        }
        return state;
    }

    public void update() {
        update(DLManager.getInstance(getContext()).getDLInfo(mDownloadUrl));
    }

    private void update(DLInfo info) {
        mDLInfo = info;

        if (mDLInfo != null) {
            if (mDLInfo.state == DLInfo.STATE_COMPLETED) {
                if (isAppInstalled()) {
                    mDLInfo.state = DLInfo.STATE_INSTALLED;
                    DLManager.getInstance(getContext()).updateDLInfo(mDLInfo);
                }
            } else if (mDLInfo.state == DLInfo.STATE_INSTALLED) {
                if (!isAppInstalled()) {
                    mDLInfo.state = DLInfo.STATE_COMPLETED;
                    DLManager.getInstance(getContext()).updateDLInfo(mDLInfo);
                }
            }
        }

        int progress = 0;
        if (info != null) {
            progress = info.currentBytes;
        }
        updateProgress(progress, 0);
    }

    protected void updateProgress(int progress, int speed) {
    }

    protected int getCurrentBytes() {
        return mDLInfo != null ? mDLInfo.currentBytes : 0;
    }

    protected int getTotalBytes() {
        return mDLInfo != null ? mDLInfo.totalBytes : 0;
    }

    protected int parseProgress(int progress) {
        if (mDLInfo != null && mDLInfo.totalBytes > 0) {
            return (int) (progress * 100L / mDLInfo.totalBytes);
        }
        return 0;
    }

//    public void onAppInstalled() {
//        if (isAppInstalled()) {
//            if (mDLInfo.state != DLInfo.STATE_INSTALLED) {
//                mDLInfo.state = DLInfo.STATE_INSTALLED;
//                DLManager.getInstance(getContext()).updateDLInfo(mDLInfo);
//                update(mDLInfo);
//            }
//        }
//    }

    private boolean isAppInstalled() {
        return mDownloadRecord != null && Util.isInstalled(getContext(), mDownloadRecord.getPackageName());
    }

    private boolean isDlingTask() {
        return DLManager.getInstance(getContext()).isDlingTask(mDownloadUrl);
    }

    private void saveDownloadInfo() {
        if (mDownloadRecord != null) {
            UserDB.getInstance().saveGameDownload(mDownloadRecord);
        } else {
            DebugLog.e("save downloadRecord. mDownloadRecord is null.");
        }
    }

    protected void start() {
        DownloadManager.getInstance().addDListener(this);
        DownloadManager.getInstance().start(mDownloadUrl, String.format(Locale.US, "%s.apk", mDownloadRecord.getPackageName()),
                mDownloadRecord.getSize());
        saveDownloadInfo();
    }

    protected void stop() {
        DownloadManager.getInstance().stop(mDownloadUrl);
    }

    protected void install() {
        File apkFile = new File(mDLInfo.dirPath, mDLInfo.fileName);
        if (apkFile.exists()) {
            Util.startInstall(getContext(), Uri.fromFile(apkFile));
        } else {
            mDLInfo.state = DLInfo.STATE_UNKNOWN;
            mDLInfo.currentBytes = 0;
            mDLInfo.totalBytes = 0;
            DLManager.getInstance(getContext()).updateDLInfo(mDLInfo);
            update();
            ToastUtils.showShort(getContext(), getResources().getString(R.string.failed_to_install));
        }
    }

    protected void open() {
        if (!TextUtils.isEmpty(mDownloadRecord.getPackageName())) {
            Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(mDownloadRecord.getPackageName());
            getContext().startActivity(launchIntent);
        } else {
            ToastUtils.showShort(getContext(), getResources().getString(R.string.failed_to_open));
        }
    }

    public void delete() {
        DownloadManager.getInstance().removeDListener(this);
        if (mDownloadRecord != null) {
            CartoonApp.getInstance().getDaoSession().getDownloadGameDao().delete(mDownloadRecord);
            if (!TextUtils.isEmpty(mDownloadUrl)) {
                DLManager.getInstance(getContext()).dlCancel(mDownloadUrl);
            }
        }
    }

    @Override
    public void onPrepare() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @Override
    public void onStart(String fileName, String realUrl, int fileLength) {
        DebugLog.d("onStart. appName = " + fileName + ", fileLength = " + fileLength);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @Override
    public void onProgress(final int progress, final int speed) {
//        LOG.debug("onProgress. progress = {}", progress);

        if (mHandler != null && isDlingTask()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateProgress(progress, speed);
                }
            });
        }
    }

    @Override
    public void onStop(int progress) {
        DebugLog.d("onStop. progress = " + progress);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @Override
    public void onFinish(File file) {
        DebugLog.d("onFinish");

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    @Override
    public void onError(int status, String error) {
        DebugLog.d("onError. error = " + error);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }
}
