package com.cartoon.module.changxian.base;

//import com.activeandroid.Model;
//import com.activeandroid.annotation.Column;

public abstract class BaseRecord /*extends Model*/ {
//    @Column(name = "create_at")
    public long createAt;

//    @Column(name = "update_at")
    public long updateAt;

    public BaseRecord() {
        createAt = System.currentTimeMillis();
        updateAt = createAt;
    }

    public boolean isNewRecord() {
        return false/*getId() == null*/;
    }

    public Long saveRecord() {
//        updateAt = System.currentTimeMillis();
//        return save();
        return 0L;
    }

    public void delete() {

    }
}
