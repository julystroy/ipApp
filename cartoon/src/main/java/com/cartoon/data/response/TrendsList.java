package com.cartoon.data.response;

import com.cartoon.data.TrendsData;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by cc on 17-8-24.
 */
public class TrendsList extends BaseResponse{
/* "push": false,  //是否有精彩活动，true= 有
        "pushType": -1, //业务模块（活动=0 征文=11）
		   "pushId": -1,  //条目id*/
    private List<TrendsData> list;

    private boolean push;
    private int pushType;
    private int pushId;

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public int getPushId() {
        return pushId;
    }

    public void setPushId(int pushId) {
        this.pushId = pushId;
    }

    public List<TrendsData> getList() {
        return list;
    }

    public void setList(List<TrendsData> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TrendsList{" +
                "list=" + list +
                ", push=" + push +
                ", pushType=" + pushType +
                ", pushId=" + pushId +
                '}';
    }
}
