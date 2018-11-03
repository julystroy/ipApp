package com.cartoon.data.game;

import com.cartoon.data.CXAppInfo;

/**
 * Created by wusue on 17/4/12.
 */

public class PlayHistoryItemData extends CXAppInfo implements Cloneable {
    public int state;
    public long updatedAt;
    public long createdAt;
    public float userRating;
    public long duration;

    @Override
    public PlayHistoryItemData clone()  {
        PlayHistoryItemData test=null;
        try {
            test = (PlayHistoryItemData)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return test;
    }
}
