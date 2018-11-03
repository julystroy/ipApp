package com.cartoon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cartoon.data.EventNewMessage;
import com.cartoon.data.EventPlay;
import com.cartoon.data.chat.SectChat;
import com.cartoon.module.splash.SplashActivity;
import com.cartoon.utils.EasySharePreference;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.WebsocketUtil;
import com.cartoon.view.DefineToast;
import com.cartton.library.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class CartoonReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
            //            DefineToast.showShort(context, regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));


            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            int position = 0;

            try {
                JSONObject jsonObject = new JSONObject(extras);
                position = jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//[宗门聊天]2988#2#皮皮虾#http://image.mopian.tv/upload/avatar/user/2017-03/20/2988_1489997209784.jpg#长老#结丹后期#type#sectId
//type:0 正常状态   1  加入   2 禁言  3解除禁言   4 踢出宗门  5  解散宗门
            if (position == 10) {
                if (content != null && content.startsWith("[弹窗]")) {
                    //任务提示栏
                    // 获得广播发送的数据  content.split("\\[弹窗]")[1]
                    DefineToast.createToastConfig().ToastShow(context, content.split("\\[弹窗]")[1]);
                    Log.d(TAG, "[MyTask] 弹窗");
                } else if(content != null && content.startsWith("[宗门聊天]")){
                    String message = content.split("\\[宗门聊天]")[1];
                    if (message != null) {
                        String[] split = message.split("#");
                        if (split.length ==8) {
                            if (split[6].equals("2")) {
                                WebsocketUtil.getInstance().sendMessage("###"+split[2]+"已被禁言",split[0]);
                                UserDB.getInstance().updataSpeak(split[0],"2");
                                EventBus.getDefault().post(new EventPlay(true));
                            }else if(split[6].equals("3")){
                                WebsocketUtil.getInstance().sendMessage("###"+split[2]+"已解除禁言",split[0]);
                                UserDB.getInstance().updataSpeak(split[0],"0");
                            }else if (split[6].equals("1")){
                                SectChat chat = new SectChat();
                                String s = split[0];
                                chat.setId(Long.valueOf(s));
                                chat.setUser_status("0");
                                chat.setAvatar(split[3]);
                                chat.setRank_name(split[4]);
                                chat.setLv_name(split[5]);
                                chat.setNickname(split[2]);
                                chat.setSect_id(split[7]);
                                //WebSocketUtils.getInstance().sendMessage("###"+split[2]+"已加入宗门",split[0]);
                                UserDB.getInstance().insertSect(chat);
                            }else if(split[6].equals("4")){
                               // UserDB.getInstance().updataSpeak(split[0],"2");
                                UserDB.getInstance().deleteSect(split[0]);
                                WebsocketUtil.getInstance().sendMessage("###"+split[2]+"已被踢出宗门",split[0]);
                                if (CartoonApp.getInstance().getUserId().equals(split[0]))
                                    WebsocketUtil.getInstance().sendLeave();
                                EventBus.getDefault().post(new EventPlay(false));
                            }else{
                                ToastUtils.showShort(context,"宗门已解散");
                                UserDB.getInstance().deleteSectTable();
                                UserDB.getInstance().deleteTable();
                            }
                        }
                    }
                  //  ToastUtils.showShort(context,message);
                }
                else{
                    EventBus.getDefault().post(new EventNewMessage(true));
                    EasySharePreference.setHasNewMessage(context);
                    //记录推送信息数目
                    int myMessageCount = EasySharePreference.getMyMessageCount(context);
                    myMessageCount += 1;
                    EasySharePreference.setMyMessageCount(context, myMessageCount);
                }
            } else {

                JPushLocalNotification notification = new JPushLocalNotification();
                notification.setTitle("凡人修仙传");
                notification.setContent(content);
                notification.setExtras(extras);
                JPushInterface.addLocalNotification(context, notification);
            }
            //            DefineToast.showShort(context, "...消息..." + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            EventBus.getDefault().post(new EventNewMessage(true));
            EasySharePreference.setHasNewMessage(context);

           /* //记录推送信息数目
            int myMessageCount = EasySharePreference.getMyMessageCount(context);
            myMessageCount += 1;
            EasySharePreference.setMyMessageCount(context, myMessageCount);
*/

            //            DefineToast.showShort(context, "通知..." + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            Log.d(TAG, "[MyReceiver] content=" + content);
            context.startActivity(new Intent(context, SplashActivity.class).putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}

