/*
package com.cartoon.bean;

import com.cartoon.greendao.BangaiDownDao;
import com.cartoon.greendao.DaoSession;

*/
/**
 * Created by cc on 16-12-6.
 *//*

public class BangaiDownLoadList {

    private transient DaoSession daoSession;

    private transient BangaiDownDao myDao;
    private String catalog;
    private String coverpic;
    private String id;
    private String title;
    private String content;

    public BangaiDownLoadList() {}

    public BangaiDownLoadList(String id, String title, String coverpic, String content, String catalog) {
        this.id = id;
        this.title = title;
        this.coverpic = coverpic;
        this.content = content;
        this.catalog = catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    public String getCatalog() {
        return catalog;
    }

    public void setCoverPic(String coverPic) {
        this.coverpic = coverPic;
    }
    public String getCoverPic() {
        return coverpic;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }


    */
/** called by internal mechanisms, do not call yourself. *//*

    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBangaiDownDao() : null;
    }
    */
/** Resets a to-many relationship, making the next get call to query for a fresh result. *//*

    public synchronized void resetContent() {
        content = null;
    }


}*/
