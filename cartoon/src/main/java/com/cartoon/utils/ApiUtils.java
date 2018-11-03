package com.cartoon.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cartoon.CartoonApp;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.listener.ApiQuestListener;
import com.cartton.library.utils.DebugLog;
import com.cartton.library.utils.ToastUtils;
import com.umeng.socialize.media.UMImage;
import com.zhy.http.okhttp.builder.GetBuilder;

import java.util.List;

import cn.com.xuanjiezhimen.R;
import simbest.com.sharelib.IShareCallback;
import simbest.com.sharelib.ShareModel;
import simbest.com.sharelib.ShareUtils;

/**
 * 接口请求工具类
 * <p/>
 * Created by David on 16/6/26.
 */
public class ApiUtils {

    /**
     * 请求下载列表
     *
     * @param collects
     * @param callback
     */

    public static void requestDownloadList(List<String> collects, BaseCallBack callback) {
        if (collects == null)
            return;

        GetBuilder builder = BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_CARTOON_DOWNLOAD);

        StringBuilder stringBuilder = new StringBuilder();

        for (String key : collects) {
            stringBuilder.append(key);
            stringBuilder.append(",");
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");


        builder.addParams("collects", stringBuilder.toString());

        builder.build().execute(callback);
    }


    /**
     * 点赞
     *
     * @param target_id 资源ID。
     * @param type      类型
     */
    public static void approveTarget(String target_id, int type, final ApiQuestListener listener) {
        if (!CartoonApp.getInstance().isLogin(CartoonApp.getInstance())) {
            return;
        }

        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_APP_APPROVE)
                .addParams("target_id", String.valueOf(target_id))
                .addParams("type", String.valueOf(type))
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                if (listener != null) {
                    listener.onLoadFail();
                }
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                ToastUtils.showShort(CartoonApp.getInstance(), "点赞成功!");
                if (listener != null) {
                    listener.onLoadSuccess(response);
                }
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }

    public static void uploadPlayNum(String id) {
        BuilderInstance.getInstance()
                .getGetBuilderInstance(StaticField.URL_LISTENER_ADD_NUM)
                .addParams("id", id)
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                DebugLog.i("...uploadPlayNum...fail..");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                DebugLog.i("...uploadPlayNum...success..");
            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }

        });
    }


    public static String spliteURL(String URL) {
        if (URL == null || TextUtils.isEmpty(URL) || TextUtils.equals("NULL", URL) || TextUtils.equals("null", URL)) {
            return "";
        }

        String[] noHostURLs = URL.split("\\.com/");

        if (noHostURLs.length > 1) {
            return noHostURLs[1];
        } else {

            String[] noHostQTURLs = URL.split("qingting\\.fm/");
            if (noHostQTURLs.length > 1) {
                return noHostQTURLs[1].split("\\?deviceid")[0];
            } else {
                return "";
            }

        }

    }

    public static void share(final Activity context, String content, String imageUrl, String source, String id) {
        share(context, content, imageUrl, source, id, "");
    }

    public static void share(final Activity context, String content, String imageUrl, String source, String id, String audioSrc) {
        ShareModel model = new ShareModel();
        model.setTitle("凡人-" + source);
        model.setContent(content);
        String url;
        if ("仙界连载".equals(source)) {
            url = StaticField.URL_SHARE_EXPOUND + id;
        } else if ("凡人纪年".equals(source)) {
            url = StaticField.URL_SHARE_BANGAI + id;
            Log.i("xxx", url);
        }  else if ("追听".equals(source)) {
            url = String.format(StaticField.URL_SHARE_AUDIO, content, "qn", spliteURL(imageUrl), spliteURL(audioSrc), id);
            url = removeNullImgUrl(url);
            Log.i("xxx", url);
        }else if ("征文".equals(source)){
            url =StaticField.HTML_BASE_URL+ "/essay/goShareDetail?essayId="+ id;
        } else if ("活动规则".equals(source)) {
            url = StaticField.HTML_BASE_URL + "/activity/goShareActivityRule?dataid="+id;
            Log.i("xxx", url);
        }
//        else if ("图片".equals(source)) {
//            UMImage image = new UMImage(context, BitmapFactory.decodeFile(imageUrl));
//            image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//            image.compressFormat = Bitmap.CompressFormat.PNG;
//            model.setImageMedia(image);
//            url ="";
//        }


/*
        else if ("听书".equals(source)) {
            url = String.format(StaticField.URL_SHARE_AUDIO, content, "qt", spliteURL(imageUrl), spliteURL(audioSrc), id);
            url = removeNullImgUrl(url);
            Log.i("xxx", url);
        }

        */

        else if ("凡人次元".equals(source)) {

            url = String.format(StaticField.URL_SHARE_NEWBASE, 7, id);

        }  else if ("凡人精品".equals(source)) {

            url = String.format(StaticField.URL_SHARE_NEWBASE, 9, id);

        } else {
            if (source.equals("活动")) {
                model.setTitle("凡人-" + "活动");
                // int type = NewBaseStaticField.apiNumByType[Integer.valueOf(source)];
                url = String.format(StaticField.URL_SHARE_NEWBASE, 0, id);
            } else {
                url = "http://xjzm.mopian.tv/share/share.html";
            }
        }
        model.setTargetUrl(url);
        if (TextUtils.isEmpty(imageUrl) || imageUrl.endsWith("NULL")) {
            model.setImageMedia(new UMImage(context, R.mipmap.ic_launcher));
        } else {
            model.setImageMedia(new UMImage(context, imageUrl));
        }


        new ShareUtils(context,CartoonApp.getInstance().getUserId(),CartoonApp.getInstance().getToken()).share(model, new IShareCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
               // SendToBG.SendShare();
            }

            @Override
            public void onFaild() {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static String removeNullImgUrl(String url) {

        url = url.replace("&img=&src", "&src");

        return url;
    }

}
