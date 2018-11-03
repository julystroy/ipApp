package com.cartoon.data;

//import com.activeandroid.annotation.Column;
//import com.activeandroid.annotation.Table;
//import com.activeandroid.query.Select;
//import com.hy.changxian.database.BaseRecord;

//@Table(name = "package_info")
public class PackageInfoRecord /*extends BaseRecord*/ {
//    @Column(name = "appId")
    public long appId;
//    @Column(name = "packageName")
    public String packageName;

    public static PackageInfoRecord get(long appId) {
        PackageInfoRecord record = find(appId);
        if (record == null) {
            record = new PackageInfoRecord();
            record.appId = appId;
        }
        return record;
    }

    public static PackageInfoRecord find(long appId) {
//        return new Select().from(PackageInfoRecord.class).where("appId = ?", appId).executeSingle();
        return null;
    }

    public void saveRecord() {

    }
}
