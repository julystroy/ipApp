package com.cartoon.module.zong;

import com.cartoon.CartoonApp;
import com.cartoon.data.EventMessageCount;
import com.cartoon.data.UserInfo;
import com.cartoon.data.Users;
import com.cartoon.data.message.Message;
import com.cartoon.utils.UserDB;
import com.cartoon.utils.WebsocketUtil;
import com.cartton.library.utils.DebugLog;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import okhttp3.Response;
import okhttp3.WebSocketListener;

/**
 * Created by cc on 18-1-5.
 */
public class ChatClients extends WebSocketListener {
    private  long              time;
    private  okhttp3.WebSocket mWebSocket;

    private String          sectionId;
    private String time2;
    private String content;
    private static boolean recode =false;
    private static int count =1;
    public ChatClients(long time){
        this.time = time;
        UserInfo userInfo = CartoonApp.getInstance().getUserInfo();
        if (userInfo!=null)
        sectionId = userInfo.getSect_id();
    }
    @Override
    public void onOpen(okhttp3.WebSocket webSocket, Response response) {
        mWebSocket = webSocket;
        //webSocket.send("join zongmen1 cc: 从1970/01/01至今已过去%@毫秒");
        webSocket.send("join "+sectionId+" "+time+ "\0");
        DebugLog.i("opened connection"+sectionId);
        //开启消息定时发送
        WebsocketUtil.getInstance().startTask();
        WebsocketUtil.getInstance().STATE =true;
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, String message) {
        if (!message.isEmpty()&&message.equals("\0\0\0\0\0\0\0\0"))
            return;
        DebugLog.i("received: " + message);
        int indexOf = message.indexOf(" ");
        int useId = message.indexOf(":");
        String userId = message.substring(indexOf+1, useId);
        content = message.substring(useId+1, message.length()-1);

        time2= message.substring(0, indexOf);
        Calendar c = Calendar.getInstance();
        long dataTime = Long.parseLong(time2);
        c.setTimeInMillis(dataTime);
        Message message2 = new Message();
        message2.setUid(userId);
        message2.setCreatedAt(c.getTime());
        message2.setText(content);
        message2.setUser(sectionId!=null?sectionId:"");
        UserDB.getInstance().insertDb(message2);
        // messages.add(message);

        if (recode) {
            EventBus.getDefault().post(new Users(message2));
            //宗门ID#发送者ID#时间戳#内容
            String chatId ="";
            if (sectionId!=null) {
                chatId = sectionId;
            }
            WebsocketUtil.getInstance().sendMessage(chatId+"#"+ time+"#"+CartoonApp.getInstance().getUserId()+"#"+content);

        }else {
            count++;
            if (count <= 99)
                EventBus.getDefault().post(new EventMessageCount(count));
        }


    }


    @Override
    public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
        webSocket.send("leave " + sectionId  + "\0");
        webSocket.close(1000, null);
        DebugLog.i("client onClosing" +"code:" + code + " reason:" + reason);
    }

    @Override
    public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
        DebugLog.i("Connection closed by " +" Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        //出现异常会进入此回调
       // webSocket.send("leave " + chat.getSect_id()  + "\0");
       // webSocket.close(0,"bye");

        webSocket.cancel();

//            List<Message> messages = UserDB.getInstance().queryLast();
//            if (messages != null&&messages.size()!=0&&messages.get(0).getCreatedAt()!=null) {
//                WebsocketUtil.getInstance().startWeb(messages.get(0).getCreatedAt().getTime());
//            }else{
//                WebsocketUtil.getInstance().startWeb(System.currentTimeMillis());
//            }

        DebugLog.i("Connection onFailure " +" throwable: " + t + " response: " + response);

    }

    public static int getCount(boolean record){
        recode = record;
        if (record)
            count=1;
        return count;
    }
}
