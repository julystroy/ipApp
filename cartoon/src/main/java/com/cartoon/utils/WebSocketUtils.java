/*
package com.cartoon.utils;

import android.os.Handler;

import com.cartoon.CartoonApp;
import com.cartoon.data.chat.SectChat;
import com.cartoon.data.message.Message;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;
import com.cartoon.module.zong.ChatClient;
import com.cartoon.websocket.WebSocket;
import com.cartton.library.utils.ToastUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class WebSocketUtils  {
    private static WebSocketUtils instance;
    private boolean isAutoPlay;

    private WebSocketUtils (){}

    private List<String> messages = new ArrayList<>();
    private SectChat sectChat;

    private WeakHandler handler = new WeakHandler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                websocket.send( "keeplive\0" );
                handler.postDelayed(task, 20000);
            }
        }
    };
    public static WebSocketUtils getInstance() {
        if (instance == null) {
            instance = new WebSocketUtils();
        }
        return instance;
    }

   private ChatClient websocket =null;
    //启动websocket  parm；用户宗门  用户id   以及上次信息毫秒值
    public void startWeb(final long time) throws InterruptedException{
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (websocket != null) {
                        sendLeave();
                    }
                    websocket = new ChatClient(new URI( "ws://im.mopian.tv:50018" ));
                    try {
                        if( !websocket.connectBlocking() ) {
                            System.err.println( "Could not connect to the server." );
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
                if (websocket!=null&&sectChat!=null&&sectChat.getSect_id()!=null){
                    websocket.send("join "+sectChat.getSect_id()+" "+time+ "\0");
                }
            }
        }).start();

    }

    public boolean isConnect() {
        boolean b = false;
        if (websocket != null) {
                b = !websocket.isClosed();
            return b;
        } else {
            List<Message> messages = UserDB.getInstance().queryLast();
            if (messages!=null&&messages.size()!=0&&messages.get(0)!=null)
                try {
                    startWeb(messages.get(0).getCreatedAt().getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return true;
        }

    }

    //发送信息
    public void sendMessage(final String message, final String userId){
        sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
        if (sectChat != null && sectChat.getSect_id() != null) {
            if (websocket.getReadyState()!=WebSocket.READYSTATE.OPEN ) {
                messages.add(message);
                websocket.send("msg " + sectChat.getSect_id() + " " + userId + ":" + "" + "\0");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (messages != null && messages.size() != 0) {
                            for (int i = 0; i < messages.size(); i++) {
                                websocket.send("msg " + sectChat.getSect_id() + " " + userId + ":" + messages.get(i) + "\0");
                            }
                            messages.clear();
                        }

                    }
                },1000);
            } else {
              websocket.send("msg " + sectChat.getSect_id() + " " + userId + ":" + message + "\0");
            }


        } else {
            ToastUtils.showShort(CartoonApp.getInstance(),"已不在宗门内");
        }
       // websocket.send( "msg 76 "+userId+":"+message+"\0" );
    }


    public void  sendLeave(){
        sectChat = UserDB.getInstance().querySect(CartoonApp.getInstance().getUserId());
        if (sectChat != null && sectChat.getSect_id() != null&&websocket!=null) {
            websocket.send( "leave " + sectChat.getSect_id()  + "\0" );
            websocket=null;
            handler.removeCallbacksAndMessages(null);
        }

    }

    public void keepLive(boolean isAutoPlay){
        this.isAutoPlay =isAutoPlay;
        if (isAutoPlay) {
            handler.removeCallbacks(task);
            handler.postDelayed(task, 20000);
        }

    }

    public void stopHandler(){

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
*/
