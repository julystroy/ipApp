package com.cartoon.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cartoon.CartoonApp;
import com.cartoon.data.UserInfo;
import com.cartoon.data.chat.SectChat;
import com.cartoon.data.chat.SectChatDao;
import com.cartoon.data.game.DaoSession;
import com.cartoon.data.game.DownloadGame;
import com.cartoon.data.game.DownloadGameDao;
import com.cartoon.data.message.Message;
import com.cartoon.data.message.MessageDao;
import com.cartoon.http.BaseCallBack;
import com.cartoon.http.BuilderInstance;
import com.cartoon.http.StaticField;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

import cn.aigestudio.downloader.bizs.DLInfo;

/**
 * Created by cc on 17-12-7.
 */
public class UserDB {

    public static final int STATE_UNKNOWN = DLInfo.STATE_UNKNOWN;
    public static final int STATE_WAITING = DLInfo.STATE_WAITING;
    public static final int STATE_DOWNLOADING = DLInfo.STATE_DOWNLOADING;
    public static final int STATE_STOP = DLInfo.STATE_STOP;
    public static final int STATE_COMPLETED = DLInfo.STATE_COMPLETED;
    public static final int STATE_INSTALLED = DLInfo.STATE_INSTALLED;
    public static final int STATE_FAILED = DLInfo.STATE_FAILED;

    private static MessageDao      messageDao;
    private static  SectChatDao     sectChatDao;
    private static DownloadGameDao downloadGameDao;
    private static  DaoSession      daoSession;
    private        String          sect_id;
    private UserDB() {}
    private static UserDB single=null;

