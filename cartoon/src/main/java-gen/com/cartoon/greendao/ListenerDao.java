/*
package com.cartoon.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.cartoon.bean.Listener;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
*/
/**
 * DAO for table "LISTENER".
*//*

public class ListenerDao extends AbstractDao<Listener, String> {

    public static final String TABLENAME = "LISTENER";

    */
/**
     * Properties of entity Listener.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    *//*

    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Approve_num = new Property(1, String.class, "approve_num", false, "APPROVE_NUM");
        public final static Property Remote_connect = new Property(2, String.class, "remote_connect", false, "REMOTE_CONNECT");
        public final static Property Is_approve = new Property(3, Integer.class, "is_approve", false, "IS_APPROVE");
        public final static Property Book_id = new Property(4, String.class, "book_id", false, "BOOK_ID");
        public final static Property Listener_num = new Property(5, String.class, "listener_num", false, "LISTENER_NUM");
        public final static Property Size = new Property(6, String.class, "size", false, "SIZE");
        public final static Property Title = new Property(7, String.class, "title", false, "TITLE");
        public final static Property Cover_pic = new Property(8, String.class, "cover_pic", false, "COVER_PIC");
        public final static Property Time_num = new Property(9, Integer.class, "time_num", false, "TIME_NUM");
        public final static Property Local_connect = new Property(10, Integer.class, "local_connect", false, "LOCAL_CONNECT");
        public final static Property Domain = new Property(11, String.class, "domain", false, "DOMAIN");
        public final static Property Create_time = new Property(12, String.class, "create_time", false, "CREATE_TIME");
        public final static Property Comment_num = new Property(13, String.class, "comment_num", false, "COMMENT_NUM");
        public final static Property Program_id = new Property(14, String.class, "program_id", false, "PROGRAM_ID");
        public final static Property Small_pic = new Property(15, String.class, "small_pic", false, "SMALL_PIC");
        public final static Property Collect = new Property(16, String.class, "collect", false, "COLLECT");
        public final static Property Data = new Property(17, String.class, "data", false, "DATA");
        public final static Property Previous_id = new Property(18, String.class, "previous_id", false, "PREVIOUS_ID");
        public final static Property Next_id = new Property(19, String.class, "next_id", false, "NEXT_ID");
        public final static Property State = new Property(20, Integer.class, "state", false, "STATE");
        public final static Property Progress = new Property(21, Integer.class, "progress", false, "PROGRESS");
        public final static Property Path = new Property(22, String.class, "path", false, "PATH");
    };


    public ListenerDao(DaoConfig config) {
        super(config);
    }
    
    public ListenerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    */
/** Creates the underlying database table. *//*

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LISTENER\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "\"APPROVE_NUM\" TEXT," + // 1: approve_num
                "\"REMOTE_CONNECT\" TEXT," + // 2: remote_connect
                "\"IS_APPROVE\" INTEGER," + // 3: is_approve
                "\"BOOK_ID\" TEXT," + // 4: book_id
                "\"LISTENER_NUM\" TEXT," + // 5: listener_num
                "\"SIZE\" TEXT," + // 6: size
                "\"TITLE\" TEXT," + // 7: title
                "\"COVER_PIC\" TEXT," + // 8: cover_pic
                "\"TIME_NUM\" INTEGER," + // 9: time_num
                "\"LOCAL_CONNECT\" INTEGER," + // 10: local_connect
                "\"DOMAIN\" TEXT," + // 11: domain
                "\"CREATE_TIME\" TEXT," + // 12: create_time
                "\"COMMENT_NUM\" TEXT," + // 13: comment_num
                "\"PROGRAM_ID\" TEXT," + // 14: program_id
                "\"SMALL_PIC\" TEXT," + // 15: small_pic
                "\"COLLECT\" TEXT," + // 16: collect
                "\"DATA\" TEXT," + // 17: data
                "\"PREVIOUS_ID\" TEXT," + // 18: previous_id
                "\"NEXT_ID\" TEXT," + // 19: next_id
                "\"STATE\" INTEGER," + // 20: state
                "\"PROGRESS\" INTEGER," + // 21: progress
                "\"PATH\" TEXT);"); // 22: path
    }

    */
