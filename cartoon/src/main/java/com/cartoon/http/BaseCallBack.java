package com.cartoon.http;

import android.text.TextUtils;

import com.cartoon.CartoonApp;
import com.cartoon.data.Keys;
import com.cartoon.view.DialogToast;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 请求基类。可以屏蔽code.
 * <p/>
 * Created by David on 16/4/9.
 */
public abstract class BaseCallBack<T> extends Callback<T> {

    public static final int HTTP_SUCCESS = 0;

    private String message;

    public int getErrorCode() {
        return code;
    }

    private int code;

    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String res_value = response.body().string().trim();
        JSONObject object = new JSONObject(res_value);

        code = object.optInt(Keys.HTTP_CODE);
        message = object.optString(Keys.HTTP_MESSAGE);

        DebugLog.d(response.request().url().toString());

        if (code == HTTP_SUCCESS) {
            String result = object.optString(Keys.HTTP_RESULT);
            DebugLog.i("...result.." + result);
            return parseNetworkResponse(result);
        } else {
            DebugLog.i("..code.." + code + "..message.." + message);
            return null;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();

        onLoadFail();

        if (e instanceof ConnectException) {
            ToastUtils.showShort(CartoonApp.getInstance(), "网络连接异常");

        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShort(CartoonApp.getInstance(), "网络连接超时");
        } else if (e instanceof IOException) {
            ToastUtils.showShort(CartoonApp.getInstance(), "网络异常");
        } else {
            if (!TextUtils.isEmpty(e.getMessage())) {
                ToastUtils.showShort(CartoonApp.getInstance(), e.getMessage());
            }
        }
    }

    @Override
    public void onResponse(T response, int id) {
        if (null == response) {
            if (code == 108) {// 108=token失效
                CartoonApp.getInstance().logout();
              //  CartoonApp.getInstance().startActivity(new Intent(CartoonApp.getInstance(), LoginActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
                DialogToast.createToastConfig().ToastShow(CartoonApp.getInstance(), "登陆过期，请重新登录", 2);
            } if (code == 15) {// 15评论内容为空
                onContentNull();
                return;
            }else {
                if (!TextUtils.isEmpty(message)) {
                    if (!message.equals("广告获取失败")) {

                        if (com.cartton.library.BuildConfig.DEBUG) {
                            if (code!=1004)
                            ToastUtils.showShort(CartoonApp.getInstance(), message);
                        }
                    }
                }
            }
            onLoadFail();
            if (message != null && !message.equals("SUCCESS")) {
                if (code != -1&&code!=1004) {
                    if (code == 1005) {
                        CartoonApp.getInstance().logout();
                     //   CartoonApp.getInstance().startActivity(new Intent(CartoonApp.getInstance(), LoginActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
                        DialogToast.createToastConfig().ToastShow(CartoonApp.getInstance(), "登陆过期，请重新登录", 2);
                    } else {
                        if (code!=0)
                        DialogToast.createToastConfig().ToastShow(CartoonApp.getInstance(),message,2);
                    }
                }
            }

        } else {
            onLoadSuccess(response);
//            DebugLog.i("..res.." + t.toString());
        }
    }

    public String getMessage() {
        return message;
    }

    public abstract void onLoadFail();
    public abstract void onContentNull();

    public abstract void onLoadSuccess(T response);

    public abstract T parseNetworkResponse(String response) throws Exception;

}