    //静态工厂方法
    public static UserDB getInstance() {
        if (single == null) {
            single = new UserDB();
        }

         daoSession = CartoonApp.getInstance().getDaoSession();
         messageDao  = daoSession.getMessageDao();
         sectChatDao = daoSession.getSectChatDao();
        downloadGameDao = daoSession.getDownloadGameDao();

        return single;
    }
    //宗门成员
    public void insertSect(final SectChat message){
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (sectChatDao.getDatabase()!=null&&querySect(String.valueOf(message.getId()))==null)
                    sectChatDao.insert(message);
            }
        });

    }
    public List<SectChat>  QueryAllSect(){
        Query<SectChat> messageQuery = sectChatDao.queryBuilder().orderAsc(SectChatDao.Properties.Id).build();
        return messageQuery.list();
    }
    public SectChat querySect(String uid){
        List<SectChat> list = null;
        SectChat chat=null;
         list = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().list();
            if (list != null && list.size() != 0) {
                chat = list.get(0);
            }



        return chat;
    }
   //更改用户
    public void updataNickName(final String uid, final String nickName){
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                SectChat unique = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().unique();
                if (unique!=null){
                    unique.setNickname(nickName);
                    sectChatDao.update(unique);
                }
            }
        });

    }

    //更改lv_name
    public void updataLevelName(String uid,String lvName){
        SectChat unique = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().unique();
        if (unique!=null){
            unique.setLv_name(lvName);
            sectChatDao.update(unique);
        }
    }
    //更改rank_name
    public void updataRankName(final String uid, final String rankName){
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                SectChat unique = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().unique();
                if (unique!=null){
                    unique.setRank_name(rankName);
                    sectChatDao.update(unique);
                }
            }
        });

    }
    public void deleteSect(String uid) {
        SectChat unique = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().unique();
        if (unique!=null){
            sectChatDao.delete(unique);
        }
    }


    public void deleteSectTable() {
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (messageDao.getDatabase()!=null)
                    messageDao.deleteAll();
            }
        });

    }
    //更改禁言状态
    public void updataSpeak(String uid,String state){
        SectChat unique = sectChatDao.queryBuilder().where(SectChatDao.Properties.Id.eq(uid)).build().unique();
        if (unique!=null){
            unique.setUser_status(state);
            sectChatDao.update(unique);
        }
    }

    public  void  deleteTable(){
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (sectChatDao.getDatabase() != null) {
                    // sectChatDao.dropTable(sectChatDao.getDatabase(),true);
                    sectChatDao.deleteAll();
                }
            }
        });


    }




    public void insertDb(final Message message){

        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                messageDao.insert(message);
            }
        });

    }

    //聊天
    public List<Message> queryDb(int i){
        SectChat sectChat = querySect(CartoonApp.getInstance().getUserId());
        if (sectChat!=null) {
            sect_id = sectChat.getSect_id();
        }
        List<Message> list = messageDao.queryBuilder().where(MessageDao.Properties.User.eq(sect_id)).where(MessageDao.Properties.Id.between(i, i + 100)).orderAsc(MessageDao.Properties.Id).build().list();
        return list;
    }
    //获取所有的聊天数据
    public List<Message>  QueryAllDb(){
        Query<Message> messageQuery = messageDao.queryBuilder().orderAsc(MessageDao.Properties.Id).build();
        return messageQuery.list();
    }

    //获取聊天最后的一条数据
    public List<Message> queryLast(){
        SectChat sectChat = querySect(CartoonApp.getInstance().getUserId());
        List<Message> list=null;
        if (sectChat!=null) {
             sect_id = sectChat.getSect_id();
        }
        if (sect_id != null) {
            QueryBuilder<Message> limit = messageDao.queryBuilder().where(MessageDao.Properties.User.eq(sect_id)).orderDesc(MessageDao.Properties.Id).limit(1);
                list = limit.list();
        }
        return list;
    }
    //获取用户信息
    public  void buildSectDB() {
        deleteTable();
        BuilderInstance.getInstance().getPostBuilderInstance(StaticField.URL_SECT_CHEAT)
                .build().execute(new BaseCallBack<List<SectChat>>() {
            @Override
            public void onLoadFail() {
            }

            @Override
            public void onContentNull() {

            }

            @Override
            public void onLoadSuccess(List<SectChat> response) {
                if (response != null && response.size() != 0) {
                    SectChat chat = response.get(0);
                    if (chat !=null){
                        UserInfo s = CartoonApp.getInstance().getUserInfo();
                        if (s!=null){
                            s.setSect_id(chat.getSect_id());
                            CartoonApp.getInstance().setUserInfo(s);
                        }
                    }
                    for (int i = 0; i < response.size(); i++) {
                        insertSect(response.get(i));
                    }

                    List<Message> messages = queryLast();
                    if (messages!=null&&messages.size()!=0){
                        Date createdAt = messages.get(0).getCreatedAt();
                        if (createdAt != null) {
                            WebsocketUtil.getInstance().startWeb(createdAt.getTime());//启动宗门聊天
                        }
                    }else {
                        WebsocketUtil.getInstance().startWeb(System.currentTimeMillis() - 10000);//启动宗门聊天
                    }
                }
            }

            @Override
            public List<SectChat> parseNetworkResponse(String response) throws Exception {
                List<SectChat> sectChats = null;
                try{
                   sectChats = JSON.parseArray(response, SectChat.class);
                    return JSON.parseArray(response,SectChat.class);
                }catch (Exception e) {

                }
                return null;
            }
        });
    }


    public DownloadGame findGameDownload(String url) {
        List<DownloadGame> list = downloadGameDao.queryBuilder()
                .where(DownloadGameDao.Properties.DownloadUrl.eq(url))
                .list();
        Log.d("DownloadRecord", "find url:" + url + ", result:" +list);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else return null;
    }

    public DownloadGame findGameDownloadByPackageName(String packageName) {
        List<DownloadGame> list = downloadGameDao.queryBuilder()
                .where(DownloadGameDao.Properties.PackageName.eq(packageName))
                .list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else return null;
    }

    public DownloadGame getGameDownload(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        DownloadGame record = findGameDownload(url);
        if (record == null) {
            record = new DownloadGame();
            record.setDownloadUrl(url);
        }
        return record;
    }

    public void saveGameDownload(DownloadGame game) {
        long nowTime = System.currentTimeMillis();
        if (findGameDownload(game.getDownloadUrl()) != null) {
            game.setUpdatedAt(nowTime);
            downloadGameDao.update(game);
            Log.d("DownloadRecord", "update record:" + game.getAppName());
        } else {
            game.setCreatedAt(nowTime);
            game.setUpdatedAt(nowTime);
            downloadGameDao.insert(game);
            Log.d("DownloadRecord", "create record:" + game.getAppName());
        }
    }

    public List<DownloadGame> findAllGameDownload() {
        List<DownloadGame> list = downloadGameDao.queryBuilder()
                .orderDesc(DownloadGameDao.Properties.UpdatedAt)
                .list();
        if (list != null && list.size() > 0) {
            return list;
        } else return null;
    }

}
