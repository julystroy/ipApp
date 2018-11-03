package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-4.
 */
public class PackageList implements Parcelable{

    /*   "uid": 21,
                "buy_time": "2016-12-29 11:39:45",
                "item_id": 2, //道具类型
                "use_time": "2016-12-29 17:12:02", //使用时间
                "is_del": 0,
                "name": "七日经验卡",
                "icon": null, //道具图案
                "expire_time": "2017-01-01 17:12:02", //到期时间
                "description": "使用后可以在七日内经验翻倍",
                "id": 2, //我所购买的道具的id
                "is_use": 1 //已使用道具*/

    private  int uid;
    private  int item_id;
    private  int is_del;
    private  int id;
    private  int is_use;

    private String buy_time;
    private String use_time;
    private String name;
    private String icon;
    private String expire_time;
    private String description;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_use() {
        return is_use;
    }

    public void setIs_use(int is_use) {
        this.is_use = is_use;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getUse_time() {
        return use_time;
    }

    public void setUse_time(String use_time) {
        this.use_time = use_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PackageList{" +
                "uid=" + uid +
                ", item_id=" + item_id +
                ", is_del=" + is_del +
                ", id=" + id +
                ", is_use=" + is_use +
                ", buy_time='" + buy_time + '\'' +
                ", use_time='" + use_time + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public PackageList(){}
    protected PackageList(Parcel in) {
        uid = in.readInt();
        item_id = in.readInt();
        is_del = in.readInt();
        id = in.readInt();
        is_use = in.readInt();
        buy_time = in.readString();
        use_time = in.readString();
        name = in.readString();
        icon = in.readString();
        expire_time = in.readString();
        description = in.readString();
    }

    public static final Creator<PackageList> CREATOR = new Creator<PackageList>() {
        @Override
        public PackageList createFromParcel(Parcel in) {
            return new PackageList(in);
        }

        @Override
        public PackageList[] newArray(int size) {
            return new PackageList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeInt(item_id);
        dest.writeInt(is_del);
        dest.writeInt(id);
        dest.writeInt(is_use);
        dest.writeString(buy_time);
        dest.writeString(use_time);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(expire_time);
        dest.writeString(description);
    }
}
