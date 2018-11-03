package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-3.
 */
public class BuyItem implements Parcelable{
    /*"msg": "恭喜你，购买成功！请到我的背包中查看",
        "uid": "21",
        "buy_time": "2016-12-30",
        "item_id": "1",
        "is_del": 0,
        "is_use": 0*/
    private  String msg;
    private  String uid;
    private  String buy_time;
    private  String item_id;
    private int is_del;
    private int is_use;

    protected BuyItem(Parcel in) {
        uid = in.readString();
        msg = in.readString();
        buy_time = in.readString();
        item_id = in.readString();
        is_del = in.readInt();
        is_use = in.readInt();
    }
    public BuyItem(){}

    public static final Creator<BuyItem> CREATOR = new Creator<BuyItem>() {
        @Override
        public BuyItem createFromParcel(Parcel in) {
            return new BuyItem(in);
        }

        @Override
        public BuyItem[] newArray(int size) {
            return new BuyItem[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getIs_use() {
        return is_use;
    }

    public void setIs_use(int is_use) {
        this.is_use = is_use;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(msg);
        dest.writeString(buy_time);
        dest.writeString(item_id);
        dest.writeInt(is_del);
        dest.writeInt(is_use);
    }
}
