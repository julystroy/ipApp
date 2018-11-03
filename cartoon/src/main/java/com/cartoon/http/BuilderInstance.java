package com.cartoon.http;

import android.text.TextUtils;

import com.cartoon.CartoonApp;
import com.cartton.library.utils.GeneraUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

import java.util.HashMap;

/**
 * 请求实体初始类
 * <p/>
 * Created by David on 16/3/1.
 */
public class BuilderInstance {

    private BuilderInstance() {

    }

    private static CartoonApp sApp;
    private static BuilderInstance sInstance;

    public static BuilderInstance getInstance() {
        if (sInstance == null) {
            return sInstance = new BuilderInstance();
        }
        return sInstance;
    }

    static {
        sApp = CartoonApp.getInstance();
    }

    private static final int MAX_AGE = 60 * 60 * 24 * 28;
    private static final int MAX_STALE = 60 * 60 * 24 * 28;

    /*****
     * 返回纯净的Get方法实体类←_←
     ****/
    public GetBuilder getPureBuilderInstance(String requestUrl) {
        return OkHttpUtils.get().url(requestUrl);
    }

    /*****
     * 返回Get方法实体类
     ****/
    public GetBuilder getGetBuilderInstance(String requestUrl) {
        long time = System.currentTimeMillis();

        return OkHttpUtils.get().url(requestUrl)
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("timestamp", String.valueOf(time))
//                .addHeader("Cache-Control", "public, max-age=" + MAX_AGE)
//                .addParams("sign", "test")
//                .addParams("timestamp", String.valueOf(time))
                .addParams("uid", CartoonApp.getInstance().getUserId());


//        HashMap<String, String> params = initParams(null);

//        return OkHttpUtils.get().url(requestUrl);
    }

    /******
     * 返回Post方法实体类
     ******/
    public PostFormBuilder getPostBuilderInstance(String requestUrl) {
        long time = System.currentTimeMillis() ;
        return OkHttpUtils.post().url(requestUrl)
//                .addHeader("Content-Type","application/json;charset=UTF-8")
//                .addParams("X-GL-Token", "1")
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("timestamp", String.valueOf(time))
//                .addParams("sign", "test")
                .addParams("uid", CartoonApp.getInstance().getUserId());
//        HashMap<String, String> params = initParams(null);

//        return OkHttpUtils.post().url(requestUrl);
    }

    /*****
     * 返回Get方法实体类
     *
     * @param map 请求参数
     ****/
    public GetBuilder getGetBuilderInstance(String requestUrl, HashMap<String, String> map) {
        HashMap<String, String> params = initParams(map);
        return OkHttpUtils.get().url(requestUrl).params(params);
    }

    public void encodeParams(HashMap<String, String> map) {

    }

    /******
     * 返回Post方法实体类
     *
     * @param map 请求参数
     ******/
    public PostFormBuilder getPostBuilderInstance(String requestUrl, HashMap<String, String> map) {
        HashMap<String, String> params = initParams(map);
        return OkHttpUtils.post().url(requestUrl).params(params);
    }

    private HashMap<String, String> initParams(HashMap<String, String> params) {

        if (params == null && !TextUtils.isEmpty(sApp.getToken())) {
            params = new HashMap<>();
        }

        if (params != null) {

            if (!TextUtils.isEmpty(sApp.getToken())) {
                params.put("token", sApp.getToken());
                params.put("uid", sApp.getUserId());
            }

            long time = System.currentTimeMillis() / 1000;
            params.put("timestamp", String.valueOf(time));

            params.put("sign", GeneraUtils.getHttpOpaque(params));
        }

        return params;
    }
}
