package com.cartoon;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cartoon.data.UserInfo;
import com.cartoon.utils.JPushUtils;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.com.xuanjiezhimen.BuildConfig;
import cn.com.xuanjiezhimen.R;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 极光推送请求的后台服务
 * <p/>
 * Created by David on 16/4/5.
 */
public class CartoonService extends Service {

    private static final String TAG = "JPush";
    public static final String ACTION_JPUSH = "com.medbanks.action.JPUSH";
    private static Thread mThread;

    @Override
    public void onCreate() {

        super.onCreate();
        Log.i(TAG, "onCreate");

        // 极光推送
        JPushInterface.setDebugMode(BuildConfig.DEBUG); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext()); // 初始化 JPush
        JPushInterface.setSilenceTime(this, 0, 0, 23, 59);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        Log.i(TAG, "onStartCommand");
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        if (userInfo != null) {
            Log.d(TAG, "...onStartCommand..." + userInfo.getId());
            setAlias(userInfo.getId());
            String s = "chat" + userInfo.getSect_id();
            setAlias("chat"+userInfo.getSect_id());
        } else {
            setAlias("android");
            setAlias("android");
        }





        if (intent != null && ACTION_JPUSH.equals(intent.getAction())) {
            JPushInterface.resumePush(getApplicationContext());
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        JPushInterface.stopPush(getApplicationContext());
        super.onDestroy();
    }

    /****
     * 设置标签
     ****/
    private void setTag(String tag) {
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(getBaseContext(), R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<>();
        for (String sTagItme : sArray) {
            if (!JPushUtils.isValidTagAndAlias(sTagItme)) {
                Toast.makeText(getBaseContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }

            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }



    /**
     * 设置别名
     ***/
    private void setAlias(String alias) {

        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(getBaseContext(), R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!JPushUtils.isValidTagAndAlias(alias)) {
            Toast.makeText(getBaseContext(), R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));

    }


    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    Log.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };



    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JPushUtils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (JPushUtils.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
}
