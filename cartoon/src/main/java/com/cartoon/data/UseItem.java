package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-4.
 */
public class UseItem implements Parcelable{
    /*  "msg": "恭喜你，使用道具成功！", //消息
        "expire_time": "2017-01-28 17:35:32", //过期时间
        "id": 3  //这张道具的id*/

    private String msg;
    private String expire_time;
    private int  id;

    protected UseItem(Parcel in) {
        msg = in.readString();
        expire_time = in.readString();
        id = in.readInt();
    }

    public static final Creator<UseItem> CREATOR = new Creator<UseItem>() {
        @Override
        public UseItem createFromParcel(Parcel in) {
            return new UseItem(in);
        }

        @Override
        public UseItem[] newArray(int size) {
            return new UseItem[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UseItem() {

    }

    @Override
    public String toString() {
        return "UseItem{" +
                "msg='" + msg + '\'' +
                ", expire_time='" + expire_time + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeString(expire_time);
        dest.writeInt(id);
    }
}
