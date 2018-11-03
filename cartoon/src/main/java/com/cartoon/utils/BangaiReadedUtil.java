package com.cartoon.utils;

import android.text.TextUtils;

import com.cartoon.CartoonApp;
import com.cartoon.http.StaticField;

/**
 * Created by cc on 16-12-2.
 */
public class BangaiReadedUtil {
    private static BangaiReadedUtil readedUtils = new BangaiReadedUtil();
    private String RID;
    private BangaiReadedUtil() {
        RID = EasySharePreference.getPrefInstance(CartoonApp.getInstance()).getString(StaticField.SP_READEDID, "");
    }

    public static BangaiReadedUtil getInstance() {
        return readedUtils;
    }

    public   void addReadedId(int id){
        if (id>= 0) {

            if (RID != null) {

              RID += "##"+id;
            } else {
                RID=String.valueOf(id);
            }
            EasySharePreference.getPrefInstance(CartoonApp.getInstance()).edit().putString(StaticField.SP_READEDID, RID).commit();
        }
    }
    //取id
    public  boolean  getReadedId(String s){

        if (!TextUtils.isEmpty(RID)) {
            String[] split = RID.split("##");
            for (int i=0;i<split.length;i++) {
                if (TextUtils.equals(s,split[i])) {
                    return true;
                }
            }
        }
        return false;
    }


    //取最后一个id
    public  boolean  getReadedLastId(String s){

        if (!TextUtils.isEmpty(RID)) {
            String[] split = RID.split("##");
            for (int i=0;i<split.length;i++) {
                if (TextUtils.equals(s,split[split.length-1])) {
                    return true;
                }
            }
        }
        return false;
    }


}
