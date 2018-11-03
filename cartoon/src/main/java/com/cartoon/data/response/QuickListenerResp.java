package com.cartoon.data.response;

import com.cartoon.bean.QuickListener;
import com.cartoon.http.BaseResponse;

import java.util.List;

/**
 * Created by apple on 2016/10/10.
 */

public class QuickListenerResp extends BaseResponse {
    private int limit_num;//传入次参数，为分页显示

    @Override
    public int getTotalRow() {
        return totalRow;
    }

    @Override
    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    private int                 totalRow;
    private List<QuickListener> list;

    public int getLimit_num() {
        return limit_num;
    }

    public void setLimit_num(int limit_num) {
        this.limit_num = limit_num;
    }

    public List<QuickListener> getList() {
        return list;
    }

    public void setList(List<QuickListener> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListenerListResp{" +
                "limit_num=" + limit_num +
                ", list=" + list +
                '}';
    }
}
