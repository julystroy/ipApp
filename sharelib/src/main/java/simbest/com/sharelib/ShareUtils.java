package simbest.com.sharelib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.Map;

/**
 * Created by zhangxiaolong on 2016/4/14.
 */
public class ShareUtils {
    private UMShareAPI mShareAPI = null;
    private Activity c;
    private ILoginCallback loginCallback;
    private IShareCallback shareCallback;
    private String id;
    private String token;

    public ShareUtils(Activity context,String id ,String token) {
        this.c = context;
        this.id = id;
        this.token = token;
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(context);
        }

    }

    public void login(SHARE_MEDIA platform, ILoginCallback callback) {
        this.loginCallback = callback;
        Log.d("zxl", "444444444");
        mShareAPI.doOauthVerify(c, platform, umAuthListener);
    }


    public void share(final ShareModel model, IShareCallback shareCallback) {
        this.shareCallback = shareCallback;
        ShareBoardConfig config = new ShareBoardConfig();
        config.setTitleVisibility(false);
        config.setCancelButtonText("取消");
        config.setShareboardBackgroundColor(Color.parseColor("#ffffff"));
        config.setIndicatorVisibility(false);
        config.setStatusBarHeight(20);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR,10);
        new ShareAction(c).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                            UMWeb web = new UMWeb(model.getTargetUrl());
                            web.setTitle(model.getTitle());
                            web.setDescription(model.getContent());
                            web.setThumb(model.getImageMedia());
                            new ShareAction(c).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(umShareListener)
                                    .share();
                    }
                }).open(config);

    }

    /**********
     * 内部方法
     ***********/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.d("zxl", "登陆授权获取成功" + data);
            if (data != null) {
                mShareAPI.getPlatformInfo(c, platform, getInfoListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.d("zxl", "000000000");
            loginCallback.onFaild("授权失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.d("zxl", "111111111");
            loginCallback.onCancel();
        }
    };
    private UMAuthListener getInfoListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.d("zxl", "获取用户信息成功" + data);
            if (data != null) {
                loginCallback.onSuccess(data);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.d("zxl", "22222222");
            loginCallback.onFaild("获取用户信息失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.d("zxl", "33333333");
            loginCallback.onCancel();
        }
    };
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendShare.requestPost(id,token);
                }
            }).start();
            shareCallback.onSuccess();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            t.printStackTrace();
            shareCallback.onFaild();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            shareCallback.onCancel();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

}
