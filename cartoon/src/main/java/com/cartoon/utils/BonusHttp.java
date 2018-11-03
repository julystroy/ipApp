package com.cartoon.utils;

import android.content.Context;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cartoon.CartoonApp;
import com.cartoon.data.Keys;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.listener.ResponseListener;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import cn.com.xuanjiezhimen.R;
import okhttp3.Response;

/**
 * Created by cc on 16-12-12.
 */
public class BonusHttp {

//获取总积分
public static  String getUserBonusPointFromServer() {
        String bonuspoint = "-2";

        try {
            Response response = OkHttpUtils.get().url(String.format(StaticField.URL_USER_LOAD_BONUS_POINT, CartoonApp.getInstance().getUserInfo().getId(),CartoonApp.getInstance().getUserInfo().getToken())).build().execute();

            JSONObject object = new JSONObject(response.body().string().trim());
            bonuspoint =  object.optString(Keys.HTTP_RESULT,"-1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.equals(bonuspoint , "{}")) {
            bonuspoint = "-3";
        }

        return bonuspoint;

    }
//获取他人总积分数
public static  String getOtherUserBonusPointFromServer(int uid) {
        String bonuspoint = "-2";

        try {

            Response response = OkHttpUtils.get().url(String.format(StaticField.URL_USER_LOAD_BONUS_POINT, uid)).build().execute();

            JSONObject object = new JSONObject(response.body().string().trim());
            bonuspoint =  object.optString(Keys.HTTP_RESULT,"-1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.equals(bonuspoint , "{}")) {
            bonuspoint = "-3";
        }

        return bonuspoint;

    }

    //获取总灵石数
    public static  String getUserStoneFromServer() {
        String stone = "-2";
        try {
            Response response = OkHttpUtils.get().url(String.format(StaticField.URL_USER_LOADE_STONES, CartoonApp.getInstance().getUserInfo().getId() , CartoonApp.getInstance().getUserInfo().getToken())).build().execute();

            JSONObject object = new JSONObject(response.body().string().trim());
            stone = object.optString(Keys.HTTP_RESULT, "-1");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.equals(stone , "{}")) {
            stone = "-3";
        }

        return stone;
    }

//发送日常任务
    public static void SendUserPoint(String id,int action_type){
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_USER_POINT)
                .addParams("uid", id)
                .addParams("action_type", action_type+"")
                .build().execute(new BaseCallBack() {
            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Object response) {

            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return null;
            }
        });
    }

    //记录打开app和分享
    public static void SendUser2Point(String id,int action_type){
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_USER2_TASK)
                .addParams("uid", id)
                .addParams("action_type", action_type+"")
                .addParams("token", CartoonApp.getInstance().getToken())
                .build().execute(new BaseCallBack() {
            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(Object response) {

            }

            @Override
            public Object parseNetworkResponse(String response) throws Exception {
                return null;
            }
        });
    }


    public static void ActionVote(final Context context, String essay_id, final ResponseListener listener) {

        showLoadingDialog(context);
        String userId = CartoonApp.getInstance().getUserId();
        String actionId = EasySharePreference.getActionId(context);
        if (actionId == null) {
            ToastUtils.showShort(context,"请从活动界面重新进入");
            return;
        }
        if (userId == null)
            userId = "";

        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_ADD_ACTION_VOTE)
                .addParams("uid", userId)
                .addParams("activityId", actionId)
                .addParams("essayId", essay_id)
                .build().execute(new BaseCallBack<String>() {

            @Override
            public void onLoadFail() {
                hideLoadingDialog();
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(String response) {
                hideLoadingDialog();
                //System.out.println(response+"...............");
                //{"msg":"你今日的投票次数已用完"}
                if (response.contains("你今日的投票次数已用完")) {
                    ToastUtils.showShort(context, "你今日的投票次数已用完");
                    if (listener != null) {
                        listener.onLoadSuccess("2");
                    }
                } else {
                    if (listener != null) {
                        listener.onLoadSuccess("1");
                    }
                }

            }

            @Override
            public String parseNetworkResponse(String response) throws Exception {
                return response;
            }
        });
    }


    public static void showLoadingDialog(Context context) {
        if (dialog == null||dialog.isCancelled()) {
            dialog = new MaterialDialog.Builder(context)
                    .title(R.string.notice)
                    .content(R.string.please_wait)
                    .progress(true, 0)
                    .build();
        }

        dialog.show();
    }

    public static void hideLoadingDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
    private static MaterialDialog dialog;

}
