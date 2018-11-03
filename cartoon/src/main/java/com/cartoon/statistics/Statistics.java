package com.cartoon.statistics;

import android.content.Context;

import com.cartoon.http.StaticField;
import com.cartton.library.utils.DebugLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Locale;

import okhttp3.Call;

public class Statistics {

    public static void reportDownload(Context context, long appId) {
        String url = String.format(Locale.US, "%s/api/apps/download", StaticField.BASE_CXURL);
        DebugLog.d("reportDownload. url = " + url + ", appId = " + appId);

        OkHttpUtils.post().url(url)
                .addParams("id", String.valueOf(appId))
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                DebugLog.d("reportDownload success.");
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                DebugLog.d("reportDownload failed. e = " + e.toString());
            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }
}
