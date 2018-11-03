package com.cartoon.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.DayQuestionAnswer;
import com.cartoon.data.DayResponse;
import com.cartoon.data.ReportData;
import com.cartoon.data.response.VoteResponse;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.listener.ResponseListener;
import com.cartton.library.utils.ToastUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;

/**
 * Created by apple on 2016/10/19.
 * <p>
 * 完成客戶端和後臺的交互
 */

public class SendToBG {


    //分享
    public static void SendShare() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //增加積fen
                BonusHttp.SendUser2Point(CartoonApp.getInstance().getUserId(), 4);
            }
        }).start();
    }


    //每日答题,获取答案
    public static void getAnswer(final Context context, int id,int position, final ResponseListener listener) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_DAY_QUESTION_GET_ANSWER)
                .addParams("id", id + "")
                .addParams("choice", "0")
                .addParams("position", position+"")
                .build().execute(new BaseCallBack<DayQuestionAnswer>() {

            @Override
            public void onLoadFail() {
                ToastUtils.showShort(context, "获取答案失败");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(DayQuestionAnswer response) {
               // ToastUtils.showShort(context, response.getMsg());
                if (listener!=null)
                    listener.onLoadSuccess("成功");

            }

            @Override
            public DayQuestionAnswer parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, DayQuestionAnswer.class);
            }
        });
    }


    //每日答题，获取下一题
    public static void getNextQuestion( int lastPosition, final ResponseListener listener) {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_NEXT_QUESTION)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("position", lastPosition + "")
                .build().execute(new BaseCallBack<DayResponse>() {

            @Override
            public void onLoadFail() {
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(DayResponse response) {
                //EasySharePreference.setNextQuestion(context,true);
                if (listener != null) {
                    listener.onLoadSuccess("cc");
                }
            }

            @Override
            public DayResponse parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, DayResponse.class);
            }
        });
    }

   /* //不回答问题时调用的参数
    public static void getAnswer( int id, int position) {
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_DAY_QUESTION_GET_ANSWER)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("token", CartoonApp.getInstance().getToken())
                .addParams("id", id+"")
                .addParams("choice","")
                .addParams("position",position+"" )
                .build().execute(new BaseCallBack<DayQuestionAnswer>() {

            @Override
            public void onLoadFail() {

            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(DayQuestionAnswer response) {

            }
            @Override
            public DayQuestionAnswer parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, DayQuestionAnswer.class);
            }
        });
    }*/


    /*
    //举报
    举报者的id
    被举报者的id
    类型（暴力，色情...）
    这条评论的id
    评论的等级（一级评论 二级评论）
    */
    public static void sendReport(final Context context, String id, String type, String commentid, String levelid) {
        GetBuilder getBuilder = BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_REPORT)
                .addParams("uid", CartoonApp.getInstance().getUserId())
                .addParams("bad_uid", id)
                .addParams("core_id", commentid)
                .addParams("type", type);
        if (levelid.equals("4")) {
            getBuilder.addParams("isEssay", "1")
                    .addParams("level_id", "1");
        } else {
            getBuilder.addParams("level_id", levelid);
        }
        getBuilder.build().execute(new BaseCallBack<ReportData>() {

            @Override
            public void onLoadFail() {
                ToastUtils.showShort(context, "举报异常");
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(ReportData response) {

                ToastUtils.showShort(context, response.getMsg());
            }

            @Override
            public ReportData parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response, ReportData.class);
            }
        });
    }
//http://116.228.59.173:8888/book/book/api/action/report?token=36382E42F1A5F1600969E1723DB1FF4D&uid=336&bad_uid=2988&core_id=3242&type=2&level_id=1
//http://116.228.59.173:8888/book/book/api/action/report?token=36382E42F1A5F1600969E1723DB1FF4D&uid=336&bad_uid=3130&core_id=51&type=2&isEssay=1&level_id=1
    /*
    * 领取福利
    * */
    public static void getWelfare(final Context context, int i, final TextView tvReciver, final ImageView rlReceiver){
        BuilderInstance.getInstance().getGetBuilderInstance(StaticField.URL_VOTE_ITEM)
                .addParams("activity_id", i+"")
                .addParams("type", "2")
                .build().execute(new BaseCallBack<VoteResponse>() {
            @Override
            public void onLoadFail() {ToastUtils.showShort(context, "投票失败");}
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(VoteResponse response) {

                ToastUtils.showShort(context, response.getMsg());
                if (!TextUtils.equals(response.getMsg().toString().trim(), "对不起，您目前的等级还不能领取福利，请加速升级哦！")) {

                    tvReciver.setText("已领取");
                    tvReciver.setTextColor(Color.parseColor("#FFCCC3C3"));
                    rlReceiver.setSelected(true);
                }
            }

            @Override
            public VoteResponse parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,VoteResponse.class);
            }
        });
    }

    public static void getSectNextQuestion(boolean isAnswer,int position, int sectWarId,int questionId, String sectionId, final ResponseListener listener) {
        PostFormBuilder postFormBuilder = BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_NEXT_QUESTION)
                .addParams("position", position + "")
                .addParams("sectWarId", sectWarId + "")
                .addParams("questionId", questionId + "")
                .addParams("sectId", sectionId);
        if (!isAnswer)
            postFormBuilder.addParams("choice","0");

            postFormBuilder.build().execute(new BaseCallBack<DayQuestionAnswer>() {
            @Override
            public void onLoadFail() {}
            @Override
            public void onContentNull() {
            }
            @Override
            public void onLoadSuccess(DayQuestionAnswer response) {
                if (listener !=null&&response!=null)
                    listener.onLoadSuccess(response.getCode()+"");

            }

            @Override
            public DayQuestionAnswer parseNetworkResponse(String response) throws Exception {
                return JSON.parseObject(response,DayQuestionAnswer.class);
            }
        });
    }
}
