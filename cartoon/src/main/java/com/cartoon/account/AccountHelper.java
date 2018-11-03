package com.cartoon.account;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.data.Keys;
import com.cartoon.data.User;
import com.cartoon.data.response.ConfigsResponse;
import com.cartoon.data.response.UserInfoResponse;
import com.cartoon.http.StaticField;
import com.cartoon.utils.PreferenceManager;
import com.cartton.library.utils.DebugLog;

import org.json.JSONException;
import org.json.JSONObject;

import cn.idianyun.streaming.util.Util;
import cn.idianyun.streaming.volley.GsonRequest;
import cn.idianyun.streaming.volley.VolleySingleton;

/**
 * 账户辅助类，
 * 用于更新用户信息和保存当前账户等操作
 * TODO 区分游客模式 第三方登录模式
 */
public final class AccountHelper {
    private        User          user;
    private        String        mCookie;
    private        Application   application;
    private static AccountHelper instances;

    private static final int DEFAULT_RATING_TIME = 180; //180秒
    private int mPlayTimeNeededForRating; //单位：秒

    private AccountHelper(Application application) {
        this.application = application;
    }

    public static AccountHelper instance() {
        return instances;
    }

    public static void init(Application application) {
        DebugLog.d("init userInstance");
        instances = new AccountHelper(application);
        instances.initUser();

    }

    public static void fini() {
        DebugLog.d("fini instances");
        instances = null;
    }

    public static boolean isLogin() {
        return getUser() != null && !TextUtils.isEmpty(getCookie());
    }

    public static String getCookie() {
        String s = getUser().getCookie() == null ? (instances == null ? "" : (instances.mCookie == null ? "" : instances.mCookie)) : getUser().getCookie();
        return getUser().getCookie() == null ? (instances == null ? "" : (instances.mCookie == null ? "" : instances.mCookie)) : getUser().getCookie();
    }

    private void initUser(){
        if (instances.user == null)
            instances.user = PreferenceManager.instance().getUser();
        if (instances.user == null)
            instances.user = new User();
    }
    public synchronized static User getUser() {
        if (instances == null) {
            DebugLog.e("AccountHelper instances is null, you need call init() method.");
            return new User();
        }

        return instances.user;
    }

    public static void logoutUser(User user, Context context) {
        // 更新Cookie
        user.setCookie(instances.mCookie);
        updateUserCache(user);
    }

    public static void updateUserVip(User userInfo) {
        User user = AccountHelper.getUser();
        user.setVIP(userInfo.isVIP());
        user.setVipExpiryDate(userInfo.getVipExpiryDate());
        updateUserCache(user);
    }

    public static void updateUserCache(User user) {
        if (user == null)
            return;
        // 保留Cookie信息
        if (TextUtils.isEmpty(user.getCookie()) && instances.user != null&&instances.user!=user)
            user.setCookie(instances.user.getCookie());
        instances.user = user;
        PreferenceManager.instance().saveUser(user);
        DebugLog.d("updateUserCache. user = " + user);

        // 切换账号（登陆或退出登陆）成功后更新未评分数
        updateUserUI(user);
    }

    private static void clearUserCache() {
        instances.user = null;
        PreferenceManager.instance().clearUser();
    }

    /*
    *游客模式
    */
    public static void register(Context context) {
        User user = PreferenceManager.instance().getUser();
        AccountHelper.updateRatingTime(context);
        if (user == null || (user.isVisitor() && TextUtils.isEmpty(user.getCookie()))) {
            doRegister(context);
        }else {
            getLatestUserInfo(context);
        }
    }

