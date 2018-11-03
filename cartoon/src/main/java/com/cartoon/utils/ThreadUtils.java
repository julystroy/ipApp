package com.cartoon.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * 线程辅助类
 * <p>
 * Created by David on 16/5/11.
 **/
public final class ThreadUtils {

    public static final Handler mHandler = new Handler(Looper.getMainLooper());

    public static Thread getMainThread() {
        return Looper.getMainLooper().getThread();
    }

    /***
     * 是否运行在主线程
     ***/
    public static boolean isRunInMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /***
     * 在主线程中运行
     ***/
    public static void runInMainThread(Runnable runnable) {
        if (runnable != null) {
            mHandler.post(runnable);
        }
    }

    /***
     * 延迟在主线程运行
     ***/
    public static void runInMainThreadLater(Runnable runnable, long delayMillis) {
        if (runnable != null) {
            mHandler.postDelayed(runnable, delayMillis);
        }
    }

    /***
     * 取消在主线程中运行
     ***/
    public static void cancelRunnableInMainThread(Runnable runnable) {
        if (runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
    }

    /***
     * 线程休眠
     ***/
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
