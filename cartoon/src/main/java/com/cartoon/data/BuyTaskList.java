package com.cartoon.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 17-1-6.
 */
public class BuyTaskList implements Parcelable {
    /**
     * buy_stone : 2
     * buy_limit : 1
     * canBuyNum : 1
     */

    private int buy_stone;
    private int buy_limit;
    private int canBuyNum;

    protected BuyTaskList(Parcel in) {
        buy_stone = in.readInt();
        buy_limit = in.readInt();
        canBuyNum = in.readInt();
    }

    public static final Creator<BuyTaskList> CREATOR = new Creator<BuyTaskList>() {
        @Override
        public BuyTaskList createFromParcel(Parcel in) {
            return new BuyTaskList(in);
        }

        @Override
        public BuyTaskList[] newArray(int size) {
            return new BuyTaskList[size];
        }
    };

    public int getBuy_stone() {
        return buy_stone;
    }

    public void setBuy_stone(int buy_stone) {
        this.buy_stone = buy_stone;
    }

    public int getBuy_limit() {
        return buy_limit;
    }

    public void setBuy_limit(int buy_limit) {
        this.buy_limit = buy_limit;
    }

    public int getCanBuyNum() {
        return canBuyNum;
    }

    public void setCanBuyNum(int canBuyNum) {
        this.canBuyNum = canBuyNum;
    }

    public BuyTaskList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(buy_stone);
        dest.writeInt(buy_limit);
        dest.writeInt(canBuyNum);
    }
}