    private static void doRegister(final Context context) {
        final String url = String.format("%s/api/users/register", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("aid", Util.getDeviceId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("doRegister. url = " + url);

        GsonRequest<UserInfoResponse> request = new GsonRequest<UserInfoResponse>(Request.Method.POST, url, params.toString(), UserInfoResponse.class, new Response.Listener<UserInfoResponse>() {
            @Override
            public void onResponse(UserInfoResponse response) {
                //success
                DebugLog.d("doRegister success = " + response.data);
                AccountHelper.onLoginSuccess(response.data, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //error
                DebugLog.e("doRegister failed. e = " + error.toString());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void onLoginSuccess(User user, Context context) {
        // 更新Cookie
        user.setCookie(instances.mCookie);

        // 保存缓存
        updateUserCache(user);
    }

    public void saveCookie(String cookie) {
        if (!TextUtils.isEmpty(cookie)) {
            mCookie = cookie;
        }
    }

    /**
     * 退出登陆操作需要传递一个View协助完成延迟检测操作
     *
     * @param view     View
     * @param runnable 当清理完成后回调方法
     */
    public static void logout(final View view, final Runnable runnable) {
        // 清除用户缓存
        clearUserCache();
        // 等待缓存清理完成
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.removeCallbacks(this);
                User user = PreferenceManager.instance().getUser();
                // 判断当前用户信息是否清理成功
                if (user == null) {
                    clearAndPostBroadcast(instances.application);
                    runnable.run();
                } else {
                    view.postDelayed(this, 200);
                }
            }
        }, 200);
    }

    /**
     * 当前用户信息清理完成后调用方法清理服务等信息
     *
     * @param application Application
     */
    private static void clearAndPostBroadcast(Application application) {
        // 清理相关

        // Logout 广播
        Intent intent = new Intent(Keys.INTENT_ACTION_LOGOUT);
        LocalBroadcastManager.getInstance(application).sendBroadcast(intent);
    }

    public static void updateRatingTime(Context context) {
        String url = String.format("%s/api/configs", StaticField.BASE_CXURL);

        GsonRequest<ConfigsResponse> request = new GsonRequest<ConfigsResponse>(url, ConfigsResponse.class, new Response.Listener<ConfigsResponse>() {
            @Override
            public void onResponse(ConfigsResponse response) {
                AccountHelper.setPlayTimeNeededForRating(response.data.ratingCondition);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (AccountHelper.getPlayTimeNeededForRating() == 0) {
                    AccountHelper.setPlayTimeNeededForRating(DEFAULT_RATING_TIME);
                }
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static void setPlayTimeNeededForRating(int time) {
        instances.mPlayTimeNeededForRating = time;
    }

    public static int getPlayTimeNeededForRating() {
        if (instances == null) return DEFAULT_RATING_TIME;

        return instances.mPlayTimeNeededForRating;
    }

    public static void getLatestUserInfo(final Context context) {
        String url = String.format("%s/api/users/info", StaticField.BASE_CXURL);
        GsonRequest<UserInfoResponse> request = new GsonRequest<UserInfoResponse>(url, UserInfoResponse.class, new Response.Listener<UserInfoResponse>() {
            @Override
            public void onResponse(UserInfoResponse response) {
                User user = response.data;
                updateUserUI(user);

                // 更新vip信息
                updateUserVip(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("get no rating count fail: " + error.toString());
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private static void updateUserUI (User user) {
        //TODO:
//        MainActivity activity = (MainActivity) ActivityManager.getInstance().getMainActivity();
//        if (activity != null){
//            if (user.noRatingCount == 0) {
//                activity.hideRedSpot();
//                DebugLog.d("hide red spot in tab");
//            }else {
//                activity.showRedSpot();
//                DebugLog.d("show red spot in tab");
//            }
//        }
    }

    //TODO:
//    public static void doLogin(final Context context, UserInfo userInfo, final Response.Listener listener, final Response.ErrorListener errorListener) {
//        String url = String.format("%s/api/users/login", StaticField.BASE_CXURL);
//        JSONObject params = new JSONObject();
//        try {
//            params.put("aid", Util.getDeviceId(context));
//            params.put("platform", userInfo.platform);
//            params.put("unionid", userInfo.unionid);
//            params.put("openid", userInfo.openid);
//            params.put("name", userInfo.name);
//            params.put("avatar", userInfo.avatarUrl);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        DebugLog.d("login. url = " + url + " parames : " + params.toString());
//
//        final GsonRequest<UserInfoResponse> request = new GsonRequest<UserInfoResponse>(Request.Method.POST, url, params.toString(), UserInfoResponse.class, new Response.Listener<UserInfoResponse>() {
//            @Override
//            public void onResponse(UserInfoResponse response) {
//                DebugLog.d("login success. data = " + response.data);
//                User user = response.data;
//                user.setVisitor(false);
//                AccountHelper.onLoginSuccess(user, context);
//                listener.onResponse(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                DebugLog.e("login failed. e = " + error.toString());
//                errorListener.onErrorResponse(error);
//            }
//        });
//        VolleySingleton.getInstance(context).addToRequestQueue(request);
//    }
}
