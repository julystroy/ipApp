package com.cartoon.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cc on 16-12-12.
 */
public class TaskBean implements Parcelable ,Comparable<TaskBean>{


    /*  "lv_type": 1,
                "buy_num": 3,
                "action_type": 1,
                "extra_stone": 0,
                "bonus": 60,
                "action_name": "评论和发帖",
                "limit": 1,
                "buy_stone": 2,
                "id": 1,
                "collect": 1, //用于安卓端排序（显示位置）
                "stone": 1,
                "status": "已完成"
                activityId:
               finishToday  :false  未完成
*/
    private int remain;
    private int count;
    private int limit;
    private int buy_limit;
    private int extraStone;
    private int id;
    private int lv_type;
    private int actionType;
    private String status;
    private String actionName;
    private int stone;
    private int bonus;
    private int    collect;
    private int    activityId;
    private boolean finishToday;

    public TaskBean() {
    }

    protected TaskBean(Parcel in) {
        remain = in.readInt();
        count = in.readInt();
        limit = in.readInt();
        buy_limit = in.readInt();
        extraStone = in.readInt();
        id = in.readInt();
        lv_type = in.readInt();
        actionType = in.readInt();
        status = in.readString();
        actionName = in.readString();
        stone = in.readInt();
        bonus = in.readInt();
        collect = in.readInt();
        activityId = in.readInt();
        finishToday = in.readByte() != 0;
    }

    public static final Creator<TaskBean> CREATOR = new Creator<TaskBean>() {
        @Override
        public TaskBean createFromParcel(Parcel in) {
            return new TaskBean(in);
        }

        @Override
        public TaskBean[] newArray(int size) {
            return new TaskBean[size];
        }
    };

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getBuy_limit() {
        return buy_limit;
    }

    public void setBuy_limit(int buy_limit) {
        this.buy_limit = buy_limit;
    }

    public int getExtraStone() {
        return extraStone;
    }

    public void setExtraStone(int extraStone) {
        this.extraStone = extraStone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLv_type() {
        return lv_type;
    }

    public void setLv_type(int lv_type) {
        this.lv_type = lv_type;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public boolean isFinishToday() {
        return finishToday;
    }

    public void setFinishToday(boolean finishToday) {
        this.finishToday = finishToday;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(remain);
        dest.writeInt(count);
        dest.writeInt(limit);
        dest.writeInt(buy_limit);
        dest.writeInt(extraStone);
        dest.writeInt(id);
        dest.writeInt(lv_type);
        dest.writeInt(actionType);
        dest.writeString(status);
        dest.writeString(actionName);
        dest.writeInt(stone);
        dest.writeInt(bonus);
        dest.writeInt(collect);
        dest.writeInt(activityId);
        dest.writeByte((byte) (finishToday ? 1 : 0));
    }

    @Override
    public int compareTo(TaskBean another) {


        return (another.getCollect()-this.collect );

    }

    public TaskBean( int collect) {
        this.collect = collect;
    }
}


