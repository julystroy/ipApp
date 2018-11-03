package com.cartoon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cartoon.CartoonApp;
import com.cartoon.data.chat.SectChat;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.zong.ChatClients;
import com.cartton.library.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * Created by cc on 18-1-5.
 */
public class WebsocketUtil {
    private static WebsocketUtil instance;
    private        WebSocket     webSocket;
    private        SectChat      sectChat;
    private  Timer             mTimer;
    private static OkHttpClient client;
    private static Request request;

    public boolean STATE =true;
    public static WebsocketUtil getInstance() {
        if (instance == null) {
            instance = new WebsocketUtil();
            //新建client
             client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
            //构造request对象
             request = new Request.Builder()
                    .url("ws://im.mopian.tv:50018")
                    .build();
        }
        return instance;
    }

    //开启join
    public void startWeb(long time){
        if (!isNetworkConnected(CartoonApp.getInstance())) return;
        if (webSocket != null) {
            webSocket.cancel();
            webSocket =null;
        }
        if (mTimer != null) {//避免保活信息累加
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }


        if (STATE) {
            mTimer= new Timer();
            webSocket = client.newWebSocket(request, new ChatClients(time));
            STATE = false;
            if (messages != null) {//发送没发出去的信息
                webSocket.send("msg " + sectChat.getSect_id() + " " + uid + ":" + messages + "\0");
                messages =null;
                uid = null;
            }
        }
    }

    //发送信息
    private String messages =null;
    private String uid =null;
    public void sendMessage(final String message, final String userId){
        sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
        if (webSocket!=null&&sectChat != null && sectChat.getSect_id() != null) {
             isSend = webSocket.send("msg " + sectChat.getSect_id() + " " + userId + ":" + message + "\0");
            if (!isSend && STATE) {
                messages =message;
                uid  = userId;
                startWeb(System.currentTimeMillis());
            }

        } else {
            if (webSocket == null && sectChat != null) {
                messages =message;
                uid  = userId;
                startWeb(System.currentTimeMillis());
            }else
            ToastUtils.showShort(CartoonApp.getInstance(),"已不在宗门内");
        }
    }
    public void  sendLeave(){
        sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
        if (webSocket!=null&&sectChat != null && sectChat.getSect_id() != null&&webSocket!=null) {
            webSocket.send( "leave " + sectChat.getSect_id()  + "\0" );
            webSocket.close(1000,"bye");
            webSocket=null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }

    }

    boolean isSend = true;
    //每20秒发送一条消息
    public   void startTask(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(webSocket == null) return;
                 isSend = webSocket.send("keeplive\0");
                System.out.println("yy"+isSend);
                if (!isSend && STATE) {
                    startWeb(System.currentTimeMillis());
                }
            }
        };
        mTimer.schedule(timerTask, 0, 20000);
    }


    //检查网络是否连接
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public void sendMessage(String msg) {
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_SEND_CHEAT_CONTROL)
                .addParams("content",msg)
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

}
