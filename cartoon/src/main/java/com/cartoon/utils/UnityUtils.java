package com.cartoon.utils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.unity3d.player.UnityPlayer.UnitySendMessage;


/**
 * Created by HeTao on 17-9-22 下午5:56
 */


public class UnityUtils {

    private        UnityCallBack mUnityCallBack;
    private static UnityUtils    unityUtils;


    public static UnityUtils getInstance() {
        synchronized (UnityUtils.class) {
            if (unityUtils == null) {
                unityUtils = new UnityUtils();
            }
            return unityUtils;
        }

    }

    public void setUnityCallBack(UnityCallBack unityCallBack) {
        this.mUnityCallBack = unityCallBack;
    }

    public interface UnityCallBack {
        void startComplete();

        void clickedBackKey();

        void loadDataSetsCompleted();

        void downAssetBundle(String info);

        void takePhoteBacks(String info);

        void audioBacks(String audioPath);
    }

    //---------------------------------------接受Unity发来的消息-----------------------------

    public void from(String json) {
        try {
            final JSONObject jsonObject = new JSONObject(json);
            final String method = jsonObject.optString("Method");
            final int status = jsonObject.optInt("Status");
            switch (method) {
                case "StartComplete":
                    mUnityCallBack.startComplete();
                    break;
                case "EscapeClicked":
                    mUnityCallBack.clickedBackKey();
                    break;
                case "LoadDataSet":
                    mUnityCallBack.loadDataSetsCompleted();
                    break;
                case "DownAssetBundle":
                    String target = jsonObject.optString("target");
                    mUnityCallBack.downAssetBundle(target);
                    break;
                case "ScreenShot":
                    String ImagePath = jsonObject.optString("ImagePath");
                    mUnityCallBack.takePhoteBacks(ImagePath);
                    break;
                case "StopScreenCap":
                    String audioPath = jsonObject.optString("VideoPath");
                    mUnityCallBack.audioBacks(audioPath);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //---------------------------------------发送通知给Unity-----------------------------

    /**
     * 发送消息到unity
     *
     * @param method
     */
    public void onSendMessage(String method, String parameter) {
        //        System.out.println("onSendMessage:" + method);
        UnitySendMessage("InteractObject", method, parameter);
    }


    /**
     * 开启/关闭闪关灯
     */
    public void switchFlash() {
        onSendMessage("SwitchFlash", "");
    }

    /**
     * 切换前后置摄像头
     */
    public void switchCamera() {
        onSendMessage("SwitchCamera", "");
    }


    /**
     * 加载识别图
     *
     * @param json
     */
    public void loadDataSet(String json) {
        onSendMessage("LoadDataSet", json == null ? "" : json);
    }


    /**
     * 设置全屏
     *
     * @param full
     */
    public void setScreen(boolean full) {
        onSendMessage("SetScreen", String.valueOf(full));
    }

    /*
    * 开启毛玻璃效果
    * */

    public void FadeIn() {
        onSendMessage("FadeIn", "");
    }

    /*关闭毛玻璃效果
    * */
    public void FadeOut() {
        onSendMessage("FadeOut", "");
    }

    /*
    * 截屏
    * */
    public void ScreenShot() {
        onSendMessage("ScreenShot", "");
    }


    /*
    * 录屏*/
    public void StartScreenCap(boolean ff){
        if (ff)
        onSendMessage("StartScreenCap", "");
        else
            onSendMessage("StopScreenCap", "");
    }
}
