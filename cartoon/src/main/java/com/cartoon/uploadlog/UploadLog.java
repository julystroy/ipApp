package com.cartoon.uploadlog;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.data.response.EmptyResponse;
import com.cartoon.http.StaticField;
import com.cartton.library.utils.DebugLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cn.idianyun.streaming.util.Util;
import cn.idianyun.streaming.volley.GsonRequest;
import cn.idianyun.streaming.volley.VolleySingleton;

/**
 * Created by wusue on 17/5/22.
 */

public class UploadLog {

    public static final int NO_VALUE = 0;

    //统计页面(page)
    public static final int FEATURED_TAB_PAGE = 1;
    public static final int TOP_GAMES_TAB_PAGE = 2;
    public static final int RANK_TAB_PAGE = 3;
    public static final int PLAY_HISTORY_PAGE = 4;
    public static final int FAVOR_PAGE = 5;
    public static final int DOWNLOAD_PAGE = 6;
    public static final int DETAIL_PAGE = 7;
    public static final int NEW_GAME_PAGE = 8;
    public static final int PLAYING_PAGE = 9;
    //试玩内部弹窗上的下载按钮，sdk未提供细致分类，待添加
    public static final int PLAYING_MID_DIALOG = 10;
    public static final int PLAYING_END_DIALOG = 11;

    //统计按钮(btn)
    public static final int FAVOR_BUTTON = 1;
    public static final int SHARE_BUTTON = 2;
    public static final int RATING_BUTTON = 3;
    public static final int COMMENT_BUTTON = 4;
    public static final int REPLY_BUTTON = 5;
    public static final int PLAY_BUTTON = 6;
    public static final int DOWNLOAD_AND_PLAY_BUTTON = 7;
    public static final int DOWNLOAD_BUTTON = 8;

    //统计事件(code)
    public static final int DO_SHOW = 1;
    public static final int DO_LEFT = 2;
    public static final int DO_CLICK = 3;
    public static final int DO_COMPLETED = 4;

    //页面对应fragment
    public static final String FEATURED_FRAGMENT = "FeaturedFragment";
    public static final String TOP_GAMES_FRAGMENT = "TopgamesFragment";
    public static final String RANK_FRAGMENT = "TabRankFragment";
    public static final String PLAY_HISTORY_FRAGMENT = "PlayHistoryTapFragment";
    public static final String FAVOR_FRAGMENT = "FavoriteFragment";
    public static final String DOWNLOAD_FRAGMENT = "DownloadFragment";
    public static final String DETAIL_FRAGMENT = "DetailFragment";
    public static final String NEW_GAMES_TAB_FRAGMENT = "NewGamesFragment";

    private static int preFragment = NO_VALUE;
    private static boolean playing = false;

    private long enterTime  = new Date().getTime() / 1000;   //秒为单位

    public long getDuration() {
        long nowTime = new Date().getTime() / 1000;
        return nowTime - enterTime;
    }

