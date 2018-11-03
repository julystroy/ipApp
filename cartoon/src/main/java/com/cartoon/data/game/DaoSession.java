package com.cartoon.data.game;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.cartoon.data.game.DownloadGame;
import com.cartoon.data.chat.SectChat;
import com.cartoon.data.message.Message;

import com.cartoon.data.game.DownloadGameDao;
import com.cartoon.data.chat.SectChatDao;
import com.cartoon.data.message.MessageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downloadGameDaoConfig;
    private final DaoConfig sectChatDaoConfig;
    private final DaoConfig messageDaoConfig;

    private final DownloadGameDao downloadGameDao;
    private final SectChatDao sectChatDao;
    private final MessageDao messageDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downloadGameDaoConfig = daoConfigMap.get(DownloadGameDao.class).clone();
        downloadGameDaoConfig.initIdentityScope(type);

        sectChatDaoConfig = daoConfigMap.get(SectChatDao.class).clone();
        sectChatDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        downloadGameDao = new DownloadGameDao(downloadGameDaoConfig, this);
        sectChatDao = new SectChatDao(sectChatDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);

        registerDao(DownloadGame.class, downloadGameDao);
        registerDao(SectChat.class, sectChatDao);
        registerDao(Message.class, messageDao);
    }
    
    public void clear() {
        downloadGameDaoConfig.getIdentityScope().clear();
        sectChatDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
    }

    public DownloadGameDao getDownloadGameDao() {
        return downloadGameDao;
    }

    public SectChatDao getSectChatDao() {
        return sectChatDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

}
