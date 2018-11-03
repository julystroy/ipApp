package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-10-20.
 */
public class AppPoint implements Parcelable {

    /**
     * rankId : 1
     * rankName : 掌门
     * canChoose : false
     */

    private int rankId;
    private String  rankName;
    private boolean canChoose;

    public AppPoint() {
    }

    protected AppPoint(Parcel in) {
        rankId = in.readInt();
        rankName = in.readString();
        canChoose = in.readByte() != 0;
    }

    public static final Creator<AppPoint> CREATOR = new Creator<AppPoint>() {
        @Override
        public AppPoint createFromParcel(Parcel in) {
            return new AppPoint(in);
        }

        @Override
        public AppPoint[] newArray(int size) {
            return new AppPoint[size];
        }
    };

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public boolean isCanChoose() {
        return canChoose;
    }

    public void setCanChoose(boolean canChoose) {
        this.canChoose = canChoose;
    }

    @Override
    public String toString() {
        return "AppPoint{" +
                "rankId=" + rankId +
                ", rankName='" + rankName + '\'' +
                ", canChoose=" + canChoose +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rankId);
        dest.writeString(rankName);
        dest.writeByte((byte) (canChoose ? 1 : 0));
    }
}
