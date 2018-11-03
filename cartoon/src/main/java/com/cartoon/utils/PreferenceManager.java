package com.cartoon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cartoon.data.User;

import cn.idianyun.streaming.util.Util;

public class PreferenceManager {
    private static PreferenceManager sInstance;
    private SharedPreferences mSharedPreferences;

    private static final String USER = "user";
    private static final String ONLY_WHEN_WIFI = "ONLY_WHEN_WIFI";
    private static final String AUTO_DELETE_APK = "auto_delete_apk"; //安装完成自动删除安装包

    private PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences("changxian_preferences", Context.MODE_PRIVATE);
    }

    public static PreferenceManager instance() {
        return sInstance;
    }

    public static void init(Context context) {
        sInstance = new PreferenceManager(context);
    }

    public static void fini() {
        sInstance = null;
    }

    public void save(String key, int value) {
        Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void save(String key, String value) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, boolean value) {
        Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void save(String key, long value) {
        Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public int retriveIntPreference(String key) {
        return mSharedPreferences.getInt(key, -1);
    }

    public String retriveStringPreference(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public boolean retriveBooleanPreference(String key) {
        return mSharedPreferences.getBoolean(key, true);
    }

    public long retriveLongPreference(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void saveUser(User user) {
        String userString = Util.ObjToJson(user);
        save(USER, userString);
    }

    public User getUser() {
        String userString = retriveStringPreference(USER);
        return Util.loadObject(userString, User.class);
    }

    public void clearUser() {
        save(USER, "");
    }

    public boolean usableOnlyWhenWifi() {
        return retriveBooleanPreference(ONLY_WHEN_WIFI);
    }

    public void setUsableOnlyWhenWIfi(boolean usable) {
        save(ONLY_WHEN_WIFI, usable);
    }

    public boolean autoDeleteApk() {
        return retriveBooleanPreference(AUTO_DELETE_APK);
    }

    public void setAutoDeleteApk(boolean deleteApk) {
        save(AUTO_DELETE_APK, deleteApk);
    }



}
