/*
package com.cartoon.utils;

import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.cartoon.CartoonApp;
import com.cartoon.data.Designs;
import com.cartoon.http.StaticField;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.com.xuanjiezhimen.R;
import okhttp3.Call;

*/
/**
 * Created by debuggerx on 16-11-21.
 *//*

public class WebDesignUtils {

    private static StringBuilder sSb;

    public static void getDesignConfig() {
        Designs designs;
        boolean isFirst = CartoonApp.getInstance().getPreferences().getBoolean(StaticField.IS_FIRST, true);
        if (isFirst) {
            designs = initDesignConfig();
        } else {
            File designConfig = new File(CartoonApp.getInstance().getFilesDir(), "design.json");
            if (!designConfig.exists()) {
                designs = initDesignConfig();
            } else {
                designs = readDesignConfig(designConfig);
            }

        }
       // CartoonApp.getInstance().setDesigns(designs);
    }

    private static Designs readDesignConfig(File designConfig) {
        FileInputStream fis = null;
        Designs designs = null;

        try {
            fis = new FileInputStream(designConfig);
            designs = getDesignFromInPutStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return designs;
    }

    private static Designs initDesignConfig() {
        InputStream inputStream = CartoonApp.getInstance().getApplicationContext().getResources().openRawResource(R.raw.design);

        Designs designs = getDesignFromInPutStream(inputStream);

        File defaultDesign = new File(CartoonApp.getInstance().getFilesDir(), "design.json");
        defaultDesign.setWritable(true);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(defaultDesign);
            fileOutputStream.write(sSb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        CartoonApp.getInstance().getPreferences().edit().putBoolean(StaticField.IS_FIRST, false).commit();

        return designs;
    }

    private static Designs getDesignFromInPutStream(InputStream inputStream) {
        BufferedReader Reader = new BufferedReader(new InputStreamReader(inputStream));
        sSb = new StringBuilder();
        String line = null;
        try {
            while ((line = Reader.readLine()) != null) {
                sSb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return (Designs) JSONObject.parseObject(sSb.toString(), Designs.class);
    }

    public static void getLastedDesign() {

        //获取服务器端最新布局设计
        OkHttpUtils.get().url(StaticField.URL_APP_LASTED_DESIGN).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Designs designs = JSONObject.parseObject(response, Designs.class);
//// FIXME: 16-11-22 第一次登陆  报空java.lang.String com.cartoon.data.Designs.getVer()

                    if (Integer.valueOf(designs.ver) > Integer.valueOf(CartoonApp.getInstance().getDesigns().getVer())) {
                        updateDesignConfig(response);
                        Toast.makeText(CartoonApp.getInstance().getBaseContext() , "界面有更新，请重启应用以应用配置" , Toast.LENGTH_SHORT).show();
                    }
                }
        });

    }

    private static void updateDesignConfig(String response) {
        File defaultDesign = new File(CartoonApp.getInstance().getFilesDir(), "design.json");
        boolean delete = defaultDesign.delete();
        if (delete) {
            defaultDesign.setWritable(true);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(defaultDesign);
                fileOutputStream.write(response.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != fileOutputStream) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
*/