    private static void uploadLog(int page, int btn, int code, long duration, int fromPage, long appId, Context context) {
        String url = String.format("%s/api/logs", StaticField.BASE_CXURL);
        JSONObject params = new JSONObject();
        try {
            params.put("code", code);
            if (page != NO_VALUE) {
                params.put("page", page);
            }else if (btn != NO_VALUE) {
                params.put("btn", btn);
            }
            if (duration != NO_VALUE) {
                params.put("duration", duration);
            }
            if (fromPage != NO_VALUE) {
                params.put("from", fromPage);
            }
            if (appId != NO_VALUE) {
                params.put("appId", appId);
            }
            params.put("versionName", Util.getAppVersion(context));
            params.put("versionCode", Util.getAppVersionCode(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("upload url = " + url +" , params = " + params.toString());
        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
            @Override
            public void onResponse(EmptyResponse response) {
                DebugLog.d("upload succeed");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.d("upload failed, error: " + error.toString());
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 上报页面展示
     *
     * @param fragmentName     fragment名称
     * @param context          Activity context
     */
    public static void uploadPageStartLog(String fragmentName, Activity context) {
        DebugLog.d("upload page start: " + fragmentName);
        int page = getPageCodeFromFragmentName(fragmentName);
        if (page != NO_VALUE) {
            DebugLog.d("upload page start has value: " + page);
            if (page == DETAIL_PAGE) {
                uploadLog(page, NO_VALUE, DO_SHOW, NO_VALUE, preFragment, NO_VALUE, context);
            }else {
                uploadLog(page, NO_VALUE, DO_SHOW, NO_VALUE, NO_VALUE, NO_VALUE, context);
            }
            preFragment = page;
        }
    }

    /**
     * 上报页面展示结束，及其时长
     *
     * @param fragmentName     fragment名称
     * @param duration         展示时长，秒为单位
     * @param context          Activity context
     */
    public static void uploadPageEndLog(String fragmentName, long duration, Activity context) {
        DebugLog.d("upload page end: " + fragmentName);
        int page = getPageCodeFromFragmentName(fragmentName);
        if (page != NO_VALUE) {
            DebugLog.d("upload page end has value: " + page);
            uploadLog(page, NO_VALUE, DO_LEFT, duration, NO_VALUE, NO_VALUE, context);
        }
    }

    /**
     * 上报试玩，边下边玩，下载按钮点击
     *
     * @param buttonIndex   对应前面Button，只取PLAY，DOWNLOAD_AND_PLAY，DOWNLOAD
     * @param appId         对应app，取C端id，非试玩id
     * @param context       Activity context
     * */
    public static void uploadGamePlayButtonClickLog(int buttonIndex, long appId, Context context) {
        DebugLog.d("click button at " + buttonIndex);
        if (preFragment != NO_VALUE) {
            if (buttonIndex == DOWNLOAD_BUTTON && playing) {
                uploadLog(NO_VALUE, buttonIndex, DO_CLICK, NO_VALUE, PLAYING_PAGE, appId, context);
            }else {
                uploadLog(NO_VALUE, buttonIndex, DO_CLICK, NO_VALUE, preFragment, appId, context);
                if (buttonIndex != DOWNLOAD_BUTTON) {
                    playing = true;
                }
            }
        }
    }

    /**
     * 上报试玩，边下边玩，下载按钮操作结束
     *
     * @param buttonIndex   对应前面Button，只取PLAY，DOWNLOAD_AND_PLAY，DOWNLOAD
     * @param duration      试玩时长，DOWNLOAD不带此参数，设置为NO_VALUE
     * @param appId         对应app，取C端id，非试玩id
     * @param context       Activity context
     * */
    public static void uploadGamePlayActionCompleteLog(int buttonIndex, long duration, long appId, Context context) {
        DebugLog.d("action " + buttonIndex + " completed, duration:" + duration);
        if (preFragment != NO_VALUE) {
            uploadLog(NO_VALUE, buttonIndex, DO_COMPLETED, duration, preFragment, appId, context);
            if (buttonIndex != DOWNLOAD_BUTTON) {
                playing = false;
            }
        }
    }

    /**
     * 上报点击操作（点赞，分享）
     *
     * @param buttonIndex   对应前面Button
     * @param appId         对应app，取C端id，非试玩id
     * @param context       Activity context
     * */
    public static void uploadPageActionClickLog(int buttonIndex, long appId, Activity context) {
        DebugLog.d("upload page: " + preFragment + " action: " + buttonIndex);
        uploadLog(NO_VALUE, buttonIndex, DO_CLICK, NO_VALUE, preFragment, appId, context);
    }

    /**
     * 上报点击操作完成（评分，评论，回复）
     *
     * @param buttonIndex   对应前面Button
     * @param appId         对应app，取C端id，非试玩id
     * @param context       Activity context
     * */
    public static void uploadPageActionCompleteLog(int buttonIndex, long appId, Activity context) {
        DebugLog.d("upload page: " + preFragment + " action: " + buttonIndex);
        uploadLog(NO_VALUE, buttonIndex, DO_COMPLETED, NO_VALUE, preFragment, appId, context);
    }

    private static int getPageCodeFromFragmentName(String FragmentName) {
        if (FragmentName.equals(FEATURED_FRAGMENT) ) {
            return FEATURED_TAB_PAGE;
        }else if (FragmentName.equals(TOP_GAMES_FRAGMENT)) {
            return TOP_GAMES_TAB_PAGE;
        }else if (FragmentName.equals(RANK_FRAGMENT)) {
            return RANK_TAB_PAGE;
        }else if (FragmentName.equals(PLAY_HISTORY_FRAGMENT)) {
            return PLAY_HISTORY_PAGE;
        }else if (FragmentName.equals(FAVOR_FRAGMENT)) {
            return FAVOR_PAGE;
        }else if (FragmentName.equals(DOWNLOAD_FRAGMENT)) {
            return DOWNLOAD_PAGE;
        }else if (FragmentName.equals(DETAIL_FRAGMENT)) {
            return DETAIL_PAGE;
        }else if (FragmentName.equals(NEW_GAMES_TAB_FRAGMENT)) {
            return NEW_GAME_PAGE;
        }else
        return 0;
    }
}
