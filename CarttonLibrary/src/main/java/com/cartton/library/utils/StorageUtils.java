package com.cartton.library.utils;

import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.IOException;

/**
 * 目录工具类
 * Created by David on 16/6/26.
 */
public class StorageUtils {

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private StorageUtils() {
    }

    public static File getCacheDirectory(Context context) {
        return getCacheDirectory(context, true);
    }

    public static File getCacheDirectory(Context context, boolean preferExternal) {
        File appCacheDir = null;

        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException var5) {
            externalStorageState = "";
        }

        if (preferExternal && "mounted".equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }

        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }

        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            L.w("Can\'t define system cache directory! \'%s\' will be used.", new Object[]{cacheDirPath});
            appCacheDir = new File(cacheDirPath);
        }

        return appCacheDir;
    }

    public static File getIndividualCacheDirectory(Context context, String path) {
        File cacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(cacheDir, path);
        if (!individualCacheDir.exists() && !individualCacheDir.mkdir()) {
            individualCacheDir = cacheDir;
        }
        return individualCacheDir;
    }

//    public static File getOwnCacheDirectory(Context context, String cacheDir) {
//        File appCacheDir = null;
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
//            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
//        }
//
//        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
//            appCacheDir = context.getCacheDir();
//        }
//
//        return appCacheDir;
//    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                L.w("Unable to create external cache directory", new Object[0]);
                return null;
            }

            try {
                (new File(appCacheDir, ".nomedia")).createNewFile();
            } catch (IOException var4) {
                L.i("Can\'t create \".nomedia\" file in application external cache directory", new Object[0]);
            }
        }

        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == 0;
    }
}
