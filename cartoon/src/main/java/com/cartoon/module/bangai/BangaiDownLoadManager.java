/*
package com.cartoon.module.bangai;

import android.os.Environment;
import android.text.TextUtils;

import com.cartoon.bean.BangaiDownLoadList;
import com.cartoon.module.cartoon.CartoonDownManager;
import com.cartoon.utils.GreenDaoUtils;

import java.io.File;

*/
/**
 * Created by cc on 16-12-5.
 *//*

public class BangaiDownLoadManager {
    private           File                  downLoadPath;
    private        GreenDaoUtils         utils;

    private static BangaiDownLoadManager mSingleton;
    private  BangaiDownLoadManager(){
        this.utils = new GreenDaoUtils();
        this.downLoadPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/BangaiDL/");
    }

    */
/**
     * 获取实例
     **//*

    public static BangaiDownLoadManager getInstance() {
        if (mSingleton == null) {
            synchronized (CartoonDownManager.class) {
                mSingleton = new BangaiDownLoadManager();
            }
        }

        return mSingleton;
    }

public void insertBangaidown(BangaiDownLoadList list){
    utils.insertBangaiDown(list);
}

    public void deleteBangaiDown(String cartoon_id) {

        if (TextUtils.isEmpty(cartoon_id)) return;

        try {
            utils.deleteBangaiDown(cartoon_id);
            File file = new File(downLoadPath.getAbsolutePath(), cartoon_id+".mht");
           file.deleteOnExit();
           // file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
*/