/** Drops the underlying database table. *//*

    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LISTENER\"";
        db.execSQL(sql);
    }

    */
/** @inheritdoc *//*

    @Override
    protected void bindValues(SQLiteStatement stmt, Listener entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String approve_num = entity.getApprove_num();
        if (approve_num != null) {
            stmt.bindString(2, approve_num);
        }
 
        String remote_connect = entity.getRemote_connect();
        if (remote_connect != null) {
            stmt.bindString(3, remote_connect);
        }
 
        Integer is_approve = entity.getIs_approve();
        if (is_approve != null) {
            stmt.bindLong(4, is_approve);
        }
 
        String book_id = entity.getBook_id();
        if (book_id != null) {
            stmt.bindString(5, book_id);
        }
 
        String listener_num = entity.getListener_num();
        if (listener_num != null) {
            stmt.bindString(6, listener_num);
        }
 
        String size = entity.getSize();
        if (size != null) {
            stmt.bindString(7, size);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(8, title);
        }
 
        String cover_pic = entity.getCover_pic();
        if (cover_pic != null) {
            stmt.bindString(9, cover_pic);
        }
 
        Integer time_num = entity.getTime_num();
        if (time_num != null) {
            stmt.bindLong(10, time_num);
        }
 
        Integer local_connect = entity.getLocal_connect();
        if (local_connect != null) {
            stmt.bindLong(11, local_connect);
        }
 
        String domain = entity.getDomain();
        if (domain != null) {
            stmt.bindString(12, domain);
        }
 
        String create_time = entity.getCreate_time();
        if (create_time != null) {
            stmt.bindString(13, create_time);
        }
 
        String comment_num = entity.getComment_num();
        if (comment_num != null) {
            stmt.bindString(14, comment_num);
        }
 
        String program_id = entity.getProgram_id();
        if (program_id != null) {
            stmt.bindString(15, program_id);
        }
 
        String small_pic = entity.getSmall_pic();
        if (small_pic != null) {
            stmt.bindString(16, small_pic);
        }
 
        String collect = entity.getCollect();
        if (collect != null) {
            stmt.bindString(17, collect);
        }
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(18, data);
        }
 
        String previous_id = entity.getPrevious_id();
        if (previous_id != null) {
            stmt.bindString(19, previous_id);
        }
 
        String next_id = entity.getNext_id();
        if (next_id != null) {
            stmt.bindString(20, next_id);
        }
 
        Integer state = entity.getState();
        if (state != null) {
            stmt.bindLong(21, state);
        }
 
        Integer progress = entity.getProgress();
        if (progress != null) {
            stmt.bindLong(22, progress);
        }
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(23, path);
        }
    }

    */
/** @inheritdoc *//*

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    */
/** @inheritdoc *//*

    @Override
    public Listener readEntity(Cursor cursor, int offset) {
        Listener entity = new Listener( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // approve_num
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // remote_connect
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // is_approve
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // book_id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // listener_num
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // size
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // title
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // cover_pic
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // time_num
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // local_connect
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // domain
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // create_time
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // comment_num
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // program_id
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // small_pic
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // collect
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // data
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // previous_id
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // next_id
            cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20), // state
            cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21), // progress
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22) // path
        );
        return entity;
    }
     
    */
/** @inheritdoc *//*

    @Override
    public void readEntity(Cursor cursor, Listener entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setApprove_num(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRemote_connect(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIs_approve(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setBook_id(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setListener_num(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSize(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTitle(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCover_pic(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setTime_num(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setLocal_connect(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setDomain(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreate_time(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setComment_num(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setProgram_id(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setSmall_pic(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCollect(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setData(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setPrevious_id(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setNext_id(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setState(cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20));
        entity.setProgress(cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21));
        entity.setPath(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
     }
    
    */
/** @inheritdoc *//*

    @Override
    protected String updateKeyAfterInsert(Listener entity, long rowId) {
        return entity.getId();
    }
    
    */
/** @inheritdoc *//*

    @Override
    public String getKey(Listener entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    */
/** @inheritdoc *//*

    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
*/
