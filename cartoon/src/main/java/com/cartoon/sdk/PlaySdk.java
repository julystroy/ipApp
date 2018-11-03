package com.cartoon.sdk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cartoon.account.AccountHelper;
import com.cartoon.data.HangupDeviceResponse;
import com.cartoon.data.Keys;
import com.cartoon.data.response.EmptyResponse;
import com.cartoon.http.StaticField;
import com.cartoon.module.changxian.download.DownloadManager;
import com.cartoon.uploadlog.UploadLog;
import com.cartoon.utils.UIHelper;
import com.cartton.library.utils.DebugLog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.com.xuanjiezhimen.R;
import cn.idianyun.streaming.DianyunSdk;
import cn.idianyun.streaming.data.HubInfo;
import cn.idianyun.streaming.data.LaunchInfo;
import cn.idianyun.streaming.listener.DYSdkListener;
import cn.idianyun.streaming.volley.GsonRequest;
import cn.idianyun.streaming.volley.VolleySingleton;

public class PlaySdk implements DYSdkListener {
    private static PlaySdk mInstance;

    private long               mAppId;
    private LaunchInfo         mLaunchInfo;
    private Context            mContext;
    private Activity           mShareActivity;
    private OnDownloadListener mOnDownloadListener;
    private OnPlayEndListener  mOnPlayEndListener;

    private static final int START_PLAY = 0;
    private static final int END_PLAY = 1;

    private Timer mBackupTimer;
    private static final int HISTORY_BACKUP_DURATION = 5 * 1000; // 5s上报一次历史记录
    private long mPrePlayTime = 0; //上次获取试玩时间
    private Handler mHandler = new Handler();

    private boolean mShowDialog;

    private PlaySdk() {
    }

    public static PlaySdk getInstance() {
        if (mInstance == null) {
            mInstance = new PlaySdk();
        }
        return mInstance;
    }

    public void launch(Context context, long id, LaunchInfo launchInfo, OnDownloadListener listener) {
        mContext = context.getApplicationContext();
        mAppId = id;
        mLaunchInfo = launchInfo;
        mOnDownloadListener = listener;

        try {
            DianyunSdk.getInstance().launch((Activity) context, this, launchInfo);
        } catch (UnsupportedOperationException e) {
            showUnsupportDialog(mContext, mContext.getString(R.string.hangup_play_unsupported));
        }
    }

    public void launch(Context context, long id, LaunchInfo launchInfo, OnDownloadListener listener, OnPlayEndListener onPlayEndListener) {
        mOnPlayEndListener = onPlayEndListener;
        launch(context, id, launchInfo, listener);
    }

    /**
     * @param context
     * @param hangUpId 云挂机按 1:游戏 进入试玩
     */
    public void launch(final Context context, final int hangUpId) {
        launch(context, hangUpId, 1);// 1:游戏， 2:辅助
    }

