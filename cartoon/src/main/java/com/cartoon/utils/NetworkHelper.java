package com.cartoon.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.cartoon.module.changxian.download.DownloadManager;

import java.util.HashSet;
import java.util.Set;

import cn.com.xuanjiezhimen.R;

public class NetworkHelper {
    private static NetworkHelper sInstance = null;

    private NetworkMonitor mNetworkMonitor;
    private NetworkInfo mActiveNetwork;

    private Context mContext;
    private AlertDialog mAlertDialog;
    private Set<Runnable> mAcceptTasks;
    private Set<Runnable> mRejectTasks;
    private Set<NetworkChangeListener> mNetworkListeners;
    private boolean mMobileNetworkAccessAccepted; //本次设置保存在内存中有效

    private Handler mHandler;

    public interface NetworkChangeListener {
        /**
         * 网络环境发生变化的回调接口
         *
         * @param actived 为当前的网络环境，可能为null
         */
        public void onNetworkChangeListener(NetworkInfo actived);
    }

    public static void init(Context context) {
        sInstance = new NetworkHelper(context);
    }

    public static void fini() {
        sInstance.mContext.unregisterReceiver(sInstance.mNetworkMonitor);

        sInstance = null;
    }

    public static NetworkHelper getInstance() {
        return sInstance;
    }

    public void registerNetworkListener(NetworkChangeListener listener) {
        if (listener != null) {
            mNetworkListeners.add(listener);
        }
    }

    public void unRegisterNetworkListener(NetworkChangeListener listener) {
        if (listener != null) {
            mNetworkListeners.remove(listener);
        }
    }

    public void accessNetwork(Context context, Runnable runnable, Runnable cancelRunnable, boolean showDialog) {
        if (needNetworkAccessInteract()) {
            if (runnable != null) {
                mAcceptTasks.add(runnable);
            }
            if (cancelRunnable != null) {
                mRejectTasks.add(cancelRunnable);
            }

            if (showDialog) {
                mAlertDialog = (new AlertDialog.Builder(context, R.style.CXAlertDialogStyle))
                        .setMessage(R.string.tips_mobile_network)
                        .setPositiveButton(R.string.go_on, mAcceptClickListener)
                        .setNegativeButton(R.string.cancel, mRejectClickListener)
                        .create();
                mAlertDialog.setOnDismissListener(mOnDismissListener);
                mAlertDialog.setCanceledOnTouchOutside(false);
                if (!(context instanceof Activity)) {
                    mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                mAlertDialog.show();
            }
        } else {
            if (runnable != null) {
                mHandler.post(runnable);
            }
        }
    }

    public void accessNetwork(Context context, Runnable runnable, Runnable cancelRunnable, boolean showDialog, String message) {
        if (needNetworkAccessInteract()) {
            if (runnable != null) {
                mAcceptTasks.add(runnable);
            }
            if (cancelRunnable != null) {
                mRejectTasks.add(cancelRunnable);
            }

            if (showDialog) {
                mAlertDialog = (new AlertDialog.Builder(context, R.style.CXAlertDialogStyle))
                        .setMessage(message)
                        .setPositiveButton(R.string.go_on, mAcceptClickListener)
                        .setNegativeButton(R.string.cancel, mRejectClickListener)
                        .create();
                mAlertDialog.setOnDismissListener(mOnDismissListener);
                mAlertDialog.setCanceledOnTouchOutside(false);
                if (!(context instanceof Activity)) {
                    mAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); //小米悬浮窗默认关闭的,目前没需求悬浮窗。
                }
                mAlertDialog.show();
            }
        } else {
            if (runnable != null) {
                mHandler.post(runnable);
            }
        }
    }

    public void accessNetwork(Context context, final Runnable runnable, final Runnable cancelRunnable) {
        accessNetwork(context, runnable, cancelRunnable, true);
    }

    public void accessNetwork(Context context, final Runnable runnable) {
        accessNetwork(context, runnable, null);
    }

    public boolean needNetworkAccessInteract() {
        boolean acceptedInSettings = PreferenceManager.instance().usableOnlyWhenWifi();

        return isMobileNetwork() && (acceptedInSettings);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null;
    }

    public boolean isMobileNetwork() {
        boolean result = false;

        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                result = networkInfo.isConnectedOrConnecting();
            }
        }

        return result;
    }

    public boolean isMobileNetworkAccepted() {
        return mMobileNetworkAccessAccepted;
    }

    private NetworkHelper(Context context) {
        mContext = context;
        mAlertDialog = null;
        mAcceptTasks = new HashSet<Runnable>();
        mRejectTasks = new HashSet<Runnable>();
        mNetworkListeners = new HashSet<NetworkChangeListener>();
        mHandler = new Handler();
        mMobileNetworkAccessAccepted = false;

        mNetworkMonitor = new NetworkMonitor();
        context.registerReceiver(mNetworkMonitor, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private final OnClickListener mAcceptClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            mMobileNetworkAccessAccepted = true;

            for (Runnable runnable : mAcceptTasks) {
                mHandler.post(runnable);
            }

            clearTask();
        }
    };

    private final OnClickListener mRejectClickListener = new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            for (Runnable runnable : mRejectTasks) {
                mHandler.post(runnable);
            }

            clearTask();
        }
    };
    private OnDismissListener mOnDismissListener = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            clearTask();
        }
    };

    private void clearTask() {
        mAcceptTasks.clear();
        mRejectTasks.clear();
        mAlertDialog = null;
    }

    public class NetworkMonitor extends BroadcastReceiver {

        public NetworkMonitor() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo activeInfo = manager.getActiveNetworkInfo();

                NetworkInfo temp = mActiveNetwork;
                if (!(temp == null && activeInfo == null)) {
                    if ((temp == null && activeInfo != null) || (temp != null && activeInfo == null)
                            || (temp.getType() != activeInfo.getType())) {
                        if (activeInfo != null && activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            mMobileNetworkAccessAccepted = false;
                        }
                        for (final NetworkChangeListener listener : mNetworkListeners) {
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    listener.onNetworkChangeListener(activeInfo);
                                }
                            });
                        }
                    }
                }
                mActiveNetwork = activeInfo;

                NetworkHelper helper = NetworkHelper.getInstance();
                if (helper != null) {
                    if (helper.needNetworkAccessInteract()) {
                        stopActiveTasks();
                    }
                }

            }
        }

        private void stopActiveTasks() {
            DownloadManager.getInstance().stopAll();
        }

        private void startActivedTasks() {
            DownloadManager.getInstance().resumeAll();
        }

        private final Runnable mStartActivedTasksTask = new Runnable() {

            @Override
            public void run() {
                startActivedTasks();
            }
        };

        private final Runnable mClearActivedTasksTask = new Runnable() {

            @Override
            public void run() {
            }
        };
    }

}
