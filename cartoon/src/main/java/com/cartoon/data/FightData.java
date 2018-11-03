package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 18-1-16.
 */
public class FightData implements Parcelable{
    /*"msg":""cc"正在攻击"乌漆嘛黑"",//内容
                "sectWarTime":"2018/01/16 10:37",//时间
                "end_time":"2018-01-16 12:37:50",
                "begin_time":"2018-01-16 10:37:50",
                isMyself
                "sect_war_id":3*/
    private String msg;
    private String sectWarTime;
    private String end_time;
    private String begin_time;
    private String sect_war_id;
    private boolean isMyself;

    public FightData() {
    }

    protected FightData(Parcel in) {
        msg = in.readString();
        sectWarTime = in.readString();
        end_time = in.readString();
        begin_time = in.readString();
        sect_war_id = in.readString();
        isMyself = in.readByte() != 0;
    }

    public static final Creator<FightData> CREATOR = new Creator<FightData>() {
        @Override
        public FightData createFromParcel(Parcel in) {
            return new FightData(in);
        }

        @Override
        public FightData[] newArray(int size) {
            return new FightData[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSectWarTime() {
        return sectWarTime;
    }

    public void setSectWarTime(String sectWarTime) {
        this.sectWarTime = sectWarTime;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getSect_war_id() {
        return sect_war_id;
    }

    public void setSect_war_id(String sect_war_id) {
        this.sect_war_id = sect_war_id;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }

    @Override
    public String toString() {
        return "FightData{" +
                "msg='" + msg + '\'' +
                ", sectWarTime='" + sectWarTime + '\'' +
                ", end_time='" + end_time + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", sect_war_id='" + sect_war_id + '\'' +
                ", isMyself=" + isMyself +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeString(sectWarTime);
        dest.writeString(end_time);
        dest.writeString(begin_time);
        dest.writeString(sect_war_id);
        dest.writeByte((byte) (isMyself ? 1 : 0));
    }
}