    public void launch(final Context context, final int hangUpId, final int appType) {
        if (mShowDialog) return;

        mContext = context.getApplicationContext();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        mShowDialog = true;

        String url = String.format("%s/api/devices", StaticField.BASE_CXURL); //设备操作
        JSONObject params = new JSONObject();
        try {
            params.put("operation", 1);// 0:分配设备， 1:直连设备， 2:释放设备
            if (hangUpId > 0) {
                params.put("hangUpId", hangUpId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GsonRequest<HangupDeviceResponse> gsonRequest = new GsonRequest<HangupDeviceResponse>(Request.Method.POST, url, params.toString(), HangupDeviceResponse.class, new Response.Listener<HangupDeviceResponse>() {
            @Override
            public void onResponse(HangupDeviceResponse response) {
                progressDialog.dismiss();
                mShowDialog = false;

                if (response.data != null) {
                    LaunchInfo launchInfo = new LaunchInfo();
                    HubInfo hubInfo = new HubInfo();
                    hubInfo.ip = response.data.ip;
                    hubInfo.port = response.data.port;
                    hubInfo.session = response.data.session;
                    hubInfo.app = response.data.app;
                    if (appType == 2) {// 1:游戏， 2:辅助
                        hubInfo.app = null;
                    }
                    launchInfo.hubInfo = hubInfo;
                    try {
                        DianyunSdk.getInstance().launch((Activity) context, PlaySdk.this, launchInfo);
                    } catch (UnsupportedOperationException e) {
                        showUnsupportDialog(mContext, mContext.getString(R.string.hangup_play_unsupported));
                    }
                } else {
                    UIHelper.showToast(context, "连接设备失败稍后重试");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                mShowDialog = false;

                UIHelper.showToast(context, "连接设备失败稍后重试");
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }

    private void showUnsupportDialog(Context context, String msg) {
        AlertDialog alert = (new AlertDialog.Builder(context, R.style.CXAlertDialogStyle)).setMessage(msg).setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    @Override
    public void onStart() {
        uploadHistory(START_PLAY, 0);
        mPrePlayTime = 0;
        startBackupTimer();
        UploadLog.uploadGamePlayButtonClickLog(UploadLog.PLAY_BUTTON, mAppId, mContext);
    }

    @Override
    public void onStop() {
        endPlay();
    }

    /**
     * @param url
     * @param pkgName
     * @param btnId   点击按钮id, 0:浮动菜单下载,1:提示浮层下载,2:结束浮层下载
     */
    @Override
    public void onDownloadClick(String url, String pkgName, int btnId) {
        DebugLog.d("onDownloadClick. url = " + url + ", pkgName = " + pkgName);

        DownloadManager.getInstance().start(url, String.format(Locale.US, "%s.apk", pkgName));
        if (mOnDownloadListener != null) {
            mOnDownloadListener.onDownload(url, pkgName);
        }

        DebugLog.d("UploadLog onDownloadClick. url = " + url + ", pkgName = " + pkgName);
        UploadLog.uploadGamePlayButtonClickLog(UploadLog.DOWNLOAD_BUTTON, mAppId, mContext);
    }

    @Override
    public void onShare(Activity activity, int index) {
        mShareActivity = activity;

        String url = String.format("%s%d", StaticField.SHARE_URL, mAppId);
        UMWeb web = new UMWeb(url);
        web.setTitle(String.format(activity.getResources().getString(R.string.share_title), mLaunchInfo.appName));//标题
        web.setDescription(mLaunchInfo.intro);//描述
        UMImage thumb = new UMImage(activity, mLaunchInfo.logo);
        web.setThumb(thumb);

        SHARE_MEDIA[] shareMedias = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
        if (index < shareMedias.length) {
            new ShareAction(activity).setPlatform(shareMedias[index])
                    .withMedia(web)
                    .setCallback(new ShareListener(activity))
                    .share();
        }
    }

    @Override
    public void onShareComplete(int requestCode, int resultCode, Intent intent) {
        DebugLog.d("onShareComplete");
        UMShareAPI.get(mShareActivity).onActivityResult(requestCode, resultCode, intent);
    }

    private static class ShareListener implements UMShareListener {
        private WeakReference<Activity> mContext;
        private ProgressDialog mProgressDialog;
        private Resources mResources;

        public ShareListener(Activity context) {
            super();
            mContext = new WeakReference<Activity>(context);
            //mProgressDialog = new ProgressDialog(context);
            mResources = context.getResources();
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
            DebugLog.d("start share");
            //SocializeUtils.safeShowDialog(mProgressDialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            DebugLog.d("get result");
            Toast.makeText(mContext.get(), mResources.getString(R.string.share_success), Toast.LENGTH_LONG).show();
            //SocializeUtils.safeCloseDialog(mProgressDialog);
            Activity activity = mContext.get();
            if (activity != null) {
                activity.finish();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            DebugLog.d("get error");
            //SocializeUtils.safeCloseDialog(mProgressDialog);

            String[] splits = t.getLocalizedMessage().split(mResources.getString(R.string.error_info));
            splits = splits[1].split(" ");
            String msg = mResources.getString(R.string.share_error) + splits[0];
            UIHelper.showToast(mContext.get(), msg, Toast.LENGTH_SHORT);
            DebugLog.d("share fail, because: " + t.getLocalizedMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            DebugLog.d("cancel share");
            //SocializeUtils.safeCloseDialog(mProgressDialog);
        }
    }

    /**
     * 上报试玩记录时长
     *
     * @param type     0开始 1结束
     * @param duration 时长，单位秒
     */
    private void uploadHistory(final int type, final long duration) {
        String url = String.format("%s/api/apps/play", StaticField.BASE_CXURL);
        DebugLog.d("uploadHistory:" + duration);
        JSONObject params = new JSONObject();
        try {
            params.put("id", mAppId);
            params.put("type", type);
            params.put("playType", 1);
            if (type == END_PLAY) {
                params.put("duration", duration);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugLog.d("upload play history = " + url + ", parm = " + params);
        GsonRequest<EmptyResponse> request = new GsonRequest<EmptyResponse>(Request.Method.POST, url, params.toString(), EmptyResponse.class, new Response.Listener<EmptyResponse>() {
            @Override
            public void onResponse(EmptyResponse response) {
                if (type == END_PLAY) {
                    //刷新红点
                    AccountHelper.getLatestUserInfo(mContext);
                }
                DebugLog.d("upload play history " + (type == START_PLAY ? "play" : "end") + " success.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                DebugLog.d("upload play history " + (type == START_PLAY ? "play" : "end") + " fail: " + error.toString());
            }
        });
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    private void sendPlayEndBroadcast(long duration) {
        Intent it = new Intent(Keys.SEND_PLAY_END_WITH_DURATION_BROADCAST);
        it.putExtra(Keys.EXTRA_APP_ID, mAppId);
        it.putExtra(Keys.EXTRA_DURATION, duration);
        it.putExtra(Keys.EXTRA_PLAY_TYPE, 1);
        mContext.sendBroadcast(it);
    }

    public interface OnDownloadListener {
        void onDownload(String url, String pkgName);
    }

    public interface OnPlayEndListener {
        void onPlayEnd();
    }

    private void startBackupTimer() {
        if (mBackupTimer == null) {
            mBackupTimer = new Timer("timer");
            mBackupTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            savePlayHistory();
                        }
                    });
                }
            }, HISTORY_BACKUP_DURATION, HISTORY_BACKUP_DURATION);
        }
    }

    private void stopBackupTimer() {
        if (mBackupTimer != null) {
            mBackupTimer.cancel();
            mBackupTimer = null;
        }
        savePlayHistory();
    }

    private void savePlayHistory() {
        DebugLog.d("savePlayHistory");
        long duration = getPlayDuration();
        if (duration != mPrePlayTime) {
            uploadHistory(END_PLAY, duration - mPrePlayTime);
        }
        mPrePlayTime = duration;
    }

    private long getPlayDuration() {
        return DianyunSdk.getInstance().getPlayDuration() / 1000;
    }

    private void endPlay() {
        mHandler.removeCallbacksAndMessages(null);
        stopBackupTimer();
        sendPlayEndBroadcast(getPlayDuration());
        if (mOnPlayEndListener != null) {
            mOnPlayEndListener.onPlayEnd();
        }
        mAppId = -1;
        mLaunchInfo = null;
        mShareActivity = null;
        mOnDownloadListener = null;
        mOnPlayEndListener = null;
    }

//    @Override
    public void onVipExpired(Activity activity) {

    }
}
