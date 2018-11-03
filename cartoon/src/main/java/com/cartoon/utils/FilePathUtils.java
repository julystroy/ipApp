package com.cartoon.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by jinbangzhu on 8/5/16.
 */

public class FilePathUtils {
    public static String getCartoonMainPath() {
        File extStore = Environment.getExternalStorageDirectory();
        return extStore.getPath() + "/cartoon";
    }


    public static String getCartoonImagePath() {
        return getCartoonMainPath() + "/images/";
    }
}
