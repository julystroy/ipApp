package com.cartoon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.bean.Listener;
import com.cartoon.bean.QuickListener;


/**
 * Created by jinbangzhu on 3/3/15.
 */
public class EasySharePreference {

    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor sp_editor = null;

    public static SharedPreferences getPrefInstance(final Context c) {
        if (sharedPreferences == null) {
            sharedPreferences = c.getSharedPreferences("cartoon", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static SharedPreferences.Editor getEditorInstance(final Context c) {
        if (sp_editor == null) {
            sp_editor = getPrefInstance(c).edit();
        }
        return sp_editor;
    }


    public static void setPlayListener(Context context, Listener listener) {
        String json = JSON.toJSONString(listener);
        getEditorInstance(context).putString("playListener"+ CartoonApp.versionCode, json).commit();
        getEditorInstance(context).putString("lastplay"+ CartoonApp.versionCode, "qt").commit();
    }


    public static String getLastPlay(Context context){
        return getPrefInstance(context).getString("lastplay"+ CartoonApp.versionCode , "qt");
    }


    public static Listener getPlayListener(Context context) {
        String json =  getPrefInstance(context).getString("playListener"+ CartoonApp.versionCode, null);
        if (TextUtils.isEmpty(json)) return null;
        return JSON.parseObject(json, Listener.class);
    }
    public static void setQuickPlayListener(Context context, QuickListener listener) {
        String json = JSON.toJSONString(listener);
        getEditorInstance(context).putString("playQuickListener"+ CartoonApp.versionCode, json).commit();
        getEditorInstance(context).putString("lastplay"+ CartoonApp.versionCode, "qn").commit();
    }
    public static QuickListener getPlayQuickListener(Context context) {
        String json =  getPrefInstance(context).getString("playQuickListener"+ CartoonApp.versionCode, null);
        if (TextUtils.isEmpty(json)) return null;
        return JSON.parseObject(json, QuickListener.class);
    }

    public static void setHasNewMessage(Context context) {
        getEditorInstance(context).putBoolean("newMessage"+ CartoonApp.versionCode, true).commit();
    }

    public static void setHaveNoNewMessage(Context context) {
        getEditorInstance(context).putBoolean("newMessage"+ CartoonApp.versionCode, false).commit();
    }


    public static boolean isHaveNewMessage(Context context) {
        return getPrefInstance(context).getBoolean("newMessage"+ CartoonApp.versionCode, false);
    }


    public static void setBangaiCount(Context context, int process) {
        getEditorInstance(context).putInt("Bangai"+ CartoonApp.versionCode, process).commit();
    }

    public static int getBangaiCount(Context context) {
        return getPrefInstance(context).getInt("Bangai"+ CartoonApp.versionCode, 0);
    }

    public static void setBangaiDownCount(Context context, int process) {
        getEditorInstance(context).putInt("BangaiDown"+ CartoonApp.versionCode, process).commit();
    }

    public static int getBangaiDownCount(Context context) {
        return getPrefInstance(context).getInt("BangaiDown", 0);
    }

    public static void setMyMessageCount(Context context, int process) {
        getEditorInstance(context).putInt("MyMessage"+ CartoonApp.versionCode, process).commit();
    }

    public static int getMyMessageCount(Context context) {
        return getPrefInstance(context).getInt("MyMessage"+ CartoonApp.versionCode, 0);
    }


//记录最后一个题目的位置
    public static void setPositionNum(Context context, int process) {
        getEditorInstance(context).putInt("PositionNum"+ CartoonApp.versionCode, process).commit();
    }

    public static int getPositionNum(Context context) {
        return getPrefInstance(context).getInt("PositionNum"+ CartoonApp.versionCode, 0);
    }
//记录显示题目时间
    public static void setLastQuestionTime(Context context, long i) {
        getEditorInstance(context).putLong("LastQuestionTime"+ CartoonApp.versionCode, i).commit();
    }
    public static long getLastQuestionTime(Context context) {
        return getPrefInstance(context).getLong("LastQuestionTime"+ CartoonApp.versionCode, 0);
    }
//记录题目id
    public static void setLastQestionId(Context context, int i) {
        getEditorInstance(context).putInt("lastquestionId"+ CartoonApp.versionCode,i).commit();
    }
    public static int getLastQestionId(Context context) {
        return getPrefInstance(context).getInt("lastquestionId"+ CartoonApp.versionCode, 0);
    }


    //是否已答
    public static void setHaveAnswered(Context context,boolean b) {
        getEditorInstance(context).putBoolean("answered"+ CartoonApp.versionCode, b).commit();
    }
    public static boolean getHaveAnswered(Context context) {
        return getPrefInstance(context).getBoolean("answered"+ CartoonApp.versionCode, false);
    }



    public static void setAllQuestion(Context context, String s) {
        getEditorInstance(context).putString("question"+ CartoonApp.versionCode,s).commit();
    }

    public static String getAllQuestion(Context context){
       return getPrefInstance(context).getString("question"+ CartoonApp.versionCode, null);
    }

    public static void setFightQuestion(Context context, String s) {
        getEditorInstance(context).putString("FightQuestion"+ CartoonApp.versionCode,s).commit();
    }

    public static String getFightQuestion(Context context){
        return getPrefInstance(context).getString("FightQuestion"+ CartoonApp.versionCode, null);
    }


    public static void setActionId(Context context, String s) {
        getEditorInstance(context).putString("ActionId"+ CartoonApp.versionCode,s).commit();
    }

    public static String getActionId(Context context){
        return getPrefInstance(context).getString("ActionId"+ CartoonApp.versionCode, null);
    }
    public static void setPushActionId(Context context, String s) {
        getEditorInstance(context).putString("PushActionId"+ CartoonApp.versionCode,s).commit();
    }

    public static String getPushActionId(Context context){
        return getPrefInstance(context).getString("PushActionId"+ CartoonApp.versionCode, null);
    }

    public static void setQuestionPoint(Context context, String s) {
        getEditorInstance(context).putString("QuestionPoint"+ CartoonApp.versionCode,s).commit();
    }

    public static String getQuestionPoint(Context context){
        return getPrefInstance(context).getString("QuestionPoint"+ CartoonApp.versionCode, null);
    }

    public static void setReadId(Context context, String s) {
        getEditorInstance(context).putString("ReadId"+ CartoonApp.versionCode,s).commit();
    }

    public static String getReadId(Context context){
        return getPrefInstance(context).getString("ReadId"+ CartoonApp.versionCode, null);
    }

    public static long getSectQuestionTime(Context context) {
        return getPrefInstance(context).getLong("SectQuestionTime"+ CartoonApp.versionCode, 0);
    }

    public static void setSectQuestionTime(Context context, long s) {
        getEditorInstance(context).putLong("SectQuestionTime"+ CartoonApp.versionCode,s).commit();
    }

    public static void setSectPositionNum(Context context, int i) {
        getEditorInstance(context).putInt("SectPositionNum"+ CartoonApp.versionCode,i).commit();
    }
    public static int getSectPositionNum(Context c) {
        return getPrefInstance(c).getInt("SectPositionNum"+ CartoonApp.versionCode,0);
    }

    public static void setSectHaveAnswered(Context context, boolean b) {
        getEditorInstance(context).putBoolean("Answered"+ CartoonApp.versionCode,b).commit();
    }
    public static boolean getSectHaveAnswered(Context c) {
        return getPrefInstance(c).getBoolean("Answered"+ CartoonApp.versionCode,false);
    }

    public static void setSectQuestionPoint(Context context, String s) {
        getEditorInstance(context).putString("SectQuestionPoint"+ CartoonApp.versionCode,s).commit();
    }
    public static String getSectQuestionPoint(Context c) {
        return getPrefInstance(c).getString("SectQuestionPoint"+ CartoonApp.versionCode,null);
    }
}
