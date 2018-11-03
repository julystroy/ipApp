package com.cartoon.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wusue on 17/5/16.
 */

public abstract class CommentInfo implements Serializable {
    public long id;
    public String avatar;
    public String name;
    public long userId;
    public boolean isVip;
    public String content;
    public long publishedAt = new Date().getTime() / 1000; //与服务器时间统一
    public long usec;
    public long likes;
    public boolean liked; //我是否已经点赞了

    public int rating;
    public long duration; //秒

    public void setSpeaker(User user) {
        avatar = user.avatar;
        name = user.name;
        userId = user.id;
        isVip = user.isVIP();
    }
}
